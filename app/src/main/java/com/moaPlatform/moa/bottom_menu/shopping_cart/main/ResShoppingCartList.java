package com.moaPlatform.moa.bottom_menu.shopping_cart.main;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.delivery_menu.store_product.model.ResStoreProductDetailInfoModel;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.ResCartList;
import com.moaPlatform.moa.util.models.StoreInfoModel;

import java.util.ArrayList;

public class ResShoppingCartList extends CommonModel {
    // 장바구니 아이디
    @SerializedName("cartId")
    public String shoppingCartId;
    // 장바구니 리스트
    @SerializedName("cartList")
    public ArrayList<ShoppingCartModel> shoppingCartList;
    @SerializedName("cartDtlInfo")
    public ResCartList resCartDtlInfo;

    public class ShoppingCartModel extends StoreInfoModel {
        // 장바구니 id
        @SerializedName("cartId")
        public String shoppingCartId;
        // 합계 금액
        @SerializedName("totSumAmt")
        public int totalAmount;
        // 장방구니 상세 리스트
        @SerializedName("cartDtlList")
        public ArrayList<ShoppingCartDetailInfoModel> shoppingCartDetailList;
    }

    public class ShoppingCartDetailInfoModel {
        // 장바구니 상세 id
        @SerializedName("cartDtlId")
        private int detailId;
        // 장바구니 아이디
        @SerializedName("cartId")
        private String shoppingCartId;
        // 수량
        @SerializedName("cnt")
        public int cnt;
        // 합계 금액
        @SerializedName("sumAmt")
        private int totalAmount;
        // 매장 상품명
        @SerializedName("storProdNm")
        public String storeProductName;
        // 매장 상품 가격
        @SerializedName("storProdPrice")
        private String storeProductPrice;
        // 매장 상품 옵션
        @SerializedName("storProdOptId")
        private String storeProductOptionId;
        // 매장 옵션 명
        @SerializedName("storProdOptNm")
        private String storeProductOptionName;
        // 매장 옵션 가격
        @SerializedName("storProdOptPrice")
        private String storeProductOptionPrice;
        // 장바구니 상세 옵션
        @SerializedName("cartDtlOptList")
        public ArrayList<ResStoreProductDetailInfoModel.AddProductCategoryModel> shoppingCartDetailOptionList;
    }
}
