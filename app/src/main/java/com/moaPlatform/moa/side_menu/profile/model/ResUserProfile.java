package com.moaPlatform.moa.side_menu.profile.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ResUserProfile extends CommonModel {

    @SerializedName("userProfile")
    public UserProfile UserProfile;
}
