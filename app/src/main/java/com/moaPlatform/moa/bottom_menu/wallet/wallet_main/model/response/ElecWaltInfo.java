package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * 서버에서 받은 코인 및 포인트 정보
 */
public class ElecWaltInfo {
    // 코인 잔액
    @SerializedName("coinBal")
    public String coinBal;
    // 포인트 잔액
    @SerializedName("pointBal")
    public String pointBal;
    //활성 예정 금액
    @SerializedName("lockBal")
    public String lockBal;
    // 지갑 주소
    @SerializedName("walletAddress")
    public String walletAddress;


    @Override
    public String toString() {
        return "ElecWaltInfo{" +
                "coinBal=" + coinBal +
                ", pointBal=" + pointBal +
                ", lockBal=" + lockBal +
                '}';
    }
}
