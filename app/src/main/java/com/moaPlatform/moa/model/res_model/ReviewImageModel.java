package com.moaPlatform.moa.model.res_model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;


/**
 * [배달/외식 리뷰] 리뷰 첨부 파일 정보
 */
public class ReviewImageModel extends CommonModel {
    @SerializedName("userRevwAttchFileId")  //IMAGE ID
    public String userRevwAttchFileId;
    @SerializedName("userRevwAttchFileNm")  //IMAGE NAME
    public String userRevwAttchFileNm;
    @SerializedName("imageUrl")             //IMAGE URL
    public String imageUrl;


    @Override
    public String toString() {
        return "ReviewImageModel{" +
                "userRevwAttchFileId='" + userRevwAttchFileId + '\'' +
                ", userRevwAttchFileNm='" + userRevwAttchFileNm + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
