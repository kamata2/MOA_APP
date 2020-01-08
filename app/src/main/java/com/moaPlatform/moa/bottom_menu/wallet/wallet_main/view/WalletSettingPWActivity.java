package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view;

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
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.online_payment.order_payment.WalletPayActivity;
import com.moaPlatform.moa.payment.CardRegisterActivity;
import com.moaPlatform.moa.preference.PaySignPreference;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.custom_view.CommonPasswordInputView;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.moaPlatform.moa.constants.MoaConstants.EXTRA_TO_VIEW;


public class WalletSettingPWActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_TO_VIEW_WALLET_ACTIVITY = "EXTRA_TO_VIEW_WALLET_ACTIVITY";
    public static final String EXTRA_TO_VIEW_WALLET_PAY_ACTIVITY = "EXTRA_TO_VIEW_WALLET_PAY_ACTIVITY";
    public static final String EXTRA_TO_VIEW_WALLET_SETTING_ACTIVITY = "EXTRA_TO_VIEW_WALLET_SETTING_ACTIVITY";

    private CommonPasswordInputView inputPasswordSettingPassword;
    private CommonPasswordInputView inputPasswordSettingPasswordCheck;
    private TextView tvResultMsg, intoPass;
    private Button btnSave;
    private EditText etCardNick;

    private String toView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goeatpay_password);

        initDefaultData();
        initLayout();
    }

    private void initDefaultData() {
        toView = getIntent().getStringExtra(EXTRA_TO_VIEW);
    }

    /**
     * 변수 초기화
     */
    private void initLayout() {

        inputPasswordSettingPassword = findViewById(R.id.inputPasswordSettingPassword);
        inputPasswordSettingPasswordCheck = findViewById(R.id.inputPasswordSettingPasswordCheck);
        inputPasswordSettingPassword.showKeyboard();

        inputPasswordSettingPassword.setTitleName(getString(R.string.password));
        inputPasswordSettingPasswordCheck.setTitleName(getString(R.string.payment_password_check_pw));


        intoPass = findViewById(R.id.into_password_number);
        etCardNick = findViewById(R.id.nameofnick);

        etCardNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != s.toString().replace(" ", "").length()) {
                    etCardNick.setText(s.toString().replace(" ", ""));
                    etCardNick.setSelection(etCardNick.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvResultMsg = findViewById(R.id.resultmsg);
        tvResultMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(WalletSettingPWActivity.this, com.moaPlatform.moa.wallet.WalletMainActivity.class));
//                startActivity(new Intent(WalletSettingPWActivity.this, MoaPayActivity.class));
            }
        });
        btnSave = findViewById(R.id.goeatpayok);
        btnSave.setOnClickListener(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassWord();
            }
        });

        CommonTitleView commonTitleView = findViewById(R.id.commontitle);
        commonTitleView.setBackButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(MoaConstants.RESULT_CARDREGISTER_CANCEL);
                finish();
            }
        });
        commonTitleView.setTitleName(getString(R.string.pay_password_title));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gonextpage:
//                startActivity(new Intent(WalletSettingPWActivity.this, MoaPayActivity.class));
//                checkPassWord();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("requestCode " + requestCode + " resultCode " + resultCode);
        if (requestCode == MoaConstants.REQUEST_WALLET_SETTING_PW_ACTIVITY) {
            if (resultCode == MoaConstants.RESULT_CARDREGISTER_SUCCESS) {
                setResult(MoaConstants.RESULT_CARDREGISTER_SUCCESS);
                finish();
            } else {

            }
        }
    }

    private void checkPassWord() {

        String nick = etCardNick.getText().toString().trim();

        // 맨 위
        if (inputPasswordSettingPassword.getInputText().trim().length() == 6) {
            intoPass.setVisibility(View.GONE);
        } else {
            intoPass.setVisibility(View.VISIBLE);

        }
        // 두번째 비밀번호
        if (inputPasswordSettingPasswordCheck.getInputText().trim().length() == 6 && inputPasswordSettingPassword.getInputText().equals(inputPasswordSettingPasswordCheck.getInputText())){
            tvResultMsg.setVisibility(View.GONE);
        } else {
            tvResultMsg.setVisibility(View.VISIBLE);
        }/*
        if (inputPasswordSettingPassword.getInputText().trim().length() < 6 || inputPasswordSettingPasswordCheck.getInputText().trim().length() < 6) {
            return;
        }*/
        // 카드 닉네임
        if (!ObjectUtil.checkNotNull(nick)) {
            Toast.makeText(WalletSettingPWActivity.this, "카드닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 입력한 비밀번호가 같으면 수행
        if ((inputPasswordSettingPassword.getInputText().trim().length() == 6 && inputPasswordSettingPasswordCheck.getInputText().trim().length() == 6) &&
                ObjectUtil.checkNotNull(inputPasswordSettingPassword.getInputText())
                && ObjectUtil.checkNotNull(inputPasswordSettingPasswordCheck.getInputText())
                && inputPasswordSettingPassword.getInputText().equals(inputPasswordSettingPasswordCheck.getInputText())) {
            tvResultMsg.setVisibility(View.GONE);

//            SharedPreferences sharedPreferences = getSharedPreferences("paysign", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("walletpw", inputPasswordSettingPasswordCheck.getInputText());
//            editor.putString("walletnick", nick);
//            editor.putBoolean("pwactivity", true);
//            editor.commit();

//            SharedPreferences sf = getSharedPreferences("where", MODE_PRIVATE);
//            String sWhere = sf.getString("payyesno","");

            Logger.d("FROM VIEW >>> " + toView);

            if (toView != null) {
                if (toView.equals(EXTRA_TO_VIEW_WALLET_PAY_ACTIVITY)) {
                    Intent walletPayIntent = new Intent(this, WalletPayActivity.class);
                    walletPayIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    walletPayIntent.putExtra(CardRegisterActivity.EXTRA_CARD_REGISTER_NICK_NAME, etCardNick.getText().toString());
                    startActivity(walletPayIntent);

                } else if (toView.equals(EXTRA_TO_VIEW_WALLET_ACTIVITY)) {
                    PaySignPreference.getInstance().setWalletPassword(this, inputPasswordSettingPassword.getInputText());
                    Intent cardRegisterIntent = new Intent(this, CardRegisterActivity.class);
                    cardRegisterIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    cardRegisterIntent.putExtra(CardRegisterActivity.EXTRA_CARD_REGISTER_NICK_NAME, etCardNick.getText().toString());
                    cardRegisterIntent.putExtra(MoaConstants.EXTRA_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT, getIntent().getBooleanExtra(MoaConstants.EXTRA_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT, false));
                    startActivityForResult(cardRegisterIntent, MoaConstants.REQUEST_WALLET_SETTING_PW_ACTIVITY);

                } else if (toView.equals(EXTRA_TO_VIEW_WALLET_SETTING_ACTIVITY)) {
                    startActivity(new Intent(WalletSettingPWActivity.this, WalletSettingActivity.class));
                }
            }
            //todo server로 전송
        } else {
            // 비밀번호가 일치하지 않습니다.
            tvResultMsg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        //패스워드 설정에서 뒤로가기시 인증화면을 닫고 월렛 화면으로 이동처리
        setResult(MoaConstants.RESULT_CARDREGISTER_CANCEL);
        finish();
    }
}
