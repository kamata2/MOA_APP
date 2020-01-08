package com.moaPlatform.moa.delivery_menu.eatout_store_list.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.bottom_menu.main.model.EatOutStoreModel;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;

public class ResEatOutStoreListModel extends CommonModel {

    // 가맹점 리스트
    @SerializedName("eatOutStorList")
    public ArrayList<EatOutStoreModel> storeList;

}
