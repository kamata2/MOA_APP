package com.moaPlatform.moa.delivery_menu.store_list.model;

import com.moaPlatform.moa.delivery_menu.store_list.datasource.StoreListDataSource;
import com.moaPlatform.moa.delivery_menu.store_list.datasource.StoreListDataSourceFactory;
import com.moaPlatform.moa.util.models.StoreInfoModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class StoreListViewModel extends ViewModel {

    public LiveData<PagedList<StoreInfoModel>> storeList;
    public LiveData<Boolean> isLoading;
    public LiveData<Boolean> isEmpty;

    @SuppressWarnings("unchecked")
    public void storeListInit(ReqStoreListModel reqModel) {
        StoreListDataSourceFactory dataSourceFactory = new StoreListDataSourceFactory(reqModel);
        isLoading = Transformations.switchMap(dataSourceFactory.getStoreListLiveDataSource(), StoreListDataSource::getIsLoading);
        isEmpty = Transformations.switchMap(dataSourceFactory.getStoreListLiveDataSource(), StoreListDataSource::getIsEmpty);
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .build();
        storeList = new LivePagedListBuilder(dataSourceFactory, config).build();
    }
}
