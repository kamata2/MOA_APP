package com.moaPlatform.moa.delivery_menu.store_product.model.req;

import com.google.gson.annotations.SerializedName;

public class ReqCartDtlOptDto {
    // 매장추가상품항목_ID
    @SerializedName("storAddProdCtgryId")
    private int storAddProdCtgryId;
    // 추가옵션상품ID
    @SerializedName("storAddProdOptId")
    private int storAddProdOptId;
}
