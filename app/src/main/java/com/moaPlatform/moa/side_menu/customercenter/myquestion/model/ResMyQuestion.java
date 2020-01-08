package com.moaPlatform.moa.side_menu.customercenter.myquestion.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class ResMyQuestion extends CommonModel {
    @SerializedName("inqryList")
    public List<MyQuestionItems> InqryLists = new ArrayList<>();
}

