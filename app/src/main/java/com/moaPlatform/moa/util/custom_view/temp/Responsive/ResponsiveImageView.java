package com.moaPlatform.moa.util.custom_view.temp.Responsive;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.util.custom_view.temp.model.CustomViewModel;
import com.moaPlatform.moa.util.responsive.ResponsiveUiController;

import androidx.appcompat.widget.AppCompatImageView;

public class ResponsiveImageView extends AppCompatImageView {

    CustomViewModel customViewModel = new CustomViewModel();
    ResponsiveUiController responsiveUiController = new ResponsiveUiController();

    public ResponsiveImageView(Context context) {
        super(context);
    }

    public ResponsiveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResponsiveImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        super.onMeasure(customViewModel.widthMeasureSpec, customViewModel.heightMeasureSpec);
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

    /**
     * 글라이드 라이브러리 사용해서 이미지 로드
     */
    public void glideImgLoad(int bg) {
        Glide.with(this)
                .load(bg)
//                .apply(RequestOptions.skipMemoryCacheOf(true))
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(this);
    }

    public void glideImgLoad(String subUrl) {
        Glide.with(this)
                .load(BuildConfig.FILE_SERVER_BASE_URL + subUrl)
                .into(this);
    }
}
