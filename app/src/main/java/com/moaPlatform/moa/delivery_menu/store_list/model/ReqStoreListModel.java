package com.moaPlatform.moa.delivery_menu.store_list.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqStoreListModel extends CommonModel {

    // 서브 메뉴 코드
    @SerializedName("subMenuCd")
    private String subMenuCode;
    // 거리
    @SerializedName("distance")
    private String distance;
    // 햔제 페이지
    @SerializedName("currentPage")
    public int currentPage;
    // 정렬
    @SerializedName("sort")
    public String sort = "0";

    public void init(String subMenuCode, String distance, int currentPage) {
        this.subMenuCode = subMenuCode;
        this.distance = distance;
        this.currentPage = currentPage;
    }
}
