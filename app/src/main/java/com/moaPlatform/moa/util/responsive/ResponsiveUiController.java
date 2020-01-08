package com.moaPlatform.moa.util.responsive;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.moaPlatform.moa.util.custom_view.temp.model.CustomViewModel;
import com.moaPlatform.moa.util.custom_view.temp.model.MarginModel;

public class ResponsiveUiController {

    // ui를 구성할떄 맞춘 가로, 세로 사이즈
    int baseWidth = 1080;
    int baseHeight = 1920;

    // 디바이스의 가로, 세로
    int deviceWidth = 0;
    int deviceHeight = 0;

    // 증감된 퍼센트 값
    float percentWidth = 0f;
    float percentHeight = 0f;

    public ResponsiveUiController() {
        getDeviceResolution();
        deviceIncreaseAndDecrease();
    }

    /**
     * 현재 디바이스의 크기를 구함
     * */
    private void getDeviceResolution() {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        deviceWidth = displayMetrics.widthPixels;
        deviceHeight = displayMetrics.heightPixels;
    }

    /**
     * 디바이스 크기의 증감을 퍼센트로 구함
     * */
    private void deviceIncreaseAndDecrease() {
        percentWidth = percentCheck(baseWidth, deviceWidth);
        percentHeight = percentCheck(baseHeight, deviceHeight);
    }

    /**
     * 베이스 사이즈가 변경된 값으로 얼마나 증감됐는지 퍼센트로 반환
     * @param base
     * 베이스 사이즈
     * @param change
     * 변경된 사이즈
     * */
    private float percentCheck(float base, float change) {
        return ((change - base) / base * 100);
    }



    /**
     * 마진값을 가로 사이즈가 바뀐 퍼센트 만큼 변경
     * @param marginModel
     * 변경한 마진값을 저장할 변수
     * */
    public MarginModel marginResizeBaseWidth(MarginModel marginModel) {
        marginModel.leftMargin = resizeIncreaseAndDecrease(marginModel.leftMargin, percentWidth);
        marginModel.topMargin = resizeIncreaseAndDecrease(marginModel.topMargin, percentWidth);
        marginModel.rightMargin = resizeIncreaseAndDecrease(marginModel.rightMargin, percentWidth);
        marginModel.bottomMargin = resizeIncreaseAndDecrease(marginModel.bottomMargin, percentWidth);
        marginModel.isResponsiveMargin = true;
        return marginModel;
    }

    public void marginResizeBaseWidth(CustomViewModel customViewModel) {
        customViewModel.leftMargin = resizeIncreaseAndDecrease(customViewModel.leftMargin, percentWidth);
        customViewModel.topMargin = resizeIncreaseAndDecrease(customViewModel.topMargin, percentWidth);
        customViewModel.rightMargin = resizeIncreaseAndDecrease(customViewModel.rightMargin, percentWidth);
        customViewModel.bottomMargin = resizeIncreaseAndDecrease(customViewModel.bottomMargin, percentWidth);
        customViewModel.isResponsiveMargin = true;
//        return customViewModel;
    }

    public void paddingResizeBaseWidth(CustomViewModel customViewModel) {
        customViewModel.leftPadding = resizeIncreaseAndDecrease(customViewModel.leftPadding, percentWidth);
        customViewModel.topPadding = resizeIncreaseAndDecrease(customViewModel.topPadding, percentWidth);
        customViewModel.rightPadding = resizeIncreaseAndDecrease(customViewModel.rightPadding, percentWidth);
        customViewModel.bottomPadding = resizeIncreaseAndDecrease(customViewModel.bottomPadding, percentWidth);
        customViewModel.isResponsivePadding = true;
    }

    public MarginModel initMargin(MarginModel marginModel) {
        return marginModel;
    }

    /**
     * 베이스값에서 값을 증가할꺼인지 감소할것인지 체크한후
     * 그에 따른 함수를 호출후 값을 반환
     * 증감시킬값은 증감된 가로의 퍼센트 값
     * @param base
     * 베이스값
     * */
    public int resizeIncreaseAndDecreaseBaseWidth(float base) {
        return (int)(base + (base * percentWidth / 100));
    }

    /**
     * 뷰의 사이즈를 세팅
     * */
    public void viewSizeSetting(View view, CustomViewModel customViewModel) {
        if (customViewModel.width == 0 && view.getLayoutParams().width != ViewGroup.LayoutParams.WRAP_CONTENT && view.getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT) {
            customViewModel.width = ViewGroup.MeasureSpec.getSize(customViewModel.widthMeasureSpec);
        }

        if (customViewModel.height == 0 && view.getLayoutParams().height != ViewGroup.LayoutParams.WRAP_CONTENT && view.getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT) {
            customViewModel.height = ViewGroup.MeasureSpec.getSize(customViewModel.heightMeasureSpec);
        }

        if (view.getLayoutParams().width != ViewGroup.LayoutParams.WRAP_CONTENT && view.getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT) {
            view.getLayoutParams().width = customViewModel.width;
        }

        if (view.getLayoutParams().height != ViewGroup.LayoutParams.WRAP_CONTENT && view.getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT) {
            view.getLayoutParams().height = customViewModel.height;
        }
    }

    /**
     * 가로, 세로 사이즈를 가로 사이즈가 증가된 퍼센트 만큼 증가시킴
     * @param target
     * 증감할 뷰
     * */
    public void onSquareBaseWidth(View target, CustomViewModel customViewModel) {
        // 가로
        if (customViewModel.isWidthResize == false) {
            if (target.getLayoutParams().width != 0 && target.getLayoutParams().width !=  ViewGroup.LayoutParams.WRAP_CONTENT && target.getLayoutParams().width !=  ViewGroup.LayoutParams.MATCH_PARENT) {
                customViewModel.isWidthResize = true;
                onSizeWidthBaseWidth(target);
            }
        }

        // 세로
        if (customViewModel.isHeightResize == false) {
            if (target.getLayoutParams().height != 0 && target.getLayoutParams().height !=  ViewGroup.LayoutParams.WRAP_CONTENT && target.getLayoutParams().height !=  ViewGroup.LayoutParams.MATCH_PARENT) {
                customViewModel.isHeightResize = true;
                onSizeHeightBaseWidth(target);
            }
        }
    }

    /**
     * 가로 사이즈를 가로 사이즈가 증가된 퍼센트 만큼 증가시킴
     * */
    public void onSizeWidthBaseWidth(View target) {
        target.getLayoutParams().width = resizeIncreaseAndDecrease(target.getLayoutParams().width, percentWidth);
    }

    /**
     * 세로 사이즈를 가로 사이즈가 증가된 퍼센트 만큼 증가시킴
     * */
    public void onSizeHeightBaseWidth(View target) {
        target.getLayoutParams().height = resizeIncreaseAndDecrease(target.getLayoutParams().height, percentWidth);
    }
    /**
     * 베이스값에서 값을 증가할꺼인지 감소할것인지 체크한후
     * 그에 따른 함수를 호출후 값을 반환
     * @param base
     * 베이스값
     * @param percent
     * 증감시킬 퍼센트값
     * */
    private int resizeIncreaseAndDecrease(float base, float percent) {
        return (int)(base + (base * percent / 100));
    }


}
