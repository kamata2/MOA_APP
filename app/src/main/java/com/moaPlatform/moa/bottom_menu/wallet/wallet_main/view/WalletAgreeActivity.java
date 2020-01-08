package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.activity.EventWebViewActivity;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.constants.MoaUrlConstants;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class WalletAgreeActivity extends AppCompatActivity implements View.OnClickListener {


    private CheckBox checkAgree, checkAgree2, checkAgree3, checkAgree4, checkAll;
    private TextView tvAgree, tvAgree2, tvAgree3, tvAgree4, tvAgree5;
    private Button btnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreement_to_terms_moapay);
        init();
    }

    /**
     * 변수 초기회
     */
    private void init() {
        checkAll = findViewById(R.id.allCheckBoxs);
        checkAgree = findViewById(R.id.checkBoxAgree);
        checkAgree2 = findViewById(R.id.checkBoxAgree2);
        checkAgree3 = findViewById(R.id.checkBoxAgree3);
        checkAgree4 = findViewById(R.id.checkBoxAgree4);
        tvAgree = findViewById(R.id.textViewAgree);
        tvAgree2 = findViewById(R.id.textViewAgree2);
        tvAgree3 = findViewById(R.id.textViewAgree3);
        tvAgree4 = findViewById(R.id.textViewAgree4);

        checkAll.setOnClickListener(this);
        checkAgree.setOnClickListener(this);
        checkAgree2.setOnClickListener(this);
        checkAgree3.setOnClickListener(this);
        checkAgree4.setOnClickListener(this);

        btnNext = findViewById(R.id.gonextpage);
        btnNext.setOnClickListener(this);

        CommonTitleView commonTitleView = findViewById(R.id.commontitle);
        commonTitleView.setOnClickListener(v -> finish());
        commonTitleView.setTitleName(getString(R.string.pay_join_agreement));
        commonTitleView.setClosedButtonType();

        tvAgree.setOnClickListener(v -> webViewMove(MoaUrlConstants.GO_EAT_PAY_USE_TERMS_AND_CONDITIONS_URL));
        tvAgree2.setOnClickListener(v -> webViewMove(MoaUrlConstants.ACCEPT_ELECTRONIC_FINANCIAL_TRANSACTION_TERMS_AND_CONDITIONS));
        tvAgree3.setOnClickListener(v -> webViewMove(MoaUrlConstants.AGREE_TO_COLLECT_AND_USE_PERSONAL_INFOMATION_URL));
        tvAgree4.setOnClickListener(v -> webViewMove(MoaUrlConstants.AGEE_TO_PROVIDE_PRESONAL_INFOMATION_TO_THIRD_PARTIES));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gonextpage:
                moveIdentity();
                break;
            case R.id.allCheckBoxs:
                allCheck(checkAll.isChecked());
                btnDoneSetting();
                break;
            default:
                allCheck();
                btnDoneSetting();
                break;
        }
    }

    private void btnDoneSetting() {
        btnNext.setEnabled(checkAgree.isChecked()
                && checkAgree2.isChecked()
                && checkAgree3.isChecked()
                && checkAgree4.isChecked()
        );
        btnNext.setBackgroundColor(getResources().getColor(btnNext.isEnabled() ? R.color.coral : R.color.darkGray));
    }

    private void allCheck() {
        checkAll.setChecked(checkAgree.isChecked() && checkAgree2.isChecked()
                && checkAgree3.isChecked()
                && checkAgree4.isChecked());
    }

    private void allCheck(boolean state) {
        checkAgree.setChecked(state);
        checkAgree2.setChecked(state);
        checkAgree3.setChecked(state);
        checkAgree4.setChecked(state);
    }

    private void moveIdentity() {
        Intent intent = new Intent(WalletAgreeActivity.this, IdentityActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, MoaConstants.REQUEST_IDENTITY_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MoaConstants.REQUEST_IDENTITY_ACTIVITY){
            if (resultCode == MoaConstants.RESULT_CARDREGISTER_SUCCESS) {
                setResult(MoaConstants.RESULT_CARDREGISTER_SUCCESS);
                finish();
            }
        }
    }

    private void webViewMove(String subUrl) {
        Intent intent = new Intent(getApplicationContext(), EventWebViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, BuildConfig.REST_API_BASE_URL + subUrl);
        intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_TRANSPARENT_VALUE);
        startActivity(intent);
    }
}
