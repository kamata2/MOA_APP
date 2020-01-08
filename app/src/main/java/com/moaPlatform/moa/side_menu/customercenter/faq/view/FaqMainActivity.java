package com.moaPlatform.moa.side_menu.customercenter.faq.view;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.customercenter.faq.model.FaqDetailItems;
import com.moaPlatform.moa.side_menu.customercenter.faq.model.FaqModel;
import com.moaPlatform.moa.side_menu.customercenter.faq.model.FaqTitleItems;
import com.moaPlatform.moa.side_menu.customercenter.faq.model.ResFaq;
import com.moaPlatform.moa.side_menu.customercenter.faq.model.ResFaqDetailItems;
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

public class FaqMainActivity extends AppCompatActivity {

    private Button[] questionBtn = new Button[8];
    private RecyclerView mRecyclerView;
    private FaqDetailDataRecyclerViewAdapter msRecycleViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_customercenter_ask_question);

        initView();
        settingAdapter();
        initData();
        initAction();
    }

    private void initView() {
        questionBtn[0] = findViewById(R.id.askquestion1);
        questionBtn[1] = findViewById(R.id.askquestion2);
        questionBtn[2] = findViewById(R.id.askquestion3);
        questionBtn[3] = findViewById(R.id.askquestion4);
        questionBtn[4] = findViewById(R.id.askquestion5);
        questionBtn[5] = findViewById(R.id.askquestion6);
        questionBtn[6] = findViewById(R.id.askquestion7);
        questionBtn[7] = findViewById(R.id.askquestion8);

        mRecyclerView = findViewById(R.id.askQuestionRecyclerView);
        CommonTitleView commonTitleOrderList = findViewById(R.id.commonTitleMyReview);
        commonTitleOrderList.setBackButtonClickListener(v -> finish());
        commonTitleOrderList.setTitleName(getString(R.string.side_question_list));
        commonTitleOrderList.isShowBottomLine(false);
    }

    private void settingAdapter() {
        msRecycleViewAdapter = new FaqDetailDataRecyclerViewAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(msRecycleViewAdapter);
    }

    private void initData() {
        RetrofitClient.getInstance().getMoaService().faqList().enqueue(new Callback<ResFaq>() {

            @Override
            public void onResponse(@NonNull Call<ResFaq> call, @NonNull Response<ResFaq> response) {
                ResFaq resFaq = response.body();
                if (resFaq == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resFaq)) {
                    initData();
                    return;
                }
                btnUpdate(resFaq.faqLists);
            }

            @Override
            public void onFailure(@NonNull Call<ResFaq> call, @NonNull Throwable t) {
                Logger.d(t.getMessage());
            }
        });
    }

    private void initAction() {
    }

    private void btnUpdate(List<FaqTitleItems> arrayList) {
        if (arrayList == null)
            return;
        for (int i = 0; i < arrayList.size(); i++) {
            questionBtn[i].setText(arrayList.get(i).qstnTitle);
            questionBtn[i].setTag(arrayList.get(i).faqCd);

            final int position = Integer.parseInt(arrayList.get(i).faqCd);

            questionBtn[i].setOnClickListener(v -> initDtData(position));
        }
        initDtData(1);
    }

    private void initDtData(Integer i) {
        FaqModel FAQModel = new FaqModel();
        FAQModel.faqCd = i + "";

        for (int index = 0; index < 8; index++) {
            questionBtn[index].setBackground(getResources().getDrawable(R.drawable.side_customercenter_border));
            questionBtn[index].setTextColor(Color.parseColor("#989797"));
        }
        questionBtn[i - 1].setBackground(getResources().getDrawable(R.drawable.side_customercenter_border_coral));
        questionBtn[i - 1].setTextColor(Color.parseColor("#ff794c"));


        RetrofitClient.getInstance().getMoaService().faqDtList(FAQModel).enqueue(new Callback<ResFaqDetailItems>() {

            @Override
            public void onResponse(@NonNull Call<ResFaqDetailItems> call, @NonNull Response<ResFaqDetailItems> response) {
                ResFaqDetailItems resFaqDetailItems = response.body();
                if (resFaqDetailItems == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resFaqDetailItems)) {
                    initDtData(i);
                    return;
                }
                listUpdate(resFaqDetailItems.faqDtLists);
            }

            @Override
            public void onFailure(@NonNull Call<ResFaqDetailItems> call, @NonNull Throwable t) {
                Logger.d(t.getMessage());
            }
        });
    }

    private void listUpdate(List<FaqDetailItems> arrayList) {
        if (arrayList == null)
            return;
        msRecycleViewAdapter.setData(arrayList);
        msRecycleViewAdapter.notifyDataSetChanged();
    }
}
