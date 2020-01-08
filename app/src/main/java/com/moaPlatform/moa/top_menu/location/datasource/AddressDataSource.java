package com.moaPlatform.moa.top_menu.location.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.moaPlatform.moa.top_menu.location.model.ReqAddressSearchModel;
import com.moaPlatform.moa.top_menu.location.model.ResAddressSearchBaseModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressDataSource extends PageKeyedDataSource<Integer, ResAddressSearchBaseModel.AddressModel> {
    private ReqAddressSearchModel reqModel = new ReqAddressSearchModel();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDataEmpty = new MutableLiveData<>();
    private MutableLiveData<Boolean> isHistoryAddress = new MutableLiveData<>();

    AddressDataSource(String inputAddress) {
        reqModel.keyword = inputAddress;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getIsDataEmpty() {
        return isDataEmpty;
    }



    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ResAddressSearchBaseModel.AddressModel> callback) {
        isLoading.postValue(true);
        RetrofitClient.getInstance().getMoaAddressService()
                .addressSearch(reqModel.key, reqModel.currentPage, reqModel.countPerPage, reqModel.keyword, reqModel.resultType)
                .enqueue(new Callback<ResAddressSearchBaseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ResAddressSearchBaseModel> call, @NonNull Response<ResAddressSearchBaseModel> response) {
                        if (response.body() != null) {
                            isLoading.postValue(false);
                            if (response.body().getAddressModel() != null) {
                                callback.onResult(response.body().getAddressModel(), null, reqModel.currentPage + 1);
                                if (response.body().getAddressModel().size() == 0) {
                                    isDataEmpty.postValue(true);
                                } else {
                                    isDataEmpty.postValue(false);
                                }
                            } else {
                                isDataEmpty.postValue(true);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResAddressSearchBaseModel> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        isLoading.postValue(false);
                        isDataEmpty.postValue(true);
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ResAddressSearchBaseModel.AddressModel> callback) {}

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ResAddressSearchBaseModel.AddressModel> callback) {
        Logger.i("key : " + params.key);
        isLoading.postValue(true);
        RetrofitClient.getInstance().getMoaAddressService()
                .addressSearch(reqModel.key, params.key, reqModel.countPerPage, reqModel.keyword, reqModel.resultType)
                .enqueue(new Callback<ResAddressSearchBaseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ResAddressSearchBaseModel> call, @NonNull Response<ResAddressSearchBaseModel> response) {
                        if (response.body() != null) {
                            Integer key = response.body().getAddressModel().size() != 0 ? params.key + 1 : null;
                            Logger.i("페이징 완료 key setting : "+ key);
                            callback.onResult(response.body().getAddressModel(), key);
                            isLoading.postValue(false);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResAddressSearchBaseModel> call, @NonNull Throwable t) {
                        isLoading.postValue(false);
                    }
                });

    }
}
