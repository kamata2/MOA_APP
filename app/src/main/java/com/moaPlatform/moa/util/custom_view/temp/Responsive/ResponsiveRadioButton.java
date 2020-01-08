package com.moaPlatform.moa.util.custom_view.temp.Responsive;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.temp.model.CustomViewModel;
import com.moaPlatform.moa.util.responsive.ResponsiveUiController;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ResponsiveRadioButton extends ConstraintLayout {
    // 라디오 버튼의 일반 이미지, 클릭했을때 이미지
    ResponsiveImageView radioIcNor, radioIcPress;
    // 라디오 버튼의 텍스트 내용
    public ResponsiveTextView radioContent;
    boolean radioCheck = false;
    CustomViewModel customViewModel = new CustomViewModel();
    ResponsiveUiController responsiveUiController = new ResponsiveUiController();

    public ResponsiveRadioButton(Context context) {
        super(context);
        initView();
    }

    public ResponsiveRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public ResponsiveRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    private void initView() {
        String inService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(inService);
        View view = layoutInflater.inflate(R.layout.custon_radio_button, this, false);
        addView(view);
        radioIcNor = findViewById(R.id.radioIcNor);
        radioIcPress = findViewById(R.id.radioIcPress);
        radioContent = findViewById(R.id.radioContent);

        view.setOnClickListener((View v)-> {
//            check = !check;
//            checkControl(null);
        });

    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ResponsiveRadioButton);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ResponsiveRadioButton, defStyleAttr, 0 );
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        // 기본 체크박스 이미지
        int norImage = typedArray.getResourceId(R.styleable.ResponsiveRadioButton_radioButtonNorImage, R.drawable.radio_n);
        // 눌렸을때 이미지
        int pressImage = typedArray.getResourceId(R.styleable.ResponsiveRadioButton_radioButtonPressImage, R.drawable.radio_p);
        // 텍스트 사이즈
        int textSize = typedArray.getInt(R.styleable.ResponsiveRadioButton_radioButtonTextSize, 10);
        // 텍스트 스타일
        int textStyle = typedArray.getInt(R.styleable.ResponsiveRadioButton_radioButtonTextStyle, Typeface.NORMAL);
        // 텍스트 내용
        String textContent = typedArray.getString(R.styleable.ResponsiveRadioButton_radioButtonText);

        textFontChange(textStyle);
        setText(textContent);
        setTextSize(textSize);
        defaultCheckBoxImage(norImage);
        pressCheckBoxImage(pressImage);
        typedArray.recycle();
    }

    public void textFontChange(int textStyle) {
        radioContent.textFontChange(textStyle);
    }

    public void setTextSize(int textSize) {
        radioContent.textSizeChange(textSize);
    }

    public void setText(String text) {
        radioContent.setText(text);
    }

    public void pressCheckBoxImage(int pressImage) {
        radioIcPress.setBackgroundResource(pressImage);
    }

    public void defaultCheckBoxImage(int norImage) {
        radioIcNor.setBackgroundResource(norImage);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        customViewModel.paddingSetting(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        if (!customViewModel.isResponsivePadding) {
            responsiveUiController.paddingResizeBaseWidth(customViewModel);
            setPadding(customViewModel.leftPadding, customViewModel.topPadding, customViewModel.rightPadding, customViewModel.bottomPadding);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ViewGroup.MarginLayoutParams marginLayoutParams = ViewGroup.MarginLayoutParams.class.cast(getLayoutParams());

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
