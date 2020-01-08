package com.moaPlatform.moa.util.custom_view.temp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.moaPlatform.moa.util.resolution.ResolutionChange;


public class CustomImageView extends androidx.appcompat.widget.AppCompatImageView {

    int originalWidth = 0;
    int originalHeight = 0;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);

        setOriginalSize(width, height);
        ResolutionChange resolutionChange = new ResolutionChange();
        resolutionChange.onSquareResize(this);
        setMeasuredDimension(getViewWidth(), getViewHeight());
    }

    /**
     * 원본 사이즈 저장
     * @param height
     * 높이
     * @param width
     * 가로
     * */
    private void setOriginalSize(int width, int height) {
        if (originalWidth == 0) {
            originalWidth = width;
        }

        if (originalHeight == 0) {
            originalHeight = height;
        }

        viewSizeChange(originalWidth, originalHeight);
    }

    /**
     * 뷰의 사이즈를 변경
     * @param width
     * 가로
     * @param height
     * 높이
     * */
    private void viewSizeChange(int width, int height) {
        this.getLayoutParams().width = width;
        this.getLayoutParams().height = height;
    }

    /**
     * 현재 뷰의 가로 길이
     * */
    private int getViewWidth() {
        return this.getLayoutParams().width;
    }

    /**
     * 현재 뷰의 세로 길이
     * */
    private int getViewHeight() {
        return this.getLayoutParams().height;
    }

}
