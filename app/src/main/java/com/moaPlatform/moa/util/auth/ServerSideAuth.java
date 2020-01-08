package com.moaPlatform.moa.util.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.FingerprintFragment;
import com.moaPlatform.moa.auth.FingerprintLoginFragment;
import com.moaPlatform.moa.auth.sign_up.controller.LogInActivity;
import com.moaPlatform.moa.auth.sign_up.controller.LogInFingerActivity;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.MoaAuthResultCode;
import com.moaPlatform.moa.util.auth.model.RequestMoaAuth;
import com.moaPlatform.moa.util.auth.model.ResponseMoaAuth;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.manager.CompositeDisposableManager;
import com.moaPlatform.moa.util.singleton.App;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.moa.auth.userauth.android.api.MoaAuthHelper;
import org.moa.auth.userauth.android.api.MoaMember;
import org.moa.auth.userauth.client.api.MoaClientLogInLib;
import org.moa.auth.userauth.client.api.MoaClientMsgParser;
import org.moa.auth.userauth.client.api.MoaClientRegistLib;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ServerSideAuth extends BaseController {
    private RequestMoaAuth requestMoaAuth = new RequestMoaAuth();
    private MoaAuthHelper moaAuthHelper = MoaAuthHelper.getInstance();

    private ServerSideAuth() {
    }

    public static ServerSideAuth getInstance() {
        return Singleton.instance;
    }

    /**
     * 서버에서 비회원 계정 생성
     */
    void registerNonMemberPIN() {
        final int REGISTER_NON_MEMBER = 1;

        SharedPreferences sf = context.getSharedPreferences("haveiduser", MODE_PRIVATE);
        String sIdHave = sf.getString("memid", "");
        if (!TextUtils.isEmpty(sIdHave)) {
            Logger.e("bbgetCurrentMemberID" + sIdHave);
            requestMoaAuth.setPreUserID(sIdHave);
        }

        RetrofitClient.getInstance().getMoaBasicService()
                .registerNonMemberID(requestMoaAuth).enqueue(new Callback<ResponseMoaAuth>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMoaAuth> call, @NonNull Response<ResponseMoaAuth> response) {
                ResponseMoaAuth responseMoaAuth = response.body();
                if (responseMoaAuth == null || responseMoaAuth.userId == null || responseMoaAuth.getNonMemberId() == null)
                    return;
                moaAuthHelper.setBasePrimaryInfo(responseMoaAuth.userId);
                moaAuthHelper.setNonMemberPIN(responseMoaAuth.getNonMemberId());

                if (restApiResult != null)
                    restApiResult.onRestApiSuccess(CodeTypeManager.RestApi.REGISTER_NON_MEMBER, null);

                if (httpConnectionResult != null)
                    httpConnectionResult.onHttpConnectionSuccess(REGISTER_NON_MEMBER, null);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (restApiResult != null)
                    restApiResult.onRestApiFail(CodeTypeManager.RestApi.REGISTER_NON_MEMBER);
            }
        });
    }

    void doLoginNonMemberPIN(String nonMemberID) {
        final int LOGIN_SUCCESS = 2;

        requestMoaAuth.setNonMemberId(nonMemberID);
        requestMoaAuth.setUserId(moaAuthHelper.getBasePrimaryInfo());
        RetrofitClient.getInstance().getMoaBasicService()
                .doLoginNonMemberID(requestMoaAuth).enqueue(new Callback<ResponseMoaAuth>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMoaAuth> call, @NonNull Response<ResponseMoaAuth> response) {
                ResponseMoaAuth responseMoaAuth = response.body();
                if (responseMoaAuth == null)
                    return;
                if (responseMoaAuth.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    RetrofitClient.getInstance().hasReissuedAccessToken(responseMoaAuth);
                    Logger.d("비회원 로그인 성공");
                    if (httpConnectionResult != null)
                        httpConnectionResult.onHttpConnectionSuccess(LOGIN_SUCCESS, null);
                    if (restApiResult != null)
                        restApiResult.onRestApiSuccess(CodeTypeManager.RestApi.NON_MEMBER_LOGIN, null);
                } else
                    Logger.e("비회원 로그인 실패");
                if (restApiResult != null)
                    restApiResult.onRestApiFail(CodeTypeManager.RestApi.LOGIN_SUCCESS);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, context.getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    void registerMemberPIN(String pinID, String pinPW, String recommenderNumber) {
        requestMoaAuth.clearPIN();
        requestMoaAuth.setNonMemberId(moaAuthHelper.getCurrentMemberID());
        requestMoaAuth.setIdExistStr(MoaClientRegistLib.IdExistMsgGenProcess(pinID));
        requestMoaAuth.setEmail(pinID);
        requestMoaAuth.setUserId(moaAuthHelper.getBasePrimaryInfo());
        requestMoaAuth.setRecommenderNumber(recommenderNumber);
        RetrofitClient.getInstance().getMoaBasicService()
                .canRegisterMemberPin(requestMoaAuth).enqueue(new Callback<ResponseMoaAuth>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMoaAuth> call, @NonNull Response<ResponseMoaAuth> response) {
                ResponseMoaAuth responseMoaAuth = response.body();
                if (responseMoaAuth == null) {
                    return;
                }
                try {
                    String result = MoaClientMsgParser.MoaClientAuthMsgPacketParser(responseMoaAuth.getIdExistAckStr());
                    if (result.equals(MoaAuthResultCode.COMMON_ID_EXIST.getAuthCode())) {
                        Toast.makeText(context, context.getString(MoaAuthResultCode.COMMON_ID_EXIST.getStringId()), Toast.LENGTH_LONG).show();
                    } else {
                        doFinalRegisterMemberPin(pinID, pinPW);
                    }
                } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException e) {
                    Logger.d("[*] --- Error occurred : " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                Toast.makeText(context, context.getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doFinalRegisterMemberPin(String pinID, String pinPW) {
        requestMoaAuth.setPinRegReqMsgGenStr(moaAuthHelper.generatePINRegisterMessage(pinID, pinPW));
        requestMoaAuth.setUserId(moaAuthHelper.getBasePrimaryInfo());
        RetrofitClient.getInstance().getMoaBasicService()
                .registerMemberPin(requestMoaAuth).enqueue(new Callback<ResponseMoaAuth>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMoaAuth> call, @NonNull Response<ResponseMoaAuth> response) {
                ResponseMoaAuth responseMoaAuth = response.body();
                if (responseMoaAuth == null)
                    return;
                try {
                    String result = MoaClientMsgParser.MoaClientAuthMsgPacketParser(responseMoaAuth.getPinRegResStr());
                    if (result.equals(MoaAuthResultCode.REGIST_PIN_SUCCESS.getAuthCode())) {
                        moaAuthHelper.setControlInfoData(pinID, MoaMember.MEMBER_PIN);
                        Toast.makeText(context, context.getString(MoaAuthResultCode.REGIST_PIN_SUCCESS.getStringId()), Toast.LENGTH_LONG).show();
                        if (httpConnectionResult != null)
                            httpConnectionResult.onHttpConnectionSuccess(App.getInstance().SIGN_UP_SUCCESS, null);
                        if (restApiResult != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("pinID", pinID);
                            bundle.putString("pinPW", pinPW);
                            restApiResult.onRestApiSuccess(CodeTypeManager.RestApi.USER_SIGN_UP, bundle);
                        }
                    } else
                        Toast.makeText(context, context.getString(MoaAuthResultCode.REGIST_PIN_FAIL.getStringId()), Toast.LENGTH_LONG).show();
                } catch (InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException e) {
                    Logger.d("[*] --- Error occurred : " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                Toast.makeText(context, context.getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * ID/PW 로그인 유효성 검사
     *
     * @param pinID
     * @param pinPW
     */
    void doLoginMemberPIN(String pinID, String pinPW) {
        requestMoaAuth.clearPIN();
        requestMoaAuth.setEmail(pinID);
        requestMoaAuth.setPinLogInStartReqStr(MoaClientLogInLib.PinLogInStartRequestMsgGenProcess());
        requestMoaAuth.setUserId(moaAuthHelper.getBasePrimaryInfo());
        RetrofitClient.getInstance().getMoaBasicService()
                .startLoginMemberPin(requestMoaAuth).enqueue(new Callback<ResponseMoaAuth>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMoaAuth> call, @NonNull Response<ResponseMoaAuth> response) {
                ResponseMoaAuth responseMoaAuth = response.body();
                if (responseMoaAuth == null || responseMoaAuth.getPinLogInStartResStr() == null)
                    return;
                try {
                    String receivedNonceOTP = MoaClientMsgParser.MoaClientAuthMsgPacketParser(responseMoaAuth.getPinLogInStartResStr());
                    String pinLoginReqBase64 = moaAuthHelper.generatePINLoginRequestMessage(pinID, pinPW, receivedNonceOTP);
                    doFinalLoginMemberPin(pinID, pinLoginReqBase64);
                } catch (InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException e) {
                    Logger.d("[*] --- Error occurred : " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                Toast.makeText(context, context.getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doFinalLoginMemberPin(String pinID, String pinLoginReqBase64) {
        final int LOGIN_SUCCESS = 2;

        requestMoaAuth.setPinLogInStartReqStr("");
        requestMoaAuth.setPinLogInReqStr(pinLoginReqBase64);
        RetrofitClient.getInstance().getMoaBasicService()
                .doLoginMemberPin(requestMoaAuth).enqueue(new Callback<ResponseMoaAuth>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMoaAuth> call, @NonNull Response<ResponseMoaAuth> response) {
                ResponseMoaAuth responseMoaAuth = response.body();
                if (responseMoaAuth == null || responseMoaAuth.userId == null)
                    return;
                try {
                    String[] results = MoaClientMsgParser.MoaClientAuthMsgPacketParser(responseMoaAuth.getPinLoginResultStr()).split("\\$");
                    if (results[results.length - 1].equals(MoaAuthResultCode.LOGIN_PIN_SUCCESS.getAuthCode())) {
                        RetrofitClient.getInstance().hasReissuedAccessToken(responseMoaAuth);
                        moaAuthHelper.setBasePrimaryInfo(responseMoaAuth.userId);
                        moaAuthHelper.setControlInfoData(pinID, MoaMember.MEMBER_PIN);
                        Toast.makeText(context, context.getString(MoaAuthResultCode.LOGIN_PIN_SUCCESS.getStringId()), Toast.LENGTH_SHORT).show();
                        setPrefNo();
                        if (restApiResult != null) {
                            restApiResult.onRestApiSuccess(CodeTypeManager.RestApi.USER_LOGIN, null);
                        }
                        if (httpConnectionResult != null) {
                            httpConnectionResult.onHttpConnectionSuccess(LOGIN_SUCCESS, null);
                        }
                    } else {
//                        Toast.makeText(context, context.getString(R.string.msg_pin_login_fail), Toast.LENGTH_SHORT).show();
                        if (restApiResult != null)
                            restApiResult.onRestApiFail(CodeTypeManager.RestApi.LOGIN_SUCCESS);
                    }
                } catch (InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException e) {
                    if (restApiResult != null)
                        restApiResult.onRestApiFail(CodeTypeManager.RestApi.LOGIN_SUCCESS);
                    Logger.d("[*] --- Error occurred : " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                Toast.makeText(context, context.getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void checkRegisterFingerprint() {
        requestMoaAuth.clearFingerprint();
        String memberID = moaAuthHelper.getCurrentMemberID();
        requestMoaAuth.setEmail(memberID);
        requestMoaAuth.setFpRegStartReqStr(MoaClientRegistLib.FingerRegistStartRequestMsgGenProcess(memberID));
        requestMoaAuth.setUserId(moaAuthHelper.getBasePrimaryInfo());
        RetrofitClient.getInstance().getMoaBasicService()
                .canRegisterFingerprint(requestMoaAuth).enqueue(new Callback<ResponseMoaAuth>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMoaAuth> call, @NonNull Response<ResponseMoaAuth> response) {
                ResponseMoaAuth responseMoaAuth = response.body();
                if (responseMoaAuth == null || responseMoaAuth.getFpRegReqStartResStr() == null)
                    return;
                try {
                    String receivedFingerprintReqStartResStr = MoaClientMsgParser.MoaClientAuthMsgPacketParser(responseMoaAuth.getFpRegReqStartResStr());
                    StringTokenizer stringTokenizer = new StringTokenizer(receivedFingerprintReqStartResStr, "$");
                    boolean existID = stringTokenizer.nextToken().equals(MoaAuthResultCode.COMMON_ID_EXIST.getAuthCode());
                    stringTokenizer.nextToken();
                    String base64AuthToken = stringTokenizer.nextToken();
                    if (existID) {
                        FingerprintFragment fingerprintFragment = new FingerprintFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("paramType", "Register");
                        bundle.putString("base64AuthToken", base64AuthToken);
                        fingerprintFragment.setArguments(bundle);
                        fingerprintFragment.show(((LogInFingerActivity) context).getSupportFragmentManager(), FingerprintFragment.class.getSimpleName());
                    } else
                        Toast.makeText(context, context.getString(MoaAuthResultCode.REGIST_FINGER_AUTHTOKEN_EXIST.getStringId()), Toast.LENGTH_SHORT).show();

                } catch (InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException e) {
                    Logger.d("[*] --- Error occurred : " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                Toast.makeText(context, context.getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void registerFingerprint(String base64AuthToken) {
        String memberID = moaAuthHelper.getCurrentMemberID();
        Map<String, String> fingerprintRegisterData = new HashMap<>();
        fingerprintRegisterData.put("curve", "secp256r1");
        fingerprintRegisterData.put("suite", "SHA256withECDSA");
        fingerprintRegisterData.put("authToken", base64AuthToken);
        byte[] fingerprintSignBytes = moaAuthHelper.getFingerprintRegisterECDSASign(fingerprintRegisterData);
        byte[] fingerprintPublicKey = moaAuthHelper.getFingerprintPublicKey().getEncoded();
        requestMoaAuth.setFpRegStartReqStr("");
        requestMoaAuth.setFpRegReqStr(MoaClientRegistLib.FingerRegistRequestMsgGenProcess(memberID, fingerprintPublicKey, fingerprintSignBytes));
        RetrofitClient.getInstance().getMoaBasicService()
                .registerFingerprint(requestMoaAuth).enqueue(new Callback<ResponseMoaAuth>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMoaAuth> call, @NonNull Response<ResponseMoaAuth> response) {
                ResponseMoaAuth responseMoaAuth = response.body();
                if (responseMoaAuth == null || responseMoaAuth.getFpRegResStr() == null)
                    return;
                try {
                    String result = MoaClientMsgParser.MoaClientAuthMsgPacketParser(responseMoaAuth.getFpRegResStr());
                    if (result.equals(MoaAuthResultCode.REGIST_FINGER_SUCCESS.getAuthCode())) {
                        moaAuthHelper.setControlInfoData(memberID, MoaMember.MEMBER_FINGER);
                        Toast.makeText(context, context.getString(MoaAuthResultCode.REGIST_FINGER_SUCCESS.getStringId()), Toast.LENGTH_LONG).show();
//                        context.startActivity(new Intent(context, LogInActivity.class));
                        Intent i = new Intent(context, LogInActivity.class);
                        i.putExtra("finger", "yes");
                        setPrefYes();
                        context.startActivity(i);
                    } else
                        Toast.makeText(context, context.getString(MoaAuthResultCode.REGIST_FINGER_FAIL.getStringId()), Toast.LENGTH_LONG).show();

                } catch (InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException e) {
                    Logger.d("[*] --- Error occurred : " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                Toast.makeText(context, context.getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setPrefYes() {
        SharedPreferences sf = context.getSharedPreferences("havefinger", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();

        editor.putString("setfinger", "yes");
        editor.commit();
    }

    private void setPrefNo() {
        SharedPreferences sf = context.getSharedPreferences("havefinger", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();

        editor.putString("setfinger", "no");
        editor.commit();
    }

    public void checkLoginFingerprint() {
        requestMoaAuth.clearFingerprint();
        String memberID = moaAuthHelper.getMemberID();
        requestMoaAuth.setEmail(memberID);
        requestMoaAuth.setFpLogInStartReqStr(MoaClientLogInLib.FingerLogInStartRequestMsgGenProcess(memberID));
        requestMoaAuth.setUserId(moaAuthHelper.getBasePrimaryInfo());
        RetrofitClient.getInstance().getMoaBasicService()
                .canLoginFingerprint(requestMoaAuth).enqueue(new Callback<ResponseMoaAuth>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMoaAuth> call, @NonNull Response<ResponseMoaAuth> response) {
                ResponseMoaAuth responseMoaAuth = response.body();
                if (responseMoaAuth == null || responseMoaAuth.getFpLogInStartResStr() == null)
                    return;
                try {
                    String receivedFingerprintReqStartResStr = MoaClientMsgParser.MoaClientAuthMsgPacketParser(responseMoaAuth.getFpLogInStartResStr());
                    StringTokenizer stringTokenizer = new StringTokenizer(receivedFingerprintReqStartResStr, "$");
                    boolean existID = stringTokenizer.nextToken().equals(MoaAuthResultCode.COMMON_ID_EXIST.getAuthCode());
                    boolean existAuthToken = stringTokenizer.nextToken().equals(MoaAuthResultCode.REGIST_FINGER_AUTHTOKEN_EXIST.getAuthCode());
                    String nonceOTP = stringTokenizer.nextToken();
                    String base64AuthToken = stringTokenizer.nextToken();

                    if (existID && existAuthToken) {
                        FingerprintLoginFragment fingerprintLoginFragment = new FingerprintLoginFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("paramType", "Login");
                        bundle.putString("nonceOTP", nonceOTP);
                        bundle.putString("base64AuthToken", base64AuthToken);
                        fingerprintLoginFragment.setArguments(bundle);
                        fingerprintLoginFragment.show(((LogInActivity) context).getSupportFragmentManager(), FingerprintLoginFragment.class.getSimpleName());
                    } else
                        Toast.makeText(context, context.getString(MoaAuthResultCode.LOGIN_FINGER_FAIL.getStringId()), Toast.LENGTH_LONG).show();
                } catch (InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException e) {
                    Logger.d("[*] --- Error occurred : " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                Toast.makeText(context, context.getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void doLoginFingerprint(String nonceOTP, String base64AuthToken) {
        final int LOGIN_SUCCESS = 2;
        String savedAuthToken = moaAuthHelper.getAuthTokenData();
        if (!base64AuthToken.equals(savedAuthToken)) {
            Toast.makeText(context, "Auth Token이 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        String memberID = moaAuthHelper.getMemberID();
        Map<String, String> fingerprintLoginData = new HashMap<>();
        fingerprintLoginData.put("curve", "secp256r1");
        fingerprintLoginData.put("suite", "SHA256withECDSA");
        fingerprintLoginData.put("authToken", base64AuthToken);
        fingerprintLoginData.put("nonce", nonceOTP);
        byte[] fingerprintSignData = moaAuthHelper.getFingerprintLoginECDSASign(fingerprintLoginData);

        requestMoaAuth.setFpLogInStartReqStr("");
        requestMoaAuth.setFpLoginReqStr(MoaClientLogInLib.FingerLogInRequestMsgGenProcess(memberID, fingerprintSignData));
        RetrofitClient.getInstance().getMoaBasicService()
                .doLoginFingerprint(requestMoaAuth).enqueue(new Callback<ResponseMoaAuth>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMoaAuth> call, @NonNull Response<ResponseMoaAuth> response) {
                ResponseMoaAuth responseMoaAuth = response.body();
                if (responseMoaAuth == null || responseMoaAuth.getFpLoginResStr() == null || responseMoaAuth.userId == null)
                    return;
                try {
                    String result = MoaClientMsgParser.MoaClientAuthMsgPacketParser(responseMoaAuth.getFpLoginResStr());
                    if (result.equals(MoaAuthResultCode.LOGIN_FINGER_SUCCESS.getAuthCode())) {
                        setPrefYes();
                        RetrofitClient.getInstance().hasReissuedAccessToken(responseMoaAuth);
                        moaAuthHelper.setBasePrimaryInfo(responseMoaAuth.userId);
                        moaAuthHelper.setControlInfoData(moaAuthHelper.getMemberID(), MoaMember.MEMBER_FINGER);
                        Toast.makeText(context, context.getString(MoaAuthResultCode.LOGIN_FINGER_SUCCESS.getStringId()), Toast.LENGTH_SHORT).show();

                        if (restApiResult != null) {
                            restApiResult.onRestApiSuccess(CodeTypeManager.RestApi.FINGER_LOGIN, null);
                        }
                        if (httpConnectionResult != null) {
                            httpConnectionResult.onHttpConnectionSuccess(LOGIN_SUCCESS, null);
                        }
                    } else
                        Toast.makeText(context, context.getString(MoaAuthResultCode.LOGIN_FINGER_FAIL.getStringId()), Toast.LENGTH_SHORT).show();
                } catch (InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException e) {
                    Logger.d("[*] --- Error occurred : " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                Toast.makeText(context, context.getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    void doLogout() {
        requestMoaAuth.clearPIN();
        requestMoaAuth.clearFingerprint();
        requestMoaAuth.setUserId(moaAuthHelper.getBasePrimaryInfo());
        RetrofitClient.getInstance().getMoaBasicService()
                .doLogout(requestMoaAuth).enqueue(new Callback<ResponseMoaAuth>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMoaAuth> call, @NonNull Response<ResponseMoaAuth> response) {
                ResponseMoaAuth responseMoaAuth = response.body();
                if (responseMoaAuth == null)
                    return;
                if (responseMoaAuth.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    Toast.makeText(context, context.getString(R.string.msg_logout_complete), Toast.LENGTH_LONG).show();
                    moaAuthHelper.removeControlInfo();
                    moaAuthHelper.setAutoLoginInfo(null);
                    if (restApiResult != null)
                        restApiResult.onRestApiSuccess(CodeTypeManager.RestApi.USER_LOGOUT, null);
                } else {
                    Toast.makeText(context, context.getString(R.string.msg_logout_fail), Toast.LENGTH_LONG).show();
                    if (restApiResult != null)
                        restApiResult.onRestApiFail(CodeTypeManager.RestApi.USER_LOGOUT);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                Toast.makeText(context, context.getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    void resetPin(String id, String newPw) {
        requestMoaAuth.clearPIN();
        requestMoaAuth.setUserId(moaAuthHelper.getBasePrimaryInfo());
        try {
            requestMoaAuth.setPswReSetReqMsgGenResultStr(moaAuthHelper.generatePINResetRequestMessage(id, newPw));
        } catch (Exception e) {
            Logger.d("PswReSetRequestMsgGenProcess failed!");
        }
        RetrofitClient.getInstance().getMoaBasicService()
                .resetPin(requestMoaAuth).enqueue(new Callback<ResponseMoaAuth>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMoaAuth> call, @NonNull Response<ResponseMoaAuth> response) {
                ResponseMoaAuth responseMoaAuth = response.body();
                if (responseMoaAuth == null || responseMoaAuth.getPswReSetResMsgGenResultStr() == null)
                    return;
                try {
                    String result = MoaClientMsgParser.MoaClientAuthMsgPacketParser(responseMoaAuth.getPswReSetResMsgGenResultStr());
                    if (result.equals(MoaAuthResultCode.RESET_PW_SUCCESS.getAuthCode())) {
                        Toast.makeText(context, context.getString(MoaAuthResultCode.RESET_PW_SUCCESS.getStringId()), Toast.LENGTH_LONG).show();
                        if (restApiResult != null)
                            restApiResult.onRestApiSuccess(CodeTypeManager.RestApi.PASSWORD_CHANGE_SUCCESS, null);
                    } else {
                        Toast.makeText(context, context.getString(MoaAuthResultCode.RESET_PW_FAIL.getStringId()), Toast.LENGTH_LONG).show();
                    }
                } catch (InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException e) {
                    throw new RuntimeException("[*] --- Error occurred : ", e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                Toast.makeText(context, context.getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    void changePin(String id, String pw, String newPw) {
        requestMoaAuth.clearPIN();
        requestMoaAuth.setUserId(moaAuthHelper.getBasePrimaryInfo());
        try {
            requestMoaAuth.setPswChangeReqMsgGenResultStr(moaAuthHelper.generatePINChangeRequestMessage(id, pw, newPw));
        } catch (Exception e) {
            Logger.d("PswReSetRequestMsgGenProcess failed!");
        }
        RetrofitClient.getInstance().getMoaService().changePin(requestMoaAuth).enqueue(new Callback<ResponseMoaAuth>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMoaAuth> call, @NonNull Response<ResponseMoaAuth> response) {
                ResponseMoaAuth responseMoaAuth = response.body();
                if (responseMoaAuth == null || responseMoaAuth.getPswChangeResMsgGenResultStr() == null)
                    return;
                try {
                    String result = MoaClientMsgParser.MoaClientAuthMsgPacketParser(responseMoaAuth.getPswChangeResMsgGenResultStr());
                    StringTokenizer st = new StringTokenizer(result, "$");
                    if (!st.hasMoreTokens())
                        return;
                    if (st.nextToken().equals(MoaAuthResultCode.CHANGE_PW_CURRENT_PW_VERIFY.getAuthCode()) &&
                            st.nextToken().equals(MoaAuthResultCode.CHANGE_PW_SUCCESS.getAuthCode())) {
                        Toast.makeText(context, context.getString(MoaAuthResultCode.CHANGE_PW_SUCCESS.getStringId()), Toast.LENGTH_LONG).show();
                        App.getInstance().userPw = newPw;
                        moaAuthHelper.setAutoLoginInfo(newPw);
                        if (restApiResult != null) {
                            restApiResult.onRestApiSuccess(CodeTypeManager.RestApi.PASSWORD_CHANGE_SUCCESS, null);
                        }
                    } else {
                        Toast.makeText(context, context.getString(MoaAuthResultCode.CHANGE_PW_FAIL.getStringId()), Toast.LENGTH_LONG).show();
                    }
                } catch (InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException e) {
                    Logger.d("[*] --- Error occurred : " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                Toast.makeText(context, context.getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    void unregisterMember(String id) {
        requestMoaAuth.clearPIN();
        requestMoaAuth.clearFingerprint();
        requestMoaAuth.setUserId(MoaAuthHelper.getInstance().getBasePrimaryInfo());
        requestMoaAuth.setUserUnREgistReqMsgGenResultStr(
                MoaClientLogInLib.UserUnRegistRequestMsgGenProcess(id));

        CompositeDisposableManager.getInstance().add(
                RetrofitClient.getInstance().getMoaService().unregisterMember(requestMoaAuth)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.body() == null) {
                                Toast.makeText(
                                        context,
                                        context.getString(MoaAuthResultCode.WITHDRAWAL_FAIL.getStringId()),
                                        Toast.LENGTH_SHORT
                                ).show();
                                return;
                            }
                            if (response.body().getUserUnRegistReqParserResultStr()
                                    .equals(MoaAuthResultCode.WITHDRAWAL_SUCCESS.getAuthCode())) {
                                Toast.makeText(
                                        context,
                                        context.getString(MoaAuthResultCode.WITHDRAWAL_SUCCESS.getStringId()),
                                        Toast.LENGTH_SHORT
                                ).show();

                                if (restApiResult != null) {
                                    restApiResult.onRestApiSuccess(
                                            CodeTypeManager.RestApi.MEMBERSHIP_WITHDRAWAL_SUCCESS,
                                            null
                                    );
                                }
                            } else {
                                Toast.makeText(
                                        context,
                                        context.getString(MoaAuthResultCode.WITHDRAWAL_FAIL.getStringId()),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }, err -> {
                            Logger.i(err.getMessage());
                            Toast.makeText(
                                    context,
                                    context.getString(R.string.common_toast_msg_connection_fail),
                                    Toast.LENGTH_LONG
                            ).show();
                        })
        );
    }

    private static class Singleton {
        @SuppressLint("StaticFieldLeak")
        private static ServerSideAuth instance = new ServerSideAuth();
    }
}