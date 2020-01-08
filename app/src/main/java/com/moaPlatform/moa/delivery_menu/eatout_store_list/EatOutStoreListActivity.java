package com.moaPlatform.moa.delivery_menu.eatout_store_list;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.dialog.yesOrNo.ListFilterDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * [정의]
 * 외식 매장 리스트 화면 Activity
 *
 * [구성]
 * 상단에 배너 위치(상단 고정)
 * 탭메뉴 구성
 *
 * @author chan
 */
public class EatOutStoreListActivity extends AppCompatActivity {

    public static final String EXTRA_EATOUT_STORE_GROUP_TYPE = "EXTRA_EATOUT_STORE_GROUP_TYPE";
    public static final String EXTRA_EATOUT_STORE_GROUP_ID = "EXTRA_EATOUT_STORE_GROUP_ID";

    @BindView(R.id.eatOutStoreListTitle)
    CommonTitleView titleView;
    @BindView(R.id.btnEatoutStoreListFilter)
    FloatingActionButton btnEatoutStoreListFilter;

    private EatOutStoreListFragment eatOutStoreListFragment;

    private String titleText;
    private String groupType;
    private String groupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_eat_out_store_list);
        ButterKnife.bind(this);

        initDefaultData();

        initTitleView(titleText);
        initFragment();
        initListener();
    }

    private void initDefaultData(){
        titleText = getIntent().getStringExtra(MoaConstants.EXTRA_TITLE_NAME);
        groupType = getIntent().getStringExtra(EXTRA_EATOUT_STORE_GROUP_TYPE);
        groupId = getIntent().getStringExtra(EXTRA_EATOUT_STORE_GROUP_ID);
    }

    private void initTitleView(String titleText){
        titleView.setBackButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleView.setTitleName(titleText);
    }

    private void initFragment() {

        eatOutStoreListFragment = new EatOutStoreListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EatOutStoreListFragment.ARG_EATOUT_STORE_GROUP_TYPE, groupType);
        bundle.putString(EatOutStoreListFragment.ARG_EATOUT_STORE_GROUP_ID, groupId);
        eatOutStoreListFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.flEatOutStoreContainer, eatOutStoreListFragment);
        fragmentTransaction.commit();
    }

    private void initListener(){
        ListFilterDialog listFilterDialog = new ListFilterDialog();

        btnEatoutStoreListFilter.setOnClickListener(v -> {
            listFilterDialog.setFromViewType(ListFilterDialog.FROM_VIEW_TYPE_EATOUT_LIST);
                listFilterDialog.show(getSupportFragmentManager(), "filterDialog");
                listFilterDialog.setListFilterDialogFragmentListener(order -> {
                    // 정렬
                    eatOutStoreListFragment.showProgress();
                    eatOutStoreListFragment.listUpdate(order);
            });
        });
    }
}
