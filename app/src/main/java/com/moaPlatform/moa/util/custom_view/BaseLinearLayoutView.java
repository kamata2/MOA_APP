package com.moaPlatform.moa.util.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

abstract class BaseLinearLayoutView extends LinearLayout {

    protected Context context;

    abstract int layoutResourceId();
    abstract void initViews();

    public BaseLinearLayoutView(Context context) {
        super(context);
        inflateViews(context);
    }

    public BaseLinearLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateViews(context);
    }

    public BaseLinearLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateViews(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    protected void inflateViews(Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutResourceId(), this);

        ButterKnife.bind(this);

        initViews();
    }
}
