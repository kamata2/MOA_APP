package com.moaPlatform.moa.side_menu.eventnotice.view.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.SideMainActivity;
import com.moaPlatform.moa.side_menu.eventnotice.MainTabPagerAdapter;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.interfaces.CodeListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class EventMainActivity extends AppCompatActivity implements CodeListener {
    View topToolbar;
    private TabLayout eventTabLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_event_noti_main);

        initView();
        initEventFragment();
    }

    private void initView() {
        topToolbar = findViewById(R.id.topToolbar);
        //뷰 페이저
        eventTabLayout = findViewById(R.id.event_notice_tabs);
        eventTabLayout.addTab(eventTabLayout.newTab().setText(getString(R.string.event)));
        eventTabLayout.addTab(eventTabLayout.newTab().setText(getString(R.string.notice)));
        eventTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        CommonTitleView commonTitleOrderList = findViewById(R.id.commonTitleMyReview);
        commonTitleOrderList.setBackButtonClickListener(v -> finish());
        commonTitleOrderList.setTitleName(getString(R.string.event_or_notice));
        commonTitleOrderList.isShowBottomLine(false);
    }

    private void initEventFragment() {
        final ViewPager viewPager = findViewById(R.id.event_notice_viewpager);
        final MainTabPagerAdapter adapter = new MainTabPagerAdapter
                (getSupportFragmentManager(), eventTabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(eventTabLayout));
        eventTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void resultCode(int code) {
        Intent intent = new Intent(this, SideMainActivity.class);
        startActivity(intent);
    }
}