package com.moaPlatform.moa.side_menu.customercenter.myquestion.model;

import com.google.gson.annotations.SerializedName;

public class MyQuestionAttachFileItems {

    @SerializedName("inqryLstId")
    public int inqryLstId;   // 문의내역_ID

    @SerializedName("inqryLstAttchFileId")
    public int inqryLstAttchFileId; // 문의내역첨부파일_ID

    @SerializedName("inqryLstAttchFileNm")
    public String inqryLstAttchFileNm;    // 문의내역첨부파일명

    @SerializedName("imageUrl")
    public String imageUrl;  //이미지 경로

}

