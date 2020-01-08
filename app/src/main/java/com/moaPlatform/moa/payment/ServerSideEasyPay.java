package com.moaPlatform.moa.payment;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.moaPlatform.moa.side_menu.order.detail.model.ReqOrderDetail;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CompositeDisposableManager;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.moa.auth.userauth.android.api.MoaAuthHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ServerSideEasyPay {
    private PaymentResultsReceiver receiver;
    private RequestCardData requestCardData = new RequestCardData();

    private ServerSideEasyPay() {
        requestCardData.setUserId(MoaAuthHelper.getInstance().getBasePrimaryInfo());
    }

    public static ServerSideEasyPay getInstance() {
        return Singleton.instance;
    }

    public void init(PaymentResultsReceiver receiver) {
        this.receiver = receiver;
    }

    void setEasyPayData(String paymentOption, String orderId) {
        requestCardData.setOrderId(orderId);
        RetrofitClient.getInstance().getMoaService().setEasyPayData(requestCardData).enqueue(new Callback<ResponseCardData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCardData> call, @NonNull Response<ResponseCardData> response) {
                ResponseCardData responseCardData = response.body();
                if (responseCardData == null || receiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseCardData)) {
                    setEasyPayData(paymentOption, orderId);
                    return;
                }
                if (responseCardData.getResultValue().equals(ServerSideMsg.SUCCESS)) {
                    String commonBillingJsonData = generateCommonBillingData(responseCardData.getCommonBillingData(), paymentOption);
                    receiver.onReadyJsonData(commonBillingJsonData);
                } else {
                    String msg = responseCardData.getResultMsg();
                    if (msg == null)
                        msg = "";
                    receiver.onFailPayment(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCardData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                if (receiver != null)
                    receiver.onFailPayment(t.getMessage());
            }
        });
    }

    void initOrderIfCanceled(String orderId) {
        if (orderId == null || orderId.length() == 0) {
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

    private String generateCommonBillingData(CommonBillingData commonBillingData, String paymentOption) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sp_mall_id", commonBillingData.getMallId());
            jsonObject.put("sp_mall_nm", commonBillingData.getMallNm());
            jsonObject.put("sp_order_no", commonBillingData.getOrderNo());
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
            jsonObject.put("sp_pay_type", paymentOption);
        } catch (JSONException e) {
            throw new RuntimeException("Json Exception : " + e.getMessage());
        }
        return jsonObject.toString();
    }

    private static final class Singleton {
        @SuppressLint("StaticFieldLeak")
        private static final ServerSideEasyPay instance = new ServerSideEasyPay();
    }
}
