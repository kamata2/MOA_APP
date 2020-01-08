package com.moaPlatform.moa.side_menu.settings.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.controller.IdentityVerificationFragment;
import com.moaPlatform.moa.side_menu.settings.account.model.AccountSettingInfoDataModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.auth.UserWalletHelper;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.interfaces.Identifiable;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 계정 설정 화면
 * 본인 인증 시, instanceOf를 사용하기 위하여 Identifiable 을 implements 함 (구현은 하지 않음)
 */
public class AccountSettingActivity extends AppCompatActivity implements Identifiable {

    // 이메일 수신 동의, SMS 수신 동의 체크박스
    private CheckBox cbEmailReceive, cbSmsReceive;
    private AccountSettingInfoDataModel.AccountSettingReceiveModel reqAccountReceiveModel;
    private FragmentManager fragmentManager;
    private Fragment identityVerificationFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting_activity);
        initView();
        getAccountInfo();
    }

    /**
     * 뷰 초기화
     */
    private void initView() {
        // 툴바  세팅
        CommonTitleView topToolbar = findViewById(R.id.topToolbar);
        topToolbar.setTitleName(getString(R.string.account_setting));
        topToolbar.setBackButtonClickListener(v -> finish());
        // 비밀번호 변경 작업
        Button btnPasswordChange = findViewById(R.id.btnPasswordChange);
        btnPasswordChange.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PasswordChangeActivity.class);
            startActivity(intent);
        });
        // 휴대폰 번호 변경
        Button btnPhoneNumberChange = findViewById(R.id.btnUserPhoneNumberChange);
        btnPhoneNumberChange.setOnClickListener(v -> phoneNumberChange());
        // 이메일 수신 동의 체크박스
        cbEmailReceive = findViewById(R.id.cbEmailReceive);
        cbEmailReceive.setOnClickListener(v -> {
            reqAccountReceiveModel.setIsEmailReceive(cbEmailReceive.isChecked());
            receiveEmailAndSmsChange(ReceiveType.EMAIL);
        });
        // SMS 수신 동의 체크박스
        cbSmsReceive = findViewById(R.id.cbSmsReceive);
        cbSmsReceive.setOnClickListener(v -> {
            reqAccountReceiveModel.setIsSmsReceive(cbSmsReceive.isChecked());
            receiveEmailAndSmsChange(ReceiveType.SMS);
        });
    }

    /**
     * 사용자의 계정 정보 세팅
     *
     * @param accountSettingInfoModel 서버에서 받은 데이터
     */
    private void userAccountInfoSetting(AccountSettingInfoDataModel accountSettingInfoModel) {
        if (accountSettingInfoModel == null)
            return;
        // 이메일, 휴대전화, 이메일 및 SMS 수신 여부 초기화
        ((TextView) findViewById(R.id.tvUserEmail)).setText(accountSettingInfoModel.getEmail());
        ((EditText) findViewById(R.id.etUserPhoneNumber)).setText(accountSettingInfoModel.getPhoneNumber());
        cbEmailReceive.setChecked(accountSettingInfoModel.isEmailReceive());
        cbSmsReceive.setChecked(accountSettingInfoModel.isSmsReceive());
        // 이메일 및 SMS 수신 여부를 수정 할때 사용할 req 모델 초기화
        reqAccountReceiveModel = new AccountSettingInfoDataModel().new AccountSettingReceiveModel();
        reqAccountReceiveModel.init(accountSettingInfoModel.isEmailReceive(), accountSettingInfoModel.isSmsReceive());
    }

    /**
     * 이메일 및 SMS 수신 동의 여부 변경
     */
    private void receiveEmailAndSmsChange(ReceiveType receiveType) {
        RetrofitClient.getInstance().getMoaService().accountSettingReceiveChange(reqAccountReceiveModel).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    receiveEmailAndSmsChange(receiveType);
                    return;
                }
                if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    // 수신 동의 여부 변경 성공시 Toast 메시지 띄움
                    switch (receiveType) {
                        case EMAIL:
                            Toast.makeText(getApplicationContext(), getString(R.string.account_setting_activity_receive_success, getToDay(), "이메일", (reqAccountReceiveModel.isEmailReceive() ? "동의" : "거부")), Toast.LENGTH_SHORT).show();
                            break;
                        case SMS:
                            Toast.makeText(getApplicationContext(), getString(R.string.account_setting_activity_receive_success, getToDay(), "SMS", (reqAccountReceiveModel.isSmsReceive() ? "동의" : "거부")), Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    // 수신 동의 여부 실패시 reqAccountReceiveModel 및 ui 롤백

                    reqReceiveRollback(receiveType);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                // 수신 동의 여부 실패시 reqAccountReceiveModel 및 ui 롤백
                reqReceiveRollback(receiveType);
                Logger.e("receiveEmailAndSmsChange : " + t.getMessage());
            }
        });
    }

    /**
     * 서버와 통신했을때 롤백 처리
     *
     * @param receiveType 이메일인지 SMS 인지 체크할수있는 값
     */
    private void reqReceiveRollback(ReceiveType receiveType) {
        switch (receiveType) {
            case EMAIL:
                reqAccountReceiveModel.setIsEmailReceive(!reqAccountReceiveModel.isEmailReceive());
                cbEmailReceive.setChecked(!reqAccountReceiveModel.isEmailReceive());
                break;
            case SMS:
                reqAccountReceiveModel.setIsSmsReceive(!reqAccountReceiveModel.isSmsReceive());
                cbSmsReceive.setChecked(!reqAccountReceiveModel.isSmsReceive());
                break;
        }
    }

    /**
     * 오늘 날짜 구하기
     *
     * @return 오늘 날짜 반환
     */
    public String getToDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * 계정설정정보 받아오기
     */
    public void getAccountInfo() {
        RetrofitClient.getInstance().getMoaService().accountSettingInfoSearch(new CommonModel()).enqueue(new Callback<AccountSettingInfoDataModel>() {
            @Override
            public void onResponse(@NonNull Call<AccountSettingInfoDataModel> call, @NonNull Response<AccountSettingInfoDataModel> response) {

                if(!isFinishing()){
                    try{
                        AccountSettingInfoDataModel accountSettingInfoDataModel = response.body();
                        if (accountSettingInfoDataModel == null)
                            return;
                        if (RetrofitClient.getInstance().hasReissuedAccessToken(accountSettingInfoDataModel)) {
                            getAccountInfo();
                            return;
                        }
                        userAccountInfoSetting(accountSettingInfoDataModel);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountSettingInfoDataModel> call, @NonNull Throwable t) {
                Logger.e("getAccountInfo 계정 정보 조회 에러 : " + t.getMessage());
            }
        });
    }

    /**
     * 핸드폰 번호 변경
     */
    private void phoneNumberChange() {
        FrameLayout viewPhoneNumberChange = findViewById(R.id.viewPhoneNumberChange);
        viewPhoneNumberChange.setVisibility(View.VISIBLE);
        identityVerificationFragment = new IdentityVerificationFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.viewPhoneNumberChange, identityVerificationFragment).commit();
    }

    @Override
    public void identityVerificationSuccess() {
        fragmentManager.beginTransaction().remove(identityVerificationFragment).commit();
        runOnUiThread(() -> {
            FrameLayout viewPhoneNumberChange = findViewById(R.id.viewPhoneNumberChange);
            viewPhoneNumberChange.setVisibility(View.GONE);
            Toast.makeText(this, "휴대전화 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
        });
        getAccountInfo();
    }

    @Override
    public void identityVerificationFail() {
        fragmentManager.beginTransaction().remove(identityVerificationFragment).commit();
        runOnUiThread(() -> {
            FrameLayout viewPhoneNumberChange = findViewById(R.id.viewPhoneNumberChange);
            viewPhoneNumberChange.setVisibility(View.GONE);
        });
    }

    private enum ReceiveType {
        EMAIL, SMS
    }

    public void initWalletPsw(View v) {
        UserWalletHelper userWalletHelper = new UserWalletHelper(this);
        userWalletHelper.init(findViewById(R.id.wv_init_psw));
        userWalletHelper.startInitWalletPsw(MoaAuthHelper.getInstance().getCurrentMemberID());
    }
}
