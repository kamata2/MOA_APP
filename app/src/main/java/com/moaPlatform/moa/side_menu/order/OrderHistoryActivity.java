package com.moaPlatform.moa.side_menu.order;


import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.TimeUtil;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import static com.moaPlatform.moa.util.TimeUtil.DATE_FORMAT_SIMPLE;

public class OrderHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    public ViewPager pagerSideOrderMenu;
    public TabLayout tabSideOrderMenu;
    private List<String> tabList = new ArrayList<>();
    private TextView tvSideOrderMenuStartDate;
    private TextView tvSideOrderMenuEndDate;
    private TextView tvSideOrderMenu1Month;
    private TextView tvSideOrderMenu3Month;
    private TextView tvSideOrderMenu6Month;
    private TextView btnSideOrderMenuSearch;
    private OrderViewPagerAdapter orderViewPagerAdapter;
    private DatePickerDialog startDatePickerDialog;     //달력 시작일
    private DatePickerDialog endDatePickerDialog;       //달력 종료일

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        initView();
        initListener();
        initDialog();
        initFragment();
    }

    private void initView() {
        CommonTitleView commonTitleOrderList = findViewById(R.id.commonTitleMyReview);
        commonTitleOrderList.setBackButtonClickListener(v -> finish());
        commonTitleOrderList.setTitleName(getString(R.string.side_menu_order_title));
        commonTitleOrderList.isShowBottomLine(false);

        pagerSideOrderMenu = findViewById(R.id.pagerSideOrderMenu);
        tabSideOrderMenu = findViewById(R.id.tabSideOrderMenu);
        tvSideOrderMenuStartDate = findViewById(R.id.tvSideOrderMenuStartDate);
        tvSideOrderMenuEndDate = findViewById(R.id.tvSideOrderMenuEndDate);
        tvSideOrderMenu1Month = findViewById(R.id.tvSideOrderMenu1Month);
        tvSideOrderMenu3Month = findViewById(R.id.tvSideOrderMenu3Month);
        tvSideOrderMenu6Month = findViewById(R.id.tvSideOrderMenu6Month);
        btnSideOrderMenuSearch = findViewById(R.id.btnSideOrderMenuSearch);

        setCalendarTextView(TimeUtil.getStringFormatDate(DATE_FORMAT_SIMPLE, TimeUtil.getTodayCalendar(), -30)
                , TimeUtil.getStringFormatDate(DATE_FORMAT_SIMPLE, TimeUtil.getTodayCalendar()));
    }

    private void setCalendarTextView(String startDate, String endDate) {
        if (ObjectUtil.checkNotNull(startDate) && ObjectUtil.checkNotNull(endDate)) {
            if (tvSideOrderMenuStartDate != null) {
                tvSideOrderMenuStartDate.setText(startDate);
            }
            if (tvSideOrderMenuEndDate != null) {
                tvSideOrderMenuEndDate.setText(endDate);
            }
        }
    }

    private void initListener() {

        tvSideOrderMenuStartDate.setOnClickListener(this);
        tvSideOrderMenuEndDate.setOnClickListener(this);
        tvSideOrderMenu1Month.setOnClickListener(this);
        tvSideOrderMenu3Month.setOnClickListener(this);

        tvSideOrderMenu6Month.setOnClickListener(this);
        btnSideOrderMenuSearch.setOnClickListener(this);

    }

    private void initDialog() {

        Logger.d("initDialog >>> year " + TimeUtil.getCalendarYear(DATE_FORMAT_SIMPLE, tvSideOrderMenuStartDate.getText().toString()));
        Logger.d("initDialog >>> month " + TimeUtil.getCalendarMonth(DATE_FORMAT_SIMPLE, tvSideOrderMenuStartDate.getText().toString()));
        Logger.d("initDialog >>> date " + TimeUtil.getCalendarDay(DATE_FORMAT_SIMPLE, tvSideOrderMenuStartDate.getText().toString()));

        Logger.d("initDialog >>> year " + TimeUtil.getCalendarYear(DATE_FORMAT_SIMPLE, tvSideOrderMenuEndDate.getText().toString()));
        Logger.d("initDialog >>> month " + TimeUtil.getCalendarMonth(DATE_FORMAT_SIMPLE, tvSideOrderMenuEndDate.getText().toString()));
        Logger.d("initDialog >>> date " + TimeUtil.getCalendarDay(DATE_FORMAT_SIMPLE, tvSideOrderMenuEndDate.getText().toString()));

        startDatePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            if (tvSideOrderMenuStartDate != null) {
                tvSideOrderMenuStartDate.setText(TimeUtil.getStringFormatDate(TimeUtil.DATE_FORMAT_SIMPLE, year, month, dayOfMonth));
            }
        }, TimeUtil.getCalendar(DATE_FORMAT_SIMPLE, tvSideOrderMenuStartDate.getText().toString()).get(Calendar.YEAR)
                , TimeUtil.getCalendar(DATE_FORMAT_SIMPLE, tvSideOrderMenuStartDate.getText().toString()).get(Calendar.MONTH)
                , TimeUtil.getCalendar(DATE_FORMAT_SIMPLE, tvSideOrderMenuStartDate.getText().toString()).get(Calendar.DATE)
        );

        endDatePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            if (tvSideOrderMenuEndDate != null) {
                tvSideOrderMenuEndDate.setText(TimeUtil.getStringFormatDate(TimeUtil.DATE_FORMAT_SIMPLE, year, month, dayOfMonth));
            }
        }, TimeUtil.getCalendar(DATE_FORMAT_SIMPLE, tvSideOrderMenuEndDate.getText().toString()).get(Calendar.YEAR)
                , TimeUtil.getCalendar(DATE_FORMAT_SIMPLE, tvSideOrderMenuEndDate.getText().toString()).get(Calendar.MONTH)
                , TimeUtil.getCalendar(DATE_FORMAT_SIMPLE, tvSideOrderMenuEndDate.getText().toString()).get(Calendar.DATE)
        );
    }

    private void requestOrderList() {
        int index = pagerSideOrderMenu.getCurrentItem();
        OrderHistoryFragment fragment = (OrderHistoryFragment) orderViewPagerAdapter.getItem(index);
        if (fragment != null) {

            Calendar startCalendar = TimeUtil.getCalendar(DATE_FORMAT_SIMPLE, tvSideOrderMenuStartDate.getText().toString());
            Calendar endCalendar = TimeUtil.getCalendar(DATE_FORMAT_SIMPLE, tvSideOrderMenuEndDate.getText().toString());

            if (endCalendar.before(startCalendar)) {
                Toast.makeText(OrderHistoryActivity.this, getString(R.string.common_toast_msg_check_date), Toast.LENGTH_SHORT).show();
            } else {
                fragment.getOrderList(tvSideOrderMenuStartDate.getText().toString(), tvSideOrderMenuEndDate.getText().toString());
            }
        }
    }

    private void initFragment() {
        tabList.add(getString(R.string.side_menu_order_title));
        tabList.add(getString(R.string.side_menu_order_cancel));

        orderViewPagerAdapter = new OrderViewPagerAdapter(getSupportFragmentManager());
        ArrayList<OrderHistoryFragment> orderHistoryFragments = new ArrayList<>();

        OrderHistoryFragment orderHistortFragment = new OrderHistoryFragment();

        Bundle orderHistoryBundle = new Bundle();

        orderHistoryBundle.putString(OrderHistoryFragment.ARG_ORDER_LIST_START_DATE, TimeUtil.getStringFormatDate(DATE_FORMAT_SIMPLE, TimeUtil.getTodayCalendar(), -30));
        orderHistoryBundle.putString(OrderHistoryFragment.ARG_ORDER_LIST_END_DATE, TimeUtil.getStringFormatDate(DATE_FORMAT_SIMPLE, TimeUtil.getTodayCalendar()));
        orderHistoryBundle.putString(OrderHistoryFragment.ARG_ORDER_LIST_ORDER_TYPE, OrderRecyclerViewAdapter.LIST_TYPE_ORDER_TYPE);
        orderHistortFragment.setArguments(orderHistoryBundle);

        OrderHistoryFragment orderCancelHistoryFragment = new OrderHistoryFragment();

        Bundle orderCancelBundle = new Bundle();
        orderCancelBundle.putString(OrderHistoryFragment.ARG_ORDER_LIST_START_DATE, TimeUtil.getStringFormatDate(DATE_FORMAT_SIMPLE, TimeUtil.getTodayCalendar(), -30));
        orderCancelBundle.putString(OrderHistoryFragment.ARG_ORDER_LIST_END_DATE, TimeUtil.getStringFormatDate(DATE_FORMAT_SIMPLE, TimeUtil.getTodayCalendar()));
        orderCancelBundle.putString(OrderHistoryFragment.ARG_ORDER_LIST_ORDER_TYPE, OrderRecyclerViewAdapter.LIST_TYPE_CANCEL_TYPE);
        orderCancelHistoryFragment.setArguments(orderCancelBundle);

        orderHistoryFragments.add(orderHistortFragment);
        orderHistoryFragments.add(orderCancelHistoryFragment);

        orderViewPagerAdapter.setData(orderHistoryFragments, tabList);
        pagerSideOrderMenu.setAdapter(orderViewPagerAdapter);

        tabSideOrderMenu.setupWithViewPager(pagerSideOrderMenu);
        tabSideOrderMenu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LinearLayout tabLayout = (LinearLayout) ((ViewGroup) tabSideOrderMenu.getChildAt(0)).getChildAt(tab.getPosition());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSideOrderMenuStartDate:     //시작일
                showStartDatePickerDialog();
                break;

            case R.id.tvSideOrderMenuEndDate:       //종료일
                showEndDatePickerDialog();
                break;

            case R.id.tvSideOrderMenu1Month:        //1개월
                setCalendarTextView(TimeUtil.getStringFormatDate(TimeUtil.DATE_FORMAT_SIMPLE, TimeUtil.getTodayCalendar(), -30)
                        , TimeUtil.getStringFormatDate(TimeUtil.DATE_FORMAT_SIMPLE, TimeUtil.getTodayCalendar()));
                break;

            case R.id.tvSideOrderMenu3Month:        //3개월
                setCalendarTextView(TimeUtil.getStringFormatDate(TimeUtil.DATE_FORMAT_SIMPLE, TimeUtil.getTodayCalendar(), -90)
                        , TimeUtil.getStringFormatDate(TimeUtil.DATE_FORMAT_SIMPLE, TimeUtil.getTodayCalendar()));
                break;

            case R.id.tvSideOrderMenu6Month:        //6개월
                setCalendarTextView(TimeUtil.getStringFormatDate(TimeUtil.DATE_FORMAT_SIMPLE, TimeUtil.getTodayCalendar(), -180)
                        , TimeUtil.getStringFormatDate(TimeUtil.DATE_FORMAT_SIMPLE, TimeUtil.getTodayCalendar()));
                break;

            case R.id.btnSideOrderMenuSearch:       //기간조회
                requestOrderList();
                break;
        }
    }

    private void showStartDatePickerDialog() {
        if (startDatePickerDialog != null && !startDatePickerDialog.isShowing()) {
            startDatePickerDialog.show();
        }
    }

    private void showEndDatePickerDialog() {
        if (endDatePickerDialog != null && !endDatePickerDialog.isShowing()) {
            endDatePickerDialog.show();
        }
    }
}
