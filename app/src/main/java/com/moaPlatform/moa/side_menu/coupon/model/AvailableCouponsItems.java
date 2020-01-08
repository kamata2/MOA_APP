package com.moaPlatform.moa.side_menu.coupon.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AvailableCouponsItems {
    @SerializedName("cpCd")
    public String cpCd;			// 쿠폰번호

    @SerializedName("issueTp")
    public String issueTp;			// 발급타입

    @SerializedName("title")
    public String title;			// 타이틀

    @SerializedName("applyTp")
    public String applyTp;			// 적용타입

    @SerializedName("applyVal")
    public String applyVal;			// 적용값

    @SerializedName("otherApplyVal")
    public String otherApplyVal;		// 기타적용값

    @SerializedName("endDt")
    public String endDt;			// 종료일시

    @SerializedName("applyScope")
    public String applyScope;			// 적용범위

    @SerializedName("imgNm")
    public String imgNm;			// 이미지명

    @SerializedName("usePsblYn")
    public String usePsblYn;			// 사용가능여부

    @SerializedName("recvDt")
    public String recvDt;			// 수신일시

    @SerializedName("rstrcCndtnList")
    public List<CouponConstraintsItems> rstrcList = new ArrayList<>();
}
