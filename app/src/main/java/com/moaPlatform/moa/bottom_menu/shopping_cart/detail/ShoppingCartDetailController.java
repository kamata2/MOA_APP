package com.moaPlatform.moa.bottom_menu.shopping_cart.detail;

import android.content.Context;

import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model.ReqShoppingCartDetailModel;
import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model.ResShoppingCartDetailModel;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShoppingCartDetailController extends BaseController {
    // 장바구니 상세 정보 호출
    final int SHOPPING_CART_DETAIL_INFO = 17401;
    // 상품 개수 증가
    static final int SHOPPING_CART_PRODUCT_ITEM_PLUS = 17402;
    // 상품 개수 감소
    static final int SHOPPING_CART_PRODUCT_ITEM_MINUS = 17403;
    // 상품 삭재
    final int SHOPPING_CART_PRODUCT_ITEM_REMOVE = 17404;
    // 상품 전체 삭제
    final int SHOPPING_CART_PRODUCT_ITEM_ALL_REMOVE = 17405;

    public ShoppingCartDetailController(Context context) {
        super(context);
    }

    /**
     * 서버와 통신해서 쇼핑카드 정보를 불러옴
     *
     * @param reqModel 서버와 통신할때 사용할 req 모델
     */
    void getShoppingCartDetailInfo(ReqShoppingCartDetailModel reqModel) {
        RetrofitClient.getInstance().getMoaService().getShoppingCartDetailInfo(reqModel).enqueue(new Callback<ResShoppingCartDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<ResShoppingCartDetailModel> call, @NonNull Response<ResShoppingCartDetailModel> response) {

                ResShoppingCartDetailModel resModel = response.body();
                Logger.toJson("getShoppingCartDetailInfo req data :", reqModel);
                switch (resModelCheck("getShoppingCartDetailInfo", resModel)) {
                    case MODEL_NULL:
                        serverErrorMsgShow();
                        break;
                    case MODEL_ACCESS_TOKEN_REFRESH:
                        getShoppingCartDetailInfo(reqModel);
                        break;
                    case RESPONSE_DATA_FAIL:
                        serverErrorMsgShow();
                        break;
                    case RESPONSE_DATA_SUCCESS:
                        onRetrofitSuccess(SHOPPING_CART_DETAIL_INFO, resModel);
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResShoppingCartDetailModel> call, @NonNull Throwable t) {
                serverErrorMsgShow();
                Logger.e("getShoppingCartDetailInfo : ");
                t.printStackTrace();
            }
        });
    }

    /**
     * 장바구니 전체 데이터 삭제
     */
    void shoppingCartAllDelete(ReqShoppingCartDetailModel reqModel) {
        RetrofitClient.getInstance().getMoaService().deleteShoppingCart(reqModel).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                Logger.toJson("shoppingCartAllDelete req Model : ",reqModel);
                switch (resModelCheck("shoppingCartAllDelete", response.body())) {
                    case MODEL_NULL:
                        serverErrorMsgShow();
                        break;
                    case MODEL_ACCESS_TOKEN_REFRESH:
                        shoppingCartAllDelete(reqModel);
                        break;
                    case RESPONSE_DATA_FAIL:
                        serverErrorMsgShow();
                        break;
                    case RESPONSE_DATA_SUCCESS:
                        onRetrofitSuccess(SHOPPING_CART_PRODUCT_ITEM_ALL_REMOVE, response.body());
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                serverErrorMsgShow();
                Logger.e("shoppingCartAllDelete : ");
                t.printStackTrace();
            }
        });
    }

    /**
     * 상품 개수 증가
     */
    void productCountPlus(ReqShoppingCartDetailModel reqModel) {
        RetrofitClient.getInstance().getMoaService().productPlus(reqModel).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                Logger.toJson("productCountPlus reqModel ", reqModel);

                CommonModel resModel = response.body();
                switch (resModelCheck("productCountPlus", response.body())) {
                    case MODEL_NULL:
                        serverErrorMsgShow();
                        onRetrofitFail(SHOPPING_CART_PRODUCT_ITEM_PLUS, "");
                        break;
                    case MODEL_ACCESS_TOKEN_REFRESH:
                        productCountPlus(reqModel);
                        break;
                    case RESPONSE_DATA_FAIL:
                        serverErrorMsgShow();
                        onRetrofitFail(SHOPPING_CART_PRODUCT_ITEM_PLUS, "");
                        break;
                    case RESPONSE_DATA_SUCCESS:
                        if (retrofitConnectionResult != null) {
                            onRetrofitSuccess(SHOPPING_CART_PRODUCT_ITEM_PLUS, resModel);
                        }
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                serverErrorMsgShow();
                onRetrofitFail(SHOPPING_CART_PRODUCT_ITEM_PLUS, "");
                Logger.e("productCountPlus : ");
                t.printStackTrace();
            }
        });
    }

    /**
     * 상품 개수 감소
     *
     * @param reqModel 서버와 통신할 모델
     */
    void productCountMinus(ReqShoppingCartDetailModel reqModel) {
        RetrofitClient.getInstance().getMoaService().productMinus(reqModel).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                Logger.toJson("productCountMinus reqModel ", reqModel);
                switch (resModelCheck("productCountMinus", response.body())) {
                    case MODEL_NULL:
                        serverErrorMsgShow();
                        onRetrofitFail(SHOPPING_CART_PRODUCT_ITEM_MINUS, "");
                        break;
                    case MODEL_ACCESS_TOKEN_REFRESH:
                        productCountMinus(reqModel);
                        break;
                    case RESPONSE_DATA_FAIL:
                        serverErrorMsgShow();
                        onRetrofitFail(SHOPPING_CART_PRODUCT_ITEM_MINUS, "");
                        break;
                    case RESPONSE_DATA_SUCCESS:
                        onRetrofitSuccess(SHOPPING_CART_PRODUCT_ITEM_MINUS, response.body());
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                serverErrorMsgShow();
                onRetrofitFail(SHOPPING_CART_PRODUCT_ITEM_MINUS, "");
                Logger.e("productCountMinus : ");
                t.printStackTrace();
            }
        });
    }

    /**
     * 장바구니 상품 삭제
     *
     * @param reqModel 서버와 통신할 모델
     */
    public void shoppingCartItemRemove(ReqShoppingCartDetailModel reqModel) {
        RetrofitClient.getInstance().getMoaService().shoppingCartItemRemove(reqModel).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                switch (resModelCheck("shoppingCartItemRemove", response.body())) {
                    case MODEL_NULL:
                        serverErrorMsgShow();
                        onRetrofitFail(SHOPPING_CART_PRODUCT_ITEM_REMOVE, "");
                        break;
                    case RESPONSE_DATA_FAIL:
                        serverErrorMsgShow();
                        onRetrofitFail(SHOPPING_CART_PRODUCT_ITEM_REMOVE, "");
                        break;
                    case MODEL_ACCESS_TOKEN_REFRESH:
                        shoppingCartItemRemove(reqModel);
                        break;
                    case RESPONSE_DATA_SUCCESS:
                        onRetrofitSuccess(SHOPPING_CART_PRODUCT_ITEM_REMOVE, response.body());
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                serverErrorMsgShow();
                onRetrofitFail(SHOPPING_CART_PRODUCT_ITEM_REMOVE, "");
                Logger.e("shoppingCartItemRemove : ");
                t.printStackTrace();
            }
        });
    }

}
