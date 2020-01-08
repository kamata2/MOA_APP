package com.moaPlatform.moa.online_payment.order_payment.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;

public class ResUseCouponModel extends CommonModel {

    @SerializedName("userCpCnt")
    public int userCpCnt;

    // 사용 가능한 쿠폰 리스트
    @SerializedName("userCpList")
    public ArrayList<UseCouponModel> useCouponList;

    public class UseCouponModel{
        // 타이틀
        @SerializedName("title")
        public String couponTitle;
        // 쿠폰 코드
        @SerializedName("cpCd")
        public String couponCode;
        // 적용 타입
        @SerializedName("applyTp")
        public String applyType;
        // 적용값
        @SerializedName("applyVal")
        public int applyValue;
        // 종료일시
        @SerializedName("endDt")
        public String endTime;
        // 최대 혜택 금액
        @SerializedName("maxBnftAmt")
        public int maxSalePrice;
    }

}
