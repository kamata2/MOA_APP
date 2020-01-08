package com.moaPlatform.moa.side_menu.usernotice.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class ResUserNotice extends CommonModel {
    @SerializedName("userNotiList")
    public List<UserNoticeItems> arrayList = new ArrayList<>();
}
