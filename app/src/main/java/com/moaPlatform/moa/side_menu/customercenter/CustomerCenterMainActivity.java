package com.moaPlatform.moa.side_menu.customercenter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.customercenter.faq.view.FaqMainActivity;
import com.moaPlatform.moa.side_menu.customercenter.myquestion.view.MyQuestionMainActivity;
import com.moaPlatform.moa.side_menu.customercenter.myquestion.view.OneOnOneQuestionMainActivity;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import javax.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerCenterMainActivity extends AppCompatActivity {
    RelativeLayout rTab, rTab2, rTab3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_customercenter_main);

        initView();
        initAction();
    }

    private void initView() {
        rTab = findViewById(R.id.every_question);
        rTab2 = findViewById(R.id.one_on_one_question);
        rTab3 = findViewById(R.id.my_question);

        CommonTitleView commonTitleOrderList = findViewById(R.id.commonTitleMyReview);
        commonTitleOrderList.setBackButtonClickListener(v -> finish());
        commonTitleOrderList.setTitleName(getString(R.string.side_customercenter_list));
    }

    private void initAction() {
        rTab.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerCenterMainActivity.this, FaqMainActivity.class);
            startActivity(intent);
        });

        rTab2.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerCenterMainActivity.this, OneOnOneQuestionMainActivity.class);
            startActivity(intent);
        });

        rTab3.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerCenterMainActivity.this, MyQuestionMainActivity.class);
            startActivity(intent);
        });
    }
}
