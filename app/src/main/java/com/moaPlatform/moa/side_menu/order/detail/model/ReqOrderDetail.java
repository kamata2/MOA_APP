package com.moaPlatform.moa.side_menu.order.detail.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqOrderDetail extends CommonModel {
    @SerializedName("orderId")
    public String orderId;  //주문_ID
}
