package com.moaPlatform.moa.side_menu.eventnotice.view.notice;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moaPlatform.moa.R;

class NoticeHolder extends RecyclerView.ViewHolder {
    TextView tTitleText, tTimeText;
    RelativeLayout rParentRelative;

    NoticeHolder(@NonNull View v) {
        super(v);
        tTitleText = v.findViewById(R.id.event_noti_text1);
        tTimeText = v.findViewById(R.id.event_noti_text2);
        rParentRelative = v.findViewById(R.id.side_relative_notice);
    }
}
