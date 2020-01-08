package com.moaPlatform.moa.delivery_menu.store_detail.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.StoreInfoModel;
import com.moaPlatform.moa.util.models.StoreProductInfoModel;

import java.util.ArrayList;

public class ResStoreDetailInfo extends CommonModel {

    // 가맹점 정보
    @SerializedName("storInfo")
    public StoreAddInfoModel storeInfoModel;

    // 대표 메뉴 리스트
    @SerializedName("rpsntProd")
    public ArrayList<StoreMenuInfoModel> storeRepresentativeMenuList;

    // 전체 메뉴
    @SerializedName("storProdCtgryList")
    public ArrayList<StoreMenuCategoryInfo> storeAllMenuList;

    // 가맹점 매뉴 정보
    public class StoreMenuInfoModel extends StoreProductInfoModel {
        // 상품 인포 타입
        @SerializedName("prodInfoTp")
        private String productInfoType;

        // 대표 상품 여부
        @SerializedName("rpsntProdYn")
        private String isRepresentative;
    }

    // 가맹점 메뉴 카테고리 정보
    public class StoreMenuCategoryInfo extends BaseModel {
        // 메장 상품 항복 id
        @SerializedName("storProdCtgryId")
        private int categoryId;

        // 매장 id
        @SerializedName("storId")
        private int storId;

        // 메장 상품 항목명
        @SerializedName("storProdCtgryNm")
        public String categoryName;

        // 순서
        @SerializedName("sqnc")
        private int sqnc;

        // 해당 카테고리의 메뉴 리스트
        @SerializedName("storProdList")
        public ArrayList<StoreMenuInfoModel> storeMenuList;
    }

    public class ShoppingCartCountModel extends CommonModel {
        // 장바구니 개수
        @SerializedName("cartCnt")
        public int count;
    }

    /**
     * 가맹점 정보 모델
     */
    public class StoreAddInfoModel extends StoreInfoModel {
        // 지도 이미지
        @SerializedName("mapImg")
        private String storeMapImg;

        // 도로명 기본 주소
        @SerializedName("roadNmDefltAddr")
        private String roeadNameAddress;

        // 위도
        @SerializedName("latu")
        private String latu;

        // 경도
        @SerializedName("lont")
        private String lont;

        public String getStoreMapImg() {
            return storeMapImg;
        }

        public String getRoeadNameAddress() {
            return roeadNameAddress;
        }

        public String getLatu() {
            return latu;
        }

        public String getLont() {
            return lont;
        }
    }

}
