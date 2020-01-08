package com.moaPlatform.moa.online_payment.order_payment.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.App;

public class ReqUseCouponModel extends CommonModel {
    @SerializedName("subMenuCd")
    private String SubMenuCode = App.getInstance().SUB_MENU_CODE;
}
