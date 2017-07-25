package com.quanmai.yiqu.ui.recycle;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.base.BaseActivity;

public class RecycleScoreExplainActivity extends BaseActivity implements View.OnClickListener {

    ProgressBar progressBarWeb;
    WebView webViewExplain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_score_explain);
     init();
    }

    public void init() {
        ((TextView) findViewById(R.id.tv_title)).setText("得分说明");
        findViewById(R.id.iv_back).setOnClickListener(this);

        progressBarWeb = (ProgressBar) findViewById(R.id.progressBarWeb);
        webViewExplain = (WebView) findViewById(R.id.webViewExplain);
        webViewExplain.requestFocus();
        webViewExplain.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url); //在当前的webview中跳转到新的url
                return true;
            }
        });

        webViewExplain.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
                if (newProgress > 0 && newProgress < 100) {
                    progressBarWeb.setVisibility(View.VISIBLE);
                    progressBarWeb.setProgress(newProgress);
                } else {
                    progressBarWeb.setVisibility(View.GONE);
                }
            }
        });

        webViewExplain.loadUrl(ApiConfig.CLSSSIFIGARBAGE_URL + "/yiqu/garbage-check/");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
        }
    }
}
