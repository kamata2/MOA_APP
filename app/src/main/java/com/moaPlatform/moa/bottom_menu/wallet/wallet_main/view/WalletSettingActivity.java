package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.MoaPayAgrmnt;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * GOEAT PAY 설정 화면
 */
public class WalletSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlPwSearch, rlPwChange, rlExpireService;
    private TextView tvAddCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_setting_activity);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void initView() {
        rlPwSearch = findViewById(R.id.pay_pw_search);
        rlPwChange = findViewById(R.id.pay_pw_change);
        rlExpireService = findViewById(R.id.pay_expire);
        tvAddCard = findViewById(R.id.set_cardadd);

        rlPwSearch.setOnClickListener(this);
        rlPwChange.setOnClickListener(this);
        tvAddCard.setOnClickListener(this);

        //20190708 임실장님 숨기기 요청.
        rlExpireService.setVisibility(View.GONE);

//        ImageButton btnFinish = findViewById(R.id.btnBack);
//        btnFinish.setOnClickListener(v -> finish());

        CommonTitleView top_tolbar = findViewById(R.id.top_tolbar);
        top_tolbar.setBackButtonClickListener(v -> finish());
        top_tolbar.setTitleName("GOEAT PAY 설정");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_cardadd:
                addCard();
                break;
            case R.id.pay_pw_search://결제 비밀번호 찾기 //초기화 //
                startActivity(new Intent(WalletSettingActivity.this, PaymentSearchPasswordActivity.class));
                break;
            case R.id.pay_pw_change://비밀번호 변경
                startActivity(new Intent(WalletSettingActivity.this, PaymentResetPasswordActivity.class));
                break;
            case R.id.pay_expire:
                break;
        }
    }

    /**
     * 카드 추가
     */
    private void addCard() {
        String memberId = MoaAuthHelper.getInstance().getCurrentMemberID();
        //로그인이면..
        if (memberId.contains("@")) {
            MoaPayAgrmnt moaPayAgrmnt = new MoaPayAgrmnt();
            RetrofitClient.getInstance().getMoaService().getMoaPayAgrmnt(moaPayAgrmnt).enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                    CommonModel commonModel = response.body();
                    if (commonModel == null)
                        return;
                    if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                        addCard();
                        return;
                    }
                    if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                        //약관 동의 화면으로 이동
                        Intent intent = new Intent(WalletSettingActivity.this, WalletAgreeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        //인증화면으로 이동
                        Intent intent = new Intent(WalletSettingActivity.this, IdentityActivity.class);
                        intent.putExtra(MoaConstants.EXTRA_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT, true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(intent, MoaConstants.REQUEST_WALLET_ACTIVITY);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                    Toast.makeText(WalletSettingActivity.this, getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Logger.d("requestCode " + requestCode + " resultCode " + resultCode);
        if (resultCode == MoaConstants.RESULT_CARDREGISTER_SUCCESS) {
            setResult(MoaConstants.RESULT_CARDREGISTER_SUCCESS);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

