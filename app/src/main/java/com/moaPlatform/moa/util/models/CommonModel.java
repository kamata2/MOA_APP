package com.moaPlatform.moa.util.models;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

/**
 * 공통으로 사용할 모델
 */
public class CommonModel extends BaseModel {
    // 엑세스 토큰 만기일 (MilliSeconds)
    @SerializedName("accessExpirationDate")
    public String accessExpirationDate;
    // 엑세스 토큰
    @SerializedName("accessToken")
    public String accessToken;
    // 유저 아이디
    public String userId = MoaAuthHelper.getInstance().getBasePrimaryInfo();
    // 결과값
    @SerializedName("resultValue")
    public String resultValue;
    // 결과 메시지
    @SerializedName("resultMessage")
    public String resultMessage;

    public boolean isDataSuccess() {
        return resultValue.equals(ServerSideMsg.SUCCESS);
    }
}
