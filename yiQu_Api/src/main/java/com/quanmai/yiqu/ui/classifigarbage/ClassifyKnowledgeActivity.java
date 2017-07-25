package com.quanmai.yiqu.ui.classifigarbage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.base.BaseActivity;

public class ClassifyKnowledgeActivity extends BaseActivity {

    WebView webView;
    String weburl;
    TextView iv_no_data;
    String []shareUrl;
    ImageView iv_right;
    LinearLayout linear_no_data;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_knowledge);

        init();
        initSetting();
        if (getIntent().hasExtra("http_url")){
            weburl = getIntent().getStringExtra("http_url");
        }

        if (!TextUtils.isEmpty(weburl)){
            webView.loadUrl(weburl);
        }else {
            linear_no_data.setVisibility(View.VISIBLE);
        }


    }
    //初始化
    private void init() {
        webView = (WebView)findViewById(R.id.webView);
        iv_no_data = (TextView)findViewById(R.id.iv_no_data);
        iv_right = (ImageView)findViewById(R.id.iv_right);
        linear_no_data = (LinearLayout)findViewById(R.id.linear_no_data);
        iv_back = (ImageView)findViewById(R.id.iv_back);
    }
    //webView设置初始化
    private void initSetting() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webView.requestFocusFromTouch();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.stopLoading();
                shareUrl = url.split("&",url.length());
                Intent intent = new Intent(ClassifyKnowledgeActivity.this, ClassifyGarbageActivity.class);
                intent.putExtra("webUrl",shareUrl[0]);
                startActivity(intent);
                return  true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                linear_no_data.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                showLoadingDialog("请稍后");
                webView.setEnabled(false);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dismissLoadingDialog();
                webView.setEnabled(true);

                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                dismissLoadingDialog();
                linear_no_data.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }
        });

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }


}
