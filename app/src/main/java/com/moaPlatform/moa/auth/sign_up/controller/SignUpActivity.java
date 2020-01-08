package com.moaPlatform.moa.auth.sign_up.controller;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.WalletAddressCreateFragment;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.auth.UserUseHelper;
import com.moaPlatform.moa.util.auth.UserWalletHelper;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.interfaces.ClassConnectInterface;
import com.moaPlatform.moa.util.interfaces.HttpConnectionResult;
import com.moaPlatform.moa.util.interfaces.Identifiable;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.singleton.App;

import java.util.Objects;

/**
 * 회원가입 화면
 * 개인정보 입력 -> 전자지갑 비밀번호 입력 -> 본인인증 -> 자동로그인 -> 지갑 생성
 * Created by jiwun
 */
public class SignUpActivity extends AppCompatActivity implements HttpConnectionResult, Identifiable, ClassConnectInterface {

    private FragmentManager fragmentManager;
    private UserUseHelper userUseHelper;
    private UserWalletHelper userWalletHelper;

    private final String SIGN_UP = "signUp";
    private final String INPUT_WALLET_PASSWORD = "inputWalletPw";
    private final String IDENTITY_VERIFICATION = "IdentityVerification";

    // 홈키 및 본인인증시 마켓 이동시 onStart() 재실행 이슈 방지용
    private boolean isFirst = true;
    public WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        init();

        webView = findViewById(R.id.webView);
        userUseHelper = new UserUseHelper(this);

        userWalletHelper = new UserWalletHelper(this);
        userWalletHelper.setClassConnectInterface(this);
        userWalletHelper.init(webView);
    }

    /**
     * 초기화
     */
    private void init() {
        // 툴바 초기화
        CommonTitleView commonTitleView = findViewById(R.id.viewToolbar);
        commonTitleView.setVisibility(View.VISIBLE);
        commonTitleView.setTitleName(getString(R.string.sign_up));
        commonTitleView.setBackButtonClickListener(v -> finish());
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment, new SignUpFragment(), SIGN_UP)
                .add(R.id.fragment, new WalletAddressCreateFragment(), INPUT_WALLET_PASSWORD)
                .add(R.id.fragment, new IdentityVerificationFragment(), IDENTITY_VERIFICATION)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isFirst) {
            Fragment f = fragmentManager.findFragmentByTag(INPUT_WALLET_PASSWORD);
            if (f != null) {
                fragmentManager.beginTransaction()
                        .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag(INPUT_WALLET_PASSWORD)))
                        .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag(IDENTITY_VERIFICATION)))
                        .commit();
                isFirst = false;
            }

        }
    }

    /**
     * 개인정보 입력 완료시 호출
     */
    public void inpuPrsnlInfrmSuccess() {
        fragmentManager.beginTransaction()
                .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag(INPUT_WALLET_PASSWORD)))
                .show(Objects.requireNonNull(fragmentManager.findFragmentByTag(IDENTITY_VERIFICATION)))
                .commit();

    }

    /**
     * 전자 지갑 비밀번호 세팅 화면 표시
     */
    public void walletAddressPwInit() {
        fragmentManager.beginTransaction()
                .hide(Objects.requireNonNull(fragmentManager.findFragmentByTag(SIGN_UP)))
                .show(Objects.requireNonNull(fragmentManager.findFragmentByTag(INPUT_WALLET_PASSWORD)))
                .commit();

    }

    @Override
    public void identityVerificationSuccess() {
        signUpFragmentShow();
//        userUseHelper = new UserUseHelper(this);
        userUseHelper.setClassConnectInterface(this);
        SignUpFragment signUpFragment = (SignUpFragment) fragmentManager.findFragmentByTag(SIGN_UP);
        userUseHelper.userRegist(signUpFragment.getEmail(), signUpFragment.getPw(), signUpFragment.recommenderClear ? signUpFragment.getRecommenderNumber() : "");
    }

    @Override
    public void identityVerificationFail() {
        signUpFragmentShow();
    }

    private void signUpFragmentShow() {

    }

    @Override
    public void onHttpConnectionSuccess(int type, BaseModel baseModel) {
        if (type == App.getInstance().SIGN_UP_SUCCESS) {
//            finish();
        }
    }

    private void walletChecking() {
        fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag(IDENTITY_VERIFICATION))).commit();
        userWalletHelper.createWallet(((WalletAddressCreateFragment) fragmentManager.findFragmentByTag(INPUT_WALLET_PASSWORD)).getPw(),((WalletAddressCreateFragment) fragmentManager.findFragmentByTag(INPUT_WALLET_PASSWORD)).getBirth());
//        userUseHelper.walletAddressCheck();
    }

    @Override
    public void onActType(CodeTypeManager.ClassCodeManager type) {
        switch (type) {
            case USER_LOGIN:
                walletChecking();
//                Logger.d("login success");
//                finish();
                break;
            case LOGIN_FAIL:
                Logger.e("로그인 실패");
                setResult(MoaConstants.RESULT_USER_JOIN_FAIL);
                break;
            case WALLET_CREATE_SUCCESS :
                // TODO LoginActivity 를 통하여 SignUpActivity 로 진입하는 경우 화면 종료 처리
                setResult(MoaConstants.RESULT_USER_JOIN_SUCCESS);
                finish();
                break;
//            case NOT_WALLET_ADDRESS:
//                Logger.d("전자지갑 번호 없음");
//                userWalletHelper.createWallet(((WalletAddressCreateFragment)fragmentManager.findFragmentByTag(INPUT_WALLET_PASSWORD)).getPw());
////                userUseHelper.createWallet(((WalletAddressCreateFragment)fragmentManager.findFragmentByTag(INPUT_WALLET_PASSWORD)).getPw());
//                break;
//            case EXIST_WALLET_ADDRESS :
//                Logger.d("전자지갑 있음");
//                break;
        }
    }
}
