package com.moaPlatform.moa.util.resolution;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;

import com.moaPlatform.moa.util.Logger;

public class ResolutionChange {

    // 원본 가로, 세로 사이즈
    int originalDeviceWidth = 1080;
    int originalDeviceHeight = 1920;

    // 현재 디바이스의 가로, 세로
    int nowDeviceWidth = 1080;
    int nowDeviceHeight = 1585;

//    public ResolutionChange() {
//        getDeviceResolution();
//    }

    /**
     * 현재 디바이스의 해상도 구하기
     * */
    private void getDeviceResolution() {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        nowDeviceWidth = displayMetrics.widthPixels;
        nowDeviceHeight = displayMetrics.heightPixels;
    }

    /**
     * 해당 뷰의 사이즈를 변경
     * @param width
     * 가로
     * @param height
     * 높이
     * @param targetView
     * 사이즈를 변경할 뷰
     * */
    public void resize(int width, int height, View targetView) {

        // 기존 길이에서 더해진 값
        int plusWidth = 0;

        if ( width > 0 ) {
            float changeWidth = (nowDeviceWidth * getPercentWidth(width) / 100);
            targetView.getLayoutParams().width = (int)changeWidth;
            plusWidth = (int)changeWidth - width;
        }

        // 가로가 길어진 길이만큼 세로도 증가
        if (height > 0) {
            targetView.getLayoutParams().height += plusWidth;
        }
    }

    public void newResize(int width, int height, View targetView) {
        float percentWidth = 0f;
//        percentWidth = (float) originalDeviceWidth/ (float) nowDeviceWidth * 100;
        percentWidth = getValuePlusPercent(originalDeviceWidth, nowDeviceWidth);
        Logger.i("percentWidth" + "newResize: " + percentWidth);
        Logger.i("percentWidth" + "originalDeviceWidth: " + originalDeviceWidth);
        Logger.i("percentWidth" + "nowDeviceWidth: " + nowDeviceWidth);
    }

    /**
     * 가로의 퍼센트 값 구하기
     * 전체값의 몇 퍼센트는 얼마인지 구할떄 사용
     * @param width
     * 가로 길이
     * */
    private float getPercentWidth(float width) {
        float percentWidth = 0f;
        percentWidth = width / originalDeviceWidth * 100;
        return percentWidth;
    }

    /**
     * 어떤값이 다른 값으로 변경되었을때
     * 몇 퍼센트가 증가또는 감소 됬는지 구할떄 사용
     * @param originalValue
     * 기준값
     * @param changeValue
     * 변경된 값
     *
     */
    private float getValuePlusPercent (float originalValue, float changeValue) {
        float percent = 0f;
        percent = (changeValue - originalValue) / originalValue * 100;
        return percent;
    }

    /**
     * 특정값이 퍼센트값 만큼 증가
     * @param targetValue
     * 특정값
     * @param percent
     * 증가할 퍼센트 값
     * */
    private float percentAdd(float targetValue, float percent) {
        float a = targetValue + (targetValue * 10 / 100);
        return 0f;
    }


    /**
     * 2018.10.10 뉴 버전
     * */

    // 원본 디바이스 가로, 세로
    int ogDvWidth = 1080;
    int ogDvHeight = 1920;

    // 변경된 디바이스 가로, 세로
    int chgDvWidth = 1080;
    int chgDvHeight = 1920;

    // 원본에서 현재 해상도로 변경됬을떄 가로, 세로의 퍼센트값
    float dvWidthPct = 0f;
    float dvHeightPct = 0f;

    public ResolutionChange() {
        getNowDeviceResolution();
        getDeviceResolution();
        devicePercent();
    }

    /**
     * 현재 디바이스 해상도 구하기
     * */
    private void getNowDeviceResolution() {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        chgDvWidth = displayMetrics.widthPixels;
        chgDvHeight = displayMetrics.heightPixels;
    }

    /**
     * 원본에서 현재 디바이스 변경됬을떄
     * 가로 세로의 증가된 퍼센트값 구하기
     * */
    private void devicePercent() {
        dvWidthPct = targetSizeChangeToOtherSize(ogDvWidth, chgDvWidth);
        dvHeightPct = targetSizeChangeToOtherSize(ogDvHeight, chgDvHeight);
    }

    /**
     * 기준값이 다른 사이즈로 변경됬을경우
     * 몇퍼센트가 증가됬는지 구함
     * */
    private float targetSizeChangeToOtherSize(float target, float change) {
        float percent = (change - target) / target * 100;
        return percent;
    }

    /**
     * 타겟이 증가될것인지 감소될것인지 체크
     * 퍼센트가 0보다 크다면 증가
     * 0보다 작다면 감소
     * @param value
     * 증감될 값
     * @param percent
     * 증감할 퍼센트
     * */
    private int targetPercentIncreaseAndDecrease(float value, float percent) {
        if (percent > 0) {
            return targetPercentIncrease(value, percent);
        } else {
            return targetPercentDecrease(value, percent);
        }
    }

    /**
     * 특정 길이를 해당 퍼센트 만큼 증가
     * @param value
     * 증가시킬 길이
     * @param percent
     * 증가시킬 퍼센트
     * */
    private int targetPercentIncrease(float value, float percent) {
        float chgValue = value + (value * percent / 100);
        return (int)chgValue;
    }

    /**
     * 특정 길이를 해당 퍼센트 만큼 감소
     * @param value
     * 감소시킬 길이
     * @param percent
     * 감소시킬 퍼센트
     * */
    private int targetPercentDecrease(float value, float percent) {
        float chgValue = value - (value * percent / 100);
        return (int)chgValue;
    }

    /**
     * 가로 세로 비율 일정하게 증감
     * @param target
     * 증감할 뷰
     * */
    public void onSquareResize(View target) {
        int width = target.getLayoutParams().width;
        int height = target.getLayoutParams().height;
        int chgWidth = targetPercentIncreaseAndDecrease(width, dvWidthPct);
        int chgHeight = targetPercentIncreaseAndDecrease(height, dvWidthPct);

        target.getLayoutParams().width = chgWidth;
        target.getLayoutParams().height = chgHeight;
    }

}
