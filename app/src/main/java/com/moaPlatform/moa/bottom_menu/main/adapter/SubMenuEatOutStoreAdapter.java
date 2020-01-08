package com.moaPlatform.moa.bottom_menu.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.main.model.EatOutStoreModel;
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

public class SubMenuEatOutStoreAdapter extends RecyclerView.Adapter<SubMenuEatOutStoreAdapter.StoreInfoViewHolder>{

    private final int LIST_ROW_COLS = 2;

    private Context context;
    private List<EatOutStoreModel> storeList;
    private ListItemClickListener listItemClickListener;

    private float imageWidth;
    private float imageHeight;
    private float marginLeft;

    public SubMenuEatOutStoreAdapter(Context context, ListItemClickListener listener) {
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
    public StoreInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_menu_eat_out_store, parent, false);
        return new StoreInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreInfoViewHolder holder, int position) {
        EatOutStoreModel eatOutStoreModel = storeList.get(position);
        holder.init(eatOutStoreModel, position);
        holder.itemView.setOnClickListener((View view) -> {
            if(listItemClickListener != null){
                listItemClickListener.clickItem(EatOutConstants.LIST_CLICK_LISTENER_TYPE.EATOUT_LIST_STORE.getType(), position, eatOutStoreModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeList == null ? 0 : storeList.size();
    }

    class StoreInfoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivSubMenuEatOutStoreThumb)           public ImageView ivThumb;
        @BindView(R.id.tvSubMenuEatOutStoreScore)           public TextView tvScore;
        @BindView(R.id.tvSubMenuEatOutStoreDistance)        public TextView tvDistance;
        @BindView(R.id.tvSubMenuEatOutStoreName)            public TextView tvName;
        @BindView(R.id.tvSubMenuEatOutStoreTagAddress)      public TextView tvTagAndAddr;
        @BindView(R.id.tvSubMenuEatOutStoreLikeCntText)     public TextView tvLikeCnt;
        @BindView(R.id.tvSubMenuEatOutStoreVisitCntText)    public TextView tvVisitCnt;
        @BindView(R.id.tvSubMenuEatOutStoreReviewCnt)       public TextView tvReviewCnt;
        @BindView(R.id.viewSubMenuEatOutStoreInfoTopDivider)    public View viewDivider;


        private ConstraintLayout.LayoutParams layoutParams;
        private ConstraintLayout.LayoutParams viewDividerParams;


        StoreInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            layoutParams = (ConstraintLayout.LayoutParams) ivThumb.getLayoutParams();
            layoutParams.width = (int) imageWidth;
            layoutParams.height = (int) imageHeight;

            viewDividerParams = (ConstraintLayout.LayoutParams) viewDivider.getLayoutParams();

        }

        void init(EatOutStoreModel eatOutStoreModel, int position) {

            if (getAdapterPosition() % 2 != 0) {
                layoutParams.leftMargin = (int) marginLeft;
                viewDividerParams.leftMargin = (int) marginLeft;
            }else{
                layoutParams.leftMargin = 0;
            }

            Glide.with(itemView)
                    //.load(App.getInstance().FILE_SERVER_BASE_URL + url)
                    .load(StringUtil.getImageUrl(eatOutStoreModel.imageUrl))
                    .thumbnail(1.0f)
                    //.apply(new RequestOptions().centerCrop())
                    .into(ivThumb);

            try{
                tvScore.setText(eatOutStoreModel.evalScor);
                tvDistance.setText(eatOutStoreModel.distance + "km");
                tvName.setText(eatOutStoreModel.storNm);
                tvTagAndAddr.setText(eatOutStoreModel.category + " / " + eatOutStoreModel.landNumAddr);
                tvLikeCnt.setText(eatOutStoreModel.bmarkCnt);
                tvVisitCnt.setText(eatOutStoreModel.lookUpCnt);
                tvReviewCnt.setText(eatOutStoreModel.storRevwCnt);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void setStoreList(List<EatOutStoreModel> storeList) {
        this.storeList = storeList;
    }

    public List<EatOutStoreModel> getStoreList() {
        return storeList;
    }
}
