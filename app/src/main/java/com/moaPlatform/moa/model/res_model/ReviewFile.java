package com.moaPlatform.moa.model.res_model;

import com.google.gson.annotations.SerializedName;

public class ReviewFile {

    @SerializedName("userRevwAttchFileId")
    public String userRevwAttchFileId;      //사진파일 유일 Key 값

    @SerializedName("userRevwAttchFileNm")
    public String userRevwAttchFileNm;      //첨부파일명

    @SerializedName("imageUrl")
    public String imageUrl;	//사용자 리뷰 이미지 url
}
