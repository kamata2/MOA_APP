package com.moaPlatform.moa.bottom_menu.main.model;

import com.google.gson.annotations.SerializedName;

/**
 * 상점정보 Item Model
 */
public class EatOutStoreModel {
    @SerializedName("storId")
    public String storId;
    @SerializedName("storNm")
    public String storNm;
    @SerializedName("landNumAddr")
    public String landNumAddr;
    @SerializedName("storImgAttchFileNm")
    public String storImgAttchFileNm;
    @SerializedName("evalScor")
    public String evalScor;
    @SerializedName("storGrd")
    public String storGrd;
    @SerializedName("saveRate")
    public String saveRate;
    @SerializedName("bltDlvry")
    public String bltDlvry;
    @SerializedName("bmarkCnt")
    public String bmarkCnt;
    @SerializedName("storRevwCnt")
    public String storRevwCnt;
    @SerializedName("distance")
    public String distance;
    @SerializedName("imageUrl")
    public String imageUrl;
    @SerializedName("timeEvntInfo")
    public EatOutTimeEventInfoModel timeEvntInfo;
    @SerializedName("lookUpCnt")
    public String lookUpCnt;
    @SerializedName("category")
    public String category;
}
