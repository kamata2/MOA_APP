package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

/**
 * 서버에서 받은 데이터 모델
 */
public class PointCoinModel extends CommonModel {
    // 코인 및 포인트 잔액 정보 저장 모델
    @SerializedName("elecWaltInfo")
    public ElecWaltInfo elecWaltInfo;

}
