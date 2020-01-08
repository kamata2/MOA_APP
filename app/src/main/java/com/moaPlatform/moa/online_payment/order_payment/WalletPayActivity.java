package com.moaPlatform.moa.online_payment.order_payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.adapter.CardPagerAdapter;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.controller.WalletController;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.MoaPayAgrmnt;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.IdentityActivity;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.PaymentResetPasswordActivity;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.PaymentSearchPasswordActivity;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.WalletAgreeActivity;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.WalletSettingActivity;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.online_payment.order_payment.controller.OrderPaymentServerController;
import com.moaPlatform.moa.online_payment.order_payment.model.ResGetPintDataModel;
import com.moaPlatform.moa.payment.CardListItem;
import com.moaPlatform.moa.payment.PaymentResultsReceiver;
import com.moaPlatform.moa.payment.ServerSideMoaPayController;
import com.moaPlatform.moa.side_menu.coupon.CouponListActivity;
import com.moaPlatform.moa.util.DeviceUtil;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.custom_view.CommonLoadingView;
import com.moaPlatform.moa.util.custom_view.CommonPasswordInputView;
import com.moaPlatform.moa.util.dialog.yesOrNo.OneBtnDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.HttpConnectionResult;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.interfaces.ViewDataInitHelper;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.moaPlatform.moa.payment.CardRegisterActivity.EXTRA_CARD_REGISTER_NICK_NAME;

/**
 * 제휴점 상세 > 결제 지갑 화면
 * 상단 뷰에는 카드 리스트 UI를 가진다.
 * 하단 뷰에는 password 입력 UI를 가진다.
 */
public class WalletPayActivity extends AppCompatActivity implements View.OnClickListener, ViewDataInitHelper, PaymentResultsReceiver, RetrofitConnectionResult {

    private ScrollView scrollWalletPayContainer;

    //카드 뷰영역
    private ViewPager cardViewPager;
    private ImageView ivWalletMainMaskImage;
    private CommonLoadingView commonLoadingViewWalletPay;
    private RelativeLayout rlLeftArrow;
    private RelativeLayout rlRightArrow;
    private CardPagerAdapter cardPagerAdapter;
    private RelativeLayout rlSetting;
    private LinearLayout llWalletMainAddcard;
    private RelativeLayout rlWalletMainTopInfoContainer;
    private final double aspectAnim = 1.2676;       //배경 이미지 가로세로 비율

    private TextView tvMoney, tvPoint, tvProduct;
    private CheckBox ckCard;
    private RelativeLayout rlPwView;
    private TextView tvPwSearch, tvPwChange;
    private CommonPasswordInputView inputWalletPayPassword;
    private Button btnPayOk;
    private RelativeLayout rlWalletMainBackBtn;

    private WalletController walletController;
    private ServerSideMoaPayController serverSideMoaPayController;

    private String cardpw;
    private String pMoney, pPoint, pProduct, pOrderID;
    private String nickName;
    private int removeCardPosition;
    private int cardViewPagerPosition;      //뷰페이저 position

    private List<CardListItem> cardListItems;
    private TimerTask requestCardListTimerTask;
    private int requestCardListCnt;             //카드 리스트 반복요청 갯수
    private boolean isAddCardStatus;            //카드 추가하기 상태 Flag
    Boolean haveNoCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_pay_activity);

        initLayout();
        initAdapter();
        showIntent();
        initData();
        initController();

        getCardList();
        getPointCoin();

    }

    @Override
    public void onBackPressed() {
        onFailPayment("back");
        serverSideMoaPayController.initOrderIfCanceled(pOrderID);
        super.onBackPressed();
    }

    /**
     * 변수 초기화
     */
    private void initLayout() {

        commonLoadingViewWalletPay = findViewById(R.id.commonLoadingViewWalletPay);
        commonLoadingViewWalletPay.animationStop(this);

        rlLeftArrow = findViewById(R.id.rlLeftArrow);
        rlRightArrow = findViewById(R.id.rlRightArrow);

        llWalletMainAddcard = findViewById(R.id.llWalletMainAddcard);
        ivWalletMainMaskImage = findViewById(R.id.ivWalletMainMaskImage);

        float width = DeviceUtil.getScreenWidth(this);
        float height = (float) (width / 2.64);
        ivWalletMainMaskImage.getLayoutParams().width = (int) width;
        ivWalletMainMaskImage.getLayoutParams().height = (int) height;
        ivWalletMainMaskImage.requestLayout();
        rlSetting = findViewById(R.id.rlWalletSetting);

        rlWalletMainTopInfoContainer = findViewById(R.id.rlWalletMainTopInfoContainer);
        int walletBackgroundWidth = DeviceUtil.getScreenWidth(this);
        double walletBackgroundHeight = walletBackgroundWidth / aspectAnim;
        rlWalletMainTopInfoContainer.getLayoutParams().width = walletBackgroundWidth;
        rlWalletMainTopInfoContainer.getLayoutParams().height = (int) walletBackgroundHeight;
        rlWalletMainBackBtn = findViewById(R.id.rlWalletMainBackBtn);

        tvMoney = findViewById(R.id.paytotalmoney);
        tvPoint = findViewById(R.id.paypoint);
        tvProduct = findViewById(R.id.paycontentname);
        ckCard = findViewById(R.id.paycheckfnt);

        scrollWalletPayContainer = findViewById(R.id.scrollWalletPayContainer);
        rlPwView = findViewById(R.id.passwordview);
        rlPwView.setVisibility(View.GONE);

        inputWalletPayPassword = findViewById(R.id.inputWalletPayPassword);
        inputWalletPayPassword.isShowTitle(false);

        btnPayOk = findViewById(R.id.orderpayok);

        tvPwSearch = findViewById(R.id.password_search);
        tvPwChange = findViewById(R.id.password_change);

        rlWalletMainBackBtn.setOnClickListener(this);
        tvPwSearch.setOnClickListener(this);
        tvPwChange.setOnClickListener(this);
        btnPayOk.setOnClickListener(this);
        llWalletMainAddcard.setOnClickListener(this);
        rlSetting.setOnClickListener(this);

        // 추후 수정
        OrderPaymentServerController orderPaymentServerController = new OrderPaymentServerController(this);
        orderPaymentServerController.getPoint();
        orderPaymentServerController.setHttpConnectionResult(new HttpConnectionResult() {
            @Override
            public void onHttpConnectionSuccess(int type, BaseModel baseModel) {
                ResGetPintDataModel resGetPintDataModel = (ResGetPintDataModel) baseModel;
//                pointText.setText(viewDataInitHelper.commaUnitChange(resGetPintDataModel.walletModel.point) + "P");
//                pointExChange.setText("("+viewDataInitHelper.commaUnitChange(resGetPintDataModel.walletModel.point)+")");
            }
        });

        rlLeftArrow.setOnClickListener(v -> {
            if (cardViewPager != null) {
                int currentPosition = cardViewPager.getCurrentItem();
                if (currentPosition > 0) {
                    cardViewPager.setCurrentItem(currentPosition - 1);
                }
            }
        });

        rlRightArrow.setOnClickListener(v -> {
            if (cardViewPager != null) {
                int currentPosition = cardViewPager.getCurrentItem();
                if (currentPosition + 1 <= cardPagerAdapter.getCount()) {
                    cardViewPager.setCurrentItem(currentPosition + 1);
                }
            }
        });
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
                //좌우 화살표 노출 처리
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

    private void initData() {
        tvMoney.setText(StringUtil.convertCommaPrice(pMoney) + "원");
        tvPoint.setText("주문 혜택 " + StringUtil.convertCommaPrice(pPoint) + "P 적립 예정");
        tvProduct.setText(pProduct);
    }

    private void showIntent() {
        pProduct = getIntent().getStringExtra("productname");
        pMoney = getIntent().getStringExtra("productmoney");
        pPoint = getIntent().getStringExtra("productpoint");
        pOrderID = getIntent().getStringExtra("productorderid");
        nickName = getIntent().getStringExtra("productorderid");

        try {
            nickName = getIntent().getStringExtra(EXTRA_CARD_REGISTER_NICK_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initController() {

        walletController = new WalletController(this);

        serverSideMoaPayController = new ServerSideMoaPayController(this);
        serverSideMoaPayController.init(this);
        serverSideMoaPayController.setRetrofitConnectionResult(this);
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
                startActivity(new Intent(WalletPayActivity.this, CouponListActivity.class));
                break;
            case R.id.rlWalletSetting:
                String memberIds = MoaAuthHelper.getInstance().getCurrentMemberID();
                if (memberIds.contains("@")) {
                    Intent walletSettingIntent = new Intent(WalletPayActivity.this, WalletSettingActivity.class);
                    walletSettingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(walletSettingIntent, MoaConstants.REQUEST_WALLET_SETTING_ACTIVITY);
                } else {
                    Toast.makeText(this, getString(R.string.common_toast_msg_use_available_after_login), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.orderpayok:
                haveNoCard = cardListItems.size() == 0;

                if (haveNoCard == true) {
                    OneBtnDialog addCardDlg = new OneBtnDialog();
                    addCardDlg.dialogContent("GOEAT PAY를 이용하기 위해서는 카드를 등록해야 합니다.");
                    addCardDlg.show(getSupportFragmentManager(), "joinResultOneBtnDialog");
                    addCardDlg.oneBtnDialogFragmentListener(() -> {
                        addCardDlg.dismiss();
                    });

                } else {
                    if (scrollWalletPayContainer.getVisibility() == View.VISIBLE) {
                        if (ckCard.isChecked()) {
                            scrollWalletPayContainer.setVisibility(View.GONE);
                            rlPwView.setVisibility(View.VISIBLE);
                            Logger.d("결제하기 화면이 보여지는 상태.....");
                            inputWalletPayPassword.showKeyboard();
//                    Intent i = new Intent(this, OrderPaySetPwActivity.class);
//                    i.putExtra("tempid", pOrderID);
//                    i.putExtra("temptoken", cardToken);
//                    startActivityForResult(i, 100);
                        } else {
                            Toast.makeText(this, "결제 동의해주세요", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        checkPassWord();
                    }
                }

                break;

            case R.id.password_search:
                startActivity(new Intent(WalletPayActivity.this, PaymentSearchPasswordActivity.class));
                break;
            case R.id.password_change:
                startActivity(new Intent(WalletPayActivity.this, PaymentResetPasswordActivity.class));
                break;
            case R.id.rlWalletMainBackBtn:
                onFailPayment("back");
                serverSideMoaPayController.initOrderIfCanceled(pOrderID);
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

//                Intent returnIntent = new Intent();
//                setResult(Activity.RESULT_OK, returnIntent);
//                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
        if (requestCode == MoaConstants.REQUEST_IDENTITY_ACTIVITY
                || requestCode == MoaConstants.REQUEST_WALLET_SETTING_ACTIVITY
                || requestCode == MoaConstants.REQUEST_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT) {
            //카드 추가하기 메뉴에서 카드 추가 등록시
            //설정 화면에서 카드 추가 등록시
            //약관동의->인증->카드등록 완료가 정상적으로 되었을시에만 갱신처리
            if (resultCode == MoaConstants.RESULT_CARDREGISTER_SUCCESS) {
                getCardList();
            }
        }
    }

    private void checkPassWord() {
        String pw = inputWalletPayPassword.getInputText();

        if (pw.equals("") & pw.length() < 6) {
            Toast.makeText(WalletPayActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        cardpw = pw;

//        String result = data.getStringExtra("pwresult");
//        String resultid = data.getStringExtra("idresult");
//        String resulttoken = data.getStringExtra("toeknresult");

        Map<String, String> payData = new HashMap<>();
        payData.put("payPw", cardpw);
        payData.put("orderID", pOrderID);
        payData.put("token", cardPagerAdapter.getCardDataList().get(cardViewPager.getCurrentItem()).getCARDTOKEN());
        serverSideMoaPayController.init(this);
        serverSideMoaPayController.payCard(payData);

//        String orderID = getIntent().getStringExtra("tempid");
//        String orderToken = getIntent().getStringExtra("temptoken");
//
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("pwresult",pw);
//        returnIntent.putExtra("idresult",orderID);
//        returnIntent.putExtra("toeknresult",orderToken);
//
//        setResult(Activity.RESULT_OK,returnIntent);
//        finish();
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
                        Intent intent = new Intent(WalletPayActivity.this, WalletAgreeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(intent, MoaConstants.REQUEST_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT);
                    } else {
                        Intent intent = new Intent(WalletPayActivity.this, IdentityActivity.class);
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

    /**
     * 카드 뷰페이저 이동시에 따른 UI 처리
     *
     * @param position
     */
    private void moveCardViewPager(int position) {

        Logger.d("moveCardViewPager position " + position);

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
     *
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


    @Override
    public void onLoadCompleteCardList(List<CardListItem> cardListItems) {

        this.cardListItems = cardListItems;
        Logger.d("onLoadCompleteCardList >>>> cardListItems size " + cardListItems.size());

        if (cardPagerAdapter != null) {
            cardPagerAdapter.clearCardList();
        }

        haveNoCard = cardListItems.size() == 0;

        if (haveNoCard == true) {
            rlSetting.setVisibility(View.GONE);
        } else {
            rlSetting.setVisibility(View.VISIBLE);
        }

        if (cardListItems != null) {
            this.cardListItems = cardListItems;
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
        //Toast.makeText(this, "성공요!", Toast.LENGTH_SHORT).show();
        scrollWalletPayContainer.setVisibility(View.VISIBLE);
        rlPwView.setVisibility(View.GONE);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onFailPayment(String msg) {
        if (msg.equals("back")) {
            setResult(RESULT_FIRST_USER, getIntent());
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_CANCELED, getIntent());
        }
        finish();

    }

    @Override
    public void onForwardInitPswNonce(String nonce) {
    }

    @Override
    public void onNotMatchedPw() {
        commonLoadingViewWalletPay.animationStop(this);
        View tvWalletPayPwErrorMsg = findViewById(R.id.tvWalletPayPwErrorMsg);
        tvWalletPayPwErrorMsg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRetrofitSuccess(int type, BaseModel baseModel) {
        if (cardPagerAdapter != null) {
            cardPagerAdapter.removePage(removeCardPosition);

            if (cardPagerAdapter.getCount() == 0) {
                List<CardListItem> cardList = new ArrayList<>();
                CardListItem cardItem = new CardListItem();
                cardList.add(cardItem);
                cardPagerAdapter.setCardDataList(cardList);
            }

            cardPagerAdapter.notifyDataSetChanged();

            Boolean haveCard = cardPagerAdapter.getCount() == 0;
            if (haveCard == true) {
                rlSetting.setVisibility(View.GONE);
            } else {
                rlSetting.setVisibility(View.VISIBLE);
            }
            Toast.makeText(WalletPayActivity.this, getString(R.string.msg_card_delete_success), Toast.LENGTH_SHORT).show();
            moveCardViewPager(cardViewPager.getCurrentItem());
        }
    }

    @Override
    public void onRetrofitFail(int type, String msg) {

    }
}
