package com.moaPlatform.moa.online_payment.order_payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.online_payment.order_payment.model.res.ResPaymentResult;
import com.moaPlatform.moa.util.interfaces.ViewDataInitHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PaymentDialog extends DialogFragment implements ViewDataInitHelper {

    private ResPaymentResult resPaymentResult;
    private View view;
    // 현장 결제 유무
    private boolean isSitePayment = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getActivity() instanceof OrderPaymentActivity) {
            resPaymentResult = new Gson().fromJson(getArguments().getString("orderInfo"), ResPaymentResult.class);
            OrderPaymentActivity orderPaymentActivity = (OrderPaymentActivity)getActivity();
            isSitePayment = getArguments().getBoolean(orderPaymentActivity.KEY_IS_SITE_PAYMENT);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);
        view = inflater.inflate(R.layout.payment_success_dialog, container, false);
        viewInit();
        return view;
    }


    private void viewInit() {
        TextView tvPaymentSuccessTitle = view.findViewById(R.id.tvPaymentSuccessTitle);
        tvPaymentSuccessTitle.setText(isSitePayment ? "주문 금액 정보" : "결제 금액 정보");
        TextView tvPaymentSuccessTotalPrice = view.findViewById(R.id.tvPaymentSuccessTotalPrice);
        tvPaymentSuccessTotalPrice.setText(isSitePayment ? "주문 금액" : "결제 금액");
        ViewDataInitHelper viewDataInitHelper = this;
        viewDataInitHelper.textViewInit(view, R.id.storeName, resPaymentResult.storeName);
        viewDataInitHelper.textViewInit(view, R.id.tvOrderMenus, resPaymentResult.orderProducts);
        viewDataInitHelper.textViewInitConvertPrice(view, R.id.tvPaymentPrice, R.string.store_product_total_money, resPaymentResult.paymentMoney);
        viewDataInitHelper.textViewInitConvertPrice(view, R.id.tvSavePoint, R.string.store_product_total_money, resPaymentResult.savePoint);
    }
}
