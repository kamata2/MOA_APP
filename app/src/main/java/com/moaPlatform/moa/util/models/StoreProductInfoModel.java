package com.moaPlatform.moa.util.models;

import com.google.gson.annotations.SerializedName;

public class StoreProductInfoModel extends BaseModel{
    // 상품 코드
    @SerializedName("storProdCd")
    public int productCode;
    // 상품 이름
    @SerializedName("storProdNm")
    public String productName;
    // 상품 설명
    @SerializedName("prodExpn")
    public String productExplain;
    // 상품 가격
    @SerializedName("storProdPrice")
    public int productPrice;
    // 상품 이미지
    @SerializedName("imageUrl")
    public String productThumbNail;
    // 타임 이벤트 목록
    @SerializedName("timeEvntInfo")
    public TimeEventInfo timeEventModel;
    // 배달 무료 금액 (해당 금액 이상의 주문을 했을시에는 배달료 0원)
    @SerializedName("freeDlvryAmt")
    public int freeDeliveryPrice;
    // 최소 주문 금액
    @SerializedName("miniOrderAmt")
    public int minOrderPrice;

    // 서비스 유무 (1 : 오픈, 0 : 닫기)
    @SerializedName("serviceYn")
    public int isOpen;
    // 매장 전화번호
    @SerializedName("storPhonNum")
    public String storePhoneNumber;

    public class TimeEventInfo {
        // 타임 이벤트 id
        @SerializedName("timeEvntId")
        private String timeEventId;
        // 타임 이벤트 타입 ( 0 : 배달, 1 ㅣ 테이크 아웃)
        @SerializedName("timeEvntTp")
        public String timeEventType;
        // 할인 문구
        @SerializedName("fixAmtCntnt")
        public String saleContent;
        // 할인 문구
        @SerializedName("fixRateCntnt")
        public String saleRateContent;
        // 추가 적립 문구
        @SerializedName("addSaveRateCntnt")
        public String addSaveRateContent;
        // 할인 금액
        @SerializedName("fixAmt")
        public String fixAmt;
        // 적률 할인
        @SerializedName("fixRate")
        public int fixRate;
        // 추가 적립 퍼센트
        @SerializedName("addSaveRate")
        public int addSaveRate;
        // 최대 할인 금액
        @SerializedName("maxDscntAmt")
        public int maxSalePrice;
    }
}
