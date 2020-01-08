package com.moaPlatform.moa.top_menu.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.TimeEventHelper;
import com.moaPlatform.moa.util.custom_view.CommonTimeEventView;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.StoreInfoModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class StoreSearchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList recentlyKeywordList;
    private ListItemClickListener listItemClickListener;
    private int thisViewType = CodeTypeManager.TopToolbarManager.RECENTLY_STORE_SEARCH.getType();
    private HistoryItemClickListener historyItemClickListener;

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (thisViewType == CodeTypeManager.TopToolbarManager.RECENTLY_STORE_SEARCH.getType()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_history_item, parent, false);
            return new StoreSearchHistoryHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_list_item, parent, false);
            return new StoreSearchHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (thisViewType == CodeTypeManager.TopToolbarManager.RECENTLY_STORE_SEARCH.getType()) {
            ((StoreSearchHistoryHolder) holder).init((ResSearchModel.SearchHistory) recentlyKeywordList.get(position));
            ((StoreSearchHistoryHolder) holder).btRemove.setOnClickListener(v -> listItemClickListener.clickItem(
                    CodeTypeManager.TopToolbarManager.RECENTLY_SEARCH_HISTORY_REMOVE.getType(), position, recentlyKeywordList.get(position)));
        } else {
            ((StoreSearchHolder) holder).init((StoreInfoModel) recentlyKeywordList.get(position));
            holder.itemView.setOnClickListener(v -> listItemClickListener.clickItem(CodeTypeManager.TopToolbarManager.STORE_DETAIL_MOVE.getType(), position, recentlyKeywordList.get(position)));
//            holder.itemView.setOnClickListener(v -> listItemClickListener.clickItem(
//                    CodeTypeManager.TopToolbarManager.RECENTLY_SEARCH_HISTORY_REMOVE.getType(), position, recentlyKeywordList.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return recentlyKeywordList != null ? recentlyKeywordList.size() : 0;
    }

    void setRecentlyKeywordList(ArrayList searchStoreList, int thisViewType) {
        this.recentlyKeywordList = searchStoreList;
        this.thisViewType = thisViewType;
        notifyDataSetChanged();
    }

    void removeItem(int removePosition) {
        recentlyKeywordList.remove(removePosition);
        notifyItemRemoved(removePosition);
        notifyItemRangeChanged(removePosition, recentlyKeywordList.size());
    }

    void allRemoveItem() {
        recentlyKeywordList.clear();
        notifyDataSetChanged();
    }

    class StoreSearchHistoryHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout constraintSearchHistoryItemContainer;
        private TextView tvSearchKeyword;
        private View btRemove;

        StoreSearchHistoryHolder(@NonNull View itemView) {
            super(itemView);
            constraintSearchHistoryItemContainer = itemView.findViewById(R.id.constraintSearchHistoryItemContainer);
            tvSearchKeyword = itemView.findViewById(R.id.tvSearchKeyword);
            btRemove = itemView.findViewById(R.id.btRemove);
        }

        void init(ResSearchModel.SearchHistory searchHistoryModel) {
            tvSearchKeyword.setText(searchHistoryModel.keyword);

            constraintSearchHistoryItemContainer.setOnClickListener(v -> {
                if (historyItemClickListener != null) {
                    if (ObjectUtil.checkNotNull(searchHistoryModel.keyword)) {
                        historyItemClickListener.clickItem(searchHistoryModel.keyword);
                    }
                }
            });
        }
    }

    class StoreSearchHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout viewHead;
        private TextView tvCoupon, tvTakeOut, tvStoreName, tvSaveRate, tvRatingPoint, tvReview, tvBookMark;
        private ImageView ivThumbNail;
        private Gson gson;
        private View tvNotOpen;
        private CommonTimeEventView viewStoreTimeEventOne, viewStoreTimeEventTwo, viewStoreTimeEventThree, viewStoreTimeEventFour;

        StoreSearchHolder(@NonNull View itemView) {
            super(itemView);
            gson = new Gson();
            viewHead = itemView.findViewById(R.id.viewHead);
            viewHead.setVisibility(View.GONE);
            tvCoupon = itemView.findViewById(R.id.tvCoupon);
            tvTakeOut = itemView.findViewById(R.id.tvTakeOut);
            tvCoupon.setBackgroundResource(R.drawable.view_default_store_bg);
            tvTakeOut.setBackgroundResource(R.drawable.view_default_store_bg);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            ivThumbNail = itemView.findViewById(R.id.ivThumbNail);
            tvSaveRate = itemView.findViewById(R.id.tvSaveRate);
            tvRatingPoint = itemView.findViewById(R.id.tvRatingPoint);
            tvReview = itemView.findViewById(R.id.tvReview);
            tvBookMark = itemView.findViewById(R.id.tvBookMark);
            tvNotOpen = itemView.findViewById(R.id.tvNotOpen);
            viewStoreTimeEventOne = itemView.findViewById(R.id.viewStoreTimeEventOne);
            viewStoreTimeEventTwo = itemView.findViewById(R.id.viewStoreTimeEventTwo);
            viewStoreTimeEventThree = itemView.findViewById(R.id.viewStoreTimeEventThree);
            viewStoreTimeEventFour = itemView.findViewById(R.id.viewStoreTimeEventFour);
        }

        private void init(StoreInfoModel storeInfoModel) {
            tvStoreName.setText(storeInfoModel.storeName);
            tvSaveRate.setText(itemView.getResources().getString(R.string.store_detail_save_percent, storeInfoModel.saveRate));
            tvRatingPoint.setText(String.valueOf(storeInfoModel.ratingPoint));
            tvReview.setText(itemView.getResources().getString(R.string.store_list_review, storeInfoModel.storeReviewCnt));
            tvBookMark.setText(itemView.getResources().getString(R.string.store_list_bookMark, storeInfoModel.bookmarkCnt));

            tvTakeOut.setVisibility(storeInfoModel.isTakeOut.equals("Y") ? View.VISIBLE : View.GONE);
//            tvCoupon.setVisibility(gson.toJson(storeInfoModel.timeEventModel).equals("{}") ? View.GONE : View.VISIBLE);

            tvNotOpen.setVisibility(View.GONE);


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

    public interface HistoryItemClickListener {
        void clickItem(String keyword);
    }

    public void setHistoryItemClickListener(HistoryItemClickListener historyItemClickListener) {
        this.historyItemClickListener = historyItemClickListener;
    }
}
