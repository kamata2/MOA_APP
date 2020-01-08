package com.moaPlatform.moa.bottom_menu.main.model;

import com.google.gson.annotations.SerializedName;

public class EatOutTimeEventInfoModel {

    @SerializedName("fixAmtCntnt")      //정액문구
    public String fixAmtCntnt;

    @SerializedName("dscntProd")        //적용상품코드(all/all : 전부, 그 외 상품코드)
    public String dscntProd;

    @SerializedName("addSaveRateCntnt") //추가적립률문구
    public String addSaveRateCntnt;

    @SerializedName("fixRateCntnt")     //정률문구
    public String fixRateCntnt;

    @SerializedName("addSaveRate")      //추가적립률
    public String addSaveRate;

    @SerializedName("fixAmt")           //정액
    public String fixAmt;

    @SerializedName("timeEvntTp")       //타임이벤트타입	(0:배달,1:외식,3:둘다)
    public String timeEvntTp;

    @SerializedName("fixRate")          //정률
    public String fixRate;

}
