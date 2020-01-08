package com.moaPlatform.moa.online_payment.order_payment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.activity.EventWebViewActivity;
import com.moaPlatform.moa.auth.sign_up.controller.LogInActivity;
import com.moaPlatform.moa.auth.sign_up.controller.SignUpActivity;
import com.moaPlatform.moa.bottom_menu.main.MainActivity;
import com.moaPlatform.moa.bottom_menu.main.model.ResMainServiceModel;
import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.ShoppingCartDetailController;
import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model.ReqShoppingCartDetailModel;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.MoaPayAgrmnt;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.WalletAgreeActivity;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.constants.MoaUrlConstants;
import com.moaPlatform.moa.online_payment.order_payment.controller.OrderPaymentServerController;
import com.moaPlatform.moa.online_payment.order_payment.model.ReqPaymentOrderModel;
import com.moaPlatform.moa.online_payment.order_payment.model.ResGetPintDataModel;
import com.moaPlatform.moa.online_payment.order_payment.model.ResUseCouponModel;
import com.moaPlatform.moa.online_payment.order_payment.model.req.OrderDscntList;
import com.moaPlatform.moa.online_payment.order_payment.model.res.ResPaymentResult;
import com.moaPlatform.moa.payment.StartEasyPayActivity;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.auth.UserUseHelper;
import com.moaPlatform.moa.util.custom_view.WaySelectButton;
import com.moaPlatform.moa.util.dialog.yesOrNo.LoginSigninDialog;
import com.moaPlatform.moa.util.interfaces.HttpConnectionResult;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.interfaces.ViewDataInitHelper;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.ProductAddOption;
import com.moaPlatform.moa.util.models.StoreProductInfoModel;
import com.moaPlatform.moa.util.singleton.App;
import com.moaPlatform.moa.util.singleton.RetrofitClient;
import com.moaPlatform.moa.util.toolbar.TopToolbarController;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderPaymentActivity extends AppCompatActivity implements HttpConnectionResult, ViewDataInitHelper {

    public final static String INTENT_REMOVE_SHOPPING_CART_ITEM = "removeShoppingCartRemoveItem";

    // 결제 성공 타입
    private final int PAYMENT_CODE = 3000;
    private final int MOAPAYMENT_CODE = 5000;
    private final int SELECT_COUPON_CODE = 4000;
    // 테이크아웃으로 주문 버튼
    @BindView(R.id.btnTakeOut)
    WaySelectButton btnTakeOut;
    // 배달 주문 버튼
    @BindView(R.id.btnDelivery)
    WaySelectButton btnDelivery;
    // 바로 주문 버튼
    @BindView(R.id.btnImmediateOrder)
    WaySelectButton btnImmediateOrder;
    // 예약 주문 버튼
    @BindView(R.id.btnReservationOrder)
    WaySelectButton btnReservationOrder;
    // 현금 결제
    @BindView(R.id.btnCashPay)
    WaySelectButton btnCashPay;
    // 카드 결제
    @BindView(R.id.btnCreditCard)
    WaySelectButton btnCreditCard;
    // 모아 페이로 결제
    @BindView(R.id.btnMoaPay)
    WaySelectButton btnMoaPay;
    // 이지 페이로 결제
    @BindView(R.id.btnEasyPay)
    WaySelectButton btnEasyPay;
    // 현장 결제 라이오 버튼
    @BindView(R.id.radioBtnPay)
    RadioButton radioBtnPay;
    // 바로 결제 라이오 버튼
    @BindView(R.id.radioBtnImmediatePay)
    RadioButton radioBtnImmediatePay;
    // 기본 주소
//    @BindView(R.id.editDefaultAddress)
//    TextView tvOrderPaymentRoadAddress;
    //지번 주소
//    @BindView(R.id.editDefaultRoadAddress)
//    TextView editDefaultRoadAddress;
    // 상세 주소
    @BindView(R.id.etOrderPaymentDetailRoadAddress)
    EditText etDetailAddress;
    // 휴대폰 번호
    @BindView(R.id.etOrderPaymentPhoneNumber)
    EditText etPhoneNumber;
    ResMainServiceModel.UserInfoModel userInfoMode;
    OrderPaymentServerController serverController;
    int orderPrice;
    int deliveryPrice;
    int couponType = -1;
    int couopnValue = -1;
    int couoponMaxValue = -1;
    int usePoint = 0;
    int maxPoint = 0;
    private int totalPaymentPrice = 0;
    // 서버에 전송할 결제 주문 정보 입력 정보
    private ReqPaymentOrderModel reqPaymentOrderModel = new ReqPaymentOrderModel();
    private ViewDataInitHelper viewDataInitHelper = this;
    // 주문 번호
    private String orderId = null;
    private boolean isPaymentWayBtnClick = false;
    private ResPaymentResult resPaymentResult;
    private TextView useMoaPoint;
    private EditText etMyUsePoint;
    private StoreProductInfoModel.TimeEventInfo timeEventInfo;

    private LoginSigninDialog loginSigninDialog;
    // 현장 결제 유무
    private boolean isSitePayment = false;
    // 현장 결제 유무 키값
    public final String KEY_IS_SITE_PAYMENT = "isSitePayment";

    // 0 : 포인트 사용안함, 1 : 포인트 사용, 2 : 포인트 전액 사용
    private int pointType = 0;
    private final int NON_POINT = 0;
    private final int USE_POINT = 1;
    private final int ALL_USE_POINT = 2;

    private View.OnClickListener loginListener = v -> {
        if (loginSigninDialog != null && loginSigninDialog.isShowing()) {
            loginSigninDialog.dismiss();
        }
        Intent intent = new Intent(OrderPaymentActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, MoaConstants.REQUEST_USER_LOGIN);
    };

    private View.OnClickListener signinListener = v -> {
        if (loginSigninDialog != null && loginSigninDialog.isShowing()) {
            loginSigninDialog.dismiss();
        }
        Intent intent = new Intent(OrderPaymentActivity.this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, MoaConstants.REQUEST_USER_JOIN);
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_payment_activity);
        ButterKnife.bind(this);
        radioBtnImmediatePay.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO JIWON : 2019-07-18 onActivityResult 에서 필요할때 데이터만 갱신 하도록 수정 진행 할 것(동일 날짜 주석 참조할것)
        init();
        viewInit();
        topToolbarSetting();
        subTitlerequiredIndications();
    }

    /**
     * 초기화
     */
    private void init() {
        Logger.i("사용자 정보 확인 : " + new Gson().toJson(App.getInstance().userInfoModel));

        userInfoMode = App.getInstance().userInfoModel;

        // 기본 도로명 주소
        final String ROAD_DEFAULT_ADDRESS = userInfoMode.roadDefaultAddress;
        final String JIBUN_ADDRESS = userInfoMode.jibunAddress;

        // 도로명 주소 세팅
        TextView tvOrderPaymentRoadAddress = findViewById(R.id.tvOrderPaymentRoadAddress);
        tvOrderPaymentRoadAddress.setText(ROAD_DEFAULT_ADDRESS);
        // 지번 주소 세팅
        TextView tvOrderPaymentJibunAddress = findViewById(R.id.tvOrderPaymentJibunAddress);
        // textView 에 적용할 지번 주소
        String jibunAddressText = getString(R.string.activity_order_payment_jibun_address, JIBUN_ADDRESS);
        tvOrderPaymentJibunAddress.setText(StringUtil.convertHtmlFormat(jibunAddressText));

        if (etDetailAddress.getText().toString().trim().length() == 0)
            etDetailAddress.setText(userInfoMode.roadDetailAddress);
        if (etPhoneNumber.getText().toString().trim().length() == 0)
            etPhoneNumber.setText(userInfoMode.phoneNumber);


        serverController = new OrderPaymentServerController(this);
        serverController.setHttpConnectionResult(this);
        serverController.getPoint();


        // 결제정보의 기본정보 세팅
        // 도로명 주소 세팅
        reqPaymentOrderModel.orderDefaultRoadAddress = ROAD_DEFAULT_ADDRESS;
        // 지번 주소 세팅
        reqPaymentOrderModel.orderJibunAddress = JIBUN_ADDRESS;

        View deliveryOrderDangerAllSee = findViewById(R.id.deliveryOrderDangerAllSee);
        deliveryOrderDangerAllSee.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EventWebViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, BuildConfig.REST_API_BASE_URL + MoaUrlConstants.PRECAUTIONS_FOR_DELIVERT_ORDER_URL);
            intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_TRANSPARENT_VALUE);
            startActivity(intent);
        });

        View otherAgreeAllSee = findViewById(R.id.otherAgreeAllSee);
        otherAgreeAllSee.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EventWebViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, BuildConfig.REST_API_BASE_URL + MoaUrlConstants.AGEE_TO_PROVIDE_PRESONAL_INFOMATION_TO_THIRD_PARTIES);
            intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_TRANSPARENT_VALUE);
            startActivity(intent);
        });

    }

    /**
     * 서브 타이틀의 별표 표시
     */
    public void subTitlerequiredIndications() {
        TextView addressTitle = findViewById(R.id.tvOrderPaymentPhoneNumberTitle);
        String defaultText = getString(R.string.activity_order_payment_contact);
        String starText = "<font color=\"" + getResources().getColor(R.color.coral) + "\" , seTextAlign(Align align)> *</font>";

        addressTitle.setText(Html.fromHtml(defaultText + starText));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            addressTitle.setText(Html.fromHtml(defaultText + starText));
        } else {
            addressTitle.setText(Html.fromHtml(defaultText + starText, Html.FROM_HTML_MODE_LEGACY));
        }
    }

    private void viewInit() {
        View view = getWindow().getDecorView();
        viewDataInitHelper.textViewInit(view, R.id.tvSaveRatePointText, getIntent().getStringExtra(CodeTypeManager.StoreInfo.STORE_SAVE_RATE_POINT_TEXT.toString()));
        orderPrice = getIntent().getIntExtra(CodeTypeManager.StoreInfo.STORE_TOTAL_PRICE.toString(), 0);
        deliveryPrice = getIntent().getIntExtra(CodeTypeManager.StoreInfo.STORE_DELIVERY_PRICE.toString(), 0);
        viewDataInitHelper.textViewInitConvertPrice(view, R.id.orderMoney, R.string.store_product_total_money, orderPrice);
//        viewDataInitHelper.textViewInitConvertPrice(view, R.id.deliveryMoneyContent, R.string.store_product_total_money, deliveryPrice);

//        viewDataInitHelper.textViewInit(view, R.id.deliveryMoneyTitle, getIntent().getStringExtra("deliveryTitle"));
        useMoaPoint = findViewById(R.id.useMoaPoint);

        UserUseHelper userUseHelper = new UserUseHelper(this);
        Button btMyUsePoint = findViewById(R.id.btMyUsePoint);
        Button btnAllPoint = findViewById(R.id.btnAllPoint);
        Button btnCouponSelect = findViewById(R.id.btnCouponSelect);
        if (userUseHelper.isUserLogin()) {
            viewDataInitHelper.viewVisibility(view, R.id.saleCoupon, "Y");
            viewDataInitHelper.viewVisibility(view, R.id.saleCouponNeedLogin, null);
//            viewDataInitHelper.viewVisibility(view, R.id.useMoaPoint, "Y");
            useMoaPoint.setVisibility(View.VISIBLE);
            viewDataInitHelper.viewVisibility(view, R.id.useMoaPointNeedLogin, null);
//            viewDataInitHelper.viewVisibility(view, R.id.btnAllPoint, "Y");
            // 포인트 사용 버튼 활성화
            btMyUsePoint.setEnabled(true);
            btMyUsePoint.setBackgroundResource(R.drawable.common_white_coral_style_nonradius);
            btMyUsePoint.setTextColor(getResources().getColor(R.color.coral));
            // 포인트 전액 사용 버튼 활성화
            btnAllPoint.setEnabled(true);
            btnAllPoint.setBackgroundResource(R.drawable.common_white_coral_style_nonradius);
            btnAllPoint.setTextColor(getResources().getColor(R.color.coral));
            // 쿠폰 조회 버튼 활성화
            btnCouponSelect.setEnabled(true);
            btnCouponSelect.setBackgroundResource(R.drawable.common_white_coral_style_nonradius);
            btnCouponSelect.setTextColor(getResources().getColor(R.color.coral));
        } else {
            viewDataInitHelper.viewVisibility(view, R.id.saleCoupon, null);
            viewDataInitHelper.viewVisibility(view, R.id.saleCouponNeedLogin, "Y");
//            viewDataInitHelper.viewVisibility(view, R.id.useMoaPoint, null);
            useMoaPoint.setVisibility(View.GONE);
            viewDataInitHelper.viewVisibility(view, R.id.useMoaPointNeedLogin, "Y");
//            viewDataInitHelper.viewVisibility(view, R.id.btnAllPoint, "N");
            // 포인트 사용 버튼 비활성화
            btMyUsePoint.setEnabled(false);
            btMyUsePoint.setTextColor(getResources().getColor(R.color.color_989898));
            // 포인트 전액 사용 버튼 비활성화
            btnAllPoint.setEnabled(false);
            btnAllPoint.setTextColor(getResources().getColor(R.color.color_989898));
            // 쿠폰 조회 버튼 비활성화
            btnCouponSelect.setEnabled(false);
            btnCouponSelect.setTextColor(getResources().getColor(R.color.color_989898));

            TextView useMoaPointNeedLogin = findViewById(R.id.useMoaPointNeedLogin);
            useMoaPointNeedLogin.setOnClickListener(loginListener);

            TextView saleCouponNeedLogin = findViewById(R.id.saleCouponNeedLogin);
            saleCouponNeedLogin.setOnClickListener(loginListener);
        }

//        CheckBox cbMoaPoint = findViewById(R.id.moaPointTitle);
//        if (userUseHelper.isUserLogin())
//            cbMoaPoint.setClickable(true);
//        else
//            cbMoaPoint.setClickable(false);

//        cbMoaPoint.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked)
//                viewDataInitHelper.viewVisibility(view, R.id.viewInputMoaPoint, "Y");
//            else
//                viewDataInitHelper.viewVisibility(view, R.id.viewInputMoaPoint, "N");
//        });

        etMyUsePoint = findViewById(R.id.etMyUsePoint);
        etMyUsePoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btMyUsePoint.setOnClickListener(v -> {
            usePoint = etMyUsePoint.getText().toString().equals("") ? 0 : Integer.valueOf(etMyUsePoint.getText().toString());
            pointType = USE_POINT;
            totalPriceInit();
        });

        EditText editReqTermTitle = findViewById(R.id.etOrderPaymentReqTerm);
        viewDataInitHelper.textViewInit(view, R.id.tvOrderPaymentReqTermCount, getString(R.string.order_payment_delivery_info_req_count, 0));
        TextView tvOrderPaymentReqTermCount = findViewById(R.id.tvOrderPaymentReqTermCount);
        editReqTermTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable inputText) {
                if (inputText.toString().length() == 50) {
                    SpannableStringBuilder asd = new SpannableStringBuilder();
                    asd.append(StringUtil.convertFontColor("50", Color.RED));
                    asd.append(StringUtil.convertHtmlFormat("/50자"));
                    tvOrderPaymentReqTermCount.setText(asd);
                } else {
                    tvOrderPaymentReqTermCount.setText(getString(R.string.order_payment_delivery_info_req_count, inputText.toString().length()));
                }
            }
        });

        timeEventInfo = new Gson().fromJson(getIntent().getStringExtra("timeEvent"), StoreProductInfoModel.TimeEventInfo.class);
        viewDataInitHelper.textViewVisibility(view, R.id.tvSavePercent, R.string.store_detail_save_percent, getIntent().getStringExtra("saveRate"));
        viewDataInitHelper.textViewVisibility(view, R.id.additionalDc, timeEventInfo.addSaveRateContent);
        if (!ObjectUtil.checkNotNull(timeEventInfo.saleContent)) {
            viewDataInitHelper.viewVisibility(view, R.id.deliveryDc, null);
        } else {
            viewDataInitHelper.textViewInit(view, R.id.deliveryDc, timeEventInfo.saleContent);
        }

        if (!ObjectUtil.checkNotNull(timeEventInfo.saleRateContent)) {
            viewDataInitHelper.viewVisibility(view, R.id.deliveryPercentDc, null);
        } else {
            viewDataInitHelper.textViewInit(view, R.id.deliveryPercentDc, timeEventInfo.saleRateContent);
        }
        totalPriceInit();
//        radioBtnPay.setChecked(false);


        btnAllPoint.setOnClickListener(v -> {
            if (maxPoint >= totalPaymentPrice) {
                usePoint = totalPaymentPrice;
            } else {
                usePoint = maxPoint;
            }
//            useMoaPoint.setText(getString(R.string.order_payment_use_point, StringUtil.convertCommaPrice(usePoint), StringUtil.convertCommaPrice(usePoint)));
            pointType = ALL_USE_POINT;
            totalPriceInit();
        });
    }

    /**
     * 상단 툴바 세팅
     */
    private void topToolbarSetting() {
        View topToolbar = findViewById(R.id.topToolbar);
        TopToolbarController topToolbarController = new TopToolbarController(topToolbar);
        topToolbarController.orderPaymentInit();
        topToolbarController.setTopToolbarClickListener(code -> finish());
    }

    private void totalPriceInit() {

        int tempDeliveryPirce = 0;
        TextView deliveryMoneyContent = findViewById(R.id.deliveryMoneyContent);
        if (reqPaymentOrderModel.dealSeparationCode.equals(CodeTypeManager.PaymentCode.ORDER_SEPA_CD_TAKE_OUT.getType())) {
            deliveryMoneyContent.setText(getString(R.string.store_product_total_money, "0"));
        } else {
            String deliveryPriceConvert = StringUtil.convertCommaPrice(deliveryPrice);
            String deliveryPriceText = getString(R.string.store_product_total_money, deliveryPriceConvert);
            deliveryMoneyContent.setText(deliveryPriceText);
            tempDeliveryPirce = deliveryPrice;
        }

        int tempTotalOriginPrice = orderPrice + tempDeliveryPirce;

        // 타임 이벤트 계산
        // 적액률
        int fixAmount = 0;
        // 적립률
        int fixRateAmount = 0;
        if (timeEventInfo != null) {
            if (Integer.valueOf(timeEventInfo.fixAmt) > tempTotalOriginPrice) {
                fixAmount = tempTotalOriginPrice;
            } else {
                fixAmount = Integer.valueOf(timeEventInfo.fixAmt);
            }

            int tempFixRateAmount = (tempTotalOriginPrice - fixAmount) * timeEventInfo.fixRate / 100;

            if (tempFixRateAmount > timeEventInfo.maxSalePrice) {
                fixRateAmount = timeEventInfo.maxSalePrice;
            } else {
                fixRateAmount = tempFixRateAmount;
            }
        }

        tempTotalOriginPrice = tempTotalOriginPrice - fixAmount - fixRateAmount;

        if (tempTotalOriginPrice <= 0) {
            totalPriceViewInit(0, 0);
            return;
        }

        int couoponSale = 0;
        if (couopnValue != -1) {
            if (couponType == 2) {
                couoponSale = couopnValue > couoponMaxValue ? couoponMaxValue : couopnValue;
            } else {
                int couoponSaleTemp = (tempTotalOriginPrice * couopnValue / 100);
                couoponSale = couoponSaleTemp > couoponMaxValue ? couoponMaxValue : couoponSaleTemp;
            }
        }

        tempTotalOriginPrice -= couoponSale;

        if (tempTotalOriginPrice <= 0) {
            totalPriceViewInit(0, couoponSale);
            return;
        }

        switch (pointType) {
            case USE_POINT:
                if (usePoint > maxPoint)
                    usePoint = maxPoint;

                if (tempTotalOriginPrice - usePoint <= 0) {
                    usePoint = tempTotalOriginPrice;
                    tempTotalOriginPrice = 0;
                } else {
                    tempTotalOriginPrice -= usePoint;
                }
                break;
            case ALL_USE_POINT:
                if (tempTotalOriginPrice >= maxPoint) {
                    tempTotalOriginPrice -= maxPoint;
                    usePoint = maxPoint;
                } else {
                    usePoint = tempTotalOriginPrice;
                    tempTotalOriginPrice = 0;
                }
                break;
        }

        totalPriceViewInit(tempTotalOriginPrice, couoponSale);

//        int salePrice = Integer.valueOf(timeEventInfo.fixAmt);
//        salePrice += ((orderPrice - salePrice) * timeEventInfo.fixRate / 100) > timeEventInfo.maxSalePrice ? timeEventInfo.maxSalePrice : (orderPrice * timeEventInfo.fixRate / 100);
//
//        totalPaymentPrice = orderPrice - salePrice;
//        TextView deliveryMoneyContent = findViewById(R.id.deliveryMoneyContent);
//        if (reqPaymentOrderModel.dealSeparationCode.equals(CodeTypeManager.PaymentCode.ORDER_SEPA_CD_TAKE_OUT.getType())) {
//            deliveryMoneyContent.setText(getString(R.string.store_product_total_money, "0"));
//        } else {
//            String deliveryPriceConvert = StringUtil.convertCommaPrice(deliveryPrice);
//            String deliveryPriceText = getString(R.string.store_product_total_money, deliveryPriceConvert);
//            deliveryMoneyContent.setText(deliveryPriceText);
//            totalPaymentPrice += deliveryPrice;
//        }
//
//        int couoponSale = 0;
//        if (couopnValue != -1) {
//            if (couponType == 2) {
//                couoponSale = couopnValue > couoponMaxValue ? couoponMaxValue : couopnValue;
//                totalPaymentPrice -= couoponSale;
//            } else {
//                int couoponSaleTemp = (totalPaymentPrice * couopnValue / 100);
//                couoponSale = couoponSaleTemp > couoponMaxValue ? couoponMaxValue : couoponSaleTemp;
//                totalPaymentPrice -= couoponSale;
//            }
//        }
//
//        String couopnText = "-" + StringUtil.convertCommaPrice(couoponSale) + "원";
//        TextView saleCoupon = findViewById(R.id.saleCoupon);
//        saleCoupon.setText(couopnText);
//        totalPaymentPrice -= usePoint;
//        viewDataInitHelper.textViewInitConvertPrice(getWindow().getDecorView(), R.id.totalPrice, R.string.store_product_total_money, totalPaymentPrice);
//        viewDataInitHelper.textViewInitConvertPrice(getWindow().getDecorView(), R.id.totalPrice, R.string.store_product_total_money, totalPaymentPrice);
//
//        int saveRatePercent = Integer.valueOf(getIntent().getStringExtra("saveRate")) + timeEventInfo.addSaveRate;
//        viewDataInitHelper.textViewInitConvertPrice(getWindow().getDecorView(), R.id.tvSaveRatePointText, R.string.store_product_save_point_title, (totalPaymentPrice * saveRatePercent / 100));

    }

    private void totalPriceViewInit(int finalTotalPrice, int couponSalePrice) {
//        pointType = NON_POINT;
        TextView tvTotalPrice = findViewById(R.id.totalPrice);
        String totalPrice = StringUtil.convertCommaPrice(finalTotalPrice);
        String totalPriceText = getString(R.string.store_product_total_money, totalPrice);
        tvTotalPrice.setText(totalPriceText);

        String couopnText = "-" + StringUtil.convertCommaPrice(couponSalePrice) + "원";
        TextView saleCoupon = findViewById(R.id.saleCoupon);
        saleCoupon.setText(couopnText);

//        etMyUsePoint.getText().toString().equals("") ? 0 : Integer.valueOf(etMyUsePoint.getText().toString());
        etMyUsePoint.setText(String.valueOf(usePoint));
        String usePt = viewDataInitHelper.commaUnitChange(usePoint);
        useMoaPoint.setText(getString(R.string.order_payment_use_point, usePt, usePt));

        TextView tvSaveRatePointText = findViewById(R.id.tvSaveRatePointText);
        int d = Integer.valueOf(getIntent().getStringExtra("saveRate"));
        tvSaveRatePointText.setText(getString(R.string.store_product_save_point_title, StringUtil.convertCommaPrice(String.valueOf(((finalTotalPrice * d) / 100)))));
    }

    /**
     * 배달방식 선택에서 배달, 테이크 아웃 선택시
     */
    @OnClick({R.id.btnDelivery, R.id.btnTakeOut})
    void orderSeparationCodeInit(View view) {
        switch (view.getId()) {
            case R.id.btnDelivery:
                reqPaymentOrderModel.dealSeparationCode = CodeTypeManager.PaymentCode.ORDER_SEPA_CD_DELIVERY.getType();
                break;
            case R.id.btnTakeOut:
                reqPaymentOrderModel.dealSeparationCode = CodeTypeManager.PaymentCode.ORDER_SEPA_CD_TAKE_OUT.getType();
                break;
        }
        btnDelivery.setChecked(!btnDelivery.isChecked);
        btnTakeOut.setChecked(!btnTakeOut.isChecked);
        totalPriceInit();
    }

    /**
     * 바로주문 및 예약주문 클릭시
     */
    @OnClick({R.id.btnImmediateOrder, R.id.btnReservationOrder})
    void dealSeparationCodeInit(View view) {
        switch (view.getId()) {
            case R.id.btnImmediateOrder:
                reqPaymentOrderModel.dealSeparationCode = CodeTypeManager.PaymentCode.DEAL_SEPA_CD_RIGHT_ORDER.getType();
                break;
            case R.id.btnReservationOrder:
                reqPaymentOrderModel.dealSeparationCode = CodeTypeManager.PaymentCode.DEAL_SEPA_CD_RESERVATION_ORDER.getType();
                break;
        }
        btnImmediateOrder.setChecked(!btnImmediateOrder.isChecked);
        btnReservationOrder.setChecked(!btnReservationOrder.isChecked);
    }

    // 결제 수단 - 바로결제, 현장 결제
    @OnCheckedChanged({R.id.radioBtnImmediatePay, R.id.radioBtnPay})
    void paymentWay(CompoundButton button, boolean checked) {
        switch (button.getId()) {
            case R.id.radioBtnImmediatePay:
                if (checked) {
                    reqPaymentOrderModel.paymentCode = CodeTypeManager.PaymentCode.PAY_MTHD_CD_RIGHT_PAYMENT.getType();
                    btnPaymentWay(btnMoaPay);
                    isPaymentWayBtnClick = false;
                }
                break;
            case R.id.radioBtnPay:
                if (checked) {
                    reqPaymentOrderModel.paymentCode = CodeTypeManager.PaymentCode.PAY_MTHD_CD_MEET_PAYMENT.getType();
                    if (!isPaymentWayBtnClick)
                        btnPaymentWay(btnCreditCard);
                    isPaymentWayBtnClick = false;
                }
                break;
        }
    }

    @OnClick({R.id.btnMoaPay, R.id.btnCreditCard, R.id.btnCashPay, R.id.btnEasyPay})
    void btnPaymentWay(View view) {
        isPaymentWayBtnClick = true;
        if (view.getId() == R.id.btnMoaPay || view.getId() == R.id.btnEasyPay) {
            radioBtnImmediatePay.setChecked(true);
            btnCreditCard.setChecked(false);
            radioBtnPay.setChecked(false);
            btnCashPay.setChecked(false);
            btnMoaPay.setBackgroundResource(R.drawable.common_white_coral_style_10);
//            btnMoaPay.setTextColor(getResources().getColor(R.color.coral));
            if (view.getId() == R.id.btnMoaPay) {
                btnMoaPay.setChecked(true);
                btnEasyPay.setChecked(false);
                reqPaymentOrderModel.paymentSeparationCode = CodeTypeManager.PaymentCode.PAY_MTHD_SEPA_CD_MOA_PAY.getType();
            } else {
                btnMoaPay.setChecked(false);
                btnEasyPay.setChecked(true);
                reqPaymentOrderModel.paymentSeparationCode = CodeTypeManager.PaymentCode.PAY_MTHD_SEPA_CD_CARD.getType();
            }

        } else {
            if (view.getId() == R.id.btnCreditCard) {
                reqPaymentOrderModel.paymentSeparationCode = CodeTypeManager.PaymentCode.PAY_MTHD_SEPA_CD_CARD.getType();
                btnCreditCard.setChecked(true);
                btnCashPay.setChecked(false);
            } else {
                reqPaymentOrderModel.paymentSeparationCode = CodeTypeManager.PaymentCode.PAY_MTHD_SEPA_CD_CASH.getType();
                btnCreditCard.setChecked(false);
                btnCashPay.setChecked(true);
            }
            btnMoaPay.setChecked(false);
            btnEasyPay.setChecked(false);
            radioBtnImmediatePay.setChecked(false);
            radioBtnPay.setChecked(true);
//            btnMoaPay.setBackgroundResource(R.drawable.common_button_type);
//            btnMoaPay.setTextColor(getResources().getColor(R.color.matterhorn));
        }

    }

    /**
     * 결제버튼 클릭시
     */
    @SuppressWarnings("unchecked")
    @OnClick(R.id.paymentBtn)
    void payment() {
        serverController.setHttpConnectionResult(this);
        View view = getWindow().getDecorView();
        reqPaymentOrderModel.userId = MoaAuthHelper.getInstance().getBasePrimaryInfo();
        reqPaymentOrderModel.orderDetailRoadAddress = etDetailAddress.getText().toString();

        if (!editTextEmptyCheck(etPhoneNumber))
            reqPaymentOrderModel.orderPhoneNumber = etPhoneNumber.getText().toString();
        else {
            Toast.makeText(this, "연락처 정보가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
            ScrollView scrollView = findViewById(R.id.scrollView);
            etPhoneNumber.requestFocus();
            scrollView.smoothScrollTo(0, 0);
            return;
        }
        // * 요청사항은 데이터가 공백이여도 상관없음 but 현재 공백이면 결제 불가
        reqPaymentOrderModel.orderReqTerm = viewDataInitHelper.getEditTextData(view, R.id.etOrderPaymentReqTerm);
        reqPaymentOrderModel.storeId = getIntent().getIntExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), 0);
//        reqPaymentOrderModel.orderDefaultRoadAddress = tvOrderPaymentRoadAddress.getText().toString();
//        reqPaymentOrderModel.orderJibunAddress = userInfoMode.jibunAddress;
        reqPaymentOrderModel.orderLatitude = userInfoMode.lat;
        reqPaymentOrderModel.orderLongitude = userInfoMode.lnt;
        reqPaymentOrderModel.orderPostNumber = userInfoMode.postNum;
        if (getIntent().getStringExtra("tempTemp") != null) {
            ReqPaymentOrderModel tempReqPaymentOrderModel = App.getInstance().gson.fromJson(getIntent().getStringExtra("tempTemp"), ReqPaymentOrderModel.class);
            reqPaymentOrderModel.orderDtl = tempReqPaymentOrderModel.orderDtl;
        } else {
            ArrayList<ProductAddOption> addOptionList = (ArrayList<ProductAddOption>) getIntent().getSerializableExtra("addOptionList");
            reqPaymentOrderModel.orderDetailModelInit(
                    getIntent().getIntExtra(CodeTypeManager.StoreInfo.STORE_PRODUCT_CODE.toString(), 0),
                    getIntent().getIntExtra(CodeTypeManager.StoreInfo.STORE_PRODUCT_DEFAULT_OPTION_ID.toString(), 0),
                    getIntent().getIntExtra(CodeTypeManager.StoreInfo.STORE_PRODUCT_COUNT.toString(), 0),
                    addOptionList);

//            if (etMyUsePoint.getText().toString().equals(""))
//                reqPaymentOrderModel.pointAmountUse = 0;
//            else
//                reqPaymentOrderModel.pointAmountUse = Integer.valueOf(etMyUsePoint.getText().toString());
        }
        reqPaymentOrderModel.pointAmountUse = usePoint;
        Logger.i("결제 req 데이터 : " + new Gson().toJson(reqPaymentOrderModel));

//        Log.e(" req 데이터 :", new Gson().toJson(reqPaymentOrderModel));

        if (btnMoaPay.isChecked) {
            String memberId = MoaAuthHelper.getInstance().getCurrentMemberID();
            if (memberId.contains("@")) {
                checkArmnt();
            } else {
                loginSigninDialog = new LoginSigninDialog(this, loginListener, signinListener);
                loginSigninDialog.show();
            }

        } else {
            serverController.paymentServerCall(reqPaymentOrderModel);
        }

//        serverController.paymentServerCall(reqPaymentOrderModel);
    }

    private boolean editTextEmptyCheck(EditText editText) {
        return editText.getText().toString().replace(" ", "").equals("");
    }

    @Override
    public void onHttpConnectionSuccess(int type, BaseModel baseModel) {

        if (type == 20) {
            ResGetPintDataModel resGetPintDataModel = (ResGetPintDataModel) baseModel;
            int point = resGetPintDataModel.walletModel == null ? 0 : resGetPintDataModel.walletModel.point;
            maxPoint = point;
            TextView tvKeepPoint = findViewById(R.id.tvMyPoint);
            tvKeepPoint.setText(viewDataInitHelper.getChangeHtmlFormat(getString(R.string.order_payment_my_keep_point, viewDataInitHelper.commaUnitChange(point), viewDataInitHelper.commaUnitChange(point))));
        } else {
            orderId = ((ResPaymentResult) baseModel).orderId;
            resPaymentResult = (ResPaymentResult) baseModel;

//            if (radioBtnImmediatePay.isChecked()) {
//                checkArmnt();
//            } else {
//                eayPayStart();
//            }
            if (radioBtnImmediatePay.isChecked()) {
                isSitePayment = false;
                if (((ResPaymentResult) baseModel).paymentMoney.equals("0")) {
                    paymentSuccess();
                } else {
                    if (btnMoaPay.isChecked) {
                        String memberId = MoaAuthHelper.getInstance().getCurrentMemberID();
                        if (memberId.contains("@")) {
//                            checkArmnt();
                            moaPayStart();
                        } else {
                            loginSigninDialog = new LoginSigninDialog(this, loginListener, signinListener);
                            loginSigninDialog.show();
                        }

                    } else {
                        eayPayStart();
                    }
                }

            } else {
                //todo 현장결제? 신용카드 현금결제.
                isSitePayment = true;
                paymentSuccess();
            }
//            paymentSuccess();
        }

    }

    /**
     * 이지 페이
     */
    private void eayPayStart() {
        String payType = "11";
        if (reqPaymentOrderModel.paymentSeparationCode.equals(CodeTypeManager.PaymentCode.PAY_MTHD_SEPA_CD_CARD.getType())) {
            payType = CodeTypeManager.PaymentCode.EASY_PAY_TYPE_CREDIT_CARD.getType();
        }
        Intent intent = new Intent(this, StartEasyPayActivity.class);
        intent.putExtra(CodeTypeManager.PaymentCode.EASY_PAY_TYPE.toString(), payType);
        intent.putExtra(CodeTypeManager.PaymentCode.PAYMENT_ORDER_ID.toString(), orderId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivityForResult(intent, PAYMENT_CODE);
    }

    /**
     * 모아 페이
     */
    private void moaPayStart() {
        String payType = "11";
        if (reqPaymentOrderModel.paymentSeparationCode.equals(CodeTypeManager.PaymentCode.PAY_MTHD_SEPA_CD_CARD.getType())) {
            payType = CodeTypeManager.PaymentCode.EASY_PAY_TYPE_CREDIT_CARD.getType();
        }
        Intent i = new Intent(this, WalletPayActivity.class);
//        intent.putExtra(CodeTypeManager.PaymentCode.EASY_PAY_TYPE.toString(), payType);
        i.putExtra("productname", resPaymentResult.orderProducts);
        i.putExtra("productmoney", resPaymentResult.paymentMoney);
        i.putExtra("productpoint", resPaymentResult.savePoint);
        i.putExtra("productorderid", resPaymentResult.orderId);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra(CodeTypeManager.PaymentCode.PAYMENT_ORDER_ID.toString(), orderId);
        startActivityForResult(i, MOAPAYMENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("onActivityResult " + requestCode + "  resultCode  " + resultCode);
        if (requestCode == PAYMENT_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    paymentSuccess();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "결제를 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    orderId = "";
                    break;
                case RESULT_FIRST_USER:
                    Toast.makeText(this, "결제를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (requestCode == SELECT_COUPON_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    reqPaymentOrderModel.orderDscntList.clear();
                    OrderDscntList orderDscntList = new OrderDscntList();
                    ResUseCouponModel.UseCouponModel useCouponModel = new Gson().fromJson(data != null ? data.getStringExtra("couponData") : null, ResUseCouponModel.UseCouponModel.class);
                    orderDscntList.dscntApplyCd = useCouponModel.couponCode;
                    couponType = Integer.valueOf(useCouponModel.applyType);
                    couopnValue = useCouponModel.applyValue;
                    couoponMaxValue = useCouponModel.maxSalePrice;
                    reqPaymentOrderModel.orderDscntList.add(orderDscntList);
                    totalPriceInit();
                    break;
            }
        } else if (requestCode == MOAPAYMENT_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    paymentSuccess();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "결제를 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_FIRST_USER:
                    Toast.makeText(this, "결제를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (requestCode == MoaConstants.REQUEST_USER_JOIN || requestCode == MoaConstants.REQUEST_USER_LOGIN) {
            if (resultCode == MoaConstants.RESULT_USER_JOIN_SUCCESS || requestCode == MoaConstants.RESULT_LOGIN_SUCCESS) {
                //TODO JIWON :  2019-07-18 로그인, 회원가입 이후 갱신할 로직 해당부분에 작성하세요.
            }
        }
    }

    private void paymentSuccess() {
        Bundle bundle = new Bundle();
        bundle.putString("orderInfo", new Gson().toJson(resPaymentResult));
        bundle.putBoolean("isSitePayment", isSitePayment);
        PaymentDialog paymentDialog = new PaymentDialog();
        paymentDialog.setArguments(bundle);
        paymentDialog.show(getSupportFragmentManager(), "successDialog");
        if (getIntent().getStringExtra(INTENT_REMOVE_SHOPPING_CART_ITEM) != null) {

            ShoppingCartDetailController shoppingCartDetailController = new ShoppingCartDetailController(getApplicationContext());
            shoppingCartDetailController.shoppingCartItemRemove(App.getInstance().gson.fromJson(getIntent().getStringExtra(INTENT_REMOVE_SHOPPING_CART_ITEM), ReqShoppingCartDetailModel.class));
            shoppingCartDetailController.setRetrofitConnectionResult(new RetrofitConnectionResult() {
                @Override
                public void onRetrofitSuccess(int type, BaseModel baseModel) {
                    new Handler().postDelayed(() -> {
                        paymentDialog.dismiss();
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mainActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(mainActivity);
                        finish();
                    }, 4000);
                }

                @Override
                public void onRetrofitFail(int type, String msg) {
                    new Handler().postDelayed(() -> {
                        paymentDialog.dismiss();
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mainActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(mainActivity);
                        finish();
                    }, 4000);
                }
            });

        } else {
            new Handler().postDelayed(() -> {
                paymentDialog.dismiss();
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mainActivity);
                finish();
            }, 4000);
        }
    }

    @OnClick(R.id.btnCouponSelect)
    void couponSearch() {
        Intent intent = new Intent(this, PaymentCouponActivity.class);
        startActivityForResult(intent, SELECT_COUPON_CODE);
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
                if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    Intent intent = new Intent(OrderPaymentActivity.this, WalletAgreeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, MoaConstants.REQUEST_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT);
                } else {
                    serverController.paymentServerCall(reqPaymentOrderModel);
//                    moaPayStart();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                Logger.e("checkArmnt 서버 통신 애러");
                t.printStackTrace();
            }
        });
    }
}
