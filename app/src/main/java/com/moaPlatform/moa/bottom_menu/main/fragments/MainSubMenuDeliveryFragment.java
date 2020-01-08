package com.moaPlatform.moa.bottom_menu.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.main.model.ResMainServiceModel;
import com.moaPlatform.moa.util.StoreGridListFragment;
import com.moaPlatform.moa.util.interfaces.ViewDataInitHelper;
import com.moaPlatform.moa.util.manager.CodeTypeManager;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainSubMenuDeliveryFragment extends Fragment implements ViewDataInitHelper {

    private ResMainServiceModel.MainDeliveryModel mainDeliveryModel;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mainDeliveryModel = new Gson().fromJson(
                    getArguments().getString(CodeTypeManager.MainActivityManager.MAIN_DELIVERY_CATAGORY_DATA.toString()),
                    ResMainServiceModel.MainDeliveryModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_sub_menu_delivery_fragment, container, false);
        subMenuInit();
        advertisingInit();
        return view;
    }

    /**
     * 서브 메뉴 세팅
     */
    private void subMenuInit() {
        ((MainSubMenuFragment) Objects.requireNonNull(getChildFragmentManager().findFragmentById(R.id.submenuListFragment))).subMenuListUpdate(mainDeliveryModel.subMenuModelList);
    }

    /**
     * 광고 세팅
     */
    private void advertisingInit() {
        ViewDataInitHelper viewDataInitHelper = this;
        viewDataInitHelper.imageViewInit(view, R.id.lvFamousStoreBg, mainDeliveryModel.famousStoreBanner.bannerThumbnail);
        viewDataInitHelper.textViewInit(view, R.id.tvFamousStoreText, mainDeliveryModel.famousStoreBanner.bannerTitle);
        viewDataInitHelper.imageViewInit(view, R.id.lvNewFamousStoreBg, mainDeliveryModel.newStoreInfoBanner.bannerThumbnail);
        viewDataInitHelper.textViewInit(view, R.id.tvNewFamousStoreText, mainDeliveryModel.newStoreInfoBanner.bannerTitle);
        ((StoreGridListFragment) Objects.requireNonNull(getChildFragmentManager().findFragmentById(R.id.adListFragment))).listUpdate(mainDeliveryModel.adStoreList);

    }
}
