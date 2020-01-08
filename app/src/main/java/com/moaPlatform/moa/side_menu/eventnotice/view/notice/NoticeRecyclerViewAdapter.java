package com.moaPlatform.moa.side_menu.eventnotice.view.notice;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.eventnotice.model.EventOrNoticeItems;
import com.moaPlatform.moa.side_menu.eventnotice.view.event.EventSubWebActivity;

import java.util.List;

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<NoticeHolder> {
    private List<EventOrNoticeItems> mNoticeItem;

    @NonNull
    @Override
    public NoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.side_event_noti_notiitem, parent, false);
        return new NoticeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeHolder holder, int position) {
        final EventOrNoticeItems eventOrNoticeItems = mNoticeItem.get(position);
        holder.tTitleText.setText(eventOrNoticeItems.title);
        holder.tTimeText.setText(eventOrNoticeItems.startDt);
        holder.rParentRelative.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, EventSubWebActivity.class);

            intent.putExtra("loadurl", eventOrNoticeItems.dtlUrl);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return mNoticeItem == null ? 0 : mNoticeItem.size();
    }

    public void setData(List<EventOrNoticeItems> mNoticeItem) {
        this.mNoticeItem = mNoticeItem;
    }
}
