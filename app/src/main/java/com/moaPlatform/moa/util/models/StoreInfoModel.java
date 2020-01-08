package com.moaPlatform.moa.util.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoreInfoModel extends CommonModel{
    // 원산지
    @SerializedName("origin")
    public String origin;
    // 서브 메뉴 코드
    @SerializedName("subMenuCd")
    public String subMenuCode;
    // 사업자 등록 번호
    @SerializedName("bizPrsnLicNum")
    public String businessLicenseNumber;
    // 배달 가능 지역
    @SerializedName("dlvryPsblArea")
    public String deliveryArea;
    // 부가 정보
    @SerializedName("addInfo")
    public String addInfo;
    // 휴일
    @SerializedName("hldy")
    public String holyDay;
    // 이벤트 혜택 안내
    @SerializedName("evntBnftInfo")
    public String eventInfo;
    // 매장 정보
    @SerializedName("storInfo")
    public String storeInfo;
    // 매장 id
    @SerializedName("storId")
    public int storeId;
    // 매장 이름
    @SerializedName("storNm")
    public String storeName;
    // 평점
    @SerializedName("evalScor")
    public float ratingPoint;
    // 매장 등급
    @SerializedName("storGrd")
    private String storGrd;
    // 방문 포장 가능 여부
    @SerializedName("visitPackEnblYn")
    public String isTakeOut;
    // 적립률
    @SerializedName("saveRate")
    public String saveRate;
    // 주문 가능 여부
    @SerializedName("orderEnblYn")
    public String isOrder;
    // 주문 가능 여부, 1 : 오픈 , 0 : 닫김
    @SerializedName("serviceYn")
    public int isOpen;
    // 최소 주문 금액
    @SerializedName("miniOrderAmt")
    public String minOrderPrice;
    // 로켓배송
    @SerializedName("bltDlvry")
    private String rocketDelivery;
    // 즐겨찾기 개수
    @SerializedName("bmarkCnt")
    public int bookmarkCnt;
    // 리뷰 개수
    @SerializedName("storRevwCnt")
    public int storeReviewCnt;
    // 사장님 댓글 개수
    @SerializedName("affiStorCnt")
    private int ceoReviewCnt;
    // 평일 운영 시작 시간
    @SerializedName("wkdyOprtnStartTime")
    public String openTime;
    // 평일 운영 종료 시간
    @SerializedName("wkdyOprtnEndTime")
    public String closeTime;
    // 평일 배달비
    @SerializedName("wkdyDlvryPrice")
    public String deliveryPrice;
    @SerializedName("freeDlvryAmt")
    public int freeDeliveryPrice;
    // 이미지 url
    @SerializedName("imageUrl")
    public String storeThumbnail;
    // 거리
    @SerializedName("distance")
    private String distance;
    // 사용자 즐겨찾기 여부 ( 1 : Y, 0 : N)
    @SerializedName("userBmarkYn")
    public String isBookMarkCheck;
    // 광고 상품 코드
    @SerializedName("adProdCd")
    public String adProductCode;
    // 타임 이벤트 리스트
    @SerializedName("timeEvntInfo")
    public TimeEventInfo timeEventModel;
    // 이벤트 존재 유무
    @SerializedName("evntExist")
    private String isEvent;
    // 키워드 (테그)
    @SerializedName("keyWord")
    public String keyWord;
    // 우수업체 여부 - Y or N
    @SerializedName("exclntBizYn")
    public String isBestStore;
    // 온라인 결제 가능 여부
    @SerializedName("onlinPayEnblYn")
    public String isOnlinePay;
    // 오프라인 결제 가능 여부
    @SerializedName("oflinPayEnblYn")
    public String isOfflinePay;
    // 가맹점 전화번호
    @SerializedName("storPhonNum")
    public String storeCallNumber;

    public class TimeEventInfo {
        // 타임 이벤트 id
        @SerializedName("timeEvntId")
        private String timeEventId;
        // 타임 이벤트 타입 ( -1 : 아무것도 해당 안됨, 0 : 배달, 1 : 테이크 아웃, 2 : 배달, 테이크아웃 전부 )
        @SerializedName("timeEvntTp")
        public int timeEventType;
        // 할인 문구
        @SerializedName("fixAmtCntnt")
        public String saleContent;
        // 추가 적립 문구
        @SerializedName("addSaveRateCntnt")
        public String addSaveRateContent;
        // 할인 내용 목록
        @SerializedName("dscntCntntList")
        private ArrayList<DiscountInfo> discountInfoList;
        // 할인 문구
        @SerializedName("fixRateCntnt")
        public String saleRateContent;
        // 할인 금액
        @SerializedName("fixAmt")
        public int fixAmt;
        // 추가 적립률
        @SerializedName("addSaveRate")
        public int addSaveRate;
        // 정률
        @SerializedName("fixRate")
        public int fixRate;
        // 최대 할인 금액
        @SerializedName("maxDscntAmt")
        public int maxDscntAmt;
    }

    public class DiscountInfo {
        // 추가 적립률
        @SerializedName("addAmtRate")
        private String additionSaveRate;
        // 정액 할인
        @SerializedName("fixAmt")
        private String flatDiscount;
        // 정률 할인
        @SerializedName("fixRate")
        private String rateDiscount;
    }

}
