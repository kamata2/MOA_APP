package com.moaPlatform.moa.util.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResCartList {
    // 장바구니 ID
    @SerializedName("cartId")
    public String cartId;
    // 사용자 ID
    @SerializedName("userId")
    private String userId;
    // 매장 ID
    @SerializedName("storId")
    private int storId;
    // 매장이름
    @SerializedName("storNm")
    public String storNm;
    // 매장이미지파일명
    @SerializedName("storImgAttchFileNm")
    private String storImgAttchFileNm;
    //이미지 경로
    @SerializedName("imageUrl")
    public String imageUrl;
    // 합계금액
    @SerializedName("totSumAmt")
    public int totSumAmt;
    // 평일배달비
    @SerializedName("wkdyDlvryPrice")
    private int wkdyDlvryPrice;
    // 장바구니 상세 옵션
    @SerializedName("cartDtlList")
    public List<ResShoppingCartDetailItem> resShoppingCartDetailList;
    // 타임 이벤트
    @SerializedName("timeEvntDtoList")
    private List<ResTimeEvent> resTimeEvents;
}
