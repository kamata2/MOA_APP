package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.controller;

import android.content.Context;

import androidx.annotation.NonNull;

import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.ReqPointSaveUseModel;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response.ResPointSaveUseListModel;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 포인트 사용내역 조회 컨트롤러
 */
public class WalletPointHistoryController extends BaseController {

    public WalletPointHistoryController(Context context) {
        super(context);
    }

    //포인트 사용내역 조회
    public void getPointSaveUseList(ReqPointSaveUseModel model) {

        RetrofitClient.getInstance().getMoaService().getPointSaveUseList(model).enqueue(new Callback<ResPointSaveUseListModel>() {
            @Override
            public void onResponse(@NonNull Call<ResPointSaveUseListModel> call, @NonNull Response<ResPointSaveUseListModel> response) {
                ResPointSaveUseListModel resPointSaveUseListModel = response.body();
                if (resPointSaveUseListModel == null || retrofitConnectionResult == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resPointSaveUseListModel)) {
                    getPointSaveUseList(model);
                    return;
                }
                if (resPointSaveUseListModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    retrofitConnectionResult.onRetrofitSuccess(0, resPointSaveUseListModel);
                } else {
                    retrofitConnectionResult.onRetrofitFail(0, gson.toJson(resPointSaveUseListModel));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResPointSaveUseListModel> call, @NonNull Throwable t) {
                Logger.e("paymentServerCall : " + t);
                if (retrofitConnectionResult != null) {
                    retrofitConnectionResult.onRetrofitFail(0, t.toString());
                }
            }
        });
    }
}
