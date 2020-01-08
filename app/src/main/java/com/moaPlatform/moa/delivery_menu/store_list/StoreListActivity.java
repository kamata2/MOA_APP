package com.moaPlatform.moa.delivery_menu.store_list;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.activity.EventWebViewActivity;
import com.moaPlatform.moa.bottom_menu.main.model.ResMainServiceModel;
import com.moaPlatform.moa.delivery_menu.store_list.model.ResBannerModel;
import com.moaPlatform.moa.side_menu.SideMainActivity;
import com.moaPlatform.moa.top_menu.location.LocationSettingActivity;
import com.moaPlatform.moa.top_menu.search.StoreSearchActivity;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.dialog.yesOrNo.ListFilterDialog;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;
import com.moaPlatform.moa.util.toolbar.BottomToolbarController;
import com.moaPlatform.moa.util.toolbar.TopToolbarController;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.moaPlatform.moa.side_menu.SideMainActivity.MIN_CLICK_INTERVAL;

public class StoreListActivity extends AppCompatActivity implements ListItemClickListener {

    private Timer timer;
    private TimerTask mTask;
    private ViewPager storeListBanner;
    private BannerAdapter bannerAdapter;
    // 중복 클릭 방지 시간 설정
    private long mLastClickTime = 0;

    private StoreListViewPagerAdapter viewPagerAdapter;
    private ViewPager storeListViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_list_activity);
        init();
    }

    /**
     * 초기화
     */
    private void init() {

        bannerInit();
        viewPagerInit();
        toolbarInit();

        FloatingActionButton btStoreListFilter = findViewById(R.id.btStoreListFilter);
        ListFilterDialog listFilterDialog = new ListFilterDialog();
        btStoreListFilter.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < MIN_CLICK_INTERVAL) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            listFilterDialog.show(getSupportFragmentManager(), "filterDialog");
            listFilterDialog.setListFilterDialogFragmentListener(order -> {
                if (viewPagerAdapter != null && storeListViewPager != null) {
                    StoreListFragment storeListFragment;
                    for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
                        storeListFragment = (StoreListFragment) viewPagerAdapter.getItem(i);
                        storeListFragment.setStoreListSort(order);
                    }
                }
            });
        });
    }

    private void bannerInit() {
        storeListBanner = findViewById(R.id.storeListBanner);
        bannerAdapter = new BannerAdapter(this);
        storeListBanner.setAdapter(bannerAdapter);

        RetrofitClient.getInstance().getMoaService().getBanner(new CommonModel()).enqueue(new Callback<ResBannerModel>() {
            @Override
            public void onResponse(@NonNull Call<ResBannerModel> call, @NonNull Response<ResBannerModel> response) {
                ResBannerModel resBannerModel = response.body();
                if (resBannerModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resBannerModel)) {
                    bannerInit();
                    return;
                }
                if (resBannerModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    bannerAdapter.setTopbannerList(resBannerModel.topbannerList);
                    bannerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResBannerModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        timer = new Timer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerTaskInit();
    }

    private void timerTaskInit() {
        mTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> storeListBanner.setCurrentItem((storeListBanner.getCurrentItem() == bannerAdapter.getCount() - 1 ? 0 : storeListBanner.getCurrentItem() + 1)));
            }
        };
        if (timer != null) {
            timer.schedule(mTask, 0, 4500);
        }
    }

    private void toolbarInit() {
        View topToolbar = findViewById(R.id.topToolbar);
        TopToolbarController topToolbarController = new TopToolbarController(topToolbar);
        topToolbarController.deliveryToolbarInit();
        View viewBottomToolbar = findViewById(R.id.bottomToolbar);
        new BottomToolbarController(viewBottomToolbar);
        topToolbarController.setTopToolbarClickListener(code -> {
            if (code == CodeTypeManager.TopToolbarManager.BACK_BUTTON_PRESS.getType()) {
                finish();
            } else if (code == CodeTypeManager.TopToolbarManager.NOW_LOCATION_SEARCH.getType()) {
                Intent intent = new Intent(StoreListActivity.this, LocationSettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (code == CodeTypeManager.TopToolbarManager.STORE_SEARCH.getType()) {
                Intent intent = new Intent(StoreListActivity.this, StoreSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (code == CodeTypeManager.TopToolbarManager.SUB_MENU_MOVE.getType()) {
                Intent intent = new Intent(StoreListActivity.this, SideMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    /**
     * 카테고리에 따른 매장 리스트 세팅
     */
    @SuppressWarnings("unchecked")
    private void viewPagerInit() {
        ArrayList<ResMainServiceModel.SubMenuModel> subMenuModels = (ArrayList<ResMainServiceModel.SubMenuModel>) getIntent().getSerializableExtra(CodeTypeManager.DeliveryStoreManager.SUB_MENUS.toString());
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<Fragment> storeListFragments = new ArrayList<>();
        StoreListFragment storeListFragment;
        for (ResMainServiceModel.SubMenuModel subMenuModel : subMenuModels) {
            titles.add(subMenuModel.subMenuName);
            storeListFragment = new StoreListFragment();
            storeListFragment.subMenuCode = subMenuModel.subMenuCode;
            storeListFragments.add(storeListFragment);
        }

        viewPagerAdapter = new StoreListViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.init(storeListFragments, titles);
        TabLayout tabLayout = findViewById(R.id.subMenuTitles);
        storeListViewPager = findViewById(R.id.storeListViewPager);
        storeListViewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(storeListViewPager);
        storeListViewPager.setOffscreenPageLimit(storeListFragments.size());
        storeListViewPager.setCurrentItem(getIntent().getIntExtra(CodeTypeManager.UtilManager.ITEM_CLICK_POSITION.toString(), 0));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTask != null) {
            mTask.cancel();
        }
    }

    @Override
    public void clickItem(Object codeType, int position, Object object) {
        Logger.d("codeType is >>>" + codeType + " position is >>>" + Integer.valueOf(position) + " object is >>>" + object);
        if (ObjectUtil.checkNotNull(codeType) && ObjectUtil.checkNotNull(object)) {
            if (codeType.equals(BannerAdapter.CLICK_BANNER_IMAGE)) {
                String loarUrl = (String) object;
                if (ObjectUtil.checkNotNull(loarUrl)) {
                    Intent intent = new Intent(StoreListActivity.this, EventWebViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, BuildConfig.REST_API_BASE_URL + loarUrl);
                    String memberId = MoaAuthHelper.getInstance().getCurrentMemberID();
                    if (memberId.contains("@")) {
                        intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL_PARMS, "isLogin=Y");
                    } else {
                        intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL_PARMS, "isLogin=N");
                    }
                    intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_SHOW_VALUE);
                    intent.putExtra(EventWebViewActivity.EXTRA_TITLE_NAME, getString(R.string.event));
                    startActivity(intent);
                }
            }
        }
    }
}
