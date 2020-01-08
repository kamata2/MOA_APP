package com.moaPlatform.moa.side_menu.order.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqOrder extends CommonModel {

    @SerializedName("getStartDt")
    public String postStartDt;    // 조회시작일

    @SerializedName("getLastDt")
    public String postLastDt;    // 조회마감일

}
