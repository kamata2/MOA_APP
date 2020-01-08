package com.moaPlatform.moa.model.req_model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqStoreInfoModel extends CommonModel {

    // 가맹점 아이디
    @SerializedName("storId")
    public int storeId;

}
