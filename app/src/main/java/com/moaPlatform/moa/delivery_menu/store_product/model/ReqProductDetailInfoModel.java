package com.moaPlatform.moa.delivery_menu.store_product.model;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqProductDetailInfoModel extends CommonModel {

    // 상품코드
    @SerializedName("storProdCd")
    public int storeProductCode;
    // 가맹점 id
    @SerializedName("storId")
    public int storeId;

    public void init(Intent storeIntent) {
        storeProductCode = storeIntent.getIntExtra(CodeTypeManager.StoreInfo.STORE_PRODUCT_CODE.toString(), 0);
        storeId = storeIntent.getIntExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), 0);
    }
}
