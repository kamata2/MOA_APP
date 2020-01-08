package com.moaPlatform.moa.bottom_menu.theme;

import android.os.Bundle;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_layout);
        init();
    }

    private void init() {
//        TopToolbarController topToolbarController = new TopToolbarController(findViewById(R.id.topToolbar));
//        topToolbarController.themeInit();
//        topToolbarController.setTopToolbarClickListener(code -> {
//            if (code == CodeTypeManager.TopToolbarManager.BACK_BUTTON_PRESS.getType()) {
//                finish();
//            }
//        });

        CommonTitleView commonTitleView = findViewById(R.id.commontitle);
        commonTitleView.setOnClickListener(v -> finish());
        commonTitleView.setTitleName(getString(R.string.bottom_menu_them));

    }
}
