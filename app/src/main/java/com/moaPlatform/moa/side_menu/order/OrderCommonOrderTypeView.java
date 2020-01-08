package com.moaPlatform.moa.side_menu.order;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.moaPlatform.moa.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by jiwun on 2019-07-02.
 * 사이드 메뉴의 주문 내역, 주문 내역 상세해서 주문 타입 표시할때사용
 */
public class OrderCommonOrderTypeView extends LinearLayout {
    private Context context;

    public OrderCommonOrderTypeView(Context context) {
        super(context);
        viewInit(context);
    }

    public OrderCommonOrderTypeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        viewInit(context);
    }

    public OrderCommonOrderTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewInit(context);
    }

    private void viewInit(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_common_order_history_order_type, this, false);
        addView(view);
    }

    public void orderTypeCheck(String orderType) {
        TextView tvCommonOrderType = findViewById(R.id.tvCommonOrderType);
        // 주문 타입 코드값
        // 주문 대기
        final String ORDER_TYPE_ORDER_WAIT = "00";
        // 주문 완료
        final String ORDER_TYPE_ORDER_RECEPTION = "01";
        // 배달 완료
        final String ORDER_TYPE_DELIVERY_RECEPTION = "03";
        // 포장 판매 완료
        final String ORDER_TYPE_TAKE_OUT_RECEPTION = "04";
        // 고객 주문 취소
        final String ORDER_TYPE_CLIENT_CANCEL = "11";
        // 매장 주문 취소
        final String ORDER_TYPE_STORE_CANCEL = "12";

        int stringResId = 0;
        int colorResId = 0;
        int bgResId = 0;

        switch (orderType) {
            case ORDER_TYPE_ORDER_WAIT:
                // 주문 대기
                stringResId = R.string.item_order_history_order_type_order_wait;
                colorResId = R.color.item_order_history_order_type_wait;
                bgResId = R.drawable.shape_order_history_order_type_order_wait;
                break;
            case ORDER_TYPE_ORDER_RECEPTION:
                stringResId = R.string.item_order_history_order_type_order_reception;
                colorResId = R.color.coral;
                bgResId = R.drawable.shape_order_history_order_type_order_reception;
                break;
            case ORDER_TYPE_DELIVERY_RECEPTION:
                stringResId = R.string.item_order_history_order_type_delivery_reception;
                colorResId = R.color.dimGray;
                bgResId = R.drawable.shape_order_history_order_type_delivery_reception;
                break;
            case ORDER_TYPE_TAKE_OUT_RECEPTION:
                stringResId = R.string.item_order_history_order_type_take_out_reception;
                colorResId = R.color.dimGray;
                bgResId = R.drawable.shape_order_history_order_type_delivery_reception;
                break;
            case ORDER_TYPE_CLIENT_CANCEL:
                stringResId = R.string.item_order_history_order_type_client_cancel;
                colorResId = R.color.hong;
                bgResId = R.drawable.shape_order_history_order_type_order_cancel;
                break;
            case ORDER_TYPE_STORE_CANCEL:
                stringResId = R.string.item_order_history_order_type_store_cancel;
                colorResId = R.color.hong;
                bgResId = R.drawable.shape_order_history_order_type_order_cancel;
                break;
            default:
                stringResId = R.string.item_order_history_order_type_order_wait;
                colorResId = R.color.item_order_history_order_type_wait;
                bgResId = R.drawable.shape_order_history_order_type_order_wait;
                break;

        }
        tvCommonOrderType.setText(context.getString(stringResId));
        tvCommonOrderType.setBackgroundResource(bgResId);
        tvCommonOrderType.setTextColor(ContextCompat.getColor(context, colorResId));
    }

}
