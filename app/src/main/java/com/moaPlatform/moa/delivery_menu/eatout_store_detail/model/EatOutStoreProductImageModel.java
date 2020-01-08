package com.moaPlatform.moa.delivery_menu.eatout_store_detail.model;

import com.google.gson.annotations.SerializedName;

/**
 * [외식상세] 상품 이미지 정보
 */
public class EatOutStoreProductImageModel{

    //@SerializedName("prodImgAttchFileNm")   public String prodImgAttchFileNm;   //상품이미지첨부파일명
    @SerializedName("imageUrl")             public String imageUrl;             //이미지 주소

    @Override
    public String toString() {
        return "EatOutStoreProductImageModel{" +
                "imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
