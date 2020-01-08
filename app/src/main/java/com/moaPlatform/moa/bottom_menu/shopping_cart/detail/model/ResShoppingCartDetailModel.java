package com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.StoreInfoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResShoppingCartDetailModel extends CommonModel {
    /// 장바구니 상세 정보
    @SerializedName("cartDtlInfo")
    public ShoppingCartInfoModel shoppingCartDetailInfoModel;

    /**
     * 장바구니 정보
     */
    public class ShoppingCartInfoModel extends StoreInfoModel {
        // 장바구니 아이디
        @SerializedName("cartId")
        private String shoppingCartId;
        // 합계금액
        @SerializedName("totSumAmt")
        private int totalAmount;
        // 생성일시
        @SerializedName("creeDt")
        private String createDate;
        // 장바구니 상세 리스트
        @SerializedName("cartDtlList")
        public List<ShoppingCartDetailModel> shoppingCartDetailList;
    }

    /**
     * 장바구니 상세 정보
     */
    public class ShoppingCartDetailModel extends BaseModel {
        // 장바구니 상세 아이디
        @SerializedName("cartDtlId")
        public String shoppingCartDetailId;
        // 장바구니 아이디
        @SerializedName("cartId")
        private String shoppingCartId;
        // 수량
        @SerializedName("cnt")
        private int cnt;
        // 합계 금액
        @SerializedName("sumAmt")
        public int sumAmt;
        // 매장 상품 코드
        @SerializedName("storProdCd")
        public int storeProductCode;
        // 매장 상품 명
        @SerializedName("storProdNm")
        public String sotreProductName;
        // 매장 상품 가격
        @SerializedName("storProdPrice")
        private int storeProductPrice;
        // 매장 상품 옵션 id
        @SerializedName("storProdOptId")
        public int storeProductOptionId;
        // 매장 상품 옵션 명
        @SerializedName("storProdOptNm")
        private String sotreProductOptionName;
        // 매장 상품 옵션 가격
        @SerializedName("storProdOptPrice")
        private int storeProductOptionPrice;
        // 장바구니 상세 옵션
        @SerializedName("cartDtlOptList")
        public ArrayList<ShoppingCartDetailOption> shoppingCartDetailOptionList;

        /**
         * @return 상품 수량을 스트링 형식으로 반환
         */
        public String getCntToString() {
            return String.valueOf(cnt);
        }

        /**
         * @param addOptionPrice 추가 옵션 가격
         * @return 상품 총 가격
         */
        public int getTotalPrice(int addOptionPrice) {
            return (getProductPrice() + addOptionPrice) * cnt;
        }

        /**
         * @return 상품 개수 반환
         */
        public int getCnt() {
            return cnt;
        }

        /**
         * @param cnt 상품 개수을 세팅
         */
        public void setCnt(int cnt) {
            this.cnt = cnt;
        }

        /**
         * @return 기본 가격 + 기본 옵션 가격을 더해서 반환
         */
        public int getProductPrice() {
            return storeProductPrice + storeProductOptionPrice;
        }

        // 추가 상품 리스트
        public final String ADD_PRODUCT_LIST = "addProductList";
        // 추가 상품 가격
        public final String ADD_PRODUCT_TOTAL_PRICE = "addpridcutTotalPrice";
        /**
         * @return 추가옵션 리스트와 추가 옵션 가격을 map 형태로 반환
         */
        public HashMap<String, String> getAddProductMap() {
            List<String> productOptionList = new ArrayList<>();
            int addOptionPrice = 0;
            for (ResShoppingCartDetailModel.ShoppingCartDetailOption shoppingCartDetailOption : shoppingCartDetailOptionList) {
                for (ResShoppingCartDetailModel.ShoppingCartAddOption shoppingCartAddOption : shoppingCartDetailOption.shoppingCartAddOptionList) {
                    productOptionList.add(shoppingCartAddOption.productOptionName);
                    addOptionPrice += shoppingCartAddOption.productOptionPrice;
                }
            }
            HashMap<String, String> addProductMap = new HashMap<>();
            addProductMap.put(ADD_PRODUCT_LIST, TextUtils.join(", ", productOptionList));
            addProductMap.put(ADD_PRODUCT_TOTAL_PRICE, String.valueOf(addOptionPrice));
            return addProductMap;

        }
    }

    /**
     * 장바구니 옵션
     */
    public class ShoppingCartDetailOption {
        // 장바구니 상세 id
        @SerializedName("cartDtlId")
        public int shoppingCartDetailId;
        // 장바구니 id
        @SerializedName("cartId")
        public String shoppingCartId;
        // 매장 상품 코드
        @SerializedName("storProdCd")
        private int storeProductCode;
        // 매장 추가 상품 카테고리
        @SerializedName("storAddProdCtgryId")
        public int addProductCategoryId;
        // 매장 추가 상품 항목 명
        @SerializedName("storAddProdCtgryNm")
        private String addProductCategoryName;
        // 추가 옵션 리스트
        @SerializedName("storAddProdOptList")
        public ArrayList<ShoppingCartAddOption> shoppingCartAddOptionList;
    }

    /**
     * 장바구니 추가 옵션
     */
    public class ShoppingCartAddOption {
        // 추가 옵션 상품 명
        @SerializedName("storAddProdOptNm")
        public String productOptionName;
        // 추가 옵션 상품 가격
        @SerializedName("storAddProdOptPrice")
        public int productOptionPrice;
        // 추가 옵션 상품 id
        @SerializedName("storAddProdOptId")
        public int productOptionId;
    }

    /**
     * @return 장바구니 상세 리스트 반환
     */
    public List<ShoppingCartDetailModel> getShoppingCartDetailList() {
        return shoppingCartDetailInfoModel.shoppingCartDetailList;
    }

}

