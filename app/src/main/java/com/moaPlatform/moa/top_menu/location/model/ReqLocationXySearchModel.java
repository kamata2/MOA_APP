package com.moaPlatform.moa.top_menu.location.model;

/**
 * 주소의 xy 좌표 조회하는 req 모델
 */
public class ReqLocationXySearchModel {
    // 키 값
    public final String key = "U01TX0FVVEgyMDE4MTIyMDEyNTY1MTEwODM5MzM=";
    // 검색 결과 형식
    public final String resultType = "json";
    // 행정구역 코드
    public String admCd;
    // 도로명 코드
    public String rnMgtSn;
    // 지하 여부
    public String isUnderground;
    // 건물 본번
    public int buildingNumber;
    // 건물 부번
    public int buildingSubCode;

    public void init(ResAddressSearchBaseModel.AddressModel addressModel) {
        this.admCd = addressModel.admCd;
        this.rnMgtSn = addressModel.rnMgtSn;
        this.isUnderground = addressModel.isUnderground;
        this.buildingNumber = addressModel.buildingNumber;
        this.buildingSubCode = addressModel.buildingSubCode;
    }
}
