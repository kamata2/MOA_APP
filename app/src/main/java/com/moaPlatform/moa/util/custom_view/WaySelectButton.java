package com.moaPlatform.moa.util.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moaPlatform.moa.R;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 결제 페이지에서 방식 선택 버튼
 */
public class WaySelectButton extends ConstraintLayout {
    private ImageView icon;
    private TextView title;
    public boolean isChecked = false;

    public WaySelectButton(Context context) {
        super(context);
        init();
    }

    public WaySelectButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        getAttrs(attrs);
    }

    public WaySelectButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        getAttrs(attrs, defStyleAttr);
    }

    /**
     * 뷰 추가
     */
    private void init() {
        String inService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(inService);
        View view = layoutInflater.inflate(R.layout.way_select_button, this, false);
        addView(view);

        icon = findViewById(R.id.icon);
        title = findViewById(R.id.title);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WaySelectButton);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WaySelectButton, defStyleAttr, 0 );
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        title.setText(typedArray.getString(R.styleable.WaySelectButton_waySelectTitle));
        setChecked(typedArray.getBoolean(R.styleable.WaySelectButton_waySelectCheck, false));
//        icon.setVisibility(typedArray.getInt(R.styleable.WaySelectButton_waySelectIconVisibility, GONE));
    }

    public void setChecked(boolean check) {
        isChecked = check;
        stateSetting();
    }

    private void stateSetting() {
        if (isChecked) {
            icon.setVisibility(VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                title.setTextColor(getResources().getColor(R.color.coral, getContext().getTheme()));
            } else {
                title.setTextColor(getResources().getColor(R.color.coral));
            }
            setBackgroundResource(R.drawable.common_white_coral_style_10);
        } else {
            icon.setVisibility(GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                title.setTextColor(getResources().getColor(R.color.matterhorn, getContext().getTheme()));
            } else {
                title.setTextColor(getResources().getColor(R.color.matterhorn));
            }
            setBackgroundResource(R.drawable.common_button_type);
        }
    }
}
