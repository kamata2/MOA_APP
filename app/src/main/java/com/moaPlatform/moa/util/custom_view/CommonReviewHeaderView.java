package com.moaPlatform.moa.util.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.moaPlatform.moa.R;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * 리뷰 리스트 헤더 뷰
 */
public class CommonReviewHeaderView extends BaseLinearLayoutView{

    @BindView(R.id.tvReviewHeaderScore)             TextView tvReviewHeaderScore;
    @BindView(R.id.ratingBarReviewHeader)           MaterialRatingBar ratingBarReviewHeader;
    @BindView(R.id.tvReviewHeaderTasteScore)        TextView tvReviewHeaderTasteScore;
    @BindView(R.id.tvReviewHeaderAmountScore)       TextView tvReviewHeaderAmountScore;
    @BindView(R.id.tvReviewHeaderDeliveryTitle)     TextView tvReviewHeaderDeliveryTitle;
    @BindView(R.id.tvReviewHeaderDeliveryScore)     TextView tvReviewHeaderDeliveryScore;
    @BindView(R.id.constraintReviewHeaderWriteContainer) public ConstraintLayout constraintReviewHeaderWriteContainer;       //리뷰 쓰기

    public boolean isEatOutView;

    public CommonReviewHeaderView(Context context) {
        super(context);
    }

    public CommonReviewHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonReviewHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int layoutResourceId() {
        return R.layout.common_review_header;
    }

    @Override
    void initViews() {
    }

    /**
     * @param averageScore  총 평점
     * @param tasteScore    맛 평점
     * @param amountScore   양 평점
     * @param deliveryScore 배달 평점
     */
    public void setInitData(String averageScore, String tasteScore, String amountScore, String deliveryScore){

        try{
            if (isEatOutView) {
                tvReviewHeaderDeliveryTitle.setText(getResources().getString(R.string.review_service_score));
            } else {
                tvReviewHeaderDeliveryTitle.setText(getResources().getString(R.string.review_delivery_score));
            }

            tvReviewHeaderScore.setText(averageScore);
            ratingBarReviewHeader.setRating(Float.parseFloat(averageScore));
            tvReviewHeaderTasteScore.setText(tasteScore);
            tvReviewHeaderAmountScore.setText(amountScore);
            tvReviewHeaderDeliveryScore.setText(deliveryScore);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 리뷰쓰기 버튼 노출 여부
     * @param isShow
     */
    public void setVisibilityReviewWriteButton(boolean isShow){
        if (isShow) {
            constraintReviewHeaderWriteContainer.setVisibility(View.VISIBLE);
        } else {
            constraintReviewHeaderWriteContainer.setVisibility(View.GONE);
        }
    }

    public void setEatOutView(boolean eatOutView) {
        isEatOutView = eatOutView;
    }
}
