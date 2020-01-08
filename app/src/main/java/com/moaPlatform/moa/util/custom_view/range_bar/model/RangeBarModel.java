package com.moaPlatform.moa.util.custom_view.range_bar.model;

import android.view.View;

import com.moaPlatform.moa.util.custom_view.temp.Responsive.ResponsiveImageView;
import com.moaPlatform.moa.util.custom_view.temp.Responsive.ResponsiveSeekBar;
import com.moaPlatform.moa.util.custom_view.temp.Responsive.ResponsiveTextView;

/**
 * 레인지바 모델
 */
public class RangeBarModel {
    // 레인지바 게이지
    public ResponsiveSeekBar seekBar;
    // 배지 아이콘
    public ResponsiveImageView giftBox;
    // 채울 횟수
    public ResponsiveTextView giftCount;

    /**
     * 모델 세팅
     * @param seekBar
     * 레인지바 게이지
     * @param giftBox
     * 배지 아이콘
     * @param giftCount
     * 채울 횟수
     */
    public RangeBarModel(ResponsiveSeekBar seekBar, ResponsiveImageView giftBox, ResponsiveTextView giftCount) {
        this.seekBar = seekBar;
        this.giftBox = giftBox;
        this.giftCount = giftCount;
    }

    public void rangeBarHidden() {
        seekBar.setVisibility(View.GONE);
        giftBox.setVisibility(View.GONE);
        giftCount.setVisibility(View.GONE);
    }

    public void rangeBarShow() {
        seekBar.setProgress(0);
        seekBar.setVisibility(View.VISIBLE);
        giftBox.setVisibility(View.VISIBLE);
        giftCount.setVisibility(View.VISIBLE);
    }
}
