package com.quanmai.yiqu.ui.Around;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.CouponInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.share.ShareActivity;

public class AroundWebDetailsActivity extends ShareActivity {

    LinearLayout linear_no_data;
    WebView webView;
    String weburl;
    Button buttonFinish, buttonShare;
    CouponInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around_web_details);

        init();
        initSetting();
        weburl = getIntent().getStringExtra("url");
        mInfo = (CouponInfo) getIntent().getSerializableExtra("info");
//        weburl = getIntent().getStringExtra("weburl");
        if (weburl != null && weburl != "" && weburl.startsWith("http")) {
            webView.loadUrl(weburl);
        } else {
            linear_no_data.setVisibility(View.VISIBLE);
        }

//        buttonFinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (webView.canGoBack()){
//                    webView.goBack();
//                }else {
//                    finish();
//                }
//            }
//        });
//
//        buttonShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getShareDialog(
//                        weburl,
//                        "",
//                       "", "响应环保，一起加入闲置物品置换吧！");
//                showShareDialog();
//            }
//        });
    }

    private void init() {
        webView = (WebView) findViewById(R.id.webView);
        linear_no_data = (LinearLayout) findViewById(R.id.linear_no_data);
//        buttonFinish = (Button)findViewById(R.id.buttonFinish);
//        buttonShare = (Button)findViewById(R.id.buttonShare);
    }

    private void initSetting() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.requestFocusFromTouch();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
                view.stopLoading();
                if (url.contains("getBack")) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        AroundWebDetailsActivity.this.finish();
                    }
                } else if (url.contains("share")) {
                    getShareDialog(
                            weburl,
                            mInfo.privilegeName,
                            UserInfo.get().prefixQiNiu
                                    + mInfo.thumbnail, mInfo.described);
                    showShareDialog();
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                webView.setVisibility(View.VISIBLE);
                linear_no_data.setVisibility(View.GONE);
                showLoadingDialog("请稍候");
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
                webView.setVisibility(View.GONE);
                linear_no_data.setVisibility(View.VISIBLE);
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
    }
}
