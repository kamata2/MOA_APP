package com.moaPlatform.moa.bottom_menu.main.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.bottom_menu.main.model.list.EatOutShopList;
import com.moaPlatform.moa.bottom_menu.main.model.list.EatOutThemeList;

public class EatOutMainModel {

    @SerializedName("topBannerOne")
    public EatOutBannerModel eatOutBannerModelOne;

    @SerializedName("eatOutShopList")
    public EatOutShopList eatOutShopList;

    @SerializedName("topBannerTwo")
    public EatOutBannerModel eatOutBannerModelTwo;

    @SerializedName("eatOutThemeList")
    public EatOutThemeList eatOutThemeList;

}
