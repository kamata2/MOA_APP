package com.moaPlatform.moa.util.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResShoppingCartDetailItem {
    // 장바구니 상세 ID
    @SerializedName("cartDtlId")
    private int cartDtlId;
    // 장바구니_ID
    @SerializedName("cartId")
    private String cartId;
    // 수량
    @SerializedName("cnt")
    public int cnt;
    // 합계금액
    @SerializedName("sumAmt")
    private int sumAmt;
    // 매장상품코드
    @SerializedName("storProdCd")
    private int storProdCd;
    // 매장상품명
    @SerializedName("storProdNm")
    public String storProdNm;
    // 매장상품가격
    @SerializedName("storProdPrice")
    private String storProdPrice;
    // 매장상품옵션_ID
    @SerializedName("storProdOptId")
    private int storProdOptId;
    //매장상품옵션명
    @SerializedName("storProdOptNm")
    private String storProdOptNm;
    //매장상품옵션가격
    @SerializedName("storProdOptPrice")
    private String storProdOptPrice;
    // 장바구니 상세 옵션
    @SerializedName("cartDtlOptList")
    public ArrayList<ResShoppingCartDetailOptionItem> resShoppingCartDetailOptionItems;

}
