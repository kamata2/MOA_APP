package com.moaPlatform.moa.util.auth.model;

import com.google.gson.annotations.SerializedName;

public class RequestMoaAuth {
    // common
    @SerializedName("email")
    private String email;
    @SerializedName("userId")
    private String userId;

    @SerializedName("previousUserId")
    private String previousUserId;

    // fingerprint
    @SerializedName("fpRegStartReqStr")
    private String fpRegStartReqStr;
    @SerializedName("fpRegReqStr")
    private String fpRegReqStr;
    @SerializedName("fpLogInStartReqStr")
    private String fpLogInStartReqStr;
    @SerializedName("fpLoginReqStr")
    private String fpLoginReqStr;

    // pin
    @SerializedName("idExistStr")
    private String idExistStr;
    @SerializedName("pinRegReqMsgGenStr")
    private String pinRegReqMsgGenStr;
    @SerializedName("pinLogInStartReqStr")
    private String pinLogInStartReqStr;
    @SerializedName("pinLogInReqStr")
    private String pinLogInReqStr;
    @SerializedName("nonMemberId")
    private String nonMemberId;
    @SerializedName("pswReSetReqMsgGenResultStr")
    private String pswReSetReqMsgGenResultStr;
    @SerializedName("pswChangeReqMsgGenResultStr")
    private String pswChangeReqMsgGenResultStr;

    @SerializedName("recmrMobiNum")
    private String recommenderNumber;

    @SerializedName("userUnREgistReqMsgGenResultStr")
    private String userUnREgistReqMsgGenResultStr;

    public String getPreUserID() {
        return previousUserId;
    }

    public void setPreUserID(String previousUserId) {
        this.previousUserId = previousUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFpRegStartReqStr(String fpRegStartReqStr) {
        this.fpRegStartReqStr = fpRegStartReqStr;
    }

    public void setFpRegReqStr(String fpRegReqStr) {
        this.fpRegReqStr = fpRegReqStr;
    }

    public void setFpLogInStartReqStr(String fpLogInStartReqStr) {
        this.fpLogInStartReqStr = fpLogInStartReqStr;
    }

    public void setFpLoginReqStr(String fpLoginReqStr) {
        this.fpLoginReqStr = fpLoginReqStr;
    }

    public void setIdExistStr(String idExistStr) {
        this.idExistStr = idExistStr;
    }

    public void setPinRegReqMsgGenStr(String pinRegReqMsgGenStr) {
        this.pinRegReqMsgGenStr = pinRegReqMsgGenStr;
    }

    public void setPinLogInStartReqStr(String pinLogInStartReqStr) {
        this.pinLogInStartReqStr = pinLogInStartReqStr;
    }

    public void setPinLogInReqStr(String pinLogInReqStr) {
        this.pinLogInReqStr = pinLogInReqStr;
    }

    public void setNonMemberId(String nonMemberId) {
        this.nonMemberId = nonMemberId;
    }

    public void setPswReSetReqMsgGenResultStr(String pswReSetReqMsgGenResultStr) {
        this.pswReSetReqMsgGenResultStr = pswReSetReqMsgGenResultStr;
    }

    public void setPswChangeReqMsgGenResultStr(String pswChangeReqMsgGenResultStr) {
        this.pswChangeReqMsgGenResultStr = pswChangeReqMsgGenResultStr;
    }

    public void setRecommenderNumber(String recommenderNumber) {
        this.recommenderNumber = recommenderNumber;
    }

    public void setUserUnREgistReqMsgGenResultStr(String userUnREgistReqMsgGenResultStr) {
        this.userUnREgistReqMsgGenResultStr = userUnREgistReqMsgGenResultStr;
    }

    public void clearPIN() {
        setNonMemberId("");
        setIdExistStr("");
        setPinRegReqMsgGenStr("");
        setEmail("");
        setPinLogInStartReqStr("");
        setPinLogInReqStr("");
        setUserId("");
        setPswReSetReqMsgGenResultStr("");
        setPswChangeReqMsgGenResultStr("");
    }

    public void clearFingerprint() {
        setEmail("");
        setFpLoginReqStr("");
        setFpLogInStartReqStr("");
        setFpRegReqStr("");
        setFpRegStartReqStr("");
    }
}
