package com.moaPlatform.moa.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.activity.EventWebViewActivity;
import com.moaPlatform.moa.constants.MoaUrlConstants;
import com.moaPlatform.moa.intro.model.ReqAgreementInfoData;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.ClassConnectInterface;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.App;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgreementToTermsOfUseFragment extends Fragment {
    // 버터나이프 해제때 사용
    private Unbinder unbinder;
    private ClassConnectInterface classConnectInterface;
    @BindView(R.id.checkBoxGpsAgree)
    CheckBox gpsCheckbox;
    @BindView(R.id.checkBoxEventAgree)
    CheckBox eventCheckbox;
    @BindView(R.id.allCheckBox)
    CheckBox allCheckbox;
    @BindView(R.id.done)
    Button btnDone;
    private View view;

    public void setClassConnectInterface(ClassConnectInterface classConnectInterface) {
        this.classConnectInterface = classConnectInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.agreement_to_terms_of_use_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        btnDoneSetting();
        return view;
    }

    /**
     * 초기화
     */
    private void init() {
        View tvLocationAgreementAllRead = view.findViewById(R.id.tvLocationAgreementAllRead);
        tvLocationAgreementAllRead.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EventWebViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, BuildConfig.REST_API_BASE_URL + MoaUrlConstants.LOCATION_TERMS_AND_CONDITIONS_URL);
            intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_TRANSPARENT_VALUE);
            startActivity(intent);
        });
    }

    /**
     * 체크박스 체크시 이벤트
     */
    @OnClick({R.id.checkBoxGpsAgree, R.id.checkBoxEventAgree, R.id.allCheckBox, R.id.done})
    void checkboxChecked(View view) {
        switch (view.getId()) {
            case R.id.done:
                btnDoneStart();
                break;
            case R.id.allCheckBox:
                allCheck(allCheckbox.isChecked());
                break;
            default:
                allCheck();
                break;
        }
        btnDoneSetting();
    }

    /**
     * 위치 및 이벤트 체크박스가 전부 활성화면 전체 동의 체크박스 활성화
     */
    private void allCheck() {
        allCheckbox.setChecked(gpsCheckbox.isChecked() && eventCheckbox.isChecked());
    }

    /**
     * 전체체크박스 클릭시 해당 체크박스의 상태값에 따라 위치 및 이벤트 체크박스 상태 변경
     *
     * @param state 상태값
     */
    private void allCheck(boolean state) {
        gpsCheckbox.setChecked(state);
        eventCheckbox.setChecked(state);
    }

    /**
     * 약관동의 완료 버튼의 세팅
     */
    private void btnDoneSetting() {
        btnDone.setClickable(gpsCheckbox.isChecked());
        btnDone.setBackgroundColor(getResources().getColor(btnDone.isClickable() ? R.color.coral : R.color.darkGray));
    }

    /**
     * @return 약관 동의시 서버에 전송할 req 모델 반환
     */
    private ReqAgreementInfoData getReqAgreementModel() {
        ReqAgreementInfoData reqModel = new ReqAgreementInfoData();
        reqModel.setData(gpsCheckbox.isChecked(), eventCheckbox.isChecked());
        return reqModel;
    }

    /**
     * 완료 버튼 클릭 시
     * 이곳에서만 서버와 통신하므로 controller 로 따로 분리할 필요 없을거라고 판단
     */
    private void btnDoneStart() {

        RetrofitClient.getInstance().getMoaService().postAgreementData(getReqAgreementModel()).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null || classConnectInterface == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    btnDoneStart();
                    return;
                }
                if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    Logger.d("btnDoneStart res data : " + App.getInstance().gson.toJson(commonModel));
                    classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.INTRO_USE_AGREEMENT);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * 프레그먼트일경우 destroy 일때 ButterKnife bind 해제
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
