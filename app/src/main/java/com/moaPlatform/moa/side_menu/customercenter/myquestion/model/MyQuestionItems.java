package com.moaPlatform.moa.side_menu.customercenter.myquestion.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyQuestionItems {
    @SerializedName("inqryLstId")
    public int inqryLstId;   //문의내역_ID

    @SerializedName("userId")
    public String userId; // 사용자_ID

    @SerializedName("email")
    public String email;   // 이메일

    @SerializedName("nm")
    public String nm;   // 이름

    @SerializedName("mobiNum")
    public String mobiNum;   // 휴대전화번호

    @SerializedName("inqryTp")
    public String inqryTp;   // 문의유형

    @SerializedName("inqryTitle")
    public String inqryTitle;   // 문의제목

    @SerializedName("inqryCntnt")
    public String inqryCntnt;   // 문의내용

    @SerializedName("inqryDt")
    public String inqryDt;   // 문의일시

    @SerializedName("answCntnt")
    public String answCntnt;   // 답변내용

    @SerializedName("answDt")
    public String answDt;   // 답변일시

    @SerializedName("answStat")
    public String answStat;   // 답변 상태

    @SerializedName("inqryLstAttchFileList")
    public List<MyQuestionAttachFileItems> inqryLstAttchFileList = new ArrayList<>();   // 첨부파일 리스트

}

