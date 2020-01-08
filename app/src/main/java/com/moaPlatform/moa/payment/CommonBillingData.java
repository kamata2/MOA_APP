package com.moaPlatform.moa.payment;

import com.google.gson.annotations.SerializedName;

public class CommonBillingData {
    @SerializedName("productNm")
    private String productNm;

    @SerializedName("charset")
    private String charset;

    @SerializedName("userNm")
    private String userNm;

    @SerializedName("orderNo")
    private Object orderNo;

    @SerializedName("productAmt")
    private String productAmt;

    @SerializedName("mallId")
    private String mallId;

    @SerializedName("langFlag")
    private String langFlag;

    @SerializedName("userAddr")
    private String userAddr;

    @SerializedName("totAmt")
    private String totAmt;

    @SerializedName("userId")
    private String userId;

    @SerializedName("membUserNo")
    private String membUserNo;

    @SerializedName("userPhone2")
    private String userPhone2;

    @SerializedName("userPhone1")
    private String userPhone1;

    @SerializedName("productExpr")
    private String productExpr;

    @SerializedName("appScheme")
    private String appScheme;

    @SerializedName("userMail")
    private String userMail;

    @SerializedName("currency")
    private String currency;

    @SerializedName("mallNm")
    private String mallNm;

    @SerializedName("productType")
    private String productType;

    String getProductNm() {
        return productNm;
    }

    String getCharset() {
        return charset;
    }

    String getUserNm() {
        return userNm;
    }

    Object getOrderNo() {
        return orderNo;
    }

    String getProductAmt() {
        return productAmt;
    }

    String getMallId() {
        return mallId;
    }

    String getLangFlag() {
        return langFlag;
    }

    String getUserAddr() {
        return userAddr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String getMembUserNo() {
        return membUserNo;
    }

    String getUserPhone2() {
        return userPhone2;
    }

    String getUserPhone1() {
        return userPhone1;
    }

    String getProductExpr() {
        return productExpr;
    }

    String getAppScheme() {
        return appScheme;
    }

    String getUserMail() {
        return userMail;
    }

    String getCurrency() {
        return currency;
    }

    String getMallNm() {
        return mallNm;
    }

    String getProductType() {
        return productType;
    }
}