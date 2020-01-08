package com.moaPlatform.moa.util.custom_view.temp.Responsive;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.moaPlatform.moa.R;

public class QuantityControlView extends ResponsiveConstraintLayout {

    private ResponsiveTextView quantityDown, quantityCount, quantityUp;

    public QuantityControlView(Context context) {
        super(context);
        initView();
    }

    public QuantityControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public QuantityControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    /**
     * 뷰 초기화
     */
    private void initView() {
        String inService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(inService);
        View view = layoutInflater.inflate(R.layout.quantity_control_view, this, false);
        quantityDown = findViewById(R.id.quantityDown);
        quantityCount = findViewById(R.id.quantityCount);
        quantityUp = findViewById(R.id.quantityUp);
//        quantityUp.setOnClickListener(this);
        addView(view);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.QuantityControlView);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.QuantityControlView, defStyleAttr, 0 );
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        int count = typedArray.getInt(R.styleable.QuantityControlView_count, 1);
//        quantityCount.setText(String.valueOf(count));
        typedArray.recycle();
    }

}
