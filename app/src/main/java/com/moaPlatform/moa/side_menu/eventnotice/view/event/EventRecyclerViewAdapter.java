package com.moaPlatform.moa.side_menu.eventnotice.view.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.activity.EventWebViewActivity;
import com.moaPlatform.moa.side_menu.eventnotice.model.EventOrNoticeItems;
import com.moaPlatform.moa.util.DeviceUtil;
import com.moaPlatform.moa.util.ObjectUtil;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventHolder> {
    private Activity activity;
    private List<EventOrNoticeItems> eventOrNoticeItems;

    EventRecyclerViewAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.side_event_noti_eventitem, parent, false);
        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        final EventOrNoticeItems eventOrNoticeItems = this.eventOrNoticeItems.get(position);
        String newYn = eventOrNoticeItems.chckYn;

        if (newYn.equals("N")) {
            holder.iNewYnIcon.setVisibility(View.GONE);
        } else {
            holder.iNewYnIcon.setVisibility(View.VISIBLE);
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.iEventImage.getLayoutParams();
        params.width = DeviceUtil.getScreenWidth(activity);
        double aspect = 2.297;
        params.height = (int) (params.width / aspect);

        glideImages(holder.iEventImage, eventOrNoticeItems.img);
        holder.tStartData.setText(eventOrNoticeItems.startDt);
        holder.tEndDate.setText(eventOrNoticeItems.endDt);

        holder.rEventRelative.setOnClickListener(v -> {
                    Context context = v.getContext();
                    if (ObjectUtil.checkNotNull(eventOrNoticeItems.dtlUrl)) {
                        Intent intent = new Intent(context, EventWebViewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, BuildConfig.REST_API_BASE_URL + eventOrNoticeItems.dtlUrl);
                        String memberId = MoaAuthHelper.getInstance().getCurrentMemberID();
                        if (memberId.contains("@")) {
                            intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL_PARMS, "isLogin=Y");
                        } else {
                            intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL_PARMS, "isLogin=N");
                        }
                        intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_TRANSPARENT_VALUE);
                        context.startActivity(intent);
                    }
                }
        );
    }

    private void glideImages(ImageView imageview, String subUrl) {
        try {
            Glide.with(activity.getApplicationContext())
                    .asBitmap()
                    .load(BuildConfig.FILE_SERVER_BASE_URL + subUrl)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(imageview);
        } catch (Exception ignored) {
        }
    }

    @Override
    public int getItemCount() {
        return this.eventOrNoticeItems == null ? 0 : eventOrNoticeItems.size();
    }

    public void setData(List<EventOrNoticeItems> eventOrNoticeItems) {
        this.eventOrNoticeItems = eventOrNoticeItems;
    }
}
