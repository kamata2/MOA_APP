package com.moaPlatform.moa.delivery_menu.eatout_store_detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.EatOutStoreProductImageModel;
import com.moaPlatform.moa.util.DeviceUtil;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EatOutStoreDetailImageHorizontalAdapter extends RecyclerView.Adapter<EatOutStoreDetailImageHorizontalAdapter.ThemeViewHolder>{


    private Context context;

    private List<EatOutStoreProductImageModel> imageList;
    private ListItemClickListener listItemClickListener;

    private float imageWidth;
    private float imageHeight;

    public EatOutStoreDetailImageHorizontalAdapter(Context context, ListItemClickListener listener){

        this.context = context;
        this.listItemClickListener = listener;

        float screenWidth = DeviceUtil.getScreenWidth(context);
        imageWidth = (float) (screenWidth / 2.7);
        imageHeight = imageWidth;

    }

    @NonNull
    @Override
    public ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eat_out_store_deatil_horizontal_image, parent, false);
        return new ThemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeViewHolder holder, int position) {
        EatOutStoreProductImageModel model = imageList.get(position);
        holder.init(model, position);
//        holder.itemView.setOnClickListener((View view) -> {
//            if(listItemClickListener != null){
//                listItemClickListener.clickItem(EatOutConstants.LIST_CLICK_LISTENER_TYPE.EATOUT_LIST_THEME.getType(), position, eatOutThemeModel);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return imageList == null ? 0 : imageList.size();
    }

    class ThemeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivEatoutStoreDetailHorizontal)
        public ImageView ivThumb;

        private ConstraintLayout.LayoutParams layoutParams;

        ThemeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            layoutParams = (ConstraintLayout.LayoutParams) ivThumb.getLayoutParams();
            layoutParams.width = (int) imageWidth;
            layoutParams.height = (int) imageHeight;
        }

        void init(EatOutStoreProductImageModel model, int position) {

            Glide.with(itemView)
                    .load(StringUtil.getImageUrl(model.imageUrl))
                    .thumbnail(1.0f)
                    //.apply(new RequestOptions().centerCrop())
                    .into(ivThumb);

        }
    }
    public void setImageList(List<EatOutStoreProductImageModel> list) {
        this.imageList = list;
    }

}
