package com.moaPlatform.moa.side_menu.customercenter.myquestion;

import android.os.Bundle;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import androidx.appcompat.app.AppCompatActivity;

public class MyQuestionNomember extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myquestion_nomember);

        init();
    }

    private void init() {
        CommonTitleView commonTitleView = findViewById(R.id.commontitle);
        commonTitleView.setOnClickListener(v -> finish());
        commonTitleView.setTitleName(getString(R.string.myquestion_login));
    }
}
