package com.moaPlatform.moa.delivery_menu.eatout_store_detail;

import android.content.Context;

import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.EatOutStoreInfoModel;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.ResEatOutStoreDetailInfo;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.ResEatOutStoreProductListInfo;
import com.moaPlatform.moa.model.req_model.ReqStoreInfoModel;
import com.moaPlatform.moa.model.res_model.ResReviewListModel;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EatOutStoreDetailServerController extends BaseController {

    public EatOutStoreDetailServerController(Context context) {
        super(context);
    }

    /**
     * [외식]가맹점 상세 정보 불러오기
     */
    void getEatOutStoreDetailInfo(ReqStoreInfoModel reqModel) {
        RetrofitClient.getInstance().getMoaService().getEatOutStoreDetailInfo(reqModel).enqueue(new Callback<ResEatOutStoreDetailInfo>() {
            @Override
            public void onResponse(@NonNull Call<ResEatOutStoreDetailInfo> call, @NonNull Response<ResEatOutStoreDetailInfo> response) {
                ResEatOutStoreDetailInfo resEatOutStoreDetailInfo = response.body();
                if (resEatOutStoreDetailInfo == null) {
                    return;
                }
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resEatOutStoreDetailInfo)) {
                    getEatOutStoreDetailInfo(reqModel);
                    return;
                }
                if (resEatOutStoreDetailInfo.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    onRetrofitSuccess(0, resEatOutStoreDetailInfo);
                } else {
                    onRetrofitFail(0, resEatOutStoreDetailInfo.resultValue);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResEatOutStoreDetailInfo> call, @NonNull Throwable t) {
                onRetrofitFail(0, t.toString());
            }
        });
    }

    /**
     * 즐겨찾기 추가
     *
     * @param reqModel 즐겨찾기 추가시 서버에 보낼 req 데이터
     */
    void addBookmark(ReqStoreInfoModel reqModel) {
        RetrofitClient.getInstance().getMoaService().addEatOutStoreBookmark(reqModel).enqueue(new Callback<EatOutStoreInfoModel>() {
            @Override
            public void onResponse(@NonNull Call<EatOutStoreInfoModel> call, @NonNull Response<EatOutStoreInfoModel> response) {
                EatOutStoreInfoModel eatOutStoreInfoModel = response.body();
                if (eatOutStoreInfoModel == null) {
                    return;
                }
                if (RetrofitClient.getInstance().hasReissuedAccessToken(eatOutStoreInfoModel)) {
                    addBookmark(reqModel);
                    return;
                }
                if (eatOutStoreInfoModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    onRetrofitSuccess(CodeTypeManager.StoreInfo.STORE_ADD_BOOKMARK.getType(), eatOutStoreInfoModel);
                } else {
                    onRetrofitFail(0, eatOutStoreInfoModel.resultValue);
                }
            }

            @Override
            public void onFailure(@NonNull Call<EatOutStoreInfoModel> call, @NonNull Throwable t) {
                onRetrofitFail(0, t.toString());
                Logger.e("addBookmark : " + t);
            }
        });
    }

    /**
     * 즐겨찾기 삭제
     *
     * @param reqModel 즐겨찾기 추가시 서버에 보낼 req 데이터
     */
    void removeBookmark(ReqStoreInfoModel reqModel) {
        RetrofitClient.getInstance().getMoaService().removeEatOutStoreBookmark(reqModel).enqueue(new Callback<EatOutStoreInfoModel>() {
            @Override
            public void onResponse(@NonNull Call<EatOutStoreInfoModel> call, @NonNull Response<EatOutStoreInfoModel> response) {
                EatOutStoreInfoModel eatOutStoreInfoModel = response.body();
                if (eatOutStoreInfoModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(eatOutStoreInfoModel)) {
                    removeBookmark(reqModel);
                    return;
                }
                onRetrofitSuccess(CodeTypeManager.StoreInfo.STORE_REMOVE_BOOKMARK.getType(), eatOutStoreInfoModel);
            }

            @Override
            public void onFailure(@NonNull Call<EatOutStoreInfoModel> call, @NonNull Throwable t) {
                Logger.e("removeBookmark : " + t);
            }
        });
    }

    /**
     * 더보기 > 상품 리스트 요청
     *
     * @param reqModel
     */
    void getEatOutStoreProductList(ReqStoreInfoModel reqModel) {
        RetrofitClient.getInstance().getMoaService().getEatOutStoreProductList(reqModel).enqueue(new Callback<ResEatOutStoreProductListInfo>() {
            @Override
            public void onResponse(@NonNull Call<ResEatOutStoreProductListInfo> call, @NonNull Response<ResEatOutStoreProductListInfo> response) {
                ResEatOutStoreProductListInfo resEatOutStoreProductListInfo = response.body();
                if (resEatOutStoreProductListInfo == null || httpConnectionResult == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resEatOutStoreProductListInfo)) {
                    getEatOutStoreProductList(reqModel);
                    return;
                }
                httpConnectionResult.onHttpConnectionSuccess(0, resEatOutStoreProductListInfo);
            }

            @Override
            public void onFailure(@NonNull Call<ResEatOutStoreProductListInfo> call, @NonNull Throwable t) {
                Logger.e("removeBookmark : " + t);
            }
        });
    }

    /**
     * 외식 리뷰 리스트 조회
     * 3줄 리뷰 더보기 > 리스트 요청
     * @param reqModel
     */
    public void getEatOutStoreReviewList(ReqStoreInfoModel reqModel) {
        RetrofitClient.getInstance().getMoaService().getEatOutStoreReviewList(reqModel).enqueue(new Callback<ResReviewListModel>() {
            @Override
            public void onResponse(@NonNull Call<ResReviewListModel> call, @NonNull Response<ResReviewListModel> response) {
                ResReviewListModel resReviewListModel = response.body();
                if (resReviewListModel == null || httpConnectionResult == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resReviewListModel)) {
                    getEatOutStoreReviewList(reqModel);
                    return;
                }
                httpConnectionResult.onHttpConnectionSuccess(0, resReviewListModel);
            }

            @Override
            public void onFailure(@NonNull Call<ResReviewListModel> call, @NonNull Throwable t) {
            }
        });
    }
}
