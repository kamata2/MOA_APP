package com.moaPlatform.moa.util.custom_view.temp.Responsive;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.moaPlatform.moa.util.custom_view.temp.model.CustomViewModel;
import com.moaPlatform.moa.util.responsive.ResponsiveUiController;

import androidx.appcompat.widget.AppCompatSeekBar;

public class ResponsiveSeekBar extends AppCompatSeekBar {

    CustomViewModel customViewModel = new CustomViewModel();

    public ResponsiveSeekBar(Context context) {
        super(context);
    }

    public ResponsiveSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResponsiveSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.setThumb(null);
        this.setEnabled(false);
        ViewGroup.MarginLayoutParams marginLayoutParams = ViewGroup.MarginLayoutParams.class.cast(getLayoutParams());
        customViewModel.marginSetting(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
        if (!customViewModel.isResponsiveMargin) {
            ResponsiveUiController responsiveUiController = new ResponsiveUiController();
            responsiveUiController.marginResizeBaseWidth(customViewModel);
            marginLayoutParams.leftMargin = customViewModel.leftMargin;
            marginLayoutParams.topMargin = customViewModel.topMargin;
            marginLayoutParams.rightMargin = customViewModel.rightMargin;
            marginLayoutParams.bottomMargin = customViewModel.bottomMargin;
            setLayoutParams(marginLayoutParams);
        }
    }
}
