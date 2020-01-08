package com.moaPlatform.moa.side_menu;

import com.google.gson.annotations.SerializedName;

public class OrderProductModel {

    @SerializedName("storProdNm")
    public String storProdNm;	// 매장상품명

    @SerializedName("cnt")
    public int cnt;             // 수량

}
