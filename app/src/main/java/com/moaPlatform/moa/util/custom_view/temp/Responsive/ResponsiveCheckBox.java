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

public class ResponsiveCheckBox extends ConstraintLayout {

    CustomViewModel customViewModel = new CustomViewModel();
    ResponsiveUiController responsiveUiController = new ResponsiveUiController();

    // 체크박스의 기본 및 클릭 되었을때 이미지
    ResponsiveImageView checkNor, checkPress;
    // 체크박스의 텍스트
    ResponsiveTextView checkBoxContent;

    CheckBoxClickListener checkBoxClickListener;

    boolean check = false;

    public ResponsiveCheckBox(Context context) {
        super(context);
        initView();
    }

    public ResponsiveCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public ResponsiveCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    public void setCheckBoxClickListener(CheckBoxClickListener checkBoxClickListener) {
        this.checkBoxClickListener = checkBoxClickListener;
    }

    private void initView() {
        String inService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(inService);
        View view = layoutInflater.inflate(R.layout.custom_check_box, this, false);
        addView(view);
        checkBoxContent = findViewById(R.id.checkBoxContent);
        checkNor = findViewById(R.id.checkBoxNor);
        checkPress = findViewById(R.id.checkBoxPress);

        view.setOnClickListener((View v)-> {
            check = !check;
            checkControl(null);
            if (checkBoxClickListener != null) {
                checkBoxClickListener.clickEvent(check);
            }
        });
        
    }

    public void checkControl(Boolean checkValue) {
        if (checkValue != null) {
            check = checkValue;
        }
        if (check == true) {
            checkPress.setVisibility(VISIBLE);
        } else {
            checkPress.setVisibility(GONE);
        }
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ResponsiveCheckBox);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ResponsiveCheckBox, defStyleAttr, 0 );
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        // 기본 체크박스 이미지
        int norImage = typedArray.getResourceId(R.styleable.ResponsiveCheckBox_defaultImage, R.drawable.checkbox_n);
        // 눌렸을때 이미지
        int pressImage = typedArray.getResourceId(R.styleable.ResponsiveCheckBox_pressImage, R.drawable.checkbox_p);
        int textSize = typedArray.getInt(R.styleable.ResponsiveCheckBox_checkBoxTextSize, 10);
        int textStyle = typedArray.getInt(R.styleable.ResponsiveCheckBox_checkBoxTextStyle, Typeface.NORMAL);
        String text = typedArray.getString(R.styleable.ResponsiveCheckBox_checkBoxText);
        textFontChange(textStyle);
        setText(text);
        setTextSize(textSize);
        defaultCheckBoxImage(norImage);
        pressCheckBoxImage(pressImage);
        typedArray.recycle();
    }

    public void textFontChange(int textStyle) {
        checkBoxContent.textFontChange(textStyle);
    }

    public void setTextSize(int textSize) {
        checkBoxContent.textSizeChange(textSize);
    }

    public void setText(String text) {
        checkBoxContent.setText(text);
    }

    public void pressCheckBoxImage(int pressImage) {
        checkPress.setBackgroundResource(pressImage);
    }

    public void defaultCheckBoxImage(int norImage) {
        checkNor.setBackgroundResource(norImage);
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
