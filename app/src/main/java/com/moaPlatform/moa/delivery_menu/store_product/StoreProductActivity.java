package com.moaPlatform.moa.delivery_menu.store_product;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.ShoppingCartDetailActivity;
import com.moaPlatform.moa.bottom_menu.shopping_cart.main.ReqInputShoppingCart;
import com.moaPlatform.moa.bottom_menu.shopping_cart.main.ResShoppingCartList;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.delivery_menu.store_product.adapter.AddProductOptionAdapter;
import com.moaPlatform.moa.delivery_menu.store_product.adapter.StoreProductPriceAdapter;
import com.moaPlatform.moa.delivery_menu.store_product.model.AddProductOptionModel;
import com.moaPlatform.moa.delivery_menu.store_product.model.ReqProductDetailInfoModel;
import com.moaPlatform.moa.delivery_menu.store_product.model.ResStoreProductDetailInfoModel;
import com.moaPlatform.moa.online_payment.order_payment.OrderPaymentActivity;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.interfaces.HttpConnectionResult;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.interfaces.ViewDataInitHelper;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.manager.IntentKeyManager;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.ProductAddOption;
import com.moaPlatform.moa.util.toolbar.TopToolbarController;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 상품 상세정보 페이지
 */
public class StoreProductActivity extends AppCompatActivity implements HttpConnectionResult, ViewDataInitHelper, ListItemClickListener, AddProductOptionAdapter.ListItemChangeListener {
    // 탑 툴바
    @BindView(R.id.topToolbar)
    View topToolbar;
    // 서버 연동 컨트롤러
    private StoreProductServerController serverController;
    // 상품 수량 표시할 뷰
    @BindView(R.id.productQuantity)
    TextView tvProductQuantity;
    // 주문 금액 표시할 뷰
    @BindView(R.id.orderPriceContent)
    TextView orderPriceText;
    // 총 주문 금액 표시할 뷰
    @BindView(R.id.totalOrderPriceContent)
    TextView tvTotalOrderPriceText;
    // 가격 표시할 string 포맷
    @BindString(R.string.store_product_price_item_option_price)
    String priceFormat;
    @BindView(R.id.addProductRecyclerView)
    RecyclerView productOption;

    // 상품 수량
    private int productQuantity = 1;
    // 배달표
    private int deliveryPrice = 0;
    // 추가 옵션 가격, 기본 옵션 아이디, 기본 옵션 가격, 할인 가격, 무료 배달 금액
    private int addOptionPrice = 0, defaultOptionId = 0, defaultOptionPrice = 0;
    // 상품 옵션 어뎁터, 상품 선택 옵션 어뎁터
    private RecyclerView.Adapter productionOptAdapter, productAddOptAdapter;
    // 추가 상품 옵션 카테고리를 저장할 sparseArray;
    private SparseArray<AddProductOptionModel> addProductOptionModelSparseArray;
    // 사용자가 추가한 추가 옵션 정보들
    private SparseArray<ProductAddOption> addProductOptionArray = new SparseArray<>();
    private ResStoreProductDetailInfoModel.ProductDetailInfoModel productDetailInfoModel;
    // 가맹점 id, 상품 코드
    int storeId = 0, storeProductCode = 0;
    private ViewDataInitHelper viewDataInitHelper = this;

    // 총 주문 금액
    private int totalOrderPrice = 0;

    private Button orderButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_product_activity);
        ButterKnife.bind(this);
        init();
    }

    /**
     * 변수 초기화
     */
    private void init() {
        orderButton = findViewById(R.id.orderButton);
        storeId = getIntent().getIntExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), 0);
        storeProductCode = getIntent().getIntExtra(CodeTypeManager.StoreInfo.STORE_PRODUCT_CODE.toString(), 0);
        productionOptAdapter = new StoreProductPriceAdapter();
        productAddOptAdapter = new AddProductOptionAdapter();
        ((StoreProductPriceAdapter) productionOptAdapter).setListItemClickListener(this);
        ((AddProductOptionAdapter) productAddOptAdapter).setListItemChangeListener(this);
        ((AddProductOptionAdapter) productAddOptAdapter).setListItemClickListener(this);
        productOptionRecyclerInit(R.id.priceRecyclerView, productionOptAdapter);
        productOptionRecyclerInit(R.id.addProductRecyclerView, productAddOptAdapter);


        getProductInfo();
        TopToolbarController topToolbarController = new TopToolbarController(topToolbar);
        topToolbarController.productDetailToolbarInit(getIntent().getStringExtra(MoaConstants.EXTRA_STORE_NAME));
        topToolbarController.setTopToolbarClickListener(code -> finish());


    }

    /**
     * 상품 정보 서버에서 가져오기
     */
    private void getProductInfo() {
        ReqProductDetailInfoModel reqModel = new ReqProductDetailInfoModel();
        reqModel.init(getIntent());
        serverController = new StoreProductServerController(this);
        serverController.setHttpConnectionResult(this);
        serverController.getStoreProductInfo(reqModel);
    }

    /**
     * 주문 결제 페이지로 이동
     */
    @OnClick(R.id.orderButton)
    void toOrderPayment() {
        Intent intent = new Intent(this, OrderPaymentActivity.class);
        intent.putExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), storeId);
        intent.putExtra(CodeTypeManager.StoreInfo.STORE_PRODUCT_COUNT.toString(), productQuantity);
        intent.putExtra(CodeTypeManager.StoreInfo.STORE_PRODUCT_DEFAULT_OPTION_ID.toString(), defaultOptionId);
        intent.putExtra(CodeTypeManager.StoreInfo.STORE_PRODUCT_CODE.toString(), storeProductCode);
        intent.putExtra("addOptionList", createProductAddOptionList());
        intent.putExtra(CodeTypeManager.StoreInfo.STORE_TOTAL_PRICE.toString(), totalOrderPrice);
        intent.putExtra(CodeTypeManager.StoreInfo.STORE_DELIVERY_PRICE.toString(), deliveryPrice);
        intent.putExtra(CodeTypeManager.StoreInfo.STORE_SAVE_RATE_POINT_TEXT.toString(), viewDataInitHelper.getTextString(getWindow().getDecorView(), R.id.savePoint));
        intent.putExtra("timeEvent", new Gson().toJson(productDetailInfoModel.timeEventModel));
        intent.putExtra("saveRate", productDetailInfoModel.saveRate);
        intent.putExtra("deliveryTitle", getIntent().getStringExtra("deliveryTitle"));
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * 장바구니 담기 버튼 클릭
     */
    @OnClick(R.id.shoppingCartButton)
    void shoppingCart() {
        ReqInputShoppingCart reqInputShoppingCart = new ReqInputShoppingCart();
        reqInputShoppingCart.init(storeId, storeProductCode, defaultOptionId, productQuantity, createProductAddOptionList());
        serverController.inputShoppingCart(reqInputShoppingCart);
    }

    /**
     * SparseArray 로 만들어진 추기옵션 리스트를
     * arrayList 로 변환해서 반환
     *
     * @return 상품 추가 옵션을 arrayList 반환
     */
    private ArrayList<ProductAddOption> createProductAddOptionList() {
        ArrayList<ProductAddOption> productAddOptions = new ArrayList<>();
        if (addProductOptionArray == null)
            addProductOptionArray = new SparseArray<>();
        for (int i = 0; i < addProductOptionArray.size(); i++) {
            productAddOptions.add(addProductOptionArray.valueAt(i));
        }
        return productAddOptions;
    }

    /**
     * 상품 옵션 관련된 RecyclerView 초기화
     *
     * @param layoutId RecyclerView 아이디
     * @param adapter  연결할 Adapter
     */
    private void productOptionRecyclerInit(int layoutId, RecyclerView.Adapter adapter) {
        RecyclerView recyclerView = findViewById(layoutId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 매당 상품 옵션 업데이트
     *
     * @param defaultProductOptionList 업데이트할 리스트
     */
    private void productOptionListUpdate(ArrayList<ResStoreProductDetailInfoModel.DefaultProductOptionModel> defaultProductOptionList) {
        if (defaultProductOptionList != null && defaultProductOptionList.size() != 0) {
            defaultOptionId = defaultProductOptionList.get(0).productOptionId;
            defaultOptionPrice = defaultProductOptionList.get(0).productOptionPrice;
            totalOrderPriceInit();
        } else {
            TextView tvStoreProductSingleItemPrice = findViewById(R.id.tvStoreProductSingleItemPrice);
            tvStoreProductSingleItemPrice.setText(getString(R.string.store_product_price_item_option_price, StringUtil.convertCommaPrice(productDetailInfoModel.productPrice)));
            tvStoreProductSingleItemPrice.setVisibility(View.VISIBLE);
        }
        ((StoreProductPriceAdapter) productionOptAdapter).storProdOptListSetting(defaultProductOptionList, productDetailInfoModel.productPrice);
        productionOptAdapter.notifyDataSetChanged();
    }

    private void addProductOptionListUpdate(ArrayList<ResStoreProductDetailInfoModel.AddProductCategoryModel> storeAddProductList) {
        if (storeAddProductList != null) {
            addProductOptionModelSparseArray = new SparseArray<>();
            ArrayList<AddProductOptionModel> addProductOptionModels = new ArrayList<>();
            for (ResStoreProductDetailInfoModel.AddProductCategoryModel addProductCategoryModel : storeAddProductList) {
                addProductOptionModels.add(addProductCategoryModel);
                addProductOptionModelSparseArray.put(addProductCategoryModel.addProductCategoryId, addProductCategoryModel);
                for (ResStoreProductDetailInfoModel.DefaultProductOptionModel defaultProductOptionModel : addProductCategoryModel.addProductOptionItemList) {
                    defaultProductOptionModel.addProductCategoryId = addProductCategoryModel.addProductCategoryId;
                    addProductOptionModels.add(defaultProductOptionModel);
                }
            }
            ((AddProductOptionAdapter) productAddOptAdapter).listUpdate(addProductOptionModels);
            productAddOptAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 서버 통신 성공했을때
     *
     * @param type      서버 통신 타입값
     * @param baseModel 결과값 모델
     */
    @Override
    public void onHttpConnectionSuccess(int type, BaseModel baseModel) {
        if (type == CodeTypeManager.StoreInfo.GET_STORE_INFO.getType()) {
            productDetailInfoModel = ((ResStoreProductDetailInfoModel) baseModel).productDetailInfoModel;
            productDefaultInfoInit();
            productOptionListUpdate(((ResStoreProductDetailInfoModel) baseModel).defaultProductOptionList);
            addProductOptionListUpdate(((ResStoreProductDetailInfoModel) baseModel).storeAddProdList);
        } else if (type == CodeTypeManager.StoreInfo.INPUT_SHOPPING_CART.getType()) {
            Intent intent = new Intent(this, ShoppingCartDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(IntentKeyManager.SHOPPING_CART_ID, ((ResShoppingCartList) baseModel).shoppingCartId);
            startActivity(intent);
        }
    }

    /**
     * 상품 기본 정보 세팅
     */
    private void productDefaultInfoInit() {
        View view = getWindow().getDecorView();
        try {
            viewDataInitHelper.textViewInit(view, R.id.productName, productDetailInfoModel.productName);
            viewDataInitHelper.textViewInit(view, R.id.productDescription, productDetailInfoModel.productExplain);
            if (productDetailInfoModel.productExplain.length()==0){
                TextView menuExplain = findViewById(R.id.productDescription);
                menuExplain.setVisibility(View.GONE);
            }
            if (productDetailInfoModel.productThumbNail != null && !productDetailInfoModel.productThumbNail.equals("")) {
                viewDataInitHelper.imageViewInit(view, R.id.thumbNail, productDetailInfoModel.productThumbNail);
            } else {
                ImageView thumbNail = findViewById(R.id.thumbNail);
                thumbNail.setVisibility(View.GONE);
            }
            viewDataInitHelper.textViewVisibility(view, R.id.tvSavePercent, R.string.store_detail_save_percent, productDetailInfoModel.saveRate);
            viewDataInitHelper.textViewVisibility(view, R.id.additionalDc, productDetailInfoModel.timeEventModel.addSaveRateContent);
            if (!ObjectUtil.checkNotNull(productDetailInfoModel.timeEventModel.saleContent)) {
                viewDataInitHelper.viewVisibility(view, R.id.deliveryDc, null);
            } else {
                String[] eventType = {"배달", "외식", ""};
                viewDataInitHelper.textViewInit(view, R.id.deliveryDc, eventType[Integer.valueOf(productDetailInfoModel.timeEventModel.timeEventType)] + productDetailInfoModel.timeEventModel.saleContent);
            }
            if (!ObjectUtil.checkNotNull(productDetailInfoModel.timeEventModel.saleRateContent)) {
                viewDataInitHelper.viewVisibility(view, R.id.deliveryPercentDc, null);
            } else {
                String[] eventType = {"배달", "외식", ""};
                viewDataInitHelper.textViewInit(view, R.id.deliveryPercentDc, eventType[Integer.valueOf(productDetailInfoModel.timeEventModel.timeEventType)] + productDetailInfoModel.timeEventModel.saleRateContent);
            }

            Button shoppingCartButton = findViewById(R.id.shoppingCartButton);
            // 가맹점 오픈
            final int STORE_OPEN = 1;
            if (productDetailInfoModel.isOpen == STORE_OPEN) {
                orderButton.setVisibility(View.VISIBLE);
                shoppingCartButton.setVisibility(View.VISIBLE);
            } else {
                orderButton.setVisibility(View.GONE);
                shoppingCartButton.setVisibility(View.GONE);
            }

            TextView tvStoreProductMinOrderPrice = findViewById(R.id.tvStoreProductMinOrderPrice);
            String storeProductMinOrderPriceConvert = StringUtil.convertCommaPrice(productDetailInfoModel.minOrderPrice);
            String storeProductMinOrderPriceText = getString(R.string.activity_store_product_min_order_price, storeProductMinOrderPriceConvert);
            tvStoreProductMinOrderPrice.setText(storeProductMinOrderPriceText);

            TextView deliveryPriceTitle = findViewById(R.id.deliveryPriceTitle);
            String deliveryPriceTitleText = getString(R.string.store_product_delivery_price_title_free_price, StringUtil.convertCommaPrice(productDetailInfoModel.freeDeliveryPrice));
            deliveryPriceTitle.setText(deliveryPriceTitleText);

        } catch (Exception e) {
            Logger.e("매장 상세화면 : " + e.toString());
        }
        totalOrderPriceInit();
    }

    /**
     * 가맹점에 전화 걸기
     */
    private void phoneCall(String phoneNumber) {

        if (phoneNumber != null) {
            TedPermission.with(this).setPermissionListener(new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    Intent intent = new Intent(Intent.ACTION_DIAL);     //ACTION_CALL 전화 바로 걸기 | ACTION_DIAL 전화 화면으로 이동
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                }

                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                }
            }).setDeniedMessage(R.string.call_permission_denied)
                    .setPermissions(Manifest.permission.CALL_PHONE).check();
        }
    }

    /**
     * 상품 수량 세팅
     *
     * @param view 버튼 뷰
     */
    @OnClick({R.id.quantityDown, R.id.quantityUp})
    void quantitySetting(View view) {

        switch (view.getId()) {
            case R.id.quantityDown:
                productQuantity = productQuantity > 1 ? productQuantity - 1 : 1;
                break;
            case R.id.quantityUp:
                productQuantity++;
                break;
        }

        tvProductQuantity.setText(String.valueOf(productQuantity));
        totalOrderPriceInit();
    }

    /**
     * 총 주문 금액
     */
    private void totalOrderPriceInit() {
        totalOrderPrice = (productDetailInfoModel.productPrice + defaultOptionPrice) * productQuantity + addOptionPrice;
        // 주문 가격 세팅
        orderPriceText.setText(getString(R.string.store_product_total_money, viewDataInitHelper.commaUnitChange(totalOrderPrice)));

        if (totalOrderPrice >= productDetailInfoModel.minOrderPrice) {
            orderButton.setBackgroundResource(R.color.coral);
            orderButton.setEnabled(true);
        } else {
            orderButton.setBackgroundResource(R.color.nobel);
            orderButton.setEnabled(false);
        }

        // 배달비 세팅
        deliveryPrice = totalOrderPrice >= productDetailInfoModel.freeDeliveryPrice ? 0 : Integer.valueOf(productDetailInfoModel.deliveryPrice);
        viewDataInitHelper.textViewInitConvertPrice(getWindow().getDecorView(), R.id.deliveryPriceContent, R.string.store_product_price_item_option_price, deliveryPrice);

        int tempTotalPrice = totalOrderPrice + deliveryPrice;

        // 타임 이벤트 계산
        // 적액률
        int fixAmount = 0;
        // 적립률
        int fixRateAmount = 0;
        if (productDetailInfoModel.timeEventModel != null) {
            if (Integer.valueOf(productDetailInfoModel.timeEventModel.fixAmt) > tempTotalPrice) {
                fixAmount = tempTotalPrice;
            } else {
                fixAmount = Integer.valueOf(productDetailInfoModel.timeEventModel.fixAmt);
            }

            int tempFixRateAmount = (tempTotalPrice - fixAmount) * productDetailInfoModel.timeEventModel.fixRate / 100;

            if (tempFixRateAmount > productDetailInfoModel.timeEventModel.maxSalePrice) {
                fixRateAmount = productDetailInfoModel.timeEventModel.maxSalePrice;
            } else {
                fixRateAmount = tempFixRateAmount;
            }
        }

        // 할인가격
//        int salePrice = productDetailInfoModel.timeEventModel.fixAmt == null ? 0 : Integer.valueOf(productDetailInfoModel.timeEventModel.fixAmt);
//        salePrice += ((totalOrderPrice - salePrice) * productDetailInfoModel.timeEventModel.fixRate / 100) > productDetailInfoModel.timeEventModel.maxSalePrice ? productDetailInfoModel.timeEventModel.maxSalePrice : (totalOrderPrice * productDetailInfoModel.timeEventModel.fixRate / 100);
        viewDataInitHelper.textViewInitConvertPrice(getWindow().getDecorView(), R.id.salePriceContent, R.string.store_product_total_money, fixAmount + fixRateAmount);

        // 총 결제 금액 , 정액 계산후 정률 계산
//        int totalPaymentPrice = totalOrderPrice + deliveryPrice - salePrice;
        tvTotalOrderPriceText.setText(getString(R.string.store_product_total_money, viewDataInitHelper.commaUnitChange(tempTotalPrice - fixAmount - fixRateAmount)));

        // 적립률
        int saveRatePercent = Integer.valueOf(productDetailInfoModel.saveRate) + productDetailInfoModel.timeEventModel.addSaveRate;
        viewDataInitHelper.textViewInitConvertPrice(getWindow().getDecorView(), R.id.savePoint, R.string.store_product_save_point_title, ((tempTotalPrice - fixAmount - fixRateAmount) * saveRatePercent / 100));

    }

    @Override
    public void clickItem(Object codeType, int position, Object object) {
        if (codeType.equals(CodeTypeManager.StoreInfo.STORE_PRODUCT_ADD_OPTION_REMOVE.getType())) {
            ResStoreProductDetailInfoModel.DefaultProductOptionModel defaultProductOptionModel = (ResStoreProductDetailInfoModel.DefaultProductOptionModel) object;
            addProductOptionModelSparseArray.get(defaultProductOptionModel.addProductCategoryId).checkingSize--;
            addProductOptionArray.delete(defaultProductOptionModel.productOptionId);
            addOptionPrice -= defaultProductOptionModel.productOptionPrice;
        } else {
            ResStoreProductDetailInfoModel.DefaultProductOptionModel productDefaultOptionModel = (ResStoreProductDetailInfoModel.DefaultProductOptionModel) object;
            defaultOptionId = productDefaultOptionModel.productOptionId;
            defaultOptionPrice = productDefaultOptionModel.productOptionPrice;
            productionOptAdapter.notifyDataSetChanged();
        }
        totalOrderPriceInit();
    }

    @Override
    public void onItemChanged(int type, int position, Object object, RecyclerView.ViewHolder holder) {
        ResStoreProductDetailInfoModel.DefaultProductOptionModel defaultProductOptionModel = (ResStoreProductDetailInfoModel.DefaultProductOptionModel) object;
        if (addProductOptionModelSparseArray.get(defaultProductOptionModel.addProductCategoryId).checkPossible()) {
            ((AddProductOptionAdapter.AddProductOptionContentHolder) holder).addProductCheckBoxChange();
            ProductAddOption productAddOption = new ProductAddOption();
            productAddOption.init(defaultProductOptionModel.addProductCategoryId, defaultProductOptionModel.productOptionId);
            addProductOptionArray.put(defaultProductOptionModel.productOptionId, productAddOption);
            addOptionPrice += defaultProductOptionModel.productOptionPrice;
            totalOrderPriceInit();
        }
    }
}
