package com.moaPlatform.moa.side_menu.customercenter.myquestion.view;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.customercenter.myquestion.model.MyQuestionItems;
import com.moaPlatform.moa.side_menu.customercenter.myquestion.model.ReqMyQuestion;
import com.moaPlatform.moa.side_menu.customercenter.myquestion.model.ResMyQuestion;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import java.util.List;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyQuestionMainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyQuestionRecyclerViewAdapter mRecycleViewAdapter;
    private RelativeLayout noQuestion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_customercenter_my_question);

        initView();
        settingAdapter();
        initData();
    }

    private void initView() {
        noQuestion = findViewById(R.id.no_question);
        mRecyclerView = findViewById(R.id.questionRecyclerView);
        CommonTitleView commonTitleOrderList = findViewById(R.id.commonTitleMyReview);
        commonTitleOrderList.setBackButtonClickListener(v -> finish());
        commonTitleOrderList.setTitleName(getString(R.string.side_myquestion));
    }

    private void settingAdapter() {
        mRecycleViewAdapter = new MyQuestionRecyclerViewAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecycleViewAdapter);
    }

    private void initData() {
        ReqMyQuestion reqMyQuestion = new ReqMyQuestion();

        reqMyQuestion.mmbrSepa = "01";
        RetrofitClient.getInstance().getMoaService().InqryList(reqMyQuestion).enqueue(new Callback<ResMyQuestion>() {

            @Override
            public void onResponse(@NonNull Call<ResMyQuestion> call, @NonNull Response<ResMyQuestion> response) {
                ResMyQuestion resMyQuestion = response.body();
                if (resMyQuestion == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resMyQuestion)) {
                    initData();
                    return;
                }
                listUpdate(resMyQuestion.InqryLists);
            }

            @Override
            public void onFailure(@NonNull Call<ResMyQuestion> call, @NonNull Throwable t) {
                Logger.d(t.getMessage());
            }
        });
    }

    private void listUpdate(List<MyQuestionItems> arrayList) {
        if (arrayList == null)
            return;
        mRecycleViewAdapter.setData(arrayList);
        mRecycleViewAdapter.notifyDataSetChanged();

        if (arrayList.size() == 0) {
            emptyView();
        }
    }

    public void emptyView() {
        mRecyclerView.setVisibility(View.GONE);
        noQuestion.setVisibility(View.VISIBLE);
    }
}