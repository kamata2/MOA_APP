package com.moaPlatform.moa.online_payment.order_payment.controller;

import android.content.Context;

import androidx.annotation.NonNull;

import com.moaPlatform.moa.online_payment.order_payment.model.ReqPaymentOrderModel;
import com.moaPlatform.moa.online_payment.order_payment.model.ReqUseCouponModel;
import com.moaPlatform.moa.online_payment.order_payment.model.ResGetPintDataModel;
import com.moaPlatform.moa.online_payment.order_payment.model.ResUseCouponModel;
import com.moaPlatform.moa.online_payment.order_payment.model.res.ResPaymentResult;
import com.moaPlatform.moa.side_menu.coupon.model.ReqCouponRegistration;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.App;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.moaPlatform.moa.util.manager.CodeTypeManager.RestApi.REGISTER_COUPON;

public class OrderPaymentServerController extends BaseController {

    public OrderPaymentServerController(Context context) {
        super(context);
    }

    public void paymentServerCall(ReqPaymentOrderModel reqPaymentOrder) {
        RetrofitClient.getInstance().getMoaService().setOrder(reqPaymentOrder).enqueue(new Callback<ResPaymentResult>() {
            @Override
            public void onResponse(@NonNull Call<ResPaymentResult> call, @NonNull Response<ResPaymentResult> response) {
                ResPaymentResult resPaymentResult = response.body();
                if (resPaymentResult == null || httpConnectionResult == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resPaymentResult)) {
                    paymentServerCall(reqPaymentOrder);
                    return;
                }
                if (resPaymentResult.resultValue.equals(ServerSideMsg.SUCCESS))
                    httpConnectionResult.onHttpConnectionSuccess(0, resPaymentResult);
            }

            @Override
            public void onFailure(@NonNull Call<ResPaymentResult> call, @NonNull Throwable t) {
                Logger.e("paymentServerCall : " + t);
            }
        });
    }

    public void getPoint() {
        RetrofitClient.getInstance().getMoaService().getPaymentPointModel(new CommonModel()).enqueue(new Callback<ResGetPintDataModel>() {
            @Override
            public void onResponse(@NonNull Call<ResGetPintDataModel> call, @NonNull Response<ResGetPintDataModel> response) {
                ResGetPintDataModel resGetPintDataModel = response.body();
                if (resGetPintDataModel == null || httpConnectionResult == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resGetPintDataModel)) {
                    getPoint();
                    return;
                }
                httpConnectionResult.onHttpConnectionSuccess(20, resGetPintDataModel);
            }

            @Override
            public void onFailure(@NonNull Call<ResGetPintDataModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void useCouponSearch(ReqUseCouponModel reqUseCouponModel) {
        RetrofitClient.getInstance().getMoaService().getUseCoupon(reqUseCouponModel).enqueue(new Callback<ResUseCouponModel>() {
            @Override
            public void onResponse(@NonNull Call<ResUseCouponModel> call, @NonNull Response<ResUseCouponModel> response) {
                ResUseCouponModel resUseCouponModel = response.body();
                if (resUseCouponModel == null || httpConnectionResult == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resUseCouponModel)) {
                    useCouponSearch(reqUseCouponModel);
                    return;
                }
                httpConnectionResult.onHttpConnectionSuccess(-1, resUseCouponModel);
            }

            @Override
            public void onFailure(@NonNull Call<ResUseCouponModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * 쿠폰 등록
     *
     * @param reqModel 서버에 모델 req 모델
     */
    public void registerCoupon(ReqCouponRegistration reqModel) {
        RetrofitClient.getInstance().getMoaService().couponRegisterList(reqModel).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                if (RetrofitClient.getInstance().hasReissuedAccessToken(response.body())) {
                    registerCoupon(reqModel);
                    return;
                }
                Logger.d("registerCoupon reqData : " + App.getInstance().gson.toJson(reqModel));
                if (response.body() != null) {
                    if (restApiResult != null) {
                        Logger.d("registerCoupon resData : " + App.getInstance().gson.toJson(response.body()));
                        restApiResult.onRestApiSuccess(REGISTER_COUPON, response.body());
                    } else
                        Logger.e("registerCoupon restApiResult null");
                } else {
                    Logger.e("registerCoupon data null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                if (restApiResult != null)
                    restApiResult.onRestApiFail(REGISTER_COUPON);
                Logger.e("registerCoupon");
                t.printStackTrace();
            }
        });
    }

}
