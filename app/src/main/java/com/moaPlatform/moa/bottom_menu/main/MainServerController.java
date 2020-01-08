package com.moaPlatform.moa.bottom_menu.main;

import android.content.Context;

import androidx.annotation.NonNull;

import com.moaPlatform.moa.bottom_menu.main.model.ResMainServiceModel;
import com.moaPlatform.moa.notification.ReqNotificationModel;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainServerController extends BaseController {

    public MainServerController(Context context) {
        super(context);
    }

    /**
     * 카테고리, 매장 리스트 정보를 불러옴
     */
    void getMainServiceList() {
        try{
            RetrofitClient.getInstance().getMoaService().mainServiceInfo(new CommonModel()).enqueue(new Callback<ResMainServiceModel>() {
                @Override
                public void onResponse(@NonNull Call<ResMainServiceModel> call, @NonNull Response<ResMainServiceModel> response) {
                    ResMainServiceModel resMainServiceModel = response.body();
                    if (resMainServiceModel == null || httpConnectionResult == null)
                        return;
                    if (RetrofitClient.getInstance().hasReissuedAccessToken(resMainServiceModel)) {
                        getMainServiceList();
                        return;
                    }
                    if (resMainServiceModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                        httpConnectionResult.onHttpConnectionSuccess(CodeTypeManager.MainActivityManager.MAIN_SERVICE_INFO.getType(), resMainServiceModel);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResMainServiceModel> call, @NonNull Throwable t) {
                    Logger.i("getMainServiceList server error : " + t);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 푸시키 등록
     *
     * @param model
     */
    public void putFcmKey(ReqNotificationModel model) {
        RetrofitClient.getInstance().getMoaService().setFcmKey(model).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    putFcmKey(model);
                    return;
                }
                if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    onRetrofitSuccess(0, response.body());
                } else {
                    onRetrofitFail(0, commonModel.resultValue);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                onRetrofitFail(0, t.toString());
            }
        });

    }


}
