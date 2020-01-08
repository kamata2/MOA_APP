package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response;

import com.google.gson.annotations.SerializedName;

public class PointSaveUseModel {
    @SerializedName("occrAmt")              public int occrAmt;             //발생금액
    @SerializedName("pointSaveUseCntnt")    public String pointSaveUseCntnt;   //포인트적립사용내용
    @SerializedName("remainingLockTime")    public int remainingLockTime;   //남은 잠금시간
    @SerializedName("saveUseSepaCntnt")     public String saveUseSepaCntnt;    //적립사용구분 내용
    @SerializedName("lockStateCntnt")       public String lockStateCntnt;      //잠금상태 내용
    @SerializedName("saveUseDt")            public String saveUseDt;           //적립사용일시
}
