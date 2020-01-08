package com.moaPlatform.moa.auth.sign_up.controller;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.account.FindIdActivity;
import com.moaPlatform.moa.auth.FingerprintUiHelper;
import com.moaPlatform.moa.auth.sign_up.FindPasswordActivity;
import com.moaPlatform.moa.auth.sign_up.model.ResLoginActivityUserInfoShellModel;
import com.moaPlatform.moa.bottom_menu.main.MainServerController;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.intro.db.IntroCheckModel;
import com.moaPlatform.moa.notification.ReqNotificationModel;
import com.moaPlatform.moa.preference.PushTokenPreference;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.auth.ServerSideAuth;
import com.moaPlatform.moa.util.auth.UserUseHelper;
import com.moaPlatform.moa.util.auth.UserWalletHelper;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.db.RealmController;
import com.moaPlatform.moa.util.dialog.yesOrNo.OneBtnDialog;
import com.moaPlatform.moa.util.interfaces.ClassConnectInterface;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.App;
import com.moaPlatform.moa.util.singleton.RetrofitClient;
import com.moaPlatform.moa.wallet.WalletRestoreActivity;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LogInActivity extends AppCompatActivity implements ClassConnectInterface, View.OnClickListener {
    FingerprintUiHelper fingerprintUiHelper;
    private EditText inputPassWord;
    private boolean isAutoLogin = true;
    private UserUseHelper userUseHelper;
    private UserWalletHelper userWalletHelper;
    private LinearLayout lFingerView, lHeightView, lBtnView, lSettingView;
    private ServerSideAuth serverSideAuth = ServerSideAuth.getInstance();

    private LinearLayout llMainLoginDefaultLoginContainer;      //아이디&패스워드 UI
    private Button btnMainLoginIdPwLogin;                       //로그인 버튼
    private RelativeLayout rlMainLoginDefaultLoginOptionContainer;  //자동로그인 & 아이디 비밀번호 찾기 UI

    private TextView tvLoginFailMsg;

    private String joinFailmessage;
    private String alreadyJoinedEmail;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loagin);

        initDefaultData();
        initView();
        WebView wbWallet = findViewById(R.id.wbWallet);
        userWalletHelper = new UserWalletHelper(this);
        userWalletHelper.setClassConnectInterface(this);
        userWalletHelper.init(wbWallet);
    }

    private void initDefaultData() {
        //회원가입중 이미 가입된 전화번호일시 로그인 화면으로 이동되어 노출되는 팝업 다이얼로그
        joinFailmessage = getIntent().getStringExtra(IdentityVerificationFragment.EXTRA_USER_JOIN_RESULT_MESSAGE);
        alreadyJoinedEmail = getIntent().getStringExtra(IdentityVerificationFragment.EXTRA_USER_JOIN_RESULT_EMAIL);
        showAlreadyJoinNoticePopup(joinFailmessage, alreadyJoinedEmail);
    }

    //로그인 > 회원가입으로 이동하여 중복된 번호로 가입을 하려고 시도하였을시에 대한 처리
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        joinFailmessage = intent.getStringExtra(IdentityVerificationFragment.EXTRA_USER_JOIN_RESULT_MESSAGE);
        alreadyJoinedEmail = intent.getStringExtra(IdentityVerificationFragment.EXTRA_USER_JOIN_RESULT_EMAIL);
        Logger.d("onNewIntent >>>>>> joinFailmessage " + joinFailmessage + " alreadyJoinedEmail >>> " + alreadyJoinedEmail);
        showAlreadyJoinNoticePopup(joinFailmessage, alreadyJoinedEmail);
    }

    /**
     * 이미 가입된 계정임을 알려주는 팝업 다이얼로그
     */
    private void showAlreadyJoinNoticePopup(String message, String email) {
        if (ObjectUtil.checkNotNull(message) && ObjectUtil.checkNotNull(email)) {
            OneBtnDialog oneBtnDialog = new OneBtnDialog();
            oneBtnDialog.dialogContent(message + "\n\n등록된 계정은\n" + email + " 입니다.");
            oneBtnDialog.show(getSupportFragmentManager(), "joinResultOneBtnDialog");
            oneBtnDialog.oneBtnDialogFragmentListener(oneBtnDialog::dismiss);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        userUseHelper = new UserUseHelper(this);
        userUseHelper.setClassConnectInterface(this);
        fingerprintUiHelper = new FingerprintUiHelper(FingerprintManagerCompat.from(getApplication()));
        ServerSideAuth.getInstance().setContext(this);
//        serverSideAuth.setContext(this);
        String fingerYN = getIntent().getStringExtra("finger");
        if (fingerYN == null || fingerYN.equals("")) {
            fingerYN = "no";
        }
        if (fingerYN.equals("yes")) {

            Logger.d("지문인증일때 yes");

            isShowIdPwView(false);

            lFingerView.setVisibility(View.VISIBLE);
            lHeightView.setVisibility(View.VISIBLE);
            lBtnView.setVisibility(View.VISIBLE);
            lSettingView.setVisibility(View.GONE);
        } else {
            Logger.d("지문인증일때 nono");
        }

        SharedPreferences sf = getSharedPreferences("havefinger", MODE_PRIVATE);
        String sHave = sf.getString("setfinger", "no");

        if (sHave.equals("yes")) {
            introCheckModelChanges();
        }
//        else if (sHave.equals("no")) {
//
//        }
    }

    private void initView() {

        // 툴바세팅
        CommonTitleView viewToolbar = findViewById(R.id.viewToolbar);
        viewToolbar.setTitleName(getString(R.string.login));
        viewToolbar.isShowBottomLine(false);
        viewToolbar.setBackButtonClickListener(v -> finish());

        // 로그인 실패 에러 메시지
        tvLoginFailMsg = findViewById(R.id.tvLoginFailMsg);
        tvLoginFailMsg.setVisibility(View.GONE);

        inputPassWord = findViewById(R.id.etInputPassword);
        llMainLoginDefaultLoginContainer = findViewById(R.id.llMainLoginDefaultLoginContainer);
        btnMainLoginIdPwLogin = findViewById(R.id.btnMainLoginIdPwLogin);
        rlMainLoginDefaultLoginOptionContainer = findViewById(R.id.rlMainLoginDefaultLoginOptionContainer);

        LinearLayout llMainLoginJoin = findViewById(R.id.llMainLoginJoin);

        CheckBox checkLogin = findViewById(R.id.cbAuthLogin);
        checkLogin.setOnCheckedChangeListener((v, checked) -> loginAuto(checked));
        btnMainLoginIdPwLogin.setOnClickListener(v -> loginMemberPin());
        TextView tvExpired = findViewById(R.id.search_pw);
        tvExpired.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FindPasswordActivity.class);
            startActivity(intent);
        });

        TextView search_id = findViewById(R.id.search_id);
        search_id.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FindIdActivity.class);
            startActivity(intent);
        });
        lFingerView = findViewById(R.id.main_fingerprint_login);
        lHeightView = findViewById(R.id.main_fix_height);
        lBtnView = findViewById(R.id.main_btn_view);
        lSettingView = findViewById(R.id.main_setting_login);
        RelativeLayout lIdLogin = findViewById(R.id.main_auth_idpw);
        RelativeLayout lFingerLogin = findViewById(R.id.main_auth_finger);

        lFingerView.setVisibility(View.GONE);
        lHeightView.setVisibility(View.GONE);
        lBtnView.setVisibility(View.GONE);
        lSettingView.setVisibility(View.GONE);

        lBtnView.setOnClickListener(this);
        lIdLogin.setOnClickListener(this);
        lFingerLogin.setOnClickListener(this);
        lFingerView.setOnClickListener(this);

        lBtnView.setVisibility(View.VISIBLE);
        TextView tvOtherLogin = findViewById(R.id.login_other_type);
        tvOtherLogin.setOnClickListener(this);

        llMainLoginJoin.setOnClickListener(v -> {
            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, MoaConstants.REQUEST_USER_JOIN);
        });

        inputPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String checkingText = s.toString().replace(" ", "");
                if (s.toString().length() != checkingText.length()) {
                    inputPassWord.setText(checkingText);
                    inputPassWord.setSelection(checkingText.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loginMemberPin() {
        keyboardDown();
        // Step 2. ID 조회 및 PW 입력
        EditText etLoginID = findViewById(R.id.etInputEmail);
        String pinID = etLoginID.getText().toString();
        EditText loginPinPW = findViewById(R.id.etInputPassword);
        String pinPW = loginPinPW.getText().toString();

        if (pinID.trim().equals("") || !StringUtil.isEmail(pinID)) {
            tvLoginFailMsg.setText(getString(R.string.loginactivity_empty_email));
            tvLoginFailMsg.setVisibility(View.VISIBLE);
            return;
        }

        if (pinPW.trim().equals("")) {
            tvLoginFailMsg.setText(getString(R.string.loginactivity_empty_password));
            tvLoginFailMsg.setVisibility(View.VISIBLE);
            return;
        }

        tvLoginFailMsg.setVisibility(View.GONE);
        tvLoginFailMsg.setText(getString(R.string.loginactivity_login_fail_msg));
        userUseHelper.userLogin(pinID, pinPW, isAutoLogin);
    }

    private void loginAuto(boolean checked) {
        isAutoLogin = checked;
    }

    /**
     * id/pw  UI 노출 여부
     *
     * @param isVisible 노출 유무
     */
    private void isShowIdPwView(boolean isVisible) {
        if (llMainLoginDefaultLoginContainer != null) {
            if (isVisible) {
                llMainLoginDefaultLoginContainer.setVisibility(View.VISIBLE);
            } else {
                llMainLoginDefaultLoginContainer.setVisibility(View.GONE);
            }
        }

        if (btnMainLoginIdPwLogin != null) {
            if (isVisible) {
                btnMainLoginIdPwLogin.setVisibility(View.VISIBLE);
            } else {
                btnMainLoginIdPwLogin.setVisibility(View.GONE);
            }
        }

        if (rlMainLoginDefaultLoginOptionContainer != null) {
            if (isVisible) {
                rlMainLoginDefaultLoginOptionContainer.setVisibility(View.VISIBLE);
            } else {
                rlMainLoginDefaultLoginOptionContainer.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onActType(CodeTypeManager.ClassCodeManager type) {
        Intent walletRestoreIntent;
        switch (type) {
            case LOGIN_SUCCESS:
                introCheckModelChange();
                break;
            case LOGIN_FAIL:
                setResult(MoaConstants.RESULT_LOGIN_FAIL, null);
                tvLoginFailMsg.setVisibility(View.VISIBLE);
//                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show();
                break;
            case USER_LOGIN:
                setResult(MoaConstants.RESULT_LOGIN_SUCCESS, null);
                pushSetting(type);
                break;
            case MOVE_WALLET_RESTORE_SELF_AUTH:
                walletRestoreIntent = new Intent(this, WalletRestoreActivity.class);
                startActivityForResult(walletRestoreIntent, 0);
                break;
            case WALLET_CREATE_SUCCESS:
                getUserInfoModel();
//                finish();
                break;
            case FINGER_LOGIN:
                pushSetting(type);
                break;
            case WALLET_RESTORE_FAIL:
                walletRestoreIntent = new Intent(this, WalletRestoreActivity.class);
                walletRestoreIntent.putExtra("onlyPw", "onlyPw");
                startActivityForResult(walletRestoreIntent, 0);
                break;
        }
    }

    public void pushSetting(CodeTypeManager.ClassCodeManager enumType) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Logger.d("getInstanceId failed " + task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    // Log and toast
                    Logger.d("getInstanceId token " + token);

                    //등록된 적이 없으면 푸시키 등록 수행
                    if (ObjectUtil.checkNotNull(token)) {
                        ReqNotificationModel reqNotificationModel = new ReqNotificationModel();
                        reqNotificationModel.fcmToken = token;

                        MainServerController mainServerController = new MainServerController(getApplicationContext());
                        mainServerController.putFcmKey(reqNotificationModel);
                        mainServerController.setRetrofitConnectionResult(new RetrofitConnectionResult() {
                            @Override
                            public void onRetrofitSuccess(int type, BaseModel baseModel) {
                                if (!isFinishing()) {
                                    PushTokenPreference.getInstance().setUpload(getApplicationContext(), true);
                                }
                                switch (enumType) {
                                    case FINGER_LOGIN:
                                        introCheckModelChangeFinger();
                                        break;
                                    case USER_LOGIN:
                                        introCheckModelChange();
                                        break;
                                }
                            }

                            @Override
                            public void onRetrofitFail(int type, String msg) {
                                if (!isFinishing()) {
                                    PushTokenPreference.getInstance().setUpload(getApplicationContext(), false);
                                }
                                switch (enumType) {
                                    case FINGER_LOGIN:
                                        introCheckModelChangeFinger();
                                        break;
                                    case USER_LOGIN:
                                        introCheckModelChange();
                                        break;
                                }
                            }
                        });
                    } else {
                        switch (enumType) {
                            case FINGER_LOGIN:
                                introCheckModelChangeFinger();
                                break;
                            case USER_LOGIN:
                                introCheckModelChange();
                                break;
                        }
                    }
                });
    }

    /**
     * 자동 로그인이 아닐경우 사용자가 앱을 종료시 userId 를 새로 발급 받아야하므로
     * 비회원 로그인 후 주소 설정 프로세스를 탈수 있도록 IntroCheckModel 값을 변경
     */
    private void introCheckModelChange() {
        if (!isAutoLogin) {
            Realm.init(this);
            RealmController realmController = new RealmController();
            Realm realm = realmController.realmInstance();

            realm.executeTransactionAsync(realm1 -> {
                IntroCheckModel introCheckModel = realm1.where(IntroCheckModel.class).findFirst();
                if (introCheckModel != null)
                    introCheckModel.type = CodeTypeManager.RealmCodeManager.INTRO_SELECT_PERMISSION_SUCCESS.getCode();
            }, () -> {
                realm.close();
                userWalletHelper.prepareRestoreWallet();
            });
        } else {
            userWalletHelper.prepareRestoreWallet();
        }

    }

    /**
     * 자동 로그인이 아닐경우 사용자가 앱을 종료시 userId 를 새로 발급 받아야하므로
     * 비회원 로그인 후 주소 설정 프로세스를 탈수 있도록 IntroCheckModel 값을 변경
     */
    private void introCheckModelChangeFinger() {

        Realm.init(this);
        RealmController realmController = new RealmController();
        Realm realm = realmController.realmInstance();

        realm.executeTransactionAsync(realm1 -> {
            IntroCheckModel introCheckModel = realm1.where(IntroCheckModel.class).findFirst();
            if (introCheckModel != null)
                introCheckModel.type = CodeTypeManager.RealmCodeManager.INTRO_SELECT_PERMISSION_SUCCESS.getCode();
        }, () -> {
            realm.close();
            userWalletHelper.prepareRestoreWallet();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Logger.d("requestCode " + requestCode + " resultCode " + resultCode);

        if (requestCode == MoaConstants.REQUEST_USER_JOIN) {
            if (resultCode == MoaConstants.RESULT_USER_JOIN_SUCCESS) {
                setResult(MoaConstants.RESULT_USER_JOIN_SUCCESS);
                finish();
            }
        }
        switch (resultCode) {
            case RESULT_OK:
                userWalletHelper.startRestoreWallet(data.getStringExtra("walletPw"));
                break;
            case RESULT_CANCELED:
//                onFailWalletRestore();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_other_type://다른 로그인 방법
//                lIdView.setVisibility(View.GONE);
                isShowIdPwView(false);
                lFingerView.setVisibility(View.GONE);
                lHeightView.setVisibility(View.GONE);
                lBtnView.setVisibility(View.GONE);
                lSettingView.setVisibility(View.VISIBLE);
                break;

            case R.id.main_auth_idpw://아이디 인증
//                lIdView.setVisibility(View.VISIBLE);
                isShowIdPwView(true);
                lFingerView.setVisibility(View.GONE);
                lHeightView.setVisibility(View.GONE);
                lBtnView.setVisibility(View.VISIBLE);
                lSettingView.setVisibility(View.GONE);
                break;

            case R.id.main_auth_finger://지문 인증 가기 위한 단계
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    Toast.makeText(this, "해당 기기는 지문 인식 기능을 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!fingerprintUiHelper.isFingerprintAuthAvailable()) {
                    Toast.makeText(this, "해당 기기는 지문 인식 기능을 지원하지 않습니다..", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    startActivity(new Intent(LogInActivity.this, LogInFingerBeforeActivity.class));
                    finish();
                }
                break;
            case R.id.main_fingerprint_login://지문 인증 로그인 실제
                serverSideAuth.checkLoginFingerprint();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRegisterFingerprint(String base64AuthToken) {
        serverSideAuth.registerFingerprint(base64AuthToken);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onLoginFingerprint(String nonceOTP, String base64AuthToken) {
        serverSideAuth.doLoginFingerprint(nonceOTP, base64AuthToken);
    }

    private void introCheckModelChanges() {
        isShowIdPwView(false);

        lFingerView.setVisibility(View.VISIBLE);
        lHeightView.setVisibility(View.VISIBLE);
        lBtnView.setVisibility(View.VISIBLE);
        lSettingView.setVisibility(View.GONE);
    }

    private void getUserInfoModel() {
        RetrofitClient.getInstance().getMoaService().getUserInfo(new CommonModel()).enqueue(new Callback<ResLoginActivityUserInfoShellModel>() {
            @Override
            public void onResponse(@NonNull Call<ResLoginActivityUserInfoShellModel> call, @NonNull Response<ResLoginActivityUserInfoShellModel> response) {

                if (response.body() != null) {
                    if (RetrofitClient.getInstance().hasReissuedAccessToken(response.body())) {
                        getUserInfoModel();
                        return;
                    }

                    if (response.body().resultValue.equals(ServerSideMsg.SUCCESS)) {
                        App.getInstance().userInfoModel = response.body().userInfoModel;
                        finish();
                    }

                } else {
                    finish();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResLoginActivityUserInfoShellModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                finish();
            }
        });
    }

    private void keyboardDown() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

}
