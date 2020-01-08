package com.moaPlatform.moa.side_menu.favorite.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqFavorite extends CommonModel {
    @SerializedName("storId")
    public int storId;        //매장ID
}
