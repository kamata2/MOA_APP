package com.moaPlatform.moa.delivery_menu.store_product.model.res;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.delivery_menu.store_product.model.AddProductOptionModel;

import java.util.ArrayList;

public class ResStorAddProdListModel extends AddProductOptionModel {
//    //매장추가상품항목_ID
//    @SerializedName("storAddProdCtgryId")
//    public int storAddProdCtgryId;
    //매장상품항목명
    @SerializedName("storAddProdCtgryNm")
    public String storAddProdCtgryNm;
    //매장_ID
    @SerializedName("storId")
    private int storId;
    //순서
    @SerializedName("sqnc")
    private int sqnc;
//    //중복선택가능개수
//    @SerializedName("dplctChoiceEnblCnt")
//    public String dplctChoiceEnblCnt;
    // 매장 추가 상품 리스트
    @SerializedName("storAddProdOptList")
    public ArrayList<ResStorAddProdOptListModel> storAddProdOptList;
}
