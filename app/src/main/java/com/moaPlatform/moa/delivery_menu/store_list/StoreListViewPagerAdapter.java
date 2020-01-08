package com.moaPlatform.moa.delivery_menu.store_list;


import com.moaPlatform.moa.util.BaseViewPagerAdapter;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class StoreListViewPagerAdapter extends BaseViewPagerAdapter {

    private ArrayList<String> titleList;

    StoreListViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void init(ArrayList<Fragment> fragments, ArrayList<String> titleList) {
        setFragmentList(fragments);
        this.titleList = titleList;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

}
