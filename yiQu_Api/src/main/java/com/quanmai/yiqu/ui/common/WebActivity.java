package com.quanmai.yiqu.ui.common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.common.widget.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

public class WebActivity extends Activity implements View.OnClickListener {
    private WebView mWebView;
    private TextView title_textView;
    private String URL = "";
    private ProgressBar progressBar;
    private LinearLayout linear_no_data;
    private String strTitle = ""; //标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        URL = getIntent().getStringExtra("http_url");
        if (getIntent().hasExtra("title")) {
            strTitle = getIntent().getStringExtra("title");
        }
        setContentView(R.layout.activity_hot);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager manager = new SystemBarTintManager(this);
            manager.setStatusBarTintEnabled(true);
            manager.setStatusBarTintColor(getResources().getColor(R.color.theme));
        }

        init();
    }

    private void init() {
        linear_no_data = (LinearLayout) findViewById(R.id.linear_no_data);
        mWebView = (WebView) findViewById(R.id.webView1);
        title_textView = (TextView) findViewById(R.id.tv_title);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        findViewById(R.id.rl_right).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        if (!TextUtils.isEmpty(strTitle)) {
            title_textView.setText(strTitle);
        }

//        mWebView.getSettings()
//                .setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setUseWideViewPort(true);
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
                linear_no_data.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.setEnabled(false);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mWebView.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                if (TextUtils.isEmpty(URL)) {
                    linear_no_data.setVisibility(View.VISIBLE);
                    mWebView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                linear_no_data.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
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

            @Override
            public void onReceivedTitle(WebView view, String title) {
//                title_textView.setText(title);
                super.onReceivedTitle(view, title);
            }
        });

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });

        mWebView.loadUrl(URL);
        mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
    }

    @Override
    public void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        mWebView.onResume();

        MobclickAgent.onPageStart(strTitle);//统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
        super.onResume();
    }

    @Override
    protected void onPause() {
        mWebView.onPause();

        MobclickAgent.onPageEnd(strTitle); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
        super.onPause();
    }

    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams wlp = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            wlp.flags |= bits;
        } else {
            wlp.flags &= ~bits;
        }
        win.setAttributes(wlp);
    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
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
