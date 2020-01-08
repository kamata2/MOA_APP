package com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.List;

public class ReqShoppingCartDetailModel extends CommonModel {
    // 장바구니 ID
    @SerializedName("cartId")
    private String shoppingCartId;
    // 장바구니 상세 id
    @SerializedName("cartDtlId")
    private String shoppingCartDetailId;
    // 장바구니 상세 id 들
    @SerializedName("cartDtlIds")
    private List<String> shoppingCartDetailIds;

    /**
     * 장바구니 아이디 세팅
     *
     * @param shoppingCartId 장바구니 아이디
     */
    public void setShoppingCartId(String shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public void setShoppingCartDetailIds(List<String> shoppingCartDetailIds) {
        this.shoppingCartDetailIds = shoppingCartDetailIds;
    }

    /**
     * 장바구나 상세 아이디 세팅
     *
     * @param shoppingCartDetailId 장바구니 상세 아이디
     */
    public void setShoppingCartDetailId(String shoppingCartDetailId) {
        this.shoppingCartDetailId = shoppingCartDetailId;
    }
}
