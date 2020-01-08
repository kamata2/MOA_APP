package com.moaPlatform.moa.auth.sign_up.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.FingerprintUiHelper;
import com.moaPlatform.moa.util.auth.ServerSideAuth;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;


public class LogInFingerActivity extends AppCompatActivity implements View.OnClickListener {

    private NotiFingerDialog notiFingerDialog;
    //    private SetFingerDialog setFingerDialog;
    private Button btnCancel, btnRetry;
    private ServerSideAuth serverSideAuth = ServerSideAuth.getInstance();

    private View.OnClickListener noListener = new View.OnClickListener() {
        public void onClick(View v) {
            notiFingerDialog.dismiss();
        }
    };

    private View.OnClickListener yesListener = new View.OnClickListener() {
        public void onClick(View v) {
            notiFingerDialog.dismiss();
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                intent = new Intent(Settings.ACTION_FINGERPRINT_ENROLL);
            } else {
                intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            }
            startActivityForResult(intent, 0);
        }
    };

//    private View.OnClickListener cancelListener = new View.OnClickListener() {
//        public void onClick(View v) {
//            setFingerDialog.dismiss();
//        }
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login_finger);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        serverSideAuth.setContext(this);
        FingerprintUiHelper fingerprintUiHelper = new FingerprintUiHelper(FingerprintManagerCompat.from(getApplication()));
        if (!fingerprintUiHelper.ishasEnrolledFingerprints()) {
            notiFingerDialog = new NotiFingerDialog(this, noListener, yesListener);
            notiFingerDialog.show();
        } else {
            serverSideAuth.checkRegisterFingerprint();
        }
    }

    private void initView() {
        btnCancel = findViewById(R.id.fingerlogin_cancel);
        btnRetry = findViewById(R.id.fingerlogin_retry);

        btnCancel.setOnClickListener(this);
        btnRetry.setOnClickListener(this);

        CommonTitleView commonTitleView = findViewById(R.id.commontitle);
        commonTitleView.setOnClickListener(v -> finish());
        commonTitleView.setTitleName(getString(R.string.finger_print_set));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fingerlogin_cancel:
                finish();
                break;
            case R.id.fingerlogin_retry:
                serverSideAuth.checkRegisterFingerprint();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //onResume 에서 처리
        switch (requestCode) {
            case 0:
                FingerprintUiHelper fingerprintUiHelper = new FingerprintUiHelper(FingerprintManagerCompat.from(getApplication()));
                if (!fingerprintUiHelper.ishasEnrolledFingerprints()) {
                } else {
//                    setFingerDialog = new SetFingerDialog(this, cancelListener);
//                    serverSideAuth.checkRegisterFingerprint();
//                    Intent intent = new Intent(this, LogInActivity.class);
//                    intent.putExtra("finger","yes");
//                    startActivity(intent);
//                    finish();
                }
//                serverSideAuth.checkRegisterFingerprint();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRegisterFingerprint(String base64AuthToken) {
        serverSideAuth.registerFingerprint(base64AuthToken);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onLoginFingerprint(String nonceOTP, String base64AuthToken) {
        serverSideAuth.doLoginFingerprint(nonceOTP, base64AuthToken);
    }

}
