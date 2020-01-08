package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.controller.IdentityVerificationFragment;
import com.moaPlatform.moa.payment.CardListItem;
import com.moaPlatform.moa.payment.MoaPayPswInitActivity;
import com.moaPlatform.moa.payment.PaymentResultsReceiver;
import com.moaPlatform.moa.payment.ServerSideMoaPayController;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.custom_view.CommonPasswordInputView;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * 결제 비밀번호 재설정 화면 activity
 */
public class PaymentSearchPasswordActivity extends AppCompatActivity implements PaymentResultsReceiver {

    private CommonTitleView titleView;
    private CommonPasswordInputView inputPasswordSettingPassword;
    private CommonPasswordInputView inputPasswordSettingPasswordCheck;
    private ServerSideMoaPayController serverSideMoaPayController;
    Button btnSave;
    private ProgressBar progressBar;
    private IdentityVerificationFragment identityVerificationFragment;
    private FragmentManager fragmentManager;
    private TextView tvResultMsg, intoPwSet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_search_password);
        initLayout();

//        progressBar = findViewById(R.id.progressbar);
//        progressBar.setVisibility(View.VISIBLE);

        identityVerificationFragment = new IdentityVerificationFragment();
        fragmentManager = getSupportFragmentManager();

        serverSideMoaPayController = new ServerSideMoaPayController(this);
        serverSideMoaPayController.init(this);
    }

    private void initLayout() {
//        titleView = findViewById(R.id.commonTitlePaymentResetPassword);
        inputPasswordSettingPassword = findViewById(R.id.inputPasswordSettingPassword);
        inputPasswordSettingPasswordCheck = findViewById(R.id.inputPasswordSettingPasswordCheck);

        inputPasswordSettingPassword.setTitleName(getString(R.string.password));
        inputPasswordSettingPasswordCheck.setTitleName(getString(R.string.payment_password_check_pw));

        intoPwSet = findViewById(R.id.intoPasswordNumberSetting);
        tvResultMsg = findViewById(R.id.resultmsg);

        CommonTitleView top_tolbar = findViewById(R.id.top_tolbar);
        top_tolbar.setTitleName("결제 비밀번호 초기화");
        top_tolbar.setBackButtonClickListener(v -> finish());

//        ImageView ivBack = findViewById(R.id.btnBack);
//        ivBack.setOnClickListener(v -> finish());

//        titleView.setTitleName(getString(R.string.payment_reset_password_title));

//        //타이틀 뒤로가기
//        titleView.setBackButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        btnSave = findViewById(R.id.pwsearchok);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassWord();
            }
        });


//        //변경하기
//        btnPaymentResetPasswordUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logger.d("현재 비밀번호 >>> " + inputPaymentResetPasswordCurrentPassword.getInputText());
//                Logger.d("새 비밀번호 >>> " + inputPaymentResetPasswordNewPassword.getInputText());
//                Logger.d("새 비밀번호 확인 >>> " + inputPaymentResetPasswordCheckPassword.getInputText());
//
//                serverSideMoaPayController.changePayPsw(inputPaymentResetPasswordCurrentPassword.getInputText().trim(), inputPaymentResetPasswordCheckPassword.getInputText());
//            }
//        });
    }

    private void checkPassWord() {
        if (inputPasswordSettingPassword.getInputText().trim().length() == 6) {
            intoPwSet.setVisibility(View.GONE);
        } else {
            intoPwSet.setVisibility(View.VISIBLE);
        }

        if (inputPasswordSettingPasswordCheck.getInputText().trim().length() == 6) {
            tvResultMsg.setVisibility(View.GONE);
        } else {
            tvResultMsg.setVisibility(View.VISIBLE);
        }

        if (inputPasswordSettingPassword.getInputText().trim().length() < 6 || inputPasswordSettingPasswordCheck.getInputText().trim().length() < 6) {
            return;
        }

        // 입력한 비밀번호가 같으면 수행
        if (ObjectUtil.checkNotNull(inputPasswordSettingPassword.getInputText().trim())
                && ObjectUtil.checkNotNull(inputPasswordSettingPasswordCheck.getInputText().trim()) && inputPasswordSettingPassword.getInputText().equals(inputPasswordSettingPasswordCheck.getInputText().trim())) {
            tvResultMsg.setVisibility(View.GONE);
//            serverSideMoaPayController.initializePayPsw(inputPasswordSettingPassword.getInputText());
            Intent initPswPayIntent = new Intent(this, MoaPayPswInitActivity.class);
            initPswPayIntent.putExtra("initPsw", inputPasswordSettingPassword.getInputText());
            startActivity(initPswPayIntent);
            finish();
        } else {
            tvResultMsg.setVisibility(View.VISIBLE);
        }
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
    }

    @Override
    public void onFailPayment(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onForwardInitPswNonce(String nonce) {
    }

    @Override
    public void onNotMatchedPw() {
    }
}
