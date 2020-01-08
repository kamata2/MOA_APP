package com.moaPlatform.moa.util.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.model.res_model.ReviewImageModel;
import com.moaPlatform.moa.model.res_model.ReviewModel;
import com.moaPlatform.moa.util.DeviceUtil;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.StringUtil;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * 리뷰 리스트 아이템 뷰
 */
public class CommonReviewItemView extends BaseLinearLayoutView{

    private ReviewItemClickListener reviewItemClickListener;
    public enum REVIEW_ITEM_CLICK {REVIEW_MODIFY, REVIEW_DELETE}

    private boolean isShowOwnerReview;      //사장님 댓글 노출 여부
    private boolean isShowDivider;          //구분선 노출 여부
    private boolean isEatOutView;           //외식 여부

    @BindView(R.id.ivReviewProfile)             ImageView ivProfile;
    @BindView(R.id.tvReviewTitleUserName)       TextView tvUserName;
    @BindView(R.id.tvReviewTitleTime)           TextView tvTitleTime;
    @BindView(R.id.ratingBarReview)             MaterialRatingBar ratingBar;
    @BindView(R.id.llReviewScoreContainer)      LinearLayout llEatoutStoreReviewScoreContainer;
    @BindView(R.id.tvReviewScoreTaste)          TextView tvTaste;       //맛
    @BindView(R.id.tvReviewScoreAmount)         TextView tvAmount;      //양
    @BindView(R.id.tvReviewScoreDeliveryTitle)  TextView tvDeliveryTitle;       //배달 타이틀
    @BindView(R.id.tvReviewScoreDelivery)       TextView tvDelivery;    //배달


    @BindView(R.id.llReviewLikeContainer)   LinearLayout llLikeContainer;
    @BindView(R.id.cbReviewLikeIcon)        CheckBox cbLikeIcon;
    @BindView(R.id.tvReviewLikeCnt)         TextView tvLikeCnt;
    @BindView(R.id.tvReviewContent)         TextView tvContent;
    @BindView(R.id.llReviewVerticalListContainer)    LinearLayout llVerticalListContainer;          //세로 리스트 이미지
    @BindView(R.id.llReviewHorizontalListContainer)  LinearLayout llHorizontalListContainer;        //가로 리스트 이미지

    @BindView(R.id.constrainReviewOwnerContainer)   ConstraintLayout constrainEatOutStoreReviewOwnerContainer;  //사장님 댓글 View Container
    @BindView(R.id.tvReviewOwnerWriteTime)          TextView tvEatOutStoreReviewOwnerWriteTime;         //글 작성 시간
    @BindView(R.id.tvReviewOwnerContent)            TextView tvEatOutStoreReviewOwnerContent;           //글 내용
    @BindView(R.id.rlReviewOptionButtonContainer)   RelativeLayout rlReviewOptionButtonContainer;
    @BindView(R.id.btnReviewModify)                 TextView btnReviewModify;           //수정
    @BindView(R.id.btnReviewDelete)                 TextView btnReviewDelete;           //삭제
    @BindView(R.id.llReviewDivider)                 LinearLayout llEatOutStoreReviewDivider;           //구분선

    public CommonReviewItemView(Context context) {
        super(context);
    }

    public CommonReviewItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonReviewItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int layoutResourceId() {
        return R.layout.common_review;
    }

    @Override
    void initViews() {
        llLikeContainer.setVisibility(View.GONE);
        rlReviewOptionButtonContainer.setVisibility(View.GONE);     //default gone 처리
    }

    public void setInitData(ReviewModel reviewModel){

        try{
            if(ObjectUtil.checkNotNull(reviewModel.profImgUrl)){
                Glide.with(this)
                        //.load(App.getInstance().FILE_SERVER_BASE_URL + url)
                        .load(StringUtil.getImageUrl(reviewModel.profImgUrl))
                        .thumbnail(1.0f)
                        .apply(RequestOptions.circleCropTransform())
                        //.apply(new RequestOptions().centerCrop())
                        .into(ivProfile);
            }

            tvUserName.setText(reviewModel.nick);
            tvTitleTime.setText(reviewModel.writDt);
            ratingBar.setRating(reviewModel.totEvalScor);

            if(isEatOutView){
                tvDeliveryTitle.setText(getResources().getString(R.string.review_service_score));
            }else{
                tvDeliveryTitle.setText(getResources().getString(R.string.review_delivery_score));
            }

            tvTaste.setText(String.valueOf(reviewModel.tastEvalScor));
            tvAmount.setText(String.valueOf(reviewModel.volumEvalScor));
            tvDelivery.setText(String.valueOf(reviewModel.dlvryEvalScor));

            tvLikeCnt.setText(String.valueOf(reviewModel.sympCnt));
            tvContent.setText(reviewModel.cntnt);

            llVerticalListContainer.removeAllViews();
            llHorizontalListContainer.removeAllViews();


            llVerticalListContainer.setVisibility(View.VISIBLE);
            llHorizontalListContainer.setVisibility(View.VISIBLE);

            if (ObjectUtil.checkNotNull(reviewModel.userRevwFileList)) {

                final float aspectRow1Type = (float) 2.06;    //큰 이미지 비율
                final float aspectRow2Type = (float) 1.02;    //작은 이미지 비율

                final float screenWidth = DeviceUtil.getScreenWidth(context);
                final float reviewHorizontalMargin = context.getResources().getDimension(R.dimen.item_store_review_horizontal) * 2;   //좌우 여백
                final float imageAcrossMargin = context.getResources().getDimension(R.dimen.item_store_review_across_margin);         //이미지 사이 여백

                final float bigPictureTypeWidth = screenWidth - reviewHorizontalMargin;
                final float bigPictureTypeHeight = bigPictureTypeWidth / aspectRow1Type;
                final float smallPictureTypeWidth = (bigPictureTypeWidth - imageAcrossMargin) / 2;
                final float smallPictureTypeHeight = smallPictureTypeWidth / aspectRow2Type;

                LinearLayout.LayoutParams bigPictureParams1 = new LayoutParams((int) bigPictureTypeWidth, (int) bigPictureTypeHeight);
                LinearLayout.LayoutParams bigPictureParams2 = new LayoutParams((int) bigPictureTypeWidth, (int) bigPictureTypeHeight);
                LinearLayout.LayoutParams smallPictureParams1 = new LayoutParams((int) smallPictureTypeWidth, (int) smallPictureTypeHeight);
                LinearLayout.LayoutParams smallPictureParams2 = new LayoutParams((int) smallPictureTypeWidth, (int) smallPictureTypeHeight);

                bigPictureParams2.topMargin = (int) context.getResources().getDimension(R.dimen.item_store_review_across_margin);

                smallPictureParams1.topMargin = (int) context.getResources().getDimension(R.dimen.item_store_review_across_margin);
                smallPictureParams2.topMargin = (int) context.getResources().getDimension(R.dimen.item_store_review_across_margin);
                smallPictureParams2.leftMargin = (int) context.getResources().getDimension(R.dimen.item_store_review_across_margin);

                if (reviewModel.userRevwFileList.size() == 1) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setLayoutParams(bigPictureParams1);

                    Glide.with(this)
                            .load(StringUtil.getImageUrl(reviewModel.userRevwFileList.get(0).imageUrl))
                            .thumbnail(0.1f)
                            .apply(new RequestOptions().centerCrop())
                            .into(imageView);

                    llVerticalListContainer.addView(imageView);

                } else if (reviewModel.userRevwFileList.size() == 2) {
                    //이미지가 두장일시 세로 형태로
                    for (int i = 0; i < reviewModel.userRevwFileList.size(); i++) {
                        ReviewImageModel model = reviewModel.userRevwFileList.get(i);
                        ImageView imageView = new ImageView(getContext());
                        if (i == 0) {
                            imageView.setLayoutParams(bigPictureParams1);
                        } else {
                            imageView.setLayoutParams(bigPictureParams2);
                        }
                        Glide.with(this)
                                .load(StringUtil.getImageUrl(model.imageUrl))
                                .thumbnail(1.0f)
                                .apply(new RequestOptions().centerCrop())
                                .into(imageView);
                        llVerticalListContainer.addView(imageView);
                    }
                } else if (reviewModel.userRevwFileList.size() == 3) {
                    //이미지가 세장일시 1열 한장 2열 두장
                    ImageView bigPictureImageView = new ImageView(getContext());
                    bigPictureImageView.setLayoutParams(bigPictureParams1);

                    Glide.with(this)
                            .load(StringUtil.getImageUrl(reviewModel.userRevwFileList.get(0).imageUrl))
                            .thumbnail(1.0f)
                            .apply(new RequestOptions().centerCrop())
                            .into(bigPictureImageView);
                    llVerticalListContainer.addView(bigPictureImageView);

                    for (int i = 1; i < reviewModel.userRevwFileList.size(); i++) {
                        ReviewImageModel model = reviewModel.userRevwFileList.get(i);
                        ImageView smallPictureImageView = new ImageView(getContext());
                        if (i == 1) {
                            smallPictureImageView.setLayoutParams(smallPictureParams1);
                        } else {
                            smallPictureImageView.setLayoutParams(smallPictureParams2);
                        }
                        Glide.with(this)
                                .load(StringUtil.getImageUrl(model.imageUrl))
                                .thumbnail(1.0f)
                                .apply(new RequestOptions().centerCrop())
                                .into(smallPictureImageView);
                        llHorizontalListContainer.addView(smallPictureImageView);
                    }
                }
            }else{
                llVerticalListContainer.setVisibility(View.GONE);
                llHorizontalListContainer.setVisibility(View.GONE);
            }

            //내가 쓴글이면 수정&삭제 버튼 노출 처리
            String userId = reviewModel.userId;
            if(MoaAuthHelper.getInstance().getBasePrimaryInfo().equals(userId)){
                rlReviewOptionButtonContainer.setVisibility(View.VISIBLE);

                btnReviewModify.setOnClickListener(v -> {
                    if(reviewItemClickListener != null){
                        reviewItemClickListener.onReviewItemClick(REVIEW_ITEM_CLICK.REVIEW_MODIFY);
                    }
                });

                btnReviewDelete.setOnClickListener(v -> {
                    if(reviewItemClickListener != null){
                        reviewItemClickListener.onReviewItemClick(REVIEW_ITEM_CLICK.REVIEW_DELETE);
                    }
                });

            }else{
                rlReviewOptionButtonContainer.setVisibility(View.GONE);
            }

            //구분선 노출 처리
            if(isShowDivider){
                llEatOutStoreReviewDivider.setVisibility(View.VISIBLE);
            }else{
                llEatOutStoreReviewDivider.setVisibility(View.GONE);
            }

            //사장님 댓글 UI 노출 처리
            if(isShowOwnerReview && ObjectUtil.checkNotNull(reviewModel.cmntWritDt) && ObjectUtil.checkNotNull(reviewModel.affiStorCmnt)){
                constrainEatOutStoreReviewOwnerContainer.setVisibility(View.VISIBLE);
                tvEatOutStoreReviewOwnerWriteTime.setText(reviewModel.cmntWritDt);
                tvEatOutStoreReviewOwnerContent.setText(reviewModel.affiStorCmnt);
            }else{
                constrainEatOutStoreReviewOwnerContainer.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setShowOwnerReview(boolean showOwnerReview) {
        isShowOwnerReview = showOwnerReview;
    }

    public void setShowDivider(boolean showDivider) {
        isShowDivider = showDivider;
    }

    public void setEatOutView(boolean eatOutView) {
        isEatOutView = eatOutView;
    }

    public interface ReviewItemClickListener{
        void onReviewItemClick(REVIEW_ITEM_CLICK clickItem);
    }
    public void setReviewItemClickListener(ReviewItemClickListener reviewItemClickListener) {
        this.reviewItemClickListener = reviewItemClickListener;
    }

    /**
     * 수정 삭제 버튼 노출 여부
     * @param isShow
     */
    public void isShowOptionButton(boolean isShow) {
        if(isShow){
            rlReviewOptionButtonContainer.setVisibility(View.VISIBLE);
        }else{
            rlReviewOptionButtonContainer.setVisibility(View.GONE);
        }
    }
}
