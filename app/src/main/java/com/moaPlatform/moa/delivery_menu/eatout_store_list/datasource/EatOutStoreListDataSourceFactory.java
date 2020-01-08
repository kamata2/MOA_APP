package com.moaPlatform.moa.delivery_menu.eatout_store_list.datasource;

import com.moaPlatform.moa.delivery_menu.eatout_store_list.model.ReqEatOutStoreListModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class EatOutStoreListDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<EatOutStoreListDataSource> eatOutStoreListDataSource = new MutableLiveData<>();
    private ReqEatOutStoreListModel reqModel;

    public EatOutStoreListDataSourceFactory(ReqEatOutStoreListModel reqModel) {
        this.reqModel = reqModel;
    }

    @NonNull
    @Override
    public DataSource create() {
        EatOutStoreListDataSource dataSource = new EatOutStoreListDataSource(reqModel);
        eatOutStoreListDataSource.postValue(dataSource);
        return dataSource;
    }

    public MutableLiveData<EatOutStoreListDataSource> getEatOutStoreListDataSource() {
        return eatOutStoreListDataSource;
    }
}
