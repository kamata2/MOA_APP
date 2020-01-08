package com.moaPlatform.moa.intro.controller;

import android.content.Context;

import com.moaPlatform.moa.intro.model.ReqAgreementInfoData;
import com.moaPlatform.moa.intro.model.ResVersionModel;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.EmptyModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroServerController extends BaseController {


    public IntroServerController(Context context) {
        super(context);
    }

    public void postAgreementInfoData(ReqAgreementInfoData reqModel) {
        RetrofitClient.getInstance().getMoaService().postAgreementData(reqModel).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null || httpConnectionResult == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    postAgreementInfoData(reqModel);
                    return;
                }
                httpConnectionResult.onHttpConnectionSuccess(0, commonModel);
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {

            }
        });
    }

    /**
     * 앱 버전 체크
     */
    public void getStartData() {
        RetrofitClient.getInstance().getMoaBasicService()
                .getStartData(new EmptyModel()).enqueue(new Callback<ResVersionModel>() {
            @Override
            public void onResponse(@NonNull Call<ResVersionModel> call, @NonNull Response<ResVersionModel> response) {
                ResVersionModel resVersionModel = response.body();
                if (resVersionModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resVersionModel)) {
                    getStartData();
                    return;
                }
                if (resVersionModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    onRetrofitSuccess(0, resVersionModel);
                } else {
                    onRetrofitFail(0, resVersionModel.resultValue);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResVersionModel> call, @NonNull Throwable t) {
                onRetrofitFail(0, t.toString());
            }
        });
    }
}
