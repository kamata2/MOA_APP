package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view;

import android.content.Intent;
import android.os.Bundle;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.controller.IdentityVerificationFragment;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.HttpConnectionResult;
import com.moaPlatform.moa.util.interfaces.Identifiable;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.singleton.App;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * 전화번호 인증화면 웹뷰
 */
public class IdentityActivity extends AppCompatActivity implements HttpConnectionResult, Identifiable {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_up_activity);
        IdentityVerificationFragment identityVerificationFragment = new IdentityVerificationFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment, identityVerificationFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("requestCode " + requestCode + " resultCode " + resultCode);

        if(requestCode == MoaConstants.REQUEST_IDENTITY_ACTIVITY){
            if(resultCode == MoaConstants.RESULT_CARDREGISTER_SUCCESS){
                setResult(MoaConstants.RESULT_CARDREGISTER_SUCCESS);
                finish();
            }else if(resultCode == MoaConstants.RESULT_CARDREGISTER_FAIL){
                finish();
            }else if(resultCode == MoaConstants.RESULT_CARDREGISTER_CANCEL){
                //패스워드 입력 화면에서 Back키 수행시
                finish();
            }
        }
    }

    @Override
    public void identityVerificationSuccess() {
        Logger.e("identityVerificationSuccess()");
            //startActivity(new Intent(IdentityActivity.this, WalletSettingPWActivity.class));
        Intent intent = new Intent(IdentityActivity.this, WalletSettingPWActivity.class);
        intent.putExtra(MoaConstants.EXTRA_TO_VIEW, WalletSettingPWActivity.EXTRA_TO_VIEW_WALLET_ACTIVITY);
        intent.putExtra(MoaConstants.EXTRA_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT, getIntent().getBooleanExtra(MoaConstants.EXTRA_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT, false));
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, MoaConstants.REQUEST_IDENTITY_ACTIVITY);
    }

    @Override
    public void identityVerificationFail() {
        Logger.e("identityVerificationFail()");
        if(!isFinishing()){
            finish();
        }
    }

    @Override
    public void onHttpConnectionSuccess(int type, BaseModel baseModel) {
        Logger.i("zxcxz " + "onSuccess type asdas: " + type);
        if (type == App.getInstance().SIGN_UP_SUCCESS) {

        }
    }
}