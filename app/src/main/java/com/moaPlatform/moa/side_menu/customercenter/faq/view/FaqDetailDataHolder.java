package com.moaPlatform.moa.side_menu.customercenter.faq.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moaPlatform.moa.R;

class FaqDetailDataHolder extends RecyclerView.ViewHolder {
    TextView tTitleText;
    TextView tDetailText;
    RelativeLayout rParentRelative;
    ImageView arrow, arrowDown;

    FaqDetailDataHolder(@NonNull View v) {
        super(v);
        tTitleText = v.findViewById(R.id.customercenter_noti_text);
        tDetailText = v.findViewById(R.id.customer_detail_data);
        rParentRelative = v.findViewById(R.id.customre_relative);
        arrow = v.findViewById(R.id.side_customercenter_main_next_top);
        arrowDown = v.findViewById(R.id.side_customercenter_main_next);
    }
}
