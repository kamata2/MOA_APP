package com.moaPlatform.moa.bottom_menu.badge.badge_main.model.req;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqBadgeModel extends CommonModel {
    // 코인나무 id
    @SerializedName("coinTreeId")
    public int coinTreeId;
}
