package com.moaPlatform.moa.util.custom_view.temp.Responsive;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.moaPlatform.moa.util.custom_view.temp.model.CustomViewModel;
import com.moaPlatform.moa.util.responsive.ResponsiveUiController;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * ConstraintLayout 커스텀 버전
 * 마진, 해상도가 값이 디바이스 해상도에따라 변경됨
 * 마진값이 화면 사이즈보다 커지게 되면 onDraw 가 작동 안하여
 * 마진값이 해상도에 따라 변경이 안되는 이슈가 있음
 * 예로 해상도가 1080 * 720 인 디바이스에서 왼쪽 마진값이 780px 일경우
 * 1080 * 720 해상도에서는 마진이 변경되는 코드가 잘 작동 되지만
 * 450 * 400 인 디바이스에서는 가로 사이즈보다 마진값이 더 커서
 * onDraw 가 작동이 안되서 화면에 표시 안됨
 * */
public class ResponsiveConstraintLayout extends ConstraintLayout {

    CustomViewModel customViewModel = new CustomViewModel();
    ResponsiveUiController responsiveUiController = new ResponsiveUiController();

    public ResponsiveConstraintLayout(Context context) {
        super(context);
    }

    public ResponsiveConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResponsiveConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 초기 뷰의 사이즈를 측정
     * 해당 함수는 한번이상 호출됩니다.
     * 두번째부터는 처음에 사용자가 xml 부터 입력한 사이즈가 아닌
     * 해상도에 따라 달라진 사이즈 값을 가져옴
     * 따라서 가로, 세로 사이즈를 전역으로 지정한후에 0으로 초기화하고
     * 해당 함수가 호출될떄마다 가로 및 세로값을 저장
     * 만약 각각의 사이즈가 0이 아닐시에는 저장을 안함
     * 구한 가로 세로값을 해상도가 변경된 퍼센트 값만큼 증감시킨후
     * measureSpec 사이즈로 변환시켜서 super.onMeasure 에 넘김
     * super.onMeasure 이 아닌 setMeasuredDimension 로 바로 호출시
     * 자식뷰로 다른뷰를 추가할시에 자식뷰가 표시가 안되는 이슈가 발생
     * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        customViewModel.measureSpecSetting(widthMeasureSpec, heightMeasureSpec);
        responsiveUiController.onSquareBaseWidth(this, customViewModel);
        customViewModel.measureSpecSetting(this);

        customViewModel.paddingSetting(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        if (!customViewModel.isResponsivePadding) {
            responsiveUiController.paddingResizeBaseWidth(customViewModel);
            setPadding(customViewModel.leftPadding, customViewModel.topPadding, customViewModel.rightPadding, customViewModel.bottomPadding);
        }

        super.onMeasure(customViewModel.widthMeasureSpec, customViewModel.heightMeasureSpec);
    }

    /**
     * onLayout 에서 left, top, right, bottom 을
     * 직접적으로 조정하여 마진을 추가할수있지만
     * 해당방법을 사용할경우 부모 뷰의 값이 wrap_content 일경우
     * 부모뷰의 값이 변경하지 않음
     * 따라서 MarginLayoutParams 을 사용
     * 또한 MarginLayoutParams 을 onMeasure 쪽에서 사용할시
     * 일부 기기에서 작동을 하지 않음
     * */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        MarginLayoutParams marginLayoutParams = MarginLayoutParams.class.cast(getLayoutParams());

        customViewModel.marginSetting(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
        if (!customViewModel.isResponsiveMargin) {
            responsiveUiController.marginResizeBaseWidth(customViewModel);
            marginLayoutParams.leftMargin = customViewModel.leftMargin;
            marginLayoutParams.topMargin = customViewModel.topMargin;
            marginLayoutParams.rightMargin = customViewModel.rightMargin;
            marginLayoutParams.bottomMargin = customViewModel.bottomMargin;
            setLayoutParams(marginLayoutParams);
        }
        // 지도 세팅 메인부분에서 높이 변경하때
        // 아래 한줄을 뺴면 변경이 안됨
        requestLayout();
    }

}