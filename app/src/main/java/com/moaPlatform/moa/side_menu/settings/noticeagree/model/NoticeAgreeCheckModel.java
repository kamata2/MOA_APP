package com.moaPlatform.moa.side_menu.settings.noticeagree.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class NoticeAgreeCheckModel extends CommonModel {

    @SerializedName("cpDscntEvntBnftNotiAgrmntYn")
    public String cpDscntEvntBnftNotiAgrmntYn; //광고정보알림동의여부

    @SerializedName("nightAdInfoNotiAgrmntYn")
    public String nightAdInfoNotiAgrmntYn;  //야간광고정보알림동의여부

    @SerializedName("inqAnswNotiAgrmntYn")
    public String inqAnswNotiAgrmntYn;  	//문의답변알림동의여부
}

