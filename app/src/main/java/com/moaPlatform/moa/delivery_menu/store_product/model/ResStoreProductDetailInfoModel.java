package com.moaPlatform.moa.delivery_menu.store_product.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.StoreProductInfoModel;

import java.util.ArrayList;

public class ResStoreProductDetailInfoModel extends CommonModel {
    // 상품 상세 정보
    @SerializedName("storProdDtlInfo")
    public ProductDetailInfoModel productDetailInfoModel;
    //상품 옵션
    @SerializedName("storProdOptList")
    public ArrayList<DefaultProductOptionModel> defaultProductOptionList;
    // 추가 상품항목
    @SerializedName("storAddProdCtgryList")
    public ArrayList<AddProductCategoryModel> storeAddProdList;

    // 상품 상세 정보 모델
    public class ProductDetailInfoModel extends StoreProductInfoModel {
        // 적립률
        @SerializedName("saveRate")
        public String saveRate;
        // 배달비
        @SerializedName("wkdyDlvryPrice")
        public String deliveryPrice;
    }

    public class DefaultProductOptionModel extends AddProductOptionModel {
        // 옵션상품ID
        @SerializedName(value = "optionId", alternate = {"storProdOptId", "storAddProdOptId"})
        public int productOptionId;
        // 옵션상품명
        @SerializedName(value = "optionName", alternate = {"storProdOptNm", "storAddProdOptNm"})
        public String productOptionName;
        // 옵션상품가격
        @SerializedName(value = "optionPrice", alternate = {"storProdOptPrice", "storAddProdOptPrice"})
        public int productOptionPrice;
    }

    public class AddProductCategoryModel extends AddProductOptionModel{
        // 매장 추가 상품 카테고리 이름
        @SerializedName("storAddProdCtgryNm")
        public String addProductCategoryName;
        // 매장 아이디
        @SerializedName("storId")
        public int StoreId;
        // 순서
        @SerializedName("sqnc")
        public int sqnc;
        // 매장 추가 상품 옵션 항목
        @SerializedName("storAddProdOptList")
        public ArrayList<DefaultProductOptionModel> addProductOptionItemList;
    }

}
