package com.moaPlatform.moa.bottom_menu.main.model;

import com.google.gson.annotations.SerializedName;
/**
 * 테마 Item Model
 */
public class EatOutThemeModel {
    @SerializedName("bannerSepaCd")     //01(url), 02(가맹점 상세), 03(가맹점 리스트)
    public String bannerSepaCd;
    @SerializedName("bannerSepaCntnt")  //bannerSepaCd  타입에 따라 id 정보나 url정보가 들어옴
    public String bannerSepaCntnt;
    @SerializedName("bannerImg")
    public String bannerImg;
    @SerializedName("bannerCntnt")      //제목
    public String bannerCntnt;
    @SerializedName("bannerImgUrl")
    public String bannerImgUrl;
}
