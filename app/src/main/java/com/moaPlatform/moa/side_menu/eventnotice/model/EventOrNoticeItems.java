package com.moaPlatform.moa.side_menu.eventnotice.model;

import com.google.gson.annotations.SerializedName;

public class EventOrNoticeItems {
    @SerializedName("evntNoticeId")
    public String evntNoticeId; // 이벤트 또는 공지사항ID

    @SerializedName("title")
    public String title; // 타이틀

    @SerializedName("cntnt")
    public String cntnt; // 내용

    @SerializedName("writDt")
    public String writDt; // 작성일시

    @SerializedName("startDt")
    public String startDt; // 시작일시

    @SerializedName("endDt")
    public String endDt; // 종료일시

    @SerializedName("chckYn")
    public String chckYn; // 확인여부

    @SerializedName("img")
    public String img; // 이미지

    @SerializedName("dtlUrl")
    public String dtlUrl; // 상세URL


}
