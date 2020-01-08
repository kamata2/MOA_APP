package com.moaPlatform.moa.side_menu.customercenter.faq.model;

import com.google.gson.annotations.SerializedName;

public class FaqDetailItems extends FaqModel {

    @SerializedName("faqDtlCd")
    public String faqDtlCd; // 타이틀

    @SerializedName("dtlQstnTitle")
    public String dtlQstnTitle; // 내용

    @SerializedName("dtlQstnAnsw")
    public String dtlQstnAnsw; // 작성일시

}