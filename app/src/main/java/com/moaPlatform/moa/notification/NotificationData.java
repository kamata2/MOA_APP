package com.moaPlatform.moa.notification;

import com.google.gson.annotations.SerializedName;

public class NotificationData {

    @SerializedName("pushScreenType")       //푸시 노출 타입
    public String pushScreenType;

    @SerializedName("pushTitleText")        //푸시 타이틀 문구
    public String pushTitleText;

    @SerializedName("pushContentText")      //푸시 내용 문구
    public String pushContentText;

    @SerializedName("actionView")           //화면 이동(서버와 협의 필요)
    public String actionView;

    @SerializedName("storId")              //제휴점 아이디
    public String storeId;

    @Override
    public String toString() {
        return "NotificationData{" +
                "pushScreenType='" + pushScreenType + '\'' +
                ", pushTitleText='" + pushTitleText + '\'' +
                ", pushContentText='" + pushContentText + '\'' +
                ", actionView='" + actionView + '\'' +
                ", storeId='" + storeId + '\'' +
                '}';
    }
}
