package com.moaPlatform.moa.bottom_menu.badge.badge_main.model.res;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AchvmGroupDtoList {
    //업적그룹ID
    @SerializedName("achvmGroupId")
    public String achvmGroupId;
    //업적구분
    @SerializedName("achvmSepa")
    public String achvmSepa;
    //업적그룹명
    @SerializedName("achvmGroupNm")
    public String achvmGroupNm;
    //업적 설명
    @SerializedName("achvmExpn")
    public String achvmExpn;
    //대표이미지명
    @SerializedName("rpsntImgNm")
    public String rpsntImgNm;
    //달성 수치
    @SerializedName("cmplFigure")
    public int cmplFigure;
    //업적 리스트
    @SerializedName("achvmInfo")
    public ArrayList<AchvmInfoList> achvmInfoList;

}
