package com.moaPlatform.moa.side_menu.customercenter.faq.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class ResFaq extends CommonModel {

    @SerializedName("faqList")
    public List<FaqTitleItems> faqLists = new ArrayList<>();
}