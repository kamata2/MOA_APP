package com.moaPlatform.moa.delivery_menu.store_detail.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by jiwun on 2019-07-24.
 * <p>
 * 가맹점 정보 표시해주는 모델
 */
public class StoreAddInfoViewModel extends ViewModel {

    private MutableLiveData<ResStoreDetailInfo.StoreAddInfoModel> storeAddInfoModel = new MutableLiveData<>();

    public void setStoreAddInfoModel(ResStoreDetailInfo.StoreAddInfoModel storeAddInfoModel) {
        this.storeAddInfoModel.setValue(storeAddInfoModel);

    }

    public LiveData<ResStoreDetailInfo.StoreAddInfoModel> getStoreAddInfoModel() {
        return storeAddInfoModel;
    }
}
