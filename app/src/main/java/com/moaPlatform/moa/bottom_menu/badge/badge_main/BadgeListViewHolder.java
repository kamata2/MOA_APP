package com.moaPlatform.moa.bottom_menu.badge.badge_main;

import android.view.View;
import android.widget.ImageView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.range_bar.RangeBar;
import com.moaPlatform.moa.util.custom_view.temp.Responsive.ResponsiveImageView;
import com.moaPlatform.moa.util.custom_view.temp.Responsive.ResponsiveTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BadgeListViewHolder extends RecyclerView.ViewHolder {

    ResponsiveTextView badgeTitle;
    ResponsiveTextView badgeSubTitle;
    RangeBar badgeRangeBar;
    ResponsiveImageView badgeIcon;
    ImageView badgeQuestion;

    public BadgeListViewHolder(@NonNull View itemView) {
        super(itemView);
        badgeTitle = itemView.findViewById(R.id.badgeTitle);
        badgeSubTitle = itemView.findViewById(R.id.badgeSubTitle);
        badgeRangeBar = itemView.findViewById(R.id.badgeRangeBar);
        badgeIcon = itemView.findViewById(R.id.badgeIcon);
        badgeQuestion = itemView.findViewById(R.id.badgeQuestion);
    }
}
