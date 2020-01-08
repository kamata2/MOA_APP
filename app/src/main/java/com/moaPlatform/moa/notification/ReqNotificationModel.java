package com.moaPlatform.moa.notification;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqNotificationModel extends CommonModel {

    @SerializedName("fcmToken")
    public String fcmToken;

}
