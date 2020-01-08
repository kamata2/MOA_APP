package com.moaPlatform.moa.delivery_menu.store_product.model.res;

import com.google.gson.annotations.SerializedName;

/**
 * 서버와 통신해서
 * 상품 정보를 담아둔 모델
 */
public class ResStorProdDtlInfoModel {
    //상품코드
    @SerializedName("storProdCd")
    public int storProdCd;
    //상품이름
    @SerializedName("storProdNm")
    public String storProdNm;
    //상품설명
    @SerializedName("prodExpn")
    public String prodExpn;
    //가격
    @SerializedName("storProdPrice")
    public int storProdPrice;
    //상품이미지첨부파일ID
    @SerializedName("prodImgAttchFileId")
    public String prodImgAttchFileId;
    //이미지URL
    @SerializedName("imageUrl")
    public String imageUrl;
    //적립률
    @SerializedName("saveRate")
    public String saveRate;
    //배달비
    @SerializedName("wkdyDlvryPrice")
    public String wkdyDlvryPrice;

}
