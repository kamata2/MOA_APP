package com.moaPlatform.moa.bottom_menu.shopping_cart.main;

import android.content.Context;

import androidx.annotation.NonNull;

import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model.ReqShoppingCartDetailModel;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ShoppingCartController extends BaseController {

    public ShoppingCartController(Context context) {
        super(context);
    }

    /**
     * 장바구니 데이터를 서버에서 받아옴
     */
    void getShoppingCartListInfo() {
        RetrofitClient.getInstance().getMoaService().getShoppingCartList(new CommonModel()).enqueue(new Callback<ResShoppingCartList>() {
            @Override
            public void onResponse(@NonNull Call<ResShoppingCartList> call, @NonNull Response<ResShoppingCartList> response) {
                ResShoppingCartList resShoppingCartList = response.body();
                if (resShoppingCartList == null || httpConnectionResult == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resShoppingCartList)) {
                    getShoppingCartListInfo();
                    return;
                }
                httpConnectionResult.onHttpConnectionSuccess(0, resShoppingCartList);
            }

            @Override
            public void onFailure(@NonNull Call<ResShoppingCartList> call, @NonNull Throwable t) {

            }
        });
    }

    /**
     * 장바구니 데이터 삭제
     */
    void shoppingCartDelete(ReqShoppingCartDetailModel reqModel) {
        RetrofitClient.getInstance().getMoaService().deleteShoppingCart(reqModel).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null || httpConnectionResult == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    shoppingCartDelete(reqModel);
                    return;
                }
                if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    httpConnectionResult.onHttpConnectionSuccess(-1, commonModel);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                Logger.e("장바구니 데이터 전부 삭제 실패 : " + t);
            }
        });
    }

}
