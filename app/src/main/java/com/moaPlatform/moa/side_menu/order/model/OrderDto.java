package com.moaPlatform.moa.side_menu.order.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.side_menu.OrderProductModel;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {

    @SerializedName("orderId")
    public String orderId;                 // 주문_ID

    @SerializedName("storId")
    public int storId;                     // 매장_ID

    @SerializedName("storNm")
    public String storNm;            // 매장명

    @SerializedName("storImgAttchFileNm")
    public String storImgAttchFileNm;        // 매장이미지파일명

    @SerializedName("orderDt")
    public String orderDt;                 // 주문일시

    @SerializedName("orderStatCd")
    public String orderStatCd;             // 주문상태코드

    @SerializedName("orderStatCdNm")
    public String orderStatCdNm; //주문 상태 코드명

    @SerializedName("revwWritYn")
    public String revwWritYn;              // 리뷰작성여부

    @SerializedName("imageUrl")
    public String imageUrl;        // 이미지경로

    @SerializedName("revwRmnngDays")
    public int revwRmnngDays;        // 리뷰남은일수

    @SerializedName("orderDtlList")
    public List<OrderProductModel> orderDtlList = new ArrayList<>();

    /**
     * @return 주문 상품 목록을 리터
     */
    public String getOrderMenuList() {
        List<String> menuList = new ArrayList<>();
        StringBuilder menuStringBuilder = new StringBuilder();
        for (OrderProductModel orderProductModel : orderDtlList) {
            menuStringBuilder.append(orderProductModel.storProdNm).append("(").append(orderProductModel.cnt).append(")");
            menuList.add(menuStringBuilder.toString());
            menuStringBuilder.setLength(0);
        }
        return TextUtils.join(" , ", menuList);
    }

}
