package com.moaPlatform.moa.delivery_menu.store_detail;


import com.moaPlatform.moa.util.BaseViewPagerAdapter;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class StoreDetailViewPagerAdapter extends BaseViewPagerAdapter {

    private ArrayList<String> titles;

    StoreDetailViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void init(ArrayList<Fragment> fragments) {
        setFragmentList(fragments);
        titles = new ArrayList<>();
        titles.add("메뉴");
        titles.add("정보");
        titles.add("리뷰");
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
