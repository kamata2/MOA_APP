package com.moaPlatform.moa.bottom_menu.shopping_cart.main;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.ProductAddOption;

import java.util.ArrayList;

/**
 * 서버에 보낼 장바구니 데이터
 */
public class ReqInputShoppingCart extends CommonModel {
    // 매장 ID
    @SerializedName("storId")
    private int storeId;
    // 상품코드
    @SerializedName("storProdCd")
    private int storeProductCode;
    //옵션상품코드
    @SerializedName("storProdOptId")
    private int storeProductOptionId;
    // 수량
    @SerializedName("cnt")
    private int cnt;
    // 장바구니 상세 옵션 리스트
    @SerializedName("cartDtlOptDto")
    private ArrayList<ProductAddOption> reqInputDetailShoppingCartOptions = new ArrayList<>();

    public void init(int storeId, int storeProductCode ,int storeProductOptionId,  int quantity, ArrayList<ProductAddOption> productAddOptions) {
        this.storeId = storeId;
        this.storeProductCode = storeProductCode;
        this.storeProductOptionId = storeProductOptionId;
        cnt = quantity;
        reqInputDetailShoppingCartOptions = productAddOptions;
    }
}
