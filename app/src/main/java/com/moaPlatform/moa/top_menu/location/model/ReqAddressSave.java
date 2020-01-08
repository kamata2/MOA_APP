package com.moaPlatform.moa.top_menu.location.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqAddressSave extends CommonModel {
    // 기본주소여부
    @SerializedName("defltAddrYn")
    private String defltAddrYn = "Y";
    // 우편번호
    @SerializedName("postNum")
    private String zipNo;
    // 도로명 기본 주소
    @SerializedName("roadNmDefltAddr")
    private String fullRoadAddress;
    // 도로명 상세 주소
    @SerializedName("roadNmDtlAddr")
    private String roadNmDtlAddr;
    // 지번 주소
    @SerializedName("landNumAddr")
    private String jibunAddress;
    // 경도
    @SerializedName("lont")
    private String lnt;
    // 위도
    @SerializedName("latu")
    private String lat;
    // 주소(동)
    @SerializedName("dongNm")
    private String emdNm;

    public void init(String detailAddress, ResAddressSearchBaseModel.AddressModel addressModel, ResAddressSearchBaseModel.LocationCoordinate locationCoordinate) {
        if (detailAddress == null || addressModel == null || locationCoordinate == null)
            return;
        zipNo = addressModel.zipNo;
        jibunAddress = addressModel.jibunAddress;
        fullRoadAddress = addressModel.fullRoadAddress;
        emdNm = addressModel.emdNm;
        roadNmDtlAddr = detailAddress;
        lnt = String.valueOf(locationCoordinate.lnt);
        lat = String.valueOf(locationCoordinate.lat);
    }
}
