package com.moaPlatform.moa.bottom_menu.main.model.list;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.bottom_menu.main.model.EatOutStoreModel;

import java.util.List;

public class EatOutShopList {
    @SerializedName("cTypeLayoutTitle")
    public String title;

    @SerializedName("eatOutCTypeList")
    public List<EatOutStoreModel> shopList;
}
