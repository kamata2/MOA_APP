package com.moaPlatform.moa.delivery_menu.eatout_store_detail.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.model.res_model.ReviewModel;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.List;

/**
 * [외식] 상세정보
 */
public class ResEatOutStoreDetailInfo extends CommonModel {

    //가맹점 정보
    @SerializedName("storInfo")
    public EatOutStoreInfoModel eatOutStoreInfoModel;

    //상품리스트
    @SerializedName("rpsntProd")
    public List<EatOutStoreProductTextModel> productTextList;

    //이미지 리스트
    @SerializedName("rpsntProdImg")
    public List<EatOutStoreProductImageModel> imageList;

    //리뷰리스트
    @SerializedName("storUserRevwList")
    public List<ReviewModel> reviewModel;

}
