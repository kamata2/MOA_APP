package com.moaPlatform.moa.side_menu.coupon;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moaPlatform.moa.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

class CouponHolder extends RecyclerView.ViewHolder {

    TextView couponUsed, couponTitle, couponDay, couponPrice, rstrcVal, couponDiscount;
    ConstraintLayout coupon_used, coupon_hide;
    ImageView arrowDown, arrow;

    CouponHolder(@NonNull View itemView) {
        super(itemView);
        arrow = itemView.findViewById(R.id.arrow_up2);
        arrowDown = itemView.findViewById(R.id.arrow_down2);
        coupon_used = itemView.findViewById(R.id.coupon_use_way);
        couponUsed = itemView.findViewById(R.id.used);
        couponTitle = itemView.findViewById(R.id.couponTitle);
        couponDay = itemView.findViewById(R.id.couponDay);
        couponPrice = itemView.findViewById(R.id.couponPrice);
        rstrcVal = itemView.findViewById(R.id.couponLimite);
        coupon_hide = itemView.findViewById(R.id.coupon_hide);
        couponDiscount = itemView.findViewById(R.id.coupon_price_discount);
    }
}
