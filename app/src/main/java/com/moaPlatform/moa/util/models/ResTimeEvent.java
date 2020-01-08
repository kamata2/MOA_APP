package com.moaPlatform.moa.util.models;

import com.google.gson.annotations.SerializedName;

class ResTimeEvent {
    //타임이벤트_ID
    @SerializedName("timeEvntId")
    private String timeEvntId;
    //타임이벤트타입
    @SerializedName("timeEvntTp")
    private String timeEvntTp;
    //타임이벤트명
    @SerializedName("timeEvntNM")
    private String timeEvntNM;

}
