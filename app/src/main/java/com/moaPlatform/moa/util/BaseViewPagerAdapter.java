package com.moaPlatform.moa.util;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public abstract class BaseViewPagerAdapter extends FragmentPagerAdapter {

    // 프레그먼트 리스트
    private ArrayList<Fragment> fragmentList;

    public void setFragmentList(ArrayList<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
    }

    public BaseViewPagerAdapter(FragmentManager fm ) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
