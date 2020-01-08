package com.moaPlatform.moa.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.controller.IdentityVerificationFragment;
import com.moaPlatform.moa.util.interfaces.Identifiable;

import java.util.List;

public class MoaPayPswInitActivity extends AppCompatActivity implements PaymentResultsReceiver, Identifiable {
    private ProgressBar progressBar;
    private IdentityVerificationFragment identityVerificationFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moa_pay_psw_init);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        identityVerificationFragment = new IdentityVerificationFragment();
        fragmentManager = getSupportFragmentManager();

        ServerSideMoaPayController serverSideMoaPayController = new ServerSideMoaPayController(this);
        serverSideMoaPayController.init(this);
        serverSideMoaPayController.initializePayPsw(getIntent().getStringExtra("initPsw"));
    }

    @Override
    public void onLoadCompleteCardList(List<CardListItem> cardListItems) {
    }

    @Override
    public void onReadyJsonData(String jsonData) {
    }

    @Override
    public void onSuccessPayment() {
    }

    @Override
    public void onFailPayment(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onForwardInitPswNonce(String nonce) {
        progressBar.setVisibility(View.GONE);
        showKGMobilianceContainingNonce(nonce);
    }

    @Override
    public void onNotMatchedPw() {
    }

    @Override
    public void identityVerificationSuccess() {
        Toast.makeText(this, "비밀번호 초기화 성공", Toast.LENGTH_SHORT).show();
        fragmentManager.beginTransaction().hide(identityVerificationFragment).commit();
        finish();
    }

    @Override
    public void identityVerificationFail() {
        Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show();
    }

    private void showKGMobilianceContainingNonce(String nonce) {
        Bundle bundle = new Bundle();
        bundle.putString("nonce", nonce);

        identityVerificationFragment.setArguments(bundle);
        fragmentManager.beginTransaction().add(R.id.fl_identity_verification, identityVerificationFragment).commit();
    }
}
