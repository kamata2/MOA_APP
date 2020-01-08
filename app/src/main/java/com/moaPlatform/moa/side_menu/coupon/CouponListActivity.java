package com.moaPlatform.moa.side_menu.coupon;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import java.util.ArrayList;

import javax.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class CouponListActivity extends AppCompatActivity {
    public ViewPager couponViewPager;
    public TabLayout couponTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_main_activity);
        init();
        couponFragmentSetting();
    }

    private void init() {
        couponViewPager = findViewById(R.id.couponViewPager);
        couponTabLayout = findViewById(R.id.couponTablayout);
        CommonTitleView commonTitleOrderList = findViewById(R.id.commonTitleMyReview);
        commonTitleOrderList.setBackButtonClickListener(v -> finish());
        commonTitleOrderList.setTitleName(getString(R.string.side_coupon_list));
        commonTitleOrderList.isShowBottomLine(false);
    }

    private void couponFragmentSetting() {
        ArrayList<String> titleLists = new ArrayList<>();
        titleLists.add("사용 가능");
        titleLists.add("지난 쿠폰");

        CouponViewPagerAdapter couponViewPagerAdapter = new CouponViewPagerAdapter(getSupportFragmentManager());
        ArrayList<CouponFragment> couponFragments = new ArrayList<>();
        CouponFragment couponFragment = new CouponFragment();
        couponFragment.setUnavailableCoupon();
        couponFragments.add(couponFragment);
        couponFragments.add(new CouponFragment());
        couponViewPagerAdapter.setData(couponFragments, titleLists);
        couponViewPager.setAdapter(couponViewPagerAdapter);
        couponTabLayout.setupWithViewPager(couponViewPager);
        couponTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LinearLayout tabLayout = (LinearLayout) ((ViewGroup) couponTabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabLayout.getChildAt(1);
                Typeface tf = Typeface.createFromAsset(getAssets(), getString(R.string.font_name_nanum_gothic));
                tabTextView.setTypeface(tf, Typeface.NORMAL);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab ignored) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab ignored) {
            }
        });

    }
}
