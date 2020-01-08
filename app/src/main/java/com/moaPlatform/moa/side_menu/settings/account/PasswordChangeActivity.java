package com.moaPlatform.moa.side_menu.settings.account;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.auth.UserUseHelper;
import com.moaPlatform.moa.util.custom_view.CommonEditTextErrorMsgView;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.singleton.App;

import androidx.appcompat.app.AppCompatActivity;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

public class PasswordChangeActivity extends AppCompatActivity {

    private CommonEditTextErrorMsgView viewNowPassword, viewNewPassword, viewNewPasswordCheck;
    //    private boolean nowPasswordSuccess = false, newPasswordSuccess = false, newPasswordCheckSuccess = false;
    private Button btnPasswordChange;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_change_activity);
        initView();
    }

    /**
     * 뷰 초기화
     */
    private void initView() {
        // 상단 툴바 세팅
        CommonTitleView topToolbar = findViewById(R.id.topToolbar);
        topToolbar.setTitleName(getString(R.string.common_pw_change));
        topToolbar.setBackButtonClickListener(v -> finish());
        // 현재 비밀번호 세팅
        viewNowPassword = findViewById(R.id.viewNowPassword);
        viewNowPassword.setInputTextMaxSize(20);
        viewNowPassword.setNotEmpty(true);
        editTextErrorMsgViewInit(viewNowPassword, R.string.payment_reset_password_current_pw, getString(R.string.sign_up_input_password), getString(R.string.common_pw_not_match));
//        viewNowPassword.setUserInputDataChecking(inputCount -> {
//            nowPasswordSuccess = inputCount >= 8 && inputCount <= 20;
//            btnPasswordChangeActiveCheck();
//        });
        // 새 비밀번호 세팅
        viewNewPassword = findViewById(R.id.viewNewPassword);
        viewNewPassword.setInputTextMaxSize(20);
        viewNewPassword.setNotEmpty(true);
        editTextErrorMsgViewInit(viewNewPassword, R.string.common_new_pw, getString(R.string.sign_up_input_password), getString(R.string.sign_up_input_password));
//        viewNewPassword.setUserInputDataChecking(inputCount -> {
//            newPasswordSuccess = inputCount >= 8 && inputCount <= 20;
//            btnPasswordChangeActiveCheck();
//        });
        // 새 비밀번호 확인 세팅
        viewNewPasswordCheck = findViewById(R.id.viewNewPasswordCheck);
        viewNewPasswordCheck.setInputTextMaxSize(20);
        viewNewPasswordCheck.setNotEmpty(true);
        editTextErrorMsgViewInit(viewNewPasswordCheck, R.string.common_new_pw_check, getString(R.string.sign_up_input_password), getString(R.string.common_pw_not_match));
//        viewNewPasswordCheck.setUserInputDataChecking(inputCount -> {
//            newPasswordCheckSuccess = inputCount >= 8 && inputCount <= 20;
//            btnPasswordChangeActiveCheck();
//        });
        // 변경 완료 버튼
        btnPasswordChange = findViewById(R.id.btnPasswordChange);
        btnPasswordChange.setOnClickListener(v -> passwordChecking());
    }

    /**
     * 비밀번호 변경전 패스워드 체크
     */
    private void passwordChecking() {
        if (!viewNowPassword.getInputText().equals(App.getInstance().userPw)) {
            viewNowPassword.errorMsgShow();
            return;
        } else {
            viewNowPassword.errorMsgHidden();
        }

        if (viewNewPassword.getInputText().length() == 8) {
            viewNewPassword.errorMsgHidden();
        } else {
            viewNewPassword.errorMsgShow();
            return;
        }

        if (viewNewPassword.getInputText().equals(viewNewPasswordCheck.getInputText()) && viewNewPassword.getInputText().trim().length() >= 8) {
            viewNewPasswordCheck.errorMsgHidden();
        } else {
            viewNewPasswordCheck.errorMsgShow();
            return;
        }
        passwordChange();
    }

    /**
     * 패스워드 변경
     */
    private void passwordChange() {
        UserUseHelper userUseHelper = new UserUseHelper(this);
        userUseHelper.passwordChange(viewNewPassword.getInputText());
        userUseHelper.setClassConnectInterface(type -> {
            Toast.makeText(getApplicationContext(), getString(R.string.common_toast_msg_success_pw_change), Toast.LENGTH_SHORT).show();
            finish();
        });

    }

    /**
     * CommonEditTextErrorMsgView 데이터 세팅
     *
     * @param view       적용할 뷰
     * @param titleResId 타이틀 텍스트
     * @param errorMsg   에러 메시지
     */
    private void editTextErrorMsgViewInit(CommonEditTextErrorMsgView view, int titleResId, String errorMsg) {
        view.setTitle(titleResId);
        view.editTextInit(errorMsg, TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
        view.setErrorMsg(errorMsg);
    }

    private void editTextErrorMsgViewInit(CommonEditTextErrorMsgView view, int titleResId, String mainText, String errorMsg) {
        view.setTitle(titleResId);
        view.editTextInit(mainText, TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
        view.setErrorMsg(errorMsg);
    }

    /**
     * 변경 완료 버튼을 활성화 및 비활성화 컨트롤
     */
//    private void btnPasswordChangeActiveCheck() {
//        if (nowPasswordSuccess && newPasswordSuccess && newPasswordCheckSuccess) {
//            btnPasswordChange.setEnabled(true);
//            btnPasswordChange.setBackgroundResource(R.color.coral);
//        } else {
//            btnPasswordChange.setEnabled(false);
//            btnPasswordChange.setBackgroundResource(R.color.darkGray);
//        }
//    }
}
