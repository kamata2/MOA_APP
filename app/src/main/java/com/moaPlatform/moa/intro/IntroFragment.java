package com.moaPlatform.moa.intro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.intro.dialog.MoaOptionalAccessDialog;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.auth.UserUseHelper;
import com.moaPlatform.moa.util.auth.UserWalletHelper;
import com.moaPlatform.moa.util.interfaces.ClassConnectInterface;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.wallet.WalletRestoreActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class IntroFragment extends Fragment implements ClassConnectInterface {
    private Context context;
    // 다이얼로그 띄울건지 말건지 체크 ( true : 안띄움 , false : 띄움 )
    private boolean dialogSuccess;

    // 비회원 및 회원 관련 로그인 및 로그아웃 등등 유저 관련 처리해주는 핼퍼
    private UserUseHelper userUseHelper;
    private UserWalletHelper userWalletHelper;
    // IntroActivity 와 통신할때 사용알 interface
    private ClassConnectInterface introActivityConnectInterface;
    private View view;

    public void init(boolean dialogSuccess) {
        this.dialogSuccess = dialogSuccess;
    }

    void setIntroActivityConnectInterface(ClassConnectInterface introActivityConnectInterface) {
        this.introActivityConnectInterface = introActivityConnectInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_fragment, container, false);
        context = view.getContext();

        if (!dialogSuccess) {
            showOptionalAccessDialog();
        } else {
            nonMemberLogin();
        }
        return view;
    }

    /**
     * 비회원 생성 및 로그인
     */
    private void nonMemberLogin() {
        userUseHelperInit();
        userUseHelper.setClassConnectInterface(this);
        userUseHelper.checkPhonePermission();

        WebView webView = view.findViewById(R.id.webView);
        userWalletHelper = new UserWalletHelper(getContext());
        userWalletHelper.setClassConnectInterface(this);
        userWalletHelper.init(webView);
    }

    /**
     * UserUseHelper 초기화
     */
    private void userUseHelperInit() {
        userUseHelper = new UserUseHelper(context);
    }

    /**
     * 선택적 접근권한 다이얼로그 보이기
     */
    private void showOptionalAccessDialog() {
        MoaOptionalAccessDialog moaOptionalAccessDialog = new MoaOptionalAccessDialog(context);
        moaOptionalAccessDialog.setDialogListener((Dialog dialog) -> {
            introActivityConnectInterface.onActType(CodeTypeManager.ClassCodeManager.SELECT_PERMISSION_DIALOG);
            dialog.dismiss();
            nonMemberLogin();
        });
        moaOptionalAccessDialog.show();
    }

    @Override
    public void onActType(CodeTypeManager.ClassCodeManager type) {
        Intent walletRestoreIntent;
        switch (type) {
            case LOGIN_SUCCESS:
                introActivityConnectInterface.onActType(CodeTypeManager.ClassCodeManager.LOGIN_SUCCESS);
                break;
            case USER_LOGIN:
                userWalletHelper.prepareRestoreWallet();
//                userWalletHelper.prepareRestoreWallet();
//                introActivityConnectInterface.onActType(CodeTypeManager.ClassCodeManager.LOGIN_SUCCESS);
                break;
            case NON_MEMBER_LOGIN:
                introActivityConnectInterface.onActType(CodeTypeManager.ClassCodeManager.LOGIN_SUCCESS);
                break;
            case WALLET_CREATE_SUCCESS:
                Logger.d("지갑 생성 완료");
                introActivityConnectInterface.onActType(CodeTypeManager.ClassCodeManager.LOGIN_SUCCESS);
                break;
            case MOVE_WALLET_RESTORE_SELF_AUTH:
                walletRestoreIntent = new Intent(getContext(), WalletRestoreActivity.class);
                startActivityForResult(walletRestoreIntent, 0);
                break;
            case WALLET_RESTORE_FAIL:
                walletRestoreIntent = new Intent(getContext(), WalletRestoreActivity.class);
                walletRestoreIntent.putExtra("onlyPw", "onlyPw");
                startActivityForResult(walletRestoreIntent, 0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                userWalletHelper.startRestoreWallet(data.getStringExtra("walletPw"));
                break;
            case RESULT_CANCELED:
//                onFailWalletRestore();
                break;
            default:
                break;
        }
    }
}
