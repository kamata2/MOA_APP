package com.moaPlatform.moa.intro.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

/**
 * 앱 초기 진입시 이용약관 동의 시 전송할 데이터
 */
public class ReqAgreementInfoData extends CommonModel {
    // 위치기반 동의 여부
    @SerializedName("locBaseAgrmntYn")
    private String locationAgreementCheck;

    // 쿠폰할인 이벤트 혜택 알림 동의 여부
    @SerializedName("cpDscntEvntBnftNotiAgrmntYn")
    private String eventAgreementCheck;

    public void setData(boolean locationCheck, boolean eventAgreementCheck) {
        this.locationAgreementCheck = locationCheck ? "Y" : "N";
        this.eventAgreementCheck = eventAgreementCheck ? "Y" : "N";
    }
}
