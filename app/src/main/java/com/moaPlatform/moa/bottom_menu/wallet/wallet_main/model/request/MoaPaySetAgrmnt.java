package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class MoaPaySetAgrmnt extends CommonModel {
    @SerializedName("moaPayUseClausAgrmntYn")
    public String moaPayUseClausAgrmntYn;

    @SerializedName("elctrFnnclTrnscUseClausAgrmntYn")
    public String elctrFnnclTrnscUseClausAgrmntYn;

    @SerializedName("indvInfoClctnAndUseAgrmntYn")
    public String indvInfoClctnAndUseAgrmntYn;

    @SerializedName("indvInfoThirdPartyPrvsnAgrmntYn")
    public String indvInfoThirdPartyPrvsnAgrmntYn;

    @SerializedName("payInfoPrvsnAgrmntYn")
    public String payInfoPrvsnAgrmntYn;
}
