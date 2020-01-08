package com.moaPlatform.moa.delivery_menu.store_detail;

import android.content.Context;

import com.moaPlatform.moa.delivery_menu.store_detail.model.ResStoreDetailInfo;
import com.moaPlatform.moa.model.req_model.ReqStoreInfoModel;
import com.moaPlatform.moa.side_menu.favorite.model.ReqBookmarkStoreRemove;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.StoreInfoModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class StoreDetailServerController extends BaseController {

    public StoreDetailServerController(Context context) {
        super(context);
    }

    /**
     * 가맹점 상세 정보 불러오기
     */
    void getStoreDetailInfo(ReqStoreInfoModel reqModel) {
        RetrofitClient.getInstance().getMoaService().getStoreDetailInfo(reqModel).enqueue(new Callback<ResStoreDetailInfo>() {
            @Override
            public void onResponse(@NonNull Call<ResStoreDetailInfo> call, @NonNull Response<ResStoreDetailInfo> response) {
                ResStoreDetailInfo resStoreDetailInfo = response.body();
                if (resStoreDetailInfo == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resStoreDetailInfo)) {
                    getStoreDetailInfo(reqModel);
                    return;
                }
//                Logger.i("getStoreDetailInfo : " + gson.toJson(resStoreDetailInfo));
                Logger.toJson("getStoreDetailInfo reqModel : ", reqModel);
                Logger.toJson("getStoreDetailInfo : ", response.body());
                httpConnectionResult.onHttpConnectionSuccess(0, resStoreDetailInfo);
            }

            @Override
            public void onFailure(@NonNull Call<ResStoreDetailInfo> call, @NonNull Throwable t) {
                Logger.e("가맹점 상세 정보 받아오기 실패 : " + t);
            }
        });
    }

    void getStoreAdditionalInfo(ReqStoreInfoModel reqModel) {
        RetrofitClient.getInstance().getMoaService().getStoreAddInfo(reqModel).enqueue(new Callback<ResStoreDetailInfo>() {
            @Override
            public void onResponse(@NonNull Call<ResStoreDetailInfo> call, @NonNull Response<ResStoreDetailInfo> response) {
//                ResStoreDetailInfo resStoreDetailInfo = response.body();
//                if (resStoreDetailInfo == null)
//                    return;
//                if (RetrofitClient.getInstance().hasReissuedAccessToken(resStoreDetailInfo)) {
//                    getStoreAdditionalInfo(reqModel);
//                    return;
//                }
//                Logger.i("data : " + gson.toJson(resStoreDetailInfo));
//                httpConnectionResult.onHttpConnectionSuccess(0, resStoreDetailInfo);
//                Logger.i("가맹점 부가 정보 : " + gson.toJson(resStoreDetailInfo));
//                if (resStoreDetailInfo.resultValue.equals(ServerSideMsg.SUCCESS)) {
//
//                }

                switch (resModelCheck("getStoreAdditionalInfo", response.body())) {
                    case MODEL_NULL:
                        serverErrorMsgShow();
                        break;
                    case RESPONSE_DATA_FAIL:
                        serverErrorMsgShow();
                        break;
                    case RESPONSE_DATA_SUCCESS:
                        onRetrofitSuccess(-1, response.body());
                        break;
                    case MODEL_ACCESS_TOKEN_REFRESH:
                        getStoreAdditionalInfo(reqModel);
                        break;
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResStoreDetailInfo> call, @NonNull Throwable t) {
                Logger.e("가맹점 상세 부가 받아오기 실패 : " + t);
                serverErrorMsgShow();
            }
        });
    }

    /**
     * 즐겨찾기 추가
     * @param reqModel
     * 즐겨찾기 추가시 서버에 보낼 req 데이터
     */
    void addBookmark(ReqStoreInfoModel reqModel) {
        RetrofitClient.getInstance().getMoaService().addBookmark(reqModel).enqueue(new Callback<StoreInfoModel>() {
            @Override
            public void onResponse(@NonNull Call<StoreInfoModel> call, @NonNull Response<StoreInfoModel> response) {
                StoreInfoModel storeInfoModel = response.body();
                if (storeInfoModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(storeInfoModel)) {
                    addBookmark(reqModel);
                    return;
                }
                httpConnectionResult.onHttpConnectionSuccess(CodeTypeManager.StoreInfo.STORE_ADD_BOOKMARK.getType(), storeInfoModel);
            }

            @Override
            public void onFailure(@NonNull Call<StoreInfoModel> call, @NonNull Throwable t) {
                Logger.e("addBookmark : " + t);
            }
        });
    }

    /**
     * 즐겨찾기 삭제
     * @param reqModel
     * 즐겨찾기 추가시 서버에 보낼 req 데이터
     */
    void removeBookmark(ReqStoreInfoModel reqModel) {
        ReqBookmarkStoreRemove reqBookmarkStoreRemove = new ReqBookmarkStoreRemove();
        reqBookmarkStoreRemove.setStoreId(reqModel.storeId);
        RetrofitClient.getInstance().getMoaService().removeBookmark(reqBookmarkStoreRemove).enqueue(new Callback<StoreInfoModel>() {
            @Override
            public void onResponse(@NonNull Call<StoreInfoModel> call, @NonNull Response<StoreInfoModel> response) {
                StoreInfoModel storeInfoModel = response.body();
                if (storeInfoModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(storeInfoModel)) {
                    removeBookmark(reqModel);
                    return;
                }
                httpConnectionResult.onHttpConnectionSuccess(CodeTypeManager.StoreInfo.STORE_REMOVE_BOOKMARK.getType(), storeInfoModel);
            }

            @Override
            public void onFailure(@NonNull Call<StoreInfoModel> call, @NonNull Throwable t) {
                Logger.e("removeBookmark : " + t);
            }
        });
    }

    /**
     * 장바구니 개수 확인
     */
    void shoppingCartCountCheck() {
        RetrofitClient.getInstance().getMoaService().shoppingCartCountCheck(new CommonModel()).enqueue(new Callback<ResStoreDetailInfo.ShoppingCartCountModel>() {
            @Override
            public void onResponse(@NonNull Call<ResStoreDetailInfo.ShoppingCartCountModel> call, @NonNull Response<ResStoreDetailInfo.ShoppingCartCountModel> response) {
                ResStoreDetailInfo.ShoppingCartCountModel shoppingCartCountModel = response.body();
                if (shoppingCartCountModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(shoppingCartCountModel)) {
                    shoppingCartCountCheck();
                    return;
                }
                Logger.i("shoppingCartCountCheck : " + gson.toJson(shoppingCartCountModel));
                httpConnectionResult.onHttpConnectionSuccess(CodeTypeManager.ShoppingCart.SHOPPING_CART_COUNT.getType(), shoppingCartCountModel);
            }

            @Override
            public void onFailure(@NonNull Call<ResStoreDetailInfo.ShoppingCartCountModel> call, @NonNull Throwable t) {
                Logger.e("shoppingCartCountCheck : " + t);
            }
        });
    }

}
