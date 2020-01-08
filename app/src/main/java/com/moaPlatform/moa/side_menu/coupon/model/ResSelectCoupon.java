package com.moaPlatform.moa.side_menu.coupon.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class ResSelectCoupon extends CommonModel {
    @SerializedName("userCpCnt")
    public int userCpCnt;

    @SerializedName("userCpList")
    public List<AvailableCouponsItems> couponList = new ArrayList<>();
}
