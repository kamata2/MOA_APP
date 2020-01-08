package com.moaPlatform.moa.delivery_menu.eatout_store_list.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqEatOutStoreListModel extends CommonModel {

    //배너 타입 0:배너 1:테마 2:상점리스트
    @SerializedName("layoutTp")
    private String layoutTp;

    //배너 구분
    @SerializedName("bannerSepaCntnt")
    private String bannerSepaCntnt;

    //페이지
    @SerializedName("currentPage")
    public int currentPage;

    @SerializedName("sort")
    public String sort = "0";

    public void init(String layoutTp, String bannerSepaCntnt, int currentPage) {
        this.layoutTp = layoutTp;
        this.bannerSepaCntnt = bannerSepaCntnt;
        this.currentPage = currentPage;
    }
}

