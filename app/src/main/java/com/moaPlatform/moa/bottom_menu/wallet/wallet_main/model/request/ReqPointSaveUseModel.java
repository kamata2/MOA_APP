package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

/**
 * 포인트 사용내역 조회
 */
public class ReqPointSaveUseModel extends CommonModel {

    @SerializedName("sort")
    public String sort;

}
