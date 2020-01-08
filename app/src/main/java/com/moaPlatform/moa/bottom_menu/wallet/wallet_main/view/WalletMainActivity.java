package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.controller.LogInActivity;
import com.moaPlatform.moa.auth.sign_up.controller.SignUpActivity;
import com.moaPlatform.moa.activity.EventWebViewActivity;
import com.moaPlatform.moa.bottom_menu.wallet.barcode.QrLoginDialog;
import com.moaPlatform.moa.bottom_menu.wallet.barcode.QrMainActivity;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.adapter.CardPagerAdapter;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.controller.WalletController;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.MoaPayAgrmnt;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response.ElecWaltInfo;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response.PointCoinModel;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.constants.MoaUrlConstants;
import com.moaPlatform.moa.payment.CardListItem;
import com.moaPlatform.moa.payment.PaymentResultsReceiver;
import com.moaPlatform.moa.payment.ServerSideMoaPayController;
import com.moaPlatform.moa.side_menu.coupon.CouponListActivity;
import com.moaPlatform.moa.side_menu.order.OrderHistoryActivity;
import com.moaPlatform.moa.util.DeviceUtil;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.MathUtil;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.custom_view.CommonLoadingView;
import com.moaPlatform.moa.util.dialog.yesOrNo.LoginSigninDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.OneBtnDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.interfaces.ViewDataInitHelper;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.MoneyConverter;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 메인 탭메뉴 > 지갑 화면
 */
public class WalletMainActivity extends AppCompatActivity implements View.OnClickListener, ViewDataInitHelper, PaymentResultsReceiver, RetrofitConnectionResult, MoneyConverter {

    private final double aspectAnim = 1.2676;       //배경 이미지 가로세로 비율

    //카드 UI 영역
    private ViewPager cardViewPager;
    private ImageView ivWalletMainMaskImage;
    private CommonLoadingView commonLoadingViewWalletMain;
    private RelativeLayout rlLeftArrow;
    private RelativeLayout rlRightArrow;
    private CardPagerAdapter cardPagerAdapter;
    private View walletSetting;
    private LinearLayout llWalletMainAddcard;
    private RelativeLayout rlWalletMainTopInfoContainer;        //상단 anim BG  영역 정보

    private LinearLayout linCoupon;
    private LinearLayout llBarcodePayment;                      //QR 코드 결제
    private RelativeLayout rlWalletMainBack;
    private RelativeLayout rlWalletMainLoginContainer;
    private LinearLayout llWalletMainWalletAddressContainer;
    private LinearLayout llWalletMainMemberButtonConstainer;
    private LinearLayout llPaymentHistoryConstainer;    //결제 내역
    private Button btnBreakdownCoin;                    //적립&사용내역(월렛)
    private Button btnChangePoint;
    private Button btnPointHistory;                     //포인트 사용내역
    private Button btnWalletMainLogin;
    private TextView tvWalletMainMoaCoin;               //모아 코인
    private TextView tvWalletMainpoint;                 //포인트
    private TextView tvWalletMainpointWon;              //포인트 > 원
    private LinearLayout llWalletMainStandByPointContainer;
    private TextView tvWalletMainStandByPoint;          //활성 예정 포인트
    private TextView walletAddress;                     // 지갑 주소
    private LoginSigninDialog loginSigninDialog;
    private QrLoginDialog qrLoginDialog;

    private ServerSideMoaPayController serverSideMoaPayController;
    private WalletController walletController;

    private List<CardListItem> cardListItems;

    private int removeCardPosition;

    private View.OnClickListener loginListener = v -> {
        if(loginSigninDialog != null && loginSigninDialog.isShowing()){
            loginSigninDialog.dismiss();
        }
        Intent intent = new Intent(WalletMainActivity.this, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, MoaConstants.REQUEST_USER_LOGIN);
    };

    private View.OnClickListener signinListener = v -> {
        if(loginSigninDialog != null && loginSigninDialog.isShowing()){
            loginSigninDialog.dismiss();
        }
        Intent intent = new Intent(WalletMainActivity.this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, MoaConstants.REQUEST_USER_JOIN);
    };

    private View.OnClickListener noListener = new View.OnClickListener() {
        public void onClick(View v) {
            qrLoginDialog.dismiss();
        }
    };

    private View.OnClickListener yesListener = new View.OnClickListener() {
        public void onClick(View v) {
            qrLoginDialog.dismiss();
            Intent intent = new Intent(WalletMainActivity.this, LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, MoaConstants.REQUEST_USER_LOGIN);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_main_activity);
        initLayout();
        initAdapter();
        initController();
        initLoginUserView();
        if (serverSideMoaPayController != null) {
            serverSideMoaPayController.getCardList();
        }
        if (walletController != null) {
            walletController.getPointCoin();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 변수 초기화
     */
    private void initLayout() {

        commonLoadingViewWalletMain = findViewById(R.id.commonLoadingViewWalletMain);
        commonLoadingViewWalletMain.animationStop(this);

        rlLeftArrow = findViewById(R.id.rlLeftArrow);
        rlRightArrow = findViewById(R.id.rlRightArrow);

        llWalletMainAddcard = findViewById(R.id.llWalletMainAddcard);
        ivWalletMainMaskImage = findViewById(R.id.ivWalletMainMaskImage);

        float width = DeviceUtil.getScreenWidth(this);
        float height = (float) (width / 2.64);
        ivWalletMainMaskImage.getLayoutParams().width = (int) width;
        ivWalletMainMaskImage.getLayoutParams().height = (int) height;
        ivWalletMainMaskImage.requestLayout();

        llBarcodePayment = findViewById(R.id.llBarcodePayment);
        linCoupon = findViewById(R.id.btnCoupon);
        walletSetting = findViewById(R.id.llWalletsetting);
        rlWalletMainBack = findViewById(R.id.rlWalletMainBack);
        rlWalletMainLoginContainer = findViewById(R.id.rlWalletMainLoginContainer);
        llWalletMainWalletAddressContainer = findViewById(R.id.llWalletMainWalletAddressContainer);
        rlWalletMainTopInfoContainer = findViewById(R.id.rlWalletMainTopInfoContainer);

        int walletBackgroundWidth = DeviceUtil.getScreenWidth(this);
        double walletBackgroundHeight = walletBackgroundWidth / aspectAnim;
        rlWalletMainTopInfoContainer.getLayoutParams().width = walletBackgroundWidth;
        rlWalletMainTopInfoContainer.getLayoutParams().height = (int) walletBackgroundHeight;

        llWalletMainMemberButtonConstainer = findViewById(R.id.llWalletMainMemberButtonConstainer);
        llPaymentHistoryConstainer = findViewById(R.id.llPaymentHistoryConstainer);
        btnBreakdownCoin = findViewById(R.id.btnWalletMainMoaCoin);
        btnChangePoint = findViewById(R.id.btnChangePoint);
        btnPointHistory = findViewById(R.id.btnPointHistory);
        btnWalletMainLogin = findViewById(R.id.btnWalletMainLogin);

        walletAddress = findViewById(R.id.walletAddress);
        tvWalletMainMoaCoin = findViewById(R.id.tvWalletMainMoaCoin);
        tvWalletMainpoint = findViewById(R.id.tvWalletMainpoint);
        tvWalletMainpointWon = findViewById(R.id.tvWalletMainpointWon);
        llWalletMainStandByPointContainer = findViewById(R.id.llWalletMainStandByPointContainer);
        tvWalletMainStandByPoint = findViewById(R.id.tvWalletMainStandByPoint);

        rlWalletMainBack.setOnClickListener(this);
        llWalletMainAddcard.setOnClickListener(this);
        llBarcodePayment.setOnClickListener(this);
        linCoupon.setOnClickListener(this);
        walletSetting.setOnClickListener(this);
        llWalletMainStandByPointContainer.setOnClickListener(this);
        btnChangePoint.setOnClickListener(this);
        llPaymentHistoryConstainer.setOnClickListener(this);
        btnPointHistory.setOnClickListener(this);
        btnWalletMainLogin.setOnClickListener(this);

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

        View rlWalletMainCopyGroup = findViewById(R.id.rlWalletMainCopyGroup);

        /*
        rlWalletMainCopyGroup.setOnClickListener(v -> {

            지갑복사 기능 임시 미사용 처리
            String tempWalletAddress = walletAddress.getText().toString();
            if (ObjectUtil.checkNotNull(tempWalletAddress) && !tempWalletAddress.equals("")) {
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(tempWalletAddress);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("walletAddress", tempWalletAddress);
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(this, getString(R.string.wallet_main_wallet_address_copy), Toast.LENGTH_SHORT).show();
            }

        });
        */

    }

    private void initAdapter() {
        cardViewPager = findViewById(R.id.cardViewPager);
        cardPagerAdapter = new CardPagerAdapter(getSupportFragmentManager());
        cardViewPager.setAdapter(cardPagerAdapter);

        cardViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

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

        serverSideMoaPayController = new ServerSideMoaPayController(this);
        serverSideMoaPayController.init(this);
        serverSideMoaPayController.setRetrofitConnectionResult(this);

        walletController = new WalletController(this);
        walletController.setRetrofitConnectionResult(this);

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

    //카드리스트 조회 성공시
    @Override
    public void onLoadCompleteCardList(List<CardListItem> cardListItems) {

        this.cardListItems = cardListItems;
        if (cardPagerAdapter != null) {
            cardPagerAdapter.clearCardList();
        }
        if (cardListItems != null) {
            Boolean haveCard = cardListItems.size() == 0;

            if (haveCard == true) {
                walletSetting.setVisibility(View.GONE);
            } else {
                walletSetting.setVisibility(View.VISIBLE);
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
                        Intent intent = new Intent(WalletMainActivity.this, WalletAgreeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(intent, MoaConstants.REQUEST_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT);

                    } else {
                        //인증화면으로 이동
                        Intent intent = new Intent(WalletMainActivity.this, IdentityActivity.class);
                        intent.putExtra(MoaConstants.EXTRA_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT, true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(intent, MoaConstants.REQUEST_IDENTITY_ACTIVITY);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                }
            });

        } else {
            loginSigninDialog = new LoginSigninDialog(WalletMainActivity.this, loginListener, signinListener);
            loginSigninDialog.show();
        }
    }

    /**
     * QR 코드 결제 하기 에서 체크 및 카드 추가.
     */
    private void checkQrPay() {
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
                        startActivity(new Intent(WalletMainActivity.this, WalletAgreeActivity.class));
                    } else {
                        if (cardPagerAdapter.getCardDataList().size() > 0) {
                            Intent intent = new Intent(WalletMainActivity.this, QrMainActivity.class);
                            startActivityForResult(intent, MoaConstants.REQUEST_WALLET_QR_CODE_MAIN_ACTIVITY);
                        } else {
                            Toast.makeText(WalletMainActivity.this, "등록된 카드가 없습니다. 카드 등록 화면으로 이동합니다", Toast.LENGTH_SHORT).show();
                            Handler delayHandler = new Handler();
                            delayHandler.postDelayed(() -> addCard(), 2000);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                }
            });

        } else {
            loginSigninDialog = new LoginSigninDialog(WalletMainActivity.this, loginListener, signinListener);
            loginSigninDialog.show();
        }
    }

    @Override
    public void onReadyJsonData(String jsonData) {
    }

    @Override
    public void onSuccessPayment() {
        //Toast.makeText(this, "성공!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailPayment(String msg) {
        if (ObjectUtil.checkNotNull(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
        }
        commonLoadingViewWalletMain.animationStop(this);
    }

    @Override
    public void onForwardInitPswNonce(String nonce) {
    }

    @Override
    public void onNotMatchedPw() {
    }

    /**
     * 로그인/비로그인 유저 UI 노출 처리
     */
    private void initLoginUserView() {

        String memberId = MoaAuthHelper.getInstance().getCurrentMemberID();
        Logger.d("memberId >>> " + memberId);

        if (memberId.contains("@")) {
            rlWalletMainLoginContainer.setVisibility(View.GONE);
            llWalletMainWalletAddressContainer.setVisibility(View.VISIBLE);
            llWalletMainMemberButtonConstainer.setVisibility(View.VISIBLE);

            btnBreakdownCoin.setBackground(ContextCompat.getDrawable(this, R.drawable.border_rect_0_0_0_0_ffffff_1dp_c5c5c5));
            btnBreakdownCoin.setTextColor(ContextCompat.getColor(this, R.color.color_545454));
            btnBreakdownCoin.setEnabled(true);
            btnChangePoint.setBackground(ContextCompat.getDrawable(this, R.drawable.border_rect_0_0_0_0_ffffff_1dp_c5c5c5));
            btnChangePoint.setTextColor(ContextCompat.getColor(this, R.color.color_545454));
            btnChangePoint.setEnabled(true);

            btnBreakdownCoin.setOnClickListener(v -> {
                StringBuilder urlBuilder = new StringBuilder();
                urlBuilder.append(MoaUrlConstants.WEBVIEW_URL_WALLET_MAIN_MOA_COIN_HISTORY);
                if (walletAddress != null && ObjectUtil.checkNotNull(walletAddress.getText().toString())) {
                    urlBuilder.append(walletAddress.getText().toString());
                }
                Intent intent = new Intent(this, EventWebViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, urlBuilder.toString());
                intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_KEY);
                startActivity(intent);
            });

        } else {
            rlWalletMainLoginContainer.setVisibility(View.VISIBLE);
            llWalletMainWalletAddressContainer.setVisibility(View.GONE);
            llWalletMainMemberButtonConstainer.setVisibility(View.GONE);
            btnBreakdownCoin.setBackground(ContextCompat.getDrawable(this, R.drawable.border_rect_0_0_0_0_e9e9e9_1dp_c5c5c5));
            btnBreakdownCoin.setTextColor(ContextCompat.getColor(this, R.color.color_989898));
            btnBreakdownCoin.setEnabled(false);
            btnChangePoint.setBackground(ContextCompat.getDrawable(this, R.drawable.border_rect_0_0_0_0_e9e9e9_1dp_c5c5c5));
            btnChangePoint.setTextColor(ContextCompat.getColor(this, R.color.color_989898));
            btnChangePoint.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Logger.d("requestCode " + requestCode + " resultCode " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MoaConstants.REQUEST_USER_JOIN) {
            if(resultCode == MoaConstants.RESULT_USER_JOIN_SUCCESS){
                initLoginUserView();
                if (serverSideMoaPayController != null) {
                    serverSideMoaPayController.initUserIdAndMemberId();
                    serverSideMoaPayController.getCardList();
                }
                if (walletController != null) {
                    walletController.getPointCoin();
                }
            }
        }else if (requestCode == MoaConstants.REQUEST_USER_LOGIN) {
            //회원 로그인 화면에서 로그인 성공시 갱신처리
            //회원 로그인 화면에서 회원가입 완료후 화면 갱신처리
            if (resultCode == MoaConstants.RESULT_LOGIN_SUCCESS || resultCode == MoaConstants.RESULT_USER_JOIN_SUCCESS) {
                initLoginUserView();
                if (serverSideMoaPayController != null) {
                    serverSideMoaPayController.initUserIdAndMemberId();
                    serverSideMoaPayController.getCardList();
                }
                if (walletController != null) {
                    walletController.getPointCoin();
                }
            }
        } else if (requestCode == MoaConstants.REQUEST_WALLET_POINT_EXCHANGE) {
            //코인 및 포인트 갱신처리
            if (walletController != null) {
                walletController.getPointCoin();
            }
        } else if (requestCode == MoaConstants.REQUEST_IDENTITY_ACTIVITY
                || requestCode == MoaConstants.REQUEST_WALLET_SETTING_ACTIVITY
                || requestCode == MoaConstants.REQUEST_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT) {
            //카드 추가하기 메뉴로 카드 추가 등록시
            //설정 화면에서 카드 추가 등록시
            //약관동의->인증->카드등록 완료가 정상적으로 되었을시에만 갱신처리
            if (resultCode == MoaConstants.RESULT_CARDREGISTER_SUCCESS) {
                if (serverSideMoaPayController != null) {
                    serverSideMoaPayController.getCardList();
                }
            }
        } else if(requestCode == MoaConstants.REQUEST_WALLET_QR_CODE_MAIN_ACTIVITY){
            if(resultCode == MoaConstants.RESILT_WALLET_QR_CODE_INPUT_DATA_ACTIVITY_CARD_LIST_CHANGED){
                if (serverSideMoaPayController != null) {
                    serverSideMoaPayController.getCardList();
                }
            }
        }
    }

    @Override
    public void onRetrofitSuccess(int type, BaseModel baseModel) {

        if (baseModel instanceof PointCoinModel) {
            PointCoinModel pointCoinModel = (PointCoinModel) baseModel;
            ElecWaltInfo elecWaltInfo = pointCoinModel.elecWaltInfo;

            Logger.d("onRetrofitSuccess " + elecWaltInfo.toString());

            try {
                tvWalletMainMoaCoin.setText(String.format(getString(R.string.wallet_coin_moa), MathUtil.moaCoinFormat(elecWaltInfo.coinBal, 0, 3)));
                tvWalletMainpoint.setText(String.format(getString(R.string.wallet_point_p), MathUtil.toNumFormat(Double.valueOf(elecWaltInfo.pointBal))));
                tvWalletMainpointWon.setText(String.format(getString(R.string.wallet_point_w), MathUtil.toNumFormat(Double.valueOf(elecWaltInfo.pointBal))));
                tvWalletMainStandByPoint.setText(String.format(getString(R.string.wallet_point_p), MathUtil.toNumFormat(Double.valueOf(elecWaltInfo.lockBal))));
                walletAddress.setText(elecWaltInfo.walletAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (cardPagerAdapter != null) {
                cardPagerAdapter.removePage(removeCardPosition);

                if (cardPagerAdapter.getCount() == 0) {
                    List<CardListItem> cardList = new ArrayList<>();
                    CardListItem cardItem = new CardListItem();
                    cardList.add(cardItem);
                    cardPagerAdapter.setCardDataList(cardList);
                }
                cardPagerAdapter.notifyDataSetChanged();
                Toast.makeText(WalletMainActivity.this, getString(R.string.msg_card_delete_success), Toast.LENGTH_SHORT).show();
                moveCardViewPager(cardViewPager.getCurrentItem());

                Boolean haveCard = cardPagerAdapter.getCount() == 0;
                if (haveCard == true) {
                    walletSetting.setVisibility(View.GONE);
                } else {
                    walletSetting.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    @Override
    public void onRetrofitFail(int type, String msg) {
        Toast.makeText(WalletMainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBarcodePayment:
                String memberId = MoaAuthHelper.getInstance().getCurrentMemberID();
                if (memberId.contains("@")) {
                    checkQrPay();
                } else {
                    qrLoginDialog = new QrLoginDialog(WalletMainActivity.this, yesListener, noListener);
                    qrLoginDialog.dialogContent("QR코드 결제를 이용하기 위해서는 로그인이 필요합니다.");
                    qrLoginDialog.dialogYes("확인");
                    qrLoginDialog.dialogNo("취소");
                    qrLoginDialog.oneBtnDialogFragmentListener(() -> qrLoginDialog.dismiss());
                    qrLoginDialog.show();
                }
                break;
            case R.id.llWalletMainAddcard:
                addCard();
                break;
            case R.id.btnCoupon:
                startActivity(new Intent(WalletMainActivity.this, CouponListActivity.class));
                break;
            case R.id.btnWalletMainLogin:
                //로그인
                Intent loginIntent = new Intent(this, LogInActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(loginIntent, MoaConstants.REQUEST_USER_LOGIN);
                break;
            case R.id.llPaymentHistoryConstainer:
                //주문 내역 화면으로 이동
                Intent paymentHistoryIntent = new Intent(WalletMainActivity.this, OrderHistoryActivity.class);
                paymentHistoryIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(paymentHistoryIntent);
                break;

            case R.id.btnChangePoint:
                //포인트전환
                Intent pointSwichingIntent = new Intent(WalletMainActivity.this, WalletPointExchangeActivity.class);
                pointSwichingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(pointSwichingIntent, MoaConstants.REQUEST_WALLET_POINT_EXCHANGE);

                break;
            case R.id.btnPointHistory:
                //적립/사용내역
                //포인트 사용내역 화면으로 이동
                Intent walletPointHisroyIntent = new Intent(WalletMainActivity.this, WalletPointHistoryActivity.class);
                walletPointHisroyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(walletPointHisroyIntent);
                break;
            case R.id.llWalletsetting:
                String memberIds = MoaAuthHelper.getInstance().getCurrentMemberID();
                if (memberIds.contains("@")) {
                    Intent walletSettingIntent = new Intent(WalletMainActivity.this, WalletSettingActivity.class);
                    walletSettingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(walletSettingIntent, MoaConstants.REQUEST_WALLET_SETTING_ACTIVITY);
                } else {
                    Toast.makeText(this, getString(R.string.common_toast_msg_use_available_after_login), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.llWalletMainStandByPointContainer:
                OneBtnDialog pointToolTipDialog = new OneBtnDialog();
                pointToolTipDialog.dialogTitle(getString(R.string.one_button_dialog_title_point_tooltip));
                pointToolTipDialog.dialogContent(getString(R.string.one_button_dialog_content_point_tooltip));
                pointToolTipDialog.setShowCloseButton(true);
                pointToolTipDialog.oneBtnDialogFragmentListener(() -> pointToolTipDialog.dismiss());
                pointToolTipDialog.show(getSupportFragmentManager(),"pointToolTipDialog");
                break;

            case R.id.rlWalletMainBack:
                finish();
                break;
        }
    }
}
