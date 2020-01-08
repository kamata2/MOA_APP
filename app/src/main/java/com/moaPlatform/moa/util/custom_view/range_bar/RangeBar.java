package com.moaPlatform.moa.util.custom_view.range_bar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.model.res.AchvmInfoList;
import com.moaPlatform.moa.util.custom_view.temp.Responsive.ResponsiveConstraintLayout;
import com.moaPlatform.moa.util.custom_view.temp.Responsive.ResponsiveImageView;
import com.moaPlatform.moa.util.custom_view.temp.Responsive.ResponsiveSeekBar;
import com.moaPlatform.moa.util.custom_view.temp.Responsive.ResponsiveTextView;
import com.moaPlatform.moa.util.custom_view.range_bar.controller.RangeBarController;
import com.moaPlatform.moa.util.custom_view.range_bar.model.RangeBarModel;

import java.util.ArrayList;

/**
 * 배찌 페이지의 상품 보상
 */
public class RangeBar extends ResponsiveConstraintLayout implements View.OnClickListener {

    public ResponsiveSeekBar stepOneBar, stepTwoBar, stepThreeBar;
    public ResponsiveImageView stepOneGift, stepTwoGift, stepThreeGift;
    public ResponsiveTextView stepOneGiftCount, stepTwoGiftCount, stepThreeGiftCount;
    private RangeBarController rangeBarController = new RangeBarController();
    private ArrayList<RangeBarModel> rangeBarModels = new ArrayList<>();

    public RangeBar(Context context) {
        super(context);
        initView();
    }

    public RangeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public RangeBar(Context context, AttributeSet attrs, int defStyleAttr) {
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
        View view = layoutInflater.inflate(R.layout.range_bar, this, false);
        addView(view);

        stepOneBar = findViewById(R.id.stepOneBar);
        stepTwoBar = findViewById(R.id.stepTwoBar);
        stepThreeBar = findViewById(R.id.stepThreeBar);

        stepOneGift = findViewById(R.id.stepOneGift);
        stepTwoGift = findViewById(R.id.stepTwoGift);
        stepThreeGift = findViewById(R.id.stepThreeGift);

        stepOneGiftCount = findViewById(R.id.stepOneGiftCount);
        stepTwoGiftCount = findViewById(R.id.stepTwoGiftCount);
        stepThreeGiftCount = findViewById(R.id.stepThreeGiftCount);

        stepOneGift.setOnClickListener(this);

        rangeBarModels.add(new RangeBarModel(stepOneBar, stepOneGift, stepOneGiftCount));
        rangeBarModels.add(new RangeBarModel(stepTwoBar, stepTwoGift, stepTwoGiftCount));
        rangeBarModels.add(new RangeBarModel(stepThreeBar, stepThreeGift, stepThreeGiftCount));
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ResponsiveRangeBar);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ResponsiveRangeBar, defStyleAttr, 0 );
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        int rangeCount = typedArray.getInt(R.styleable.ResponsiveRangeBar_rangeBarCount, 3);
        int oneGiftCount = typedArray.getInt(R.styleable.ResponsiveRangeBar_rangeBarOneGiftCount, 3);
        int twoGiftCOunt = typedArray.getInt(R.styleable.ResponsiveRangeBar_rangeBarTwoGiftCount, 3);
        int threeGiftCount = typedArray.getInt(R.styleable.ResponsiveRangeBar_rangeBarThreeGiftCount, 3);
        rangeSetting(rangeCount, null,  0);
        typedArray.recycle();
    }

    public void rangeSetting(int count, ArrayList<AchvmInfoList> achvmInfoLists, int cmplFigure) {
        rangeBarController.rangeItemHidden(rangeBarModels);
        if (achvmInfoLists != null) {
            rangeBarController.rangeDataSetting(rangeBarModels, achvmInfoLists, cmplFigure);
        }
    }

    public void rangeBarInit(ArrayList<AchvmInfoList> achvmInfoLists) {
        rangeBarController.rangeItemInit(rangeBarModels, achvmInfoLists);
    }

    @Override
    public void onClick(View v) {
        stepOneGift.clearAnimation();
        stepOneGift.glideImgLoad(R.drawable.giftboxopen);
    }
}
