package com.moaPlatform.moa.delivery_menu.store_product.model.res;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.delivery_menu.store_product.model.AddProductOptionModel;

public class ResStorAddProdOptListModel extends AddProductOptionModel {
    //추가옵션상품ID
    @SerializedName("storAddProdOptId")
    public int storAddProdOptId;
    //추가옵션상품명
    @SerializedName("storAddProdOptNm")
    public String storAddProdOptNm;
    //추가옵션상품가격
    @SerializedName("storAddProdOptPrice")
    public int storAddProdOptPrice;
}
