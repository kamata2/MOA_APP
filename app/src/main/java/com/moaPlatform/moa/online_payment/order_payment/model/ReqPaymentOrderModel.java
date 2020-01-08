package com.moaPlatform.moa.online_payment.order_payment.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.online_payment.order_payment.model.req.OrderDscntList;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.ProductAddOption;

import java.util.ArrayList;

public class ReqPaymentOrderModel extends CommonModel {
    // 매장ID
    @SerializedName("storId")
    public int storeId;
    // 거래구분코드, 바로주문 or 예약주문 - (DEAL_SEPA_CD)
    @SerializedName("dealSepaCd")
    public String dealSeparationCode = CodeTypeManager.PaymentCode.DEAL_SEPA_CD_RIGHT_ORDER.getType();
    // 주문구분코드, 배달 or 방문 수령 - (ORDER_SEPA_CD)
    @SerializedName("orderSepaCd")
    public String orderSeparationCode = CodeTypeManager.PaymentCode.ORDER_SEPA_CD_DELIVERY.getType();
    // 결제수단코드 바로결제, 현장 결제
    @SerializedName("payMthdCd")
    public String paymentCode = CodeTypeManager.PaymentCode.PAY_MTHD_CD_RIGHT_PAYMENT.getType();
    // 결제수단구분코드, 현금, 신용카드  등등
    @SerializedName("payMthdSepaCd")
    public String paymentSeparationCode = CodeTypeManager.PaymentCode.PAY_MTHD_SEPA_CD_MOA_PAY.getType();
    // 포인트사용금액
    @SerializedName("pointUseAmt")
    public int pointAmountUse=0;
    // 주문요청사항
    @SerializedName("orderReqItem")
    public String orderReqTerm="";
    // 주문전화번호
    @SerializedName("orderPhonNum")
    public String orderPhoneNumber;
    // 주문우편번호
    @SerializedName("orderPostNum")
    public String orderPostNumber;
    // 주문도로명기본주소
    @SerializedName("orderRoadNmDefltAddr")
    public String orderDefaultRoadAddress;
    // 주문도로명상세주소
    @SerializedName("orderRoadNmDtlAddr")
    public String orderDetailRoadAddress;
    // 주문지번주소
    @SerializedName("orderLandNumAddr")
    public String orderJibunAddress;
    // 주문경도
    @SerializedName("orderLont")
    public String orderLongitude;
    // 주문위도
    @SerializedName("orderLatu")
    public String orderLatitude;
    // 방문 수신 예약 일시
    @SerializedName("visitRecvResvDt")
    public String visitScheduleDate;
    //주문상세
    @SerializedName("orderDtl")
    public ArrayList<OrderDetailModel> orderDtl = new ArrayList<>();
    //주문할인목록
    @SerializedName("orderDscntList")
    public ArrayList<OrderDscntList> orderDscntList = new ArrayList<>();

    /**
     * 상품 상세 화면
     */
    public class OrderDetailModel {
        // 매장 상품 코드
        @SerializedName("storProdCd")
        public int storeProductCode;
        // 매장 상품 옵션
        @SerializedName("storProdOptId")
        public int storeProductOptionId;
        // 상품 개수
        @SerializedName("cnt")
        public int productCount;
        // 추가 상품 옵션 리스트
        @SerializedName("orderDtlOptList")
        public ArrayList<ProductAddOption> addProductOptionList = new ArrayList<>();
    }

    public void orderDetailModelInit(int storeProductCode, int storeProductOptionId, int productCount, ArrayList<ProductAddOption> addProductOptionList) {
        orderDtl.clear();
        OrderDetailModel orderDetailModel = new OrderDetailModel();
        orderDetailModel.storeProductCode = storeProductCode;
        orderDetailModel.storeProductOptionId = storeProductOptionId;
        orderDetailModel.productCount = productCount;
        orderDetailModel.addProductOptionList = addProductOptionList;
        orderDtl.add(orderDetailModel);
    }
}
