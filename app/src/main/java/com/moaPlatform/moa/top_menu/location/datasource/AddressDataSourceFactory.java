package com.moaPlatform.moa.top_menu.location.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class AddressDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<AddressDataSource> addressLiveDataSource = new MutableLiveData<>();
    private String inputAddress;

    public AddressDataSourceFactory(String inputAddress) {
        this.inputAddress = inputAddress;
    }

    @NonNull
    @Override
    public DataSource create() {
        AddressDataSource addressDataSource = new AddressDataSource(inputAddress);
        addressLiveDataSource.postValue(addressDataSource);
        return addressDataSource;
    }

    public MutableLiveData<AddressDataSource> getAddressLiveDataSource() {
        return addressLiveDataSource;
    }
}
