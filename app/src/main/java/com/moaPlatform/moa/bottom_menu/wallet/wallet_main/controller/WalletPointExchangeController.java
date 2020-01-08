package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.controller;

import android.content.Context;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.ReqExchangeCoinModel;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response.ResCoinExchangeInfoModel;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 포인트 전환하기 컨트롤러
 */
public class WalletPointExchangeController extends BaseController {

    public static final int COIN_EXCHANGE_INFO_SUCCESS = 3000;
    public static final int EXCHANGE_COIN_FOR_POINT_SUCCESS = 3001;

    public WalletPointExchangeController(Context context) {
        super(context);
    }

    /**
     * 코인 변환 정보를 조회한다.
     */
    public void getCoinExchangeInfo() {

        RetrofitClient.getInstance().getMoaService().getCoinExchangeInfo(new CommonModel()).enqueue(new Callback<ResCoinExchangeInfoModel>() {
            @Override
            public void onResponse(@NonNull Call<ResCoinExchangeInfoModel> call, @NonNull Response<ResCoinExchangeInfoModel> response) {
                ResCoinExchangeInfoModel model = response.body();
                if (model == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(model)) {
                    getCoinExchangeInfo();
                    return;
                }
                if (model.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    onRetrofitSuccess(COIN_EXCHANGE_INFO_SUCCESS, model);
                } else {
                    onRetrofitFail(0, model.resultValue);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResCoinExchangeInfoModel> call, @NonNull Throwable t) {
                onRetrofitFail(0, t.toString());
                t.printStackTrace();
            }
        });
    }

    /**
     * 포인트 교환 요청
     *
     * @param reqExchangeCoin
     */
    public void exchangeCoinForPoint(ReqExchangeCoinModel reqExchangeCoin) {

        RetrofitClient.getInstance().getMoaService().exchangeCoinForPoint(reqExchangeCoin).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel model = response.body();
                if (model == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(model)) {
                    exchangeCoinForPoint(reqExchangeCoin);
                    return;
                }
                if (model.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    onRetrofitSuccess(EXCHANGE_COIN_FOR_POINT_SUCCESS, model);
                } else {
                    onRetrofitFail(0, model.resultMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                onRetrofitFail(0, context.getString(R.string.common_toast_msg_connection_fail));
                t.printStackTrace();
            }
        });
    }
}
