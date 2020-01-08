package com.moaPlatform.moa.bottom_menu.main.model;

import com.google.gson.annotations.SerializedName;

/**
 * 상단 배너 모델
 */
public class EatOutBannerModel {
    @SerializedName("bannerSepaCd")     //01(url), 02(가맹점 상세), 03(가맹점 리스트)
    public String bannerSepaCd;
    @SerializedName("bannerSepaCntnt")  //bannerSepaCd  타입에 따라 id 정보나 url정보가 들어옴
    public String bannerSepaCntnt;
    @SerializedName("bannerImg")
    public String bannerImg;
    @SerializedName("bannerTitle")  //배너 대표문구
    public String bannerTitle;
    @SerializedName("bannerCntnt")  //배너 서브문구
    public String bannerCntnt;
    @SerializedName("bannerImgUrl")
    public String bannerImgUrl;


    @Override
    public String toString() {
        return "EatOutBannerModel{" +
                "bannerSepaCd='" + bannerSepaCd + '\'' +
                ", bannerSepaCntnt='" + bannerSepaCntnt + '\'' +
                ", bannerImg='" + bannerImg + '\'' +
                ", bannerTitle='" + bannerTitle + '\'' +
                ", bannerCntnt='" + bannerCntnt + '\'' +
                ", bannerImgUrl='" + bannerImgUrl + '\'' +
                '}';
    }
}
