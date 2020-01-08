package com.moaPlatform.moa.side_menu.eventnotice;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.moaPlatform.moa.side_menu.eventnotice.view.event.EventListFragment;
import com.moaPlatform.moa.side_menu.eventnotice.view.notice.NoticeListFragment;

public class MainTabPagerAdapter extends FragmentPagerAdapter {
    private int mNumOfTabs;

    public MainTabPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new EventListFragment();
            case 1:
                return new NoticeListFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}


