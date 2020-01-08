package com.moaPlatform.moa.side_menu.eventnotice.view.event;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.moaPlatform.moa.R;

public class EventSubWebActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private WebViewClient mPageWebViewClient = new WebViewClient() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_sub_web);

        progressBar = findViewById(R.id.loading_progressbar);

        Intent intent = getIntent();
        String url = intent.getStringExtra("loadurl");
        String checkUrl = url.trim();
        dataPagerView(checkUrl);
    }

    // 웹뷰 페이지
    @SuppressLint("SetJavaScriptEnabled")
    private void dataPagerView(String url) {
        WebView wv = findViewById(R.id.menu_webview);

        wv.setHorizontalScrollBarEnabled(false);
        wv.setVerticalScrollBarEnabled(false);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setSupportMultipleWindows(true);
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        wv.setWebViewClient(mPageWebViewClient);
        wv.loadUrl(url);
    }
}
