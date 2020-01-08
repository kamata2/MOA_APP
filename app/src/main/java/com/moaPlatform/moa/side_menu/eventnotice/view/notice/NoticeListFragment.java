package com.moaPlatform.moa.side_menu.eventnotice.view.notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.eventnotice.model.EventOrNoticeItems;
import com.moaPlatform.moa.side_menu.eventnotice.model.ResEventOrNotice;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeListFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private NoticeRecyclerViewAdapter noticeRecyclerViewAdapter;
    private LinearLayout llNoticeListEmptyData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.side_event_fragment_noticelist, container, false);
        initView();
        settingAdapter();
        noticeList();
        return view;
    }

    private void noticeList() {
        CommonModel commonModel = new CommonModel();
        RetrofitClient.getInstance().getMoaService().noticeList(commonModel).enqueue(new Callback<ResEventOrNotice>() {

            @Override
            public void onResponse(@NonNull Call<ResEventOrNotice> call, @NonNull Response<ResEventOrNotice> response) {
                ResEventOrNotice resEventOrNotice = response.body();
                if (resEventOrNotice == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resEventOrNotice)) {
                    noticeList();
                    return;
                }
                listUpdate(resEventOrNotice.noticeList);
            }

            @Override
            public void onFailure(@NonNull Call<ResEventOrNotice> call, @NonNull Throwable t) {
                Logger.d(t.getMessage());
                Toast.makeText(getContext(), getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        llNoticeListEmptyData = view.findViewById(R.id.llNoticeListEmptyData);
        llNoticeListEmptyData.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.noticeRecyclerView);
    }

    private void settingAdapter() {

        noticeRecyclerViewAdapter = new NoticeRecyclerViewAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noticeRecyclerViewAdapter);
    }

    //리스트 업데이트 작업할 곳
    private void listUpdate(List<EventOrNoticeItems> arrayList) {
        if (arrayList != null) {
            noticeRecyclerViewAdapter.setData(arrayList);
            noticeRecyclerViewAdapter.notifyDataSetChanged();
        }

        if (noticeRecyclerViewAdapter != null) {
            if (noticeRecyclerViewAdapter.getItemCount() > 0) {
                llNoticeListEmptyData.setVisibility(View.GONE);
            } else {
                llNoticeListEmptyData.setVisibility(View.VISIBLE);
            }
        }
    }
}
