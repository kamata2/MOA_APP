package com.moaPlatform.moa.side_menu.usernotice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.settings.noticeagree.NoticeAgreeSettingActivity;
import com.moaPlatform.moa.side_menu.usernotice.model.ResUserNotice;
import com.moaPlatform.moa.side_menu.usernotice.model.UserNoticeItems;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserNoticeActivity extends AppCompatActivity {
    ExpandableListView expandableListView;
    CustomExpandableListAdapter customExpandableListAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_alarm);

        expandableListView = findViewById(R.id.expandableListView);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        context = this;

        TextView alarmSetting = findViewById(R.id.alarm);
        alarmSetting.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoticeAgreeSettingActivity.class);
            startActivity(intent);
        });
        initAdapter();
        requestUserNoticeList();
    }

    private void initAdapter() {
        customExpandableListAdapter = new CustomExpandableListAdapter(this);
        expandableListView.setAdapter(customExpandableListAdapter);
    }

    private void updateNoticeList(List<UserNoticeItems> userNoticeItems) {
        if (userNoticeItems == null)
            return;
        customExpandableListAdapter.setData(userNoticeItems);
        customExpandableListAdapter.notifyDataSetChanged();
        if (userNoticeItems.size() == 0) {

            expandableListView.setVisibility(View.GONE);
            final LinearLayout linearLayout = findViewById(R.id.no_alarm);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void requestUserNoticeList() {
        CommonModel commonModel = new CommonModel();
        RetrofitClient.getInstance().getMoaService().alarmSelect(commonModel).enqueue(new Callback<ResUserNotice>() {
            @Override
            public void onResponse(@NonNull Call<ResUserNotice> call, @NonNull Response<ResUserNotice> response) {
                ResUserNotice resUserNotice = response.body();
                if (resUserNotice == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resUserNotice)) {
                    requestUserNoticeList();
                    return;
                }
                updateNoticeList(resUserNotice.arrayList);
            }

            @Override
            public void onFailure(@NonNull Call<ResUserNotice> call, @NonNull Throwable t) {
                Logger.d(t.getMessage());
                Toast.makeText(context, getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
