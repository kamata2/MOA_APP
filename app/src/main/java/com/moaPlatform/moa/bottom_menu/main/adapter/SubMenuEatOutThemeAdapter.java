package com.moaPlatform.moa.bottom_menu.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.main.model.EatOutThemeModel;
import com.moaPlatform.moa.constants.EatOutConstants;
import com.moaPlatform.moa.util.DeviceUtil;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SubMenuEatOutThemeAdapter extends RecyclerView.Adapter<SubMenuEatOutThemeAdapter.ThemeViewHolder>{

    private final int LIST_ROW_COLS = 2;

    private Context context;

    private List<EatOutThemeModel> themeList;
    private ListItemClickListener listItemClickListener;

    private float imageWidth;
    private float imageHeight;
    private float marginLeft;

    public SubMenuEatOutThemeAdapter(Context context, ListItemClickListener listener){

        this.context = context;
        this.listItemClickListener = listener;

        float screenWidth = DeviceUtil.getScreenWidth(context);
        float horizontalMarginSumWidth = context.getResources().getDimension(R.dimen.item_common_margin_horizontal) * 3;

        imageWidth = ((screenWidth - horizontalMarginSumWidth) /  LIST_ROW_COLS);
        imageHeight = (float) (imageWidth * 1.02);
        marginLeft =  context.getResources().getDimension(R.dimen.item_common_margin_horizontal) / 2;

    }

    @NonNull
    @Override
    public ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_menu_eat_out_theme, parent, false);
        return new ThemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeViewHolder holder, int position) {
        EatOutThemeModel eatOutThemeModel = themeList.get(position);
        holder.init(eatOutThemeModel, position);
        holder.itemView.setOnClickListener((View view) -> {
            if(listItemClickListener != null){
                listItemClickListener.clickItem(EatOutConstants.LIST_CLICK_LISTENER_TYPE.EATOUT_LIST_THEME.getType(), position, eatOutThemeModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return themeList == null ? 0 : themeList.size();
    }

    class ThemeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivSubMenuEatOutThemeThumb)
        public ImageView ivThumb;
        @BindView(R.id.tvSubMenuEatOutThemeTitle)
        public TextView tvTitle;

        private ConstraintLayout.LayoutParams layoutParams;

        ThemeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            layoutParams = (ConstraintLayout.LayoutParams) ivThumb.getLayoutParams();
            layoutParams.width = (int) imageWidth;
            layoutParams.height = (int) imageHeight;
        }

        void init(EatOutThemeModel eatOutThemeModel, int position) {

            if (getAdapterPosition() % 2 != 0) {
                layoutParams.leftMargin = (int) marginLeft;
            }else{
                layoutParams.leftMargin = 0;
            }

            Glide.with(itemView)
                    .load(StringUtil.getImageUrl(eatOutThemeModel.bannerImgUrl))
                    .thumbnail(1.0f)
                    //.apply(new RequestOptions().centerCrop())
                    .into(ivThumb);

            if (eatOutThemeModel.bannerCntnt != null){
                tvTitle.setText(eatOutThemeModel.bannerCntnt);
            }
        }
    }
    public void setThemeList(List<EatOutThemeModel> themeList) {
        this.themeList = themeList;
    }

}
