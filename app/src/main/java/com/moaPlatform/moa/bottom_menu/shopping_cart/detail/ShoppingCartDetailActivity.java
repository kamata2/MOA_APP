package com.moaPlatform.moa.bottom_menu.shopping_cart.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model.ReqShoppingCartDetailModel;
import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model.ResShoppingCartDetailModel;
import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model.ShoppingCartDetailViewModel;
import com.moaPlatform.moa.delivery_menu.store_detail.StoreDetailActivity;
import com.moaPlatform.moa.online_payment.order_payment.OrderPaymentActivity;
import com.moaPlatform.moa.online_payment.order_payment.model.ReqPaymentOrderModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.TimeEventHelper;
import com.moaPlatform.moa.util.custom_view.CommonLoadingView;
import com.moaPlatform.moa.util.custom_view.CommonTimeEventView;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.manager.IntentKeyManager;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.ProductAddOption;
import com.moaPlatform.moa.util.models.StoreInfoModel;
import com.moaPlatform.moa.util.singleton.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.moaPlatform.moa.bottom_menu.shopping_cart.detail.ShoppingCartDetailController.SHOPPING_CART_PRODUCT_ITEM_PLUS;

public class ShoppingCartDetailActivity extends AppCompatActivity implements ListItemClickListener, RetrofitConnectionResult {

    // 서버와 통신할 컨트롤러
    private ShoppingCartDetailController shoppingCartController;
    // 리스트 표시할 어뎁터
    private ShoppingCartDetailAdapter shoppingCartDetailAdapter;
    // 서버에 요청할 장바구니 req 모델
    private ReqShoppingCartDetailModel reqShoppingCartDetailModel;
    private ReqShoppingCartDetailModel reqPaymentShoppingCartDetailModel;
    // 서버에서 받은 장바구니 res 모델
    private ResShoppingCartDetailModel shoppingCartDetailModel;
    // 리스트의 아이템을 클릭한 포지션
    private int itemClickPosition = -1;
    // 장바구니 아이템 리스트
    private List<ResShoppingCartDetailModel.ShoppingCartDetailModel> shoppingCartItemList;

    private StoreInfoModel.TimeEventInfo timeEventInfo;

    private int delivery_price = 0;

    @SuppressLint("UseSparseArrays")
    private Map<Integer, Integer> shoppingCartUnCheckPosition = new HashMap<>();

    // 로딩 뷰
    private CommonLoadingView viewLoading;

    private Button btnOrder;

    private int storeSaveRate = 0;
    private int totalPriceOrign = 0;

    private ShoppingCartDetailViewModel shoppingCartDetailViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_detail_activity);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAfter();
    }

    /**
     * 초기화
     */
    private void init() {
        // 애니메이션 로딩 시작
        viewLoading = findViewById(R.id.viewLoading);
        // 서버와 통신하는 컨트롤러 초기화
        shoppingCartController = new ShoppingCartDetailController(this);
        shoppingCartController.setRetrofitConnectionResult(this);

        // 뷰 모델 세팅
        shoppingCartDetailViewModel = ViewModelProviders.of(this).get(ShoppingCartDetailViewModel.class);
        shoppingCartDetailViewModel.getProductList().observe(this, shoppingCartDetailModels -> {
            Logger.d("ShoppingCartDetailActivity 장바구니 상품 리스트 갱신");
            shoppingCartItemList = shoppingCartDetailModel.getShoppingCartDetailList();
            shoppingCartDetailAdapter.adapterListSetting(shoppingCartDetailModels);
            shoppingCartDetailAdapter.notifyDataSetChanged();
            priceInit();
        });

        shoppingCartDetailViewModel.getStoreInfoModel().observe(this, shoppingCartInfoModel -> {
            Logger.d("ShoppingCartDetailActivity 가맹점 기본 정보 갱신");
            storeInfoInit(shoppingCartInfoModel);
        });

        // 상단툴바 초기화
        CommonTitleView commonTitleView = findViewById(R.id.viewShoppingCartDetailTopToolbar);
        commonTitleView.setTitleName(R.string.shopping_cart);
        commonTitleView.setClosedButtonType();
        commonTitleView.setBackButtonClickListener(v -> finish());

        // 장바구니 전체 삭제
        Button btnShoppingCartAllRemove = findViewById(R.id.btnAllDelete);
        btnShoppingCartAllRemove.setOnClickListener(v -> shoppingCartAllDeleteDialogShow());

    }

    /**
     * 가맹점 정보 세팅
     */
    private void storeInfoInit(ResShoppingCartDetailModel.ShoppingCartInfoModel shoppingCartInfoModel) {
        // 가맹점 이름 세팅
        TextView tvStoreName = findViewById(R.id.tvStoreName);
        String storeName = shoppingCartInfoModel.storeName;
        tvStoreName.setText(storeName);

        final View priceInfo = findViewById(R.id.priceInfo);
        TextView tvMinOrderPrice = priceInfo.findViewById(R.id.tvMinOrderPrice);
        String minOrderPriceConvert = StringUtil.convertCommaPrice(shoppingCartInfoModel.minOrderPrice);
        String minOrderPriceText = getString(R.string.activity_store_product_min_order_price, minOrderPriceConvert);
        tvMinOrderPrice.setText(minOrderPriceText);

        // 타임 이벤트 세팅
        StoreInfoModel.TimeEventInfo timeEventInfo = shoppingCartInfoModel.timeEventModel;
        this.timeEventInfo = timeEventInfo;

        CommonTimeEventView viewShoppingCartDetailTimeEventOne = findViewById(R.id.viewShoppingCartDetailTimeEventOne);
        CommonTimeEventView viewShoppingCartDetailTimeEventTwo = findViewById(R.id.viewShoppingCartDetailTimeEventTwo);
        CommonTimeEventView viewShoppingCartDetailTimeEventThree = findViewById(R.id.viewShoppingCartDetailTimeEventThree);
        CommonTimeEventView viewShoppingCartDetailTimeEventFour = findViewById(R.id.viewShoppingCartDetailTimeEventFour);

        List<CommonTimeEventView> timeEventViewList = new ArrayList<>();
        timeEventViewList.add(viewShoppingCartDetailTimeEventOne);
        timeEventViewList.add(viewShoppingCartDetailTimeEventTwo);
        timeEventViewList.add(viewShoppingCartDetailTimeEventThree);
        timeEventViewList.add(viewShoppingCartDetailTimeEventFour);

        for (CommonTimeEventView commonTimeEventView : timeEventViewList)
            commonTimeEventView.setVisibility(View.GONE);

        new TimeEventHelper.TimeEventBuilder(timeEventViewList)
                .setAddSaveRate(timeEventInfo.addSaveRate)
                .setSaveRate(Integer.valueOf(shoppingCartInfoModel.saveRate))
                .setFixedAmount(timeEventInfo.fixAmt)
                .setFixedRate(timeEventInfo.fixRate)
                .build();

        storeSaveRate = Integer.valueOf(shoppingCartInfoModel.saveRate);

        // 매뉴 추가 세팅
        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        // 최소 주문 금액 단위 표시
        btnAddProduct.setText(getString(R.string.shopping_cart_detail_activity_add_product));
        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), StoreDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), shoppingCartDetailModel.shoppingCartDetailInfoModel.storeId);
            startActivity(intent);
        });

        // 주문
        btnOrder = findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(v -> {
            ReqPaymentOrderModel reqPaymentOrderModel = orderDataInit();
            Logger.toJson("결제 모델 ", reqPaymentOrderModel);
            Intent intent = new Intent(getApplicationContext(), OrderPaymentActivity.class);
            intent.putExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), shoppingCartDetailModel.shoppingCartDetailInfoModel.storeId);
            intent.putExtra("tempTemp", App.getInstance().gson.toJson(reqPaymentOrderModel));
            intent.putExtra("saveRate", shoppingCartDetailModel.shoppingCartDetailInfoModel.saveRate);
            intent.putExtra("timeEvent", new Gson().toJson(shoppingCartDetailModel.shoppingCartDetailInfoModel.timeEventModel));
            intent.putExtra(OrderPaymentActivity.INTENT_REMOVE_SHOPPING_CART_ITEM, new Gson().toJson(reqPaymentShoppingCartDetailModel));
            intent.putExtra(CodeTypeManager.StoreInfo.STORE_TOTAL_PRICE.toString(), totalPriceOrign - delivery_price);
            intent.putExtra(CodeTypeManager.StoreInfo.STORE_DELIVERY_PRICE.toString(), delivery_price);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // 애니메이션 중지
        viewLoading.animationStop(this);
    }

    private void initAfter() {
        viewLoading.loadingAnimationPlay(this);
        // 장바구니 리스트 초기화
        shoppingCartListInit();
        // 장바구니 리스트 가져올떄 사용할 reqModel 세팅
        reqShoppingCartDetailModel = new ReqShoppingCartDetailModel();
        reqShoppingCartDetailModel.setShoppingCartId(getIntent().getStringExtra(IntentKeyManager.SHOPPING_CART_ID));
        // 장바구니 리스트 호출
        shoppingCartController.getShoppingCartDetailInfo(reqShoppingCartDetailModel);
    }

    /**
     * 장바구니 리스트 초기화
     */
    private void shoppingCartListInit() {
        RecyclerView rvDetailShoppingCartList = findViewById(R.id.categoryListRecyclerView);
        rvDetailShoppingCartList.setNestedScrollingEnabled(false);
        shoppingCartDetailAdapter = new ShoppingCartDetailAdapter();
        shoppingCartDetailAdapter.setListItemClickListener(this);
        rvDetailShoppingCartList.setLayoutManager(new LinearLayoutManager(this));
        rvDetailShoppingCartList.setAdapter(shoppingCartDetailAdapter);
    }

    /**
     * 가격 세팅
     */
    private void priceInit() {
        final View priceInfo = findViewById(R.id.priceInfo);
        // 가격을 표시해줄 문구
        @StringRes final int priceResId = R.string.store_product_total_money;
        // 총 주문 가격 세팅
        TextView tvOrderPrice = priceInfo.findViewById(R.id.orderPriceContent);
        // 총 주문 가격
        int totalOrderPrice = 0;
        int pos = 0;
        for (ResShoppingCartDetailModel.ShoppingCartDetailModel shoppingCartDetailModel : shoppingCartItemList) {
            if (shoppingCartUnCheckPosition.get(pos) == null) {
                Map<String, String> addProductMap = shoppingCartDetailModel.getAddProductMap();
                // 추가 옵션 총합 가격
                final String ADD_PRODUCT_TOTAL_PRICE = addProductMap.get(shoppingCartDetailModel.ADD_PRODUCT_TOTAL_PRICE);
                // 주문 가격 (주문 가격 + 추가 옵션 가격) * 수량)
                final int ORDER_PRICE = shoppingCartDetailModel.getTotalPrice(Integer.valueOf(ADD_PRODUCT_TOTAL_PRICE));
                totalOrderPrice += ORDER_PRICE;
            }
            pos++;
        }
        tvOrderPrice.setText(StringUtil.convertCommaPrice(this, priceResId, totalOrderPrice));

        ResShoppingCartDetailModel.ShoppingCartInfoModel shoppingCartInfoModel = shoppingCartDetailModel.shoppingCartDetailInfoModel;

        if (totalOrderPrice >= Integer.valueOf(shoppingCartDetailModel.shoppingCartDetailInfoModel.minOrderPrice)) {
            btnOrder.setText(getString(R.string.store_product_order_button));
            btnOrder.setBackgroundResource(R.color.coral);
            btnOrder.setEnabled(true);
        } else {
            btnOrder.setText("최소 주문 금액을 채워주세요");
            btnOrder.setBackgroundResource(R.color.nobel);
            btnOrder.setEnabled(false);
        }

        // 배달료 타이틀
        TextView tvDeliveryPriceTitle = findViewById(R.id.deliveryPriceTitle);
        String deliveryPriceTitleText = getString(R.string.store_product_delivery_price_title_free_price, StringUtil.convertCommaPrice(shoppingCartInfoModel.freeDeliveryPrice));
        tvDeliveryPriceTitle.setText(deliveryPriceTitleText);

        // 배달비 세팅
        if (totalOrderPrice >= shoppingCartInfoModel.freeDeliveryPrice) {
            delivery_price = 0;
        } else {
            delivery_price = Integer.valueOf(shoppingCartInfoModel.deliveryPrice);
        }
        TextView tvDeliveryPrice = findViewById(R.id.deliveryPriceContent);
        tvDeliveryPrice.setText(StringUtil.convertCommaPrice(this, priceResId, delivery_price));

        totalOrderPrice += delivery_price;

        totalPriceOrign = 0;
        totalPriceOrign = totalOrderPrice;

        int salePrce = 0;
        if (timeEventInfo != null) {
            int fixAmount = 0;
            int fixRateAmount;
            // 할인 금액 및 적립금 적용
            if (timeEventInfo.timeEventType == 0 || timeEventInfo.timeEventType == 3) {
                if (timeEventInfo.fixAmt != 0) {
                    if (timeEventInfo.fixAmt > totalOrderPrice) {
                        fixAmount = totalOrderPrice;
                    } else {
                        fixAmount = timeEventInfo.fixAmt;
                    }
                }

                if (timeEventInfo.fixRate != 0) {
                    int salePriceTemp = (totalOrderPrice - fixAmount) * timeEventInfo.fixRate / 100;
                    if (salePriceTemp > timeEventInfo.maxDscntAmt) {
                        fixRateAmount = timeEventInfo.maxDscntAmt;
                    } else {
                        fixRateAmount = salePriceTemp;
                    }
                    salePriceTemp = fixRateAmount + fixAmount;
                    totalOrderPrice -= salePriceTemp;
                }
            }
        }

        TextView salePriceContent = priceInfo.findViewById(R.id.salePriceContent);
        String salePriceContentPrice = StringUtil.convertCommaPrice(salePrce);
        String salePriceContentText = getString(R.string.store_product_total_money, salePriceContentPrice);
        salePriceContent.setText(salePriceContentText);

        // 총 주문 금액 ( 총 주문 가격 + 배달비 )
        TextView tvTotalPrice = priceInfo.findViewById(R.id.totalOrderPriceContent);

        tvTotalPrice.setText(StringUtil.convertCommaPrice(this, priceResId, totalOrderPrice));

        TextView tvSavePoint = priceInfo.findViewById(R.id.savePoint);
        int savePoint = totalOrderPrice * storeSaveRate / 100;
        String savePointPrice = StringUtil.convertCommaPrice(savePoint);
        String savePointPriceText = getString(R.string.store_product_save_point_title, savePointPrice);
        tvSavePoint.setText(savePointPriceText);
    }

    /**
     * 장바구니 데이터 전부 삭제
     */
    private void shoppingCartAllDeleteDialogShow() {
        YesOrNoDialog yesOrNoDialog = new YesOrNoDialog();
        yesOrNoDialog.dialogContent("장바구니에 담긴 모든 상품을 삭제하시겠습니까?");
        yesOrNoDialog.show(getSupportFragmentManager(), "dialog");
        yesOrNoDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
            @Override
            public void onDialogNo() {
                yesOrNoDialog.dismiss();
            }

            @Override
            public void onDialogYes() {
                shoppingCartController.shoppingCartAllDelete(reqShoppingCartDetailModel);
                yesOrNoDialog.dismiss();
            }
        });
    }

    /**
     * 주문 모델 세팅
     */
    private ReqPaymentOrderModel orderDataInit() {
        ReqPaymentOrderModel reqPaymentOrderModel = new ReqPaymentOrderModel();
        reqPaymentShoppingCartDetailModel = new ReqShoppingCartDetailModel();
        int pos = 0;
        List<String> removeIds = new ArrayList<>();
        for (ResShoppingCartDetailModel.ShoppingCartDetailModel shoppingCartDetailModel : shoppingCartItemList) {
            if (shoppingCartUnCheckPosition.get(pos) == null) {
                ReqPaymentOrderModel.OrderDetailModel orderDetailModel = reqPaymentOrderModel.new OrderDetailModel();
                orderDetailModel.productCount = shoppingCartDetailModel.getCnt();
                orderDetailModel.storeProductCode = shoppingCartDetailModel.storeProductCode;
                orderDetailModel.storeProductOptionId = shoppingCartDetailModel.storeProductOptionId;
                removeIds.add(shoppingCartDetailModel.shoppingCartDetailId);
                reqPaymentShoppingCartDetailModel.setShoppingCartDetailIds(removeIds);
                for (ResShoppingCartDetailModel.ShoppingCartDetailOption shoppingCartDetailOption : shoppingCartDetailModel.shoppingCartDetailOptionList) {
                    for (ResShoppingCartDetailModel.ShoppingCartAddOption shoppingCartAddOption : shoppingCartDetailOption.shoppingCartAddOptionList) {
                        ProductAddOption productAddOption = new ProductAddOption();
                        productAddOption.init(shoppingCartDetailOption.addProductCategoryId, shoppingCartAddOption.productOptionId);
                        orderDetailModel.addProductOptionList.add(productAddOption);
                    }
                }
                reqPaymentOrderModel.orderDtl.add(orderDetailModel);
            }
            pos++;
        }
        return reqPaymentOrderModel;
    }


    @Override
    public void clickItem(Object codeType, int position, Object object) {
        switch ((ShoppingCartDetailAdapter.shoppingCartDetailItemClickType) codeType) {
            case PRODUCT_COUNT_PLUS:
                loadingStart();
                productCountChange(true, object, position);
                break;
            case PRODUCT_COUNT_MINUS:
                loadingStart();
                productCountChange(false, object, position);
                break;
            case PRODUCT_ITEM_REMOVE:
                YesOrNoDialog yesOrNoDialog = new YesOrNoDialog();
                yesOrNoDialog.dialogContent("장바구니에서 선택한 상품을 삭제하시겠습니까?");
                yesOrNoDialog.show(getSupportFragmentManager(), "shoppingcart remove");
                yesOrNoDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
                    @Override
                    public void onDialogNo() {
                        itemClickPosition = -1;
                        yesOrNoDialog.dismiss();
                    }

                    @Override
                    public void onDialogYes() {
                        shoppingCartItemRemove(object, position);
                        yesOrNoDialog.dismiss();
                    }
                });

                break;
            case PRODUCT_ITEM_CHECK:
                shoppingCartUnCheckPosition.remove(position);
                priceInit();
                break;
            case PRODUCT_ITEM_UNCHECK:
                shoppingCartUnCheckPosition.put(position, position);
                priceInit();
                break;
        }
    }

    /**
     * 장바구니 개수 컨트롤
     *
     * @param isPlus      상품 증가, 감소 체크 값
     * @param objectModel 개수 변경할 상품 정보가 담긴 모델
     * @param position    n 번쨰 포지션
     */
    private void productCountChange(boolean isPlus, Object objectModel, int position) {
        // 증감할 상품 정보가 담긴 모델
        ResShoppingCartDetailModel.ShoppingCartDetailModel shoppingCartDetailModel = (ResShoppingCartDetailModel.ShoppingCartDetailModel) objectModel;
        // 서버에 요청할 데이터 세팅
        reqShoppingCartDetailModel.setShoppingCartDetailId(shoppingCartDetailModel.shoppingCartDetailId);
        // 현제 클릭한 포지션 세팅
        itemClickPosition = position;
        if (isPlus) {
            shoppingCartController.productCountPlus(reqShoppingCartDetailModel);
        } else {
            shoppingCartController.productCountMinus(reqShoppingCartDetailModel);
        }
    }

    /**
     * 장바구니 아이템 삭제
     *
     * @param objectModel 삭제할 아이템이 들어있는 모델
     * @param position    해당 번째 포지션
     */
    private void shoppingCartItemRemove(Object objectModel, int position) {
        // 장바구니 아이템 삭제하기 위한 reqModel 세팅
        itemClickPosition = position;
        List<String> removeIds = new ArrayList<>();
        removeIds.add(((ResShoppingCartDetailModel.ShoppingCartDetailModel) objectModel).shoppingCartDetailId);
        reqShoppingCartDetailModel.setShoppingCartDetailIds(removeIds);
        shoppingCartController.shoppingCartItemRemove(reqShoppingCartDetailModel);
    }

    @Override
    public void onRetrofitSuccess(int type, BaseModel baseModel) {
        if (type == shoppingCartController.SHOPPING_CART_DETAIL_INFO) {
            // 장바구니 업데이트
            shoppingCartDetailModel = ((ResShoppingCartDetailModel) baseModel);
            shoppingCartDetailViewModel.setStoreInfoModel(shoppingCartDetailModel.shoppingCartDetailInfoModel);
//            shoppingCartListUpdate();
//            responseAfterInit();
        } else if (type == SHOPPING_CART_PRODUCT_ITEM_PLUS || type == ShoppingCartDetailController.SHOPPING_CART_PRODUCT_ITEM_MINUS) {
            // 장바구니 상품 개수 증가 및 감소
            shoppingCartDetailAdapter.productCountChange(type, itemClickPosition);
            itemClickPosition = -1;
            priceInit();
            loadingStop();
        } else if (type == shoppingCartController.SHOPPING_CART_PRODUCT_ITEM_REMOVE) {
            shoppingCartItemList.remove(shoppingCartDetailAdapter.getItem(itemClickPosition));
            shoppingCartDetailAdapter.shoppingCartItemRemove(itemClickPosition);
            itemClickPosition = -1;
            if (shoppingCartDetailAdapter.getItemCount() == 0) {
                finish();
            }
            priceInit();
        } else if (type == shoppingCartController.SHOPPING_CART_PRODUCT_ITEM_ALL_REMOVE) {
            shoppingCartDetailAdapter.shoppingCartListRemoveAll();
            finish();
        }
    }

    @Override
    public void onRetrofitFail(int type, String msg) {
        if (type == SHOPPING_CART_PRODUCT_ITEM_PLUS ||
                type == ShoppingCartDetailController.SHOPPING_CART_PRODUCT_ITEM_MINUS || type == shoppingCartController.SHOPPING_CART_PRODUCT_ITEM_REMOVE) {
            // 장바구니 상품 개수 추가 및 감소 실패
            itemClickPosition = -1;

            if (type == SHOPPING_CART_PRODUCT_ITEM_PLUS || type == ShoppingCartDetailController.SHOPPING_CART_PRODUCT_ITEM_MINUS) {
                loadingStop();
            }
        }
    }

    /**
     * 터치 방지
     */
    private void loadingStart() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /**
     * 터치 방지 해제
     */
    private void loadingStop() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
