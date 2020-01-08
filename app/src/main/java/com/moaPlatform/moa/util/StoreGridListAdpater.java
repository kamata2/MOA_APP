package com.moaPlatform.moa.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.delivery_menu.store_detail.model.ResStoreDetailInfo;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.interfaces.ViewDataInitHelper;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.StoreInfoModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreGridListAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList baseList;
    private ListItemClickListener listItemClickListener;

    // 기멩점 광고
    private final int STORE_AD_INFO_TYPE = 0;
    // 가맹점 대표 메뉴
    private final int STORE_REPRESENTATIVE_MENU_TYPE = 1;
    // 가맹점 전체 메뉴
    private final int STORE_ALL_MENU_TYPE = 2;
    private int startAllmenuPosition = -1;

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public void setStartAllmenuPosition(int startAllmenuPosition) {
        this.startAllmenuPosition = startAllmenuPosition;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_grid_item, parent, false);
        switch (viewType) {
            case STORE_AD_INFO_TYPE:
                return new StoreAdvertisingHolder(view);
            case STORE_REPRESENTATIVE_MENU_TYPE:
                return new StoreRepresentativeMenuHolder(view);
            case STORE_ALL_MENU_TYPE:
                return new StoreAllMenuHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StoreAdvertisingHolder) {
            ((StoreAdvertisingHolder) holder).init((StoreInfoModel) baseList.get(position));
            holder.itemView.setOnClickListener(v -> listItemClickListener.clickItem(CodeTypeManager.StoreInfo.AD_STORE_INFO.getType(), position, (StoreInfoModel) baseList.get(position)));
        } else if (holder instanceof StoreRepresentativeMenuHolder) {
            ((StoreRepresentativeMenuHolder) holder).init((ResStoreDetailInfo.StoreMenuInfoModel) baseList.get(position), position);
            holder.itemView.setOnClickListener(v -> listItemClickListener.clickItem(CodeTypeManager.StoreInfo.STORE_MENU_SELECT.getType(), position, (ResStoreDetailInfo.StoreMenuInfoModel) baseList.get(position)));
        } else {
            StoreAllMenuHolder storeAllMenuHolder = ((StoreAllMenuHolder) holder);
            if (baseList.get(position) instanceof ResStoreDetailInfo.StoreMenuCategoryInfo)
                storeAllMenuHolder.titleInit((ResStoreDetailInfo.StoreMenuCategoryInfo) baseList.get(position));
            else {
                ResStoreDetailInfo.StoreMenuInfoModel storeMenuInfoModel = (ResStoreDetailInfo.StoreMenuInfoModel) baseList.get(position);
                storeAllMenuHolder.menuInit(storeMenuInfoModel);
                holder.itemView.setOnClickListener(v -> listItemClickListener.clickItem(CodeTypeManager.StoreInfo.STORE_MENU_SELECT.getType(), position, storeMenuInfoModel));
            }
        }
    }

    @Override
    public int getItemCount() {
        return baseList != null ? baseList.size() : 0;
    }

    public void listUpdate(ArrayList adStoreList) {
        this.baseList = adStoreList;
    }

    @Override
    public int getItemViewType(int position) {
        if (baseList.get(0) instanceof StoreInfoModel)
            return STORE_AD_INFO_TYPE;
        else if (baseList.get(0) instanceof ResStoreDetailInfo.StoreMenuInfoModel && position < startAllmenuPosition)
            return STORE_REPRESENTATIVE_MENU_TYPE;
        else
            return STORE_ALL_MENU_TYPE;
    }

    // 광고 상품 holder
    class StoreAdvertisingHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.thumbNail)
        ImageView thumbNail;
        @BindView(R.id.storeName)
        TextView storeName;
        @BindView(R.id.ratingPoint)
        TextView ratingPoint;
        @BindView(R.id.adStoreKeyword)
        TextView adStoreKeyword;
        @BindView(R.id.adHowFood)
        ConstraintLayout adHowFood;

        StoreAdvertisingHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void init(StoreInfoModel adStoreInfo) {
            adHowFood.setVisibility(View.VISIBLE);
            try {
                storeName.setText(adStoreInfo.storeName);
                ratingPoint.setText(String.valueOf(adStoreInfo.ratingPoint));
                adStoreKeyword.setText(adStoreInfo.keyWord);
                imageInit(itemView.getContext(), thumbNail, adStoreInfo.storeThumbnail);
            } catch (Exception e) {
                Logger.e(e.toString());
            }
        }

    }

    // 가맹점 대표 메뉴 holder
    class StoreRepresentativeMenuHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.viewStoreRepresentativeMenu)
        LinearLayout contentView;
        @BindView(R.id.thumbNail)
        ImageView thumbNail;
        @BindView(R.id.tvStoreMenuName)
        TextView tvStoreMenuName;
        @BindView(R.id.tvStoreMenuPrice)
        TextView tvStoreMenuPrice;

        private float imageWidth;
        private float imageHeight;
        private float marginLeft;
        float screenWidth;
        float horizontalMarginSumWidth;
        final int LIST_ROW_COLS = 2;

        private LinearLayout.LayoutParams layoutParams;
        private LinearLayout.LayoutParams contentViewLayoutParams;

        StoreRepresentativeMenuHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            screenWidth = DeviceUtil.getScreenWidth(itemView.getContext());
            horizontalMarginSumWidth = itemView.getContext().getResources().getDimension(R.dimen.item_common_grid_margin) * 3;
            imageWidth = ((screenWidth - horizontalMarginSumWidth) /  LIST_ROW_COLS);
            imageHeight = (float) (imageWidth * 1.02);
            marginLeft =  itemView.getContext().getResources().getDimension(R.dimen.item_common_grid_margin);
        }

        public void init(ResStoreDetailInfo.StoreMenuInfoModel storeMenuInfoModel, int position) {
            contentView.setVisibility(View.VISIBLE);
            try {
                tvStoreMenuName.setText(storeMenuInfoModel.productName);
                String priceText = itemView.getResources().getString(R.string.store_product_total_money, StringUtil.convertCommaPrice(storeMenuInfoModel.productPrice));
                tvStoreMenuPrice.setText(priceText);

                layoutParams = (LinearLayout.LayoutParams) thumbNail.getLayoutParams();
                contentViewLayoutParams = (LinearLayout.LayoutParams) contentView.getLayoutParams();
                layoutParams.width = (int) imageWidth;
                contentViewLayoutParams.width = (int) imageWidth;
                layoutParams.height = (int) imageHeight;

                if (position % 2 == 0 ) {
                    layoutParams.leftMargin = (int) marginLeft;
                    contentViewLayoutParams.leftMargin = (int) marginLeft;
                } else {
                    layoutParams.leftMargin = (int) marginLeft/2;
                    contentViewLayoutParams.leftMargin = (int) marginLeft/2;
                }

                imageInit(itemView.getContext(), thumbNail, storeMenuInfoModel.productThumbNail);

            } catch (Exception e) {
                Logger.e("가맹점 대표 메뉴 세팅 에러 : " + e.toString());
            }
        }
    }

    // 가맹점 전체 메뉴 holder
    class StoreAllMenuHolder extends RecyclerView.ViewHolder implements ViewDataInitHelper {
        private LinearLayout cardView, viewAllMenuTitle;
        private ConstraintLayout viewAllMenu;
        private TextView menuCategoryName, menuName, menuPrice, menu_explain;
        private ViewDataInitHelper viewDataInitHelper = this;

        StoreAllMenuHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setVisibility(View.GONE);
            viewAllMenuTitle = itemView.findViewById(R.id.viewAllMenuTitle);
            viewAllMenu = itemView.findViewById(R.id.viewAllMenu);
            menuCategoryName = itemView.findViewById(R.id.menuCategoryName);
            menuName = itemView.findViewById(R.id.menuName);
            menuPrice = itemView.findViewById(R.id.menuPrice);
            menu_explain = itemView.findViewById(R.id.menu_explain);
        }

        void titleInit(ResStoreDetailInfo.StoreMenuCategoryInfo storeMenuCategoryInfo) {
            viewAllMenuTitle.setVisibility(View.VISIBLE);
            viewAllMenu.setVisibility(View.GONE);
            menuCategoryName.setText(storeMenuCategoryInfo.categoryName);
        }

        void menuInit(ResStoreDetailInfo.StoreMenuInfoModel storeMenuInfoModel) {
            viewAllMenuTitle.setVisibility(View.GONE);
            viewAllMenu.setVisibility(View.VISIBLE);
            if (storeMenuInfoModel.productExplain != null && storeMenuInfoModel.productExplain.length() != 0) {
                menu_explain.setVisibility(View.VISIBLE);
                menu_explain.setText(storeMenuInfoModel.productExplain);
            } else {
                menu_explain.setVisibility(View.GONE);
            }
            menuName.setText(storeMenuInfoModel.productName);
            menuPrice.setText(viewDataInitHelper.commaUnitChange(storeMenuInfoModel.productPrice, R.string.store_product_total_money, itemView));

        }

    }

    private void imageInit(Context context, ImageView lv, String imgUrl) {
        Glide.with(context)
                .load(BuildConfig.FILE_SERVER_BASE_URL + imgUrl)
                .into(lv);
    }
}
