package com.moaPlatform.moa.util.auth.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ResponseMoaAuth extends CommonModel {
    // fingerprint
    @SerializedName("fpRegReqStartResStr")
    private String fpRegReqStartResStr;
    @SerializedName("fpRegResStr")
    private String fpRegResStr;
    @SerializedName("fpLogInStartResStr")
    private String fpLogInStartResStr;
    @SerializedName("fpLoginResStr")
    private String fpLoginResStr;

    // pin
    @SerializedName("nonMemberId")
    private String nonMemberId;
    @SerializedName("idExistAckStr")
    private String idExistAckStr;
    @SerializedName("pinRegResStr")
    private String pinRegResStr;
    @SerializedName("pinLogInStartResStr")
    private String pinLogInStartResStr;
    @SerializedName("pinLoginResultStr")
    private String pinLoginResultStr;
    @SerializedName("pswReSetResMsgGenResultStr")
    private String pswReSetResMsgGenResultStr;
    @SerializedName("pswChangeResMsgGenResultStr")
    private String pswChangeResMsgGenResultStr;

    @SerializedName("userUnRegistReqParserResultStr")
    private String userUnRegistReqParserResultStr;

    public String getFpRegReqStartResStr() {
        return fpRegReqStartResStr;
    }

    public String getFpRegResStr() {
        return fpRegResStr;
    }

    public String getFpLogInStartResStr() {
        return fpLogInStartResStr;
    }

    public String getFpLoginResStr() {
        return fpLoginResStr;
    }

    public String getNonMemberId() {
        return nonMemberId;
    }

    public String getIdExistAckStr() {
        return idExistAckStr;
    }

    public String getPinRegResStr() {
        return pinRegResStr;
    }

    public String getPinLogInStartResStr() {
        return pinLogInStartResStr;
    }

    public String getPinLoginResultStr() {
        return pinLoginResultStr;
    }

    public String getPswReSetResMsgGenResultStr() {
        return pswReSetResMsgGenResultStr;
    }

    public String getPswChangeResMsgGenResultStr() {
        return pswChangeResMsgGenResultStr;
    }

    public String getUserUnRegistReqParserResultStr() {
        return userUnRegistReqParserResultStr;
    }
}
