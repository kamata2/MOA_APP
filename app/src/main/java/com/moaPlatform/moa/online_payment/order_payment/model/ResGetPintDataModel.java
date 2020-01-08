package com.moaPlatform.moa.online_payment.order_payment.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ResGetPintDataModel extends CommonModel {

    @SerializedName("elecWaltInfo")
    public WalletModel walletModel;

    public class WalletModel {
        @SerializedName("pointBal")
        public int point;
    }

}
