package com.moaPlatform.moa.delivery_menu.store_list.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;

public class ResBannerModel extends CommonModel {

    @SerializedName("topbannerInfo")
    public ArrayList<BannerInfoModel> topbannerList;

    public class BannerInfoModel {
        @SerializedName("bannerImgUrl")
        public String thumbNail;
        @SerializedName("bannerWebViewUrl")
        public String moveUrl;
    }

}
