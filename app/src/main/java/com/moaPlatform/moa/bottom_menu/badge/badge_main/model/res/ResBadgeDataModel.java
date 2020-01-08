package com.moaPlatform.moa.bottom_menu.badge.badge_main.model.res;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;

public class ResBadgeDataModel extends CommonModel {
    // 나무정보
    @SerializedName("dplCoinTree")
    public DplCoinTreeModel dplCoinTreeModel;
    // 코인 및 포인트 정보 모델
    @SerializedName("elecWaltInfo")
    public ElecWaltInfoModel elecWaltInfoModel;

    @SerializedName("achvmGroupDto")
    public ArrayList<AchvmGroupDtoList> achvmGroupDtoList;
}
