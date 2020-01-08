package com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

/**
 * Created by jiwun on 2019-07-22.
 */
public class ShoppingCartDetailViewModel extends ViewModel {

    private MutableLiveData<ResShoppingCartDetailModel.ShoppingCartInfoModel> storeInfoModel = new MutableLiveData<>();
    private MutableLiveData<List<ResShoppingCartDetailModel.ShoppingCartDetailModel>> productList = new MutableLiveData<>();

    public void setStoreInfoModel(ResShoppingCartDetailModel.ShoppingCartInfoModel storeInfoModel) {
        this.storeInfoModel.setValue(storeInfoModel);
        setProductList(storeInfoModel.shoppingCartDetailList);
    }

    public LiveData<List<ResShoppingCartDetailModel.ShoppingCartDetailModel>> getProductList() {
        return productList;
    }

    public void setProductList(List<ResShoppingCartDetailModel.ShoppingCartDetailModel> productList) {
        this.productList.setValue(productList);
    }

    public LiveData<ResShoppingCartDetailModel.ShoppingCartInfoModel> getStoreInfoModel() {
        return storeInfoModel;
    }

}
