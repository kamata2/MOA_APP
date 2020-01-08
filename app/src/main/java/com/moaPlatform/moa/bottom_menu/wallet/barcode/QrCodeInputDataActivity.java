package com.moaPlatform.moa.bottom_menu.wallet.barcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.activity.EventWebViewActivity;
import com.moaPlatform.moa.bottom_menu.wallet.barcode.model.ReqQrMakeOrderId;
import com.moaPlatform.moa.bottom_menu.wallet.barcode.model.ReqQrPayInfo;
import com.moaPlatform.moa.bottom_menu.wallet.barcode.model.ResQrMakeOrderId;
import com.moaPlatform.moa.bottom_menu.wallet.barcode.model.ResQrPayInfo;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.adapter.CardPagerAdapter;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.controller.WalletController;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.MoaPayAgrmnt;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.IdentityActivity;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.PaymentResetPasswordActivity;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.PaymentSearchPasswordActivity;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.WalletAgreeActivity;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.WalletSettingActivity;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.constants.MoaUrlConstants;
import com.moaPlatform.moa.online_payment.order_payment.PaymentCouponActivity;
import com.moaPlatform.moa.online_payment.order_payment.controller.OrderPaymentServerController;
import com.moaPlatform.moa.online_payment.order_payment.model.ReqPaymentOrderModel;
import com.moaPlatform.moa.online_payment.order_payment.model.ResUseCouponModel;
import com.moaPlatform.moa.online_payment.order_payment.model.req.OrderDscntList;
import com.moaPlatform.moa.payment.CardListItem;
import com.moaPlatform.moa.payment.PaymentResultsReceiver;
import com.moaPlatform.moa.payment.ServerSideMoaPayController;
import com.moaPlatform.moa.side_menu.coupon.CouponListActivity;
import com.moaPlatform.moa.util.DeviceUtil;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.auth.UserUseHelper;
import com.moaPlatform.moa.util.custom_view.CommonLoadingView;
import com.moaPlatform.moa.util.custom_view.CommonPasswordInputView;
import com.moaPlatform.moa.util.dialog.yesOrNo.OneBtnDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.interfaces.ViewDataInitHelper;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.App;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCodeInputDataActivity extends AppCompatActivity implements View.OnClickListener, ViewDataInitHelper, PaymentResultsReceiver, RetrofitConnectionResult {

    private ScrollView scrollWalletPayContainer;

    //카드 뷰영역
    private ViewPager cardViewPager;
    private CardPagerAdapter cardPagerAdapter;
    private LinearLayout ivSetting;

    private CheckBox ckCard;
    private RelativeLayout rlPwView;
    private CommonPasswordInputView inputWalletPayPassword;

    private WalletController walletController;
    private ServerSideMoaPayController serverSideMoaPayController;

    private String cardpw;
    //    private String cardToken;
//    private String pMoney, pPoint, pProduct, pOrderID;
//    private String nickName;
    private int removeCardPosition;
//    public void setCardToken(String cardToken) {
//        this.cardToken = cardToken;
//    }

    private String storeIdName;
    private EditText eQrInputMoney;
    private TextView qrTotalMoney, qrTotalPoint;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private String result = "";
    private Integer iSaveRate;
    private String sOrderId;
    private Integer sStoreId;
    private String removeRuslut; //합계금액
    private ViewDataInitHelper viewDataInitHelper = this;
    private ReqPaymentOrderModel reqPaymentOrderModel = new ReqPaymentOrderModel();
    private TextView useMoaPoint;
    private EditText etMyUsePoint;
    private int orderPrice;
    private CommonLoadingView viewLoading;
    private TextView tQrStoreName;
    private int usedPoint;
    private int tempUsePoint = 0;
    private int maxPoint = 0;
    private boolean agaginCheck = false;

    int couponType = -1;
    int couponValue = -1;
    int couponMaxValue = -1;
    private String couponCode = "-1";

    private RelativeLayout rlLeftArrow;
    private RelativeLayout rlRightArrow;

    private List<CardListItem> cardListItems;
    private final int SELECT_COUPON_CODE = 4000;
    private boolean isCardListChanged;

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!TextUtils.isEmpty(charSequence.toString()) && !charSequence.toString().equals(result)) {
                result = decimalFormat.format(Double.parseDouble(charSequence.toString().replaceAll(",", "")));
                eQrInputMoney.setText(result);
                eQrInputMoney.setSelection(result.length());
                String resultPrice = result + " 원";
                qrTotalMoney.setText(resultPrice);
                if (result.length() >= 1) {
                    removeRuslut = result;
                    removeRuslut = removeRuslut.replaceAll(",", "");
                    orderPrice = Integer.parseInt(removeRuslut);
                    totalPrice();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_input_data_activity);
        initLayout();
        initIntentData();
        initAdapter();
        initController();
        getCardList();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 변수 초기화
     */
    private void initLayout() {

        viewLoading = findViewById(R.id.viewLoading);

        rlLeftArrow = findViewById(R.id.rlLeftArrow);
        rlRightArrow = findViewById(R.id.rlRightArrow);

        LinearLayout llWalletMainAddcard = findViewById(R.id.llWalletMainAddcard);
        ImageView ivWalletMainMaskImage = findViewById(R.id.ivWalletMainMaskImage);

        float width = DeviceUtil.getScreenWidth(this);
        float height = (float) (width / 2.64);
        ivWalletMainMaskImage.getLayoutParams().width = (int) width;
        ivWalletMainMaskImage.getLayoutParams().height = (int) height;
        ivWalletMainMaskImage.requestLayout();
        ivSetting = findViewById(R.id.walletsetting);

        RelativeLayout rlWalletMainTopInfoContainer = findViewById(R.id.rlWalletMainTopInfoContainer);
        int walletBackgroundWidth = DeviceUtil.getScreenWidth(this);
        //배경 이미지 가로세로 비율
        double aspectAnim = 1.2676;
        double walletBackgroundHeight = walletBackgroundWidth / aspectAnim;
        rlWalletMainTopInfoContainer.getLayoutParams().width = walletBackgroundWidth;
        rlWalletMainTopInfoContainer.getLayoutParams().height = (int) walletBackgroundHeight;

//        TextView tvMoney = findViewById(R.id.paytotalmoney);
//        TextView tvPoint = findViewById(R.id.paypoint);
//        TextView tvProduct = findViewById(R.id.paycontentname);
        ckCard = findViewById(R.id.paycheckqrs);

        scrollWalletPayContainer = findViewById(R.id.scrollWalletPayContainer);
        rlPwView = findViewById(R.id.passwordview);
        rlPwView.setVisibility(View.GONE);

        inputWalletPayPassword = findViewById(R.id.inputWalletPayPassword);
        inputWalletPayPassword.isShowTitle(false);

        Button btnPayOk = findViewById(R.id.qrpayok);

        TextView tvPwSearch = findViewById(R.id.password_search);
        TextView tvPwChange = findViewById(R.id.password_change);
        RelativeLayout rlWalletMainQrBack = findViewById(R.id.rlWalletMainQrBack);

        UserUseHelper userUseHelper = new UserUseHelper(this);

        rlWalletMainQrBack.setOnClickListener(this);
        tvPwSearch.setOnClickListener(this);
        tvPwChange.setOnClickListener(this);
        btnPayOk.setOnClickListener(this);
        llWalletMainAddcard.setOnClickListener(this);
        ivSetting.setOnClickListener(this);
        rlLeftArrow.setOnClickListener(this);
        rlRightArrow.setOnClickListener(this);
        // 추후 수정
        OrderPaymentServerController orderPaymentServerController = new OrderPaymentServerController(this);
        orderPaymentServerController.getPoint();
        orderPaymentServerController.setHttpConnectionResult((type, baseModel) -> {
//            ResGetPintDataModel resGetPintDataModel = (ResGetPintDataModel) baseModel;
//                pointText.setText(viewDataInitHelper.commaUnitChange(resGetPintDataModel.walletModel.point) + "P");
//                pointExChange.setText("("+viewDataInitHelper.commaUnitChange(resGetPintDataModel.walletModel.point)+")");
        });

        qrTotalMoney = findViewById(R.id.qrtotalmoney);
        qrTotalPoint = findViewById(R.id.qrtotalpoint);

        eQrInputMoney = findViewById(R.id.qrinputmoney);
        eQrInputMoney.addTextChangedListener(watcher);

        tQrStoreName = findViewById(R.id.qrstorename);

        View view = getWindow().getDecorView();
        useMoaPoint = findViewById(R.id.useMoaPoint);
        if (userUseHelper.isUserLogin()) {
            viewDataInitHelper.viewVisibility(view, R.id.saleCoupon, "Y");
            viewDataInitHelper.viewVisibility(view, R.id.saleCouponNeedLogin, null);
//            viewDataInitHelper.viewVisibility(view, R.id.useMoaPoint, "Y");
            useMoaPoint.setVisibility(View.VISIBLE);
            viewDataInitHelper.viewVisibility(view, R.id.useMoaPointNeedLogin, null);
            viewDataInitHelper.viewVisibility(view, R.id.btnAllPoint, "Y");
        } else {
            viewDataInitHelper.viewVisibility(view, R.id.saleCoupon, null);
            viewDataInitHelper.viewVisibility(view, R.id.saleCouponNeedLogin, "Y");
//            viewDataInitHelper.viewVisibility(view, R.id.useMoaPoint, null);
            useMoaPoint.setVisibility(View.GONE);
            viewDataInitHelper.viewVisibility(view, R.id.useMoaPointNeedLogin, "Y");
            viewDataInitHelper.viewVisibility(view, R.id.btnAllPoint, "N");
        }

        TextView useMoaCoupon = findViewById(R.id.btnCouponSelect);
        useMoaCoupon.setOnClickListener(this);


//        TextView cbMoaPoint = findViewById(R.id.moaPointTitle);
//        if (userUseHelper.isUserLogin())
//            cbMoaPoint.setClickable(true);
//        else
//            cbMoaPoint.setClickable(false);
//
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
        Button btnAllUsePoint = findViewById(R.id.btnAllPoint);
        btnAllUsePoint.setOnClickListener(v -> {
            if (eQrInputMoney.getText().toString().trim() != null && !eQrInputMoney.getText().toString().trim().equals("")) {
                if (orderPrice > maxPoint) {
                    tempUsePoint = maxPoint;
                } else {
                    tempUsePoint = orderPrice;
                }
                etMyUsePoint.setText(String.valueOf(tempUsePoint));
                totalPrice();
//                int usePoint = etMyUsePoint.getText().toString().equals("") ? 0 : Integer.valueOf(etMyUsePoint.getText().toString());
//                String usePt = viewDataInitHelper.commaUnitChange(usePoint);
//                useMoaPoint.setText(getString(R.string.order_payment_use_point, usePt, usePt));
//                int tempTotalPrice = 0;
//                if (orderPrice - usePoint < 0) {
//                    tempTotalPrice = 0;
//                } else {
//                    usedPoint = usePoint;
//                    tempTotalPrice = orderPrice - usePoint;
//                }
//                viewDataInitHelper.textViewInitConvertPrice(view, R.id.qrtotalmoney, R.string.store_product_total_money, tempTotalPrice);
            } else {
                etMyUsePoint.setText("0");
                Toast.makeText(this, "결제 금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });


        Button btMyUsePoint = findViewById(R.id.btMyUsePoint);
        btMyUsePoint.setOnClickListener(v -> {
            if (eQrInputMoney.getText().toString().trim() != null && !eQrInputMoney.getText().toString().trim().equals("")) {

                if (etMyUsePoint.getText().toString().trim().equals(""))
                    tempUsePoint = 0;
                else if (Integer.valueOf(etMyUsePoint.getText().toString().trim()) > maxPoint) {
                    tempUsePoint = maxPoint;
                } else {
                    tempUsePoint = Integer.valueOf(etMyUsePoint.getText().toString().trim());
                }

                if (tempUsePoint > orderPrice)
                    tempUsePoint = orderPrice;

                etMyUsePoint.setText(String.valueOf(tempUsePoint));
                totalPrice();
//                int usePoint = etMyUsePoint.getText().toString().equals("") ? 0 : Integer.valueOf(etMyUsePoint.getText().toString());
//                String usePt = viewDataInitHelper.commaUnitChange(usePoint);
//                useMoaPoint.setText(getString(R.string.order_payment_use_point, usePt, usePt));
//                int tempTotalPrice = 0;
//                if (orderPrice - usePoint < 0) {
//                    tempTotalPrice = 0;
//                } else {
//                    usedPoint = usePoint;
//                    tempTotalPrice = orderPrice - usePoint;
//                }
//                viewDataInitHelper.textViewInitConvertPrice(view, R.id.qrtotalmoney, R.string.store_product_total_money, tempTotalPrice);
            } else {
                etMyUsePoint.setText("0");
                Toast.makeText(this, "결제 금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        View deliveryOrderDangerAllSee = findViewById(R.id.deliveryOrderDangerAllSee);
        deliveryOrderDangerAllSee.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), EventWebViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, BuildConfig.REST_API_BASE_URL + MoaUrlConstants.AGEE_TO_PROVIDE_PRESONAL_INFOMATION_TO_THIRD_PARTIES);
            intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_TRANSPARENT_VALUE);
            startActivity(intent);
        });

    }

    private void initIntentData() {
        storeIdName = getIntent().getStringExtra("storename");
        getQrCodePayInfor();
    }

    private void initAdapter() {
        cardViewPager = findViewById(R.id.cardViewPager);
        cardPagerAdapter = new CardPagerAdapter(getSupportFragmentManager());
        cardViewPager.setAdapter(cardPagerAdapter);

        cardViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                moveCardViewPager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        cardPagerAdapter.setListItemClickListener((codeType, position, object) -> addCard());

        //카드 삭제하기
        cardPagerAdapter.setCardDeleteAdapterListener((cardToken, position) -> {
            if (cardToken != null) {

                YesOrNoDialog cardDeleteYesOrNoDialog = new YesOrNoDialog();
                cardDeleteYesOrNoDialog.dialogContent(getString(R.string.yes_or_no_dialog_content_delete_card));
                cardDeleteYesOrNoDialog.show(getSupportFragmentManager(), "cardDeleteYesOrNoDialog");
                cardDeleteYesOrNoDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
                    @Override
                    public void onDialogNo() {
                        cardDeleteYesOrNoDialog.dismiss();
                    }

                    @Override
                    public void onDialogYes() {
                        removeCardPosition = position;
                        if (serverSideMoaPayController != null) {
                            serverSideMoaPayController.deleteOneCard(cardToken);
                        }
                        cardDeleteYesOrNoDialog.dismiss();
                    }
                });
            }
        });
    }

    private void initController() {
        walletController = new WalletController(this);
        serverSideMoaPayController = new ServerSideMoaPayController(this);
        serverSideMoaPayController.init(this);
        serverSideMoaPayController.setRetrofitConnectionResult(this);
    }

    /**
     * 카드 뷰페이저 이동시에 따른 UI 처리
     *
     * @param position
     */
    private void moveCardViewPager(int position) {

        Logger.d("moveCardViewPager position " + String.valueOf(position));

        //스크롤에 따른 좌우 화살표 노출 처리
        if (cardListItems != null) {
            if (cardListItems.size() > 1) {
                //맨 왼쪽인 경우
                if (position == 0) {
                    isShowLeftRightCardMoveArrow(false, true);
                } else if (position == cardListItems.size() - 1) {
                    //맨 오른쪽(끝)인 경우
                    isShowLeftRightCardMoveArrow(true, false);
                } else {
                    isShowLeftRightCardMoveArrow(true, true);
                }
            } else {
                isShowLeftRightCardMoveArrow(false, false);
            }
        } else {
            isShowLeftRightCardMoveArrow(false, false);
        }
    }

    /**
     * 카드 좌우 화살표 노출 처리
     * @param isShowLeft
     * @param isShowRight
     */
    private void isShowLeftRightCardMoveArrow(boolean isShowLeft, boolean isShowRight) {
        if (rlLeftArrow != null) {
            if (isShowLeft) {
                rlLeftArrow.setVisibility(View.VISIBLE);
            } else {
                rlLeftArrow.setVisibility(View.GONE);
            }
        }
        if (rlRightArrow != null) {
            if (isShowRight) {
                rlRightArrow.setVisibility(View.VISIBLE);
            } else {
                rlRightArrow.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 카드 리스트틀 가져온다
     */
    private void getCardList() {
        if (serverSideMoaPayController != null) {
            serverSideMoaPayController.getCardList();
        }
    }

    /**
     * 포인트와 코인을 받아옴
     */
    private void getPointCoin() {
        if (walletController != null) {
            walletController.getPointCoin();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llWalletMainAddcard:
                addCard();
                break;
            case R.id.btnCoupon:
                startActivity(new Intent(QrCodeInputDataActivity.this, CouponListActivity.class));
                break;
            case R.id.walletsetting:
                String memberIds = MoaAuthHelper.getInstance().getCurrentMemberID();
                if (memberIds.contains("@")) {
                    Intent walletSettingIntent = new Intent(QrCodeInputDataActivity.this, WalletSettingActivity.class);
                    walletSettingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(walletSettingIntent, MoaConstants.REQUEST_WALLET_SETTING_ACTIVITY);
                } else {
                    Toast.makeText(this, getString(R.string.common_toast_msg_use_available_after_login), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.qrpayok:

                if (scrollWalletPayContainer.getVisibility() == View.VISIBLE) {
                    //빈게 아님 true!
                    Boolean realMoney = false;
                    realMoney = !eQrInputMoney.getText().toString().trim().equals("");

                    //값이 0 true!
                    Boolean totalMoney = false;
                    totalMoney = qrTotalMoney.getText().toString().trim().equals("0원");

                    //카드 없다.
                    Boolean haveCard = false;
//                    Log.e("======",cardPagerAdapter.getCardDataList().size()+"  !!!!!");
//                    haveCard = cardPagerAdapter.getCardDataList().size() > 1;
                    haveCard = cardPagerAdapter.getCardDataList().get(0).getCARDTOKEN() != null;

                    if (ckCard.isChecked()) {
                        if (haveCard == true) {

                            if (agaginCheck) {
                                OneBtnDialog oneBtnDialog = new OneBtnDialog();
                                oneBtnDialog.dialogContent("포인트 사용금액이 결제금액을 초과하였습니다.");
                                oneBtnDialog.show(getSupportFragmentManager(), "pointOverDialog");
                                oneBtnDialog.oneBtnDialogFragmentListener(() -> oneBtnDialog.dismiss());
                                return;
                            }

                            if (realMoney == true) {
                                scrollWalletPayContainer.setVisibility(View.GONE);
                                rlPwView.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(this, "결제 금액을 입력하세요.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (realMoney == true && totalMoney == true) {
                                scrollWalletPayContainer.setVisibility(View.GONE);
                                rlPwView.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(this, "카드를 등록 하세요", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(this, "결제 동의해주세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    checkPassWord();
                }
                break;

            case R.id.password_search:
                startActivity(new Intent(QrCodeInputDataActivity.this, PaymentSearchPasswordActivity.class));
                break;
            case R.id.password_change:
                startActivity(new Intent(QrCodeInputDataActivity.this, PaymentResetPasswordActivity.class));
                break;
            case R.id.btnCouponSelect:
                Intent intent = new Intent(this, PaymentCouponActivity.class);
                startActivityForResult(intent, SELECT_COUPON_CODE);
                break;

            case R.id.rlLeftArrow:
                if (cardViewPager != null) {
                    int currentPosition = cardViewPager.getCurrentItem();
                    if (currentPosition > 0) {
                        cardViewPager.setCurrentItem(currentPosition - 1);
                    }
                }
                break;

            case R.id.rlRightArrow:
                if (cardViewPager != null) {
                    int currentPosition = cardViewPager.getCurrentItem();
                    if (currentPosition + 1 <= cardPagerAdapter.getCount()) {
                        cardViewPager.setCurrentItem(currentPosition + 1);
                    }
                }
                break;
            case R.id.rlWalletMainQrBack:
                if(isCardListChanged){
                    setResult(MoaConstants.RESILT_WALLET_QR_CODE_INPUT_DATA_ACTIVITY_CARD_LIST_CHANGED);
                }
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d("requestCode " + requestCode + " resultCode " + resultCode);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("pwresult");
                String resultid = data.getStringExtra("idresult");
                String resulttoken = data.getStringExtra("toeknresult");
//                Log.e("onActivityResult", result + " / " + resultid + " / " + resulttoken);
                Map<String, String> payData = new HashMap<>();
                payData.put("payPw", result);
                payData.put("orderID", resultid);
                payData.put("token", resulttoken);
                serverSideMoaPayController.init(this);
                serverSideMoaPayController.payCard(payData);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        } else if (requestCode == SELECT_COUPON_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    reqPaymentOrderModel.orderDscntList.clear();
                    OrderDscntList orderDscntList = new OrderDscntList();
                    ResUseCouponModel.UseCouponModel useCouponModel = new Gson().fromJson(data != null ? data.getStringExtra("couponData") : null, ResUseCouponModel.UseCouponModel.class);
                    orderDscntList.dscntApplyCd = useCouponModel.couponCode;
                    couponCode = useCouponModel.couponCode;
                    reqPaymentOrderModel.orderDscntList.add(orderDscntList);
                    couponType = Integer.valueOf(useCouponModel.applyType);
                    couponValue = useCouponModel.applyValue;
                    couponMaxValue = useCouponModel.maxSalePrice;
                    reqPaymentOrderModel.orderDscntList.add(orderDscntList);
                    totalPrice();
                    break;
            }
        } else if (requestCode == MoaConstants.REQUEST_IDENTITY_ACTIVITY || requestCode == MoaConstants.REQUEST_WALLET_SETTING_ACTIVITY) {
            if (resultCode == MoaConstants.RESULT_CARDREGISTER_SUCCESS) {
                isCardListChanged = true;
                if (serverSideMoaPayController != null) {
                    serverSideMoaPayController.getCardList();
                }
            }
        } else if (requestCode == MoaConstants.REQUEST_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT) {
            //약관동의->인증->카드등록 완료가 정상적으로 되었을시에만 갱신처리
            if (resultCode == MoaConstants.RESULT_CARDREGISTER_SUCCESS) {
                isCardListChanged = true;
                getCardList();
            }
        }

    }

    private void checkPassWord() {
        String pw = inputWalletPayPassword.getInputText();

        if (pw.equals("") & pw.length() < 6) {
            Toast.makeText(QrCodeInputDataActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        cardpw = pw;
        scrollWalletPayContainer.setVisibility(View.VISIBLE);
        rlPwView.setVisibility(View.GONE);

        aniProgressStart();
        setOrderToQrCode();

    }

    private void setOrderToQrCode() {
        ReqQrMakeOrderId reqQrMakeOrderId = new ReqQrMakeOrderId();

        reqQrMakeOrderId.storId = sStoreId;
        reqQrMakeOrderId.sumAmt = Integer.parseInt(removeRuslut);
        reqQrMakeOrderId.pointUseAmt = usedPoint;

        if (couponType != -1) {
            OrderDscntList orderDscntList = new OrderDscntList();
            orderDscntList.dscntApplyCd = couponCode;
            reqQrMakeOrderId.orderDscntList.add(orderDscntList);
        }

        RetrofitClient.getInstance().getMoaService().setQrMakeOrderId(reqQrMakeOrderId).enqueue(new Callback<ResQrMakeOrderId>() {
            @Override
            public void onResponse(Call<ResQrMakeOrderId> call, Response<ResQrMakeOrderId> response) {
                ResQrMakeOrderId resQrMakeOrderId = response.body();
                if (resQrMakeOrderId == null)
                    return;
                if (resQrMakeOrderId.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    sOrderId = resQrMakeOrderId.orderId;
                    if (resQrMakeOrderId.payAmt == 0) {
                        onSuccessPayment();
                    } else {
                        sendQrCardPay();
                    }
                } else {
                    Toast.makeText(QrCodeInputDataActivity.this, getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResQrMakeOrderId> call, Throwable t) {
                Toast.makeText(QrCodeInputDataActivity.this, getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendQrCardPay() {
        Map<String, String> payData = new HashMap<>();
        payData.put("payPw", cardpw);
        payData.put("orderID", sOrderId);
        payData.put("token", cardPagerAdapter.getCardDataList().get(cardViewPager.getCurrentItem()).getCARDTOKEN());
//        List<String> payData = Arrays.asList(cardpw, pOrderID, cardPagerAdapter.getCardDataList().get(cardViewPagerPosition).getCARDTOKEN());
        serverSideMoaPayController.init(this);
        serverSideMoaPayController.payCard(payData);
    }

    /**
     * 카드 등록하기
     */
    private void addCard() {
        String memberId = MoaAuthHelper.getInstance().getCurrentMemberID();
        //로그인이면..
        if (memberId.contains("@")) {
            MoaPayAgrmnt moaPayAgrmnt = new MoaPayAgrmnt();
            RetrofitClient.getInstance().getMoaService().getMoaPayAgrmnt(moaPayAgrmnt).enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                    CommonModel commonModel = response.body();
                    if (commonModel == null)
                        return;
                    if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                        addCard();
                        return;
                    }
                    if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                        //약관 동의 화면으로 이동
                        Intent intent = new Intent(QrCodeInputDataActivity.this, WalletAgreeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(intent, MoaConstants.REQUEST_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT);
                    } else {
                        Intent intent = new Intent(QrCodeInputDataActivity.this, IdentityActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(MoaConstants.EXTRA_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT, true);
                        startActivityForResult(intent, MoaConstants.REQUEST_IDENTITY_ACTIVITY);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {

                }
            });

        } else {
            //로그인 사용자가 아닐경우 처리
            //해당 화면은 로그인한 사용자만 진입 가능하기에 별도의 로그인 다이얼로그 처리를 하지 않는다.
        }
    }

    @Override
    public void onLoadCompleteCardList(List<CardListItem> cardListItems) {
        this.cardListItems = cardListItems;
        if (cardPagerAdapter != null) {
            cardPagerAdapter.clearCardList();
        }
        if (cardListItems != null) {
            Boolean haveCard = cardListItems.size() == 0;

            if (haveCard == true) {
                ivSetting.setVisibility(View.GONE);
            } else {
                ivSetting.setVisibility(View.VISIBLE);
            }

            if (cardListItems.size() == 0) {
                //카드가 없을시 카드 추가하기 이미지 처리를 위함
                List<CardListItem> cardList = new ArrayList<>();
                CardListItem cardItem = new CardListItem();
                cardList.add(cardItem);
                cardPagerAdapter.setCardDataList(cardList);
                isShowLeftRightCardMoveArrow(false, false);
            } else {
                Collections.reverse(cardListItems);
                cardPagerAdapter.setCardDataList(cardListItems);
                cardViewPager.setCurrentItem(0);
                moveCardViewPager(cardViewPager.getCurrentItem());
            }
        }
    }

    @Override
    public void onReadyJsonData(String jsonData) {
    }

    @Override
    public void onSuccessPayment() {
        aniProgressStop();
        Toast.makeText(this, "결제 완료 되었습니다.", Toast.LENGTH_LONG).show();
        if(isCardListChanged){
            setResult(MoaConstants.RESILT_WALLET_QR_CODE_INPUT_DATA_ACTIVITY_CARD_LIST_CHANGED);
        }
        finish();
    }

    @Override
    public void onFailPayment(String msg) {
        aniProgressStop();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    public void onForwardInitPswNonce(String nonce) {
    }

    @Override
    public void onNotMatchedPw() {
        aniProgressStop();
        Toast.makeText(this, "결제 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
//        Intent returnIntent = new Intent();
//        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onRetrofitSuccess(int type, BaseModel baseModel) {
        isCardListChanged = true;
        if (cardPagerAdapter != null) {
            cardPagerAdapter.removePage(removeCardPosition);

            if (cardPagerAdapter.getCount() == 0) {
                List<CardListItem> cardList = new ArrayList<>();
                CardListItem cardItem = new CardListItem();
                cardList.add(cardItem);
                cardPagerAdapter.setCardDataList(cardList);
            }
            cardPagerAdapter.notifyDataSetChanged();
            Toast.makeText(this, getString(R.string.msg_card_delete_success), Toast.LENGTH_SHORT).show();
            moveCardViewPager(cardViewPager.getCurrentItem());

            Boolean haveCard = cardPagerAdapter.getCount() == 0;
            if (haveCard == true) {
                ivSetting.setVisibility(View.GONE);
            } else {
                ivSetting.setVisibility(View.VISIBLE);
            }

        }
    }

    private void getQrCodePayInfor() {
        ReqQrPayInfo reqQrPayInfo = new ReqQrPayInfo();
        try {
            reqQrPayInfo.storId = Integer.parseInt(storeIdName);//real storeid 받아오기
//            reqQrPayInfo.storId = 10;//storeid 테스트 코드

            sStoreId = reqQrPayInfo.storId;
        } catch (Exception e) {
            Toast.makeText(QrCodeInputDataActivity.this, "존재 하지 않는 가맹점입니다", Toast.LENGTH_SHORT).show();
            finish();
        }
//        Toast.makeText(this, reqQrPayInfo.storId + "", Toast.LENGTH_SHORT).show();

        RetrofitClient.getInstance().getMoaService().getQrPayInfo(reqQrPayInfo).enqueue(new Callback<ResQrPayInfo>() {
            @Override
            public void onResponse(Call<ResQrPayInfo> call, Response<ResQrPayInfo> response) {
                ResQrPayInfo resQrPayInfo = response.body();
                if (resQrPayInfo == null) {
                    return;
                }
                if (resQrPayInfo.storExists == true) {
                    settingData(resQrPayInfo);
                } else {
                    Toast.makeText(QrCodeInputDataActivity.this, "존재 하지 않는 가맹점입니다", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResQrPayInfo> call, Throwable t) {
            }
        });

    }

    private void totalPrice() {

        int totalPrice = orderPrice;

        View view = getWindow().getDecorView();
        int usePoint = 0;

        if (tempUsePoint != 0)
            usePoint = tempUsePoint;

        String usePt = viewDataInitHelper.commaUnitChange(usePoint);
        useMoaPoint.setText(getString(R.string.order_payment_use_point, usePt, usePt));

        int couponSale = 0;
        if (couponType != -1) {
            if (couponType == 2) {
                couponSale = couponValue > couponMaxValue ? couponMaxValue : couponValue;
                totalPrice -= couponSale;
            } else {
                int couoponSaleTemp = (totalPrice * couponValue / 100);
                couponSale = couoponSaleTemp > couponMaxValue ? couponMaxValue : couoponSaleTemp;
                totalPrice -= couponSale;
            }
        }

        int tempTotalPrice = 0;
        if (totalPrice - usePoint < 0) {
            tempTotalPrice = 0;
            agaginCheck = true;
        } else {
            usedPoint = usePoint;
            tempTotalPrice = totalPrice - usePoint;
            agaginCheck = false;
        }

        String couponText = "-" + StringUtil.convertCommaPrice(couponSale) + "원";
        TextView saleCoupon = findViewById(R.id.saleCoupon);
        saleCoupon.setText(couponText);
        viewDataInitHelper.textViewInitConvertPrice(view, R.id.qrtotalmoney, R.string.store_product_total_money, tempTotalPrice);

        int savePoint = tempTotalPrice * iSaveRate / 100;
        String sSavePoint = savePoint + "";
        sSavePoint = decimalFormat.format(Double.parseDouble(sSavePoint.replaceAll(",", "")));
        qrTotalPoint.setText(sSavePoint);

    }

    private void settingData(ResQrPayInfo resQrPayInfo) {
        App.getInstance().SUB_MENU_CODE = resQrPayInfo.subMenuCode;
        storeIdName = resQrPayInfo.storNm;
        iSaveRate = resQrPayInfo.saveRate;
        Integer iSavePoint = resQrPayInfo.pointBal;

        maxPoint = iSavePoint;
        TextView tvKeepPoint = findViewById(R.id.tvMyPoint);
        tvKeepPoint.setText(viewDataInitHelper.getChangeHtmlFormat(getString(R.string.order_payment_my_keep_point, viewDataInitHelper.commaUnitChange(maxPoint), viewDataInitHelper.commaUnitChange(maxPoint))));
        tQrStoreName.setText(storeIdName);
    }

    @Override
    public void onRetrofitFail(int type, String msg) {
    }

    private void aniProgressStart() {
        viewLoading.loadingAnimationPlay(this);
    }

    private void aniProgressStop() {
        viewLoading.animationStop(this);
    }

    @Override
    public void onBackPressed() {
        Logger.d("isCardListChanged >>>>> " + isCardListChanged);
        if(isCardListChanged){
            setResult(MoaConstants.RESILT_WALLET_QR_CODE_INPUT_DATA_ACTIVITY_CARD_LIST_CHANGED);
        }
        finish();
    }
}
