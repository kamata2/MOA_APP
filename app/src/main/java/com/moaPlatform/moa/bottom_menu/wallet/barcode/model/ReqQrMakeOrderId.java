package com.moaPlatform.moa.bottom_menu.wallet.barcode.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.online_payment.order_payment.model.req.OrderDscntList;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;

public class ReqQrMakeOrderId extends CommonModel {
    @SerializedName("storId")
    public int storId;

    @SerializedName("sumAmt")
    public int sumAmt;

    @SerializedName("pointUseAmt")
    public int pointUseAmt;

    @SerializedName("orderDscntList")
    public ArrayList<OrderDscntList> orderDscntList = new ArrayList<>();

}
