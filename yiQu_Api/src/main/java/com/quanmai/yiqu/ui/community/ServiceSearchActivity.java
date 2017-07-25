package com.quanmai.yiqu.ui.community;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseActivity;

public class ServiceSearchActivity extends BaseActivity implements View.OnClickListener {

    WebView webView;
    String weburl;
    TextView iv_no_data;
    TextView textViewTitle;
    LinearLayout linear_no_data;
    ProgressBar progressBar;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_search);

        webView = (WebView) findViewById(R.id.webView);
        iv_no_data = (TextView) findViewById(R.id.iv_no_data);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        linear_no_data = (LinearLayout) findViewById(R.id.linear_no_data);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.rl_right).setOnClickListener(this);

        initSetting();

        textViewTitle.setText(getIntent().getStringExtra("title"));
        weburl = getIntent().getStringExtra("weburl");
        if (weburl != null && weburl != "" && weburl.startsWith("http")) {
            webView.loadUrl(weburl);
        } else {
            linear_no_data.setVisibility(View.VISIBLE);
        }
    }


    private void initSetting() {
        webSettings = webView.getSettings();

//        webView.setPadding(0, 0, 0, 0);
//        webView.setInitialScale(1);

        webSettings.setJavaScriptEnabled(true);

//        webSettings.setDisplayZoomControls(false); //隐藏webview缩放按钮
//        webSettings.setBuiltInZoomControls(true); //设置支持缩放
        webSettings.setUseWideViewPort(true);       //将图片调整到适合webview的大小
//        webSettings.setLoadWithOverviewMode(true);  //缩放至屏幕大小
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                webView.setVisibility(View.VISIBLE);
                linear_no_data.setVisibility(View.GONE);
//                showLoadingDialog("请稍候");
                webView.setEnabled(false);

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                dismissLoadingDialog();

                webView.setEnabled(true);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                dismissLoadingDialog();
                webView.setVisibility(View.GONE);
                linear_no_data.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {  //表示按返回键
                        webView.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 0 && newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
            }

            case R.id.rl_right: {
                finish();
                break;
            }
        }
    }
}
