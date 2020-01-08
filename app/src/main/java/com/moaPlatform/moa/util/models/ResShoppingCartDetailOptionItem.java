package com.moaPlatform.moa.util.models;

import com.google.gson.annotations.SerializedName;

public class ResShoppingCartDetailOptionItem {
    // 매장추가상품항목_ID
    @SerializedName("storAddProdCtgryId")
    private int storAddProdCtgryId;
    //매장추가상품항목명
    @SerializedName("storAddProdCtgryNm")
    private String storAddProdCtgryNm;
    // 매장추가상품옵션_ID
    @SerializedName("storAddProdOptId")
    private int storAddProdOptId;
    //추가옵션상품명
    @SerializedName("storAddProdOptNm")
    public String storAddProdOptNm;
    //추가옵션상품가격
    @SerializedName("storAddProdOptPrice")
    private int storAddProdOptPrice;
}
