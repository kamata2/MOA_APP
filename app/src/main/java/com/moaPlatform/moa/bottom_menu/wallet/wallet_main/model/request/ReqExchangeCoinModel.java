package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqExchangeCoinModel extends CommonModel {

    @SerializedName("exCoinBal")
    public String exCoinBal;        //교환 코인 금액

    @SerializedName("encryptPwd")
    public String encryptPwd;       //암호화 비밀번호

    @Override
    public String toString() {
        return "ReqExchangeCoinModel{" +
                "exCoinBal='" + exCoinBal + '\'' +
                ", encryptPwd='" + encryptPwd + '\'' +
                '}';
    }
}
