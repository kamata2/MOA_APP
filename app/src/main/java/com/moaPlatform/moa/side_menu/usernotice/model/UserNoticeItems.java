package com.moaPlatform.moa.side_menu.usernotice.model;

import com.google.gson.annotations.SerializedName;

public class UserNoticeItems {

    @SerializedName("userNotiId")
    public String userNotiId; // 사용자알림_ID

    @SerializedName("title")
    public String title; // 제목

    @SerializedName("dtlCntnt")
    public String dtlCntnt; // 상세내용

    @SerializedName("chckYn")
    public String chckYn; // 확인여부

    @SerializedName("creeDt")
    public String creeDt; // 생성일시
}
