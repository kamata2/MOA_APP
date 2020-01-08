package com.moaPlatform.moa.bottom_menu.main.adapter;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.main.model.ResMainServiceModel;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.manager.CodeTypeManager;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SubMenuListAdapter extends RecyclerView.Adapter<SubMenuListAdapter.SubMenuHolder> {

    // 서브 메뉴 리스트
    private ArrayList<ResMainServiceModel.SubMenuModel> subMenuModels;
    private ListItemClickListener listItemClickListener;


    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public SubMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_sub_menu_item, parent, false);
        return new SubMenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubMenuHolder holder, int position) {
        holder.subMenuInit(subMenuModels.get(position));
        holder.itemView.setOnClickListener(v -> listItemClickListener.clickItem(0, position, subMenuModels.get(position)));

        /**
         * Todo 선황
         */
        holder.subMenuInit(subMenuModels.get(position));
        if (position == 0) {
            holder.itemView.setBackgroundResource(R.drawable.circle_sub_menu_edge_type_one);
        }
        if (position == 3) {
            holder.itemView.setBackgroundResource(R.drawable.circle_sub_menu_edge_type_three);
        }
        if (position == 6) {
            holder.itemView.setBackgroundResource(R.drawable.circle_sub_menu_edge_type_three);
        }
        if (position == 9) {
            holder.itemView.setBackgroundResource(R.drawable.circle_sub_menu_edge_type_five);
        }
        if (position == 2) {
            holder.itemView.setBackgroundResource(R.drawable.circle_sub_menu_edge_type_two);
        }
        if (position == 5) {
            holder.itemView.setBackgroundResource(R.drawable.circle_sub_menu_edge_type_four);
        }
        if (position == 8) {
            holder.itemView.setBackgroundResource(R.drawable.circle_sub_menu_edge_type_four);
        }
        if (position == 1) {
            holder.itemView.setBackgroundResource(R.drawable.circle_sub_menu_edge_type_six);
        }
        if (position == 4) {
            holder.itemView.setBackgroundResource(R.drawable.circle_sub_menu_edge_type_six);
        }
        if (position == 7) {
            holder.itemView.setBackgroundResource(R.drawable.circle_sub_menu_edge_type_six);
        }
        if (position == 10) {
            holder.itemView.setBackgroundResource(R.drawable.circle_sub_menu_edge_type_five);
        }
    }

    @Override
    public int getItemCount() {
        return subMenuModels != null ? subMenuModels.size() : 0;
    }

    /**
     * 리스트 업데이트
     *
     * @param subMenuModels 서버에서 통신해서 받아온 서브 매뉴 모델
     */
    public void listUpdate(ArrayList<ResMainServiceModel.SubMenuModel> subMenuModels) {
        this.subMenuModels = subMenuModels;
        notifyDataSetChanged();
    }


    class SubMenuHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.subMenuTitle)
        TextView subMenuTitle;
        @BindView(R.id.subMenuIcon)
        ImageView subMenuIcon;
        // 베달카테고리의 서브 메뉴 아이콘 저장된 array
        private SparseIntArray deliverySubmenus;

        SubMenuHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            subMenuInit();
        }

        /**
         * 서브메뉴 세팅
         *
         * @param subMenuModel 서브 메뉴 정보가 담긴 모델
         */
        void subMenuInit(ResMainServiceModel.SubMenuModel subMenuModel) {
            subMenuTitle.setText(subMenuModel.subMenuName);
            Glide.with(itemView)
                    .load(deliverySubmenus.get(Integer.parseInt(subMenuModel.subMenuCode)))
                    .into(subMenuIcon);
        }

        /**
         * 베달카테고리의 서브 메뉴 아이콘 세팅
         */
        private void subMenuInit() {
            deliverySubmenus = new SparseIntArray();
            deliverySubmenus.put(CodeTypeManager.MainActivityManager.SUB_MENU_KOREAN.getType(), CodeTypeManager.MainActivityManager.DELIVERY_SUB_MENU_KOREAN_IC.getType());
            deliverySubmenus.put(CodeTypeManager.MainActivityManager.SUB_MENU_CHINESE.getType(), CodeTypeManager.MainActivityManager.DELIVERY_SUB_MENU_CHINESE_IC.getType());
            deliverySubmenus.put(CodeTypeManager.MainActivityManager.SUB_MENU_JP.getType(), CodeTypeManager.MainActivityManager.DELIVERY_SUB_MENU_JP_IC.getType());
            deliverySubmenus.put(CodeTypeManager.MainActivityManager.SUB_MENU_WESTERN.getType(), CodeTypeManager.MainActivityManager.DELIVERY_SUB_MENU_WESTERN_IC.getType());
            deliverySubmenus.put(CodeTypeManager.MainActivityManager.SUB_MENU_CHICKEN.getType(), CodeTypeManager.MainActivityManager.DELIVERY_SUB_MENU_CHICKEN_IC.getType());
            deliverySubmenus.put(CodeTypeManager.MainActivityManager.SUB_MENU_PIG_HOCKS.getType(), CodeTypeManager.MainActivityManager.DELIVERY_SUB_MENU_PIG_HOCKS_IC.getType());
            deliverySubmenus.put(CodeTypeManager.MainActivityManager.SUB_MENU_LATE_NIGHT_SNACK.getType(), CodeTypeManager.MainActivityManager.DELIVERY_SUB_MENU_LATE_NIGHT_SNACK_IC.getType());
            deliverySubmenus.put(CodeTypeManager.MainActivityManager.SUB_MENU_INSTANT_FOOD.getType(), CodeTypeManager.MainActivityManager.DELIVERY_SUB_MENU_INSTANT_FOOD_IC.getType());
            deliverySubmenus.put(CodeTypeManager.MainActivityManager.SUB_MENU_FAST_FOOD.getType(), CodeTypeManager.MainActivityManager.DELIVERY_SUB_MENU_FAST_FOOD_IC.getType());
            deliverySubmenus.put(CodeTypeManager.MainActivityManager.SUB_MENU_CAFE.getType(), CodeTypeManager.MainActivityManager.DELIVERY_SUB_MENU_CAFE_IC.getType());
            deliverySubmenus.put(CodeTypeManager.MainActivityManager.SUB_MENU_FRANCHISE.getType(), CodeTypeManager.MainActivityManager.DELIVERY_SUB_MENU_FRANCHISE_IC.getType());
        }

    }

}
