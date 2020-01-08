package com.moaPlatform.moa.wallet;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ResponseWalletData extends CommonModel {
    @SerializedName("bcwRegResMsgGenResultStr")
    private String bcwRegResMsgGenResultStr;

    @SerializedName("bcwRestoreResMsgGenResultStr")
    private String bcwRestoreResMsgGenResultStr;

    @SerializedName("walletAddress")
    private String walletAddress;

    @SerializedName("bcwrDtCtResultInAndroidMsgParserResultStr")
    private String bcwrDtCtResultInAndroidMsgParserResultStr;

    @SerializedName("bcwPswReSetStartResMsgGenResultStr")
    private String bcwPswReSetStartResMsgGenResultStr;

    @SerializedName("bcwPswReSetResMsgGenResultStr")
    private String bcwPswReSetResMsgGenResultStr;

    @SerializedName("bcwPswReSetWDtCtResultAndSyncCheckResMsgGenStr")
    private String bcwPswReSetWDtCtResultAndSyncCheckResMsgGenStr;

    String getBcwRegResMsgGenResultStr() {
        return bcwRegResMsgGenResultStr;
    }

    String getBcwRestoreResMsgGenResultStr() {
        return bcwRestoreResMsgGenResultStr;
    }

    String getWalletAddress() {
        return walletAddress;
    }

    String getBcwrDtCtResultInAndroidMsgParserResultStr() {
        return bcwrDtCtResultInAndroidMsgParserResultStr;
    }

    String getBcwPswReSetStartResMsgGenResultStr() {
        return bcwPswReSetStartResMsgGenResultStr;
    }

    String getBcwPswReSetResMsgGenResultStr() {
        return bcwPswReSetResMsgGenResultStr;
    }

    String getBcwPswReSetWDtCtResultAndSyncCheckResMsgGenStr() {
        return bcwPswReSetWDtCtResultAndSyncCheckResMsgGenStr;
    }
}
