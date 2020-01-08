package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.controller;

import android.content.Context;

import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.ReqPointCoin;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response.PointCoinModel;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 지갑 컨트롤러
 */
public class WalletController extends BaseController {

    public WalletController(Context context) {
        super(context);
    }

    /**
     * 포인트 와 코인 정보를 서버에서 받아옴
     */
    public void getPointCoin() {

        RetrofitClient.getInstance().getMoaService().getCoinAndPoint(new ReqPointCoin()).enqueue(new Callback<PointCoinModel>() {
            @Override
            public void onResponse(@NonNull Call<PointCoinModel> call, @NonNull Response<PointCoinModel> response) {
                PointCoinModel pointCoinModel = response.body();
                if (pointCoinModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(pointCoinModel)) {
                    getPointCoin();
                    return;
                }
                if (pointCoinModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    onRetrofitSuccess(0, pointCoinModel);
                } else {
                    onRetrofitFail(0, pointCoinModel.resultValue);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PointCoinModel> call, @NonNull Throwable t) {
                onRetrofitFail(0, t.toString());
                t.printStackTrace();
            }
        });
    }
}
