package com.moaPlatform.moa.util.custom_view.temp.model;

import android.view.View;
import android.view.ViewGroup;

/**
 * 뷰의 데이터를 저장할 모델
 * 마진값, 가로 세로 값 등
 * */
public class CustomViewModel {

    // 가로, 세로 사이즈
    public int width = 0;
    public int height = 0;

    public int widthMeasureSpec = 0;
    public int heightMeasureSpec = 0;

    // 왼쪽, 상단, 오른쪽, 하단의 마진의 값들을 저장할 변수들
    public int leftMargin = 0;
    public int topMargin = 0;
    public int rightMargin = 0;
    public int bottomMargin = 0;
    public boolean isResponsiveMargin = false;

    // 왼쪽, 상단, 오른쪽, 하단의 패딩의 값들을 저장할 변수들
    public int leftPadding = 0;
    public int topPadding = 0;
    public int rightPadding = 0;
    public int bottomPadding = 0;
    public boolean isResponsivePadding = false;

    // 가로 및 세로 사이즈가 바뀌었는지 체크
    // false 안바뀜, true 바뀜
    public boolean isWidthResize = false;
    public boolean isHeightResize = false;

    // 왼쪽, 상단, 오른쪽, 하단의 마진값을 저장
    public void marginSetting(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
    }

    // 왼쪽, 상단, 오른쪽, 하단의 패딩값을 저장
    public void paddingSetting(int leftPadding, int topPadding, int rightPadding, int bottomPadding) {
        this.leftPadding = leftPadding;
        this.topPadding = topPadding;
        this.rightPadding = rightPadding;
        this.bottomPadding = bottomPadding;
    }

    /**
     * 가로, 세로 사이즈 세팅
     * @param widthMeasureSpec
     * 가로 사이즈의 MeasureSpec
     * @param heightMeasureSpec
     * 세로 사이즈의 MeasureSpec
     * @param view
     * 해당 뷰
     * */
    public void sizeMeasureSpecSetting(int widthMeasureSpec, int heightMeasureSpec, View view) {
        // 가로 사이즈가 0 이고 wrap_content 가 아닐경우
        if (width == 0 && view.getLayoutParams().width != ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = View.MeasureSpec.getSize(widthMeasureSpec);
            view.getLayoutParams().width = width;
        }
        // 세로 사이즈가 0 이고 wrap_content 가 아닐경우
        if (height == 0 && view.getLayoutParams().height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = View.MeasureSpec.getSize(heightMeasureSpec);
            view.getLayoutParams().height = height;
        }
    }

    public void measureSpecSetting(View view) {
        if (isWidthResize == false && view.getLayoutParams().width != 0 && view.getLayoutParams().width !=  ViewGroup.LayoutParams.WRAP_CONTENT && view.getLayoutParams().width !=  ViewGroup.LayoutParams.MATCH_PARENT) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getLayoutParams().width, View.MeasureSpec.getMode(widthMeasureSpec));
        }

        if (isHeightResize == false && view.getLayoutParams().height != 0 && view.getLayoutParams().height !=  ViewGroup.LayoutParams.WRAP_CONTENT && view.getLayoutParams().height !=  ViewGroup.LayoutParams.MATCH_PARENT) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getLayoutParams().height, View.MeasureSpec.getMode(widthMeasureSpec));
        }
//        newWidthMeasureSpec = width == 0 ? widthMeasureSpec : View.MeasureSpec.makeMeasureSpec(view.getLayoutParams().width, View.MeasureSpec.getMode(widthMeasureSpec));
//        newHeightMeasureSpec = height == 0 ? heightMeasureSpec : View.MeasureSpec.makeMeasureSpec(view.getLayoutParams().height, View.MeasureSpec.getMode(heightMeasureSpec));
    }

    public void measureSpecSetting(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
    }

}
