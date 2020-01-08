package com.moaPlatform.moa.payment;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ResponseCardData extends CommonModel {

    @SerializedName("result")
    private String result;

    @SerializedName("actionType")
    private String actionType;

    @SerializedName("step")
    private String step;

    @SerializedName("returnURL")
    private String returnURL;

    @SerializedName("token")
    private String token;

    @SerializedName("resultMsg")
    private String resultMsg;

    @SerializedName("cardList")
    private String cardList;

    @SerializedName("nonce")
    private String nonce;

    @SerializedName("commonBillingData")
    private CommonBillingData commonBillingData;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    String getReturnURL() {
        return returnURL;
    }

    String getToken() {
        return token;
    }

    String getCardList() {
        return cardList;
    }

    public String getNonce() {
        return nonce;
    }

    public String getResultValue() {
        return resultValue;
    }

    CommonBillingData getCommonBillingData() {
        return commonBillingData;
    }

    public String getResultMsg() {
        return resultMsg;
    }
}