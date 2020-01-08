package com.moaPlatform.moa.bottom_menu.shopping_cart.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.ShoppingCartDetailActivity;
import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model.ReqShoppingCartDetailModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.custom_view.CommonLoadingView;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.manager.IntentKeyManager;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingCartActivity extends AppCompatActivity implements ListItemClickListener {

    private CommonTitleView commonTitleOrderList;

    private boolean isRefresh = false;
    private ShoppingCartAdapter shoppingCartAdapter;
    private ShoppingCartController shoppingCartController;
    private CommonLoadingView viewShoppingCartLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_activity);
        TopToolbarInit();
        shoppingCartListInit();
        viewShoppingCartLoading = findViewById(R.id.viewShoppingCartLoading);
    }

    private void TopToolbarInit() {
        commonTitleOrderList = findViewById(R.id.commonTitleMyReview);
        commonTitleOrderList.setBackButtonClickListener(v -> finish());
        commonTitleOrderList.setTitleName(getString(R.string.shopping_cart));
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewShoppingCartLoading.loadingAnimationPlay(this);
        init();
    }

    private void init() {
        Logger.i("쇼핑카트 리스트 : " + getIntent().getStringExtra("shoppingCartJson"));
        if (getIntent().getStringExtra("shoppingCartJson") != null && !isRefresh) {
            shoppingCartListUpdate(new Gson().fromJson(getIntent().getStringExtra("shoppingCartJson"), ResShoppingCartList.class).shoppingCartList);
        } else {
            shoppingCartController = new ShoppingCartController(this);
            shoppingCartController.getShoppingCartListInfo();
            shoppingCartController.setHttpConnectionResult((type, baseModel) -> {
                shoppingCartListUpdate(((ResShoppingCartList) baseModel).shoppingCartList);
                viewShoppingCartLoading.animationStop(this);
            });
        }
    }

    /**
     * 장바구니 리스트 초기화
     */
    private void shoppingCartListInit() {
        RecyclerView shoppingCartRecyclerView = findViewById(R.id.shoppingCartRecyclerView);
        shoppingCartAdapter = new ShoppingCartAdapter();
        shoppingCartAdapter.setListItemClickListener(this);
        shoppingCartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shoppingCartRecyclerView.setAdapter(shoppingCartAdapter);
    }

    /**
     * 장바구니 리스트 업데이트
     *
     * @param shoppingCartList 업데이트할 리스트
     */
    public void shoppingCartListUpdate(ArrayList<ResShoppingCartList.ShoppingCartModel> shoppingCartList) {
        shoppingCartAdapter.listUpdate(shoppingCartList);
        shoppingCartAdapter.notifyDataSetChanged();

        if (shoppingCartList.size() == 0) {
            noCart();
        }
    }

    public void noCart() {
        View rlShoppingCartEmpty = findViewById(R.id.rlShoppingCartEmpty);
        rlShoppingCartEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRefresh = true;
    }

    @Override
    public void clickItem(Object codeType, int position, Object object) {
        if (codeType.equals(CodeTypeManager.ShoppingCart.INTENT_SHOPPING_CART_DETAIL_ACTIVITY.getType())) {
            Intent shoppingCartDetail = new Intent(this, ShoppingCartDetailActivity.class);
            shoppingCartDetail.putExtra(IntentKeyManager.SHOPPING_CART_ID, ((ResShoppingCartList.ShoppingCartModel) object).shoppingCartId);
            shoppingCartDetail.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            shoppingCartDetail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(shoppingCartDetail);
        } else if (codeType.equals(CodeTypeManager.ShoppingCart.SHOPPING_CART_REMOVE_DIALOG_SHOW.getType())) {
            shoppingCartDialogShow(((ResShoppingCartList.ShoppingCartModel) object).shoppingCartId, position);
        }
    }

    private void shoppingCartDialogShow(String caratId, int position) {
        YesOrNoDialog shoppingCartRemoveDialog = new YesOrNoDialog();
        shoppingCartRemoveDialog.dialogContent("선택한 매장의 장바구니에 담긴 모든 상품을 삭제하시겠습니까?");
        shoppingCartRemoveDialog.show(getSupportFragmentManager(), "shoppingCartRemoveDialog");
        shoppingCartRemoveDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
            @Override
            public void onDialogNo() {
                shoppingCartRemoveDialog.dismiss();
            }

            @Override
            public void onDialogYes() {
                // 서버에다가 해당 장바구니 삭제 요정을 보냄
                ReqShoppingCartDetailModel reqShoppingCartDelete = new ReqShoppingCartDetailModel();
                reqShoppingCartDelete.setShoppingCartId(caratId);
                if (shoppingCartController == null)
                    shoppingCartController = new ShoppingCartController(getApplicationContext());
                shoppingCartController.shoppingCartDelete(reqShoppingCartDelete);
                shoppingCartController.setHttpConnectionResult((type, baseModel) -> {
                    // 서버에 삭제가 정상적으로 됬을시 다이얼로그를 닫고 리스트 갱신
                    shoppingCartAdapter.removeList(position);
                    shoppingCartRemoveDialog.dismiss();
                    if (shoppingCartAdapter.getItemCount() == 0) {
                        finish();
                    }
                });
            }
        });
    }
}
