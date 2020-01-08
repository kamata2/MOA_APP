package com.moaPlatform.moa.online_payment.order_payment.model.res;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ResPaymentResult extends CommonModel {
    // 주문 번호
    @SerializedName("orderId")
    public String orderId;
    // 적립 금액
    @SerializedName("saveAmt")
    public String savePoint;
    // 배달 예정 시간
    @SerializedName("dlvryTime")
    public String deliveryScheduleTime;
    // 가맹점 이름
    @SerializedName("storNm")
    public String storeName;
    // 주문 상품
    @SerializedName("orderProdNm")
    public String orderProducts;
    // 결제 금액
    @SerializedName("payAmt")
    public String paymentMoney;
}
