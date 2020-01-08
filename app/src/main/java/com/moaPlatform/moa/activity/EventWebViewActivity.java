package com.moaPlatform.moa.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.controller.LogInActivity;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.custom_view.CommonLoadingView;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;

/**
 * 사용처
 * <p>
 * 배달 리스트 상단 배너 클릭시 이벤트 웹뷰
 * 월렛 모아코인 사용내역
 * 월렛 포인트 전환화면에 있는 이용약관
 */
public class EventWebViewActivity extends AppCompatActivity {

    // URL 관련정보
    public static final String EXTRA_EVENT_URL = "EXTRA_EVENT_URL";
    public static final String EXTRA_EVENT_URL_PARMS = "EXTRA_EVENT_URL_PARMS";

    // 타이틀 관련 정보
    public static final String EXTRA_TITLE_TYPE_KEY = "EXTRA_TITLE_TYPE_KEY";
    public static final String EXTRA_TITLE_TYPE_SHOW_VALUE = "EXTRA_TITLE_TYPE_SHOW_VALUE";                 // 타이틀 보이도록
    public static final String EXTRA_TITLE_TYPE_TRANSPARENT_VALUE = "EXTRA_TITLE_TYPE_TRANSPARENT_VALUE";   // 타이틀 배경이 투명이게..
    public static final String EXTRA_TITLE_NAME = "EXTRA_TITLE_NAME";

    private WebView webView;

    private String loadUrl;
    private String params;

    private CommonLoadingView viewEventWebLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_webview);

        initData();
        initLayout();
        initWebView();

        if (loadUrl != null) {
            if (ObjectUtil.checkNotNull(params)) {
                webView.loadUrl(loadUrl + "?" + params);
            } else {
                webView.loadUrl(loadUrl);
            }
        }
    }

    private void initData() {
        loadUrl = getIntent().getStringExtra(EXTRA_EVENT_URL);
        params = getIntent().getStringExtra(EXTRA_EVENT_URL_PARMS);
    }

    private void loadingStop() {
        if(viewEventWebLoading != null){
            viewEventWebLoading.animationStop(this);
        }
    }

    private void initLayout() {
        viewEventWebLoading = findViewById(R.id.viewEventWebLoading);
        viewEventWebLoading.setTouchAbleProgressBackground(true);
        viewEventWebLoading.loadingAnimationPlay(this);
        webView = findViewById(R.id.webViewBannerWebview);
        CommonTitleView commonTitleView = findViewById(R.id.commonTitleEventWebView);

        if (getIntent() != null && getIntent().getStringExtra(EXTRA_TITLE_TYPE_KEY) != null
                && getIntent().getStringExtra(EXTRA_TITLE_TYPE_KEY).equals(EXTRA_TITLE_TYPE_SHOW_VALUE)) {
            if(getIntent().getStringExtra(EXTRA_TITLE_NAME) != null){
                commonTitleView.setTitleName(getIntent().getStringExtra(EXTRA_TITLE_NAME));
            }
            //타이틀이 있는 경우에는 title 아래 webView 배치
            RelativeLayout.LayoutParams webviewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            webviewLayoutParams.addRule(RelativeLayout.BELOW, commonTitleView.getId());
            webView.setLayoutParams(webviewLayoutParams);
        }else{
            //타이틀이 없는 경우에는 title 과 webView를 겹치도록 배치하고 배경을 투명으로 한다.
            commonTitleView.setBackgroundTransparent();
        }
        commonTitleView.setBackButtonClickListener(v -> finish());
    }

    private void initWebView() {

        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString() + MoaConstants.WEBVIEW_USER_AGENT);   // 웹뷰에 앱에서 접속했음을 알림
        webView.getSettings().setDatabaseEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        webView.getSettings().setLoadsImagesAutomatically(true);

        /**
         * 웹뷰 시스템폰트에 영향받지 않도록 함
         */
        WebSettings webSettings = webView.getSettings();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            webSettings.setTextZoom(100);
        }

        //캐쉬 안사용
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setSupportMultipleWindows(false);
        //webView.getSettings().setUseWideViewPort(true);
        //webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);

        webView.addJavascriptInterface(new JavaScriptInterFace(), "GoEatAndroid");
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
    }

    private class JavaScriptInterFace {
        @JavascriptInterface
        public void webViewAction(final String moveScreen) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (ObjectUtil.checkNotNull(moveScreen)) {
                        Logger.d("webViewAction moveScreen >>> " + moveScreen);
                        if (moveScreen.equals(MoaConstants.WEBVIEW_ACTION_LOGIN_ACTIVITY)) {
                            finish();
                            Intent intent = new Intent(EventWebViewActivity.this, LogInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                    } else {
                        Logger.d("webViewAction moveScreen >>> is no data");
                    }
                }
            });
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            super.onFormResubmission(view, dontResend, resend);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            Logger.d("onPageFinished --> url : " + url);
            view.requestLayout();
            loadingStop();

        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {

            switch (errorCode) {
                case ERROR_AUTHENTICATION:              // 서버에서 사용자 인증 실패
                case ERROR_BAD_URL:                     // 잘못된 URL
                case ERROR_CONNECT:                     // 서버로 연결 실패
                case ERROR_FAILED_SSL_HANDSHAKE:        // SSL handshake 수행 실패
                case ERROR_FILE:                        // 일반 파일 오류
                case ERROR_FILE_NOT_FOUND:              // 파일을 찾을 수 없습니다
                case ERROR_HOST_LOOKUP:                 // 서버 또는 프록시 호스트 이름 조회 실패
                case ERROR_IO:                          // 서버에서 읽거나 서버로 쓰기 실패
                case ERROR_PROXY_AUTHENTICATION:        // 프록시에서 사용자 인증 실패
                case ERROR_REDIRECT_LOOP:               // 너무 많은 리디렉션
                case ERROR_TIMEOUT:                     // 연결 시간 초과
                case ERROR_TOO_MANY_REQUESTS:           // 페이지 로드중 너무 많은 요청 발생
                case ERROR_UNKNOWN:                     // 일반 오류
                case ERROR_UNSUPPORTED_AUTH_SCHEME:     // 지원되지 않는 인증 체계
                case ERROR_UNSUPPORTED_SCHEME:          // URI가 지원되지 않는 방식
                    break;
                default:
                    Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
        }

        @Override
        public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
            super.onTooManyRedirects(view, cancelMsg, continueMsg);
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            super.onUnhandledKeyEvent(view, event);
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //웹뷰 내 링크 터치 시 새로운 창이 뜨지 않고
            //해당 웹뷰 안에서 새로운 페이지가 로딩되도록 함
            Logger.d("URL >>>>>> " + url);
            if (url != null && !url.equals("about:blank")) {

                if (url.startsWith("tel:")) {
                    try {
                        //CallUtils.call(mContext, url);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Logger.d("TEST  >>> onJsAlert >>>> " + message);
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {

            Logger.d("TEST  >>> onJsConfirm >>>> " + message);

            final JsResult finalRes = result;
            //finalRes.confirm();
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }

        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }
    }
}
