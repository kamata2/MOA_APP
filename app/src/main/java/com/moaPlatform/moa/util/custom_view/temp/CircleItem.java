package com.moaPlatform.moa.util.custom_view.temp;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.moaPlatform.moa.R;

import androidx.constraintlayout.widget.ConstraintLayout;

public class CircleItem extends ConstraintLayout {

    CustomImageView circleItemBg;
    CustomImageView icon;
    TextView title;

    public CircleItem(Context context) {
        super(context);
        initView();
    }

    public CircleItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public CircleItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    /**
     * 커스텀 layout과 연결
     * */
    private void initView() {
        String inService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(inService);
        View view = layoutInflater.inflate(R.layout.circle_item, this, false);
        addView(view);

        circleItemBg = findViewById(R.id.circleItem);
        icon = findViewById(R.id.lvIcon);
        title = findViewById(R.id.txtTitle);

    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleItem);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleItem, defStyleAttr, 0 );
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        int bg_resId = typedArray.getResourceId(R.styleable.CircleItem_circleItemBackground, R.drawable.category_delivery_bg);
        circleItemBg.setBackgroundResource(bg_resId);
        int bgWidth = typedArray.getInt(R.styleable.CircleItem_circleItemBackgroundWidth, 254);
        int bgHeight = typedArray.getInt(R.styleable.CircleItem_circleItemBackgroundHeight, 557);
        circleItemBg.getLayoutParams().height = bgHeight;
        circleItemBg.getLayoutParams().width = bgWidth;

        int icon_resId = typedArray.getResourceId(R.styleable.CircleItem_circleItemIcon, R.drawable.category_delivery_ic);
        icon.setBackgroundResource(icon_resId);

        String text = typedArray.getString(R.styleable.CircleItem_circleItemTitle);
        title.setText(text);

        typedArray.recycle();
    }

}
