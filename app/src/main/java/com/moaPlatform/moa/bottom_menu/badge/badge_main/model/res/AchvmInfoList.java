package com.moaPlatform.moa.bottom_menu.badge.badge_main.model.res;

import com.google.gson.annotations.SerializedName;

public class AchvmInfoList {
    //업적 ID
    @SerializedName("achvmId")
    public String achvmId;
    //업적 이름
    @SerializedName("achvmNm")
    public String achvmNm;
    //목표수치
    @SerializedName("goalFigure")
    public int goalFigure;
    //달성유무
    @SerializedName("cmplYn")
    public String cmplYn;
    //수령유무
    @SerializedName("recvYn")
    public String recvYn;

}
