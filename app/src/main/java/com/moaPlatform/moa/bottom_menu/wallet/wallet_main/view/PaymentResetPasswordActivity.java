package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.payment.CardListItem;
import com.moaPlatform.moa.payment.PaymentResultsReceiver;
import com.moaPlatform.moa.payment.ServerSideMoaPayController;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.custom_view.CommonPasswordInputView;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 결제 비밀번호 재설정 화면 activity
 */
public class PaymentResetPasswordActivity extends AppCompatActivity implements PaymentResultsReceiver {

    private CommonTitleView titleView;
    private CommonPasswordInputView inputPaymentResetPasswordCurrentPassword;       //현재 비밀번호
    private TextView tvPaymentResetPasswordCheckPw;                                 //비밀번호 체크 문구
    private TextView tvPaymentResetPasswordCheckPw2;
    private TextView tvPaymentResetPasswordCheckPwNum;                                  //새비밀번호 비밀번호 갯수 확인
    private TextView tvNotPaymentResetPassword;                                  //새비밀번호 비밀번호 갯수 확인
    private CommonPasswordInputView inputPaymentResetPasswordNewPassword;                   //새 비밀번호
    private CommonPasswordInputView inputPaymentResetPasswordCheckPassword;                 //새 비밀번호 확인
    private Button btnPaymentResetPasswordUpdate;
    private ServerSideMoaPayController serverSideMoaPayController;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_reset_password);
        initLayout();

        int a = R.mipmap.ic_launcher;

        serverSideMoaPayController = new ServerSideMoaPayController(this);
        serverSideMoaPayController.init(this);
    }

    private void initLayout() {
        titleView = findViewById(R.id.commonTitlePaymentResetPassword);
        inputPaymentResetPasswordCurrentPassword = findViewById(R.id.inputPaymentResetPasswordCurrentPassword);
        tvPaymentResetPasswordCheckPw = findViewById(R.id.tvPaymentResetPasswordCheckPw);
        tvPaymentResetPasswordCheckPw2 = findViewById(R.id.tvPaymentResetPasswordCheckPw2);
        tvPaymentResetPasswordCheckPwNum = findViewById(R.id.intoPasswordNumberChange);
        tvNotPaymentResetPassword = findViewById(R.id.tvNotPaymentResetPassword);
        tvPaymentResetPasswordCheckPwNum.setVisibility(View.GONE);   //default 숨김처리
        tvPaymentResetPasswordCheckPw.setVisibility(View.GONE);     //default 숨김처리
        tvPaymentResetPasswordCheckPw2.setVisibility(View.GONE);     //default 숨김처리
        tvNotPaymentResetPassword.setVisibility(View.GONE);

        inputPaymentResetPasswordNewPassword = findViewById(R.id.inputPaymentResetPasswordNewPassword);
        inputPaymentResetPasswordCheckPassword = findViewById(R.id.inputPaymentResetPasswordCheckPassword);
        btnPaymentResetPasswordUpdate = findViewById(R.id.btnPaymentResetPasswordUpdate);

        titleView.setTitleName("결제 비밀번호 변경");
        inputPaymentResetPasswordCurrentPassword.setTitleName(getString(R.string.payment_reset_password_current_pw));
        inputPaymentResetPasswordNewPassword.setTitleName(getString(R.string.common_new_pw));
        inputPaymentResetPasswordCheckPassword.setTitleName(getString(R.string.common_new_pw_check));

        //타이틀 뒤로가기
        titleView.setBackButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //변경하기
        btnPaymentResetPasswordUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("현재 비밀번호 >>> " + inputPaymentResetPasswordCurrentPassword.getInputText());
                Logger.d("새 비밀번호 >>> " + inputPaymentResetPasswordNewPassword.getInputText());
                Logger.d("새 비밀번호 확인 >>> " + inputPaymentResetPasswordCheckPassword.getInputText());
                // 연구소에서 코드값 받아서 각 나라별 언어 변환 예정 (임실장님 확인 사항)

                if (inputPaymentResetPasswordCurrentPassword.getInputText().trim().length() == 6) {
                    tvPaymentResetPasswordCheckPw.setVisibility(View.GONE);
                } else {
                    tvPaymentResetPasswordCheckPw.setVisibility(View.VISIBLE);
                    tvNotPaymentResetPassword.setVisibility(View.GONE);
                    return;
                }

                if (inputPaymentResetPasswordNewPassword.getInputText().trim().length() == 6) {
                    tvPaymentResetPasswordCheckPwNum.setVisibility(View.GONE);
                } else {
                    tvPaymentResetPasswordCheckPwNum.setVisibility(View.VISIBLE);
                    return;
                }
                if (inputPaymentResetPasswordCheckPassword.getInputText().trim().length() == 6) {
                    tvPaymentResetPasswordCheckPw2.setVisibility(View.GONE);
                } else {
                    tvPaymentResetPasswordCheckPw2.setVisibility(View.VISIBLE);
                    return;
                }

                if (inputPaymentResetPasswordNewPassword.getInputText().trim().length() < 6 || inputPaymentResetPasswordCheckPassword.getInputText().trim().length() < 6) {
                    return;
                }
                if (inputPaymentResetPasswordNewPassword.getInputText().trim().equals(inputPaymentResetPasswordCheckPassword.getInputText().trim())) {
                    tvPaymentResetPasswordCheckPw2.setVisibility(View.GONE);
                    serverSideMoaPayController.changePayPsw(inputPaymentResetPasswordCurrentPassword.getInputText().trim(), inputPaymentResetPasswordCheckPassword.getInputText().trim());
                } else {
                    tvPaymentResetPasswordCheckPw2.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onLoadCompleteCardList(List<CardListItem> cardListItems) {
    }

    @Override
    public void onReadyJsonData(String jsonData) {
    }

    @Override
    public void onSuccessPayment() {
        Toast.makeText(this, "비밀번호가 변경 되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFailPayment(String msg) {
        tvPaymentResetPasswordCheckPw.setVisibility(View.GONE);
        tvNotPaymentResetPassword.setVisibility(View.VISIBLE);
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onForwardInitPswNonce(String nonce) {
    }

    @Override
    public void onNotMatchedPw() {
    }
}
