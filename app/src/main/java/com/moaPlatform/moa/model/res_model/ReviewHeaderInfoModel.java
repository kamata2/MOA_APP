package com.moaPlatform.moa.model.res_model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.side_menu.OrderProductModel;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;
import java.util.List;

/**
 * [배달/외식 리뷰] 상품 이미지 정보
 */
public class ReviewHeaderInfoModel extends CommonModel {

    @SerializedName("userRevwId")       public String userRevwId;           // 사용자리뷰ID
    @SerializedName("storId")           public int storId;                  // 매장ID
    @SerializedName("orderId")          public String orderId;              // 주문ID
    @SerializedName("totEvalScor")      public String totEvalScor;           // 총평점
    @SerializedName("tastEvalScor")     public String tastEvalScor;            // 맛평점
    @SerializedName("volumEvalScor")    public String volumEvalScor;           // 양평점
    @SerializedName("dlvryEvalScor")    public String dlvryEvalScor;           // 배달평점
    @SerializedName("cntnt")            public String cntnt;                    // 내용
    @SerializedName("affiStorCmnt")     public String affiStorCmnt;             // 가맹점댓글
    @SerializedName("cmntWritDt")       public String cmntWritDt;               // 댓글작성일시
    @SerializedName("writDt")           public String writDt;                   // 작성일시
    @SerializedName("storNm")           public String storNm;                   // 매장 이름
    @SerializedName("storImgAttchFileNm")   public String storImgAttchFileNm;       // 매장이미지첨부파일명
    @SerializedName("storImgUrl")           public String storImgUrl;               // 매장이미지경로
    @SerializedName("profImgNm")            public String profImgNm;                // 프로필이미지
    @SerializedName("nick")                 public String nick;                     // 닉네임
    @SerializedName("sympCnt")              public int sympCnt;                     // 공감개수
    @SerializedName("revwRmnngDays")        public int revwRmnngDays;               // 리뷰 남은 일수
    @SerializedName("userRevwFileList")     public List<ReviewFile> userRevwFileList;
    @SerializedName("orderProdList")        public List<OrderProductModel> orderedGoodsList;    //주문된 상품 목록

    /**
     * 주문 상품 목록을 리턴한다.
     * @return
     */
    public String getOrderedGoodsList() {
        List<String> goodsList = new ArrayList<>();
        if(orderedGoodsList != null){
            for (int i = 0; i < orderedGoodsList.size(); i++) {
                goodsList.add(orderedGoodsList.get(i).storProdNm);
            }
        }
        return TextUtils.join(", ", goodsList);
    }
}
