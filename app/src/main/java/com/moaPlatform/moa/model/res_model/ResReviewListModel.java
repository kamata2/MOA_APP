package com.moaPlatform.moa.model.res_model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.List;

/**
 * [배달/외식 리뷰] 리뷰리스트
 */
public class ResReviewListModel extends CommonModel {

    @SerializedName("revwCnt")      //리뷰개수
    public int reviewCnt;

    @SerializedName("storUserRevwList")
    public List<ReviewModel> reviewList;

    @SerializedName("storRevwInfo")
    public ReviewHeaderInfoModel reviewHeaderInfoModel;

}
