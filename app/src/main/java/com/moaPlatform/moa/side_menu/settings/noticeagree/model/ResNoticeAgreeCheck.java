package com.moaPlatform.moa.side_menu.settings.noticeagree.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ResNoticeAgreeCheck extends CommonModel {

    @SerializedName("agrmntInfo")
    public NoticeAgreeCheckItems agrmntInfos;
}

