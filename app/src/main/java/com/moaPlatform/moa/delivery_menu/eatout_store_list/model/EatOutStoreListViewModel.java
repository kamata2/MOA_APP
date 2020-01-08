package com.moaPlatform.moa.delivery_menu.eatout_store_list.model;

import com.moaPlatform.moa.bottom_menu.main.model.EatOutStoreModel;
import com.moaPlatform.moa.delivery_menu.eatout_store_list.datasource.EatOutStoreListDataSource;
import com.moaPlatform.moa.delivery_menu.eatout_store_list.datasource.EatOutStoreListDataSourceFactory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class EatOutStoreListViewModel extends ViewModel {

    public LiveData<PagedList<EatOutStoreModel>> storeList;
    public LiveData<Boolean> isProgress;

    @SuppressWarnings("unchecked")
    public void storeListInit(ReqEatOutStoreListModel reqModel) {
        EatOutStoreListDataSourceFactory dataSourceFactory = new EatOutStoreListDataSourceFactory(reqModel);
        isProgress = Transformations.switchMap(dataSourceFactory.getEatOutStoreListDataSource(), EatOutStoreListDataSource::getIsProgress);

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .build();

        storeList = new LivePagedListBuilder(dataSourceFactory, config).build();
    }
}