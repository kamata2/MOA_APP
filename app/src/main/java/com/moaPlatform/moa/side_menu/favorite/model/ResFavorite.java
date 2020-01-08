package com.moaPlatform.moa.side_menu.favorite.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class ResFavorite extends CommonModel {
    @SerializedName("storList")
    public List<SearchedStoreItems> storeItemsList = new ArrayList<>();
}
