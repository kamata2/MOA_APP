package com.moaPlatform.moa.side_menu.review_view;

import com.moaPlatform.moa.fragment.ReviewFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

/**
 * 배달리뷰 & 플래이스 리뷰 PagerAdapter
 */
public class ReviewPagerAdapter extends FragmentPagerAdapter {

    private List<ReviewFragment> reviewFragmentList;

    public ReviewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return reviewFragmentList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        if(reviewFragmentList == null){
            return 0;
        }else{
            return reviewFragmentList.size();
        }
    }

    public void setFragmentList(List<ReviewFragment> list) {
        this.reviewFragmentList = list;
        notifyDataSetChanged();
    }
}
