package com.dollop.syzygy.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.sohel.S;

public class TermAndConditionActivity extends BaseActivity {

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

        setToolbarWithBackButton("Term & Condition");
        webView = (WebView) findViewById(R.id.webView);

        webView.setBackgroundColor(0);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        final Dialog materialDialog = S.initProgressDialog(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                materialDialog.dismiss();
            }
        },4000);
        webView.loadUrl("http://syzygycare.com/page/term_condition");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }
}