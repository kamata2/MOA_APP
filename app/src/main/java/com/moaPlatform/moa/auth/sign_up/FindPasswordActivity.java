package com.moaPlatform.moa.auth.sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.PinResetActivity;
import com.moaPlatform.moa.util.auth.UserUseHelper;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jiwun on 2019-06-11.
 */
public class FindPasswordActivity extends AppCompatActivity {

    // false 면 이메일 입력 화면, true 면 패스워드 입력 화면
    private boolean isNewPasswordInput = false;
    private String userEmail;

    private EditText etInputNewPw;
    private EditText etInputNewPwCheck;
    private Button btnDone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_password_activity);
        init();
    }

    /**
     * 초기화
     */
    private void init() {
        // 툴바 세팅
        CommonTitleView viewToolbar = findViewById(R.id.viewToolbar);
        viewToolbar.setTitleName(getString(R.string.find_pw));
        viewToolbar.setBackButtonClickListener(v -> finish());
        // 비밀번호 입력 뷰를 숨김
        View viewPasswordChange = findViewById(R.id.viewPasswordChange);
        viewPasswordChange.setVisibility(View.GONE);

        // 보인인증 및 비밀번호 변경
        btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(v -> {
            if (!isNewPasswordInput) {
                userEmailCheck();
            } else {
                userPasswordCheck();
            }
        });

        etInputNewPw = findViewById(R.id.etInputNewPw);
        etInputNewPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String checkingText = s.toString().replace(" ", "");
                if (s.toString().length() != checkingText.length()) {
                    etInputNewPw.setText(checkingText);
                    etInputNewPw.setSelection(checkingText.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etInputNewPwCheck = findViewById(R.id.etInputNewPwCheck);
        etInputNewPwCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String checkingText = s.toString().replace(" ", "");
                if (s.toString().length() != checkingText.length()) {
                    etInputNewPwCheck.setText(checkingText);
                    etInputNewPwCheck.setSelection(checkingText.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void userEmailCheck() {
        EditText etInputEmail = findViewById(R.id.etInputEmail);
        userEmail = etInputEmail.getText().toString();
        if (userEmail.length() == 0 || !checkEmail(userEmail)) {
            findViewById(R.id.tvInputEmailError).setVisibility(View.VISIBLE);
        } else {
            Intent intent = new Intent(this, PinResetActivity.class);
            intent.putExtra("email", userEmail);
            startActivityForResult(intent, 0);
        }
    }

    private static boolean checkEmail(String email) {

        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();

    }

    private void userPasswordCheck() {
        // 비밀번호 입력 에러 메시지
        TextView tvInputNewPwError = findViewById(R.id.tvInputNewPwError);
        // 비밀번호 확인 에러 베시지
        TextView tvInputNewPwCheckError = findViewById(R.id.tvInputNewPwCheckError);
        if (etInputNewPw.getText().toString().trim().length() < 8 || etInputNewPw.getText().toString().trim().length() > 20) {
            tvInputNewPwError.setVisibility(View.VISIBLE);
            return;
        } else {
            tvInputNewPwError.setVisibility(View.GONE);
        }

        if (etInputNewPwCheck.getText().toString().trim().length() < 8  || etInputNewPw.getText().toString().trim().length() > 20) {
            tvInputNewPwCheckError.setVisibility(View.VISIBLE);
            return;
        } else {
            tvInputNewPwCheckError.setVisibility(View.GONE);
        }

        if (!etInputNewPw.getText().toString().trim().equals(etInputNewPwCheck.getText().toString().trim())) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            tvInputNewPwCheckError.setVisibility(View.VISIBLE);
            return;
        } else {
            tvInputNewPwCheckError.setVisibility(View.GONE);
        }

        passwordChange(etInputNewPw.getText().toString());
    }

    // 본인인증 후 결과를 받을곳
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                isNewPasswordInput = !isNewPasswordInput;
                findViewById(R.id.viewInputEmail).setVisibility(View.GONE);
                findViewById(R.id.viewPasswordChange).setVisibility(View.VISIBLE);
                break;
            case RESULT_CANCELED:
                findViewById(R.id.viewInputEmail).setVisibility(View.GONE);
                View llFindEmailNotExist = findViewById(R.id.llFindEmailNotExist);
                llFindEmailNotExist.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.GONE);
//                finish();
//                Toast.makeText(this, "비밀번호 재설정 실패", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void passwordChange(String password) {
        UserUseHelper userUseHelper = new UserUseHelper(this);
        userUseHelper.passwordChange(userEmail, password);
        userUseHelper.setClassConnectInterface(type -> {
            switch (type) {
                case PASSWORD_CHANGE_SUCCESS:
                    finish();
                    break;
            }
        });
    }
}
