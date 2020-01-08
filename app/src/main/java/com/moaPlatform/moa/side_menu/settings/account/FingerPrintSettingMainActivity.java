package com.moaPlatform.moa.side_menu.settings.account;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.moaPlatform.moa.R;

public class FingerPrintSettingMainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_menu_fingerprint);

        initView();
    }

    private void initView() {
        ImageButton btnFinish = findViewById(R.id.btnBack);
        btnFinish.setOnClickListener(v -> finish());
    }
}
