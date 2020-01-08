package com.moaPlatform.moa.side_menu.eventnotice.view.event;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moaPlatform.moa.R;

class EventHolder extends RecyclerView.ViewHolder {
    ImageView iEventImage, iNewYnIcon;
    TextView tStartData, tEndDate;
    RelativeLayout rEventRelative;

    EventHolder(@NonNull View itemView) {
        super(itemView);

        iEventImage = itemView.findViewById(R.id.image_event_row);
        tStartData = itemView.findViewById(R.id.start_date_event);
        tEndDate = itemView.findViewById(R.id.end_date_event);
        iNewYnIcon = itemView.findViewById(R.id.event_new_icon);
        rEventRelative = itemView.findViewById(R.id.side_relative_event);
    }
}
