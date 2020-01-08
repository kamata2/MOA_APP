package com.moaPlatform.moa.agreement_to_terms_of_use.model.view_model;

import android.widget.CompoundButton;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.data_binding.CustomSafeBox;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 이용약관 동의 화면의 뷰 모델
 */
public class AgreementToTermsOfUseViewModel extends ViewModel{

    private final String TAG = AgreementToTermsOfUseViewModel.class.getCanonicalName();

    public MutableLiveData<Boolean> allAgree = new MutableLiveData<>();
    public MutableLiveData<Boolean> gpsAgree = new MutableLiveData<>();
    public MutableLiveData<Boolean> eventAgree = new MutableLiveData<>();

    public AgreementToTermsOfUseViewModel() {
        allAgree.setValue(false);
        gpsAgree.setValue(false);
        eventAgree.setValue(false);
    }

    /**
     * 체크박스 클릭 (상태값 변경시)
     * @param bt
     * 컴포넌트 버튼
     * @param st
     * 체크 유무 -> true : 체크, false : 체크안됨
     */
    public void onCheckChange(CompoundButton bt, boolean st) {
        switch (bt.getId()) {
            case R.id.checkBoxGpsAgree :
                gpsAgree.setValue(st);
                break;
            case R.id.checkBoxEventAgree :
                eventAgree.setValue(st);
                break;
            case R.id.allCheckBox :
                Logger.i("onCheckChange allCheck : ");
                break;
        }

        if (CustomSafeBox.unBox(gpsAgree.getValue()) && CustomSafeBox.unBox(eventAgree.getValue())) {
            allAgree.setValue(true);
        } else {
            allAgree.setValue(false);
        }
    }

}
