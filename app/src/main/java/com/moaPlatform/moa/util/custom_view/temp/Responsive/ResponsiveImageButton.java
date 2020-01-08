package com.moaPlatform.moa.util.custom_view.temp.Responsive;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.moaPlatform.moa.R;

/**
 * 이미지 버튼
 */
public class ResponsiveImageButton extends ResponsiveConstraintLayout{
    ResponsiveTextView lvTitle;
    ResponsiveImageView lvIcon;

    public ResponsiveImageButton(Context context) {
        super(context);
        initView();
    }

    public ResponsiveImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public ResponsiveImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
        View view = layoutInflater.inflate(R.layout.custom_image_button, this, false);
        addView(view);

        lvTitle = findViewById(R.id.buttonTitle);
        lvIcon = findViewById(R.id.buttonIc);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ResponsiveImageButton);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ResponsiveImageButton, defStyleAttr, 0 );
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        // 아이콘
        int buttonIc = typedArray.getResourceId(R.styleable.ResponsiveImageButton_lvButtonIc, R.drawable.radio_n);
        int buttonIcHeight = typedArray.getInt(R.styleable.ResponsiveImageButton_lvButtonIcHeight, 40);
        int buttonIcWidth = typedArray.getInt(R.styleable.ResponsiveImageButton_lvButtonIcWidth, 41);
        boolean buttonIcGone = typedArray.getBoolean(R.styleable.ResponsiveImageButton_lvButtonIcGone, false);
        int textStyle = typedArray.getInt(R.styleable.ResponsiveImageButton_lvButtonTextStyle, Typeface.NORMAL);
        int textPadding = typedArray.getInt(R.styleable.ResponsiveImageButton_lvButtonTextPadding, 17);
        int textSize = typedArray.getInt(R.styleable.ResponsiveImageButton_lvButtonTextSize, 32);
        int textColor = typedArray.getInt(R.styleable.ResponsiveImageButton_lvButtonTextColor, R.color.matterhorn);
        String text = typedArray.getString(R.styleable.ResponsiveImageButton_lvButtonText);

        testSetting(textStyle, textSize, text, textPadding);
        lvIcon.setBackgroundResource(buttonIc);
        lvIconSizeSetting(buttonIcWidth, buttonIcHeight);
        lvIconVisibillity(((buttonIcGone == true) ? GONE : VISIBLE));
        textColor(textColor);
        typedArray.recycle();
    }

    /**
     * 아이콘의 가로 세로 사이즈 세팅
     * @param width
     * 가로 사이즈
     * @param height
     * 세로 사이즈
     */
    public void lvIconSizeSetting(int width, int height) {
        lvIcon.getLayoutParams().width = width;
        lvIcon.getLayoutParams().height = height;
    }

    /**
     * 버튼 아이콘 보여주기 유무
     * @param visibility
     * 보여줄건지 말건지 값
     */
    public void lvIconVisibillity(int visibility) {
        lvIcon.setVisibility(visibility);
    }

    /**
     * 텍스트 컬러 값 세팅
     * @param color
     * 컬러 값
     */
    public void textColor(int color) {
        lvTitle.setTextColor(color);
    }

    /**
     * 텍스트 세팅
     * @param textStyle
     * 텍스트 스타일
     * @param textSize
     * 텍스트 크기
     * @param text
     * 텍스트 내용
     * @param textPadding
     * 텍스트와 아이콘의 패딩 값
     */
    private void testSetting(int textStyle, int textSize, String text, int textPadding) {
        lvTitle.textFontChange(textStyle);
        lvTitle.setText(text);
        lvTitle.textSizeChange(textSize);
        lvTitle.setPadding(textPadding,0,0,0);
    }

}
