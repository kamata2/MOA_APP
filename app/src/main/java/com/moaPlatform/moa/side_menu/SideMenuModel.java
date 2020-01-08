package com.moaPlatform.moa.side_menu;

import com.google.gson.annotations.SerializedName;

public class SideMenuModel {
    @SerializedName("revwCnt")
    public int revwCnt; // 리뷰개수

    @SerializedName("bmarkCnt")
    public int bmarkCnt; // 즐겨찾기 개수

    @SerializedName("evntNoticeCnt")
    public int evntNoticeCnt; // 이벤트 공지사항 개수

    @SerializedName("resvCnt")
    public int resvCnt; // 예약 개수

    @SerializedName("notiCnt")
    public int notiCnt; // 알림 개수

    @SerializedName("orderCnt")
    public int orderCnt; // 주문개수

    @SerializedName("cpCnt")
    public int cpCnt; // 쿠폰 개수

    @SerializedName("inqryCnt")
    public int inqryCnt; // 1:1문의 개수

    @SerializedName("totRevwCnt")
    public int totRevwCnt; ///총 리뷰 개수

    @SerializedName("totSympCnt")
    public int totSympCnt; // 총 공감 개수

    @SerializedName("nick")
    public String  nick; //닉네임

    @SerializedName("profImg")
    public String  profImg; // 프로필이미지경로
}
