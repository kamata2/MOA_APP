package com.moaPlatform.moa.model.res_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 리뷰정보
 */
public class ReviewModel {

    @SerializedName("storId")                   public int storId;      //매장ID
    @SerializedName("storNm")                   public String storNm;   //매장이름
    @SerializedName("storImgAttchFileNm")       public String storImgAttchFileNm;   //매장 이미지 첨부파일명
    @SerializedName("storImgUrl")               public String storImgUrl;   //매장 이미지 경로
    @SerializedName("orderId")                  public String orderId;      //주문 id
    @SerializedName("userRevwId")               public String userRevwId;   //사용자 리뷰 id
    @SerializedName("writDt")                   public String writDt;       //작성일시
    @SerializedName("cntnt")                    public String cntnt;        //내용
    @SerializedName("affiStorCmnt")             public String affiStorCmnt; //가맹점 댓글
    @SerializedName("cmntWritDt")               public String cmntWritDt;   //댓글 작성일시
    @SerializedName("userId")                   public String userId;       //사용자 id
    @SerializedName("profImgNm")                public String profImgNm;    //프로필 이름
    @SerializedName("profImgUrl")               public String profImgUrl;   //프로필 이미지 경로
    @SerializedName("nick")                     public String nick;         //닉네임
    @SerializedName("totEvalScor")              public float totEvalScor;   //총 평점
    @SerializedName("tastEvalScor")             public int tastEvalScor;    //맛 평점
    @SerializedName("volumEvalScor")            public int volumEvalScor;   //양 평점
    @SerializedName("dlvryEvalScor")            public int dlvryEvalScor;   //배달 평점
    @SerializedName("sympCnt")                  public int sympCnt;         //공감 CNT
    @SerializedName("userRevwFileList")         public List<ReviewImageModel> userRevwFileList;  //첨부파일 List

    @Override
    public String toString() {
        return "ReviewModel{" +
                "storId=" + storId +
                ", storNm='" + storNm + '\'' +
                ", storImgAttchFileNm='" + storImgAttchFileNm + '\'' +
                ", storImgUrl='" + storImgUrl + '\'' +
                ", orderId='" + orderId + '\'' +
                ", userRevwId='" + userRevwId + '\'' +
                ", writDt='" + writDt + '\'' +
                ", cntnt='" + cntnt + '\'' +
                ", affiStorCmnt='" + affiStorCmnt + '\'' +
                ", cmntWritDt='" + cmntWritDt + '\'' +
                ", userId='" + userId + '\'' +
                ", profImgNm='" + profImgNm + '\'' +
                ", profImgUrl='" + profImgUrl + '\'' +
                ", nick='" + nick + '\'' +
                ", totEvalScor=" + totEvalScor +
                ", tastEvalScor=" + tastEvalScor +
                ", volumEvalScor=" + volumEvalScor +
                ", dlvryEvalScor=" + dlvryEvalScor +
                ", sympCnt=" + sympCnt +
                ", userRevwFileList=" + userRevwFileList +
                '}';
    }
}
