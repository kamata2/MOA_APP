package com.moaPlatform.moa.util.custom_view.temp.Responsive;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.temp.model.CustomViewModel;
import com.moaPlatform.moa.util.responsive.ResponsiveUiController;

/**
 * 나눔고딕 폰트의 사용과
 * 해상도에따라서 글자크기를 자동으로
 * 변경하기위해서 사용
 * 마진값이 화면 사이즈보다 커지게 되면 onDraw 가 작동 안하여
 * 마진값이 해상도에 따라 변경이 안되는 이슈가 있음
 * 예로 해상도가 1080 * 720 인 디바이스에서 왼쪽 마진값이 780px 일경우
 * 1080 * 720 해상도에서는 마진이 변경되는 코드가 잘 작동 되지만
 * 450 * 400 인 디바이스에서는 가로 사이즈보다 마진값이 더 커서
 * onDraw 가 작동이 안되서 화면에 표시 안됨
 * */

public class ResponsiveTextView extends AppCompatTextView {

    CustomViewModel customViewModel = new CustomViewModel();
    ResponsiveUiController responsiveUiController = new ResponsiveUiController();

    public ResponsiveTextView(Context context) {
        super(context);
        textFontChange();
    }

    public ResponsiveTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
        textFontChange();
    }

    public ResponsiveTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(attrs, defStyleAttr);
        textFontChange();
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ResponsiveTextView);
        typedArrayTextSize(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ResponsiveTextView, defStyleAttr, 0);
        typedArrayTextSize(typedArray);
    }

    /**
     * 텍스트뷰의 폰트를 나눔고딕으로 변경
     * */
    private void textFontChange() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.font_name_nanum_gothic));
        int type = 0;
        // 젤리빈일 경우 getTypeface().getStyle() 가 null 일경우 에러가 발생
        if (getTypeface() != null) {
            type = getTypeface().getStyle();
        }
        this.setTypeface(tf, type);
    }

    public void textFontChange(int style) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), getContext().getString(R.string.font_name_nanum_gothic));
        this.setTypeface(tf, style);
    }

    /**
     * typedArray 에서 textSize를 가져옴
     * @param typedArray
     */
    private void typedArrayTextSize(TypedArray typedArray) {
        int textSize = typedArray.getInt(R.styleable.ResponsiveTextView_responsiveTextSize, 10);
        textSizeChange(textSize);
    }

    /**
     * 텍스트뷰 사이즈 변경
     * 디바이스 해상도에따라서
     * 폰트 사이즈를 변경시킴
     * */
    public void textSizeChange(int textSize) {
        // 텍스트 뷰 사이즈를 조정할때 따로 사용자가 입력한 값이 없으면 기본값을 10으로 맞추고 해상도에 따라서 사이즈 변경
        int reSize = responsiveUiController.resizeIncreaseAndDecreaseBaseWidth((textSize-4));
        // 택스트뷰 사이즈를 조정할때 px를 기준
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, reSize);
    }

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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();

        customViewModel.marginSetting(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
        if (!customViewModel.isResponsiveMargin) {
            responsiveUiController.marginResizeBaseWidth(customViewModel);
            marginLayoutParams.leftMargin = customViewModel.leftMargin;
            marginLayoutParams.topMargin = customViewModel.topMargin;
            marginLayoutParams.rightMargin = customViewModel.rightMargin;
            marginLayoutParams.bottomMargin = customViewModel.bottomMargin;
            setLayoutParams(marginLayoutParams);
        }

    }
}
