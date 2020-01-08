package com.moaPlatform.moa.util.toolbar;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.interfaces.CodeListener;
import com.moaPlatform.moa.util.manager.CodeTypeManager;

import androidx.constraintlayout.widget.ConstraintLayout;

public class TopToolbarController {

    private View view, viewTopToolbarMainBg;
    // 뒤로가기 버튼
    private ImageView btnBack;
    // 타이틀
    private TextView txtToolbarTitle;

    private CodeListener topToolbarClickListener;

    private View clBtnBack;

    public void setTopToolbarClickListener(CodeListener topToolbarClickListener) {
        this.topToolbarClickListener = topToolbarClickListener;
    }

    public TopToolbarController(View topToolbarView) {
        this.view = topToolbarView;
        btnBack = view.findViewById(R.id.btnBack);
        txtToolbarTitle = view.findViewById(R.id.toolbarTitle);
        viewTopToolbarMainBg = view.findViewById(R.id.viewTopToolbarMainBg);
        clBtnBack = view.findViewById(R.id.clBtnBack);
        clBtnBack.setVisibility(View.GONE);
    }

    /**
     * 타이틀 세팅
     */
    public void setTitle(String title) {
        txtToolbarTitle.setText(title);
    }

    public void isTitleShow(boolean isShow) {
        if (isShow) {
            txtToolbarTitle.setVisibility(View.VISIBLE);
        } else {
            txtToolbarTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 기본 주소 초기화
     */
    public void defaultAddressToolbarInit() {
        txtToolbarTitle.setText("위치 설정");
        ConstraintLayout myLocationSearch = view.findViewById(R.id.myLocationSearch);
        myLocationSearch.setVisibility(View.VISIBLE);
        backButtonShow();
        myLocationSearch.setOnClickListener((View view) -> topToolbarClickListener.resultCode(CodeTypeManager.TopToolbarManager.NOW_LOCATION_SEARCH.getType()));
    }

    public void wallet() {
        txtToolbarTitle.setText("wallet");
    }

    /**
     * 상세 주소 초기화
     */
    public void detailAddressToolbarInit() {
        txtToolbarTitle.setText(view.getResources().getString(R.string.detail_address_title));
        ConstraintLayout myLocationSearch = view.findViewById(R.id.myLocationSearch);
        myLocationSearch.setVisibility(View.GONE);
        backButtonShow();
    }

    /**
     * 메인 화면 초기화
     */
    public void mainToolbarInit() {
        txtToolbarTitle.setText("GOEAT");
        txtToolbarTitle.setTextColor(view.getResources().getColor(R.color.white));
        view.setBackgroundResource(R.color.transparent);
        ImageView btnSubMenu = view.findViewById(R.id.btnSubMenu);
        btnSubMenu.setVisibility(View.VISIBLE);
        btnSubMenu.setOnClickListener((View view) -> topToolbarClickListener.resultCode(CodeTypeManager.TopToolbarManager.SUB_MENU_MOVE.getType()));
        ImageButton btnLocation = view.findViewById(R.id.btnLocation);
        btnLocation.setVisibility(View.VISIBLE);
        ImageButton btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.VISIBLE);
        btnLocation.setOnClickListener(v -> topToolbarClickListener.resultCode(CodeTypeManager.TopToolbarManager.NOW_LOCATION_SEARCH.getType()));
        btnSearch.setOnClickListener(v -> topToolbarClickListener.resultCode(CodeTypeManager.TopToolbarManager.STORE_SEARCH.getType()));
    }

    public void setMainBgVisibility(int type) {
        viewTopToolbarMainBg.setVisibility(type);
    }

    public void orderPaymentInit() {
        txtToolbarTitle.setText("주문결제");
        backButtonShow();
    }

    public void productDetailToolbarInit(String title) {
        txtToolbarTitle.setText(title);
        backButtonShow();
    }

    /**
     * 장바구니 세팅
     */
    public void shoppingCartToolbarInit() {
        backButtonShow();
        txtToolbarTitle.setText(view.getResources().getString(R.string.shopping_cart));
    }

    public void badgeToolbarInit() {
        txtToolbarTitle.setText("MOA 배지");
        txtToolbarTitle.setTextColor(view.getResources().getColor(R.color.white));
        view.setBackgroundResource(R.color.transparent);
    }

    public void storeDetailInit() {
        txtToolbarTitle.setVisibility(View.GONE);
        view.setBackgroundResource(R.color.transparent);
        backButtonShow();
        backArrowWhite();
    }

    public void themeInit() {
        txtToolbarTitle.setText("테마");
        backButtonShow();
    }

    /**
     * 흰색 뒤로가기 아이콘으로 변경
     */
    public void backArrowWhite() {
        imageChange(R.drawable.top_toolbar_white_back_ic,btnBack);
    }

    /**
     * 검정색 뒤로가기 아이콘으로 변경
     */
    public void backArrowBlack() {
        imageChange(R.drawable.top_toolbar_black_back_ic,btnBack);
    }

    /**
     * 배달 화면 세팅
     */
    public void deliveryToolbarInit() {
        txtToolbarTitle.setText("배달");
        ImageView btnSubMenu = view.findViewById(R.id.btnSubMenu);
        btnSubMenu.setVisibility(View.VISIBLE);
        btnSubMenu.setOnClickListener((View view) -> topToolbarClickListener.resultCode(CodeTypeManager.TopToolbarManager.SUB_MENU_MOVE.getType()));
        btnSubMenu.setImageResource(R.drawable.menu_wg);
        ImageButton btnLocation = view.findViewById(R.id.btnLocation);
        btnLocation.setVisibility(View.VISIBLE);
        btnLocation.setImageResource(R.drawable.location_wg);
        ImageButton btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setVisibility(View.VISIBLE);
        btnSearch.setImageResource(R.drawable.search_wg);
        btnLocation.setOnClickListener(v -> topToolbarClickListener.resultCode(CodeTypeManager.TopToolbarManager.NOW_LOCATION_SEARCH.getType()));
        btnSearch.setOnClickListener(v -> topToolbarClickListener.resultCode(CodeTypeManager.TopToolbarManager.STORE_SEARCH.getType()));
    }

    /**
     * 뒤로가기 보여주기
     */
    private void backButtonShow() {
        clBtnBack.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        clBtnBack.setOnClickListener(v -> topToolbarClickListener.resultCode(CodeTypeManager.TopToolbarManager.BACK_BUTTON_PRESS.getType()));
    }

    private void imageChange(int img, ImageView changeView) {
        Glide.with(view.getContext())
                .load(img)
                .into(changeView);
    }

}
