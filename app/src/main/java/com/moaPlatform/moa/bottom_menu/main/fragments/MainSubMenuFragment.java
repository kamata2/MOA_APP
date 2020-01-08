package com.moaPlatform.moa.bottom_menu.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.main.adapter.SubMenuListAdapter;
import com.moaPlatform.moa.bottom_menu.main.model.ResMainServiceModel;
import com.moaPlatform.moa.delivery_menu.store_list.StoreListActivity;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.manager.CodeTypeManager;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainSubMenuFragment extends Fragment implements ListItemClickListener {

    private View view;
    private SubMenuListAdapter subMenuListAdapter;
    // 서브 메뉴 리스트
    private ArrayList<ResMainServiceModel.SubMenuModel> subMenuModelList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sub_menu_fragment, container, false);
        subMenuInit();
        return view;
    }

    /**
     * 서브 메뉴 리스트 초기화
     */
    private void subMenuInit() {
        RecyclerView subMenuList = view.findViewById(R.id.subMenuRecyclerView);
        subMenuListAdapter = new SubMenuListAdapter();
        subMenuListAdapter.setListItemClickListener(this);
        subMenuList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        subMenuList.setAdapter(subMenuListAdapter);
        subMenuList.setNestedScrollingEnabled(false);
    }

    /**
     * 서브 메뉴 리스트 업데이트
     */
    void subMenuListUpdate(ArrayList<ResMainServiceModel.SubMenuModel> subMenuModelList) {
        this.subMenuModelList = subMenuModelList;
        subMenuListAdapter.listUpdate(subMenuModelList);
    }

    @Override
    public void clickItem(Object codeType, int position, Object object) {
        Intent intent = new Intent(getContext(), StoreListActivity.class);
        intent.putExtra(CodeTypeManager.DeliveryStoreManager.SUB_MENUS.toString(), subMenuModelList);
        intent.putExtra(CodeTypeManager.UtilManager.ITEM_CLICK_POSITION.toString(), position);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
