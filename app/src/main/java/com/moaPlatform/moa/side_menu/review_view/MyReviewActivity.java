package com.moaPlatform.moa.side_menu.review_view;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.fragment.ReviewFragment;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

/**
 * 내가 쓴 리뷰 글 보기 화면
 */
public class MyReviewActivity extends AppCompatActivity {

    private CommonTitleView commonTitle;
    private TabLayout tabMyReview;
    private ViewPager pager;
    private ReviewPagerAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_review);

        initView();
        initViewPager();
        initListener();
    }

    private void initView() {

        commonTitle = findViewById(R.id.commonTitleMyReview);
        commonTitle.setBackButtonClickListener(v -> finish());
        commonTitle.setTitleName(getString(R.string.side_menu_review_title));
        commonTitle.isShowBottomLine(false);

        tabMyReview = findViewById(R.id.tabMyReview);
        tabMyReview.addTab(tabMyReview.newTab().setText(getString(R.string.side_menu_review_tab_store)));
        tabMyReview.addTab(tabMyReview.newTab().setText(getString(R.string.side_menu_review_tab_eat_out)));
        tabMyReview.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void initViewPager() {
        this.pager = findViewById(R.id.pagerMyReview);
        this.adapter = new ReviewPagerAdapter(getSupportFragmentManager());
        this.pager.setAdapter(adapter);

        //배달 리뷰
        ReviewFragment storeReviewFragment = new ReviewFragment();
        Bundle storeReviewFragmentBundle = new Bundle();
        storeReviewFragmentBundle.putBoolean(ReviewFragment.EXTRA_REVIEW_MY_WRITING, true);
        storeReviewFragmentBundle.putString(ReviewFragment.EXTRA_REVIEW_LIST_TYPE, ReviewFragment.REVIEW_LIST_TYPE_DELRIVERY);
        storeReviewFragment.setArguments(storeReviewFragmentBundle);

        //외식 리뷰
        ReviewFragment eatOutReviewFragment = new ReviewFragment();
        Bundle eatOutReviewFragmentBundle = new Bundle();
        eatOutReviewFragmentBundle.putBoolean(ReviewFragment.EXTRA_REVIEW_MY_WRITING, true);
        eatOutReviewFragmentBundle.putString(ReviewFragment.EXTRA_REVIEW_LIST_TYPE, ReviewFragment.REVIEW_LIST_TYPE_EAT_OUT);
        eatOutReviewFragment.setArguments(eatOutReviewFragmentBundle);

        List<ReviewFragment> reviewFragmentList = new ArrayList<>();
        reviewFragmentList.add(storeReviewFragment);
        reviewFragmentList.add(eatOutReviewFragment);
        adapter.setFragmentList(reviewFragmentList);

    }

    private void initListener(){

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabMyReview));
        tabMyReview.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}

