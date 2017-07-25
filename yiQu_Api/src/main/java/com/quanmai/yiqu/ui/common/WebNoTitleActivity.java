package com.quanmai.yiqu.ui.common;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseActivity;

public class WebNoTitleActivity extends BaseActivity {
    private WebView mWebView;
    private String URL;
    private ProgressBar progressbar;
    LinearLayout llNoData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_no_title);
        URL = getIntent().getStringExtra("http_url");
        init();
    }

    private void init() {
        llNoData = (LinearLayout) findViewById(R.id.ll_no_data);
        mWebView = (WebView) findViewById(R.id.webView);
        progressbar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);

        mWebView.addView(progressbar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                llNoData.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                showLoadingDialog("请稍后");
                mWebView.setEnabled(false);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dismissLoadingDialog();
                mWebView.setEnabled(true);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                dismissLoadingDialog();
                llNoData.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
            }
        });

        mWebView.loadUrl(URL);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
    }

    @Override
    public void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mWebView.onPause();
        super.onPause();
    }


    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }
}
