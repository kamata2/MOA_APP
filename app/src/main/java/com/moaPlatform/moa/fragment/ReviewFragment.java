package com.moaPlatform.moa.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.activity.ReviewModifyActivity;
import com.moaPlatform.moa.activity.ReviewWriteMainActivity;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.controller.ReviewController;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.adapter.ReviewAdapter;
import com.moaPlatform.moa.delivery_menu.store_detail.StoreDetailActivity;
import com.moaPlatform.moa.model.req_model.ReqReviewModel;
import com.moaPlatform.moa.model.req_model.ReqStoreInfoModel;
import com.moaPlatform.moa.model.res_model.ResReviewListModel;
import com.moaPlatform.moa.model.res_model.ReviewHeaderInfoModel;
import com.moaPlatform.moa.model.res_model.ReviewModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.models.BaseModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.moaPlatform.moa.constants.MoaConstants.EXTRA_FROM_VIEW;
import static com.moaPlatform.moa.delivery_menu.eatout_store_detail.adapter.ReviewAdapter.LIST_ITEM_CLICK_REVIEW_DELETE;
import static com.moaPlatform.moa.delivery_menu.eatout_store_detail.adapter.ReviewAdapter.LIST_ITEM_CLICK_REVIEW_MODIFY;
import static com.moaPlatform.moa.delivery_menu.eatout_store_detail.adapter.ReviewAdapter.LIST_ITEM_CLICK_REVIEW_WRITE;

/**
 * 공통 리뷰 리스트 fragment
 * <p>
 * ◎배달 상세 > 리뷰 리스트
 * ◎외식 상세 > 리뷰 리스트
 * ◎사이드메뉴 > 배달 & 플레이스
 * <p>
 * [기타]
 * ReviewHeaderInfoModel 항목이 null 이면 상단 헤더 정보를 그리지 않는다.
 */
public class ReviewFragment extends Fragment implements ListItemClickListener, RetrofitConnectionResult {

    public static final String EXTRA_REVIEW_MY_WRITING = "EXTRA_REVIEW_MY_WRITING";             //내가 쓴글 보기
    public static final String EXTRA_TITLE_VISIBILITY = "EXTRA_TITLE_VISIBILITY";               //타이틀 노출 여부

    public static final String EXTRA_REVIEW_LIST_TYPE = "EXTRA_REVIEW_LIST_TYPE";
    public static final String REVIEW_LIST_TYPE_DELRIVERY = "REVIEW_LIST_TYPE_DELRIVERY";       //배달 리스트
    public static final String REVIEW_LIST_TYPE_EAT_OUT = "REVIEW_LIST_TYPE_EAT_OUT";           //외식 리스트

    @BindView(R.id.rlEatOutStoreReviewListEmptyContainer)
    RelativeLayout reviewListEmptyContainer;

    @BindView(R.id.titleEatOutStoreReview)
    CommonTitleView commonTitleView;

    @BindView(R.id.rlReviewCountTitle)
    RelativeLayout rlReviewCountTitle;

    @BindView(R.id.tvReviewWriteCountText)
    TextView tvReviewWriteCountText;

    @BindView(R.id.recyclerEatOutStoreReviewList)
    RecyclerView recyclerEatOutStoreReviewList;

    private Unbinder unbinder;

    private ReviewController reviewController;
    private ReviewAdapter reviewAdapter;

    private ResReviewListModel resReviewListModel;

    private boolean isShowTitle;
    boolean isShowMyWriting;
    private int requestStoreId;
    private String storeName;
    private String reviewListType;
    private int reviewDeletePosition = -1;              //리뷰 아이템 삭제시 position

    private boolean reviewListItemChanged = false;      //리뷰 리스트 항목 갱신여부(쓰거나,수정,삭제시)

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isShowTitle = getArguments().getBoolean(EXTRA_TITLE_VISIBILITY);

        isShowMyWriting = getArguments().getBoolean(EXTRA_REVIEW_MY_WRITING); //내가 쓴글 보기 여부 flag
        if (isShowMyWriting) {
            requestStoreId = 0;         //store ID를 0으로 보내면 내가 쓴글이 조회된다.
        } else {
            requestStoreId = getArguments().getInt(MoaConstants.EXTRA_STORE_ID);
        }
        storeName = getArguments().getString(MoaConstants.EXTRA_STORE_NAME);
        reviewListType = getArguments().getString(EXTRA_REVIEW_LIST_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        unbinder = ButterKnife.bind(this, view);

        initView();
        initListener();
        initAdapter();
        initController();

        requestReviewList();
        return view;
    }

    private void initView() {
        if (isShowTitle) {
            commonTitleView.setTitleName(getResources().getString(R.string.common_title_review));
            commonTitleView.setBackButtonClickListener(v -> {
                        if (getActivity() != null && !getActivity().isFinishing()) {
                            if(isReviewListItemChanged()){
                                getActivity().setResult(MoaConstants.RESULT_REVIEW_LIST_NOTIFYCHANGED);
                            }
                            getActivity().finish();
                        }
                    }
            );
        } else {
            commonTitleView.setVisibility(View.GONE);
        }
        if(isShowMyWriting){
            rlReviewCountTitle.setVisibility(View.VISIBLE);
        }else{
            rlReviewCountTitle.setVisibility(View.GONE);
        }

        //2019-07-12 해당부분 터치이벤트가 있으면 배달 상세에서 스크롤이 안되는 문제가 발생되어 터치를 막아줌
        reviewListEmptyContainer.setFocusableInTouchMode(false);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener(){

    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerEatOutStoreReviewList.setLayoutManager(layoutManager);
        reviewAdapter = new ReviewAdapter(getContext(), this);
        if (reviewListType.equals(REVIEW_LIST_TYPE_DELRIVERY)) {
            reviewAdapter.setEatOutView(false);
        } else if (reviewListType.equals(REVIEW_LIST_TYPE_EAT_OUT)) {
            reviewAdapter.setEatOutView(true);
        }
        recyclerEatOutStoreReviewList.setAdapter(reviewAdapter);
    }

    private void initController() {
        reviewController = new ReviewController(getContext());
        reviewController.setRetrofitConnectionResult(this);
    }


    /**
     * 리뷰 리스트를 요청한다.
     */
    private void requestReviewList() {
        if (reviewListType.equals(REVIEW_LIST_TYPE_DELRIVERY)) {
            requestReviewList(requestStoreId);
        } else if (reviewListType.equals(REVIEW_LIST_TYPE_EAT_OUT)) {
            requestEatOutReviewList(requestStoreId);
        }
    }

    /**
     * 배달 리뷰 조회
     */
    private void requestReviewList(int storeId) {
        if (reviewController != null) {
            ReqStoreInfoModel requestModel = new ReqStoreInfoModel();
            requestModel.storeId = storeId;
            reviewController.getStoreReviewList(requestModel);
        }
    }

    /**
     * 외식 리뷰 조회
     */
    private void requestEatOutReviewList(int storeId) {
        if (reviewController != null) {
            ReqStoreInfoModel requestModel = new ReqStoreInfoModel();
            requestModel.storeId = storeId;
            reviewController.getEatOutStoreReviewList(requestModel);
        }
    }

    /**
     * 리뷰 삭제
     */
    private void requestDeleteReview(ReqReviewModel reqReviewModel) {
        if (reviewController != null) {
            reviewController.deleteReview(reqReviewModel);
        }
    }

    @Override
    public void clickItem(Object codeType, int position, Object object) {

        Logger.d("review list item select position is >>>> " + position);

        int type = (int) codeType;

        switch (type) {
            //리뷰 쓰기 화면으로 이동
            case LIST_ITEM_CLICK_REVIEW_WRITE:
                Intent reViewWriteIntent = new Intent(getContext(), ReviewWriteMainActivity.class);
                reViewWriteIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                reViewWriteIntent.putExtra(MoaConstants.EXTRA_STORE_NAME, storeName);
                reViewWriteIntent.putExtra(MoaConstants.EXTRA_STORE_ID, requestStoreId);

                if (reviewListType.equals(REVIEW_LIST_TYPE_DELRIVERY)) {
                    //배달은 orderId 기준으로 리뷰가 등록된다
                    ReviewHeaderInfoModel reviewHeaderInfoModel = resReviewListModel.reviewHeaderInfoModel;
                    reViewWriteIntent.putExtra(MoaConstants.EXTRA_ORDER_ID, reviewHeaderInfoModel.orderId);
                    reViewWriteIntent.putExtra(ReviewWriteMainActivity.ORDERD_GOODS_LIST, reviewHeaderInfoModel.getOrderedGoodsList());

                    reViewWriteIntent.putExtra(EXTRA_FROM_VIEW, ReviewWriteMainActivity.FROM_VIEW_STORE_REVIEW);
                } else if (reviewListType.equals(REVIEW_LIST_TYPE_EAT_OUT)) {
                    reViewWriteIntent.putExtra(EXTRA_FROM_VIEW, ReviewWriteMainActivity.FROM_VIEW_EATOUT_STORE_REVIEW);
                }
                startActivityForResult(reViewWriteIntent, MoaConstants.REQUEST_REVIEW_WRITE);
                break;

            //리뷰 수정 하기 화면으로 이동
            case LIST_ITEM_CLICK_REVIEW_MODIFY:
                ReviewModel modifyReviewModel = (ReviewModel) object;
                Intent intent = new Intent(getContext(), ReviewModifyActivity.class);
                intent.putExtra(ReviewModifyActivity.EXTRA_USER_REVIEW_ID, modifyReviewModel.userRevwId);
                intent.putExtra(ReviewModifyActivity.EXTRA_REVIEW_ADAPTER_POSITION, position);

                if (reviewListType.equals(REVIEW_LIST_TYPE_DELRIVERY)) {
                    intent.putExtra(ReviewModifyActivity.EXTRA_REVIEW_TYPE, ReviewModifyActivity.REVIEW_TYPE_DELRIVERY);
                } else if (reviewListType.equals(REVIEW_LIST_TYPE_EAT_OUT)) {
                    intent.putExtra(ReviewModifyActivity.EXTRA_REVIEW_TYPE, ReviewModifyActivity.REVIEW_TYPE_EAT_OUT);
                }
                startActivityForResult(intent, MoaConstants.REQUEST_REVIEW_MODIFY);
                break;

            case LIST_ITEM_CLICK_REVIEW_DELETE:

                YesOrNoDialog reviewDeleteDialog = new YesOrNoDialog();
                reviewDeleteDialog.dialogContent(getString(R.string.yes_or_no_dialog_content_delete_review));
                reviewDeleteDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
                    @Override
                    public void onDialogNo() {
                        reviewDeleteDialog.dismiss();
                    }

                    @Override
                    public void onDialogYes() {
                        reviewDeletePosition = position;
                        ReviewModel deleteReviewModel = (ReviewModel) object;

                        ReqReviewModel reqReviewModel = new ReqReviewModel();
                        reqReviewModel.userRevwId = deleteReviewModel.userRevwId;

                        if (reviewListType.equals(REVIEW_LIST_TYPE_DELRIVERY)) {
                            reqReviewModel.reviewCategory = MoaConstants.PARAM_VALUE_DELIVERY;
                        } else if (reviewListType.equals(REVIEW_LIST_TYPE_EAT_OUT)) {
                            reqReviewModel.reviewCategory = MoaConstants.PARAM_VALUE_EATOUT;
                        }
                        requestDeleteReview(reqReviewModel);
                        reviewDeleteDialog.dismiss();
                    }
                });
                reviewDeleteDialog.setDismissListener(new YesOrNoDialog.DismissListener() {
                    @Override
                    public void onDismiss() {
                        reviewDeleteDialog.dismiss();
                    }
                });
                reviewDeleteDialog.show(getFragmentManager(), "reviewDeleteDialog");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("requestCode " + requestCode + " resultCode " + resultCode);

        //리뷰 작성후 리스트 갱신
        if (requestCode == MoaConstants.REQUEST_REVIEW_WRITE) {
            if (resultCode == MoaConstants.RESULT_REVIEW_WRITE_SUCCESS) {
                reviewListItemChanged = true;
                if(getActivity() instanceof StoreDetailActivity){
                    ((StoreDetailActivity)getActivity()).setStoreInfoChanged(true);
                }
                requestReviewList();
            }

        } else if (requestCode == MoaConstants.REQUEST_REVIEW_MODIFY) {
            if (resultCode == MoaConstants.RESULT_REVIEW_MODIFY_SUCCESS) {
                reviewListItemChanged = true;
                if(getActivity() instanceof StoreDetailActivity){
                    ((StoreDetailActivity)getActivity()).setStoreInfoChanged(true);
                }
                //requestReviewList();
//                if(reviewAdapterPosition != -1){
//                    Intent intent = new Intent();
//                    intent.putExtra(EXTRA_REVIEW_ADAPTER_POSITION, reviewAdapterPosition);
//                    intent.putExtra(EXTRA_REVIEW_MODIFY_RESULT_MODEL, new Gson().toJson(model.reviewModel));
//                    setResult(MoaConstants.RESULT_REVIEW_MODIFY_SUCCESS, intent);
//                    finish();
//                }
                if(data != null){
                    int modifyPosition = data.getIntExtra(ReviewModifyActivity.EXTRA_REVIEW_ADAPTER_POSITION, -1);
                    ReviewModel reviewModel = new Gson().fromJson(data.getStringExtra(ReviewModifyActivity.EXTRA_REVIEW_MODIFY_RESULT_MODEL), ReviewModel.class);
                    reviewAdapter.modifyReviewItem(modifyPosition, reviewModel);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onRetrofitSuccess(int type, BaseModel baseModel) {
        if (getActivity() != null && !getActivity().isFinishing()) {

            if (type == reviewController.REQUEST_REVIEW_EAT_OUT || type == reviewController.REQUEST_REVIEW_STORE) {
                resReviewListModel = (ResReviewListModel) baseModel;
                if (reviewAdapter != null) {
                    if (resReviewListModel.reviewHeaderInfoModel != null) {
                        reviewAdapter.setReviewHeaderInfoModel(resReviewListModel.reviewHeaderInfoModel);
                    }
                    if (resReviewListModel.reviewList != null) {
                        reviewAdapter.setReviewList(resReviewListModel.reviewList);
                    }
                }

                if(isShowMyWriting){
                    StringBuilder builder = new StringBuilder();
                    builder.append(getString(R.string.review_write_count_header));
                    builder.append(" ");
                    builder.append(String.valueOf(resReviewListModel.reviewCnt));
                    tvReviewWriteCountText.setText(builder);
                }

            } else if (type == reviewController.REQUEST_REVIEW_DELETE) {
                reviewListItemChanged = true;
                Toast.makeText(getActivity(), getString(R.string.common_toast_msg_review_delete_success), Toast.LENGTH_SHORT).show();
                reviewAdapter.removeReviewItem(reviewDeletePosition);
            }

            if (resReviewListModel.reviewList != null) {
                if (resReviewListModel.reviewList.size() > 0) {
                    reviewListEmptyContainer.setVisibility(View.GONE);
                } else {
                    reviewListEmptyContainer.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onRetrofitFail(int type, String msg) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isReviewListItemChanged() {
        return reviewListItemChanged;
    }
}
