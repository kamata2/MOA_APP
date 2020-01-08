package com.moaPlatform.moa.top_menu.search;

import android.content.Context;

import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class SearchController extends BaseController {

    public SearchController(Context context) {
        super(context);
    }

    /**
     * 가맹점 검색
     *
     * @param reqSearchHistoryModel 가맹점 검색 시 사용할 reqModel
     */
    void searchStoreList(ReqSearchHistoryModel reqSearchHistoryModel) {
        RetrofitClient.getInstance().getMoaService().getSearchStoreList(reqSearchHistoryModel).enqueue(new Callback<ResSearchModel>() {
            @Override
            public void onResponse(@NonNull Call<ResSearchModel> call, @NonNull Response<ResSearchModel> response) {
                ResSearchModel resSearchModel = response.body();
                if (resSearchModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resSearchModel)) {
                    searchStoreList(reqSearchHistoryModel);
                    return;
                }
                if (httpConnectionResult != null)
                    httpConnectionResult.onHttpConnectionSuccess(CodeTypeManager.TopToolbarManager.STORE_KEYWORD_SEARCH.getType(), resSearchModel);
            }

            @Override
            public void onFailure(@NonNull Call<ResSearchModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * 최근 검색 기록
     */
    void recentlySearchHistory() {
        RetrofitClient.getInstance().getMoaService().recentlySearchHistory(new CommonModel()).enqueue(new Callback<ResSearchModel>() {
            @Override
            public void onResponse(@NonNull Call<ResSearchModel> call, @NonNull Response<ResSearchModel> response) {
                ResSearchModel resSearchModel = response.body();
                if (resSearchModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resSearchModel)) {
                    recentlySearchHistory();
                    return;
                }
                if (httpConnectionResult != null)
                    httpConnectionResult.onHttpConnectionSuccess(CodeTypeManager.TopToolbarManager.RECENTLY_STORE_SEARCH.getType(), resSearchModel);
            }

            @Override
            public void onFailure(@NonNull Call<ResSearchModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void searchHistoryRemove(ReqSearchHistoryModel reqSearchHistoryModel) {
        RetrofitClient.getInstance().getMoaService().removeSearchHistory(reqSearchHistoryModel).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    searchHistoryRemove(reqSearchHistoryModel);
                    return;
                }
                if (httpConnectionResult != null)
                    httpConnectionResult.onHttpConnectionSuccess(CodeTypeManager.TopToolbarManager.RECENTLY_SEARCH_HISTORY_REMOVE.getType(), commonModel);
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void searchHistoryAllRemove() {
        RetrofitClient.getInstance().getMoaService().allRemoveSearchHistory(new CommonModel()).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    searchHistoryAllRemove();
                    return;
                }
                if (httpConnectionResult != null) {
                    httpConnectionResult.onHttpConnectionSuccess(CodeTypeManager.TopToolbarManager.RECENTLY_SEARCH_HISTORY_ALL_REMOVE.getType(), commonModel);
                }

            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
