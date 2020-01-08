package com.moaPlatform.moa.payment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.MoaPaySetAgrmnt;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.preference.PaySignPreference;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.JsBridge;
import com.moaPlatform.moa.util.interfaces.JsReceiver;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 카드등록 웹뷰
 */
public class CardRegisterActivity extends AppCompatActivity implements PaymentResultsReceiver, JsReceiver {

    public final static String EXTRA_CARD_REGISTER_NICK_NAME = "EXTRA_CARD_REGISTER_NICK_NAME";

    private boolean clearHistory;
    private WebView webView;
    private ProgressBar progressBar;
    private ServerSideMoaPayController serverSideMoaPayController;

    private String cardNick;
    private boolean isCardTermsAndConditionsAgreement;      //카드 약관 동의

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_register);

        initDefaultData();

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        initWebView();

        serverSideMoaPayController = new ServerSideMoaPayController(this);
        serverSideMoaPayController.init(this);
    }

    private void initDefaultData() {
        cardNick = getIntent().getStringExtra(EXTRA_CARD_REGISTER_NICK_NAME);
        isCardTermsAndConditionsAgreement = getIntent().getBooleanExtra(MoaConstants.EXTRA_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO CHAN : 생명주기와 상황에 쿠키동기화 로직 처리 필요 임시 분기처리...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().flush();
        }else{
            //2019-07-12 IM-A850S 4.4.2 베가폰에서 이슈가 발생했고 해당 부분 분기처리
            CookieSyncManager.createInstance(this);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initWebView() {
        String cardRegisterUrl = BuildConfig.REST_API_BASE_URL + "payment/cert.jsp";
        webView = findViewById(R.id.wv_register_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsBridge(this), "Android");
        webView.loadUrl(cardRegisterUrl);
        webView.setWebViewClient(new WebViewClient() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // url 주소에 해당하는 웹페이지를 로딩
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // 페이지 요청이 시작될 경우 호출된다.
                if (url.endsWith("CertResKicc.do"))
                    view.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 페이지 로딩시 호출된다.
                if (clearHistory) {
                    clearHistory = false;
                    webView.clearHistory();
                    webView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
                if (url.endsWith("CertResKicc.do")) {
                    view.loadUrl("javascript:Android.showHTML" +
                            "(document.getElementsByTagName('html')[0].innerHTML);");
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                (dialogInterface, i) -> result.confirm())
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Logger.d("[kekemusa]" + consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return true;
            }
        });

        new Handler().postDelayed(() ->
                        serverSideMoaPayController.cardRegistrationPreparation(PaySignPreference.getInstance().getWalletPassword(this), cardNick),
                2000);
    }

    @Override
    public void onLoadCompleteCardList(List<CardListItem> cardListItems) {
    }

    @Override
    public void onReadyJsonData(String jsonData) {
        webView.loadUrl("javascript:setCardRegisterData('" + jsonData + "')");
        clearHistory = true;
    }

    @Override
    public void onSuccessPayment() {

    }

    @Override
    public void onFailPayment(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onForwardInitPswNonce(String nonce) {
    }

    @Override
    public void onNotMatchedPw() {
    }

    @Override
    public void onJsResultMsg(String resultMsg) {
        try {
            String result = new JSONObject(resultMsg).getString("result");
            if (result.equals(ServerSideMsg.SUCCESS_MOAPAY)) {
                if (!isCardTermsAndConditionsAgreement) {
                    requestMoaPayAgrmnt();
                } else {
                    //약관동의가 되어있는 사용자 로직 수행
                    setResult(MoaConstants.RESULT_CARDREGISTER_SUCCESS);
                    Toast.makeText(this, getString(R.string.common_toast_msg_card_register_success), Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, getString(R.string.common_toast_msg_card_register_fail) + new JSONObject(resultMsg).getString("resultMsg"), Toast.LENGTH_SHORT).show();
                setResult(MoaConstants.RESULT_CARDREGISTER_FAIL);
                finish();
            }
        } catch (JSONException e) {
            Toast.makeText(this, getString(R.string.common_toast_msg_card_register_fail), Toast.LENGTH_SHORT).show();
            setResult(MoaConstants.RESULT_CARDREGISTER_FAIL);
            finish();
        }
    }

    @Override
    public void onJsFail() {
        Toast.makeText(this, getString(R.string.common_toast_msg_card_register_fail), Toast.LENGTH_SHORT).show();
        setResult(MoaConstants.RESULT_CARDREGISTER_FAIL);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(MoaConstants.RESULT_CARDREGISTER_CANCEL);
        finish();
    }

    /**
     * 카드 등록완료시 약관동의 처리한다.
     * 이전에 카드 약관등록이 된 사용자는 해당 로직을 수행하지 않는다.
     */
    private void requestMoaPayAgrmnt() {
        MoaPaySetAgrmnt moaPaySetAgrmnt = new MoaPaySetAgrmnt();
        moaPaySetAgrmnt.moaPayUseClausAgrmntYn = "Y";
        moaPaySetAgrmnt.elctrFnnclTrnscUseClausAgrmntYn = "Y";
        moaPaySetAgrmnt.indvInfoClctnAndUseAgrmntYn = "Y";
        moaPaySetAgrmnt.indvInfoThirdPartyPrvsnAgrmntYn = "Y";
        moaPaySetAgrmnt.payInfoPrvsnAgrmntYn = "Y";
        RetrofitClient.getInstance().getMoaService().setMoaPayAgrmnt(moaPaySetAgrmnt).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    requestMoaPayAgrmnt();
                    return;
                }
                if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    setResult(MoaConstants.RESULT_CARDREGISTER_SUCCESS);
                    Toast.makeText(CardRegisterActivity.this, getString(R.string.common_toast_msg_card_register_success), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(CardRegisterActivity.this, getString(R.string.common_toast_msg_agreement_register_fail), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                Toast.makeText(CardRegisterActivity.this, getString(R.string.common_toast_msg_agreement_register_fail), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
