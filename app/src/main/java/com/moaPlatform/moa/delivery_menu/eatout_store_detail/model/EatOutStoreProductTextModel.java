package com.moaPlatform.moa.delivery_menu.eatout_store_detail.model;

import com.google.gson.annotations.SerializedName;

/**
 * [외식상세] 상품 이름 가격 정보
 */
public class EatOutStoreProductTextModel{
    @SerializedName("storProdNm")           public String storProdNm;   //상품이름
    @SerializedName("storProdPrice")        public int storProdPrice;   //가격

}
