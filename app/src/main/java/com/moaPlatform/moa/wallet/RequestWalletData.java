package com.moaPlatform.moa.wallet;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class RequestWalletData extends CommonModel {

    @SerializedName("bcwRegReqMsgGenResultStr")
    private String bcwRegReqMsgGenResultStr;

    @SerializedName("bcwRestoreReqMsgGenResultStr")
    private String bcwRestoreReqMsgGenResultStr;

    @SerializedName("walletAddress")
    private String walletAddress;

    @SerializedName("encryptPsw")
    private String encryptPsw;

    @SerializedName("puk")
    private String puk;

    @SerializedName("email")
    private String email;

    @SerializedName("bcwRegAndroidWDataCreateResultStr")
    private String bcwRegAndroidWDataCreateResultStr;

    @SerializedName("bcwPswReSetStartReqMsgGenResultStr")
    private String bcwPswReSetStartReqMsgGenResultStr;

    @SerializedName("bcWPswReSetReqMsgGenResultStr")
    private String bcWPswReSetReqMsgGenResultStr;

    @SerializedName("bcwPswReSetWDtCtResultAndSyncCheckReqStr")
    private String bcwPswReSetWDtCtResultAndSyncCheckReqStr;

    void setBcwRegReqMsgGenResultStr(String bcwRegReqMsgGenResultStr) {
        this.bcwRegReqMsgGenResultStr = bcwRegReqMsgGenResultStr;
    }

    void setBcwRestoreReqMsgGenResultStr(String bcwRestoreReqMsgGenResultStr) {
        this.bcwRestoreReqMsgGenResultStr = bcwRestoreReqMsgGenResultStr;
    }

    void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    void setEncryptPsw(String encryptPsw) {
        this.encryptPsw = encryptPsw;
    }

    void setPuk(String puk) {
        this.puk = puk;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    void setBcwRegAndroidWDataCreateResultStr(String bcwRegAndroidWDataCreateResultStr) {
        this.bcwRegAndroidWDataCreateResultStr = bcwRegAndroidWDataCreateResultStr;
    }

    void setBcwPswReSetStartReqMsgGenResultStr(String bcwPswReSetStartReqMsgGenResultStr) {
        this.bcwPswReSetStartReqMsgGenResultStr = bcwPswReSetStartReqMsgGenResultStr;
    }

    void setBcWPswReSetReqMsgGenResultStr(String bcWPswReSetReqMsgGenResultStr) {
        this.bcWPswReSetReqMsgGenResultStr = bcWPswReSetReqMsgGenResultStr;
    }

    void setBcwPswReSetWDtCtResultAndSyncCheckReqStr(String bcwPswReSetWDtCtResultAndSyncCheckReqStr) {
        this.bcwPswReSetWDtCtResultAndSyncCheckReqStr = bcwPswReSetWDtCtResultAndSyncCheckReqStr;
    }

    void clear() {
        setBcwRegReqMsgGenResultStr("");
        setBcwRestoreReqMsgGenResultStr("");
        setWalletAddress("");
        setEncryptPsw("");
        setPuk("");
        setEmail("");
        setBcwRegAndroidWDataCreateResultStr("");
        setBcwPswReSetStartReqMsgGenResultStr("");
        setBcWPswReSetReqMsgGenResultStr("");
        setBcwPswReSetWDtCtResultAndSyncCheckReqStr("");
    }
}
