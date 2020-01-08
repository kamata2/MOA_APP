package com.moaPlatform.moa.side_menu.order.detail.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResOrderDetailHistoryShellModel extends CommonModel {

    // 주문 상세 정보
    @SerializedName("orderInfo")
    private ResOrderDetailHistoryInfoModel orderDetailHistoryInfoModel;

    public ResOrderDetailHistoryInfoModel getOrderDetailHistoryInfoModel() {
        return orderDetailHistoryInfoModel;
    }

    /**
     * @return 주문 상품 상세 리스트 반환
     */
    public List<ResOrderDetailHistoryProductInfoModel> orderDetailHistoryProductList() {
        return orderDetailHistoryInfoModel.resOrderDetailHistoryProductList;
    }

    /**
     * 주문 상세 정보를 담는 resModel
     */
    public class ResOrderDetailHistoryInfoModel {

        // 주문_ID
        @SerializedName("orderId")
        public String orderId;

        // 매장_ID
        @SerializedName("storId")
        public int storId;

        //매장명
        @SerializedName("storNm")
        public String storeName;

        // 주문일시
        @SerializedName("orderDt")
        public String orderTime;

        // 결제방법코드
        @SerializedName("payMthdCd")
        private String payMthdCd;

        // 결제방법코드명
        @SerializedName("payMthdCdNm")
        public String payMethodCodeName;

        // 결제수단구분코드
        @SerializedName("payMthdSepaCd")
        private String payMthdSepaCd;

        // 결제수단구분코드명
        @SerializedName("payMthdSepaCdNm")
        public String payMethodSeparationCodeName;

        // 총합계금액
        @SerializedName("sumAmt")
        public int sumAmt;

        // 배달금액
        @SerializedName("dlvryAmt")
        public int deliveryPrice;

        // 포인트사용금액
        @SerializedName("pointUseAmt")
        public int pointUseAmount;

        // 결제금액
        @SerializedName("payAmt")
        public int payAmt;

        // 적립금액
        @SerializedName("saveAmt")
        public int saveAmt;

        // 할인금액
        @SerializedName("dscntAmt")
        private int dscntAmt;

        // 주문전화번호
        @SerializedName("orderPhonNum")
        public String orderPhoneNumber;

        // 주문우편번호
        @SerializedName("orderPostNum")
        private String orderPostNum;

        // 주문도로명기본주소
        @SerializedName("orderRoadNmDefltAddr")
        public String orderRoadNmDefaultAddress;

        // 주문도로명상세주소
        @SerializedName("orderRoadNmDtlAddr")
        public String orderRoadDetailAddress;

        // 주문지번주소
        @SerializedName("orderLandNumAddr")
        public String orderJibunAddress;

        // 주문경도
        @SerializedName("orderLont")
        private String orderLont;

        // 주문위도
        @SerializedName("orderLatu")
        private String orderLatu;

        // 주문요청사항
        @SerializedName("orderReqItem")
        public String orderReqItem;

        // 주문상태코드
        @SerializedName("orderStatCd")
        public String orderStatCd;

        // 주문상태코드명
        @SerializedName("orderStatCdNm")
        private String orderStatCdNm;

        // 주문 취소 사유 내용
        @SerializedName("takeBackCanclResonCntnt")
        public String orderCancelReasonContent;

        // 주문 상세 상품 리스트
        @SerializedName("orderDtlList")
        private List<ResOrderDetailHistoryProductInfoModel> resOrderDetailHistoryProductList;

        // 세일 항목 리스트
        @SerializedName("orderDscntList")
        private List<ResOrderDetailHistorySaleInfoModel> orderDetailHistorySaleInfoModelList;

        public Map<String, Integer> getOderDetailHistorySaleMap() {
            Map<String, Integer> saleMap = new HashMap<>();
            for (ResOrderDetailHistorySaleInfoModel orderDetailHistorySaleInfoModel : orderDetailHistorySaleInfoModelList) {
                saleMap.put(orderDetailHistorySaleInfoModel.saleCodeName, orderDetailHistorySaleInfoModel.salePrice);
            }
            return saleMap;
        }
    }

    /**
     * 세일 정보를 담는 moel
     */
    private class ResOrderDetailHistorySaleInfoModel {
        // 할인 쿠분 명
        @SerializedName("dscntSepaCdNm")
        private String saleCodeName;

        // 할인 적용 값
        @SerializedName("dscntApplyVal")
        private int salePrice;
    }

    /**
     * 주문 내역 리스트 상품 정보
     */
    public class ResOrderDetailHistoryProductInfoModel {
        // 상품 이름
        @SerializedName("storProdNm")
        public String storeName;

        // 상품 가격
        @SerializedName("storProdPrice")
        private int productPrice;

        // 상품 기본 옵션 명
        @SerializedName("storProdOptNm")
        public String storeDefaultOptionName;

        // 상품 기본 옵션 가격
        @SerializedName("storProdOptPrice")
        private int productDefaultOptionPrice;

        // 수량
        @SerializedName("cnt")
        public int productCount;

        // 추가 옵션 리스트
        @SerializedName("orderDtlOptList")
        public List<ResOrderDetailHistoryAddOptionCategoryModel> orderDetailHistoryAddOptionCategoryModelList;

        /**
         * @return 상품 가격 반환
         */
        public int getProductPrice() {
            return (productPrice + productDefaultOptionPrice) * productCount;
        }
    }

    /**
     * 매장 추가 옵션 카테고리 모델
     */
    public class ResOrderDetailHistoryAddOptionCategoryModel {
        // 추가 옵션 상품 리스트
        @SerializedName("storAddProdOptList")
        public List<ResOrderDetailHistoryAddOptionProductModel> orderDetailHistoryAddOptionProductModelList;
    }

    /**
     * 매장 추가 옵션 상품 모델
     */
    public class ResOrderDetailHistoryAddOptionProductModel {
        // 추가 옵션 상품 이름
        @SerializedName("storAddProdOptNm")
        public String addOptionProductName;

        // 추가 옵션 상품 가격
        @SerializedName("storAddProdOptPrice")
        public int addOptionProductPrice;
    }

}

