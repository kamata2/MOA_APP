package com.moaPlatform.moa.side_menu.favorite.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

/**
 * Created by jiwun on 2019-07-12.
 */
public class ReqBookmarkStoreRemove extends CommonModel {

    // 가망점 아이디
    @SerializedName("storId")
    private int storeId;

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
