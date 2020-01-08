package com.moaPlatform.moa.top_menu.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.EatOutStoreDetailActivity;
import com.moaPlatform.moa.delivery_menu.store_detail.StoreDetailActivity;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.custom_view.CommonLoadingView;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.dialog.yesOrNo.ListFilterDialog;
import com.moaPlatform.moa.util.interfaces.HttpConnectionResult;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.StoreInfoModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 검색 화면 Activity
 */
public class StoreSearchActivity extends AppCompatActivity implements HttpConnectionResult, ListItemClickListener, StoreSearchHistoryAdapter.HistoryItemClickListener {

    public final static String EXTRA_STORE_SEARCH_FROM_VIEW = "EXTRA_STORE_SEARCH_FROM_VIEW";
    public final static String FROM_VIEW_EATOUT = "FROM_VIEW_EATOUT";           //[외식] 메뉴로 진입

    private EditText inputStoreKeyword;
    private SearchController searchController;
    private StoreSearchHistoryAdapter storeSearchHistoryAdapter, searchListAdapter;
    private ReqSearchHistoryModel reqSearchHistoryModel;
    private int removePosition = 0;
    private String sort = "1";
    private String fromVIew;

    private RelativeLayout rlSearchHistoryEmpty;    //히스토리 리스트 없을시 empty data 처리
    private RelativeLayout rlSearchEmpty;           //검색 리스트 없을시 empty data 처리
    private LinearLayout llSearchHistoryContainer;  //히스토리 리스트 UI
    private RecyclerView rvSearchStoreList;         //검색 리스트 UI

    // 로딩 뷰
    private CommonLoadingView viewSearchLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        initDefaultData();
        recyclerViewInit();
        initLayout();
        init();
        searchController.recentlySearchHistory();
    }

    private void initDefaultData() {
        fromVIew = getIntent().getStringExtra(EXTRA_STORE_SEARCH_FROM_VIEW);
    }

    private void initLayout() {

        CommonTitleView commonTitleSearch = findViewById(R.id.commonTitleSearch);
        commonTitleSearch.setTitleName(getString(R.string.store_search_title));
        commonTitleSearch.setBackButtonClickListener(v -> finish());
        RadioButton rbEatOut = findViewById(R.id.rbEatOut);

        rlSearchHistoryEmpty = findViewById(R.id.rlSearchHistoryEmpty);
        rlSearchEmpty = findViewById(R.id.rlSearchEmpty);
        llSearchHistoryContainer = findViewById(R.id.llSearchHistoryContainer);

        rlSearchHistoryEmpty.setVisibility(View.GONE);
        rlSearchEmpty.setVisibility(View.GONE);
        llSearchHistoryContainer.setVisibility(View.VISIBLE);

        //외식 메뉴로 진입했을때는 외식항목이 선택되어져야 한다.
        if (ObjectUtil.checkNotNull(fromVIew) && fromVIew.equals(FROM_VIEW_EATOUT)) {
            rbEatOut.setChecked(true);
        } else {

        }

    }

    /**
     * 뷰 초기화
     */
    private void init() {

        viewSearchLoading = findViewById(R.id.viewSearchLoading);

        ListFilterDialog listFilterDialog = new ListFilterDialog();
        reqSearchHistoryModel = new ReqSearchHistoryModel();
        searchController = new SearchController(this);
        searchController.setHttpConnectionResult(this);
        inputStoreKeyword = findViewById(R.id.inputStoreKeyword);
        inputStoreKeyword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                storeSearch();
            }
            return false;
        });

        ConstraintLayout btnAddressSearch = findViewById(R.id.btnAddressSearch);
        btnAddressSearch.setOnClickListener(v -> storeSearch());

        TextView tvAllRemove = findViewById(R.id.tvAllRemove);
        tvAllRemove.setOnClickListener(v -> searchController.searchHistoryAllRemove());


        Button btFilter = findViewById(R.id.btFilter);
        btFilter.setOnClickListener(v -> listFilterDialog.show(getSupportFragmentManager(), "filterDialog"));

        listFilterDialog.setListFilterSeletedNameListener(btFilter::setText);
        listFilterDialog.setListFilterDialogFragmentListener(order -> {
            sort = order;
            storeSearch();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        storeSearchRefresh();
    }

    /**
     * 최근 검색어  리스트 초기화
     */
    private void recyclerViewInit() {
        RecyclerView rvSearchHistory = findViewById(R.id.rvSearchHistory);
        rvSearchHistory.setLayoutManager(new LinearLayoutManager(this));
        storeSearchHistoryAdapter = new StoreSearchHistoryAdapter();
        storeSearchHistoryAdapter.setListItemClickListener(this);
        storeSearchHistoryAdapter.setHistoryItemClickListener(this);
        rvSearchHistory.setAdapter(storeSearchHistoryAdapter);

        rvSearchStoreList = findViewById(R.id.rvSearchStoreList);
        rvSearchStoreList.setLayoutManager(new LinearLayoutManager(this));
        searchListAdapter = new StoreSearchHistoryAdapter();
        rvSearchStoreList.setAdapter(searchListAdapter);
        searchListAdapter.setListItemClickListener(this);
        rvSearchStoreList.setVisibility(View.GONE);
    }

    /**
     * 가맹점 검색
     */
    private void storeSearch() {
        if (ObjectUtil.checkNotNull(getSearchKeyword().trim())) {
            viewSearchLoading.loadingAnimationPlay(this);
            reqSearchHistoryModel.soteSearchInit(getSearchKeyword(), sort, getServiceCategoryCode(), "10");
            searchController.searchStoreList(reqSearchHistoryModel);
        } else {
            Toast.makeText(this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 가맹점 검색
     */
    private void storeSearchRefresh() {
        if (reqSearchHistoryModel != null)
            searchController.searchStoreList(reqSearchHistoryModel);
    }

    /**
     * @return 사용자가 입력한 검색어를 반환
     */
    private String getSearchKeyword() {
        return inputStoreKeyword.getText().toString();
    }

    /**
     * @return 서비스 카테고리 코드를 반환
     */
    private String getServiceCategoryCode() {
        RadioGroup rbRadioGroup = findViewById(R.id.rbRadioGroup);
        String categoryCode = "1";
        switch (rbRadioGroup.getCheckedRadioButtonId()) {
            case R.id.rbEatOut:
                categoryCode = "2";
                break;
            case R.id.rbTakeOut:
                categoryCode = "5";
                break;
        }
        return categoryCode;
    }

    @Override
    public void onHttpConnectionSuccess(int type, BaseModel baseModel) {
        if (type == CodeTypeManager.TopToolbarManager.RECENTLY_STORE_SEARCH.getType()) {
            if (((ResSearchModel) baseModel).resultValue.equals(ServerSideMsg.SUCCESS))

                if (storeSearchHistoryAdapter != null) {
                    storeSearchHistoryAdapter.setRecentlyKeywordList(((ResSearchModel) baseModel).keywordList, CodeTypeManager.TopToolbarManager.RECENTLY_STORE_SEARCH.getType());
                    if (storeSearchHistoryAdapter.getItemCount() > 0) {

                    } else {
                        llSearchHistoryContainer.setVisibility(View.GONE);
                        rlSearchHistoryEmpty.setVisibility(View.VISIBLE);
                    }
                }

        } else if (type == CodeTypeManager.TopToolbarManager.RECENTLY_SEARCH_HISTORY_REMOVE.getType()) {
            if (((CommonModel) baseModel).resultValue.equals(ServerSideMsg.SUCCESS)) {
                storeSearchHistoryAdapter.removeItem(removePosition);

                if (storeSearchHistoryAdapter.getItemCount() == 0) {
                    rlSearchHistoryEmpty.setVisibility(View.VISIBLE);
                    llSearchHistoryContainer.setVisibility(View.GONE);
                }
            }
        } else if (type == CodeTypeManager.TopToolbarManager.RECENTLY_SEARCH_HISTORY_ALL_REMOVE.getType()) {
            if (((CommonModel) baseModel).resultValue.equals(ServerSideMsg.SUCCESS))
                storeSearchHistoryAdapter.allRemoveItem();
            llSearchHistoryContainer.setVisibility(View.GONE);
            rlSearchHistoryEmpty.setVisibility(View.VISIBLE);
        } else if (type == CodeTypeManager.TopToolbarManager.STORE_KEYWORD_SEARCH.getType()) {
            if (((ResSearchModel) baseModel).resultValue.equals(ServerSideMsg.SUCCESS)) {

                rvSearchStoreList.setVisibility(View.VISIBLE);
                rlSearchHistoryEmpty.setVisibility(View.GONE);
                llSearchHistoryContainer.setVisibility(View.GONE);

                if (searchListAdapter != null) {
                    searchListAdapter.setRecentlyKeywordList(((ResSearchModel) baseModel).searchStoreList, CodeTypeManager.TopToolbarManager.STORE_KEYWORD_SEARCH.getType());
                    if (searchListAdapter.getItemCount() > 0) {
                        rlSearchEmpty.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(this, getString(R.string.store_search_list_empty), Toast.LENGTH_SHORT).show();
                        rlSearchEmpty.setVisibility(View.VISIBLE);
                    }
                }

                viewSearchLoading.animationStop(this);

            }
        }
    }

    @Override
    public void clickItem(Object codeType, int position, Object object) {
        if (codeType.equals(CodeTypeManager.TopToolbarManager.RECENTLY_SEARCH_HISTORY_REMOVE.getType())) {
            removePosition = position;
            reqSearchHistoryModel.removeSearchHistoryId = ((ResSearchModel.SearchHistory) object).keywordId;
            searchController.searchHistoryRemove(reqSearchHistoryModel);

        } else if (codeType.equals(CodeTypeManager.TopToolbarManager.STORE_DETAIL_MOVE.getType())) {
            if (reqSearchHistoryModel.serviceCategoryCode().equals("1")) {
                Intent intent = new Intent(this, StoreDetailActivity.class);
                intent.putExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), ((StoreInfoModel) object).storeId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            } else if (reqSearchHistoryModel.serviceCategoryCode().equals("2")) {
                Intent intent = new Intent(this, EatOutStoreDetailActivity.class);
                intent.putExtra(MoaConstants.EXTRA_STORE_ID, ((StoreInfoModel) object).storeId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }
    }

    @Override
    public void clickItem(String keyword) {
        //검색 히스토리 리스트 아이템 선택시 이벤트 처리
        if (inputStoreKeyword != null) {
            if (ObjectUtil.checkNotNull(keyword)) {
                inputStoreKeyword.setText(keyword);
            }
        }
    }
}
