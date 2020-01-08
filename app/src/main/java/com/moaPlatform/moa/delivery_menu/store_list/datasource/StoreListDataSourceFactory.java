package com.moaPlatform.moa.delivery_menu.store_list.datasource;

import com.moaPlatform.moa.delivery_menu.store_list.model.ReqStoreListModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class StoreListDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<StoreListDataSource> storeListLiveDataSource = new MutableLiveData<>();
    private ReqStoreListModel reqModel;

    public StoreListDataSourceFactory(ReqStoreListModel reqModel) {
        this.reqModel = reqModel;
    }

    @NonNull
    @Override
    public DataSource create() {
        StoreListDataSource dataSource = new StoreListDataSource(reqModel);
        storeListLiveDataSource.postValue(dataSource);
        return dataSource;
    }

    public MutableLiveData<StoreListDataSource> getStoreListLiveDataSource() {
        return storeListLiveDataSource;
    }
}
