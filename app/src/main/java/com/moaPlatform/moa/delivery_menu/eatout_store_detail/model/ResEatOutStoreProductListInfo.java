package com.moaPlatform.moa.delivery_menu.eatout_store_detail.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.delivery_menu.store_detail.model.ResStoreDetailInfo;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;

/**
 * [외식] 상품 리스트
 */
public class ResEatOutStoreProductListInfo extends CommonModel {

    // 대표 메뉴 리스트
    @SerializedName("rpsntProd")
    public ArrayList<ResStoreDetailInfo.StoreMenuInfoModel> storeRepresentativeMenuList;

    // 전체 메뉴
    @SerializedName("storProdCtgryList")
    public ArrayList<ResStoreDetailInfo.StoreMenuCategoryInfo> storeAllMenuList;
}
