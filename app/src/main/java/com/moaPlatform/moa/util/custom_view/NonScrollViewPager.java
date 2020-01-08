package com.moaPlatform.moa.util.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class NonScrollViewPager extends ViewPager {
    private boolean enabled = false;
    private boolean useMeasure = false;

    public NonScrollViewPager(@NonNull Context context) {
        super(context);
        initPageChangeListener();
    }

    public NonScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPageChangeListener();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    private void initPageChangeListener() {
        addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                requestLayout();
            }
        });
    }

    /**
     * wrap_content 일때 height 가 자식들의 크기만큼 커지지 않기 때문에 onMeasure 확장
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(useMeasure){
            View child = getChildAt(getCurrentItem());
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setUseMeasure(boolean useMeasure) {
        this.useMeasure = useMeasure;
    }
}
