package com.moaPlatform.moa.util.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 상품의 추가옵션을 저장할 모델
 * 장바구니 및 결제페이지, 매뉴 상세페이지등등에서 사용
 */
public class ProductAddOption implements Serializable {
    // 매장추가상품항목_ID
    @SerializedName("storAddProdCtgryId")
    private int storAddProdCtgryId;
    // 추가옵션상품ID
    @SerializedName("storAddProdOptId")
    private int storAddProdOptId;

    public void init(int storAddProdCtgryId, int storAddProdOptId) {
        this.storAddProdCtgryId = storAddProdCtgryId;
        this.storAddProdOptId = storAddProdOptId;
    }
}
