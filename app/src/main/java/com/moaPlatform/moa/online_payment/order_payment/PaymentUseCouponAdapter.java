package com.moaPlatform.moa.online_payment.order_payment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.online_payment.order_payment.model.ResUseCouponModel;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentUseCouponAdapter extends RecyclerView.Adapter<PaymentUseCouponAdapter.PaymentUseCouponHolder> {

    private ArrayList<ResUseCouponModel.UseCouponModel> couponList;
    private int selectCouponPos = -1;
    private RadioButton clickRadioButton = null;
    private ListItemClickListener listItemClickListener;

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public void setCouponList(ArrayList<ResUseCouponModel.UseCouponModel> couponList) {
        this.couponList = couponList;
    }

    @NonNull
    @Override
    public PaymentUseCouponHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_use_coupon_item, parent, false);
        return new PaymentUseCouponHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentUseCouponHolder holder, int position) {
        holder.init(couponList.get(position), position);
        if (position == selectCouponPos) {
            holder.rbUseCoupon.setChecked(true);
            clickRadioButton = holder.rbUseCoupon;
        } else {
            holder.rbUseCoupon.setChecked(false);
        }

        holder.rbUseCoupon.setOnClickListener(v -> {
            if (clickRadioButton != null)
                clickRadioButton.setChecked(false);
            selectCouponPos = position;
            clickRadioButton = holder.rbUseCoupon;
            holder.rbUseCoupon.setChecked(true);
            listItemClickListener.clickItem(-1, position, couponList.get(position));
        });

        holder.itemView.setOnClickListener(v -> {
            if (clickRadioButton != null)
                clickRadioButton.setChecked(false);
            selectCouponPos = position;
            clickRadioButton = holder.rbUseCoupon;
            holder.rbUseCoupon.setChecked(true);
            listItemClickListener.clickItem(-1, position, couponList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return couponList == null ? 0 : couponList.size();
    }

    public class PaymentUseCouponHolder extends RecyclerView.ViewHolder{
        private TextView tvCouponTitle, tvCouponTime;
        private ImageView ivUnderLine;
        public RadioButton rbUseCoupon;

        public PaymentUseCouponHolder(@NonNull View itemView) {
            super(itemView);
            tvCouponTitle = itemView.findViewById(R.id.tvCouponTitle);
            tvCouponTime = itemView.findViewById(R.id.tvCouponTime);
            ivUnderLine = itemView.findViewById(R.id.ivUnderLine);
            rbUseCoupon = itemView.findViewById(R.id.rbUseCoupon);
        }

        public void init(ResUseCouponModel.UseCouponModel useCouponModel, int position) {
            tvCouponTitle.setText(useCouponModel.couponTitle);
            String endTime = "유효기간 : " + useCouponModel.endTime + "까지";
            tvCouponTime.setText(endTime);

            if (couponList.size()-1 == position)
                ivUnderLine.setVisibility(View.INVISIBLE);
            else
                ivUnderLine.setVisibility(View.VISIBLE);
        }
    }
}
