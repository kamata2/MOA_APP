package com.moaPlatform.moa.bottom_menu.badge.badge_main.model.res;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ResCoinTreeReword extends CommonModel {
    // 지급받은 코인수 (0일 경우 받을게 없음)
    @SerializedName("receiveCoin")
    public int receiveCoin;
    // 나무정보
    @SerializedName("dplCoinTree")
    public DplCoinTreeModel dplCoinTreeModel;
}
