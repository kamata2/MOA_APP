package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.adapter.WalletPointHisrotyAdapter;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.controller.WalletPointHistoryController;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.ReqPointSaveUseModel;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response.ResPointSaveUseListModel;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response.WalletInfoModel;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.dialog.yesOrNo.OneBtnDialog;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.MoneyConverter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 월렛 > 포인트 내역 화면
 */
public class WalletPointHistoryActivity extends AppCompatActivity implements RetrofitConnectionResult, ListItemClickListener, MoneyConverter {

    private CommonTitleView commonTitleWalletPointHistory;

    private TextView tvWalletPointHistroyGoEatPoint;
    private TextView tvWalletPointHistroyGoEatPointWon;
    private TextView tvWalletPointHistoryUseAblePoint;          //사용가능 포인트
    private TextView tvWalletPointHistoryStandByPoint;          //활성 예정 포인트
    private LinearLayout llWalletPointHistoryStandByPointTitleParent;
    private RadioGroup rbWalletPointHistorySaveAndUse;
    private RecyclerView recyclerWalletPointHistoryList;
    private LinearLayout llWalletPointHistoryEmptyList;     //리스트 데이터 없을시 노출처리

    private WalletPointHistoryController controller;
    private ReqPointSaveUseModel requestModel;
    private WalletPointHisrotyAdapter walletPointHisrotyAdapter;

    private WalletPointHistoryTask walletPointHistoryTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_point_history);
        initLayout();
        initListener();
        initAdapter();
        initController();
        getPointHistory();
    }

    private void initLayout() {
        commonTitleWalletPointHistory = findViewById(R.id.commonTitleWalletPointHistory);
        commonTitleWalletPointHistory.setTitleName(getString(R.string.wallet_point_histoy_title));
        commonTitleWalletPointHistory.setBackButtonClickListener((View view) -> finish());

        tvWalletPointHistroyGoEatPoint = findViewById(R.id.tvWalletPointHistroyGoEatPoint);
        tvWalletPointHistroyGoEatPointWon = findViewById(R.id.tvWalletPointHistroyGoEatPointWon);
        tvWalletPointHistoryUseAblePoint = findViewById(R.id.tvWalletPointHistoryUseAblePoint);
        tvWalletPointHistoryStandByPoint = findViewById(R.id.tvWalletPointHistoryStandByPoint);
        llWalletPointHistoryStandByPointTitleParent = findViewById(R.id.llWalletPointHistoryStandByPointTitleParent);

        rbWalletPointHistorySaveAndUse = findViewById(R.id.rbWalletPointHistorySaveAndUse);
        recyclerWalletPointHistoryList = findViewById(R.id.recyclerWalletPointHistoryList);
        llWalletPointHistoryEmptyList = findViewById(R.id.llWalletPointHistoryEmptyList);
        llWalletPointHistoryEmptyList.setVisibility(View.GONE);
    }

    private void initListener() {

        llWalletPointHistoryStandByPointTitleParent.setOnClickListener(v -> {
            OneBtnDialog pointToolTipDialog = new OneBtnDialog();
            pointToolTipDialog.dialogTitle(getString(R.string.one_button_dialog_title_point_tooltip));
            pointToolTipDialog.dialogContent(getString(R.string.one_button_dialog_content_point_tooltip));
            pointToolTipDialog.setShowCloseButton(true);
            pointToolTipDialog.oneBtnDialogFragmentListener(() -> pointToolTipDialog.dismiss());
            pointToolTipDialog.show(getSupportFragmentManager(),"pointToolTipDialog");
        });

        rbWalletPointHistorySaveAndUse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //적립
                    case R.id.rbWalletPointHistorySave:
                        requestModel.sort = MoaConstants.PARAM_SAVE;
                        getPointHistory();
                        break;
                    //사용
                    case R.id.rbWalletPointHistoryUse:
                        requestModel.sort = MoaConstants.PARAM_USE;
                        getPointHistory();
                        break;
                }
            }
        });

//        recyclerWalletPointHistoryList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
//                int itemTotalCnt = recyclerWalletPointHistoryList.getAdapter().getItemCount();
//
//                if (lastVisibleItemPosition == itemTotalCnt) {
//                    //page load
//                    //TODO CHAN : 페이징 처리 필요
//                }
//            }
//        });
    }

    private void initAdapter() {
        walletPointHisrotyAdapter = new WalletPointHisrotyAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerWalletPointHistoryList.setLayoutManager(layoutManager);
        recyclerWalletPointHistoryList.setAdapter(walletPointHisrotyAdapter);
    }

    private void initController() {
        requestModel = new ReqPointSaveUseModel();
        requestModel.sort = MoaConstants.PARAM_SAVE;
        controller = new WalletPointHistoryController(this);
        controller.setRetrofitConnectionResult(this);
    }

    private void getPointHistory() {
        walletPointHistoryTask = new WalletPointHistoryTask();
        if (walletPointHistoryTask != null && walletPointHistoryTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        }
        walletPointHistoryTask.execute();
    }

    @Override
    public void onRetrofitSuccess(int type, BaseModel baseModel) {

        ResPointSaveUseListModel resPointSaveUseListModel = (ResPointSaveUseListModel) baseModel;

        if (resPointSaveUseListModel.walletInfo != null) {
            setTopInfo(resPointSaveUseListModel.walletInfo);
        }

        if (resPointSaveUseListModel.list != null) {
            walletPointHisrotyAdapter.setList(resPointSaveUseListModel.list);
        }

        if (walletPointHisrotyAdapter.getItemCount() == 0) {
            recyclerWalletPointHistoryList.setVisibility(View.GONE);
            llWalletPointHistoryEmptyList.setVisibility(View.VISIBLE);
        } else {
            recyclerWalletPointHistoryList.setVisibility(View.VISIBLE);
            llWalletPointHistoryEmptyList.setVisibility(View.GONE);
        }

    }

    @Override
    public void onRetrofitFail(int type, String msg) {

    }

    private void setTopInfo(WalletInfoModel walletInfo) {
        tvWalletPointHistroyGoEatPoint.setText(String.format(getString(R.string.wallet_point_p), commaUnitChange(walletInfo.totalPointBal)));
        tvWalletPointHistroyGoEatPointWon.setText(String.format(getString(R.string.wallet_point_w), commaUnitChange(walletInfo.totalPointBal)));
        tvWalletPointHistoryUseAblePoint.setText(String.format(getString(R.string.wallet_point_p), commaUnitChange(walletInfo.usablePointBal)));
        tvWalletPointHistoryStandByPoint.setText(String.format(getString(R.string.wallet_point_p), commaUnitChange(walletInfo.lockPointBal)));
    }

    @Override
    public void clickItem(Object codeType, int position, Object object) {

    }

    class WalletPointHistoryTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            controller.getPointSaveUseList(requestModel);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
