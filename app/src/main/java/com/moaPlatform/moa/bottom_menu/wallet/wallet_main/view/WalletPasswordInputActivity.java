package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.util.custom_view.CommonPasswordInputView;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 지갑 비밀번호 인증화면
 * 월렛 > 포인트 전환시 해당 화면을 사용한다.
 * 추후 공통 화면으로 사용될 가능성이 높은 화면
 */
public class WalletPasswordInputActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_password_input);

        Button btnWalletPasswordInputPointExchange = findViewById(R.id.btnWalletPasswordInputPointExchange);

        CommonTitleView commonTitleView = findViewById(R.id.commonTitleWalletPasswordInput);
        commonTitleView.setClosedButtonType();
        commonTitleView.setBackButtonClickListener(v -> finish());
        commonTitleView.setTitleName(getString(R.string.wallet_password_input_title_name));

        CommonPasswordInputView commonPasswordInputView = findViewById(R.id.inputWalletPasswordInputView);
        commonPasswordInputView.isShowTitle(false);

        //입력 포커스
        commonPasswordInputView.showKeyboard();

        commonPasswordInputView.getPinViewPasswordInput().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                Intent intent = new Intent();
                intent.putExtra(MoaConstants.EXTRA_WALLET_PASSWORD, commonPasswordInputView.getInputText());
                setResult(MoaConstants.RESULT_WALLET_PASSWORD_INPUT_ACTIVITY, intent);
                finish();
            }
            return false;
        });

        //포인트 전환하기
        btnWalletPasswordInputPointExchange.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(MoaConstants.EXTRA_WALLET_PASSWORD, commonPasswordInputView.getInputText());
            setResult(MoaConstants.RESULT_WALLET_PASSWORD_INPUT_ACTIVITY, intent);
            finish();
        });
    }
}
