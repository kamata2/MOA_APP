package com.moaPlatform.moa.top_menu.location.model;

import com.moaPlatform.moa.top_menu.location.datasource.AddressDataSource;
import com.moaPlatform.moa.top_menu.location.datasource.AddressDataSourceFactory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;

public class AddressViewModel extends ViewModel {
    public LiveData<PagedList<ResAddressSearchBaseModel.AddressModel>> addressList;
    public LiveData<Boolean> isLoading;
    public LiveData<Boolean> isDataEmpty;

    public MutableLiveData<List<ResAddressSearchBaseModel.AddressHistoryModel>> userAddressList = new MutableLiveData<>();

    public void setAddressList(List<ResAddressSearchBaseModel.AddressHistoryModel> userAddressList) {
        this.userAddressList.setValue(userAddressList);
    }

    public void removeItemAddressList(int removePosition) {
        List<ResAddressSearchBaseModel.AddressHistoryModel> tempAddressHistoryModelList = userAddressList.getValue();
        tempAddressHistoryModelList.remove(removePosition);
        userAddressList.setValue(tempAddressHistoryModelList);
    }

    public LiveData<List<ResAddressSearchBaseModel.AddressHistoryModel>> getUserAddressList() {
        return userAddressList;
    }

    @SuppressWarnings("unchecked")
    public void addressInit(String inputAddress) {
        AddressDataSourceFactory addressDataSourceFactory = new AddressDataSourceFactory(inputAddress);

        isLoading = Transformations.switchMap(addressDataSourceFactory.getAddressLiveDataSource(), AddressDataSource::getIsLoading);
        isDataEmpty = Transformations.switchMap(addressDataSourceFactory.getAddressLiveDataSource(), AddressDataSource::getIsDataEmpty);
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(100)
                .build();
        addressList = new LivePagedListBuilder(addressDataSourceFactory, config).build();
    }

}
