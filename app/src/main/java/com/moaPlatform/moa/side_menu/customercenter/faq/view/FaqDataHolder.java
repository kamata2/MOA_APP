package com.moaPlatform.moa.side_menu.customercenter.faq.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moaPlatform.moa.R;

public class FaqDataHolder extends RecyclerView.ViewHolder {
    public TextView tAnswerType;
    public TextView tAnswerTypeOther;
    public TextView getAnswerType2;
    public TextView tDateTime;
    public TextView tTitle;
    public TextView tDetailContent;
    public RelativeLayout rMainRelativeLayout;
    public ImageView mArrow, mArrowDown;

    public FaqDataHolder(@NonNull View v) {
        super(v);
        mArrow = v.findViewById(R.id.side_customercenter_myquestion_up);
        mArrowDown = v.findViewById(R.id.side_customercenter_myquestion_down);
        tAnswerType = v.findViewById(R.id.myquestion_type);
        tAnswerTypeOther = v.findViewById(R.id.myquestion_type_finish);
        tDateTime = v.findViewById(R.id.myquestion_time);
        getAnswerType2 = v.findViewById(R.id.myquestion_type2);
        tTitle = v.findViewById(R.id.myquestion_title);
        tDetailContent = v.findViewById(R.id.customer_myquestion_detail_data);
        rMainRelativeLayout = v.findViewById(R.id.relative_parent);
    }
}

