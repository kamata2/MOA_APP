package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.activity.EventWebViewActivity;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.controller.WalletPointExchangeController;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.ReqExchangeCoinModel;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response.ResCoinExchangeInfoModel;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.util.DecimalDigitsInputFilter;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.MathUtil;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.auth.UserWalletHelper;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.MoneyConverter;

import org.moa.wallet.android.api.MoaWalletHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.moaPlatform.moa.bottom_menu.wallet.wallet_main.controller.WalletPointExchangeController.COIN_EXCHANGE_INFO_SUCCESS;
import static com.moaPlatform.moa.bottom_menu.wallet.wallet_main.controller.WalletPointExchangeController.EXCHANGE_COIN_FOR_POINT_SUCCESS;

/**
 * 월렛 > 포인트 전환
 * 회원만 사용 가능
 * 수수료는 Moa 단위로 고정[기획 Fix]
 */
public class WalletPointExchangeActivity extends AppCompatActivity implements MoneyConverter, View.OnClickListener, RetrofitConnectionResult {

    private CommonTitleView commonTitleView;

    @BindView(R.id.tvWalletPointExchangeMoaCoin)
    TextView tvWalletPointExchangeMoaCoin;              //모아
    @BindView(R.id.tvWalletPointExchangeMoaPoint)
    TextView tvWalletPointExchangeMoaPoint;             //포인트
    @BindView(R.id.tvWalletPointExchangeWon)
    TextView tvWalletPointExchangeWon;                  //원
    @BindView(R.id.tvWalletPointExchangeRatio)
    TextView tvWalletPointExchangeRatio;                //전환 비율
    @BindView(R.id.tvWalletPointExchangeOneDayLimitTotalPoint)
    TextView tvWalletPointExchangeOneDayLimitTotalPoint;        //1일 총 전환한도
    @BindView(R.id.tvWalletPointExchangeOneDayLimitPoint)
    TextView tvWalletPointExchangeOneDayLimitPoint;             //1일 남은 포인트 전환한도
    @BindView(R.id.etWalletPointExchangeInputMoaCoin)
    EditText etWalletPointExchangeInputMoaPoint;        //moa input
    @BindView(R.id.tvWalletPointExchangeMax)
    TextView tvWalletPointExchangeMax;                  //최대
    @BindView(R.id.tvWalletPointExchangeFee)
    TextView tvWalletPointExchangeFee;                  //수수료
    @BindView(R.id.tvWalletPointExchangeToBePoint)
    TextView tvWalletPointExchangeToBePoint;            //전환될 포인트
    @BindView(R.id.cbWalletPointExchangeTermsAgree)
    CheckBox cbWalletPointExchangeTermsAgree;           //약관동의 체크박스
    @BindView(R.id.tvWalletPointExchangeTerms)
    TextView tvWalletPointExchangeTerms;                //전문보기
    @BindView(R.id.btnWalletPointExchange)
    Button btnWalletPointExchange;                      //포인트 전환하기

    private String userMoaCoin;     //보유 코인
    private String exchangeRatio;   //전환비율
    private String exchangeFee;     //전환 수수료
    private String pointTermsUrl;   //약관동의 내용 url

    private String oneDayTotalLimit;             //총 1일 전환 한도
    private String oneDayLimitPoint;       //1일 남은 전환 가능 한도
    private double postExchangePoint;       //1모아당 전환비율에 따라 계산된 포인트

    private WalletPointExchangeController walletPointExchangeController;
    private UserWalletHelper userWalletHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_point_exchange);

        ButterKnife.bind(this);

        userWalletHelper = new UserWalletHelper(this);

        initLayout();
        initListener();
        initController();

        if (walletPointExchangeController != null) {
            walletPointExchangeController.getCoinExchangeInfo();
        }
    }

    private void initLayout() {

        commonTitleView = findViewById(R.id.commonTitleWalletPointExchange);
        commonTitleView.setTitleName(getString(R.string.wallet_point_exchange_title));
        commonTitleView.setBackButtonClickListener(v -> finish());
        etWalletPointExchangeInputMoaPoint.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 8)});
        tvWalletPointExchangeToBePoint.setText(String.format(getString(R.string.wallet_point_p), "0"));

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(StringUtil.convertFontUnderLine(getString(R.string.wallet_point_exchange_terms)));
        tvWalletPointExchangeTerms.setText(builder);
    }

    private void initListener() {
        etWalletPointExchangeInputMoaPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //사용자가 입력한 Moa Coint 에 따라 포인트 자동 입력
                tvWalletPointExchangeToBePoint.setText(getExchangePoint(s.toString(), exchangeRatio));
                checkChangePointButtonStatus(cbWalletPointExchangeTermsAgree.isChecked(), s.length());
            }
        });

        cbWalletPointExchangeTermsAgree.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) ->
                checkChangePointButtonStatus(isChecked, etWalletPointExchangeInputMoaPoint.length()));

        tvWalletPointExchangeMax.setOnClickListener(this);
        tvWalletPointExchangeTerms.setOnClickListener(this);
        btnWalletPointExchange.setOnClickListener(this);
    }

    private void initController() {
        walletPointExchangeController = new WalletPointExchangeController(this);
        walletPointExchangeController.setRetrofitConnectionResult(this);
    }

    /**
     * 포인트 전환이 완료 되면 입력된 데이터를 초기화 한다.
     */
    private void refreshViewData() {
        etWalletPointExchangeInputMoaPoint.setText("");

        if (walletPointExchangeController != null) {
            walletPointExchangeController.getCoinExchangeInfo();
        }
    }

    /**
     * @param moaCoin       //보유 코인
     * @param goeatPoint    //보유 포인트(소수점 취급 안함)
     * @param exchangeRatio //전환비율
     * @param oneDayTotalLimit   //1일 전환 총 한도
     * @param exchangeAblePoint   //1일 남은 포인트 전환 가능 한도
     * @param exchangeFee   //전환 수수료
     */
    private void viewDataUpdate(String moaCoin, String goeatPoint, String exchangeRatio, String oneDayTotalLimit, String exchangeAblePoint, String exchangeFee) {

        try {
            tvWalletPointExchangeMoaCoin.setText(String.format(getString(R.string.wallet_coin_moa), MathUtil.moaCoinFormat(moaCoin, 0, MoaConstants.MOA_COIN_DECIMAL_LENGTH)));
            tvWalletPointExchangeMoaPoint.setText(String.format(getString(R.string.wallet_point_p), MathUtil.toNumFormat(Double.valueOf(goeatPoint))));
            tvWalletPointExchangeWon.setText(String.format(getString(R.string.wallet_point_w), MathUtil.toNumFormat(Double.valueOf(goeatPoint))));

            StringBuilder ExchangeRatioTextBuilder = new StringBuilder();
            ExchangeRatioTextBuilder.append(getString(R.string.wallet_point_exchange_ratio_text_header));
            ExchangeRatioTextBuilder.append(String.format(getString(R.string.wallet_point_p), String.valueOf(exchangeRatio)));
            tvWalletPointExchangeRatio.setText(ExchangeRatioTextBuilder);

            tvWalletPointExchangeOneDayLimitTotalPoint.setText("(" + String.format(getString(R.string.wallet_point_p), MathUtil.toNumFormat(Double.valueOf(oneDayTotalLimit))) + ")");
            tvWalletPointExchangeOneDayLimitPoint.setText(String.format(getString(R.string.wallet_point_p), MathUtil.toNumFormat(Double.valueOf(exchangeAblePoint))));
            tvWalletPointExchangeFee.setText(String.format(getString(R.string.wallet_coin_moa), MathUtil.moaCoinFormat(exchangeFee, 0, 0)));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 전환가능한 최대 코인을 리턴한다.
     * 전환가능 최대 코인 = (보유 코인 - 수수료)
     * 소수점 이하는 절사
     *
     * @param strUserMoaCoin 모아코인
     * @param strExchangeFee 수수료
     */
    private String getMaxInputMoaCoin(String strUserMoaCoin, String strExchangeFee) {
        //보유하고 있는 코인 기준으로 포인트를 전환하려면
        //최대 전환 가능 코인수 = (1일전환 한도 / 전환 비율) + 수수료

        String maxMoaCoin = null;
        BigDecimal oneDayPointLimitBigDecimal = new BigDecimal(oneDayLimitPoint);   //1일 남은 전환 가능 한도 포인트
        BigDecimal exchangeRatioBigDecimal = new BigDecimal(exchangeRatio);         //전환비율
        BigDecimal oneDayMoaCoinLimitBigDecimal = oneDayPointLimitBigDecimal.divide(exchangeRatioBigDecimal);       //하루 전환 가능한 Moa
        BigDecimal feeBigDecimal = new BigDecimal(strExchangeFee);                  //수수료
        BigDecimal userMoaCoinBigDecimal = new BigDecimal(strUserMoaCoin);          //유저가 소유한 코인
        BigDecimal sumBigDecimal = oneDayMoaCoinLimitBigDecimal.add(feeBigDecimal); //최대 전환 가능한 Moa + 수수료

        Logger.d("getMaxInputMoaCoin >>>> ");
        Logger.d("TEST >>>>  remain limit point " + oneDayPointLimitBigDecimal.toString());
        Logger.d("TEST >>>>  ratio " + exchangeRatioBigDecimal.toString());
        Logger.d("TEST >>>>  user Moa coin " + userMoaCoinBigDecimal.toString());
        Logger.d("TEST >>>>  user exchange possable remain moa " + oneDayMoaCoinLimitBigDecimal.toString());
        Logger.d("TEST >>>>  user moa + pee " + sumBigDecimal.toString());

        if (userMoaCoinBigDecimal.compareTo(sumBigDecimal) == 1 || userMoaCoinBigDecimal.compareTo(sumBigDecimal) == 0) {
            Logger.d("getMaxInputMoaCoin >>>>  user coin up");
            //유저가 보유한 코인이 전환가능한 코인보다 같거나 더 많으면 교환 가능한 최대 Moa 코인을 리턴
            maxMoaCoin = oneDayMoaCoinLimitBigDecimal.setScale(0, RoundingMode.FLOOR).toString();
        } else {
            //유저가 보유한 코인이 최대 전환 가능한 코인보다 적으므로 유저가 소유한 코인내에서 교환가능한 최대치를 찾는다.
            Logger.d("getMaxInputMoaCoin >>>>  user coin down");
            //사용자 모아코인에서 수수료를 뺀 수치
            maxMoaCoin = userMoaCoinBigDecimal.subtract(feeBigDecimal).setScale(0, RoundingMode.FLOOR).toString();
        }
        return maxMoaCoin;
    }

    /**
     * 사용자가 입력한 Moa Coin 전환비율에 따라 표시
     *
     * @param strUserMoaCoin 모아코인
     * @param strExchangeFee 수수료
     */
    private String getExchangePoint(String strUserMoaCoin, String strExchangeFee) {

        String resultPoint;

        if (ObjectUtil.checkNotNull(strUserMoaCoin) && ObjectUtil.checkNotNull(strExchangeFee) && strUserMoaCoin.length() > 0) {
            BigDecimal userMoaCoinBigDecimal = new BigDecimal(strUserMoaCoin);
            BigDecimal exchangeRatioDecimal = new BigDecimal(strExchangeFee);

            resultPoint = (userMoaCoinBigDecimal.multiply(exchangeRatioDecimal)).setScale(0, RoundingMode.FLOOR).toString();    //소수점 자리 절삭

            //1보다 작은 포인트는 무조건 0 포인트로 노출하도록 한다.
            if (Double.valueOf(resultPoint) < 1) {
                resultPoint = "0";
            }
        } else {
            resultPoint = "0";
        }
        postExchangePoint = Double.valueOf(resultPoint);
        return String.format(getString(R.string.wallet_point_p), resultPoint);
    }

    /**
     * 포인트 전환을 위한 전제조건 유효성 체크
     * 보유한 코인과 (교환 예정코인 + 수수료)를 합한 값을 비교한다.
     * 보유한 코인 보다 크면 버튼 비활성 처리
     *
     * @param isInputCoinCheckLogic true : 포인트 전환하기 버튼 false : 최대 버튼 로직
     */
    private boolean isCheckPointExchange(boolean isInputCoinCheckLogic) {

        if (ObjectUtil.checkNotNull(userMoaCoin)) {

            BigDecimal userMoaCoinBigDecimal = new BigDecimal(userMoaCoin);
            BigDecimal feeBigDecimal = new BigDecimal(exchangeFee);

            if (isInputCoinCheckLogic) {
                //값의 비교를 위해 사용, 소수점 맨 끝의 0을 무시하고 값이 동일하면 0, 적으면 -1, 많으면 1을 반환한다
                if (userMoaCoinBigDecimal.compareTo(feeBigDecimal.add(new BigDecimal(etWalletPointExchangeInputMoaPoint.getText().toString()))) <= -1) {
                    //(수수료 + 사용자 입력 모아코인) 보다 보유한 코인이 적은경우
                    Toast.makeText(this, getString(R.string.wallet_point_exchange_toast_msg_nothing_moa_coin_less), Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    return true;
                }
            } else {
                if (userMoaCoinBigDecimal.compareTo(feeBigDecimal.add(new BigDecimal("1"))) <= -1) {
                    //(수수료 + 1모아코인) 보다 보유한 코인이 적은경우
                    Toast.makeText(this, getString(R.string.wallet_point_exchange_toast_msg_nothing_moa_coin_less), Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    return true;
                }
            }

        } else {
            Toast.makeText(this, getString(R.string.wallet_point_exchange_toast_msg_nothing_moa_coin), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 포인트 전환하기 버튼 UI 상태 처리
     *
     * @param isAgree 약관동의 체크
     */
    private void checkChangePointButtonStatus(boolean isAgree, int inputCoinLength) {
        //전환하기 버튼 활성화 조건
        //조건1)입력한 코인 값이 1 이상이어야 한다
        //조건2)전환되는 포인트가 1 이상이어야 한다.
        if (isAgree && inputCoinLength > 0 && postExchangePoint >= 1) {
            btnWalletPointExchange.setSelected(true);
        } else {
            btnWalletPointExchange.setSelected(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //최대
            case R.id.tvWalletPointExchangeMax:
                if (isCheckPointExchange(false)) {
                    //수수료를 포함한 최대 전환 가능한 포인트를 입력한다.
                    etWalletPointExchangeInputMoaPoint.setText(getMaxInputMoaCoin(userMoaCoin, exchangeFee));
                }
                break;

            //전문보기
            case R.id.tvWalletPointExchangeTerms:
                if (ObjectUtil.checkNotNull(pointTermsUrl)) {
                    Intent intent = new Intent(this, EventWebViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, BuildConfig.REST_API_BASE_URL + pointTermsUrl);
                    intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_TRANSPARENT_VALUE);
//                    intent.putExtra(CodeTypeManager.WebViewType.TITLE_NAME_KEY.toString(), getString(R.string.wallet_point_exchange_terms_webview_title));
                    startActivity(intent);
                }
                break;

            //포인트 전환하기
            case R.id.btnWalletPointExchange:

                if (btnWalletPointExchange.isSelected()
                        && etWalletPointExchangeInputMoaPoint.getText().length() > 0
                        && Double.valueOf(etWalletPointExchangeInputMoaPoint.getText().toString()) >= 1) {

                    double oneDayRemainExchangePoint = Double.valueOf(this.oneDayLimitPoint);

                    Logger.d(" 1일 남은 교환 한도 " + String.valueOf(oneDayRemainExchangePoint));
                    Logger.d(" 전환 포인트 " + String.valueOf(postExchangePoint));

                    //1일 전환한도 체크
                    if (postExchangePoint > oneDayRemainExchangePoint) {
                        Toast.makeText(this, getString(R.string.wallet_point_exchange_over_the_limit), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (isCheckPointExchange(true)) {
                        Intent intent = new Intent(this, WalletPasswordInputActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(intent, MoaConstants.REQUEST_WALLET_PASSWORD_INPUT_ACTIVITY);
                    }

                } else {
                    if (!cbWalletPointExchangeTermsAgree.isChecked()) {
                        Toast.makeText(this, getString(R.string.wallet_point_exchange_check_terms_agree), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (etWalletPointExchangeInputMoaPoint.getText().toString().length() < 1
                            || Double.valueOf(etWalletPointExchangeInputMoaPoint.getText().toString()) < 1) {
                        Toast.makeText(this, getString(R.string.wallet_point_exchange_toast_msg_input_moa_coin), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        if(etWalletPointExchangeInputMoaPoint != null && etWalletPointExchangeInputMoaPoint.isFocused()){
//            etWalletPointExchangeInputMoaPoint.clearFocus();
//            return;
//        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MoaConstants.REQUEST_WALLET_PASSWORD_INPUT_ACTIVITY) {
            if (resultCode == MoaConstants.RESULT_WALLET_PASSWORD_INPUT_ACTIVITY) {
                if (data != null) {

                    //교환할 모아 코인수량과 암호화된 Password를 담는다.
                    ReqExchangeCoinModel model = new ReqExchangeCoinModel();

                    model.exCoinBal = etWalletPointExchangeInputMoaPoint.getText().toString();
                    model.encryptPwd = userWalletHelper.getRsaData(MoaWalletHelper.getInstance().getHmacPsw(data.getStringExtra(MoaConstants.EXTRA_WALLET_PASSWORD)));
                    Logger.d(model.toString());
                    if (walletPointExchangeController != null) {
                        walletPointExchangeController.exchangeCoinForPoint(model);
                    }
                }
            }
        }
    }

    @Override
    public void onRetrofitSuccess(int type, BaseModel baseModel) {

        if (!isFinishing()) {

            switch (type) {

                case COIN_EXCHANGE_INFO_SUCCESS:

                    String goeatPoint;      //보유 포인트(소수점 취급 안함)
                    String exchangedPointAmt;       //1일 전환한 포인트

                    ResCoinExchangeInfoModel model = (ResCoinExchangeInfoModel) baseModel;
                    if(ObjectUtil.checkNotNull(model.goeatPoint)){
                        goeatPoint = model.goeatPoint;                  //보유 포인트(소수점 취급 안함)
                    }else{
                        goeatPoint = "0";
                    }

                    if(ObjectUtil.checkNotNull(model.exchangedPointAmt)){
                        exchangedPointAmt = model.exchangedPointAmt;    //하루동안 사용자가 전환한 포인트
                    }else{
                        exchangedPointAmt = "0";
                    }

                    this.oneDayTotalLimit = model.oneDayLimit;  //1일 전환한도
                    this.userMoaCoin = model.moaCoin;           //보유 코인
                    this.exchangeRatio = model.swichingRatio;   //전환비율
                    this.exchangeFee = model.swichingFee;       //전환 수수료
                    this.pointTermsUrl = model.pointTermsUrl;   //약관동의 내용 url

                    this.oneDayLimitPoint = String.valueOf(Double.valueOf(oneDayTotalLimit) - Double.valueOf(exchangedPointAmt));

                    viewDataUpdate(userMoaCoin, goeatPoint, exchangeRatio, oneDayTotalLimit, oneDayLimitPoint, exchangeFee);
                    break;

                case EXCHANGE_COIN_FOR_POINT_SUCCESS:
                    Toast.makeText(this, getString(R.string.wallet_point_exchange_toast_msg_success), Toast.LENGTH_SHORT).show();
                    refreshViewData();
                    break;
            }
        }
    }

    @Override
    public void onRetrofitFail(int type, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
