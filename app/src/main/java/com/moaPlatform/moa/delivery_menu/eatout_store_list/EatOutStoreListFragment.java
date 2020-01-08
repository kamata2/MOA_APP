package com.moaPlatform.moa.delivery_menu.eatout_store_list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.main.model.EatOutStoreModel;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.EatOutStoreDetailActivity;
import com.moaPlatform.moa.delivery_menu.eatout_store_list.model.EatOutStoreListViewModel;
import com.moaPlatform.moa.delivery_menu.eatout_store_list.model.ReqEatOutStoreListModel;
import com.moaPlatform.moa.model.data_model.StoreInfoChangedModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * [정의]
 * 외식 매장 리스트 화면 Activity
 *
 * [구성]
 * 탭메뉴에 해당하는 리스트 구성(페이징처리)
 *
 * @author chan
 */
public class EatOutStoreListFragment extends Fragment implements ListItemClickListener {

    public static final String ARG_EATOUT_STORE_GROUP_TYPE = "ARG_EATOUT_STORE_GROUP_TYPE";
    public static final String ARG_EATOUT_STORE_GROUP_ID = "ARG_EATOUT_STORE_GROUP_ID";

    //request Param
    private String requestGroupType;
    private String requestGroupId;
    private LottieAnimationView lottieEatOutStoreListLoading;

    private ReqEatOutStoreListModel reqStoreListModel;
    private EatOutStoreListViewModel eatOutStoreListViewModel;
    private EatOutStorePagingAdapter pagingAdapter;

    public void setProgress(boolean progress) {
        Logger.d("setProgress >>>" + progress);
        if(progress){
            showProgress();
        }else{
            dismissProgress();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestGroupType = getArguments().getString(ARG_EATOUT_STORE_GROUP_TYPE);
            requestGroupId = getArguments().getString(ARG_EATOUT_STORE_GROUP_ID);
        }

//        //상점 리스트 더보기는 id값을 보내지 않음
//        if(requestGroupType != null){
//            getActivity().finish();
//            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eat_out_store_list, container, false);
        initView(view);
        storeListInit(view);
        return view;
    }

    private void initView(View view){
        lottieEatOutStoreListLoading = view.findViewById(R.id.lottieEatOutStoreListLoading);
        String[] animationList = {"loading_bread", "loading_cook_steak", "loading_soybean"};
        Random generator = new Random();
        int randomNumber = generator.nextInt(animationList.length);
        lottieEatOutStoreListLoading.setImageAssetsFolder(animationList[randomNumber]);
        lottieEatOutStoreListLoading.setAnimation(animationList[randomNumber]+".json");
    }

    /**
     * 가맹점 리스트 초기화
     */
    private void storeListInit(View view) {
        RecyclerView eatOutStoreListRecyclerView = view.findViewById(R.id.recyclerEatOutStoreList);
        eatOutStoreListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        eatOutStoreListViewModel = ViewModelProviders.of(this).get(EatOutStoreListViewModel.class);
        eatOutStoreListViewModel.storeListInit(eatOutStoreListReqModelInit());
        pagingAdapter = new EatOutStorePagingAdapter(getContext(), this);
        eatOutStoreListViewModel.isProgress.observe(this, this::setProgress);
        //eatOutStoreListViewModel.storeList.observe(this, pagingAdapter::submitList);
        eatOutStoreListViewModel.storeList.observe(this, eatOutStoreModels -> {
            if(eatOutStoreModels != null){
                pagingAdapter.submitList(eatOutStoreModels);
            }
        });
        eatOutStoreListRecyclerView.setAdapter(pagingAdapter);
    }

    /**
     * 가맹점 리스트 불러올 reqModel 초기화
     * @return
     * 데이터 세팅한 reqModel
     */
    private ReqEatOutStoreListModel eatOutStoreListReqModelInit() {
        reqStoreListModel = new ReqEatOutStoreListModel();
        reqStoreListModel.init(requestGroupType, requestGroupId, 0);          //상점 리스트 더보기는 groupId 값을 보내지 않음
        return reqStoreListModel;
    }

    public void listUpdate(String sort) {
        reqStoreListModel.currentPage = 0;
        reqStoreListModel.sort = sort;
        eatOutStoreListViewModel.storeList = null;
        pagingAdapter.submitList(null);
        eatOutStoreListViewModel.storeListInit(reqStoreListModel);
        eatOutStoreListViewModel.isProgress.observe(this, this::setProgress);
        //eatOutStoreListViewModel.storeList.observe(this, pagingAdapter::submitList);
        eatOutStoreListViewModel.storeList.observe(this, eatOutStoreModels -> {
            if(eatOutStoreModels != null){
                pagingAdapter.submitList(eatOutStoreModels);
            }
        });
    }

    public void showProgress() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            if (lottieEatOutStoreListLoading != null) {
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                lottieEatOutStoreListLoading.setVisibility(View.VISIBLE);
                lottieEatOutStoreListLoading.playAnimation();
            }
        }
    }

    private void dismissProgress() {

        if (getActivity() != null && !getActivity().isFinishing()) {
            if (lottieEatOutStoreListLoading != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(getActivity().getWindow() != null){
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                        lottieEatOutStoreListLoading.setVisibility(View.GONE);
                        lottieEatOutStoreListLoading.cancelAnimation();
                    }
                }, MoaConstants.LOADING_BAR_VISIBLE_TIME);
            }
        }
    }

    @Override
    public void clickItem(Object codeType, int position, Object object) {
        EatOutStoreModel eatOutStoreModel = (EatOutStoreModel) object;

        if (eatOutStoreModel != null && eatOutStoreModel.storId != null) {
            Intent intent = new Intent(getContext(), EatOutStoreDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(MoaConstants.EXTRA_STORE_ID, Integer.valueOf(eatOutStoreModel.storId));
            intent.putExtra(MoaConstants.EXTRA_STORE_LIST_POSITION, position);
            startActivityForResult(intent, MoaConstants.REQUEST_STORE_DETAIL);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d("requestCode " + requestCode + " resultCode " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MoaConstants.REQUEST_STORE_DETAIL){
            if(resultCode == MoaConstants.RESULT_STORE_DETAIL_INFO_CHANGED){
                if(data != null){
                    StoreInfoChangedModel storeInfoChangedModel = new Gson().fromJson(
                            data.getStringExtra(MoaConstants.EXTRA_STORE_INFO_CHANGED_DATA), StoreInfoChangedModel.class);

                    //LiveData에 속한 객체를 가져온다.
                    EatOutStoreModel tempModel = eatOutStoreListViewModel.storeList.getValue().get(storeInfoChangedModel.position);
                    tempModel.bmarkCnt = storeInfoChangedModel.getFovoriteCount();
                    tempModel.storRevwCnt = storeInfoChangedModel.reviewCount;

                    pagingAdapter.notifyItemChanged(storeInfoChangedModel.position);
                }
            }
        }
    }
}
