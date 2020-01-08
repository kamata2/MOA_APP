package com.moaPlatform.moa.util.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.StringUtil;

/**
 * Created by jiwun on 2019-07-22.
 */
public class CommonTimeEventView extends LinearLayout {

    private TextView tvCommonTimeEvent;

    public CommonTimeEventView(Context context) {
        super(context);
        init();
    }

    public CommonTimeEventView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommonTimeEventView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        String inService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(inService);
        View view = layoutInflater.inflate(R.layout.common_time_event_view, this, false);
        addView(view);

        tvCommonTimeEvent = view.findViewById(R.id.tvCommonTimeEvent);

    }

    /**
     * 적립률 타입
     *
     * @param saveRate 적립률
     */
    public void saveRateType(int saveRate) {
        tvCommonTimeEvent.setTextColor(ContextCompat.getColor(getContext(), R.color.coral));
        tvCommonTimeEvent.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shopping_cart_discount_button));
        String textViewText = getResources().getString(R.string.common_time_event_default_save_rate, saveRate);
        tvCommonTimeEvent.setText(textViewText);
    }

    /**
     * 적립률 타입
     *
     * @param saveRate 적립률
     */
    public void saveRateType(String saveRate) {
        saveRateType(Integer.valueOf(saveRate));
    }

    /**
     * 추가 적립률 타입
     *
     * @param addSaveRate 추가 적립률
     */
    public void addSaveRateType(int addSaveRate) {
        tvCommonTimeEvent.setTextColor(ContextCompat.getColor(getContext(), R.color.light_blue));
        tvCommonTimeEvent.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shopping_cart_discount_button_blue));
        String textViewText = getResources().getString(R.string.common_time_event_add_save_rate, addSaveRate);
        tvCommonTimeEvent.setText(textViewText);
    }

    /**
     * 정액 할인
     */
    public void fixedAmount(int amount) {
        tvCommonTimeEvent.setTextColor(ContextCompat.getColor(getContext(), R.color.goldenTainoi));
        tvCommonTimeEvent.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shopping_cart_discount_button_gold));
        String amountString = StringUtil.convertCommaPrice(amount);
        String textViewText = getResources().getString(R.string.common_time_event_fixed_amount, amountString);
        tvCommonTimeEvent.setText(textViewText);
    }

    public void fixedRate(int fixRate) {
        tvCommonTimeEvent.setTextColor(ContextCompat.getColor(getContext(), R.color.coral));
        tvCommonTimeEvent.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shopping_cart_discount_button));
        String textViewText = getResources().getString(R.string.common_time_event_fixed_rate, fixRate);
        tvCommonTimeEvent.setText(textViewText);
    }

}
