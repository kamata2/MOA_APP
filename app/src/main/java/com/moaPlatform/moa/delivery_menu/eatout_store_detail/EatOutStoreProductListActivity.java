package com.moaPlatform.moa.delivery_menu.eatout_store_detail;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.ResEatOutStoreProductListInfo;
import com.moaPlatform.moa.model.req_model.ReqStoreInfoModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.StoreGridListFragment;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.interfaces.HttpConnectionResult;
import com.moaPlatform.moa.util.models.BaseModel;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.moaPlatform.moa.constants.MoaConstants.EXTRA_STORE_ID;

/**
 * [정의]
 * 외식 상세 > 상품 리스트 화면 Activity
 *
 * @author chan
 */
public class EatOutStoreProductListActivity extends AppCompatActivity implements HttpConnectionResult {

    @BindView(R.id.eatOutStoreProductListTitle)
    CommonTitleView eatOutStoreProductListTitle;

    @BindView(R.id.flEatOutStoreProductListContainer)
    FrameLayout flEatOutStoreProductListContainer;

    private EatOutStoreDetailServerController controller;
    private ReqStoreInfoModel reqStoreInfoModel;      //요청 Data

    private String titleText;
    private int requestStoreId;

    private StoreGridListFragment storeGridListFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_eat_out_product_list);
        ButterKnife.bind(this);

        initDefaultData();

        initTitleView(titleText);
        initFragment();
        getProductList();
    }

    private void initDefaultData() {
        titleText = getIntent().getStringExtra(MoaConstants.EXTRA_TITLE_NAME);

        requestStoreId = getIntent().getIntExtra(EXTRA_STORE_ID, 0);
        reqStoreInfoModel = new ReqStoreInfoModel();
        reqStoreInfoModel.storeId = requestStoreId;

    }

    private void initTitleView(String titleText) {

        eatOutStoreProductListTitle.setBackButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        eatOutStoreProductListTitle.setTitleName(titleText);
    }

    private void initFragment() {

        storeGridListFragment = new StoreGridListFragment();
//        Bundle bundle = new Bundle();
//        storeGridListFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.flEatOutStoreProductListContainer, storeGridListFragment);
        fragmentTransaction.commit();
    }

    private void getProductList() {
        controller = new EatOutStoreDetailServerController(this);
        controller.getEatOutStoreProductList(reqStoreInfoModel);
        controller.setHttpConnectionResult(this);
    }

    @Override
    public void onHttpConnectionSuccess(int type, BaseModel baseModel) {

        if (!isFinishing()) {
            if (storeGridListFragment != null) {
                ResEatOutStoreProductListInfo eatOutStoreProductListInfo = ((ResEatOutStoreProductListInfo) baseModel);
                Logger.i("가맹점 정보 : " + new Gson().toJson(eatOutStoreProductListInfo.storeRepresentativeMenuList));
                Logger.i("전체 매뉴 : " + new Gson().toJson(eatOutStoreProductListInfo.storeAllMenuList));
                storeGridListFragment.listUpdate(eatOutStoreProductListInfo.storeRepresentativeMenuList, eatOutStoreProductListInfo.storeAllMenuList == null
                        ? new ArrayList<>() : eatOutStoreProductListInfo.storeAllMenuList);
            }
        }
    }
}
