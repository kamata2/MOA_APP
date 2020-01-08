package com.moaPlatform.moa.delivery_menu.store_list.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.StoreInfoModel;

import java.util.ArrayList;

public class ResStoreListModel extends CommonModel {

    // 가맹점 리스트
    @SerializedName("storList")
    public ArrayList<StoreInfoModel> storeList;

}
