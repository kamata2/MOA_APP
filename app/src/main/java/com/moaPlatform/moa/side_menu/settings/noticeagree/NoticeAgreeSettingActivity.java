package com.moaPlatform.moa.side_menu.settings.noticeagree;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.settings.noticeagree.model.NoticeAgreeCheckItems;
import com.moaPlatform.moa.side_menu.settings.noticeagree.model.NoticeAgreeCheckModel;
import com.moaPlatform.moa.side_menu.settings.noticeagree.model.ResNoticeAgreeCheck;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import java.text.DateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeAgreeSettingActivity extends AppCompatActivity {

    @BindView(R.id.adInfoNoti)
    CheckBox couponCheckbox;
    @BindView(R.id.inqAnswerNoti)
    CheckBox questionCheckbox;
    @BindView(R.id.nightAdInfoNoti)
    CheckBox mannerCheckbox;
    @BindView(R.id.currentversion)
    TextView verOldTextView;
    @BindView(R.id.moappversion)
    TextView verTextView;
    ResNoticeAgreeCheck resNoticeAgreeCheck;
    private String dateYMD;
    private String information;
    private Integer questionType = 0;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.side_setting_alarm);
        ButterKnife.bind(this);
        init();
        requestSettingInfo();
    }

    private void init() {
        CommonTitleView commonTitleView = findViewById(R.id.commontitle);
        commonTitleView.setOnClickListener(v -> finish());
        commonTitleView.setTitleName(getString(R.string.side_notice_alarm));
    }

    @OnClick({R.id.adInfoNoti, R.id.inqAnswerNoti, R.id.nightAdInfoNoti})
    void checkboxChecked(View view) {
        switch (view.getId()) {
            case R.id.adInfoNoti:
                if (couponCheckbox.isChecked()) {
                    getDate();
                    Toast.makeText(this, getString(R.string.agreement_to_terms_of_use_event_push_agree, dateYMD), Toast.LENGTH_SHORT).show();
                    settingData();
                } else {
                    information = getString(R.string.agreement_to_terms_of_use_when_coupon_or_event_subscribe_rejected);
                    questionType = 1;
                    showDialog();
                }
                break;

            case R.id.inqAnswerNoti:
                if (questionCheckbox.isChecked()) {
                    getDate();
                    String msg = getString(R.string.agreement_to_terms_of_use_answer_push_agree, dateYMD);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    settingData();
                } else {
                    information = getString(R.string.agreement_to_terms_of_use_when_answer_subscribe_reject);
                    questionType = 2;
                    showDialog();
                }
                break;

            case R.id.nightAdInfoNoti:
                if (mannerCheckbox.isChecked()) {
                    getDate();
                    String msg = getString(R.string.agreement_to_terms_of_use_etiquette_push_agree, dateYMD);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    settingData();
                } else {
                    information = getString(R.string.agreement_to_terms_of_use_when_night_subscribe_reject);
                    questionType = 3;
                    showDialog();
                }
                break;
        }
    }

    void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.yes_or_no_dialog_title));
        builder.setMessage(information);
        builder.setPositiveButton(getString(R.string.yes_or_no_dialog_yes),
                (dialog, which) -> settingData());
        builder.setNegativeButton(getString(R.string.yes_or_no_dialog_no), (dialog, which) -> {
            if (questionType == 1) {
                couponCheckbox.setChecked(true);
            } else if (questionType == 2) {
                questionCheckbox.setChecked(true);
            } else if (questionType == 3) {
                mannerCheckbox.setChecked(true);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void getDate() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT);

        dateFormat.setTimeZone(cal.getTimeZone());
        dateYMD = dateFormat.format(cal.getTime());
    }

    private void requestSettingInfo() {
        NoticeAgreeCheckModel noticeAgreeCheckModel = new NoticeAgreeCheckModel();
        RetrofitClient.getInstance().getMoaService().getInfo(noticeAgreeCheckModel).enqueue(new Callback<ResNoticeAgreeCheck>() {

            @Override
            public void onResponse(@NonNull Call<ResNoticeAgreeCheck> call, @NonNull Response<ResNoticeAgreeCheck> response) {
                resNoticeAgreeCheck = response.body();
                if (resNoticeAgreeCheck == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resNoticeAgreeCheck)) {
                    requestSettingInfo();
                    return;
                }
                if (resNoticeAgreeCheck.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    setView();
                } else {
                    Toast.makeText(NoticeAgreeSettingActivity.this, getString(R.string.common_toast_msg_data_load_fail_at_server), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResNoticeAgreeCheck> call, @NonNull Throwable t) {
                Logger.d(t.getMessage());
                Toast.makeText(NoticeAgreeSettingActivity.this, getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void settingData() {
        NoticeAgreeCheckModel noticeAgreeCheckModel = new NoticeAgreeCheckModel();
        String adInfo = "N";
        String intAnsInfo = "N";
        String mannerInfo = "N";
        if (couponCheckbox.isChecked()) {
            adInfo = "Y";
        }
        if (questionCheckbox.isChecked()) {
            intAnsInfo = "Y";
        }
        if (mannerCheckbox.isChecked()) {
            mannerInfo = "Y";
        }
        noticeAgreeCheckModel.cpDscntEvntBnftNotiAgrmntYn = adInfo;
        noticeAgreeCheckModel.inqAnswNotiAgrmntYn = intAnsInfo;
        noticeAgreeCheckModel.nightAdInfoNotiAgrmntYn = mannerInfo;

        RetrofitClient.getInstance().getMoaService().setInfo(noticeAgreeCheckModel).enqueue(new Callback<ResNoticeAgreeCheck>() {

            @Override
            public void onResponse(Call<ResNoticeAgreeCheck> call, Response<ResNoticeAgreeCheck> response) {
            }

            @Override
            public void onFailure(Call<ResNoticeAgreeCheck> call, Throwable t) {
            }
        });

    }

    private void setView() {
        try{
            if (resNoticeAgreeCheck != null && resNoticeAgreeCheck.agrmntInfos != null) {

                NoticeAgreeCheckItems item = resNoticeAgreeCheck.agrmntInfos;

                if (ObjectUtil.checkNotNull(item.cpDscntEvntBnftNotiAgrmntYn)
                        && item.cpDscntEvntBnftNotiAgrmntYn.equals("Y")) {
                    couponCheckbox.setChecked(true);
                }
                if (ObjectUtil.checkNotNull(item.inqAnswNotiAgrmntYn)
                        && item.inqAnswNotiAgrmntYn.equals("Y")) {
                    questionCheckbox.setChecked(true);
                }
                if (ObjectUtil.checkNotNull(item.nightAdInfoNotiAgrmntYn)
                        && item.nightAdInfoNotiAgrmntYn.equals("Y")) {
                    mannerCheckbox.setChecked(true);
                }
                if(ObjectUtil.checkNotNull(item.appVer)){
                    verTextView.setText(getString(R.string.latest_version, item.appVer));
                }
            }
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            verOldTextView.setText(getString(R.string.current_version, packageInfo.versionName));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
