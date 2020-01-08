package com.moaPlatform.moa.delivery_menu.eatout_store_detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.model.res_model.ReviewHeaderInfoModel;
import com.moaPlatform.moa.model.res_model.ReviewModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.custom_view.CommonReviewHeaderView;
import com.moaPlatform.moa.util.custom_view.CommonReviewItemView;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    public final static int LIST_ITEM_CLICK_REVIEW_WRITE = 9000;
    public final static int LIST_ITEM_CLICK_REVIEW_MODIFY = 9001;
    public final static int LIST_ITEM_CLICK_REVIEW_DELETE = 9002;

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    private ReviewHeaderInfoModel reviewHeaderInfoModel;            //리뷰 상단 정보
    private List<ReviewModel> reviewList = new ArrayList<>();
    private ListItemClickListener listItemClickListener;
    private boolean isEatOutView;               //외식 여부 체크

    public ReviewAdapter(Context context, ListItemClickListener listener) {
        this.context = context;
        this.listItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        RecyclerView.ViewHolder holder;

        if(viewType == TYPE_HEADER){
            view = LayoutInflater.from(context).inflate(R.layout.item_review_header, parent, false);
            holder = new HeaderViewHolder(view);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
            holder = new ReviewViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof HeaderViewHolder){
            ((HeaderViewHolder)holder).init();
            ((HeaderViewHolder)holder).commonReviewHeaderView.constraintReviewHeaderWriteContainer.setOnClickListener((View view) ->{
                if (listItemClickListener != null) {
                    listItemClickListener.clickItem(LIST_ITEM_CLICK_REVIEW_WRITE, position, null);
                }
            });
        }else if(holder instanceof ReviewViewHolder){

            int dataPosition;
            if(reviewHeaderInfoModel != null){
                dataPosition = position - 1;
            }else{
                dataPosition = position;
            }
            int finalDataPosition = dataPosition;

            ReviewModel reviewModel = reviewList.get(finalDataPosition);
            ((ReviewViewHolder)holder).init(reviewModel, finalDataPosition);

            ((ReviewViewHolder)holder).commonReviewItemView.setReviewItemClickListener(clickItem -> {
                switch (clickItem) {
                    case REVIEW_MODIFY:
                        Logger.d("수정버튼 선택");
                        if (listItemClickListener != null) {
                            listItemClickListener.clickItem(LIST_ITEM_CLICK_REVIEW_MODIFY, finalDataPosition, reviewModel);
                        }
                        break;
                    case REVIEW_DELETE:
                        Logger.d("삭제버튼 선택");
                        listItemClickListener.clickItem(LIST_ITEM_CLICK_REVIEW_DELETE, finalDataPosition, reviewModel);
                        break;
                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 && reviewHeaderInfoModel != null){
            return TYPE_HEADER;
        }else{
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if(reviewHeaderInfoModel != null){
            return reviewList.size() + 1;
        }else{
            return reviewList.size();
        }
    }

    /**
     * 리스트뷰 홀더
     */
    class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reviewEatOutStoreReview)
        public CommonReviewItemView commonReviewItemView;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            commonReviewItemView.setEatOutView(isEatOutView);
        }

        void init(ReviewModel reviewModel, int position) {

            //사장님 댓글 노출 처리
            commonReviewItemView.setShowOwnerReview(true);

            int headerSize = 0;
            if(reviewHeaderInfoModel != null){
                headerSize = - 1;
            }

            //구분선 노출 처리
            if (position == getItemCount() - headerSize) {
                commonReviewItemView.setShowDivider(false);
            } else {
                commonReviewItemView.setShowDivider(true);
            }
            commonReviewItemView.setInitData(reviewModel);
        }
    }

    /**
     * 헤더뷰 홀더
     */
    class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reviewHeader)
        CommonReviewHeaderView commonReviewHeaderView;

        HeaderViewHolder(View headerView) {
            super(headerView);
            ButterKnife.bind(this, itemView);
            commonReviewHeaderView.setEatOutView(isEatOutView);
        }

        void init(){
            if(reviewHeaderInfoModel != null){
                commonReviewHeaderView.setInitData(
                        reviewHeaderInfoModel.totEvalScor,
                        reviewHeaderInfoModel.tastEvalScor,
                        reviewHeaderInfoModel.volumEvalScor,
                        reviewHeaderInfoModel.dlvryEvalScor
                );

                if(!isEatOutView){
                    if(reviewHeaderInfoModel.revwRmnngDays > 0){
                        commonReviewHeaderView.setVisibilityReviewWriteButton(true);
                    }else{
                        commonReviewHeaderView.setVisibilityReviewWriteButton(false);
                    }
                }
            }
        }
    }

    public void setReviewList(List<ReviewModel> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    public void setReviewHeaderInfoModel(ReviewHeaderInfoModel reviewHeaderInfoModel) {
        this.reviewHeaderInfoModel = reviewHeaderInfoModel;
    }

    public void setEatOutView(boolean eatOutView) {
        isEatOutView = eatOutView;
    }

    /**
     * 리뷰 삭제
     */
    public void removeReviewItem(int dataPosition){
        if(reviewList != null){
            int adapterPosition;
            if (reviewHeaderInfoModel != null) {
                adapterPosition = dataPosition + 1;
            } else {
                adapterPosition = dataPosition;
            }
            Logger.d("removeReviewItem >>>>>> adapterPosition >>> " + adapterPosition);
            reviewList.remove(dataPosition);
            notifyItemRemoved(adapterPosition);
            notifyItemRangeChanged(adapterPosition, getItemCount());
        }
    }

    public void modifyReviewItem(int dataPosition, ReviewModel model){

        if (reviewList != null && dataPosition != -1) {
            reviewList.set(dataPosition, model);

            int adapterPosition;
            if (reviewHeaderInfoModel != null) {
                adapterPosition = dataPosition + 1;
            } else {
                adapterPosition = dataPosition;
            }
            Logger.d("modifyReviewItem >>>>>> adapterPosition >>> " + adapterPosition);
            notifyItemChanged(adapterPosition);
        }
    }
}
