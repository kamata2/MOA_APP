package com.moaPlatform.moa.payment;

import android.content.Context;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moaPlatform.moa.side_menu.order.detail.model.ReqOrderDetail;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CompositeDisposableManager;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerSideMoaPayController extends BaseController {
    private PaymentResultsReceiver payResultReceiver;
    private RequestCardData requestCardData = new RequestCardData();

    public ServerSideMoaPayController(Context context) {
        super(context);
        requestCardData.setUserId(MoaAuthHelper.getInstance().getBasePrimaryInfo());
        requestCardData.setMemberId(MoaAuthHelper.getInstance().getBasePrimaryInfo());
    }

    /**
     * 로그인이 수행되면 갱신처리
     * 주의 : 갱신처리를 하지 않으면 카드리스트를 가져오지 못하는 문제가 발생한다.
     */
    public void initUserIdAndMemberId(){
        if(requestCardData != null){
            requestCardData.setUserId(MoaAuthHelper.getInstance().getBasePrimaryInfo());
            requestCardData.setMemberId(MoaAuthHelper.getInstance().getBasePrimaryInfo());
        }
    }

    public void init(PaymentResultsReceiver paymentResult) {
        this.payResultReceiver = paymentResult;
    }

    /**
     * 카드를 등록한다. (모아페이 전용)
     *
     * <p>Logic</p>
     * 1. 인증 서버에 카드 등록 시 필수 데이터 요청 (Card Token, Return URL)
     * - Card Token : 인증 서버에서 임시 발급 (카드 등록 완료 시, 등록된 카드 데이터와 Pair 시킴)
     * - Return URL : 카드 등록 시 결과를 알려주는 URL
     * 2. Card Token과 Return URL을 B2C 서버에 세팅
     *
     * @param payPassword 모아페이 비밀번호
     * @param cardNick    카드 닉네임
     */
    //카드 등록 준비
    void cardRegistrationPreparation(String payPassword, String cardNick) {
        initRequestCardData();

        requestCardData.setActionType("CardRegist");
        requestCardData.setStep("1");
        requestCardData.setSecretKey(getHashedPassword(payPassword));
        requestCardData.setCardNick(cardNick);

        RetrofitClient.getInstance().getMoaService().requestRequiredCardRegisterData(requestCardData).enqueue(new Callback<ResponseCardData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCardData> call, @NonNull Response<ResponseCardData> response) {
                ResponseCardData responseCardData = response.body();
                if (responseCardData == null || payResultReceiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseCardData)) {
                    cardRegistrationPreparation(payPassword, cardNick);
                    return;
                }
                if (responseCardData.getResult().equals(ServerSideMsg.SUCCESS_MOAPAY)) {
                    String cardToken = responseCardData.getToken();
                    String returnUrl = responseCardData.getReturnURL();
                    setCardRegistrationPreparationData(cardToken, returnUrl);
                } else {
                    String msg = responseCardData.getResultMsg();
                    if (msg == null)
                        msg = "";
                    payResultReceiver.onFailPayment(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCardData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                if (payResultReceiver != null)
                    payResultReceiver.onFailPayment(t.getMessage());
            }
        });
    }

    private String getHashedPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
            messageDigest.update(password.getBytes());
            return Base64.encodeToString(messageDigest.digest(), Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash into password\n", e);
        }
    }

    /**
     * B2C 서버에 카드 등록 시 필요한 데이터 (Card Token, Return URL) 및 공통 결제 데이터를 세팅한다.
     *
     * <p>상세</p>
     * KICC 카드 등록 페이지 (.jsp)에 해당 데이터들을 세팅한다.
     *
     * @param cardToken 카드 토큰
     * @param returnUrl 카드 등록 시 결과를 알려주는 URL
     */
    //카드 등록 준비 데이터 set
    private void setCardRegistrationPreparationData(String cardToken, String returnUrl) {
        RetrofitClient.getInstance().getMoaService().requestInitialCardRegisterData(requestCardData).enqueue(new Callback<ResponseCardData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCardData> call, @NonNull Response<ResponseCardData> response) {
                ResponseCardData responseCardData = response.body();
                if (responseCardData == null || payResultReceiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseCardData)) {
                    setCardRegistrationPreparationData(cardToken, returnUrl);
                    return;
                }
                if (responseCardData.getResultValue().equals(ServerSideMsg.SUCCESS)) {
                    String cardRegisterValue = getCardRegisterValue(responseCardData.getCommonBillingData(), cardToken, returnUrl);
                    payResultReceiver.onReadyJsonData(cardRegisterValue);
                } else {
                    String msg = responseCardData.getResultMsg();
                    if (msg == null)
                        msg = "";
                    payResultReceiver.onFailPayment(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCardData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                if (payResultReceiver != null)
                    payResultReceiver.onFailPayment(t.getMessage());
            }
        });
    }

    private String getCardRegisterValue(CommonBillingData commonBillingData, String token, String returnUrl) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sp_mall_id", commonBillingData.getMallId());
            jsonObject.put("sp_mall_nm", commonBillingData.getMallNm());
            jsonObject.put("sp_order_no", token);
            jsonObject.put("sp_currency", commonBillingData.getCurrency());
            jsonObject.put("sp_product_nm", commonBillingData.getProductNm());
            jsonObject.put("sp_product_amt", commonBillingData.getProductAmt());
            jsonObject.put("sp_lang_flag", commonBillingData.getLangFlag());
            jsonObject.put("sp_charset", commonBillingData.getCharset());
            jsonObject.put("sp_user_id", commonBillingData.getUserId());
            jsonObject.put("sp_memb_user_no", commonBillingData.getMembUserNo());
            jsonObject.put("sp_user_nm", commonBillingData.getUserNm());
            jsonObject.put("sp_user_mail", commonBillingData.getUserMail());
            jsonObject.put("sp_user_phone1", commonBillingData.getUserPhone1());
            jsonObject.put("sp_user_phone2", commonBillingData.getUserPhone2());
            jsonObject.put("sp_user_addr", commonBillingData.getUserAddr());
            jsonObject.put("sp_product_type", commonBillingData.getProductType());
            jsonObject.put("sp_product_expr", commonBillingData.getProductExpr());
            jsonObject.put("sp_app_scheme", commonBillingData.getAppScheme());
            jsonObject.put("sp_return_url", returnUrl);
        } catch (JSONException e) {
            throw new RuntimeException("Json Exception : " + e.getMessage());
        }
        return jsonObject.toString();
    }

    /**
     * 카드 리스트를 요청한다.
     */
    public void getCardList() {

        initRequestCardData();
        requestCardData.setActionType("SelectCardList");
        requestCardData.setStep("1");

        RetrofitClient.getInstance().getMoaService().showCardList(requestCardData).enqueue(new Callback<ResponseCardData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCardData> call, @NonNull Response<ResponseCardData> response) {
                ResponseCardData responseCardData = response.body();
                if (responseCardData == null || payResultReceiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseCardData)) {
                    getCardList();
                    return;
                }
                if (responseCardData.getResult().equals(ServerSideMsg.SUCCESS_MOAPAY)) {
                    payResultReceiver.onLoadCompleteCardList(getJsonStringToCardListItem(responseCardData.getCardList()));
                } else {
                    String msg = responseCardData.getResultMsg();
                    if (msg == null)
                        msg = "";
                    payResultReceiver.onFailPayment(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCardData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                if (payResultReceiver != null)
                    payResultReceiver.onFailPayment(t.getMessage());
            }
        });
    }

    private List<CardListItem> getJsonStringToCardListItem(String cardListJson) {
        return new Gson().fromJson(cardListJson, new TypeToken<List<CardListItem>>() {
        }.getType());
    }

    public void payCard(Map<String, String> payData) {
        initRequestCardData();
        requestCardData.setActionType("RequestPay");
        requestCardData.setStep("1");
        requestCardData.setPayKind("CARD");
        requestCardData.setOrderId(payData.get("orderID"));

        RetrofitClient.getInstance().getMoaService().payCard(requestCardData).enqueue(new Callback<ResponseCardData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCardData> call, @NonNull Response<ResponseCardData> response) {
                ResponseCardData responseCardData = response.body();
                if (responseCardData == null || payResultReceiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseCardData)) {
                    payCard(payData);
                    return;
                }
                if (responseCardData.getResult().equals(ServerSideMsg.SUCCESS_MOAPAY)) {
                    payData.put("nonce", responseCardData.getNonce());
                    confirmPayCard(payData);
                } else {
                    String msg = responseCardData.getResultMsg();
                    if (msg == null)
                        msg = "";
                    payResultReceiver.onFailPayment(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCardData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                if (payResultReceiver != null)
                    payResultReceiver.onFailPayment(t.getMessage());
            }
        });
    }

    private void confirmPayCard(Map<String, String> againCardPayData) {
        requestCardData.setStep("2");
        String targetData = againCardPayData.get("nonce") + MoaAuthHelper.getInstance().getBasePrimaryInfo() + againCardPayData.get("orderID");
        setSecretAndSecureKey(targetData, Objects.requireNonNull(againCardPayData.get("payPw")));
        requestCardData.setCardToken(againCardPayData.get("token"));

        RetrofitClient.getInstance().getMoaService().payCard(requestCardData).enqueue(new Callback<ResponseCardData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCardData> call, @NonNull Response<ResponseCardData> response) {
                ResponseCardData responseCardData = response.body();
                if (responseCardData == null || payResultReceiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseCardData)) {
                    confirmPayCard(againCardPayData);
                    return;
                }
                String result = responseCardData.getResult();
                if (result == null)
                    return;
                if (result.equals(ServerSideMsg.SUCCESS_MOAPAY)) {
                    payResultReceiver.onSuccessPayment();
                } else if (result.equals(ServerSideMsg.FAIL_MOAPAY_NOT_MATCH_PW)) {
                    payResultReceiver.onNotMatchedPw();
                } else {
                    String msg = responseCardData.getResultMsg();
                    if (msg == null)
                        msg = "";
                    payResultReceiver.onFailPayment(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCardData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                if (payResultReceiver != null)
                    payResultReceiver.onFailPayment(t.getMessage());
            }
        });
    }

    private void setSecretAndSecureKey(String targetData, String password) {
        if (targetData == null || password == null)
            return;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
            byte[] payPasswordHash = messageDigest.digest(password.getBytes());
            byte[] secureKey = new byte[payPasswordHash.length + targetData.getBytes().length];
            System.arraycopy(payPasswordHash, 0, secureKey, 0, payPasswordHash.length);
            System.arraycopy(targetData.getBytes(), 0, secureKey, payPasswordHash.length, targetData.getBytes().length);
            byte[] secureKeyHash = messageDigest.digest(secureKey);
            requestCardData.setSecretKey(Base64.encodeToString(payPasswordHash, Base64.NO_WRAP));
            requestCardData.setSecureKey(Base64.encodeToString(secureKeyHash, Base64.NO_WRAP));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate secure key\n", e);
        }
    }

    void refundPay(String orderID) {
        initRequestCardData();
        requestCardData.setOrderId(orderID);

        RetrofitClient.getInstance().getMoaService().refundPay(requestCardData).enqueue(new Callback<ResponseCardData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCardData> call, @NonNull Response<ResponseCardData> response) {
                ResponseCardData responseCardData = response.body();
                if (responseCardData == null || payResultReceiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseCardData)) {
                    refundPay(orderID);
                    return;
                }
                if (responseCardData.getResultValue().equals(ServerSideMsg.SUCCESS)) {
                    payResultReceiver.onSuccessPayment();
                } else {
                    String msg = responseCardData.getResultMsg();
                    if (msg == null)
                        msg = "";
                    payResultReceiver.onFailPayment(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCardData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                if (payResultReceiver != null)
                    payResultReceiver.onFailPayment(t.getMessage());
            }
        });
    }

    /**
     * 카드 삭제를 수행한다.
     * @param cardToken
     */
    public void deleteOneCard(String cardToken) {
        initRequestCardData();
        requestCardData.setActionType("DeleteCardOne");
        requestCardData.setStep("1");
        requestCardData.setCardToken(cardToken);

        RetrofitClient.getInstance().getMoaService().deleteOneCard(requestCardData).enqueue(new Callback<ResponseCardData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCardData> call, @NonNull Response<ResponseCardData> response) {
                ResponseCardData responseCardData = response.body();
                if (responseCardData == null || payResultReceiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseCardData)) {
                    deleteOneCard(cardToken);
                    return;
                }
                if (responseCardData.getResult().equals(ServerSideMsg.SUCCESS_MOAPAY)) {
                    onRetrofitSuccess(0, responseCardData);
                } else {
                    String msg = responseCardData.getResultMsg();
                    if (msg == null)
                        msg = "";
                    onRetrofitFail(0, msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCardData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                if (payResultReceiver != null)
                    payResultReceiver.onFailPayment(t.getMessage());
            }
        });
    }

    void changeCardNick(String cardToken, String cardNick) {
        initRequestCardData();
        requestCardData.setActionType("ChangeCardNickname");
        requestCardData.setStep("1");
        requestCardData.setCardToken(cardToken);
        requestCardData.setCardNick(cardNick);

        RetrofitClient.getInstance().getMoaService().changeCardNick(requestCardData).enqueue(new Callback<ResponseCardData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCardData> call, @NonNull Response<ResponseCardData> response) {
                ResponseCardData responseCardData = response.body();
                if (responseCardData == null || payResultReceiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseCardData)) {
                    changeCardNick(cardToken, cardNick);
                    return;
                }
                if (responseCardData.getResult().equals(ServerSideMsg.SUCCESS_MOAPAY)) {
                    getCardList();
                } else {
                    String msg = responseCardData.getResultMsg();
                    if (msg == null)
                        msg = "";
                    payResultReceiver.onFailPayment(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCardData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                if (payResultReceiver != null)
                    payResultReceiver.onFailPayment(t.getMessage());
            }
        });
    }

    public void changePayPsw(String currentPsw, String newPsw) {
        initRequestCardData();
        requestCardData.setActionType("ChangePwdSimplePay");
        requestCardData.setStep("1");
        requestCardData.setSecretKeyOrg(getHashedPassword(currentPsw));
        requestCardData.setSecretKeyNew(getHashedPassword(newPsw));

        RetrofitClient.getInstance().getMoaService().changePayPsw(requestCardData).enqueue(new Callback<ResponseCardData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCardData> call, @NonNull Response<ResponseCardData> response) {
                ResponseCardData responseCardData = response.body();
                if (responseCardData == null || payResultReceiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseCardData)) {
                    changePayPsw(currentPsw, newPsw);
                    return;
                }
                if (responseCardData.getResult().equals(ServerSideMsg.SUCCESS_MOAPAY)) {
                    payResultReceiver.onSuccessPayment();
                } else {
                    String msg = responseCardData.getResultMsg();
                    if (msg == null)
                        msg = "";
                    payResultReceiver.onFailPayment(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCardData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                if (payResultReceiver != null)
                    payResultReceiver.onFailPayment(t.getMessage());
            }
        });
    }

    public void initOrderIfCanceled(String orderId) {
        if (orderId == null) {
            return;
        }
        ReqOrderDetail reqOrderDetail = new ReqOrderDetail();
        reqOrderDetail.orderId = orderId;

        CompositeDisposableManager.getInstance().add(
                RetrofitClient.getInstance().getMoaService().requestDeleteOrder(reqOrderDetail)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        );
    }


    void initializePayPsw(String initPsw) {
        initRequestCardData();
        requestCardData.setActionType("InitializePwdSimplePay");
        requestCardData.setStep("1");
        requestCardData.setSecretKeyNew(getHashedPassword(initPsw));

        RetrofitClient.getInstance().getMoaService().initPayPsw(requestCardData).enqueue(new Callback<ResponseCardData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCardData> call, @NonNull Response<ResponseCardData> response) {
                ResponseCardData responseCardData = response.body();
                if (responseCardData == null || payResultReceiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseCardData)) {
                    initializePayPsw(initPsw);
                    return;
                }
                if (responseCardData.getResult().equals(ServerSideMsg.SUCCESS_MOAPAY)) {
                    payResultReceiver.onForwardInitPswNonce(responseCardData.getNonce());
                } else {
                    String msg = responseCardData.getResultMsg();
                    if (msg == null)
                        msg = "";
                    payResultReceiver.onFailPayment(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCardData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                if (payResultReceiver != null)
                    payResultReceiver.onFailPayment(t.getMessage());
            }
        });
    }

    private void initRequestCardData() {
        if (requestCardData != null) {
            requestCardData.setActionType("");
            requestCardData.setSecretKey("");
            requestCardData.setStep("");
            requestCardData.setCardNick("");
            requestCardData.setPayKind("");
            requestCardData.setOrderId("");
            requestCardData.setSecureKey("");
            requestCardData.setCardToken("");
        }
    }
}
