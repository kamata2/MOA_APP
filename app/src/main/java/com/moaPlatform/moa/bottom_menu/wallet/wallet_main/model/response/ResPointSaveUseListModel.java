package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.List;

/**
 * 서버에서 받은 코인 및 포인트 정보
 */
public class ResPointSaveUseListModel extends CommonModel {
    // 코인 잔액
    @SerializedName("elecWaltInfo")
    public WalletInfoModel walletInfo;
    // 포인트 잔액
    @SerializedName("pointSaveUseList")
    public List<PointSaveUseModel> list;
}