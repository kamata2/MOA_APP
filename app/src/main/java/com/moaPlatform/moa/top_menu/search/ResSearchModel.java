package com.moaPlatform.moa.top_menu.search;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.StoreInfoModel;

import java.util.ArrayList;

public class ResSearchModel extends CommonModel {

    @SerializedName("rcntSrchWord")
    ArrayList<SearchHistory> keywordList;

    @SerializedName("srchStorList")
    ArrayList<StoreInfoModel> searchStoreList;

    public class SearchHistory {
        // 최근 검색어 아이디
        @SerializedName("rcntSrchWordId")
        public int keywordId;
        // 최근 검색어
        @SerializedName("srchWord")
        public String keyword;
    }

}
