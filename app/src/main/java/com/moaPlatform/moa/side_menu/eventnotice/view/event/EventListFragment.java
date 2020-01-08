package com.moaPlatform.moa.side_menu.eventnotice.view.event;

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

public class EventListFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;
    private LinearLayout llEventListEmptyData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.side_event_fragment_eventlist, container, false);
        initView();
        settingAdapter();
        getEventList();
        return view;
    }

    private void getEventList() {
        CommonModel commonModel = new CommonModel();

        RetrofitClient.getInstance().getMoaService().eventList(commonModel).enqueue(new Callback<ResEventOrNotice>() {
            @Override
            public void onResponse(@NonNull Call<ResEventOrNotice> call, @NonNull Response<ResEventOrNotice> response) {
                ResEventOrNotice resEventOrNotice = response.body();
                if (resEventOrNotice == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resEventOrNotice)) {
                    getEventList();
                    return;
                }
                if (getActivity() != null && !getActivity().isFinishing()) {
                    listUpdate(resEventOrNotice.noticeList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResEventOrNotice> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                listUpdate(null);
                Toast.makeText(view.getContext(), getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.eventRecyclerView);
        llEventListEmptyData = view.findViewById(R.id.llEventListEmptyData);
        llEventListEmptyData.setVisibility(View.GONE);
    }

    private void settingAdapter() {
        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(eventRecyclerViewAdapter);
    }

    private void listUpdate(List<EventOrNoticeItems> arrayList) {
        if (arrayList != null) {
            eventRecyclerViewAdapter.setData(arrayList);
            eventRecyclerViewAdapter.notifyDataSetChanged();
        }
        if (eventRecyclerViewAdapter != null) {
            if (eventRecyclerViewAdapter.getItemCount() > 0) {
                llEventListEmptyData.setVisibility(View.GONE);
            } else {
                llEventListEmptyData.setVisibility(View.VISIBLE);
            }
        }
    }
}

