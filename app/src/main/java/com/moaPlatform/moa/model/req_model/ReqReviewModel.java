package com.moaPlatform.moa.model.req_model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

/**
 * 리뷰 수정하기 request model
 */
public class ReqReviewModel extends CommonModel {

    //리뷰 카테고리 delivery | eatout
    @SerializedName("sepaValue")
    public String reviewCategory;

    // 사용자리뷰ID
    @SerializedName("userRevwId")
    public String userRevwId;

}
