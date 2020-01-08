package com.moaPlatform.moa.delivery_menu.eatout_store_list.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.google.gson.Gson;
import com.moaPlatform.moa.bottom_menu.main.model.EatOutStoreModel;
import com.moaPlatform.moa.delivery_menu.eatout_store_list.model.ReqEatOutStoreListModel;
import com.moaPlatform.moa.delivery_menu.eatout_store_list.model.ResEatOutStoreListModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EatOutStoreListDataSource extends PageKeyedDataSource<Integer, EatOutStoreModel> {

    private ReqEatOutStoreListModel reqModel;
    private MutableLiveData<Boolean> isProgress = new MutableLiveData<>();

    EatOutStoreListDataSource(ReqEatOutStoreListModel reqModel) {
        this.reqModel = reqModel;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, EatOutStoreModel> callback) {

        RetrofitClient.getInstance().getMoaService().getEatOutStoreList(reqModel).enqueue(new Callback<ResEatOutStoreListModel>() {
            @Override
            public void onResponse(@NonNull Call<ResEatOutStoreListModel> call, @NonNull Response<ResEatOutStoreListModel> response) {
                ResEatOutStoreListModel resEatOutStoreListModel = response.body();
                if (resEatOutStoreListModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resEatOutStoreListModel)) {
                    loadInitial(params, callback);
                    return;
                }
                if (response.body() != null) {
                    Logger.i("EatOutStoreListDataSource loadInitial : " + new Gson().toJson(response.body()));
                    callback.onResult(response.body().storeList, null, reqModel.currentPage + 1);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResEatOutStoreListModel> call, @NonNull Throwable t) {
                Logger.e("get storeList loadInitial fail " + t.toString());
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, EatOutStoreModel> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, EatOutStoreModel> callback) {

        isProgress.postValue(true);

        reqModel.currentPage = params.key;
        RetrofitClient.getInstance().getMoaService().getEatOutStoreList(reqModel).enqueue(new Callback<ResEatOutStoreListModel>() {
            @Override
            public void onResponse(@NonNull Call<ResEatOutStoreListModel> call, @NonNull Response<ResEatOutStoreListModel> response) {
                isProgress.postValue(false);
                ResEatOutStoreListModel resEatOutStoreListModel = response.body();
                if (resEatOutStoreListModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resEatOutStoreListModel)) {
                    loadAfter(params, callback);
                    return;
                }
                Integer key = resEatOutStoreListModel.storeList.size() != 0 ? params.key + 1 : null;
                callback.onResult(resEatOutStoreListModel.storeList, key);
            }

            @Override
            public void onFailure(@NonNull Call<ResEatOutStoreListModel> call, @NonNull Throwable t) {
                isProgress.postValue(false);
                Logger.e("get loadAfter loadInitial fail " + t.toString());
            }
        });
    }

    public MutableLiveData<Boolean> getIsProgress() {
        return isProgress;
    }
}
