package com.moaPlatform.moa.bottom_menu.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.activity.EventWebViewActivity;
import com.moaPlatform.moa.auth.sign_up.controller.LogInActivity;
import com.moaPlatform.moa.bottom_menu.main.adapter.SubMenuViewPagerAdapter;
import com.moaPlatform.moa.bottom_menu.main.fragments.MainSubMenuDeliveryFragment;
import com.moaPlatform.moa.bottom_menu.main.fragments.MainSubMenuEatOutFragment;
import com.moaPlatform.moa.bottom_menu.main.model.EatOutMainModel;
import com.moaPlatform.moa.bottom_menu.main.model.ResMainServiceModel;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.WalletMainActivity;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.constants.MoaUrlConstants;
import com.moaPlatform.moa.constants.PushConstants;
import com.moaPlatform.moa.intro.IntroActivity;
import com.moaPlatform.moa.notification.MyFirebaseMessagingService;
import com.moaPlatform.moa.notification.NotificationData;
import com.moaPlatform.moa.notification.ReqNotificationModel;
import com.moaPlatform.moa.preference.PushTokenPreference;
import com.moaPlatform.moa.side_menu.SideMainActivity;
import com.moaPlatform.moa.side_menu.customercenter.CustomerCenterMainActivity;
import com.moaPlatform.moa.top_menu.location.LocationSettingActivity;
import com.moaPlatform.moa.top_menu.search.StoreSearchActivity;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.auth.UserUseHelper;
import com.moaPlatform.moa.util.custom_view.NestedScrollingView;
import com.moaPlatform.moa.util.custom_view.NonScrollViewPager;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.ClassConnectInterface;
import com.moaPlatform.moa.util.interfaces.CodeListener;
import com.moaPlatform.moa.util.interfaces.HttpConnectionResult;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.manager.CompositeDisposableManager;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.singleton.App;
import com.moaPlatform.moa.util.toolbar.BottomToolbarController;
import com.moaPlatform.moa.util.toolbar.TopToolbarController;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 메인 화면
 * 원하는 카테고리 및 매뉴를 선택해서
 * 해당하는 화면으로 이동
 */
public class MainActivity extends AppCompatActivity implements CodeListener, HttpConnectionResult, ClassConnectInterface, RetrofitConnectionResult {

    // 카테고리의 아이콘과 아이콘의 텍스트 묶음 레이앙웃을 저장한 리스트
    private SparseArray<LinearLayout> categoryIcGroup;
    @BindView(R.id.nestedScrollView)
    NestedScrollingView nestedScrollView;
    @BindView(R.id.categoryLayout)
    ConstraintLayout categoryLayout;
    @BindView(R.id.subMenuViewpager)
    NonScrollViewPager subMenuViewPager;
    @BindView(R.id.selectCategory)
    TextView selectCategoryTitle;
    @BindView(R.id.categoryAnimation)
    LottieAnimationView categoryAnimation;
    @BindView(R.id.lvDeliveryCategoryBg)
    ImageView lvDeliveryCategoryBg;
    @BindView(R.id.lvEatOutCategoryBg)
    ImageView lvEatOutCategoryBg;
    // 카테고리 애니메이션을 저장할 array
    SparseArray<String> categoryAnimationFolders;
    private TopToolbarController topToolbarController;
    //    private int[] keyIds = {R.id.categoryDeliveryBg, R.id.categoryCommunityBg, R.id.categoryEatOutBg};
    private int[] keyIdsDemo = {R.id.categoryDeliveryBg, R.id.categoryEatOutBg};
    // 카테고리 rotation 관련 처리할때 사용
    private boolean isCategoryRotation = false;
    private int rotationCheckCount = 0;
    private int toolbarChangePosition = 0;

    private long backKeyPressedTime = 0;                //사용자가 뒤로가기를 하였을시 시간
    private final long onBackPresedFiishTime = 2000;    //두번 터치시 동작 유효시간 정의
    private Toast toast;
    private TextView tvCustomer;

    private UserUseHelper userUseHelper;
    private MainServerController serverController;
    TextView isLoginText;
    private boolean onlyAddressRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbarInit();
        init();
        initPagerListener();
        initFireBaseInstanceId();

        TextView customer = findViewById(R.id.customerServiceText);
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.customerServiceText:
                        activityMove(CustomerCenterMainActivity.class);
                        break;
                }

            }
        });
        actionNotification();
    }


    private void activityMove(Class moveClass) {
        Intent intent = new Intent(MainActivity.this, moveClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initPagerListener() {

        subMenuViewPager.setUseMeasure(true);
        subMenuViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Logger.d("subMenuViewPager position >>> " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 툴바 세팅
     */
    private void toolbarInit() {
        View topToolbar = findViewById(R.id.topToolbar);
        View bottomToolbar = findViewById(R.id.bottomToolbar);
        topToolbarController = new TopToolbarController(topToolbar);
        topToolbarController.setTopToolbarClickListener(this);
        topToolbarController.mainToolbarInit();
        BottomToolbarController bottomToolbarController = new BottomToolbarController(bottomToolbar);
        bottomToolbarController.setBottomToolbarClickListener(type -> {
            switch (type) {
                case BOTTOM_TOOLBAR_MAIN:
                    nestedScrollView.setScrollY(0);
                    break;
            }
        });
    }

    /**
     * 초기화
     */
    private void init() {

        userUseHelper = new UserUseHelper(this);
        userUseHelper.setClassConnectInterface(this);

        View moaEntrepreneurInfoLayout = findViewById(R.id.moaEntrepreneurInfoLayout);
        View clMoaEntrepreneurInfoContainer = findViewById(R.id.clMoaEntrepreneurInfoContainer);
        View ivMoaEntrepreneurArrow = findViewById(R.id.ivMoaEntrepreneurArrow);
        moaEntrepreneurInfoLayout.setOnClickListener(v -> {
            if (clMoaEntrepreneurInfoContainer.getVisibility() == View.VISIBLE) {
                clMoaEntrepreneurInfoContainer.setVisibility(View.GONE);
                ivMoaEntrepreneurArrow.setRotation(90);
            } else {
                clMoaEntrepreneurInfoContainer.setVisibility(View.VISIBLE);
                ivMoaEntrepreneurArrow.setRotation(-90);
            }
        });

        isLoginText = findViewById(R.id.isLoginText);
        isLoginText.setOnClickListener(v -> {
            if (userUseHelper.isUserLogin()) {
                YesOrNoDialog logOutDialog = new YesOrNoDialog();
                logOutDialog.dialogContent(getString(R.string.yes_or_no_dialog_content_logout));
                logOutDialog.show(getSupportFragmentManager(), "logOutDialog");
                logOutDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
                    @Override
                    public void onDialogNo() {
                        logOutDialog.dismiss();
                    }

                    @Override
                    public void onDialogYes() {
                        logOutDialog.dismiss();
                        savePref();
                        userUseHelper.userLogout();
                    }
                });

            } else {
                Intent loginIntent = new Intent(getApplicationContext(), LogInActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
        });

//        int[] groupIds = {R.id.categoryDeliveryGroup, R.id.categoryCommunityGroup, R.id.categoryEatOutGroup};
        tvCustomer = findViewById(R.id.customerServiceText);
        tvCustomer.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CustomerCenterMainActivity.class));
        });
        int[] groupIdsDemo = {R.id.categoryDeliveryGroup, R.id.categoryEatOutGroup};
        categoryIcGroup = new SparseArray<>();
        LinearLayout tempLinearLayout;
        for (int i = 0; i < groupIdsDemo.length; i++) {
            tempLinearLayout = findViewById(groupIdsDemo[i]);
            tempLinearLayout.setTag(R.string.category_position, i);
            categoryIcGroup.put(keyIdsDemo[i], tempLinearLayout);
        }

        serverController = new MainServerController(this);
        serverController.setHttpConnectionResult(this);
        serverController.setRetrofitConnectionResult(this);
        float toolbarSize = getResources().getDimension(R.dimen.top_toolbar_height);

        nestedScrollView.setScrollListener(state -> {
            Logger.d("스크롤 상태 체크 >>> " + state);
            if (state.equals(NestedScrollingView.SCROLL_STATE_START)) {
                Logger.d("스크롤 상태 체크 >>> 스크롤 시작");
                pauseCategoryAnimation(categoryAnimation);
            } else if (state.equals(NestedScrollingView.SCROLL_STATE_STOP)) {
                Logger.d("스크롤 상태 체크 >>> 스크롤 멈춤");
                resumeCategoryAnimation(categoryAnimation);
            } else {
                Logger.d("스크롤 상태 체크 >>> 스크롤 이동중");
            }
        });
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (toolbarChangePosition != 0 && scrollY > toolbarChangePosition - toolbarSize) {
                topToolbarController.setMainBgVisibility(View.VISIBLE);
            } else {
                topToolbarController.setMainBgVisibility(View.GONE);
            }
        });

        View useOfTerms = findViewById(R.id.useOfTerms);
        useOfTerms.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EventWebViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, BuildConfig.REST_API_BASE_URL + MoaUrlConstants.USE_TERMS_AND_CONDITIONS_URL);
            intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_TRANSPARENT_VALUE);
            startActivity(intent);
        });

        View ceoInfoCheck = findViewById(R.id.ceoInfoCheck);
        ceoInfoCheck.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EventWebViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, MoaUrlConstants.CEO_INFO_CHECK_URL);
            intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_SHOW_VALUE);
            startActivity(intent);
        });

        View privacyPolicy = findViewById(R.id.privacyPolicy);
        privacyPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EventWebViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, BuildConfig.REST_API_BASE_URL + MoaUrlConstants.PRIVACY_POLICY_URL);
            intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_TRANSPARENT_VALUE);
            startActivity(intent);
        });

    }

    private void savePref() {
        SharedPreferences sf = getSharedPreferences("haveiduser", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("memid", MoaAuthHelper.getInstance().getBasePrimaryInfo());
        editor.commit();
    }

    /**
     * 카테고리 클릭시 발생하는 발생하는 애니메이션 이벤트
     *
     * @param view 클릭한 view
     */
//    @OnClick({R.id.categoryEatOutBg, R.id.categoryCommunityBg, R.id.categoryDeliveryBg})
    @OnClick({R.id.categoryEatOutBg, R.id.categoryDeliveryBg})
    void categoryRotation(View view) {

        cancelCategoryAnimation(categoryAnimation);

        if (!isCategoryRotation) {
            isCategoryRotation = true;
            int position = Integer.valueOf(categoryIcGroup.get(view.getId()).getTag(R.string.category_position).toString());
            categoryLayout.animate().setDuration(300).rotationBy(position * 180).withEndAction(() -> {
                categoryInit(view.getId());
                rotationCheck();
            });
            LinearLayout animationLayout;
            for (int i = 0; i < categoryIcGroup.size(); i++) {
                int key = categoryIcGroup.keyAt(i);
                animationLayout = categoryIcGroup.get(key);
                animationLayout.animate().setDuration(300).rotationBy(-position * 180).withEndAction(this::rotationCheck);
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        categoryLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                toolbarChangePosition = categoryLayout.getTop();
                categoryLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void rotationCheck() {
        rotationCheckCount++;
//        if (rotationCheckCount == 4) {
//            rotationCheckCount = 0;
//            isCategoryRotation = false;
//        }
        if (rotationCheckCount == 3) {
            rotationCheckCount = 0;
            isCategoryRotation = false;
        }
    }

    /**
     * 카테고리 애니메이션 세팅
     */
    private void categoryAnimationInit() {
        categoryAnimationFolders = new SparseArray<>();
        categoryAnimationFolders.put(CodeTypeManager.MainActivityManager.CATEGORY_DELIVERY.getType(), "category_delivery");
        categoryAnimationFolders.put(CodeTypeManager.MainActivityManager.CATEGORY_EAT_OUT.getType(), "category_eat_out");
        categoryAnimationFolders.put(CodeTypeManager.MainActivityManager.CATEGORY_COMMUNITY.getType(), "category_eat_out");
    }

    /**
     * 카테고리 애니메이션 적용
     *
     * @param categoryCode 카테고리 코드
     */
    private void categoryAnimationLoad(int categoryCode) {
        String animationInfo = categoryAnimationFolders.get(categoryCode);
        categoryAnimation.setImageAssetsFolder(animationInfo);
        categoryAnimation.setAnimation(animationInfo + ".json");
        startCategoryAnimation(categoryAnimation);
    }

    @Override
    public void resultCode(int code) {
        if (code == CodeTypeManager.TopToolbarManager.NOW_LOCATION_SEARCH.getType()) {
            Intent intent = new Intent(this, LocationSettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (code == CodeTypeManager.TopToolbarManager.SUB_MENU_MOVE.getType()) {
            Intent intent = new Intent(this, SideMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (code == CodeTypeManager.TopToolbarManager.STORE_SEARCH.getType()) {
            Intent intent = new Intent(this, StoreSearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

//    /**
//     * 카테고리 세팅
//     */
//    private void categoryInit(ResMainServiceModel serviceModel) {
//        TextView addressName = findViewById(R.id.addressText);
//        addressName.setText(serviceModel.emdNm);
//        ArrayList<ResMainServiceModel.CategoryModel> categoryModels = serviceModel.categoryModels;
//        TextView title;
//        for (ResMainServiceModel.CategoryModel categoryModel : categoryModels) {
//
//            int categoryCode = Integer.valueOf(categoryModel.categoryCode);
//
//            if (categoryCode == CodeTypeManager.MainActivityManager.CATEGORY_DELIVERY.getType()) {
//                categoryIcGroup.get(R.id.categoryDeliveryBg).setTag(R.string.category_code, categoryModel.categoryCode);
//                title = findViewById(R.id.categoryDeliveryText);
//                selectCategoryTitle.setText(categoryModel.categoryName);
//                categoryAnimationLoad(categoryCode);
//            } else if (categoryCode == CodeTypeManager.MainActivityManager.CATEGORY_EAT_OUT.getType()) {
//                categoryIcGroup.get(R.id.categoryEatOutBg).setTag(R.string.category_code, categoryModel.categoryCode);
//                title = findViewById(R.id.categoryEatOutText);
//            } else {
//                categoryIcGroup.get(R.id.categoryCommunityBg).setTag(R.string.category_code, categoryModel.categoryCode);
//                title = findViewById(R.id.categoryCommunityText);
//            }
//
//            title.setText(categoryModel.categoryName);
//
//        }
//        viewPagerInit(serviceModel);
//    }

    /**
     * 카테고리 세팅
     */
    private void categoryInit(ResMainServiceModel serviceModel) {
        ArrayList<ResMainServiceModel.CategoryModel> categoryModels = serviceModel.categoryModels;
        TextView title;
        for (ResMainServiceModel.CategoryModel categoryModel : categoryModels) {

            int categoryCode = Integer.valueOf(categoryModel.categoryCode);

            if (categoryCode == CodeTypeManager.MainActivityManager.CATEGORY_DELIVERY.getType()) {
                categoryIcGroup.get(R.id.categoryDeliveryBg).setTag(R.string.category_code, categoryModel.categoryCode);
                title = findViewById(R.id.categoryDeliveryText);
                selectCategoryTitle.setText(categoryModel.categoryName);
                categoryAnimationLoad(categoryCode);
            } else {
                categoryIcGroup.get(R.id.categoryEatOutBg).setTag(R.string.category_code, categoryModel.categoryCode);
                title = findViewById(R.id.categoryEatOutText);
            }

            title.setText(categoryModel.categoryName);

        }

    }

//    /**
//     * 카테고리 클릭시 카테고리들의 포지션, 카테코리 애니메이션 등 변경
//     *
//     * @param clickId 클릭한 뷰의 아이디
//     */
//    private void categoryInit(int clickId) {
//        lvDeliveryCategoryBg.setVisibility(View.INVISIBLE);
//        lvEatOutCategoryBg.setVisibility(View.INVISIBLE);
//        switch (clickId) {
//            case R.id.categoryEatOutBg:
//                categoryIcGroup.get(R.id.categoryDeliveryBg).setTag(R.string.category_position, 1);
//                categoryIcGroup.get(R.id.categoryCommunityBg).setTag(R.string.category_position, 2);
//                selectCategoryTitle.setText("외식");
//                categoryAnimationLoad(CodeTypeManager.MainActivityManager.CATEGORY_EAT_OUT.getType());
//                lvEatOutCategoryBg.setVisibility(View.VISIBLE);
//                subMenuViewPager.setCurrentItem(1);
//                break;
//            case R.id.categoryCommunityBg:
//                categoryIcGroup.get(R.id.categoryEatOutBg).setTag(R.string.category_position, 1);
//                categoryIcGroup.get(R.id.categoryDeliveryBg).setTag(R.string.category_position, 2);
//                selectCategoryTitle.setText("커뮤니티");
//                categoryAnimationLoad(CodeTypeManager.MainActivityManager.CATEGORY_COMMUNITY.getType());
//                subMenuViewPager.setCurrentItem(0);
//                lvDeliveryCategoryBg.setVisibility(View.VISIBLE);
//                break;
//            case R.id.categoryDeliveryBg:
//                categoryIcGroup.get(R.id.categoryCommunityBg).setTag(R.string.category_position, 1);
//                categoryIcGroup.get(R.id.categoryEatOutBg).setTag(R.string.category_position, 2);
//                selectCategoryTitle.setText("배달");
//                categoryAnimationLoad(CodeTypeManager.MainActivityManager.CATEGORY_DELIVERY.getType());
//                lvDeliveryCategoryBg.setVisibility(View.VISIBLE);
//                subMenuViewPager.setCurrentItem(0);
//                break;
//        }
//        categoryIcGroup.get(clickId).setTag(R.string.category_position, 0);
//    }

    /**
     * 카테고리 클릭시 카테고리들의 포지션, 카테코리 애니메이션 등 변경
     *
     * @param clickId 클릭한 뷰의 아이디
     */
    private void categoryInit(int clickId) {
        lvDeliveryCategoryBg.setVisibility(View.INVISIBLE);
        lvEatOutCategoryBg.setVisibility(View.INVISIBLE);
        switch (clickId) {
            case R.id.categoryEatOutBg:
                categoryIcGroup.get(R.id.categoryDeliveryBg).setTag(R.string.category_position, 1);
                selectCategoryTitle.setText("플레이스");
                categoryAnimationLoad(CodeTypeManager.MainActivityManager.CATEGORY_EAT_OUT.getType());
                lvEatOutCategoryBg.setVisibility(View.VISIBLE);
                subMenuViewPager.setCurrentItem(1);
                break;
            case R.id.categoryDeliveryBg:
                categoryIcGroup.get(R.id.categoryEatOutBg).setTag(R.string.category_position, 1);
                selectCategoryTitle.setText("배달");
                categoryAnimationLoad(CodeTypeManager.MainActivityManager.CATEGORY_DELIVERY.getType());
                lvDeliveryCategoryBg.setVisibility(View.VISIBLE);
                subMenuViewPager.setCurrentItem(0);
                break;
        }
        categoryIcGroup.get(clickId).setTag(R.string.category_position, 0);
    }

    /**
     * viewpager 세팅
     */
    private void viewPagerInit(ResMainServiceModel serviceModel) {
        SubMenuViewPagerAdapter subMenuAdapter = new SubMenuViewPagerAdapter(getSupportFragmentManager());

        ArrayList<Fragment> subFragmentList = new ArrayList<>();
        subFragmentList.add(getMainSubMenuDeliveryFragment(serviceModel.mainDeliveryModel));
        subFragmentList.add(getMainSubMenuEatOutFragment(serviceModel.eatOutMainModel));
        subMenuAdapter.setFragments(subFragmentList);

        subMenuViewPager.setAdapter(subMenuAdapter);
    }

    /**
     * 외식 fragment
     *
     * @param eatOutMainModel
     * @return
     */
    private MainSubMenuEatOutFragment getMainSubMenuEatOutFragment(EatOutMainModel eatOutMainModel) {
        Bundle bundle = new Bundle();
        bundle.putString(MainSubMenuEatOutFragment.ARG_SUB_MENU_EAT_OUT_DATA, new Gson().toJson(eatOutMainModel));
        MainSubMenuEatOutFragment fragment = new MainSubMenuEatOutFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 배달 fragment
     *
     * @param mainDeliveryModel 배달 정보 model
     * @return 배달 fragment
     */
    private MainSubMenuDeliveryFragment getMainSubMenuDeliveryFragment(ResMainServiceModel.MainDeliveryModel mainDeliveryModel) {
        Bundle deliveryBundle = new Bundle();
        deliveryBundle.putString(CodeTypeManager.MainActivityManager.MAIN_DELIVERY_CATAGORY_DATA.toString(), new Gson().toJson(mainDeliveryModel));
        MainSubMenuDeliveryFragment deliveryFragment = new MainSubMenuDeliveryFragment();
        deliveryFragment.setArguments(deliveryBundle);
        return deliveryFragment;
    }

    private void userTypeCheck() {

//        ServerSideAuth serverSideAuth = new ServerSideAuth(this);
        if (userUseHelper.isUserLogin()) {
            isLoginText.setText(getString(R.string.logout));
        } else {
            isLoginText.setText(getString(R.string.login));
        }
    }

    @Override
    public void onHttpConnectionSuccess(int type, BaseModel baseModel) {
        if (type == CodeTypeManager.MainActivityManager.MAIN_SERVICE_INFO.getType()) {
            ResMainServiceModel resMainServiceModel = (ResMainServiceModel) baseModel;
            if (!onlyAddressRefresh) {
                categoryAnimationInit();
                categoryInit(resMainServiceModel);
                viewPagerInit(resMainServiceModel);
            }
            App.getInstance().userInfoModel = resMainServiceModel.userInfoModel;
            onlyAddressRefresh = true;
            TextView addressName = findViewById(R.id.addressText);
            //정회원 로그 아웃시 주소값 저장위해
//            SharedPreferences sf = getSharedPreferences("haveiduser", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sf.edit();
//            if (!TextUtils.isEmpty(resMainServiceModel.emdNm)) {
//                editor.putString("address", resMainServiceModel.emdNm);
//            }
//            editor.commit();
//
//            String sIdHave = sf.getString("address", "");
//            if (TextUtils.isEmpty(resMainServiceModel.emdNm)) {
//                addressName.setText(sIdHave);
//                //todo 지도 저장하기 이곳에서''
//            } else
            addressName.setText(resMainServiceModel.emdNm);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeCategoryAnimation(categoryAnimation);
        userUseHelper.onRefresh(this);
        serverController.getMainServiceList();
        userTypeCheck();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseCategoryAnimation(categoryAnimation);
    }

    //카테고리 애니메이션 로딩 시작
    private void startCategoryAnimation(LottieAnimationView animationView) {
        if (animationView != null && !animationView.isAnimating()) {
            animationView.playAnimation();
        }
    }

    //카테고리 애미내이션 로딩 재개
    private void resumeCategoryAnimation(LottieAnimationView animationView) {
        if (animationView != null && !animationView.isAnimating()) {
            animationView.resumeAnimation();
        }
    }

    //카테고리 애니메이션 로딩 일시정지
    private void pauseCategoryAnimation(LottieAnimationView animationView) {
        if (animationView != null && animationView.isAnimating()) {
            animationView.pauseAnimation();
        }
    }

    //카테고리 애니메이션 로딩 정지
    private void cancelCategoryAnimation(LottieAnimationView animationView) {
        if (animationView != null && animationView.isAnimating()) {
            animationView.cancelAnimation();
        }
    }

    @Override
    public void onBackPressed() {

        onCustomBackPressed();

        SharedPreferences sharedPreferences = getSharedPreferences("sign", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("oneLogin", "No");
        editor.apply();
    }

    public void onCustomBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + onBackPresedFiishTime) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, getString(R.string.common_toast_msg_finish), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + onBackPresedFiishTime) {
            toast.cancel();
            moveTaskToBack(true);
            CompositeDisposableManager.getInstance().dispose();
            finish();
//            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    public void onActType(CodeTypeManager.ClassCodeManager type) {
        switch (type) {
            case USER_LOGOUT:
                //추후 원복 대비 일부러 남김.
//                Realm.init(this);
//                RealmController realmController = new RealmController();
//                Realm realm = realmController.realmInstance();
//
//                realm.executeTransactionAsync(realm1 -> {
//                    IntroCheckModel introCheckModel = realm1.where(IntroCheckModel.class).findFirst();
//                    if (introCheckModel != null)
//                        introCheckModel.type = CodeTypeManager.RealmCodeManager.INTRO_SELECT_PERMISSION_SUCCESS.getCode();
//                }, () -> {
//                    realm.close();
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                });
                break;
        }
    }

    /**
     * FCM PUSH TOKEN 갱신 처리
     */
    private void initFireBaseInstanceId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Logger.d("getInstanceId failed " + task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Logger.d("getInstanceId token " + token);

                        if (!PushTokenPreference.getInstance().isUpload(MainActivity.this)) {
                            //등록된 적이 없으면 푸시키 등록 수행
                            if (ObjectUtil.checkNotNull(token)) {
                                ReqNotificationModel reqNotificationModel = new ReqNotificationModel();
                                reqNotificationModel.fcmToken = token;
                                if (serverController != null) {
                                    serverController.putFcmKey(reqNotificationModel);
                                }
                            }
                        }
                    }
                });
    }

    /**
     * notification 에 따른 화면 이동처리
     */
    private void actionNotification() {
        String data = getIntent().getStringExtra(MyFirebaseMessagingService.NOTIFICATION_DATA_KEY);
        if (data != null) {
            NotificationData notificationData = new Gson().fromJson(data, NotificationData.class);
            Logger.d("actionNotification >>> " + notificationData.toString());

            if (ObjectUtil.checkNotNull(notificationData.actionView)) {
                if (notificationData.actionView.equals(PushConstants.PUSH_ACTION_VIEW_MAIN_WALLET)) {
                    Intent intent = new Intent(MainActivity.this, WalletMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, MoaConstants.REQUEST_WALLET_ACTIVITY);
                } else if (notificationData.actionView.equals(PushConstants.PUSH_ACTION_VIEW_MAIN)) {
                    {
                        //아무동작 없음(메인화면으로 이동)
                    }
                }
            }
        }
    }


    @Override
    public void onRetrofitSuccess(int type, BaseModel baseModel) {
        if (!isFinishing()) {
            PushTokenPreference.getInstance().setUpload(this, true);
        }

    }

    @Override
    public void onRetrofitFail(int type, String msg) {
        if (!isFinishing()) {
            PushTokenPreference.getInstance().setUpload(this, false);
        }
    }
}