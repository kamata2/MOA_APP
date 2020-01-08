package com.moaPlatform.moa.side_menu.coupon;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.coupon.model.AvailableCouponsItems;

import java.util.List;

public class CouponRecyclerViewAdapter extends RecyclerView.Adapter<CouponHolder> {
    private boolean isAvailableCoupon;
    private List<AvailableCouponsItems> availableCouponsItems;
    private ConstraintLayout mTt;
    private ImageView arrow, arrowDown;
    private SparseBooleanArray selectedItem = new SparseBooleanArray();
    private int prePosition = -1;

    CouponRecyclerViewAdapter(boolean isAvailableCoupon) {
        this.isAvailableCoupon = isAvailableCoupon;
    }

    @NonNull
    @Override
    public CouponHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.side_coupon_use_newver, parent, false);
        return new CouponHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponHolder holder, int position) {
        final AvailableCouponsItems availableCouponsItems = this.availableCouponsItems.get(position);

        if (isAvailableCoupon) {
            holder.couponUsed.setVisibility(View.VISIBLE);
            holder.couponPrice.setTextColor(holder.itemView.getResources().getColor(R.color.darkGray));
            holder.couponDiscount.setTextColor(holder.itemView.getResources().getColor(R.color.darkGray));
        } else {
            holder.couponUsed.setVisibility(View.GONE);
        }
        holder.couponTitle.setText(availableCouponsItems.title);
        holder.couponDay.setText(availableCouponsItems.endDt);
        holder.couponPrice.setText(availableCouponsItems.applyVal);
        holder.rstrcVal.setText("");

        mTt = holder.coupon_hide;
        arrow = holder.arrow;
        arrowDown = holder.arrowDown;
        changeVisibility(selectedItem.get(position));

        holder.coupon_used.setOnClickListener(v -> {
            if(selectedItem.get(position)) {
                selectedItem.delete(position);
            }else {
                selectedItem.delete(prePosition);
                selectedItem.put(position, true);
            }
            if(prePosition != -1) notifyItemChanged(prePosition);
            notifyItemChanged(position);
            prePosition = position;

        });
    }

    @Override
    public int getItemCount() {
        return availableCouponsItems == null ? 0 : availableCouponsItems.size();
    }

    public void setData(List<AvailableCouponsItems> availableCouponsItems) {
        this.availableCouponsItems = availableCouponsItems;
    }

    private void changeVisibility(final boolean isExpanded) {
        mTt.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        arrowDown.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        arrow.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }
}
