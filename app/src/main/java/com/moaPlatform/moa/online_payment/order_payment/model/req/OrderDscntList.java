package com.moaPlatform.moa.online_payment.order_payment.model.req;

import com.google.gson.annotations.SerializedName;

public class OrderDscntList {
    // 할인 구분 코드
    @SerializedName("dscntSepaCd")
    public String dscntSepaCd = "01";
    // 할인적용코드
    @SerializedName("dscntApplyCd")
    public String dscntApplyCd;
}
