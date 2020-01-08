package com.moaPlatform.moa.bottom_menu.badge.badge_main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.model.res.AchvmGroupDtoList;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.view.BadgeActivity;
import com.moaPlatform.moa.bottom_menu.badge.dialog.reward_check.AchievementRewardCheckDialog;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BadgeListAdapter extends RecyclerView.Adapter<BadgeListViewHolder> {
    private ArrayList<AchvmGroupDtoList> achvmGroupDtoList;
    private AchievementRewardCheckDialog achievementRewardCheckDialog = new AchievementRewardCheckDialog();
    private Context context;
    /**
     * 어댑터 초기화
     * @param achvmGroupDtoList
     * 업적 리스트
     */
    public BadgeListAdapter(ArrayList<AchvmGroupDtoList> achvmGroupDtoList, Context context) {
        this.achvmGroupDtoList = achvmGroupDtoList;
        this.context = context;
    }

    @NonNull
    @Override
    public BadgeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.badge_list_item, parent, false);
        return new BadgeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeListViewHolder holder, int position) {
        AchvmGroupDtoList achvmGroupDtoL = achvmGroupDtoList.get(position);
        // 배지 이미지 타입
        int imageValue = R.drawable.badge_review;
        if (position % 3 == 0) {
            imageValue = R.drawable.badge_newbie;
        } else if (position % 3 == 1){
            imageValue = R.drawable.badge_signup;
        }

        holder.badgeTitle.setText(achvmGroupDtoL.achvmGroupNm);
        holder.badgeSubTitle.setText("/ "+achvmGroupDtoL.achvmExpn);
        holder.badgeIcon.glideImgLoad(imageValue);
        holder.badgeRangeBar.rangeBarInit(achvmGroupDtoL.achvmInfoList);
        holder.badgeRangeBar.rangeSetting(achvmGroupDtoL.achvmInfoList.size(), achvmGroupDtoL.achvmInfoList, achvmGroupDtoL.cmplFigure);
        holder.badgeQuestion.setOnClickListener((View v) -> {
            achievementRewardCheckDialog.show(((BadgeActivity)context).getSupportFragmentManager(), "dialog");
        });

    }

    @Override
    public int getItemCount() {
        return achvmGroupDtoList.size();
    }
}
