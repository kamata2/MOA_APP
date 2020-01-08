package com.moaPlatform.moa.side_menu.eventnotice.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class ResEventOrNotice extends CommonModel {

    @SerializedName("evntNoticeList")
    public List<EventOrNoticeItems> noticeList = new ArrayList<>();

}