package com.moaPlatform.moa.delivery_menu.store_list.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.moaPlatform.moa.delivery_menu.store_list.model.ReqStoreListModel;
import com.moaPlatform.moa.delivery_menu.store_list.model.ResStoreListModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.models.StoreInfoModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreListDataSource extends PageKeyedDataSource<Integer, StoreInfoModel> {

    private ReqStoreListModel reqModel;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isEmpty = new MutableLiveData<>();

    StoreListDataSource(ReqStoreListModel reqModel) {
        this.reqModel = reqModel;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getIsEmpty() {
        return isEmpty;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, StoreInfoModel> callback) {
        isLoading.postValue(true);
        RetrofitClient.getInstance().getMoaService().getStoreList(reqModel).enqueue(new Callback<ResStoreListModel>() {
            @Override
            public void onResponse(@NonNull Call<ResStoreListModel> call, @NonNull Response<ResStoreListModel> response) {
                ResStoreListModel resStoreListModel = response.body();
                if (resStoreListModel == null || resStoreListModel.storeList == null) {
                    isEmpty.postValue(true);
                    return;
                }
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resStoreListModel)) {
                    loadInitial(params, callback);
                    return;
                }
                if (resStoreListModel.storeList.size() == 0)
                    isEmpty.postValue(true);

                callback.onResult(resStoreListModel.storeList, null, reqModel.currentPage + 1);
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<ResStoreListModel> call, @NonNull Throwable t) {
                Logger.e("get storeList loadInitial fail " + t.toString());
                isEmpty.postValue(true);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, StoreInfoModel> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, StoreInfoModel> callback) {
        reqModel.currentPage = params.key;
        RetrofitClient.getInstance().getMoaService().getStoreList(reqModel).enqueue(new Callback<ResStoreListModel>() {
            @Override
            public void onResponse(@NonNull Call<ResStoreListModel> call, @NonNull Response<ResStoreListModel> response) {
                ResStoreListModel resStoreListModel = response.body();
                if (resStoreListModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resStoreListModel)) {
                    loadAfter(params, callback);
                    return;
                }
                Integer key = resStoreListModel.storeList.size() != 0 ? params.key + 1 : null;
                callback.onResult(resStoreListModel.storeList, key);
            }

            @Override
            public void onFailure(@NonNull Call<ResStoreListModel> call, @NonNull Throwable t) {
                Logger.e("get loadAfter loadInitial fail " + t.toString());
            }
        });
    }
}
