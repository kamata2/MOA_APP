package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response;

import com.google.gson.annotations.SerializedName;

public class WalletInfoModel {
    @SerializedName("totalPointBal")    public int totalPointBal;       //총 포인트 금액
    @SerializedName("usablePointBal")   public int usablePointBal;      //사용가능 포인트 금액
    @SerializedName("lockPointBal")     public int lockPointBal;        //활성예정 포인트 금액
}

