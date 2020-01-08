package com.moaPlatform.moa.top_menu.location.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;

public class ResAddressSearchBaseModel extends CommonModel {
    // 결과값
    @SerializedName("results")
    private ResultModel resultModel;

    @SerializedName("userAddrList")
    private ArrayList<AddressHistoryModel> addressHistroyModelArrayList;

    private class ResultModel {
        @SerializedName("juso")
        private ArrayList<AddressModel> addressModels;
    }

    /**
     * 주소 정보를 저장하는 모델
     */
    public class AddressModel extends BaseModel{
        // 전체 도로명주소
        @SerializedName("roadAddr")
        public String fullRoadAddress;
        // 도로명주소 (참고항목 제외)
        @SerializedName("roadAddrPart1")
        private String roadAddress;
        // 지번주소
        @SerializedName("jibunAddr")
        public String jibunAddress;
        // 건물관리번호
        @SerializedName("bdMgtSn")
        public String bdMgtSn;
        // 행정구역 코드
        @SerializedName("admCd")
        public String admCd;
        // 도로명 코드
        @SerializedName("rnMgtSn")
        public String rnMgtSn;
        // 지하 여부
        @SerializedName("udrtYn")
        public String isUnderground;
        // 건물 본번
        @SerializedName("buldMnnm")
        public int buildingNumber;
        // 건물 부번
        @SerializedName("buldSlno")
        public int buildingSubCode;
        // 우편 번호
        @SerializedName("zipNo")
        public String zipNo;
        // 동
        @SerializedName("emdNm")
        public String emdNm;

        // x좌표
        @SerializedName("entX")
        public Double locationX;
        // y좌표
        @SerializedName("entY")
        public Double locationY;
    }

    /**
     * 최근 주소 및 기본 주소 저장하는 모델
     */
    public class AddressHistoryModel extends CommonModel {
        // 주소 id
        @SerializedName("userAddrLstId")
        private int addressId;
        // 기본 주소 여부
        @SerializedName("defltAddrYn")
        public String isDefaultAddess;
        // 우편번호
        @SerializedName("postNum")
        public String postNumber;
        // 도로명 기본 주소
        @SerializedName("roadNmDefltAddr")
        public String roadDefaultAddress;
        // 도로명 상세 주소
        @SerializedName("roadNmDtlAddr")
        public String roadDetailAddress;
        // 지번 주소
        @SerializedName("landNumAddr")
        public String jibunAddress;
        // 경도
        @SerializedName("lont")
        public String lnt;
        // 위도
        @SerializedName("latu")
        public String lat;

    }

    /**
     * 주소의 위도, 경도 값을 저장하는 모델
     */
    public class LocationCoordinate extends CommonModel{
        // 경도
        @SerializedName("lnt")
        public Double lnt;
        // 위도
        @SerializedName("lat")
        public Double lat;
    }

    public ArrayList<AddressModel> getAddressModel() {
        return resultModel.addressModels;
    }
    public LocationCoordinate getlocationCoordinate() {
        return new LocationCoordinate();
    }

    public ArrayList<AddressHistoryModel> getAddressHistroyModelArrayList() {
        return addressHistroyModelArrayList;
    }
}
