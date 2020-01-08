package com.moaPlatform.moa.bottom_menu.badge.badge_main.model.res;

import android.widget.SeekBar;

public class DplCoinTreeModel {
    // 코인나무_ID
    public int coinTreeId;
    // 코인나무명
//    private String coinTreeNm;
    // 스킨
//    private String skin;
    // 등급
//    private int grd;
    // 나이
//    private int age;
    // 수명
    private int life;
    // 총적재량
    private int totLdag;
    // 분당적재량
    private int mntLdag;
    // 현재적재량
    public int nowLdag;

    /**
     * 현재 적재량을 업데이트
     * @return
     * 현재 적재량이 최대치인지 체크후 boolean 으로 반환
     */
    public boolean nowLdagUpdate() {
        nowLdag += mntLdag;
        return nowLdag >= totLdag;
    }

    /**
     * 코인 적제량 표시해주는 게이지바 세팅
     * @param seekBar
     * 게이지바
     */
    public void coinTreeSeekBarSetting(SeekBar seekBar) {
        seekBar.setProgress(nowLdag);
        seekBar.setMax(totLdag);
    }


    /**
     * 코인 나무가 죽었는지 체크
     * @return
     * 코인 나무의 수명이 아직 살아있는지 판단후 살았으면 false 죽었으면 true 를 반환
     */
    public boolean coinTreeDeath() {
        return life <= 0;
    }

    /**
     * 코인나무 수명 업데이트
     * @return
     * 코인 나무의 수명이 아직 살아있는지 판단후 살았으면 false 죽었으면 true 를 반환
     */
    public boolean coinTreeUpdate() {
        life--;
        return coinTreeDeath();
    }
}
