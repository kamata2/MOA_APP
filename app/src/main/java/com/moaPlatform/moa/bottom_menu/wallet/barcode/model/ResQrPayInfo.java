package com.moaPlatform.moa.bottom_menu.wallet.barcode.model;

import com.google.gson.annotations.SerializedName;

public class ResQrPayInfo {

    @SerializedName("resultValue")
    public String  resultValue; // 결과값

    @SerializedName("storNm")
    public String  storNm; // 매장이름

    @SerializedName("saveRate")
    public int saveRate; // 적립률

    @SerializedName("pointBal")
    public int pointBal; //

    // 서브 메뉴 코드
    @SerializedName("subMenuCd")
    public String subMenuCode;

    @SerializedName("storExists")
    public boolean  storExists; // 가맹점 존재여부

}
