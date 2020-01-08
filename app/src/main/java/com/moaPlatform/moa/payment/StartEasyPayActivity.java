package com.moaPlatform.moa.payment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.interfaces.JsBridge;
import com.moaPlatform.moa.util.interfaces.JsReceiver;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

public class StartEasyPayActivity extends AppCompatActivity implements PaymentResultsReceiver, JsReceiver {
    private boolean clearHistory = false;
    private WebView webView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_easy_pay);

        String paymentOption = getIntent().getStringExtra(CodeTypeManager.PaymentCode.EASY_PAY_TYPE.toString());
        String orderId = getIntent().getStringExtra(CodeTypeManager.PaymentCode.PAYMENT_ORDER_ID.toString());
        ServerSideEasyPay serverSideEasyPay = ServerSideEasyPay.getInstance();
        serverSideEasyPay.init(this);
        serverSideEasyPay.setEasyPayData(paymentOption, orderId);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        CookieManager.getInstance().flush();
    }

    @Override
    public void onBackPressed() {

        ServerSideEasyPay.getInstance()
                .initOrderIfCanceled(
                        getIntent().getStringExtra(
                                CodeTypeManager.PaymentCode.PAYMENT_ORDER_ID.toString()
                        )
                );
        onFailPayment("back");
        super.onBackPressed();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        String startUrl = BuildConfig.REST_API_BASE_URL + "payment/order.jsp";
        webView = findViewById(R.id.wv_easy_pay);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsBridge(this), "Android");
        webView.loadUrl(startUrl);
        webView.setWebViewClient(new WebViewClient() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    checkVGuard(url);
                    view.loadUrl(url);
                    return true;
                } else
                    return runCardPaymentApp(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    checkVGuard(url);
                    view.loadUrl(url);
                    return true;
                } else
                    return runCardPaymentApp(url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.endsWith("easypay_request_app.js") || url.endsWith("order_res_submit_app.jsp")) {
                    view.setVisibility(View.GONE);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageFinished(WebView view, String url) {
                if (clearHistory) {
                    clearHistory = false;
                    webView.clearHistory();
                    webView.setVisibility(View.VISIBLE);
                }
                if (url.endsWith("easypay_request_app.jsp") || url.endsWith("order_res_submit_app.jsp")) {
                    webView.loadUrl("javascript:Android.showHTML" +
                            "(document.getElementsByTagName('body')[0].innerHTML);");
                }
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("보안 인증서에 문제가 있습니다. 계속 진행하시겠습니까?");
                builder.setPositiveButton("예", (dialog1, which) -> handler.proceed());
                builder.setNegativeButton("아니오", (dialog1, which) -> handler.cancel());
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                (dialog, which) -> result.confirm())
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }
        });
        checkAvailableChromeWebView(webView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        else
            CookieManager.getInstance().setAcceptCookie(true);
    }

    private boolean runCardPaymentApp(String url) {
        // Custom URL Scheme으로 들어오는 경우 처리
        Intent intent = null;
        try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (intent == null)
            return false;
        if (url.startsWith("intent")) {
            // intent String의 경우 기 설치된 앱인지 확인
            if (getPackageManager().resolveActivity(intent, 0) == null)
                searchGooglePlayStore(intent);

            // 카드사 가이드 코드로 아래 하드코딩 값은 가맹점에서 정하는 부분입니다 43으로 태울지 431로 태울지 정하면 됩니다
            try {
                // 대상 액티비티가 스스로 웹 브라우저가 자신을 시작해도 되도록 허용하여 링크로 참조된 데이터를 표시하게 합니다. 예컨대 이미지나 이메일 메시지 등이 이에 해당합니다
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                // Default(null)로 주면 안드로이드에서 intent의 세팅값(데이터 유형, 카테고리)에 기초하여 적절한 클래스를 결정. 기본 실행 앱이 설정 되어있지 않은 경우 오동작할 가능성도 있음
                intent.setComponent(null);
                return startActivityIfNeeded(intent, RESULT_OK);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // 설치가 되어있는 경우 실행
            Uri uri = Uri.parse(url);
            intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        return true;
    }

    private void searchGooglePlayStore(Intent intent) {
        String packageName = intent.getPackage();
        // 설치되어있지 않다면 구글 플레이스토어에서 검색
        if (packageName != null) {
            Uri uri = Uri.parse("market://search?q=pname:" + packageName);
            intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private void checkAvailableChromeWebView(WebView webView) {
        String packageName;
        // 설치 되어있는 OS 버전 확인. android 7.0 부터는 Default 값으로 Webview를 사용하지 않고 크롬 브라우저를 Webview 대신 사용함
        if (Build.VERSION.RELEASE.compareTo("7.0.0") < 0) {
            // 7.0 보다 낮은 경우 webview
            packageName = "com.google.android.webview";
        } else {
            // 그 외 chrome app
            packageName = "com.android.chrome";
        }
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            // 앱내에서 사용되는 Webkit의 크롬 버전이 53 인 경우 업데이트 유도
            int version = Integer.parseInt(packageInfo.versionName.split("\\.")[0]);
            if (version == 53) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
                final String _packageName = packageName;
                // 사용자가 업그레이드를 하고 싶어도 제조사에서 OS 업그레이드를 지원하지 않은 경우
                // 구글 플레이에서 업데이트 버튼이 활성화되지 않습니다
                builder.setMessage("웹뷰 최신버전 업데이트가 필요합니다 업데이트가 불가능한 경우 아니오를 눌러주세요");
                builder.setPositiveButton("업데이트", (dialog, which) -> {
                    Uri uri = Uri.parse("market://details?id=" + _packageName);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                });
                builder.setNegativeButton("아니오", (dialog, which) -> {
                    // 크롬 53버전을 사용할 경우 아래 onReceivedSslError 처리를 하지 않으면 특정 사이트에 접근이 불가능하며
                    // onReceivedSslError 처리를 하여 진행이 가능하더라도 사용자에게 혼란이 올 수 있습니다
                    // 설치를 거부했거나 설치가 불가능한 사용자에게 안내 멘트 혹은 페이지를 노출하거나
                    // 추가처리(집계 등) 하는 것을 권장합니다
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkVGuard(String url) {
        // vguard의 경우 자체 링크로 앱을 실행함
        if (url.equals("http://m.vguard.co.kr/card/vguard_webstandard.apk")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("market://details?id=kr.co.shiftworks.vguardweb"));
            startActivity(intent);
        }
    }

    @Override
    public void onLoadCompleteCardList(List<CardListItem> cardListItems) {
    }

    @Override
    public void onReadyJsonData(String jsonData) {
        initWebView();
        new Handler().postDelayed(() -> {
            webView.loadUrl("javascript:setEasyPayData('" + jsonData + "' )");
            clearHistory = true;
            progressBar.setVisibility(View.GONE);
        }, 1000);
    }

    @Override
    public void onSuccessPayment() {
        progressBar.setVisibility(View.GONE);
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    public void onFailPayment(String msg) {
        progressBar.setVisibility(View.GONE);
        if(msg.equals("back")){
            setResult(RESULT_FIRST_USER, getIntent());
        }else{
            setResult(RESULT_CANCELED, getIntent());
        }
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
            String result = new JSONObject(resultMsg).getString("r_res_cd");
            Intent intent = new Intent();
            if (result.equals(ServerSideMsg.SUCCESS_EASYPAY)) {
                setResult(RESULT_OK, intent);
            } else {
                setResult(RESULT_CANCELED, intent);
            }
        } catch (JSONException e) {
            throw new RuntimeException("Js Callback is failed\n", e);
        }
        finish();
    }

    @Override
    public void onJsFail() {
        Toast.makeText(this, "결제를 실패하였습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
