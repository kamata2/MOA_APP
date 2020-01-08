package com.moaPlatform.moa.bottom_menu.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.activity.EventWebViewActivity;
import com.moaPlatform.moa.bottom_menu.main.adapter.SubMenuEatOutStoreAdapter;
import com.moaPlatform.moa.bottom_menu.main.adapter.SubMenuEatOutThemeAdapter;
import com.moaPlatform.moa.bottom_menu.main.model.EatOutBannerModel;
import com.moaPlatform.moa.bottom_menu.main.model.EatOutMainModel;
import com.moaPlatform.moa.bottom_menu.main.model.EatOutStoreModel;
import com.moaPlatform.moa.bottom_menu.main.model.EatOutThemeModel;
import com.moaPlatform.moa.bottom_menu.main.model.list.EatOutShopList;
import com.moaPlatform.moa.bottom_menu.main.model.list.EatOutThemeList;
import com.moaPlatform.moa.constants.EatOutConstants;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.EatOutStoreDetailActivity;
import com.moaPlatform.moa.delivery_menu.eatout_store_list.EatOutStoreListActivity;
import com.moaPlatform.moa.model.data_model.StoreInfoChangedModel;
import com.moaPlatform.moa.top_menu.search.StoreSearchActivity;
import com.moaPlatform.moa.util.DeviceUtil;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * [정의]
 * 메인메뉴 [외식]에 해당하는 하위 화면
 * <p>
 * [구성]
 * 외식메뉴 서브메뉴(차후 적용 예정)
 * - 외식 서브카테고리 ex..배달 > 한식/프렌차이즈/분식/치킨 등..
 * <p>
 * 광고리스트
 * - 광고타입에는 A(배너) 광고타입 B(매장리스트) 광고타입 C(테마리스트)
 *
 * @author chan
 */
public class MainSubMenuEatOutFragment extends Fragment implements ListItemClickListener, View.OnClickListener {

    public static final String ARG_SUB_MENU_EAT_OUT_DATA = "ARG_SUB_MENU_EAT_OUT_DATA";

    private SubMenuEatOutStoreAdapter storeType2Adapter;
    private SubMenuEatOutThemeAdapter themeType3Adapter;

    /**
     * Container[Start]
     * 각 항목에 따른 parent layout
     * 용도 : 데이터 없을시 숨김처리를 위함
     */
    @BindView(R.id.constrainSubMenuEatOutMenuType1)
    ConstraintLayout containerType1;
    @BindView(R.id.constrainSubMenuEatOutMenuType2)
    ConstraintLayout containerType2;
    @BindView(R.id.constrainSubMenuEatOutMenuType3)
    ConstraintLayout containerType3;
    @BindView(R.id.constraintSubMenuEatOutMenuTypeBannerTwo)
    ConstraintLayout constraintSubMenuEatOutMenuTypeBannerTwo;         //두번째 배너

    /**
     * Container[End]
     * 각 항목에 따른 parent layout
     * 용도 : 데이터 없을시 숨김처리를 위함
     */

    @BindView(R.id.constrainSubMenuEatOutSearchContainer)   //검색 버튼
    ConstraintLayout constSearchContainer;
    @BindView(R.id.ivSubMenuEatOutMenuType1Banner)          //배너 이미지
    ImageView ivBanner;
    @BindView(R.id.tvSubMenuEatOutMenuType1BannerDesc)      //배너 대표 문구
    TextView tvBannerDesc;
    @BindView(R.id.tvSubMenuEatOutMenuType1BannerDescSub)   //배너 서브 문구
    TextView tvBannerDescSub;
    @BindView(R.id.tvSubMenuEatOutMenuType2Title)           //상점 타이틀
    TextView tvType2Title;
    @BindView(R.id.llSubMenuEatOutMenuType2TitleMore)       //상점 타이틀 더보기
    LinearLayout llType2TitleMore;
    @BindView(R.id.recyclerSubMenuEatOutMenuType2List)
    RecyclerView recyclerViewType2;
    @BindView(R.id.tvSubMenuEatOutMenuType3Title)           //테마 타이틀
    TextView tvType3Title;
    @BindView(R.id.recyclerSubMenuEatOutMenuType3List)
    RecyclerView recyclerViewType3;

    @BindView(R.id.ivSubMenuEatOutMenuTypeBannerTwo)        //두번째 가로형 배너
    ImageView ivSubMenuEatOutMenuTypeBannerTwo;
    @BindView(R.id.tvSubMenuEatOutMenuTypeBannerTwoDesc)
    TextView tvSubMenuEatOutMenuTypeBannerTwoDesc;
    @BindView(R.id.tvSubMenuEatOutMenuTypeBannerTwoDescSub)
    TextView tvSubMenuEatOutMenuTypeBannerTwoDescSub;

    //Data
    private EatOutMainModel eatOutMainModel;

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eatOutMainModel = new Gson().fromJson(
                    getArguments().getString(ARG_SUB_MENU_EAT_OUT_DATA), EatOutMainModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sub_menu_eat_out, container, false);
        unbinder = ButterKnife.bind(this, view);

        initAdapter();
        initListener();
        initData();

        return view;
    }

    private void initListener() {
        constSearchContainer.setOnClickListener(this);
        ivBanner.setOnClickListener(this);
        llType2TitleMore.setOnClickListener(this);
        ivSubMenuEatOutMenuTypeBannerTwo.setOnClickListener(this);
    }

    private void initAdapter() {

        GridLayoutManager storeManager = new GridLayoutManager(getContext(), 2);
        GridLayoutManager themeManager = new GridLayoutManager(getContext(), 2);

        recyclerViewType2.setLayoutManager(storeManager);
        recyclerViewType3.setLayoutManager(themeManager);

        storeType2Adapter = new SubMenuEatOutStoreAdapter(getContext(), this);
        themeType3Adapter = new SubMenuEatOutThemeAdapter(getContext(), this);

        recyclerViewType2.setAdapter(storeType2Adapter);
        recyclerViewType3.setAdapter(themeType3Adapter);

    }

    private void initData() {
        initBanner(eatOutMainModel.eatOutBannerModelOne);
        initShopList(eatOutMainModel.eatOutShopList);
        initThemeList(eatOutMainModel.eatOutThemeList);
        initBannerTwo(eatOutMainModel.eatOutBannerModelTwo);

        Logger.d("eatOutBannerModelOne >>>" + eatOutMainModel.eatOutBannerModelOne.toString());
        Logger.d("eatOutBannerModelTwo >>>" + eatOutMainModel.eatOutBannerModelTwo.toString());

    }

    /**
     * 배너 리스트 항목을 그린다.
     *
     * @param eatOutBannerModel
     */
    private void initBanner(EatOutBannerModel eatOutBannerModel) {

        float screenWidth = DeviceUtil.getScreenWidth(getContext());
        float aspect = (float) 2.7;
        float height = screenWidth / aspect;

        if (ivBanner != null) {
            ivBanner.getLayoutParams().width = (int) screenWidth;
            ivBanner.getLayoutParams().height = (int) height;
        }

        if (eatOutBannerModel != null) {
            String url = eatOutBannerModel.bannerImgUrl;
            Glide.with(getContext())
                    .load(BuildConfig.FILE_SERVER_BASE_URL + url)
                    .thumbnail(1.0f)
                    //.apply(new RequestOptions().centerCrop())
                    .into(ivBanner);

            if (eatOutBannerModel.bannerTitle != null) {
                tvBannerDesc.setText(eatOutBannerModel.bannerTitle);
            }
            if (eatOutBannerModel.bannerCntnt != null) {
                tvBannerDescSub.setText(eatOutBannerModel.bannerCntnt);
            }
            containerType1.setVisibility(View.VISIBLE);
        } else {
            containerType1.setVisibility(View.GONE);
        }
    }

    /**
     * 매장 리스트를 그린다.
     *
     * @param eatOutShopList
     */
    private void initShopList(EatOutShopList eatOutShopList) {

        if (eatOutShopList.shopList != null) {
            if (eatOutShopList.title != null) {
                tvType2Title.setText(eatOutShopList.title);
            }
            storeType2Adapter.setStoreList(eatOutShopList.shopList);
        } else {
            containerType2.setVisibility(View.GONE);
        }
    }

    /**
     * 테마 리스트를 그린다.
     * @param eatOutThemeList
     */
    private void initThemeList(EatOutThemeList eatOutThemeList) {
        if (eatOutThemeList.themeList != null) {

            if (eatOutThemeList.themeTitle != null) {
                tvType3Title.setText(eatOutThemeList.themeTitle);
            }
            themeType3Adapter.setThemeList(eatOutThemeList.themeList);
        } else {
            containerType3.setVisibility(View.GONE);
        }
    }

    /**
     * 배너 이미지를 그린다.
     * @param eatOutBannerModel
     */
    private void initBannerTwo(EatOutBannerModel eatOutBannerModel){

        float screenWidth = DeviceUtil.getScreenWidth(getContext());
        float aspect = (float) 2.7;
        float height = screenWidth / aspect;

        if (ivSubMenuEatOutMenuTypeBannerTwo != null) {
            ivSubMenuEatOutMenuTypeBannerTwo.getLayoutParams().width = (int) screenWidth;
            ivSubMenuEatOutMenuTypeBannerTwo.getLayoutParams().height = (int) height;
        }

        if (eatOutBannerModel != null && ObjectUtil.checkNotNull(eatOutBannerModel.bannerImgUrl)) {
            String url = eatOutBannerModel.bannerImgUrl;
            Glide.with(getContext())
                    .load(BuildConfig.FILE_SERVER_BASE_URL + url)
                    .thumbnail(1.0f)
                    //.apply(new RequestOptions().centerCrop())
                    .into(ivSubMenuEatOutMenuTypeBannerTwo);

            if (eatOutBannerModel.bannerTitle != null) {
                tvSubMenuEatOutMenuTypeBannerTwoDesc.setText(eatOutBannerModel.bannerTitle);
            }
            if (eatOutBannerModel.bannerCntnt != null) {
                tvSubMenuEatOutMenuTypeBannerTwoDescSub.setText(eatOutBannerModel.bannerCntnt);
            }

            constraintSubMenuEatOutMenuTypeBannerTwo.setVisibility(View.VISIBLE);
        } else {
            constraintSubMenuEatOutMenuTypeBannerTwo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(unbinder != null){
            unbinder.unbind();
        }
    }

    @Override
    public void clickItem(Object codeType, int position, Object object) {
        if (codeType.equals(EatOutConstants.LIST_CLICK_LISTENER_TYPE.EATOUT_LIST_STORE.getType())) {
            //상점 리스트 아이템 선택 > 상세화면으로 이동
            EatOutStoreModel eatOutStoreModel = (EatOutStoreModel) object;

            if(eatOutStoreModel != null && eatOutStoreModel.storId != null){
                Intent intent = new Intent(getContext(), EatOutStoreDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MoaConstants.EXTRA_STORE_ID, Integer.valueOf(eatOutStoreModel.storId));

                intent.putExtra(MoaConstants.EXTRA_STORE_LIST_POSITION, position);
                startActivityForResult(intent, MoaConstants.REQUEST_STORE_DETAIL);
            }

        } else if (codeType.equals(EatOutConstants.LIST_CLICK_LISTENER_TYPE.EATOUT_LIST_THEME.getType())) {
            //테마 리스트 아이템 선택 > 상점 리스트 화면으로 이동
            EatOutThemeModel themeItem = (EatOutThemeModel) object;
            if(themeItem != null){
                moveEatOutStoreListActivity(themeItem.bannerCntnt,
                        EatOutConstants.EATOUT_LIST_REQUEST_TYPE.REQUEST_TYPE_THEME.getType(), themeItem.bannerSepaCntnt);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.d("requestCode " + requestCode + " resultCode " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MoaConstants.REQUEST_STORE_DETAIL){

            if(resultCode == MoaConstants.RESULT_STORE_DETAIL_INFO_CHANGED){
                if(data != null){
                    StoreInfoChangedModel storeInfoChangedModel = new Gson().fromJson(
                            data.getStringExtra(MoaConstants.EXTRA_STORE_INFO_CHANGED_DATA), StoreInfoChangedModel.class);

                    Logger.d("storeInfoChangedModel >>> " + storeInfoChangedModel.toString());

                    //LiveData에 속한 객체를 가져온다.
                    EatOutStoreModel tempModel = storeType2Adapter.getStoreList().get(storeInfoChangedModel.position);
                    tempModel.bmarkCnt = storeInfoChangedModel.getFovoriteCount();
                    tempModel.storRevwCnt = storeInfoChangedModel.reviewCount;
                    storeType2Adapter.notifyItemChanged(storeInfoChangedModel.position);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.constrainSubMenuEatOutSearchContainer:
                //Todo 지운 : 검색화면으로 넘기는 작업 필요\
                Intent storeSearchActivityIntent = new Intent(getContext(), StoreSearchActivity.class);
                storeSearchActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                storeSearchActivityIntent.putExtra(StoreSearchActivity.EXTRA_STORE_SEARCH_FROM_VIEW, StoreSearchActivity.FROM_VIEW_EATOUT);
                startActivity(storeSearchActivityIntent);
                break;

            //배너 선택시 이벤트
            case R.id.ivSubMenuEatOutMenuType1Banner:
                String banner1Title  = eatOutMainModel.eatOutBannerModelOne.bannerTitle;
                String actionType = eatOutMainModel.eatOutBannerModelOne.bannerSepaCd;
                String data = eatOutMainModel.eatOutBannerModelOne.bannerSepaCntnt;
                moveBannerAction(banner1Title, actionType, data);
                break;

            //상점 리스트 더보기 클릭시 이벤트
            case R.id.llSubMenuEatOutMenuType2TitleMore:
                moveEatOutStoreListActivity(eatOutMainModel.eatOutShopList.title, EatOutConstants.EATOUT_LIST_REQUEST_TYPE.REQUEST_TYPE_STORE_LIST.getType(), "");
                break;

            //배너 두번째
            case R.id.ivSubMenuEatOutMenuTypeBannerTwo:
                String banner2Title  = eatOutMainModel.eatOutBannerModelTwo.bannerTitle;
                String banner2ActionType = eatOutMainModel.eatOutBannerModelTwo.bannerSepaCd;
                String banner2Data = eatOutMainModel.eatOutBannerModelTwo.bannerSepaCntnt;
                moveBannerAction(banner2Title, banner2ActionType, banner2Data);
                break;
        }
    }

    /**
     * 배너 선택시 화면 이동 정의
     * 타입에 따라 웹뷰, 상세화면, 리스트 화면으로 이동한다.
     * @param actionType
     * @param data 웹뷰일때는 url이 상세일때는 id가 리스트일때는 groupId 정보가 담긴다.
     */
    private void moveBannerAction(String title, String actionType, String data) {

        if(actionType != null && data != null){
            if (actionType.equals(EatOutConstants.EATOUT_ITEM_ACTION_TYPE.ACTION_TYPE_WEBVIEW.getType())) {
//                Toast.makeText(getContext(), "웹뷰로 이동", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), EventWebViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, "https://naver.com");
                intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_SHOW_VALUE);
                //intent.putExtra(CodeTypeManager.WebViewType.TITLE_NAME_KEY.toString(), "Test");
                startActivity(intent);

            } else if (actionType.equals(EatOutConstants.EATOUT_ITEM_ACTION_TYPE.ACTION_TYPE_SHOP_DETAIL.getType())) {
//                Toast.makeText(getContext(), "상세화면으로 이동", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), EatOutStoreDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MoaConstants.EXTRA_STORE_ID, Integer.valueOf(data));
                startActivity(intent);

            } else if (actionType.equals(EatOutConstants.EATOUT_ITEM_ACTION_TYPE.ACTION_TYPE_SHOP_LIST.getType())) {
//                Toast.makeText(getContext(), "리스트화면으로 이동", Toast.LENGTH_SHORT).show();
                moveEatOutStoreListActivity(title,
                        EatOutConstants.EATOUT_LIST_REQUEST_TYPE.REQUEST_TYPE_BANNER.getType(), data);
            }
        }
    }

    /**
     * 외식 리스트 화면으로 이동한다.
     * @param title
     * @param type
     * @param id
     */
    private void moveEatOutStoreListActivity(String title, String type, String id){

        if (title != null && type != null && id != null){
            Intent intent = new Intent(getContext(), EatOutStoreListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(MoaConstants.EXTRA_TITLE_NAME, title);
            intent.putExtra(EatOutStoreListActivity.EXTRA_EATOUT_STORE_GROUP_TYPE, type);
            intent.putExtra(EatOutStoreListActivity.EXTRA_EATOUT_STORE_GROUP_ID, id);       //상점 리스트 더보기는 id값을 보내지 않음
            startActivity(intent);
        }
    }
}

