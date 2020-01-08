package com.moaPlatform.moa.auth.sign_up.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.MoaAuthResultCode;
import com.moaPlatform.moa.util.auth.ServerSideAuth;
import com.moaPlatform.moa.util.auth.UserUseHelper;
import com.moaPlatform.moa.util.auth.model.RequestMoaAuth;
import com.moaPlatform.moa.util.auth.model.ResponseMoaAuth;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.interfaces.ClassConnectInterface;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.moa.auth.userauth.android.api.MoaAuthHelper;
import org.moa.auth.userauth.client.api.MoaClientMsgParser;
import org.moa.auth.userauth.client.api.MoaClientRegistLib;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInFingerBeforeActivity extends AppCompatActivity implements ClassConnectInterface, View.OnClickListener {
    private EditText inputEmail, inputPassWord;
    private boolean isAutoLogin = false;
    private UserUseHelper userUseHelper;
    private ImageButton btnBack;
    private TextView tvOtherLogin;
    private LinearLayout lIdView, lFingerView, lHeightView, lBtnView, lSettingView;
    private LinearLayout lIdLogin, lFingerLogin;
    private LinearLayout memberJoin;
    private View failMesage;
    private RequestMoaAuth requestMoaAuth = new RequestMoaAuth();
    private MoaAuthHelper moaAuthHelper = MoaAuthHelper.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login_finger_before);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userUseHelper = new UserUseHelper(this);
        userUseHelper.setClassConnectInterface(this);
        ServerSideAuth.getInstance().setContext(this);
    }

    private void initView() {
        inputEmail = findViewById(R.id.loginemail);
        inputPassWord = findViewById(R.id.loginpassword);
        Button loginBtn = findViewById(R.id.loginbtn);

        failMesage = findViewById(R.id.fingerLoginFail);
        failMesage.setVisibility(View.GONE);
        memberJoin = findViewById(R.id.llMainLoginJoin);
        memberJoin.setOnClickListener(v -> {
            Intent intent = new Intent(LogInFingerBeforeActivity.this, SignUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, MoaConstants.REQUEST_USER_JOIN);
        });
//        CheckBox checkLogin = findViewById(R.id.autocheckbox);
//        checkLogin.setOnCheckedChangeListener((v, checked) -> loginAuto(checked));
        loginBtn.setOnClickListener(v -> loginMemberPin());

        CommonTitleView commonTitleView = findViewById(R.id.commontitle);
        commonTitleView.setBackButtonClickListener(v -> finish());
        commonTitleView.setTitleName(getString(R.string.login));
    }

    private void loginMemberPin() {

        // Step 2. ID 조회 및 PW 입력
        EditText etLoginID = findViewById(R.id.loginemail);
        String pinID = etLoginID.getText().toString();
        EditText loginPinPW = findViewById(R.id.loginpassword);
        String pinPW = loginPinPW.getText().toString();

        userUseHelper.userLogin(pinID, pinPW, isAutoLogin);
    }

    private void loginAuto(boolean checked) {
        isAutoLogin = checked;
    }

    @Override
    public void onActType(CodeTypeManager.ClassCodeManager type) {
        switch (type) {
            case LOGIN_SUCCESS:
                Logger.d("LogInActivity 회원 로그인 완료!");
//                registTrue();

//                FingerprintUiHelper fingerprintUiHelper = new FingerprintUiHelper(FingerprintManagerCompat.from(getApplication()));
//                if (!fingerprintUiHelper.ishasEnrolledFingerprints()) {
//                    startActivity(new Intent(LogInFingerBeforeActivity.this, LogInFingerActivity.class));
//                }
////                else if( ){
////                }
//                else{
//                    introCheckModelChange();
//                }
                break;
            case LOGIN_FAIL:
                failMesage.setVisibility(View.VISIBLE);
                break;
            case USER_LOGIN:
                registTrue();
                break;
        }
    }

    private void registTrue() {
//        serverSideAuth.checkRegisterFingerprint();
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
                    boolean existAuthToken = stringTokenizer.nextToken().equals(MoaAuthResultCode.REGIST_FINGER_AUTHTOKEN_EXIST.getAuthCode());
                    stringTokenizer.nextToken();
                    if (existID && !existAuthToken) {
                        startActivity(new Intent(LogInFingerBeforeActivity.this, LogInFingerActivity.class));
                        finish();
                    } else {
                        introCheckModelChange();
                    }
//                        Toast.makeText(context, context.getString(R.string.msg_fingerprint_already_registered), Toast.LENGTH_SHORT).show();

                } catch (InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException e) {
                    throw new RuntimeException("[*] --- Error occurred : ", e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMoaAuth> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
//                Toast.makeText(context, context.getString(R.string.msg_server_err_occurred), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 자동 로그인이 아닐경우 사용자가 앱을 종료시 userId 를 새로 발급 받아야하므로
     * 비회원 로그인 후 주소 설정 프로세스를 탈수 있도록 IntroCheckModel 값을 변경
     */
    private void introCheckModelChange() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.putExtra("finger", "yes");
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_other_type://다른 로그인 방법
                lIdView.setVisibility(View.GONE);
                lFingerView.setVisibility(View.GONE);
                lHeightView.setVisibility(View.GONE);
                lBtnView.setVisibility(View.GONE);
                lSettingView.setVisibility(View.VISIBLE);
                break;

            case R.id.main_auth_idpw://아이디 인증
                lIdView.setVisibility(View.VISIBLE);
                lFingerView.setVisibility(View.GONE);
                lHeightView.setVisibility(View.GONE);
                lBtnView.setVisibility(View.VISIBLE);
                lSettingView.setVisibility(View.GONE);
                break;

            case R.id.main_auth_finger:

                break;
        }
    }

//            @RequiresApi(api = Build.VERSION_CODES.M)
//            public void onRegisterFingerprint(String base64AuthToken) {
//                serverSideAuth.registerFingerprint(base64AuthToken);
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            public void onLoginFingerprint(String nonceOTP, String base64AuthToken) {
//                serverSideAuth.doLoginFingerprint(nonceOTP, base64AuthToken);
//            }
}
