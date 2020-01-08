package com.moaPlatform.moa.delivery_menu.eatout_store_detail.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

/**
 * [외식] 상세정보
 */
public class EatOutStoreInfoModel extends CommonModel {

    private final String TAG = EatOutStoreInfoModel.class.getSimpleName();

    @SerializedName("storId")           public int storId;                          //매장ID
    @SerializedName("storNm")           public String storNm;                       //매장이름
    @SerializedName("storGrd")          public String storGrd;                      //매장 등급
    @SerializedName("visitPackEnblYn")  public String visitPackEnblYn;              //방문포장가능여부
    @SerializedName("eatOutVisitResvSetYn") public String eatOutVisitResvSetYn;     //방문예약가능여부
    @SerializedName("eatOutOrderResvSetYn") public String eatOutOrderResvSetYn;     //주문예약가능여부
    @SerializedName("eatOutStorIntro")  public String eatOutStorIntro;              //외식매장소개
    @SerializedName("evalScor")         public String evalScor;                     //평점
    @SerializedName("bmarkCnt")         public String bmarkCnt;                     //즐겨찾기 cnt
    @SerializedName("storRevwCnt")      public String storRevwCnt;                  //리뷰 cnt
    @SerializedName("lookUpCnt")        public String lookUpCnt;                    //조회수
    @SerializedName("saveRate")         public String saveRate;                     //적립률
    @SerializedName("roadNmDefltAddr")  public String roadNmDefltAddr;              //도로명 기본주소
    @SerializedName("mapImg")           public String mapImg;                       //맵이미지
    @SerializedName("userBmarkYn")      public String userBmarkYn;                  //사용자 즐겨 찾기 여부( 1 : Y , 0 : N)
    @SerializedName("wkdyOprtnStartTime")   public String wkdyOprtnStartTime;       //운영시작시간
    @SerializedName("wkdyOprtnEndTime") public String wkdyOprtnEndTime;             //운영종료시간
    @SerializedName("notiCntnt")        public String notiCntnt;                    //알림내용
    @SerializedName("lastOrderTime")    public String lastOrderTime;                //라스트 오더타임
    @SerializedName("hldy")             public String hldy;                         //휴일
    @SerializedName("storPhonNum")      public String storPhonNum;                  //매장전화번호
    @SerializedName("lont")             public String lont;                         //경도
    @SerializedName("latu")             public String latu;                         //위도
    @SerializedName("eatOutSubMenuCd")  public String eatOutSubMenuCd;              //외식 카테고리 코드

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[" + TAG + "] storId= " + storId);
        builder.append(", storNm= " + storNm);
        builder.append(", storGrd= " + storGrd);
        builder.append(", visitPackEnblYn= " + visitPackEnblYn);
        builder.append(", eatOutVisitResvSetYn= " + eatOutVisitResvSetYn);
        builder.append(", eatOutOrderResvSetYn= " + eatOutOrderResvSetYn);
        builder.append(", eatOutStorIntro= " + eatOutStorIntro);
        builder.append(", evalScor= " + evalScor);
        builder.append(", bmarkCnt= " + bmarkCnt);
        builder.append(", storRevwCnt= " + storRevwCnt);
        builder.append(", lookUpCnt= " + lookUpCnt);
        builder.append(", saveRate= " + saveRate);
        builder.append(", roadNmDefltAddr= " + roadNmDefltAddr);
        builder.append(", mapImg= " + mapImg);
        builder.append(", userBmarkYn= " + userBmarkYn);
        builder.append(", wkdyOprtnStartTime= " + wkdyOprtnStartTime);
        builder.append(", wkdyOprtnEndTime= " + wkdyOprtnEndTime);
        builder.append(", notiCntnt= " + notiCntnt);
        builder.append(", lastOrderTime= " + lastOrderTime);
        builder.append(", hldy= " + hldy);
        builder.append(", lont= " + lont);
        builder.append(", latu= " + latu);
        builder.append(", eatOutSubMenuCd= " + eatOutSubMenuCd);
        builder.append("\n");
        return builder.toString();
    }
}
