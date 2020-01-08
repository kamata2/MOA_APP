package com.moaPlatform.moa.bottom_menu.wallet.barcode.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqQrPayInfo extends CommonModel {
    @SerializedName("storId")
    public Integer storId; // 회원구분 (회원 : 01, 비회원 : 02)
}
