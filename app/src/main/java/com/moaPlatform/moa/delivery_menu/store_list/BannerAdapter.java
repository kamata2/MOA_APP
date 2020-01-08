package com.moaPlatform.moa.delivery_menu.store_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.delivery_menu.store_list.model.ResBannerModel;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;

import java.util.ArrayList;

public class BannerAdapter extends PagerAdapter {

    public static final String CLICK_BANNER_IMAGE = "CLICK_BANNER_IMAGE";
    private ArrayList<ResBannerModel.BannerInfoModel> topBannerList;
    private ListItemClickListener listItemClickListener;

    public BannerAdapter(ListItemClickListener listener){
        listItemClickListener = listener;
    }

    public void setTopbannerList(ArrayList<ResBannerModel.BannerInfoModel> topBannerList) {
        this.topBannerList = topBannerList;
    }

    @Override
    public int getCount() {
        return topBannerList == null ? 0 : topBannerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View addView = inflater.inflate(R.layout.store_list_banner_item, container, false);
        ImageView thumbNail = addView.findViewById(R.id.thumbNail);
        thumbNail.setOnClickListener(v -> {
            if(listItemClickListener != null){
                listItemClickListener.clickItem(CLICK_BANNER_IMAGE, position, topBannerList.get(position).moveUrl);
            }
        });
        TextView tvBannerCount = addView.findViewById(R.id.tvBannerCount);
        tvBannerCount.setText(addView.getResources().getString(R.string.banner_count, position+1, getCount()));
        Glide.with(addView)
                .load(BuildConfig.FILE_SERVER_BASE_URL + topBannerList.get(position).thumbNail)
                .into(thumbNail);
        container.addView(addView);
        return addView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }


}
