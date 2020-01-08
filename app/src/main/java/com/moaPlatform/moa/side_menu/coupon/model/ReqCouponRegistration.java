package com.moaPlatform.moa.side_menu.coupon.model;

import android.widget.EditText;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqCouponRegistration extends CommonModel {

    @SerializedName("cpNum")
    public String cpNum;	// 쿠폰번호

    /**
     * cpNum 에 쿠폰 번호 세팅
     * @param etCouponView
     * 쿠폰 번호를 입력하는 EditText
     */
    public void setCouponNumber(EditText etCouponView) {
        if (etCouponView != null) {
            setCouponNumber(etCouponView.getText().toString());
        }
    }

    /**
     * cpNum 에 쿠폰 번호 세팅
     * @param couponNumber
     * 쿠폰 번호를 입력하는 EditText
     */
    public void setCouponNumber(String couponNumber) {
        cpNum = couponNumber;
    }
}
