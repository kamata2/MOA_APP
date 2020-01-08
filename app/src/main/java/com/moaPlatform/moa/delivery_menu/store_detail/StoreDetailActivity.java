package com.moaPlatform.moa.delivery_menu.store_detail;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.shopping_cart.main.ShoppingCartActivity;
import com.moaPlatform.moa.constants.EatOutConstants;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.delivery_menu.store_detail.model.ResStoreDetailInfo;
import com.moaPlatform.moa.fragment.ReviewFragment;
import com.moaPlatform.moa.model.data_model.StoreInfoChangedModel;
import com.moaPlatform.moa.model.req_model.ReqStoreInfoModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.StoreGridListFragment;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.TimeEventHelper;
import com.moaPlatform.moa.util.custom_view.CommonLoadingView;
import com.moaPlatform.moa.util.custom_view.CommonTimeEventView;
import com.moaPlatform.moa.util.custom_view.NonScrollViewPager;
import com.moaPlatform.moa.util.interfaces.CodeListener;
import com.moaPlatform.moa.util.interfaces.HttpConnectionResult;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.interfaces.TextSettingHtmlFormat;
import com.moaPlatform.moa.util.interfaces.ViewDataInitHelper;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.manager.KakaoApiManager;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.StoreInfoModel;
import com.moaPlatform.moa.util.singleton.App;
import com.moaPlatform.moa.util.toolbar.TopToolbarController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

public class StoreDetailActivity extends AppCompatActivity implements HttpConnectionResult, TextSettingHtmlFormat, ViewDataInitHelper, CodeListener {

    // 뷰페이저 어뎁터
    private StoreDetailViewPagerAdapter storeDetailViewPagerAdapter;
    public int storeId = 0;
    private String storeName;

    // 매장 정보 reqModel, 매장 리뷰, 메뉴, 정보의 데이터를 불러올떄 공통으로 사용
    private ReqStoreInfoModel reqStoreInfoModel;
    // 서버와 통신할 컨트롤러
    private StoreDetailServerController storeDetailServerController;
    // 즐겨찾기 체크박스
    private CheckBox cbBookMark;
    // ui 변경 관련
    private ViewDataInitHelper viewDataInitHelper = this;
    // 장바구니 이동 버튼
    private Button btnShoppingCart;
    // 자바구니 갱신 체크용 플레그
    private boolean isShoppingCartCountRefresh = false;

    public String deliveryTitle = "";

    private NonScrollViewPager viewPager;
    private String phoneNumber = null;

    private TopToolbarController topToolbarController;

    public RequestManager mGlideRequestManager;
    private CommonLoadingView viewStoreDetailLoading;
    private boolean isLoading = true;

    private StoreInfoChangedModel storeInfoChangedModel;        //변경된 데이터 정보를 담는다.
    private boolean isStoreInfoChanged = false;                 //변경된 데이터 정보가 있는지 체크 (북마크, 리뷰 cnt 변경되었는지만 체크)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_detail_activity);
        mGlideRequestManager = Glide.with(getApplicationContext());
        init();
        viewPagerInit();
        getStoreInfo();
    }

    /**
     * 기본 변수들 초기화
     */
    private void init() {

        storeInfoChangedModel = new StoreInfoChangedModel();
        storeInfoChangedModel.position = getIntent().getIntExtra(MoaConstants.EXTRA_STORE_LIST_POSITION, 0);

        // 로딩 세팅
        viewStoreDetailLoading = findViewById(R.id.viewStoreDetailLoading);
        viewStoreDetailLoading.loadingAnimationPlay(this);

        View topToolbar = findViewById(R.id.topToolbar);

        btnShoppingCart = findViewById(R.id.btnShoppingCart);
        btnShoppingCart.setOnClickListener(v -> goShoppingCart());

        topToolbarController = new TopToolbarController(topToolbar);
        topToolbarController.setTopToolbarClickListener(this);
        topToolbarController.storeDetailInit();

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.viewCollapsing);
        AppBarLayout.OnOffsetChangedListener listener = (appBarLayout, i) -> {
            if ((collapsingToolbarLayout.getHeight() + i < 2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout))) {
                topToolbar.setBackgroundResource(R.color.white);
                topToolbarController.backArrowBlack();
                topToolbarController.isTitleShow(true);
            } else {
                topToolbar.setBackgroundResource(R.color.transparent);
                topToolbarController.backArrowWhite();
                topToolbarController.isTitleShow(false);
            }
        };

        final AppBarLayout appbar = findViewById(R.id.appbarLayout);
        appbar.addOnOffsetChangedListener(listener);

        storeId = getIntent().getIntExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), 0);
        storeName = getIntent().getStringExtra(MoaConstants.EXTRA_STORE_NAME);
        reqStoreInfoModel = new ReqStoreInfoModel();
        reqStoreInfoModel.storeId = storeId;

    }

    /**
     * 가맹점에 전화 걸기
     */
    private void phoneCall() {

        if (phoneNumber != null) {
            TedPermission.with(this).setPermissionListener(new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    Intent intent = new Intent(Intent.ACTION_DIAL);     //ACTION_CALL 전화 바로 걸기 | ACTION_DIAL 전화 화면으로 이동
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                }

                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                }
            }).setDeniedMessage(R.string.call_permission_denied)
                    .setPermissions(Manifest.permission.CALL_PHONE).check();
        }
    }

    /**
     * 장바구니로 이동
     */
    private void goShoppingCart() {
        Intent shoppingCart = new Intent(this, ShoppingCartActivity.class);
        shoppingCart.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        shoppingCart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(shoppingCart);
    }

    private void viewPagerInit() {
        viewPager = findViewById(R.id.storeViewPager);
        viewPager.setPagingEnabled(true);
        TabLayout storeDetailTabMenu = findViewById(R.id.storeDetailTabMenu);
        storeDetailViewPagerAdapter = new StoreDetailViewPagerAdapter(getSupportFragmentManager());
        ArrayList<Fragment> menusFragments = new ArrayList<>();
        StoreGridListFragment storeGridListFragment = new StoreGridListFragment();
        Bundle storeMenuBundle = new Bundle();
        storeMenuBundle.putString(MoaConstants.EXTRA_STORE_NAME, storeName);
        storeGridListFragment.setArguments(storeMenuBundle);
        menusFragments.add(storeGridListFragment);
        StoreAdditionalInformationFragment storeAdditionalInformationFragment = new StoreAdditionalInformationFragment();
        storeAdditionalInformationFragment.setReqStoreInfoModel(reqStoreInfoModel);
        menusFragments.add(storeAdditionalInformationFragment);

        ReviewFragment storeReviewFragment = new ReviewFragment();
        Bundle storeReviewFragmentBundle = new Bundle();
        storeReviewFragmentBundle.putInt(MoaConstants.EXTRA_STORE_ID, storeId);
        storeReviewFragmentBundle.putString(MoaConstants.EXTRA_STORE_NAME, storeName);
        storeReviewFragmentBundle.putString(ReviewFragment.EXTRA_REVIEW_LIST_TYPE, ReviewFragment.REVIEW_LIST_TYPE_DELRIVERY);
        storeReviewFragmentBundle.putBoolean(ReviewFragment.EXTRA_TITLE_VISIBILITY, false);
        storeReviewFragment.setArguments(storeReviewFragmentBundle);

        menusFragments.add(storeReviewFragment);

        storeDetailViewPagerAdapter.init(menusFragments);
        viewPager.setAdapter(storeDetailViewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        storeDetailTabMenu.setupWithViewPager(viewPager);

        View eventNoticeGroup = findViewById(R.id.eventNoticeGroup);
        eventNoticeGroup.setOnClickListener(v -> {
            if (viewPager != null)
                viewPager.setCurrentItem(1);
        });

    }

    /**
     * 가맹점 상세 정보 받아오기
     */
    private void getStoreInfo() {
        storeDetailServerController = new StoreDetailServerController(this);
        storeDetailServerController.setHttpConnectionResult(this);
        storeDetailServerController.getStoreDetailInfo(reqStoreInfoModel);
        storeDetailServerController.shoppingCartCountCheck();
    }

    /**
     * 가맹점 기본 정보 세팅
     */
    private void storeDefaultInfoInit(StoreInfoModel storeInfoModel) {
        topToolbarController.setTitle(storeInfoModel.storeName);
        View view = getWindow().getDecorView();
        viewDataInitHelper.textViewInit(view, R.id.tvStoreName, storeInfoModel.storeName);
        viewDataInitHelper.textViewInit(view, R.id.tvReviewCnt, storeInfoModel.storeReviewCnt);
        viewDataInitHelper.textViewInit(view, R.id.tvBookMarkCnt, storeInfoModel.bookmarkCnt);
        viewDataInitHelper.textViewInit(view, R.id.tvRatingPoint, storeInfoModel.ratingPoint);

        TextView tvDeliveryMoney = findViewById(R.id.deliveryMoney);
        TextView tvOrderMoney = findViewById(R.id.orderMoney);
        String minOrderText;
        if (storeInfoModel.minOrderPrice == null || storeInfoModel.minOrderPrice.equals("0")) {
            minOrderText = getString(R.string.activity_store_detail_call_enquiry);
        } else {
            minOrderText = getString(R.string.activity_store_detail_min_order_money, StringUtil.convertCommaPrice(storeInfoModel.minOrderPrice));
        }
        tvOrderMoney.setText(minOrderText);
        // 최소 주문 금액
        if (storeInfoModel.deliveryPrice.contains("원")) {
            tvDeliveryMoney.setText(storeInfoModel.deliveryPrice);
        } else {
            String deliveryMoneyText;
            if (storeInfoModel.deliveryPrice == null || storeInfoModel.deliveryPrice.equals("0") && storeInfoModel.freeDeliveryPrice == 0 || storeInfoModel.deliveryPrice.equals("0")) {
                deliveryMoneyText = getString(R.string.activity_store_detail_call_enquiry);
            } else {
                String deliveryPriceText = StringUtil.convertCommaPrice(storeInfoModel.deliveryPrice);
                String freeDeliveryPriceText = StringUtil.convertCommaPrice(storeInfoModel.freeDeliveryPrice);
                deliveryMoneyText = getString(R.string.activity_store_detail_delivery_money, deliveryPriceText, freeDeliveryPriceText);
            }
            tvDeliveryMoney.setText(deliveryMoneyText);
        }

        viewDataInitHelper.textViewInitHtml(view, R.id.eventNotice, R.string.store_detail_activity_event_guide, storeInfoModel.storeInfo, this);

        //todo 2019.08.08 추후 타임이벤트 표시 하는 부분에 마진 값 적용 필요
        CommonTimeEventView viewStoreDetailTimeEventOne, viewStoreDetailTimeEventTwo, viewStoreDetailTimeEventThree, viewStoreDetailTimeEventFour;
        viewStoreDetailTimeEventOne = findViewById(R.id.viewStoreDetailTimeEventOne);
        viewStoreDetailTimeEventTwo = findViewById(R.id.viewStoreDetailTimeEventTwo);
        viewStoreDetailTimeEventThree = findViewById(R.id.viewStoreDetailTimeEventThree);
        viewStoreDetailTimeEventFour = findViewById(R.id.viewStoreDetailTimeEventFour);

        List<CommonTimeEventView> commonTimeEventViewList = new ArrayList<>();
        commonTimeEventViewList.add(viewStoreDetailTimeEventOne);
        commonTimeEventViewList.add(viewStoreDetailTimeEventTwo);
        commonTimeEventViewList.add(viewStoreDetailTimeEventThree);
        commonTimeEventViewList.add(viewStoreDetailTimeEventFour);

        new TimeEventHelper.TimeEventBuilder(commonTimeEventViewList)
                .setFixedAmount(storeInfoModel.timeEventModel.fixAmt)
                .setFixedRate(storeInfoModel.timeEventModel.fixRate)
                .setSaveRate(Integer.valueOf(storeInfoModel.saveRate))
                .setAddSaveRate(storeInfoModel.timeEventModel.addSaveRate)
                .build();

        deliveryTitle = storeInfoModel.storeName;

        ImageView lvBestStoreIc = findViewById(R.id.lvBestStoreIc);
        viewVisibility(lvBestStoreIc, storeInfoModel.isBestStore);


        cbBookMark = findViewById(R.id.cbBookMark);
        if (storeInfoModel.isBookMarkCheck.equals(EatOutConstants.BOOK_MARK_TYPE.Y.getType()))
            cbBookMark.setChecked(true);
        else {
            cbBookMark.setChecked(false);
        }
        cbBookMark.setOnClickListener(v -> bookmarkUpdate(cbBookMark.isChecked()));

        View viewShareGroup = findViewById(R.id.viewShareGroup);
        viewShareGroup.setOnClickListener(v -> {
            String titleName = storeInfoModel.storeName;
            String imageUrl = StringUtil.getImageUrl(BuildConfig.FILE_SERVER_BASE_URL + "/orgin/templatImg/201904/orgin_templatImg_201904_1720010230093.png?d=1020x490");
            String address = storeInfoModel.storeInfo;

            HashMap<String, String> executionParamsMap = new HashMap<>();
            executionParamsMap.put(MoaConstants.PARAM_KAKAO_LINK_ACTION_VIEW, MoaConstants.SCHEME_ACTION_STORE_DEATIL);
            executionParamsMap.put(MoaConstants.PARAM_KAKAO_LINK_STORE_ID, String.valueOf(storeId));
            executionParamsMap.put(MoaConstants.PARAM_KAKAO_LINK_URL, "");

            KakaoApiManager.getInstance().kakakLink(getApplicationContext(), executionParamsMap, titleName, imageUrl, address);
        });

        RatingBar materialRatingBar = findViewById(R.id.rbRatingPoint);

        materialRatingBar.setRating(storeInfoModel.ratingPoint);
        viewVisibility(R.id.takeOut, storeInfoModel.isTakeOut);
        viewVisibility(R.id.nowPay, storeInfoModel.isOnlinePay);
        viewVisibility(R.id.meetPay, storeInfoModel.isOfflinePay);

        phoneNumber = storeInfoModel.storeCallNumber;
        View viewStoreCall = findViewById(R.id.viewCallGroup);
        viewStoreCall.setOnClickListener(v -> phoneCall());

        Button btnStoreDetailCallOrder = findViewById(R.id.btnStoreDetailCallOrder);
        btnStoreDetailCallOrder.setOnClickListener(v -> phoneCall());

        // 가맹점이 열렸을 시
        final int OPEN_STORE = 1;
        if (storeInfoModel.isOpen == OPEN_STORE) {
            btnStoreDetailCallOrder.setVisibility(View.GONE);
            btnShoppingCart.setVisibility(View.VISIBLE);
        } else {
            btnStoreDetailCallOrder.setVisibility(View.VISIBLE);
            btnShoppingCart.setVisibility(View.GONE);
        }

    }

    /**
     * 뷰 보이고 수기기
     *
     * @param view      숨길 뷰
     * @param checkData 숨길때 체크할 데이터
     */
    private void viewVisibility(View view, String checkData) {
        final String SUCCESS = "Y";
        view.setVisibility(checkData != null && checkData.equals(SUCCESS) ? View.VISIBLE : View.GONE);
    }

    /**
     * 뷰 보이고 숨기기
     *
     * @param viewId    view id
     * @param checkData 숨길때 체크할 데이터
     */
    private void viewVisibility(int viewId, String checkData) {
        View view = findViewById(viewId);
        viewVisibility(view, checkData);
    }

    private void bookmarkUpdate(boolean isAddBookmark) {
        isStoreInfoChanged = true;
        if (isAddBookmark)
            storeDetailServerController.addBookmark(reqStoreInfoModel);
        else
            storeDetailServerController.removeBookmark(reqStoreInfoModel);
    }

    private void bookmarkUiUpdate(StoreInfoModel storeInfoModel) {
        if (storeInfoModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
            viewDataInitHelper.textViewInit(getWindow().getDecorView(), R.id.tvBookMarkCnt, storeInfoModel.bookmarkCnt);
        } else {
            cbBookMark.setChecked(!cbBookMark.isChecked());
        }
    }

    private void storeCategoryInit() {
        HashMap<String, Integer> categoryIcInfo = new HashMap<>();
        categoryIcInfo.put("1", R.drawable.sub_menu_korean_food_ic);
        categoryIcInfo.put("1024", R.drawable.sub_menu_franchise_ic);
        categoryIcInfo.put("128", R.drawable.sub_menu_instant_food_ic);
        categoryIcInfo.put("16", R.drawable.sub_menu_chicken_ic);
        categoryIcInfo.put("2", R.drawable.sub_menu_chinesefood_ic);
        categoryIcInfo.put("256", R.drawable.sub_menu_fastfood_ic);
        categoryIcInfo.put("32", R.drawable.sub_menu_pork_ic);
        categoryIcInfo.put("4", R.drawable.sub_menu_jp_ic);
        categoryIcInfo.put("512", R.drawable.sub_menu_dessert_ic);
        categoryIcInfo.put("64", R.drawable.sub_menu_late_night_ic);
        categoryIcInfo.put("8", R.drawable.sub_menu_western_ic);

        HashMap<String, Integer> categoryBgInfo = new HashMap<>();
        categoryBgInfo.put("1", R.drawable.bg_koreanfood);
        categoryBgInfo.put("1024", R.drawable.sub_menu_franchise_ic);
        categoryBgInfo.put("128", R.drawable.bg_flourfood);
        categoryBgInfo.put("16", R.drawable.chicken);
        categoryBgInfo.put("2", R.drawable.bg_chinesefood);
        categoryBgInfo.put("256", R.drawable.bg_fastfood);
        categoryBgInfo.put("32", R.drawable.bg_pork);
        categoryBgInfo.put("4", R.drawable.bg_sushi);
        categoryBgInfo.put("512", R.drawable.bg_dessert);
        categoryBgInfo.put("64", R.drawable.bg_late_night_meal);
        categoryBgInfo.put("8", R.drawable.bg_pizza);

        ImageView storeThumbNail = findViewById(R.id.storeThumbNail);
        ImageView ivStoreThumbnail = findViewById(R.id.ivStoreThumbnail);
        final String SUB_MENU_CODE = App.getInstance().SUB_MENU_CODE;
        imageChange(categoryIcInfo.get(SUB_MENU_CODE), storeThumbNail);
        imageChange(categoryBgInfo.get(SUB_MENU_CODE), ivStoreThumbnail);

        viewStoreDetailLoading.animationStop(this);
        isLoading = false;
    }

    private void imageChange(int img, ImageView view) {

        view.post(new Runnable() {
            @Override
            public void run() {
                mGlideRequestManager.load(img)
                        .into(view);
            }
        });
//        Glide.with(this)
//                .load(img)
//                .into(view);
    }

    @Override
    public void onHttpConnectionSuccess(int type, BaseModel baseModel) {
        if (!isFinishing()) {
            if (type == CodeTypeManager.StoreInfo.STORE_ADD_BOOKMARK.getType() || type == CodeTypeManager.StoreInfo.STORE_REMOVE_BOOKMARK.getType()) {
                bookmarkUiUpdate((StoreInfoModel) baseModel);
            } else if (type == CodeTypeManager.ShoppingCart.SHOPPING_CART_COUNT.getType()) {
                btnShoppingCart.setText(getString(R.string.store_product_shopping_cart, String.valueOf(((ResStoreDetailInfo.ShoppingCartCountModel) baseModel).count)));
            } else {
                ResStoreDetailInfo storeDetailModel = ((ResStoreDetailInfo) baseModel);
                App.getInstance().SUB_MENU_CODE = storeDetailModel.storeInfoModel.subMenuCode;
                Logger.i("가맹점 정보 : " + new Gson().toJson(storeDetailModel.storeInfoModel));
                Logger.i("전체 메뉴 : " + new Gson().toJson(storeDetailModel.storeAllMenuList));
                storeDefaultInfoInit(storeDetailModel.storeInfoModel);
                ((StoreGridListFragment) storeDetailViewPagerAdapter.getItem(0)).listUpdate(storeDetailModel.storeRepresentativeMenuList, storeDetailModel.storeAllMenuList == null ? new ArrayList<>() : storeDetailModel.storeAllMenuList);
                storeCategoryInit();
                if (storeDetailModel.storeInfoModel.isTakeOut.equals("N")) {
                    ((StoreAdditionalInformationFragment) storeDetailViewPagerAdapter.getItem(1)).addressMapHide();
                }
            }
        }
    }

    @Override
    public void resultCode(int code) {
        if (code == CodeTypeManager.TopToolbarManager.BACK_BUTTON_PRESS.getType()) {
            if (isStoreInfoChanged) {
                TextView tvReviewCnt = findViewById(R.id.tvReviewCnt);
                TextView tvBookmarkCnt = findViewById(R.id.tvBookMarkCnt);
                Intent intent = new Intent();
                //변경된 항목이 있을시에만 담는다.
                storeInfoChangedModel.fovoriteCount = tvBookmarkCnt.getText().toString();
                storeInfoChangedModel.reviewCount = tvReviewCnt.getText().toString();
                intent.putExtra(MoaConstants.EXTRA_STORE_INFO_CHANGED_DATA, new Gson().toJson(storeInfoChangedModel, StoreInfoChangedModel.class));
                setResult(MoaConstants.RESULT_STORE_DETAIL_INFO_CHANGED, intent);
            }
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isShoppingCartCountRefresh = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isShoppingCartCountRefresh)
            storeDetailServerController.shoppingCartCountCheck();
    }

    @Override
    public void onBackPressed() {
        if (isLoading) {
            return;
        }

        if (isStoreInfoChanged) {
            TextView tvReviewCnt = findViewById(R.id.tvReviewCnt);
            TextView tvBookmarkCnt = findViewById(R.id.tvBookMarkCnt);
            Intent intent = new Intent();
            //변경된 항목이 있을시에만 담는다.
            storeInfoChangedModel.fovoriteCount = tvBookmarkCnt.getText().toString();
            storeInfoChangedModel.reviewCount = tvReviewCnt.getText().toString();
            intent.putExtra(MoaConstants.EXTRA_STORE_INFO_CHANGED_DATA, new Gson().toJson(storeInfoChangedModel, StoreInfoChangedModel.class));
            setResult(MoaConstants.RESULT_STORE_DETAIL_INFO_CHANGED, intent);
        }

        super.onBackPressed();
    }

    public void setStoreInfoChanged(boolean storeInfoChanged) {
        isStoreInfoChanged = storeInfoChanged;
    }
}
