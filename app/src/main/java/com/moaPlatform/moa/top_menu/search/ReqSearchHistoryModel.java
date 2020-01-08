package com.moaPlatform.moa.top_menu.search;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqSearchHistoryModel extends CommonModel {

    // 검색어로 매장 조회시 사용할 정보
    // 검색어
    @SerializedName("srchWord")
    private String searchKeyword;
    // 정렬
    @SerializedName("sort")
    private String sort;
    // 서비스 카테고리 코드
    @SerializedName("srvcCtgryCd")
    private String serviceCategoryCode;
    // 거리
    @SerializedName("distance")
    private String distance;

    // 검색어 삭제시 사용
    @SerializedName("rcntSrchWordId")
    public int removeSearchHistoryId;

    /**
     * 검색어로 매장 조회
     * @param searchKeyword
     * 검색어
     * @param sort
     * 정렬 기준
     * @param serviceCategoryCode
     * 서비스 카테고리 코드
     * @param distance
     * 거리
     */
    public void soteSearchInit(String searchKeyword, String sort, String serviceCategoryCode, String distance) {
        this.searchKeyword = searchKeyword;
        this.sort = sort;
        this.serviceCategoryCode = serviceCategoryCode;
        this.distance = distance;
    }

    public String serviceCategoryCode() {
        return serviceCategoryCode;
    }
}
