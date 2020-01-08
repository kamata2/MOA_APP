package com.moaPlatform.moa.side_menu.coupon;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class CouponViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<CouponFragment> couponFragments;
    private ArrayList<String> titleLists;

    CouponViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(ArrayList<CouponFragment> couponFragments, ArrayList<String> titleLists) {
        this.couponFragments = couponFragments;
        this.titleLists = titleLists;
    }

    @Override
    public Fragment getItem(int position) {
        return couponFragments.get(position);
    }

    @Override
    public int getCount() {
        return couponFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleLists.get(position);
    }
}
