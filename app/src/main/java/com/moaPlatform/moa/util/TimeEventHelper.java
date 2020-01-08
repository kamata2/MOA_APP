package com.moaPlatform.moa.util;

import android.view.View;

import com.moaPlatform.moa.util.custom_view.CommonTimeEventView;

import java.util.List;

/**
 * 타임 이벤트 helper
 * 타임 이벤트는 4개가 존재
 * 적립률, 추가 적립률, 정액, 적률
 * Created by jiwun on 2019-07-23.
 */
public class TimeEventHelper {

    private List<CommonTimeEventView> commonTimeEventViewList;
    private int saveRate;
    private int addSaveRate;
    private int fixedRate;
    private int fixedAmount;
    private boolean hideSaveRate;

    public static class TimeEventBuilder {

        private List<CommonTimeEventView> commonTimeEventViewList;

        private int saveRate = 0;
        private int addSaveRate = 0;
        private int fixedRate = 0;
        private int fixedAmount = 0;
        // true 적립률 숨김, false 적립률 표시
        private boolean hideSaveRate= false;

        public TimeEventBuilder(List<CommonTimeEventView> commonTimeEventViewList) {
            this.commonTimeEventViewList = commonTimeEventViewList;
        }

        public TimeEventBuilder setAddSaveRate(int addSaveRate) {
            this.addSaveRate = addSaveRate;
            return this;
        }

        public TimeEventBuilder setFixedAmount(int fixedAmount) {
            this.fixedAmount = fixedAmount;
            return this;
        }

        public TimeEventBuilder setFixedRate(int fixedRate) {
            this.fixedRate = fixedRate;
            return this;
        }

        public TimeEventBuilder setSaveRate(int saveRate) {
            this.saveRate = saveRate;
            return this;
        }

        public TimeEventBuilder setHideSaveRate(boolean hideSaveRate) {
            this.hideSaveRate = hideSaveRate;
            return this;
        }

        public TimeEventHelper build() {
            return new TimeEventHelper(this);
        }

    }

    private TimeEventHelper(TimeEventBuilder builder) {
        this.commonTimeEventViewList = builder.commonTimeEventViewList;
        this.saveRate = builder.saveRate;
        this.addSaveRate = builder.addSaveRate;
        this.fixedAmount = builder.fixedAmount;
        this.fixedRate = builder.fixedRate;
        this.hideSaveRate = builder.hideSaveRate;
        timeEventInit();
    }

    /**
     * 타임 이벤트 세팅
     */
    public void timeEventInit() {
        int viewTarget = 0;
        if (commonTimeEventViewList != null && commonTimeEventViewList.size() == 4) {

            CommonTimeEventView commonTimeEventView;
            if (saveRate != 0) {
                commonTimeEventView = commonTimeEventViewList.get(viewTarget);
                commonTimeEventView.saveRateType(saveRate);
                commonTimeEventView.setVisibility(View.VISIBLE);
                viewTarget++;
                if (hideSaveRate)
                    commonTimeEventView.setVisibility(View.GONE);
            }

            if (addSaveRate != 0) {
                commonTimeEventView = commonTimeEventViewList.get(viewTarget);
                commonTimeEventView.addSaveRateType(addSaveRate);
                commonTimeEventView.setVisibility(View.VISIBLE);
                viewTarget++;
            }

            if (fixedAmount != 0) {
                commonTimeEventView = commonTimeEventViewList.get(viewTarget);
                commonTimeEventView.fixedAmount(fixedAmount);
                commonTimeEventView.setVisibility(View.VISIBLE);
                viewTarget++;
            }

            if (fixedRate != 0) {
                commonTimeEventView = commonTimeEventViewList.get(viewTarget);
                commonTimeEventView.fixedRate(fixedRate);
                commonTimeEventView.setVisibility(View.VISIBLE);
            }

        }
    }

}
