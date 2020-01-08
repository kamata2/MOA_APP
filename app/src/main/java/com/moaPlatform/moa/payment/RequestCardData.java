package com.moaPlatform.moa.payment;

import com.google.gson.annotations.SerializedName;

public class RequestCardData {

    @SerializedName("actionType")
    private String actionType;

    @SerializedName("secretKey")
    private String secretKey;

    @SerializedName("step")
    private String step;

    @SerializedName("cardNick")
    private String cardNick;

    @SerializedName("memberId")
    private String memberId;

    @SerializedName("payKind")
    private String payKind;

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("secureKey")
    private String secureKey;

    @SerializedName("cardToken")
    private String cardToken;

    @SerializedName("userId")
    private String userId;

    @SerializedName("secretKeyOrg")
    private String secretKeyOrg;

    @SerializedName("secretKeyNew")
    private String secretKeyNew;

    void setActionType(String actionType) {
        this.actionType = actionType;
    }

    void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    void setStep(String step) {
        this.step = step;
    }

    void setCardNick(String cardNick) {
        this.cardNick = cardNick;
    }

    void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    void setPayKind(String payKind) {
        this.payKind = payKind;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    void setSecretKeyOrg(String secretKeyOrg) {
        this.secretKeyOrg = secretKeyOrg;
    }

    void setSecretKeyNew(String secretKeyNew) {
        this.secretKeyNew = secretKeyNew;
    }
}