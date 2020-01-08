package com.moaPlatform.moa.side_menu.order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.activity.ReviewWriteMainActivity;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.side_menu.order.detail.view.OrderDetailHistoryActivity;
import com.moaPlatform.moa.side_menu.order.model.OrderDto;
import com.moaPlatform.moa.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.OrderHistoryHolder> {

    final static String LIST_TYPE_ORDER_TYPE = "LIST_TYPE_ORDER_TYPE";
    final static String LIST_TYPE_CANCEL_TYPE = "LIST_TYPE_CANCEL_TYPE";


    private Context context;
    private String listType;
    private List<OrderDto> orderHistoryList = new ArrayList<>();

    OrderRecyclerViewAdapter(Context context, String orderList) {
        this.context = context;
        this.listType = orderList;
    }

    @NonNull
    @Override
    public OrderHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryHolder holder, int position) {
        final OrderDto orderDto = orderHistoryList.get(position);

        holder.viewOrderHistoryOrderType.orderTypeCheck(orderDto.orderStatCd);

        if (listType.equals(LIST_TYPE_ORDER_TYPE)) {
            if (ObjectUtil.checkNotNull(orderDto.revwWritYn) && orderDto.revwWritYn.equals("N")) {
                holder.orderReview.setVisibility(View.VISIBLE);
                String tempReviewWrite = holder.itemView.getResources().getString(R.string.item_order_history_review_write_date, orderDto.revwRmnngDays);
                holder.tvOrderHistoryReviewWrite.setText(tempReviewWrite);
            } else {
                holder.orderReview.setVisibility(View.GONE);
            }
        } else {
            holder.orderReview.setVisibility(View.GONE);
        }

        Glide.with(context)
                .asBitmap()
                .load(BuildConfig.FILE_SERVER_BASE_URL + orderDto.imageUrl)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(holder.orderImage);

        holder.orderDay.setText(orderDto.orderDt);
        holder.orderStorName.setText(orderDto.storNm);
        holder.orderMenu.setText(orderDto.getOrderMenuList());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(MoaConstants.EXTRA_ORDER_ID, orderDto.orderId);
            context.startActivity(intent);
        });

        holder.orderReview.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewWriteMainActivity.class);
            intent.putExtra(MoaConstants.EXTRA_FROM_VIEW, ReviewWriteMainActivity.FROM_VIEW_STORE_REVIEW);
            intent.putExtra(MoaConstants.EXTRA_STORE_ID, orderDto.storId);
            intent.putExtra(MoaConstants.EXTRA_ORDER_ID, orderDto.orderId);
            intent.putExtra(MoaConstants.EXTRA_STORE_NAME, orderDto.storNm);
            intent.putExtra(ReviewWriteMainActivity.ORDERD_GOODS_LIST, orderDto.getOrderMenuList());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderHistoryList == null ? 0 : orderHistoryList.size();
    }

    public void setData(List<OrderDto> orderDtos) {
        this.orderHistoryList = orderDtos;
    }

    class OrderHistoryHolder extends RecyclerView.ViewHolder {

        ImageView orderImage;
        TextView orderDay, orderStorName, orderMenu, tvOrderHistoryReviewWrite;
        View orderReview;
        OrderCommonOrderTypeView viewOrderHistoryOrderType;

        OrderHistoryHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.ivOrderHistoryStoreThumbnail);
            orderDay = itemView.findViewById(R.id.tvOrderHistoryOrderDate);
            orderStorName = itemView.findViewById(R.id.tvOrderHistoryStoreName);
            orderMenu = itemView.findViewById(R.id.tvOrderHistoryOrderMenuList);
            orderReview = itemView.findViewById(R.id.orderReview);
            tvOrderHistoryReviewWrite = itemView.findViewById(R.id.tvOrderHistoryReviewWrite);
            viewOrderHistoryOrderType = itemView.findViewById(R.id.viewOrderHistoryOrderType);
        }
    }
}
