package com.moaPlatform.moa.delivery_menu.store_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.delivery_menu.store_detail.StoreDetailActivity;
import com.moaPlatform.moa.delivery_menu.store_list.model.ReqStoreListModel;
import com.moaPlatform.moa.delivery_menu.store_list.model.StoreListViewModel;
import com.moaPlatform.moa.model.data_model.StoreInfoChangedModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.StoreInfoModel;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StoreListFragment extends Fragment implements ListItemClickListener {

    // 서브 메뉴 코드
    String subMenuCode = "1";
    private View view;
    private ReqStoreListModel reqStoreListModel;
    private StoreListViewModel storeListViewModel;
    private StoreListPagingAdapter pagingAdapter;
    private LottieAnimationView lottieLoading;

    // 중복 클릭 방지 시간 설정
//    private long mLastClickTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.store_list_fragment, container, false);
        return view;
    }

    private void viewInit() {
        lottieLoading = view.findViewById(R.id.lottieLoading);
        String[] animationList = {"loading_bread", "loading_cook_steak", "loading_soybean"};
        Random generator = new Random();
        int randomNumber = generator.nextInt(animationList.length);
        lottieLoading.setImageAssetsFolder(animationList[randomNumber]);
        lottieLoading.setAnimation(animationList[randomNumber] + ".json");
        lottieLoading.playAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewInit();
        storeListInit();
    }

    void setStoreListSort(String sort) {
        reqStoreListModel.sort = sort;
        lottieLoading.playAnimation();
        lottieLoading.setVisibility(View.VISIBLE);
        reqStoreListModel.currentPage = 0;
        listUpdate();
    }

    private void listUpdate() {
        storeListViewModel.storeList = null;
        pagingAdapter.submitList(null);
        storeListViewModel.storeListInit(reqStoreListModel);
        storeListViewModel.storeList.observe(this, storeInfoModels -> {
            pagingAdapter.onlyDefaultList();
            pagingAdapter.submitList(storeInfoModels);
        });
        storeListViewModel.isLoading.observe(this, aBoolean -> {
            lottieLoading.cancelAnimation();
            lottieLoading.setVisibility(View.GONE);
        });
    }

    /**
     * 가맹점 리스트 초기화
     */
    private void storeListInit() {
        RecyclerView storeListRecyclerView = view.findViewById(R.id.storeListRecyclerView);
        storeListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storeListViewModel = ViewModelProviders.of(this).get(StoreListViewModel.class);
        storeListViewModel.storeListInit(storeListReqModelInit());
        pagingAdapter = new StoreListPagingAdapter();
        pagingAdapter.setListItemClickListener(this);
        storeListViewModel.storeList.observe(this, storeInfoModels -> pagingAdapter.submitList(storeInfoModels));

        storeListViewModel.isLoading.observe(this, isLoading -> {
            if (!isLoading) {
                lottieLoading.setVisibility(View.GONE);
                lottieLoading.cancelAnimation();
            }
        });

        storeListRecyclerView.setAdapter(pagingAdapter);

        storeListViewModel.isEmpty.observe(this, aBoolean -> {
            View viewListNullIc = view.findViewById(R.id.viewListNullIc);
            viewListNullIc.setVisibility(View.VISIBLE);
        });
    }

    /**
     * 가맹점 리스트 불러올 reqModel 초기화
     *
     * @return 데이터 세팅한 reqModel
     */
    private ReqStoreListModel storeListReqModelInit() {
        reqStoreListModel = new ReqStoreListModel();
        reqStoreListModel.init(subMenuCode, "10", 0);
        Logger.i("보여줄 페이지 : " + reqStoreListModel.currentPage);
        return reqStoreListModel;
    }

    @Override
    public void clickItem(Object codeType, int position, Object object) {
        Intent intent = new Intent(getContext(), StoreDetailActivity.class);
        intent.putExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), ((StoreInfoModel) object).storeId);
        intent.putExtra(MoaConstants.EXTRA_STORE_NAME, ((StoreInfoModel) object).storeName);
        intent.putExtra(MoaConstants.EXTRA_STORE_LIST_POSITION, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, MoaConstants.REQUEST_STORE_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d("requestCode " + requestCode + " resultCode " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MoaConstants.REQUEST_STORE_DETAIL) {

            if (resultCode == MoaConstants.RESULT_STORE_DETAIL_INFO_CHANGED) {
                if (data != null) {
                    StoreInfoChangedModel storeInfoChangedModel = new Gson().fromJson(
                            data.getStringExtra(MoaConstants.EXTRA_STORE_INFO_CHANGED_DATA), StoreInfoChangedModel.class);

                    Logger.d("storeInfoChangedModel >>> " + storeInfoChangedModel.toString());

                    //LiveData에 속한 객체를 가져온다.
                    StoreInfoModel tempModel = storeListViewModel.storeList.getValue().get(storeInfoChangedModel.position);
                    tempModel.bookmarkCnt = Integer.valueOf(storeInfoChangedModel.getFovoriteCount());
                    tempModel.storeReviewCnt = Integer.valueOf(storeInfoChangedModel.reviewCount);
                    pagingAdapter.notifyItemChanged(storeInfoChangedModel.position);
                }
            }
        }
    }

}
