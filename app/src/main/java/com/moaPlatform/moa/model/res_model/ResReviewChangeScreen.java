package com.moaPlatform.moa.model.res_model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

/**
 * [배달/외식 리뷰] 리뷰수정 data
 */
public class ResReviewChangeScreen extends CommonModel {

    @SerializedName("modfUserRevwInfo")
    public ReviewHeaderInfoModel reviewHeaderInfoModel;

}
