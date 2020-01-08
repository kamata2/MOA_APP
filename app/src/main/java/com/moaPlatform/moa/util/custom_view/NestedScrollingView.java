package com.moaPlatform.moa.util.custom_view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.widget.NestedScrollView;

import com.moaPlatform.moa.util.Logger;

/**
 * NestedScrollView Scroll Idle 상태 체크
 *
 * [문제]stopNestedScroll 플리킹하듯이 스크롤하면 해당 부분이 call 되지 않는 이슈가 있었음
 * [해결]https://stackoverflow.com/questions/8181828/android-detect-when-scrollview-stops-scrolling 스크롤에서 사용하는 내용 참조
 */
public class NestedScrollingView extends NestedScrollView {

    private NestedScrollViewScrollStateListener scrollListener;

    public static final String SCROLL_STATE_STOP = "SCROLL_STATE_STOP";
    public static final String SCROLL_STATE_START = "SCROLL_STATE_START";
    private long lastScrollUpdate = -1;
    private final int POST_DELAY_TIME = 100;

    private class ScrollStateHandler implements Runnable {

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastScrollUpdate) > POST_DELAY_TIME) {
                lastScrollUpdate = -1;
                dispatchScrollState(SCROLL_STATE_STOP);
            } else {
                postDelayed(this, POST_DELAY_TIME);
            }
        }
    }

    public NestedScrollingView(Context context) {
        super(context);
    }

    public NestedScrollingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        Logger.d("startNestedScroll >>>>>> startNestedScroll");
        return super.startNestedScroll(axes, type);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Logger.d("startNestedScroll >>>>>> onScrollChanged");
        if (lastScrollUpdate == -1) {
            dispatchScrollState(SCROLL_STATE_START);
            postDelayed(new ScrollStateHandler(), POST_DELAY_TIME);
        }
        lastScrollUpdate = System.currentTimeMillis();
    }

//    @Override
//    public void stopNestedScroll() {
//        super.stopNestedScroll();
//        Logger.d("NestedScrollingView >>>>>> stopNestedScroll");
//        dispatchScrollState(SCROLL_STATE_STOP);
//    }

    private void dispatchScrollState(String action) {
        if (scrollListener != null && action != null) {
            scrollListener.onNestedScrollViewStateChanged(action);
        }
    }

    public interface NestedScrollViewScrollStateListener {
        void onNestedScrollViewStateChanged(String action);
    }

    public void setScrollListener(NestedScrollViewScrollStateListener scrollListener) {
        this.scrollListener = scrollListener;
    }
}
