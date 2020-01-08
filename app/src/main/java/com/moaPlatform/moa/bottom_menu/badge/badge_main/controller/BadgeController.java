package com.moaPlatform.moa.bottom_menu.badge.badge_main.controller;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.BadgeListAdapter;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.model.req.ReqBadgeModel;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.model.res.AchvmGroupDtoList;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.model.res.ResBadgeDataModel;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.model.res.ResCoinTreeReword;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.view.BadgeActivity;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.custom_view.temp.Responsive.ResponsiveTextView;
import com.moaPlatform.moa.util.models.MoneyConverter;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BadgeController implements MoneyConverter{
    private final String TAG = BadgeController.class.getCanonicalName();
    private ResBadgeDataModel resBadgeDataModel;
    // 코인 및 포인트 타입
    public final String COIN_TYPE = "C";
    public final String POINT_TYPE = "P";
    // 코인나무 적재량 게이지바
    private SeekBar coinTreeCryngCpctySkbr;
    // 코인 수명 및 코인 적재량 체크시 사용할 타이머
    private Timer timer = new Timer();
    // TimerTask 저장할 array
    private SparseArray<TimerTask> timerTaskSparseArray = new SparseArray<>();
    // 코인 나무의 수명
    private final int COIN_TREE_LIFE = 0;
    // 코인 나무의 코인 적재량
    private final int COIN_TREE_COIN_CARRYING_CAPACITY = 1;

//    AchievementRewardDialog achievementRewardDialog = new AchievementRewardDialog();
//        achievementRewardDialog.show(getSupportFragmentManager(), "dialog");

    /**
     * 업적(배지) 및 트리 정보를 서버에서 받아옴
     * @param activity
     * 액티비티 정보
     */
    public void getBadgeTreeInfo(Activity activity) {
        ReqBadgeModel reqBadgeModel = new ReqBadgeModel();
        RetrofitClient.getInstance().getMoaService().getEventPageInfo(reqBadgeModel).enqueue(new Callback<ResBadgeDataModel>() {
            @Override
            public void onResponse(@NonNull Call<ResBadgeDataModel> call, @NonNull Response<ResBadgeDataModel> response) {
                resBadgeDataModel = response.body();
                if (resBadgeDataModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resBadgeDataModel)) {
                    getBadgeTreeInfo(activity);
                    return;
                }
                ((BadgeActivity)activity).eventPageSetting();
                coinTreeSetting();
            }
            @Override
            public void onFailure(@NonNull Call<ResBadgeDataModel> call, @NonNull Throwable t) {
                Logger.i("onFailure: " + t.toString());
            }
        });
    }

    /**
     * 코인 또는 포인트의 금액값을 세팅
     * ※차후 코인 및 포인트의 시세 반영된 값은 서버에서 전송해줌
     * @param moneyTextView
     * 코인 또는 포인트 금액을 표시할 텍스트뷰
     * @param exChangeTextView
     * 코인 또는 포인트의 금액을 현금화한 값을 보여줄 텍스트뷰
     * @param type
     * 코인 인지 포인트인지 확인할 타입값
     */
    public void onCoinOrPointSetting(ResponsiveTextView moneyTextView, ResponsiveTextView exChangeTextView, String type) {
//        int money = type.equals(COIN_TYPE) ? resBadgeDataModel.elecWaltInfoModel.coinBal : resBadgeDataModel.elecWaltInfoModel.pointBal;
        int money = type.equals(COIN_TYPE) ? 1000 : 300;
        MoneyConverter converter = this;
        String convertMoney = converter.commaUnitChange(money);
        String exChangeValue = "( \\"+convertMoney+" )";
        moneyTextView.setText((convertMoney+type));
        exChangeTextView.setText(exChangeValue);
    }

    /**
     * 배지 리스트 세팅
     */
    public void badgeListSetting(RecyclerView badgeList, Context context) {
        BadgeListAdapter badgeListAdapter = new BadgeListAdapter(achievementListFilter(), context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(badgeList.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        badgeList.setLayoutManager(linearLayoutManager);
        badgeList.setAdapter(badgeListAdapter);
    }

    /**
     * 서버에서 받을 업적리스트 필터링
     * 업적은 같은 그룹으로 여러개 있을경우 그중에 1개만 표시
     * ex) CT_GC 라는 업적 그룹이 3개가 있으면 그중에서 1단계만 표시
     * 1단계 클리어시 다음단계인 2단계 표시
     * @return
     * 팔터링된 업적 리스트 반환
     */
    private ArrayList<AchvmGroupDtoList> achievementListFilter() {
        // 업적그룹 필터링 하고 저장된 업적 리스트들
        LinkedHashMap<String, AchvmGroupDtoList> achievementMap = new LinkedHashMap<>();
        for (AchvmGroupDtoList achvmGroupDto : resBadgeDataModel.achvmGroupDtoList) {
            if (achievementMap.get(achvmGroupDto.achvmSepa) == null) {
                achievementMap.put(achvmGroupDto.achvmSepa, achvmGroupDto);
            }
        }
        return new ArrayList<>(achievementMap.values());
    }

    /**
     * 코인 나무 세팅
     */
    private void coinTreeSetting() {
        if (timerTaskSparseArray.get(COIN_TREE_COIN_CARRYING_CAPACITY) == null) {
            timerTaskSparseArray.append(COIN_TREE_COIN_CARRYING_CAPACITY, coinTreeCryngCpcty());
        } else {
            timerTaskSparseArray.setValueAt(COIN_TREE_COIN_CARRYING_CAPACITY, coinTreeCryngCpcty());
        }

        if (timerTaskSparseArray.get(COIN_TREE_LIFE) == null) {
            timerTaskSparseArray.append(COIN_TREE_LIFE, coinTreeLife());
        } else {
            timerTaskSparseArray.setValueAt(COIN_TREE_LIFE, coinTreeLife());
        }
        resBadgeDataModel.dplCoinTreeModel.coinTreeSeekBarSetting(coinTreeCryngCpctySkbr);
        coinTreeTimerStart();
    }

    /**
     * 코인 나무 수명 및 코인 나무의 코인 적재량을 체크할 타이머를 돌림
     */
    private void coinTreeTimerStart() {
        timer.schedule(timerTaskSparseArray.get(COIN_TREE_COIN_CARRYING_CAPACITY), 0, 60000);
        timer.schedule(timerTaskSparseArray.get(COIN_TREE_LIFE), 0, 1000);
    }

    /**
     * 코인나무 보상 받기
     * @param context
     * context
     */
    public void coinTreeReword(Context context) {
        ReqBadgeModel reqBadgeModel = new ReqBadgeModel();
        reqBadgeModel.coinTreeId = resBadgeDataModel.dplCoinTreeModel.coinTreeId;
        RetrofitClient.getInstance().getMoaService().getCoinTreeReword(reqBadgeModel).enqueue(new Callback<ResCoinTreeReword>() {
            @Override
            public void onResponse(@NonNull Call<ResCoinTreeReword> call, @NonNull Response<ResCoinTreeReword> response) {
                ResCoinTreeReword resCoinTreeReword = response.body();
                if (resCoinTreeReword == null || resCoinTreeReword.receiveCoin < -1)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resCoinTreeReword)) {
                    coinTreeReword(context);
                    return;
                }
                coinTreeCryngCpctySkbr.setProgress(0);
                Toast.makeText(context, resCoinTreeReword.receiveCoin+ "코인이 적립되었습니다", Toast.LENGTH_SHORT).show();
                if (resBadgeDataModel.dplCoinTreeModel.coinTreeDeath()) {
                    timerTaskSparseArray.get(COIN_TREE_COIN_CARRYING_CAPACITY).cancel();
                    timerTaskSparseArray.get(COIN_TREE_LIFE).cancel();
                    resBadgeDataModel.dplCoinTreeModel = resCoinTreeReword.dplCoinTreeModel;
                    coinTreeSetting();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResCoinTreeReword> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * 코인 나무의 코인 적재량 체크할 타이머 테스트
     */
    private TimerTask coinTreeCryngCpcty() {

        return new TimerTask() {
            @Override
            public void run() {
                if (resBadgeDataModel.dplCoinTreeModel.nowLdagUpdate()) {
                    coinTreeCryngCpctySkbr.setProgress(coinTreeCryngCpctySkbr.getMax());
                    this.cancel();
                } else {
                    coinTreeCryngCpctySkbr.setProgress(resBadgeDataModel.dplCoinTreeModel.nowLdag);
                }
            }
        };
    }

    /**
     * 코인 나무의 수명 적재량 체크할 타이머 테스트
     */
    private TimerTask coinTreeLife() {
        return new TimerTask() {
            @Override
            public void run() {
                if (resBadgeDataModel.dplCoinTreeModel.coinTreeUpdate()) {
                    this.cancel();
                }
            }
        };
    }

    /**
     * 코인 나무의 적재량 게이지바 세팅
     * @param activity
     * 액티비티
     */
    public void seekBarSetting(Activity activity) {
        coinTreeCryngCpctySkbr = activity.findViewById(R.id.coinTreeCryngCpctySkbr);
    }

    /**
     * 현재 실행중인 타이머를 모두 종료
     */
    public void timerClear() {
        timer.cancel();
    }

}
