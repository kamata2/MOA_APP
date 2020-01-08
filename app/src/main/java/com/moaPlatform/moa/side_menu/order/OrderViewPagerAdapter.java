package com.moaPlatform.moa.side_menu.order;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class OrderViewPagerAdapter extends FragmentPagerAdapter {
    private List<OrderHistoryFragment> orderHistoryFragments;
    private List<String> orderTitles;

    OrderViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<OrderHistoryFragment> orderHistoryFragments, List<String> orderTitles) {
        this.orderHistoryFragments = orderHistoryFragments;
        this.orderTitles = orderTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return orderHistoryFragments.get(position);
    }

    @Override
    public int getCount() {
        return orderHistoryFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return orderTitles.get(position);
    }
}
