package com.moaPlatform.moa.wallet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.controller.IdentityVerificationFragment;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.interfaces.Identifiable;

public class WalletRestoreActivity extends AppCompatActivity implements Identifiable {
    private Button btnSelfAuth;
    private LinearLayout llBefor;
    private LinearLayout llAfter;
    private IdentityVerificationFragment identityVerificationFragment;

    private TextView tvErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_restore);

        llBefor = findViewById(R.id.restore_before);
        llAfter = findViewById(R.id.restore_after);

        CommonTitleView commonTitleView = findViewById(R.id.commontitle);
        commonTitleView.setTitleName(getString(R.string.wallet_restore));
        commonTitleView.onHideBackButton();

        CommonTitleView common = findViewById(R.id.topToolbar_wallet_repair);
        common.setTitleName(getString(R.string.wallet_restore_way));
        common.onHideBackButton();

        btnSelfAuth = findViewById(R.id.btnSelfAuth);
        EditText etPw = findViewById(R.id.inputWalletPw);

        tvErrorMsg = findViewById(R.id.tvErrorMsg);
        tvErrorMsg.setVisibility(View.GONE);

        if (getIntent().getStringExtra("onlyPw") != null && getIntent().getStringExtra("onlyPw").equals("onlyPw")) {
            llBefor.setVisibility(View.GONE);
            llAfter.setVisibility(View.VISIBLE);
            btnSelfAuth.setText("전자지갑 복원 완료");
            tvErrorMsg.setText("전자지갑 비밀번호가 일치하지 않습니다.");
            tvErrorMsg.setVisibility(View.VISIBLE);
        } else {
            llBefor.setVisibility(View.VISIBLE);
            llAfter.setVisibility(View.GONE);
            btnSelfAuth.setText("전자지갑 복원 진행");
        }


        btnSelfAuth.setOnClickListener(v -> {
            if (btnSelfAuth.getText().toString().equals("전자지갑 복원 진행")) {
                llBefor.setVisibility(View.GONE);
                identityVerificationFragment = new IdentityVerificationFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.fl_identity_verification, identityVerificationFragment).commit();
            } else if (btnSelfAuth.getText().toString().equals("전자지갑 복원 완료")) {
                if (etPw.getText().toString().trim().length() == 6) {
                    getIntent().putExtra("walletPw", ((EditText) findViewById(R.id.inputWalletPw)).getText().toString());
                    setResult(RESULT_OK, getIntent());
                    finish();
                } else {
                    tvErrorMsg.setText("비밀번호 6자리를 입력하세요.");
                    tvErrorMsg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void identityVerificationSuccess() {
        getSupportFragmentManager().beginTransaction().hide(identityVerificationFragment).commit();
        new Thread(() -> runOnUiThread(() -> {
            btnSelfAuth.setText("전자지갑 복원 완료");
            llAfter.setVisibility(View.VISIBLE);
        })).start();
    }

    @Override
    public void identityVerificationFail() {
        tvErrorMsg.setText("비밀번호가 일치하지 않습니다.");
        tvErrorMsg.setVisibility(View.VISIBLE);
//        setResult(RESULT_CANCELED);
//        finish();
    }
}
