package com.moaPlatform.moa.account;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.controller.IdentityVerificationFragment;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.interfaces.Identifiable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * Created by jiwun on 2019-05-23.
 */
public class FindIdActivity extends AppCompatActivity implements Identifiable {

    private Button btnFindIdLogin;
    // 타이틀 뷰
    private CommonTitleView viewFindIdTitle;

    // 아이디 결과값 얼을 때 표시할 뷰 , 아이디 결과값 표시할 뷰
    private View llFindIdNotExist, llFindIdExist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
        init();
    }

    private void init() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.viewIdSearch, new IdentityVerificationFragment()).commit();

        // 상단 툴바 세팅
        viewFindIdTitle = findViewById(R.id.viewFindIdTitle);
        viewFindIdTitle.setTitleName(getString(R.string.find_id));
        viewFindIdTitle.setClosedButtonType();
        viewFindIdTitle.setBackButtonClickListener(v -> finish());
        viewFindIdTitle.setVisibility(View.GONE);
        // 로그인 버튼
        btnFindIdLogin = findViewById(R.id.btnFindIdLogin);
        btnFindIdLogin.setVisibility(View.GONE);
        btnFindIdLogin.setOnClickListener(v -> finish());

        // 아이디 가 없을 때 표시될 뷰
        llFindIdNotExist = findViewById(R.id.llFindIdNotExist);
        llFindIdNotExist.setVisibility(View.GONE);
        // 아이디가 있을 때 표시할 뷰
        llFindIdExist = findViewById(R.id.llFindIdExist);
        llFindIdExist.setVisibility(View.GONE);
    }

    public void setFindEmail(String email) {

        new Thread(() -> runOnUiThread(() -> {
            TextView tvFindEmail = findViewById(R.id.tvFindEmail);
            tvFindEmail.setText(email);
        })).start();

    }

    @Override
    public void identityVerificationSuccess() {
        new Thread(() -> runOnUiThread(() -> {
            FrameLayout viewIdSearch = findViewById(R.id.viewIdSearch);
            viewIdSearch.setVisibility(View.GONE);
            btnFindIdLogin.setVisibility(View.VISIBLE);
            viewFindIdTitle.setVisibility(View.VISIBLE);
            llFindIdExist.setVisibility(View.VISIBLE);
        })).start();
    }

    @Override
    public void identityVerificationFail() {
        new Thread(() -> runOnUiThread(() -> {
            llFindIdNotExist.setVisibility(View.VISIBLE);
            viewFindIdTitle.setVisibility(View.VISIBLE);
        })).start();
    }
}
