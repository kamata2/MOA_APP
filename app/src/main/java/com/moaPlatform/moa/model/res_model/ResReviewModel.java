package com.moaPlatform.moa.model.res_model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ResReviewModel extends CommonModel {

    @SerializedName("userRevwModInfo")
    public ReviewModel reviewModel;

    @Override
    public String toString() {
        return "ResReviewModel{" +
                "reviewModel=" + reviewModel +
                '}';
    }
}
