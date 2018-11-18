package com.dollop.syzygy.activity;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dollop.syzygy.R;

public class SupportDetailActivity extends BaseActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private ImageView imgHeader;

    @Override
    protected int getContentResId() {
        return R.layout.contentwebview;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbarWithBackButton("Supports");
        webView = (WebView) findViewById(R.id.webView);

        webView.setBackgroundColor(0);
        webView.getSettings().setJavaScriptEnabled(true);

      /*  webView.setWebViewClient(new WebViewClient() {

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

                handler.proceed();

            }
        });*/

        webView.loadUrl("http://syzygycare.com/page/disclaimer");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }
}