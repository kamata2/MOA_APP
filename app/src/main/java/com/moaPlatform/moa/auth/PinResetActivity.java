package com.moaPlatform.moa.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.controller.IdentityVerificationFragment;
import com.moaPlatform.moa.util.interfaces.Identifiable;

public class PinResetActivity extends AppCompatActivity implements Identifiable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_reset);

        IdentityVerificationFragment identityVerificationFragment = new IdentityVerificationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("email", getIntent().getStringExtra("email"));
        identityVerificationFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_identity_verification, identityVerificationFragment).commit();
    }

    @Override
    public void identityVerificationSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void identityVerificationFail() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
