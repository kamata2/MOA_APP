package com.moaPlatform.moa.side_menu.favorite.model;

import com.google.gson.annotations.SerializedName;

public class SearchedStoreItems {

    @SerializedName("storId")
    public int storId;        //매장ID

    @SerializedName("storNm")
    public String storNm;    //매장이름

    @SerializedName("storImgAttachFileNm")
    public String storImgAttchFileNm;      //매장 이미지

    @SerializedName("evalScor")
    public float evalScor;    //평점

    @SerializedName("storRevwCnt")
    public int storRevwCnt;    //리뷰개수

    @SerializedName("affiStorCnt")
    public int affiStorCnt;    //사장님 댓글개수

    @SerializedName("bmarkCnt")
    public int bmarkCnt;    //즐겨찾기개수

    @SerializedName("storGrd")
    public String storGrd;        //매장등급

    @SerializedName("visitPackEnblYn")
    public String visitPackEnblYn;    //방문포장가능여부

    @SerializedName("saveRate")
    public String saveRate;    //적립률

    @SerializedName("wkdyOprtnStartTime")
    public String wkdyOprtnStartTime; // 평일운영시작시간

    @SerializedName("wkdyOprtnEndTime")
    public String wkdyOprtnEndTime; // 평일운영종료시간

    @SerializedName("wkdyDlvryPrice")
    public String wkdyDlvryPrice;    // 평일배달비

    @SerializedName("orderEnblYn")
    public String orderEnblYn;    //주문가능여부

    @SerializedName("imageUrl")
    public String imageUrl;    //이미지URL

    @SerializedName("distance")
    public String distance;    //거리

    @SerializedName("userBmarkYn")
    public String userBmarkYn;    //사용자 즐겨찾기 여부 ( 1 : Y , 0 : N)

    @SerializedName("serviceSepa")
    public String serviceSepa;

    @Override
    public String toString() {
        return "SearchedStoreItems{" +
                "storId=" + storId +
                ", storNm='" + storNm + '\'' +
                ", storImgAttchFileNm='" + storImgAttchFileNm + '\'' +
                ", evalScor=" + evalScor +
                ", storRevwCnt=" + storRevwCnt +
                ", affiStorCnt=" + affiStorCnt +
                ", bmarkCnt=" + bmarkCnt +
                ", storGrd='" + storGrd + '\'' +
                ", visitPackEnblYn='" + visitPackEnblYn + '\'' +
                ", saveRate='" + saveRate + '\'' +
                ", wkdyOprtnStartTime='" + wkdyOprtnStartTime + '\'' +
                ", wkdyOprtnEndTime='" + wkdyOprtnEndTime + '\'' +
                ", wkdyDlvryPrice='" + wkdyDlvryPrice + '\'' +
                ", orderEnblYn='" + orderEnblYn + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", distance='" + distance + '\'' +
                ", userBmarkYn='" + userBmarkYn + '\'' +
                ", serviceSepa='" + serviceSepa + '\'' +
                '}';
    }
}
