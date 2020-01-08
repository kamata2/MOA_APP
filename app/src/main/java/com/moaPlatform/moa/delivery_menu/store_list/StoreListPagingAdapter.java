package com.moaPlatform.moa.delivery_menu.store_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.TimeEventHelper;
import com.moaPlatform.moa.util.custom_view.CommonTimeEventView;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.models.StoreInfoModel;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreListPagingAdapter extends PagedListAdapter<StoreInfoModel, StoreListPagingAdapter.StoreListHolder> {

    private ListItemClickListener listItemClickListener;
    private int defaultStoreStartPos = -1;
    private boolean onlyDefaultList = false;
    private View nofityView;

    StoreListPagingAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public StoreListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_list_item, parent, false);
        return new StoreListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreListHolder holder, int position) {
        holder.init(getItem(position), position);
        holder.clStoreListContent.setOnClickListener(v -> {
            listItemClickListener.clickItem(0, position, getItem(position));
            if (nofityView != null)
                nofityView.setVisibility(View.GONE);
        });
        holder.viewAdQuestion.setOnClickListener(v -> {
            if (nofityView == null)
                nofityView = holder.viewAdStoreNotify;
            nofityView.setVisibility(nofityView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });
    }

    private static DiffUtil.ItemCallback<StoreInfoModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<StoreInfoModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull StoreInfoModel oldItem, @NonNull StoreInfoModel newItem) {
            return oldItem.storeId == newItem.storeId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull StoreInfoModel oldItem, @NonNull StoreInfoModel newItem) {
            return oldItem.equals(newItem);
        }
    };

    public void onlyDefaultList() {
        onlyDefaultList = true;
    }

    class StoreListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvbar)
        LinearLayout tv_Bar;
        @BindView(R.id.ivThumbNail)
        ImageView ivThumbNail;
        @BindView(R.id.viewHead)
        ConstraintLayout viewHead;
        @BindView(R.id.viewAdQuestion)
        LinearLayout viewAdQuestion;
        @BindView(R.id.tvHeaderTitle)
        TextView tvHeaderTitle;
        @BindView(R.id.bestStoreIc)
        ImageView bestStoreIc;
        @BindView(R.id.tvStoreName)
        TextView tvStoreName;
        @BindView(R.id.tvSaveRate)
        TextView tvSaveRate;
        @BindView(R.id.tvRatingPoint)
        TextView tvRatingPoint;
        @BindView(R.id.tvReview)
        TextView tvReview;
        @BindView(R.id.tvBookMark)
        TextView tvBookMark;
        @BindView(R.id.tvTakeOut)
        TextView tvTakeOut;
        @BindView(R.id.tvCoupon)
        TextView tvCoupon;
        @BindView(R.id.viewAdStoreNotify)
        ConstraintLayout viewAdStoreNotify;
        @BindView(R.id.viewThumbNailMask)
        View viewThumbNailMask;
        @BindView(R.id.tvNotOpen)
        TextView tvNotOpen;
        @BindView(R.id.clStoreListContent)
        View clStoreListContent;
        private CommonTimeEventView viewStoreTimeEventOne, viewStoreTimeEventTwo, viewStoreTimeEventThree, viewStoreTimeEventFour;

        StoreListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            viewStoreTimeEventOne = itemView.findViewById(R.id.viewStoreTimeEventOne);
            viewStoreTimeEventTwo = itemView.findViewById(R.id.viewStoreTimeEventTwo);
            viewStoreTimeEventThree = itemView.findViewById(R.id.viewStoreTimeEventThree);
            viewStoreTimeEventFour = itemView.findViewById(R.id.viewStoreTimeEventFour);
        }

        public void init(StoreInfoModel storeInfoModel, int position) {
            tv_Bar.setVisibility(View.GONE);
            if (!onlyDefaultList) {
                if (defaultStoreStartPos == -1 && storeInfoModel.adProductCode.equals("0"))
                    defaultStoreStartPos = position;
                viewHead.setVisibility(View.VISIBLE);
                if (position == defaultStoreStartPos) {
                    tv_Bar.setVisibility(View.VISIBLE);
                    viewAdQuestion.setVisibility(View.GONE);
                    tvHeaderTitle.setText("GOEAT 일반 매장");
                } else if (position == 0) {
                    viewAdQuestion.setVisibility(View.VISIBLE);
                    tvHeaderTitle.setText("GOEAT 플러스");
                } else {
                    viewHead.setVisibility(View.GONE);
                }
            } else {
                defaultStoreStartPos = -1;
                viewHead.setVisibility(View.GONE);
            }
            //Todo 타임 이벤트 문구 임시 숨김 처리 추후 적립률 등등 문구로 변경하여 표시
            if (defaultStoreStartPos == -1 && storeInfoModel.isBestStore.equals("Y") && !onlyDefaultList) {
                tvTakeOut.setTextColor(itemView.getResources().getColor(R.color.white));
//                tvCoupon.setTextColor(itemView.getResources().getColor(R.color.white));
//                tvCoupon.setBackgroundResource(R.drawable.view_best_ad_store_bg);
                tvTakeOut.setBackgroundResource(R.drawable.view_best_ad_store_bg);
            } else {
                tvTakeOut.setTextColor(itemView.getResources().getColor(R.color.grey));
//                tvCoupon.setTextColor(itemView.getResources().getColor(R.color.grey));
//                tvCoupon.setBackgroundResource(R.drawable.view_default_store_bg);
                tvTakeOut.setBackgroundResource(R.drawable.view_default_store_bg);
            }

            if (storeInfoModel.isBestStore.equals("Y"))
                bestStoreIc.setVisibility(View.VISIBLE);
            else
                bestStoreIc.setVisibility(View.INVISIBLE);

            tvStoreName.setText(storeInfoModel.storeName);
            tvSaveRate.setText(itemView.getContext().getString(R.string.store_detail_save_percent, storeInfoModel.saveRate));
            tvRatingPoint.setText(String.valueOf(storeInfoModel.ratingPoint));
            tvReview.setText(itemView.getContext().getString(R.string.store_list_review, storeInfoModel.storeReviewCnt));
            tvBookMark.setText(itemView.getContext().getString(R.string.store_list_bookMark, storeInfoModel.bookmarkCnt));

            tvTakeOut.setVisibility(storeInfoModel.isTakeOut.equals("Y") ? View.VISIBLE : View.GONE);
//            tvCoupon.setVisibility(App.getInstance().gson.toJson(storeInfoModel.timeEventModel).equals("{}") ? View.GONE : View.VISIBLE);

            if (storeInfoModel.isOpen == 1) {
                viewThumbNailMask.setBackgroundResource(R.drawable.store_list_thumbnail_mask_w);
                tvNotOpen.setVisibility(View.GONE);
            } else {
                //Todo 2019-07-01 임실장님께서 오픈 대기여도 오픈대기문구 및 검은색 배경 표시하지말라고 하셨습니다.
                viewThumbNailMask.setBackgroundResource(R.drawable.store_list_thumbnail_mask_w);
                tvNotOpen.setVisibility(View.GONE);
//                viewThumbNailMask.setBackgroundResource(R.drawable.store_list_thumbnail_mask_b);
//                tvNotOpen.setVisibility(View.VISIBLE);
            }

            List<CommonTimeEventView> timeEventViewList = new ArrayList<>();
            timeEventViewList.add(viewStoreTimeEventOne);
            timeEventViewList.add(viewStoreTimeEventTwo);
            timeEventViewList.add(viewStoreTimeEventThree);
            timeEventViewList.add(viewStoreTimeEventFour);

            for (CommonTimeEventView commonTimeEventView : timeEventViewList) {
                commonTimeEventView.setVisibility(View.GONE);
            }

            new TimeEventHelper.TimeEventBuilder(timeEventViewList)
                    .setAddSaveRate(storeInfoModel.timeEventModel.addSaveRate)
                    .setSaveRate(Integer.valueOf(storeInfoModel.saveRate))
                    .setFixedAmount(storeInfoModel.timeEventModel.fixAmt)
                    .setFixedRate(storeInfoModel.timeEventModel.fixRate)
                    .setHideSaveRate(true)
                    .build();

            Glide.with(itemView)
                    .load(BuildConfig.FILE_SERVER_BASE_URL + storeInfoModel.storeThumbnail)
                    .into(ivThumbNail);

        }
    }
}
