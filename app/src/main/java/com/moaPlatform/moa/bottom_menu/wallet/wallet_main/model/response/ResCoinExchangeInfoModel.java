package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

/**
 * 서버에서 받은 코인 및 포인트 정보
 */
public class ResCoinExchangeInfoModel extends CommonModel {

    @SerializedName("moaCoin")
    public String moaCoin;                  //보유코인
    @SerializedName("goeatPoint")
    public String goeatPoint;               //보유 포인트(소수점 취급 안함)
    @SerializedName("swichingRatio")
    public String swichingRatio;            //전환 비율
    @SerializedName("oneDayLimit")
    public String oneDayLimit;              //1일 전환 한도
    @SerializedName("swichingFee")
    public String swichingFee;              //전환 수수료
    @SerializedName("pointTermsUrl")
    public String pointTermsUrl;            //약관 동의 URL
    @SerializedName("exchangedPointAmt")
    public String exchangedPointAmt;        //사용자가 변환한 포인트 금액


}
