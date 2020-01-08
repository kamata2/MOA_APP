package com.moaPlatform.moa.bottom_menu.wallet.barcode.model;

import com.google.gson.annotations.SerializedName;

public class ResQrMakeOrderId {

    @SerializedName("resultValue")
    public String  resultValue; // 결과값

    @SerializedName("orderId")
    public String orderId; // 포인트잔액

    @SerializedName("saveAmt")
    public int  saveAmt; // 매장이름

    // 결제금액
    @SerializedName("payAmt")
    public int payAmt;


}
