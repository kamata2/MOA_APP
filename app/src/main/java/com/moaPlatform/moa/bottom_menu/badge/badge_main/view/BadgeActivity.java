package com.moaPlatform.moa.bottom_menu.badge.badge_main.view;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.controller.BadgeController;
import com.moaPlatform.moa.util.custom_view.temp.Responsive.ResponsiveConstraintLayout;
import com.moaPlatform.moa.util.custom_view.temp.Responsive.ResponsiveTextView;
import com.moaPlatform.moa.util.toolbar.TopToolbarController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 업적 화면
 */
public class BadgeActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView badgeList;
    // 코인나무 애니메이션 뷰
    // 꽃잎, 코인 나무
    private LottieAnimationView coinTreeFlowers, coinTree, coinTreeClick, coinTreeDance;
    private ResponsiveConstraintLayout lottieBg;
    private View topToolbar;
    private BadgeController badgeController = new BadgeController();
    // 코인, 코인 현금금액
    private ResponsiveTextView coinText, coinExChange;
    // 포인트, 포인트 현금금액
    private ResponsiveTextView pointText, pointExChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.badge_activity);
        init();
        listenerInit();
        lottieHardwareInit();
        toolbarSetting();
//        getBadgeTreeInfo();
    }

    /**
     * 변수 초기화
     */
    private void init() {
        // 코인 나무의 배경
        ImageView coinTreeBg = findViewById(R.id.coinTreeBg);
        topToolbar = findViewById(R.id.topToolbar);
        badgeList = findViewById(R.id.badgeList);
        coinTreeFlowers = findViewById(R.id.coinTreeFlowers);
        coinTree = findViewById(R.id.coinTree);
        coinTreeClick = findViewById(R.id.coinTreeClick);
        lottieBg = findViewById(R.id.lottieBg);
        coinText = findViewById(R.id.coin);
        coinExChange = findViewById(R.id.coinExChange);
        pointText = findViewById(R.id.point);
        pointExChange = findViewById(R.id.pointExChange);
        coinTreeDance = findViewById(R.id.coinTreeDance);
        badgeController.seekBarSetting(this);

        // 이미지 로드
        Glide.with(this)
                .load(R.drawable.bg_pink)
                .into(coinTreeBg);
    }

    /**
     * 리스너 세팅
     */
    private void listenerInit() {
        lottieBg.setOnClickListener(this);
    }

    /**
     * lottie 애니메이션 하드웨어 가속 세팅
     */
    private void lottieHardwareInit() {
//        VideoView videoTest = findViewById(R.id.videoTest);
//        videoTest.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.test));
//        videoTest.start();
        coinTreeFlowers.useHardwareAcceleration(true);
        coinTree.useHardwareAcceleration(true);
//        treeClick.useHardwareAcceleration(true);
    }

    /**
     * 툴바 세팅
     */
    private void toolbarSetting() {
        TopToolbarController topToolbarController = new TopToolbarController(topToolbar);
        topToolbarController.badgeToolbarInit();
    }

    /**
     * 배지 리스트 세팅
     */
    private void badgeListSetting() {
        badgeController.badgeListSetting(badgeList, this);
    }

    /**
     * 클릭 이벤트
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lottieBg :
                treeAnimationClick();
                break;
        }
    }

    private void treeAnimationClick() {
        badgeController.coinTreeReword(this);
        coinTreeClick.setVisibility(View.VISIBLE);
        coinTreeClick.playAnimation();
        coinTreeClick.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                coinTreeClick.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        coinTree.setVisibility(View.GONE);
        coinTree.useHardwareAcceleration(false);
        coinTreeDance.setVisibility(View.VISIBLE);
        coinTreeDance.useHardwareAcceleration(true);
        coinTreeDance.playAnimation();
        coinTreeDance.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                coinTreeDance.useHardwareAcceleration(false);
                coinTreeDance.setVisibility(View.GONE);
                coinTree.setVisibility(View.VISIBLE);
                coinTree.useHardwareAcceleration(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
//        treeClick.setVisibility(View.VISIBLE);
//        treeClick.playAnimation();
//        treesBg.setVisibility(View.GONE);
//        treesBg.useHardwareAcceleration(false);
//        treeDance.useHardwareAcceleration(true);
//        treeDance.setVisibility(View.VISIBLE);
//        treeDance.playAnimation();
//        // 임시 방편
//        Handler delayHandler = new Handler();
//        delayHandler.postDelayed(()-> {
//            treesBg.useHardwareAcceleration(true);
//            treesBg.setVisibility(View.VISIBLE);
//            treeDance.setVisibility(View.GONE);
//            treeDance.useHardwareAcceleration(false);
//        },2000);

    }

    /**
     * 업적(배지)와 나무의 정보를 받아옴
     */
    private void getBadgeTreeInfo() {
        badgeController.getBadgeTreeInfo(this);
    }

    /**
     * 서버에서 받아온 데이터 반영
     * 코인 및 배지 세팅
     */
    public void eventPageSetting() {
        coinPointSetting();
        badgeListSetting();
    }

    /**
     * 코인과 포인트 세팅
     */
    private void coinPointSetting() {
        badgeController.onCoinOrPointSetting(coinText, coinExChange, badgeController.COIN_TYPE);
        badgeController.onCoinOrPointSetting(pointText, pointExChange, badgeController.POINT_TYPE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        badgeController.timerClear();
    }

}
