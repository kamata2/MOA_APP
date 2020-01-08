package com.moaPlatform.moa.side_menu.customercenter.myquestion.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqMyQuestion extends CommonModel {

    @SerializedName("mmbrSepa")
    public String mmbrSepa; // 회원구분 (회원 : 01, 비회원 : 02)
}

