package com.moaPlatform.moa.util.toolbar;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.controller.LogInActivity;
import com.moaPlatform.moa.auth.sign_up.controller.SignUpActivity;
import com.moaPlatform.moa.bottom_menu.main.MainActivity;
import com.moaPlatform.moa.bottom_menu.shopping_cart.main.ShoppingCartActivity;
import com.moaPlatform.moa.bottom_menu.theme.ThemeActivity;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.MoaPayAgrmnt;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.WalletAgreeActivity;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.WalletMainActivity;
import com.moaPlatform.moa.side_menu.eventnotice.view.event.EventMainActivity;
import com.moaPlatform.moa.util.dialog.yesOrNo.LoginSigninDialog;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 하단 툴바 컨트롤러
 */
public class BottomToolbarController {
    // 하단 툴바 뷰
    private View bottomToolbar;
    String autoType, oneType;
    private BottomToolbarClickListener bottomToolbarClickListener;

    public void setBottomToolbarClickListener(BottomToolbarClickListener bottomToolbarClickListener) {
        this.bottomToolbarClickListener = bottomToolbarClickListener;
    }

    public BottomToolbarController(View bottomToolbar) {
        this.bottomToolbar = bottomToolbar;
        init();
    }

    private LoginSigninDialog loginSigninDialog;

    /**
     * 초기화
     */
    private void init() {
        ConstraintLayout viewShoppingCart = bottomToolbar.findViewById(R.id.shoppingCartGroup);
        viewShoppingCart.setOnClickListener(v -> goShoppingCart());
        ConstraintLayout viewWalletGroup = bottomToolbar.findViewById(R.id.viewWalletGroup);
        viewWalletGroup.setOnClickListener(v -> goWallet());
        ConstraintLayout viewAchievement = bottomToolbar.findViewById(R.id.achievementsGroup);
        viewAchievement.setOnClickListener(v -> goAchievement());

        ConstraintLayout viewHomeGroup = bottomToolbar.findViewById(R.id.homeGroup);
        viewHomeGroup.setOnClickListener(v -> goMainActivity());

        ConstraintLayout viewThemGroup = bottomToolbar.findViewById(R.id.themeGroup);
        viewThemGroup.setOnClickListener(v -> goThemActivity());

//        btnWallet = bottomToolbar.findViewById(R.id.btnWallet);
//        my = bottomToolbar.findViewById(R.id.my);
//        menu = bottomToolbar.findViewById(R.id.menu);
    }

    private void goThemActivity() {
        Intent intentMainActivity = new Intent(bottomToolbar.getContext(), ThemeActivity.class);
        intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        bottomToolbar.getContext().startActivity(intentMainActivity);
    }

    /**
     * 메인 화면으로 이동
     */
    private void goMainActivity() {
        if (bottomToolbarClickListener == null) {
            Intent intentMainActivity = new Intent(bottomToolbar.getContext(), MainActivity.class);
            intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            bottomToolbar.getContext().startActivity(intentMainActivity);
        } else {
            bottomToolbarClickListener.onClickItem(CodeTypeManager.Toolbar.BOTTOM_TOOLBAR_MAIN);
        }
    }

    /**
     * 장바구니로 이동
     */
    private void goShoppingCart() {
        Intent intentShoppingCart = new Intent(bottomToolbar.getContext(), ShoppingCartActivity.class);
        intentShoppingCart.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentShoppingCart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        bottomToolbar.getContext().startActivity(intentShoppingCart);
    }

    private View.OnClickListener loginListener = new View.OnClickListener() {
        public void onClick(View v) {
            loginSigninDialog.dismiss();
            Intent intentWallet = new Intent(bottomToolbar.getContext(), LogInActivity.class);
            bottomToolbar.getContext().startActivity(intentWallet);
        }
    };

    private View.OnClickListener signinListener = new View.OnClickListener() {
        public void onClick(View v) {
            loginSigninDialog.dismiss();
            Intent intentWallet = new Intent(bottomToolbar.getContext(), SignUpActivity.class);
            bottomToolbar.getContext().startActivity(intentWallet);
        }
    };


    /**
     * 전자지갑 화면으로 이동
     */
    private void goWallet() {
//        SharedPreferences sf = bottomToolbar.getContext().getSharedPreferences("sign", MODE_PRIVATE);
//        autoType = sf.getString("autoLogin", "");
//        oneType = sf.getString("oneLogin", "");
//
//        if (autoType.equals("Yes") || oneType.equals("Yes")) {
//        checkArmnt();
//        } else {
//            loginSigninDialog = new LoginSigninDialog(bottomToolbar.getContext(), loginListener, signinListener);
//            loginSigninDialog.show();
//        }
        Intent intentWallet;
        intentWallet = new Intent(bottomToolbar.getContext(), WalletMainActivity.class);//리얼
        intentWallet.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentWallet.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intentWallet = new Intent(bottomToolbar.getContext(), MoaPayActivity.class);
//        intentWallet = new Intent(bottomToolbar.getContext(), AuthActivity.class);



        bottomToolbar.getContext().startActivity(intentWallet);
    }

    private void checkArmnt() {
        MoaPayAgrmnt moaPayAgrmnt = new MoaPayAgrmnt();
        RetrofitClient.getInstance().getMoaService().getMoaPayAgrmnt(moaPayAgrmnt).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    checkArmnt();
                    return;
                }
                Intent intentWallet;
                if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    intentWallet = new Intent(bottomToolbar.getContext(), WalletAgreeActivity.class);
                } else {
                    intentWallet = new Intent(bottomToolbar.getContext(), WalletMainActivity.class);
                }
                bottomToolbar.getContext().startActivity(intentWallet);
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {

            }
        });
    }


    /**
     * 업적 화면으로 이동
     */
    private void goAchievement() {
        Intent intentAchievement = new Intent(bottomToolbar.getContext(), EventMainActivity.class);
        intentAchievement.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentAchievement.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        bottomToolbar.getContext().startActivity(intentAchievement);
    }

    public interface BottomToolbarClickListener {
        void onClickItem(CodeTypeManager.Toolbar type);
    }
}
