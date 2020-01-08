package com.moaPlatform.moa.bottom_menu.main.model.list;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.bottom_menu.main.model.EatOutThemeModel;

import java.util.List;

public class EatOutThemeList {
    @SerializedName("bTypeLayoutTitle")
    public String themeTitle;
    @SerializedName("eatOutBTypeList")
    public List<EatOutThemeModel> themeList;
}
