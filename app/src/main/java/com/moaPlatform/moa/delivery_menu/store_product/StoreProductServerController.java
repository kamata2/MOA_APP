package com.moaPlatform.moa.delivery_menu.store_product;

import android.content.Context;

import androidx.annotation.NonNull;

import com.moaPlatform.moa.bottom_menu.shopping_cart.main.ReqInputShoppingCart;
import com.moaPlatform.moa.bottom_menu.shopping_cart.main.ResShoppingCartList;
import com.moaPlatform.moa.delivery_menu.store_product.model.ReqProductDetailInfoModel;
import com.moaPlatform.moa.delivery_menu.store_product.model.ResStoreProductDetailInfoModel;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class StoreProductServerController extends BaseController {

    public StoreProductServerController(Context context) {
        super(context);
    }

    /**
     * 서버에서 상품 정보를 가져옴
     */
    void getStoreProductInfo(ReqProductDetailInfoModel reqModel) {

        try{
            RetrofitClient.getInstance().getMoaService().getProductDetailInfo(reqModel).enqueue(new Callback<ResStoreProductDetailInfoModel>() {
                @Override
                public void onResponse(@NonNull Call<ResStoreProductDetailInfoModel> call, @NonNull Response<ResStoreProductDetailInfoModel> response) {
                    ResStoreProductDetailInfoModel resStoreProductDetailInfoModel = response.body();
                    if (resStoreProductDetailInfoModel == null || httpConnectionResult == null)
                        return;
                    if (RetrofitClient.getInstance().hasReissuedAccessToken(resStoreProductDetailInfoModel)) {
                        getStoreProductInfo(reqModel);
                        return;
                    }
                    Logger.i("getStoreProductInfo : " + gson.toJson(resStoreProductDetailInfoModel));
                    httpConnectionResult.onHttpConnectionSuccess(CodeTypeManager.StoreInfo.GET_STORE_INFO.getType(), resStoreProductDetailInfoModel);
                }

                @Override
                public void onFailure(@NonNull Call<ResStoreProductDetailInfoModel> call, @NonNull Throwable t) {
                    Logger.e("getStoreProductInfo fail : " + t);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 장바구니에 담기
     *
     * @param reqModel req 모델
     */
    void inputShoppingCart(ReqInputShoppingCart reqModel) {
        RetrofitClient.getInstance().getMoaService().inputShoppingCart(reqModel).enqueue(new Callback<ResShoppingCartList>() {
            @Override
            public void onResponse(@NonNull Call<ResShoppingCartList> call, @NonNull Response<ResShoppingCartList> response) {
                ResShoppingCartList resShoppingCartList = response.body();
                if (resShoppingCartList == null || httpConnectionResult == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resShoppingCartList)) {
                    inputShoppingCart(reqModel);
                    return;
                }
                httpConnectionResult.onHttpConnectionSuccess(CodeTypeManager.StoreInfo.INPUT_SHOPPING_CART.getType(), resShoppingCartList);
            }

            @Override
            public void onFailure(@NonNull Call<ResShoppingCartList> call, @NonNull Throwable t) {
                Logger.e(" 장바구니 담기 싫패 : " + t);
            }
        });
    }
}
