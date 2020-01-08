package com.moaPlatform.moa.util.custom_view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.moaPlatform.moa.R;

import java.util.Random;

import androidx.annotation.Nullable;
import butterknife.BindView;

/**
 * Created by jiwun on 2019-06-18.
 */
public class CommonLoadingView extends BaseLinearLayoutView {
    // 로딩 애니메이션 리스트
    private String[] animationList = {"loading_bread", "loading_cook_steak", "loading_soybean"};
    @BindView(R.id.ltLoadingView)
    LottieAnimationView ltLoadingView;
    // 애니메이션을 랜덤으로 선택하기위해 필요
    private Random random;
    private boolean isTouchAbleProgressBackground;        //프로그래스바가 움직이는동안 뷰의 터치영역을 허용하는 플래그

    public CommonLoadingView(Context context) {
        super(context);
    }

    public CommonLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int layoutResourceId() {
        return R.layout.common_loading_view;
    }

    @Override
    void initViews() {
        // 최상위로 올리기
        this.bringToFront();
        // 애니메이션 랜덤 재생 관련
        random = new Random();
    }

    /**
     * 애니메이션 랜덤으로 플레이
     */
    public void loadingAnimationPlay() {
        // 애니메이션 렌덤으로 선택
        int randomNumber = random.nextInt(animationList.length);
        final String ANIMATION_NAME = animationList[randomNumber];
        // 랜덤한 애니메이션을 세팅후 플레이
        ltLoadingView.setImageAssetsFolder(ANIMATION_NAME);
        ltLoadingView.setAnimation(ANIMATION_NAME + ".json");
        ltLoadingView.playAnimation();
        this.setVisibility(VISIBLE);
    }

    /**
     * 애니메이션 랜덤으로 플레이
     * activity 를 받을 경우 화면 터치를 막음
     *
     * @param activity 액티비티
     */
    public void loadingAnimationPlay(Activity activity) {
        loadingAnimationPlay();
        if(!isTouchAbleProgressBackground){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    /**
     * 애니메이션 정지
     */
    public void animationStop() {
        this.setVisibility(GONE);
        ltLoadingView.cancelAnimation();
    }

    /**
     * 애니메이션 정지
     * activity 를 받을경우 터치를 활성화
     *
     * @param activity 액티비티
     */
    public void animationStop(Activity activity) {
        animationStop();
        if(!isTouchAbleProgressBackground){
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public void setTouchAbleProgressBackground(boolean touchAbleProgressBackground) {
        isTouchAbleProgressBackground = touchAbleProgressBackground;
    }

}
