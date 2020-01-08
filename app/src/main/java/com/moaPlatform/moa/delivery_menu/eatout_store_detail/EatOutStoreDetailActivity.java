package com.moaPlatform.moa.delivery_menu.eatout_store_detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.activity.ReviewWriteMainActivity;
import com.moaPlatform.moa.constants.EatOutConstants;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.adapter.EatOutStoreDetailImageHorizontalAdapter;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.EatOutStoreInfoModel;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.EatOutStoreProductImageModel;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.EatOutStoreProductTextModel;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.ResEatOutStoreDetailInfo;
import com.moaPlatform.moa.model.data_model.StoreInfoChangedModel;
import com.moaPlatform.moa.model.req_model.ReqStoreInfoModel;
import com.moaPlatform.moa.model.res_model.ReviewModel;
import com.moaPlatform.moa.util.DeviceUtil;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.custom_view.CommonReviewItemView;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.interfaces.ViewDataInitHelper;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.manager.KakaoApiManager;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.MoneyConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.moaPlatform.moa.constants.MoaConstants.EXTRA_STORE_ID;
import static com.moaPlatform.moa.constants.MoaConstants.EXTRA_TITLE_NAME;
import static com.moaPlatform.moa.delivery_menu.eatout_store_detail.EatOutStoreDaumMapActivity.EXTRA_EATOUT_STORE_DAUM_MAP;

/**
 * [정의]
 * 외식 매장 상세 화면 Activity
 *
 * @author chan
 */
public class EatOutStoreDetailActivity extends AppCompatActivity implements ViewDataInitHelper, View.OnClickListener, LocationListener, MoneyConverter, RetrofitConnectionResult {

    private int requestStoreId;
    private String daumMapMoveType;

    private ReqStoreInfoModel reqStoreInfoModel;
    private ResEatOutStoreDetailInfo resEatOutStoreDetailInfo;      //Response Data
    private ViewDataInitHelper viewDataInitHelper = this;

    private LocationManager locationManager;
    private Location location;

    private CheckBox cbBookMark;
    private LottieAnimationView lottieEatOutStoreDetailLoading;
    private RecyclerView recyclerImageList;
    private EatOutStoreDetailImageHorizontalAdapter adapter;
    private TextView tvEatOutStoreDetailReviewCnt;          //리뷰 cnt
    private TextView tvEatOutStoreDetailReviewBookMarkCnt;      //즐겨찾기 cnt

    private EatOutStoreDetailServerController serverController;
    private RelativeLayout rlEatOutStoreDetailReviewEmpty;

    private StoreInfoChangedModel storeInfoChangedModel;        //변경된 데이터 정보를 담는다.
    private boolean isStoreInfoChanged = false;                 //변경된 데이터 정보가 있는지 체크 (북마크, 리뷰 cnt 변경되었는지만 체크)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_out_store_detail);

        initDefaultData();
        initLayout();
        initAdapter();

        getStoreInfo(reqStoreInfoModel);
    }

    private void initDefaultData() {
        requestStoreId = getIntent().getIntExtra(MoaConstants.EXTRA_STORE_ID, 0);
        reqStoreInfoModel = new ReqStoreInfoModel();
        reqStoreInfoModel.storeId = requestStoreId;

        storeInfoChangedModel = new StoreInfoChangedModel();
        storeInfoChangedModel.position = getIntent().getIntExtra(MoaConstants.EXTRA_STORE_LIST_POSITION, 0);
    }

    private void initLayout() {
        RelativeLayout rlEatOutStoreTitleBack = findViewById(R.id.rlEatOutStoreTitleBack);
        rlEatOutStoreTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStoreInfoChanged){
                    Intent intent = new Intent();
                    //변경된 항목이 있을시에만 담는다.
                    storeInfoChangedModel.fovoriteCount = tvEatOutStoreDetailReviewBookMarkCnt.getText().toString();
                    storeInfoChangedModel.reviewCount = tvEatOutStoreDetailReviewCnt.getText().toString();
                    intent.putExtra(MoaConstants.EXTRA_STORE_INFO_CHANGED_DATA, new Gson().toJson(storeInfoChangedModel, StoreInfoChangedModel.class));
                    setResult(MoaConstants.RESULT_STORE_DETAIL_INFO_CHANGED, intent);
                }
                finish();
            }
        });

        tvEatOutStoreDetailReviewCnt = findViewById(R.id.tvEatOutStoreDetailReviewCnt);
        tvEatOutStoreDetailReviewBookMarkCnt = findViewById(R.id.tvEatOutStoreDetailReviewBookMarkCnt);

        lottieEatOutStoreDetailLoading = findViewById(R.id.lottieEatOutStoreDetailLoading);
        String[] animationList = {"loading_bread", "loading_cook_steak", "loading_soybean"};
        Random generator = new Random();
        int randomNumber = generator.nextInt(animationList.length);
        lottieEatOutStoreDetailLoading.setImageAssetsFolder(animationList[randomNumber]);
        lottieEatOutStoreDetailLoading.setAnimation(animationList[randomNumber] + ".json");
        dismissProgress();

        recyclerImageList = findViewById(R.id.recyclerEatOutStoreDetailImageList);

        rlEatOutStoreDetailReviewEmpty = findViewById(R.id.rlEatOutStoreDetailReviewEmpty);
        rlEatOutStoreDetailReviewEmpty.setVisibility(View.GONE);

    }

    private void initAdapter() {
        adapter = new EatOutStoreDetailImageHorizontalAdapter(this, null);
        recyclerImageList.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        recyclerImageList.setAdapter(adapter);
    }

    /**
     * 가맹점 부가 정보 받아오기
     */
    private void getStoreInfo(ReqStoreInfoModel reqModel) {

        showProgress();

        serverController = new EatOutStoreDetailServerController(this);
        serverController.setRetrofitConnectionResult(this);
        serverController.getEatOutStoreDetailInfo(reqStoreInfoModel);
    }

    @Override
    public void onRetrofitSuccess(int type, BaseModel baseModel) {
        dismissProgress();

        Logger.d("onRetrofitSuccess >>> " + type);

        if (!isFinishing()) {
            if (type == CodeTypeManager.StoreInfo.STORE_ADD_BOOKMARK.getType() || type == CodeTypeManager.StoreInfo.STORE_REMOVE_BOOKMARK.getType()) {
                bookmarkUiUpdate((EatOutStoreInfoModel) baseModel);
            } else {
                resEatOutStoreDetailInfo = (ResEatOutStoreDetailInfo) baseModel;
                categoryUpdate(resEatOutStoreDetailInfo.eatOutStoreInfoModel);
                if (resEatOutStoreDetailInfo != null) {
                    storeDefaultInfoInit(resEatOutStoreDetailInfo);
                }
            }
        }
    }

    @Override
    public void onRetrofitFail(int type, String msg) {
        dismissProgress();
    }


    /**
     * 가맹점 기본 정보 세팅
     */
    private void storeDefaultInfoInit(ResEatOutStoreDetailInfo model) {

        List<EatOutStoreProductImageModel> imageList = model.imageList;
        EatOutStoreInfoModel eatOutStoreInfoModel = model.eatOutStoreInfoModel;
        List<EatOutStoreProductTextModel> productTextList = model.productTextList;
        List<ReviewModel> reviewModel = resEatOutStoreDetailInfo.reviewModel;

        View view = getWindow().getDecorView();

        if (eatOutStoreInfoModel != null) {

            viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailStoreName, eatOutStoreInfoModel.storNm);
            viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailReviewCnt, eatOutStoreInfoModel.storRevwCnt);
            viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailReviewBookMarkCnt, eatOutStoreInfoModel.bmarkCnt);
            viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailVisitCnt, eatOutStoreInfoModel.lookUpCnt);
            viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailRatingPoint, eatOutStoreInfoModel.evalScor);

            cbBookMark = findViewById(R.id.cbEatOutStoreDetailBookMark);
            if (eatOutStoreInfoModel.userBmarkYn.equals(EatOutConstants.BOOK_MARK_TYPE.Y.getType())) {
                cbBookMark.setChecked(true);
            }
            cbBookMark.setOnClickListener(v -> bookmarkUpdate(cbBookMark.isChecked()));

            RatingBar materialRatingBar = findViewById(R.id.ratingBarEatOutStoreDetailStar);
            materialRatingBar.setRating(Float.parseFloat(eatOutStoreInfoModel.evalScor));

            viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailDesc, eatOutStoreInfoModel.eatOutStorIntro);
            viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailAddr, eatOutStoreInfoModel.roadNmDefltAddr);

            ImageView ivEatOutStoreDetailMapImage = findViewById(R.id.ivEatOutStoreDetailMapImage);
            float imageWidth = DeviceUtil.getScreenWidth(this);
            float imageHeight = (float) (imageWidth / 2.7);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivEatOutStoreDetailMapImage.getLayoutParams();
            layoutParams.width = (int) imageWidth;
            layoutParams.height = (int) imageHeight;
            Glide.with(this)
                    .load(StringUtil.getImageUrl(eatOutStoreInfoModel.mapImg))
                    .thumbnail(1.0f)
                    //.apply(new RequestOptions().centerCrop())
                    .into(ivEatOutStoreDetailMapImage);

            ivEatOutStoreDetailMapImage.setOnClickListener(this);

            if(ObjectUtil.checkNotNull(eatOutStoreInfoModel.wkdyOprtnStartTime) && ObjectUtil.checkNotNull(eatOutStoreInfoModel.wkdyOprtnEndTime)){
                StringBuilder hourStringBuilder = new StringBuilder();
                hourStringBuilder.append(String.format(getString(R.string.store_business_hours),
                        eatOutStoreInfoModel.wkdyOprtnStartTime.substring(0, 2),
                        eatOutStoreInfoModel.wkdyOprtnStartTime.substring(2, 4),
                        eatOutStoreInfoModel.wkdyOprtnEndTime.substring(0, 2),
                        eatOutStoreInfoModel.wkdyOprtnEndTime.substring(2, 4)

                ));
                viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailBusinessHourContent,
                        getString(R.string.eatout_store_detail_business_time_header) + " " + hourStringBuilder.toString());
            }else{
                viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailBusinessHourContent,
                        getString(R.string.eatout_store_detail_common_empty_info));

            }

            if(ObjectUtil.checkNotNull(eatOutStoreInfoModel.lastOrderTime)){
                viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailLastOrderContent, eatOutStoreInfoModel.lastOrderTime);
            }else{
                viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailLastOrderContent, getString(R.string.eatout_store_detail_common_empty_info));
            }

            if(ObjectUtil.checkNotNull(eatOutStoreInfoModel.hldy)){
                viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailClosingDayContent, eatOutStoreInfoModel.hldy);
            }else{
                viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailClosingDayContent, getString(R.string.eatout_store_detail_common_empty_info));
            }

            if(ObjectUtil.checkNotNull(eatOutStoreInfoModel.storPhonNum)){
                viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailPhoneNumberContent, PhoneNumberUtils.formatNumber(eatOutStoreInfoModel.storPhonNum));
            }else{
                viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailPhoneNumberContent, getString(R.string.eatout_store_detail_common_empty_info));
            }

            if(ObjectUtil.checkNotNull(eatOutStoreInfoModel.notiCntnt)){
                viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailStoreNoticeContent, eatOutStoreInfoModel.notiCntnt);
            }else{
                //매장 알림 내용이 없을경우
                String noticeMessage = String.format(getString(R.string.eatout_store_detail_store_notice_content), eatOutStoreInfoModel.storNm);
                viewDataInitHelper.textViewInit(view, R.id.tvEatOutStoreDetailStoreNoticeContent, noticeMessage);
            }
        }

        if (imageList != null && imageList.size() > 0) {
            adapter.setImageList(imageList);
            adapter.notifyDataSetChanged();
        }else{
            //상품 이미지 리스트가 없을시에는 숨김 처리
            recyclerImageList.setVisibility(View.GONE);
        }

        TextView tvEatOutStoreDetailTitleMenuMore = findViewById(R.id.tvEatOutStoreDetailTitleMenuMore);        //메뉴 더 보기 버튼
        TextView tvEatOutStoreDetailEmptyMenuList = findViewById(R.id.tvEatOutStoreDetailEmptyMenuList);

        //대표메뉴 상품 리스트를 담는다.
        if (productTextList != null && productTextList.size() > 0) {


            if (productTextList.size() > 0) {
                LinearLayout llEatOutStoreDetailTitleMenuList = findViewById(R.id.llEatOutStoreDetailTitleMenuList);
                llEatOutStoreDetailTitleMenuList.removeAllViews();

                for (EatOutStoreProductTextModel eatOutStoreProductTextModel : productTextList) {
                    ConstraintLayout productItemView = (ConstraintLayout) getLayoutInflater().inflate(R.layout.item_eat_out_store_detail_product, null, false);
                    viewDataInitHelper.textViewInit(productItemView, R.id.tvEatOutStoreDetailProductName, eatOutStoreProductTextModel.storProdNm);
                    viewDataInitHelper.textViewInit(productItemView, R.id.tvEatOutStoreDetailProductPrice, commaUnitChange(eatOutStoreProductTextModel.storProdPrice) + "원");
                    llEatOutStoreDetailTitleMenuList.addView(productItemView);
                }
                tvEatOutStoreDetailTitleMenuMore.setVisibility(View.VISIBLE);
                tvEatOutStoreDetailEmptyMenuList.setVisibility(View.GONE);
            }
        }else{
            tvEatOutStoreDetailTitleMenuMore.setVisibility(View.GONE);
            tvEatOutStoreDetailEmptyMenuList.setVisibility(View.VISIBLE);
        }

        view.findViewById(R.id.rlEatOutStoreDetailCall).setOnClickListener(this);
        view.findViewById(R.id.rlEatOutStoreDetailBookMark).setOnClickListener(this);
        view.findViewById(R.id.rlEatOutStoreDetailReviewWrite).setOnClickListener(this);
        view.findViewById(R.id.rlEatOutStoreDetailShare).setOnClickListener(this);

        view.findViewById(R.id.rlEatOutStoreDetailSearchWalk).setOnClickListener(this);
        view.findViewById(R.id.rlEatOutStoreDetailSearchNavi).setOnClickListener(this);
        view.findViewById(R.id.rlEatOutStoreDetailSearchCopyAddress).setOnClickListener(this);

        view.findViewById(R.id.tvEatOutStoreDetailTitleMenuMore).setOnClickListener(this);
        view.findViewById(R.id.tvEatOutStoreDetailReviewMore).setOnClickListener(this);

        initReviewList(reviewModel);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rlEatOutStoreDetailCall:
                //전화하기
                actionCall(resEatOutStoreDetailInfo.eatOutStoreInfoModel.storPhonNum);
                break;
            case R.id.rlEatOutStoreDetailBookMark:
                //즐겨찾기
                break;
            case R.id.rlEatOutStoreDetailReviewWrite:
                //리뷰 작성
                Intent intentReviewWrite = new Intent(this, ReviewWriteMainActivity.class);
                intentReviewWrite.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentReviewWrite.putExtra(MoaConstants.EXTRA_FROM_VIEW, ReviewWriteMainActivity.FROM_VIEW_EATOUT_STORE_REVIEW);
                intentReviewWrite.putExtra(MoaConstants.EXTRA_STORE_ID, requestStoreId);
                intentReviewWrite.putExtra(MoaConstants.EXTRA_STORE_NAME, resEatOutStoreDetailInfo.eatOutStoreInfoModel.storNm);
                startActivityForResult(intentReviewWrite, MoaConstants.REQUEST_REVIEW_WRITE);
                break;
            case R.id.rlEatOutStoreDetailShare:
                //공유하기
                String titleName = resEatOutStoreDetailInfo.eatOutStoreInfoModel.storNm;
                String imageUrl= "";
                if (resEatOutStoreDetailInfo.imageList != null && resEatOutStoreDetailInfo.imageList.size() > 0 )
                    imageUrl = StringUtil.getImageUrl(resEatOutStoreDetailInfo.imageList.get(0).imageUrl);
                String address = resEatOutStoreDetailInfo.eatOutStoreInfoModel.roadNmDefltAddr;

                HashMap<String, String> executionParamsMap = new HashMap<>();
                executionParamsMap.put(MoaConstants.PARAM_KAKAO_LINK_ACTION_VIEW, MoaConstants.SCHEME_ACTION_EATOUT_DEATIL);
                executionParamsMap.put(MoaConstants.PARAM_KAKAO_LINK_STORE_ID, String.valueOf(requestStoreId));
                executionParamsMap.put(MoaConstants.PARAM_KAKAO_LINK_URL, "");

                KakaoApiManager.getInstance().kakakLink(this, executionParamsMap, titleName, imageUrl, address);
                break;
            case R.id.ivEatOutStoreDetailMapImage:
                //지도 보기
                Intent intentMap = new Intent(this, EatOutStoreDaumMapActivity.class);
                intentMap.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                EatOutStoreInfoModel eatOutStoreInfoModel = this.resEatOutStoreDetailInfo.eatOutStoreInfoModel;
                intentMap.putExtra(EXTRA_EATOUT_STORE_DAUM_MAP, new Gson().toJson(eatOutStoreInfoModel));
                intentMap.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMap);
                break;
            case R.id.rlEatOutStoreDetailSearchWalk:
                //길찾기
                daumMapMoveType = EatOutConstants.SEARCH_MAP_TYPE.FOOT.getType();
                locationPermissionCheck();
                break;
            case R.id.rlEatOutStoreDetailSearchNavi:
                //네비게이션
                daumMapMoveType = EatOutConstants.SEARCH_MAP_TYPE.CAR.getType();
                locationPermissionCheck();
                break;
            case R.id.rlEatOutStoreDetailSearchCopyAddress:
                //주소복사
                int sdk = android.os.Build.VERSION.SDK_INT;
                String addr = resEatOutStoreDetailInfo.eatOutStoreInfoModel.roadNmDefltAddr;

                if (addr != null) {
                    if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText(addr);
                    } else {
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clip = ClipData.newPlainText("address", addr);
                        clipboard.setPrimaryClip(clip);
                    }
                    Toast.makeText(this, getString(R.string.copy_clip_baord), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvEatOutStoreDetailTitleMenuMore:
                //대표메뉴 더보기
                Intent intentProductList = new Intent(this, EatOutStoreProductListActivity.class);
                intentProductList.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentProductList.putExtra(EXTRA_STORE_ID, requestStoreId);
                intentProductList.putExtra(MoaConstants.EXTRA_TITLE_NAME, resEatOutStoreDetailInfo.eatOutStoreInfoModel.storNm);
                intentProductList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentProductList);
                break;
            case R.id.tvEatOutStoreDetailReviewMore:
                //리뷰 더보기
                Intent intentReviewList = new Intent(this, EatOutStoreReviewActivity.class);
                intentReviewList.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentReviewList.putExtra(EXTRA_STORE_ID, requestStoreId);
                intentReviewList.putExtra(EXTRA_TITLE_NAME, resEatOutStoreDetailInfo.eatOutStoreInfoModel.storNm);
                startActivityForResult(intentReviewList, MoaConstants.REQUEST_REVIEW_LIST);
                break;
        }
    }


    /**
     * 리뷰 리스트를 그린다.
     * 최대 3개를 서버에서 내려주기로 함
     *
     * @param eatOutStoreReviewModel
     */
    private void initReviewList(List<ReviewModel> eatOutStoreReviewModel) {

        if (eatOutStoreReviewModel == null) {
            return;
        }
        LinearLayout reviewListContainer = findViewById(R.id.llEatOutStoreDetailReviewListContainer);

        if (eatOutStoreReviewModel.size() > 0) {

            reviewListContainer.removeAllViews();
            for (int i = 0; i < eatOutStoreReviewModel.size(); i++) {
                ReviewModel reviewModel = eatOutStoreReviewModel.get(i);

                CommonReviewItemView commonReviewItemView = new CommonReviewItemView(this);
                commonReviewItemView.setEatOutView(true);
                commonReviewItemView.setShowOwnerReview(false);
                if (i == eatOutStoreReviewModel.size() - 1) {
                    commonReviewItemView.setShowDivider(false);
                } else {
                    commonReviewItemView.setShowDivider(true);
                }

                commonReviewItemView.setInitData(reviewModel);
                commonReviewItemView.isShowOptionButton(false);
                reviewListContainer.addView(commonReviewItemView);
            }
            rlEatOutStoreDetailReviewEmpty.setVisibility(View.GONE);

        } else {
            rlEatOutStoreDetailReviewEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void bookmarkUpdate(boolean isAddBookmark) {

        if (serverController != null) {

            isStoreInfoChanged = true;

            if (isAddBookmark) {
                serverController.addBookmark(reqStoreInfoModel);
            } else {
                serverController.removeBookmark(reqStoreInfoModel);
            }
        }
    }

    private void bookmarkUiUpdate(EatOutStoreInfoModel storeInfoModel) {
        if (storeInfoModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
            viewDataInitHelper.textViewInit(getWindow().getDecorView(), R.id.tvEatOutStoreDetailReviewBookMarkCnt, storeInfoModel.bmarkCnt);
        } else {
            cbBookMark.setChecked(!cbBookMark.isChecked());
        }
    }

    private void categoryUpdate(EatOutStoreInfoModel storeInfoModel) {
        HashMap<String, Integer> categoryIcInfo = new HashMap<>();
        categoryIcInfo.put("1", R.drawable.sub_menu_korean_food_ic);
        categoryIcInfo.put("2", R.drawable.sub_menu_chinesefood_ic);
        categoryIcInfo.put("4", R.drawable.sub_menu_jp_ic);
        categoryIcInfo.put("64", R.drawable.sub_menu_dessert_ic);
        categoryIcInfo.put("8", R.drawable.sub_menu_word_ic);
        categoryIcInfo.put("32", R.drawable.sub_menu_buffet_ic);
        categoryIcInfo.put("128", R.drawable.sub_menu_pub_ic);
        categoryIcInfo.put("16", R.drawable.sub_menu_restaurant_ic);


        HashMap<String, Integer> categoryBgInfo = new HashMap<>();
        categoryBgInfo.put("1", R.drawable.bg_koreanfood);
        categoryBgInfo.put("2", R.drawable.bg_chinesefood);
        categoryBgInfo.put("4", R.drawable.bg_sushi);
        categoryBgInfo.put("64", R.drawable.bg_dessert);
        categoryBgInfo.put("8", R.drawable.bg_taco);
        categoryBgInfo.put("32", R.drawable.bg_buffet);
        categoryBgInfo.put("128", R.drawable.bg_pub);
        categoryBgInfo.put("16", R.drawable.bg_restaurant);

        ImageView ivEatOutStoreDeatilThumb = findViewById(R.id.ivEatOutStoreDeatilThumb);
        ImageView ivEatOutStoreDetailTopInfoBg = findViewById(R.id.ivEatOutStoreDetailTopInfoBg);

        try {
            if (storeInfoModel != null) {
                if (storeInfoModel != null) {
                    imgGlide(categoryIcInfo.get(storeInfoModel.eatOutSubMenuCd), ivEatOutStoreDeatilThumb);
                    imgGlide(categoryBgInfo.get(storeInfoModel.eatOutSubMenuCd), ivEatOutStoreDetailTopInfoBg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void imgGlide(int src, ImageView imageView) {
        Glide.with(this)
                .load(src)
                .into(imageView);
    }

    /**
     * 전화걸기
     *
     * @param number
     */
    private void actionCall(String number) {

        TedPermission.with(this).setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                if (number != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);     //ACTION_CALL 전화 바로 걸기 | ACTION_DIAL 전화 화면으로 이동
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.setData(Uri.parse("tel:" + number));
                    startActivity(intent);
                }
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        }).setDeniedMessage(R.string.call_permission_denied)
                .setPermissions(Manifest.permission.CALL_PHONE).check();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.d("requestCode " + requestCode + " resultCode " + resultCode);

        if (requestCode == MoaConstants.REQUEST_REVIEW_WRITE) {
            if (resultCode == MoaConstants.RESULT_REVIEW_WRITE_SUCCESS) {
                isStoreInfoChanged = true;
                getStoreInfo(reqStoreInfoModel);
            }
        }else if(requestCode == MoaConstants.REQUEST_REVIEW_LIST){
            if (resultCode == MoaConstants.RESULT_REVIEW_LIST_NOTIFYCHANGED) {
                isStoreInfoChanged = true;
                getStoreInfo(reqStoreInfoModel);
            }
        }
    }

    private void showProgress() {
        if (!isFinishing()) {
            if (lottieEatOutStoreDetailLoading != null) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                lottieEatOutStoreDetailLoading.setVisibility(View.VISIBLE);
                lottieEatOutStoreDetailLoading.playAnimation();
            }
        }
    }

    private void dismissProgress() {
        if (!isFinishing()) {
            if (lottieEatOutStoreDetailLoading != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        lottieEatOutStoreDetailLoading.setVisibility(View.GONE);
                        lottieEatOutStoreDetailLoading.cancelAnimation();
                    }
                }, MoaConstants.LOADING_BAR_VISIBLE_TIME);
            }
        }
    }

    /**
     * 위치 퍼미션 체크
     */
    private void locationPermissionCheck() {
        TedPermission.with(this).setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                nowLocationSearchInit();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        }).setDeniedMessage(R.string.location_permission_denied)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).check();
    }

    /**
     * 현재 위치를 가져온다.
     */
    @SuppressLint("MissingPermission")
    private void nowLocationSearchInit() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if ((locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, this);
        } else {
            YesOrNoDialog gpsOnDialog = new YesOrNoDialog();
            gpsOnDialog.dialogContent("현재 위치 검색을 위해 위치 설정 기능이 필요합니다.");
            gpsOnDialog.yesTextChange("설정하러 가기");
            gpsOnDialog.showAllowingStateLoss(getSupportFragmentManager(), "gpsOnDialog");
            gpsOnDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
                @Override
                public void onDialogNo() {
                    gpsOnDialog.dismiss();
                }

                @Override
                public void onDialogYes() {
                    gpsOnDialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onStop() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {

        this.location = location;
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        KakaoApiManager.getInstance().showDaumMap(this, daumMapMoveType, location.getLatitude(), location.getLongitude()
                , resEatOutStoreDetailInfo.eatOutStoreInfoModel.latu, resEatOutStoreDetailInfo.eatOutStoreInfoModel.lont);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (!isFinishing()) {
            // 위치 정보 가져왔을시 network 문제시에는 에러 토스를 띄우고 리스너 연결을 끊음
            if (provider.equals("network")) {
                Toast.makeText(getApplicationContext(), "연결 상태가 일시적으로 불안합니다. 다시 시도해 주세요 : " + provider, Toast.LENGTH_LONG).show();
                locationManager.removeUpdates(this);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(isStoreInfoChanged){
            Intent intent = new Intent();
            //변경된 항목이 있을시에만 담는다.
            storeInfoChangedModel.fovoriteCount = tvEatOutStoreDetailReviewBookMarkCnt.getText().toString();
            storeInfoChangedModel.reviewCount = tvEatOutStoreDetailReviewCnt.getText().toString();
            intent.putExtra(MoaConstants.EXTRA_STORE_INFO_CHANGED_DATA, new Gson().toJson(storeInfoChangedModel));
            setResult(MoaConstants.RESULT_STORE_DETAIL_INFO_CHANGED, intent);
        }
        super.onBackPressed();
    }
}
