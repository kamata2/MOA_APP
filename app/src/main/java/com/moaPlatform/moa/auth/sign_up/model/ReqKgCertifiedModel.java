package com.moaPlatform.moa.auth.sign_up.model;

import com.google.gson.annotations.SerializedName;

public class ReqKgCertifiedModel {
    // 코드
    @SerializedName("authSepaCd")
    private String authSepaCd;
    // 사용자 아이디
    @SerializedName("userId")
    public String userId;
    // 사용자 이메일
    @SerializedName("email")
    public String userEmail;

    /**
     * req 모델 초기화
     * @param userId
     * 유저 아이디
     * @param authSepaCd
     * 코드값
     */
    public void init(String userId, String authSepaCd) {
        this.userId = userId;
        this.authSepaCd = authSepaCd;
    }

    /**
     * req 모델 초기화
     * @param userId
     * 유저 아이디
     * @param authSepaCd
     * 코드값
     * @param userEmail
     * 사용자 이메일
     */
    public void init(String userId, String authSepaCd, String userEmail) {
        init(userId, authSepaCd);
        this.userEmail = userEmail;
    }
}