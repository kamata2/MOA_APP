package com.moaPlatform.moa.side_menu.profile.model;

import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("userId")
    public String userId;

    @SerializedName("nick")
    public String nick;

    @SerializedName("intro")
    public String intro;

    @SerializedName("imgUrl")
    public String imgUrl;

}
