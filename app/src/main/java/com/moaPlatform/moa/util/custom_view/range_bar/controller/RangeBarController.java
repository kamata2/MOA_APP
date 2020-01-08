package com.moaPlatform.moa.util.custom_view.range_bar.controller;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.model.res.AchvmInfoList;
import com.moaPlatform.moa.util.custom_view.temp.Responsive.ResponsiveImageView;
import com.moaPlatform.moa.util.custom_view.range_bar.model.RangeBarModel;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;

public class RangeBarController {

    private ArrayList<AchvmInfoList> achvmInfoLists;


    public void rangeItemHidden(ArrayList<RangeBarModel> rangeBarModels) {
        if (achvmInfoLists != null) {
            for (int i = achvmInfoLists.size(); i < rangeBarModels.size(); i++ ) {
                RangeBarModel rangeBarModel = rangeBarModels.get(i);
                rangeBarModel.rangeBarHidden();
                ResponsiveImageView giftBox = rangeBarModels.get(i-1).giftBox;
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) giftBox.getLayoutParams();
                layoutParams.rightMargin = 36;

                if (achvmInfoLists.size() == 1) {
                    rangeBarModels.get(0).seekBar.getLayoutParams().width = (int)rangeBarModel.seekBar.getResources().getDimension(R.dimen.test_3);
                } else if (rangeBarModels.size() == 2) {
                    rangeBarModel.seekBar.getLayoutParams().width = (int)rangeBarModel.seekBar.getResources().getDimension(R.dimen.test_2);
                }

                giftBox.requestLayout();
            }
        }
    }

    public void rangeItemHidden(ArrayList<RangeBarModel> rangeBarModels, int position) {
        for (int i = position; i < rangeBarModels.size(); i++) {
            RangeBarModel rangeBarModel = rangeBarModels.get(i);
            rangeBarModel.rangeBarHidden();
            ResponsiveImageView giftBox = rangeBarModels.get(i-1).giftBox;
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) giftBox.getLayoutParams();
            layoutParams.rightMargin = 36;

            if (position == 1) {
                rangeBarModels.get(0).seekBar.getLayoutParams().width = (int)rangeBarModel.seekBar.getResources().getDimension(R.dimen.test_3);
            } else if (rangeBarModels.size() == 2) {
                rangeBarModel.seekBar.getLayoutParams().width = (int)rangeBarModel.seekBar.getResources().getDimension(R.dimen.test_2);
            }

            giftBox.requestLayout();
        }
    }

    public void rangeDataSetting(ArrayList<RangeBarModel> rangeBarModels, ArrayList<AchvmInfoList> achvmInfoLists, int cmplFigure) {
        for (int i = 0; i < achvmInfoLists.size(); i++) {
            RangeBarModel rangeBarModel = rangeBarModels.get(i);
            AchvmInfoList achvmInfo = achvmInfoLists.get(i);
            rangeBarModel.giftCount.setText(achvmInfo.goalFigure+"회");
            rangeBarModel.seekBar.setMax(achvmInfo.goalFigure);
            if (cmplFigure >= achvmInfo.goalFigure) {
                rangeBarModel.seekBar.setProgress(rangeBarModel.seekBar.getMax());
                cmplFigure -= rangeBarModel.seekBar.getMax();
                if (achvmInfo.recvYn.equals("N")) {
                    Animation giftScaleAni = AnimationUtils.loadAnimation(rangeBarModel.giftBox.getContext(), R.anim.rang_gift_scale_anim);
                    rangeBarModel.giftBox.startAnimation(giftScaleAni);
                } else {
                    rangeBarModel.giftBox.glideImgLoad(R.drawable.giftboxopen);
                }
            } else {
                rangeBarModel.seekBar.setProgress(cmplFigure);
                cmplFigure = 0;
            }

        }
    }

    public void rangeGiftBoxOpen(ArrayList<RangeBarModel> rangeBarModels, ArrayList<AchvmInfoList> achvmInfoLists, int position) {
        RangeBarModel rangeBarModel = rangeBarModels.get(position);
        AchvmInfoList achvmInfo = achvmInfoLists.get(position);
        if (achvmInfo.cmplYn.equals("Y") && achvmInfo.recvYn.equals("N")) {
            rangeBarModel.giftBox.clearAnimation();
            rangeBarModel.giftBox.glideImgLoad(R.drawable.giftboxopen);
        }
    }

    public void rangeItemInit(ArrayList<RangeBarModel> rangeBarModels, ArrayList<AchvmInfoList> achvmInfoLists) {
        for(int i = 0; i < rangeBarModels.size(); i++) {
            RangeBarModel rangeBarModel = rangeBarModels.get(i);
            rangeBarModel.giftBox.glideImgLoad(R.drawable.giftbox);
            rangeBarModel.giftBox.clearAnimation();
            rangeBarModel.seekBar.getLayoutParams().width = (int)rangeBarModel.seekBar.getResources().getDimension(R.dimen.test_1);
            if (i > 0) {
                rangeBarModel.rangeBarShow();
                ResponsiveImageView giftBox = rangeBarModels.get(i-1).giftBox;
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) giftBox.getLayoutParams();
                layoutParams.rightMargin = 0;
                giftBox.requestLayout();
            }
        }
        this.achvmInfoLists = achvmInfoLists;
    }

}
