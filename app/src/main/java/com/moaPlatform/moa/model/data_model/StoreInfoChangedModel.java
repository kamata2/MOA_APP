package com.moaPlatform.moa.model.data_model;

import com.google.gson.annotations.SerializedName;

/**
 * 외식|배달 상세에서 리스트로 이동시
 * 리스트에서 즐겨찾기 및 리뷰 cnt 갱신처리를 위한 Model
 */
public class StoreInfoChangedModel {

    @SerializedName("position")         //adapter position
    public int position;

    @SerializedName("fovoriteCount")    //즐겨찾기 cnt
    public String fovoriteCount;

    @SerializedName("reviewCount")      //리뷰 cnt
    public String reviewCount;

    public String getFovoriteCount() {
        if(fovoriteCount != null && !fovoriteCount.equals("")){
            return fovoriteCount;
        }else{
            return "0";
        }
    }

    @Override
    public String toString() {
        return "StoreInfoChangedModel{" +
                "position=" + position +
                ", fovoriteCount='" + fovoriteCount + '\'' +
                ", reviewCount='" + reviewCount + '\'' +
                '}';
    }
}
