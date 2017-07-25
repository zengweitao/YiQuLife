package com.quanmai.yiqu.ui.Around;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.AroundApi;
import com.quanmai.yiqu.api.vo.CouponInfo;
import com.quanmai.yiqu.api.vo.NewsInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.util.RelativeDateFormat;
import com.quanmai.yiqu.common.widget.MyScrollViewForWeb;
import com.quanmai.yiqu.share.ShareCopyActivity;
import com.quanmai.yiqu.ui.community.ServiceSearchActivity;

public class AroundDetailsActivity extends ShareCopyActivity implements View.OnClickListener {
    ImageView imageViewBack, imageViewShare;
    RelativeLayout relativeBar;
    ImageView imageViewScrollBack, imageViewScrollShare;
    MyScrollViewForWeb scrollView;
    ImageView imageViewBg;
    TextView textViewTitle, textViewDesciption, textViewDateTime;
    ImageView imageViewCollection;
    WebView mWebView;
    Button buttonFetch;
    LinearLayout linearLayoutInfo;
    TextView textViewTagAndDate, textViewBrowseCount;
    LinearLayout linearLayoutDate;
    ProgressBar progressBar;
    TextView title;

    Dialog mDialog;
    CouponInfo mInfo;
    NewsInfo newsInfo;
    boolean isCollected = false;
    String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around_details);

        init();
        initSetting();
        initTitleBar();
//        if (getIntent().hasExtra("info")){
//            mInfo = (CouponInfo)getIntent().getSerializableExtra("info");
//        }

//        if (getIntent().hasExtra("type")&&getIntent().getStringExtra("type").equals("information")){
//            if (getIntent().hasExtra("newsInfo")){
//                newsInfo = (NewsInfo)getIntent().getSerializableExtra("newsInfo");
//            }
//
//        }

        if (getIntent().hasExtra("info")) {
            getCouponDetails(getIntent().getStringExtra("info"));
            title.setText("优惠券详情");
        }

        if (getIntent().hasExtra("newsInfo")) {
            getNewsInfoDetails(getIntent().getStringExtra("newsInfo"));
            title.setText("资讯详情");
        }

        scrollView.setOnScrollChangedListener(new MyScrollViewForWeb.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView who, int x, int y, int oldx, int oldy) {
                if (y < 100) {
                    imageViewBack.setVisibility(View.VISIBLE);
                    imageViewShare.setVisibility(View.VISIBLE);
                    relativeBar.setVisibility(View.GONE);
                } else {
                    imageViewBack.setVisibility(View.GONE);
                    imageViewShare.setVisibility(View.GONE);
                    relativeBar.setVisibility(View.VISIBLE);
                    if (y >= 100 && y < 860) {
                        if (y - 50 > 255) {
                            relativeBar.getBackground().mutate().setAlpha(255);
                        } else {
                            relativeBar.getBackground().mutate().setAlpha((y - 50));
                        }
                    } else {
                        relativeBar.getBackground().mutate().setAlpha(255);
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause(); // 暂停网页中正在播放的视频
        }
    }

    private void initTitleBar() {
        int sdkInt = android.os.Build.VERSION.SDK_INT;
        if (sdkInt<19){
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) imageViewBack.getLayoutParams();
            lp.topMargin = Utils.dp2px(this,0);
            imageViewBack.setLayoutParams(lp);

            FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams) imageViewShare.getLayoutParams();
            lp1.topMargin = Utils.dp2px(this,0);
            imageViewShare.setLayoutParams(lp1);

            FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) relativeBar.getLayoutParams();
            lp2.height = Utils.dp2px(this,44);
            relativeBar.setLayoutParams(lp2);

            RelativeLayout.LayoutParams lp3 = (RelativeLayout.LayoutParams) title.getLayoutParams();
            lp3.topMargin = Utils.dp2px(this,0);
            title.setLayoutParams(lp3);

            RelativeLayout.LayoutParams lp4 = (RelativeLayout.LayoutParams) imageViewScrollBack.getLayoutParams();
            lp4.topMargin = Utils.dp2px(this,0);
            imageViewScrollBack.setLayoutParams(lp4);

            RelativeLayout.LayoutParams lp5 = (RelativeLayout.LayoutParams) imageViewScrollShare.getLayoutParams();
            lp5.topMargin = Utils.dp2px(this,0);
            imageViewScrollShare.setLayoutParams(lp5);
        }
    }

    private void initSetting() {
        mWebView.getSettings()
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.clearFocus();
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                showLoadingDialog("请稍后");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                dismissLoadingDialog();
                mWebView.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
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
        });

    }

    private void init() {
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        imageViewShare = (ImageView) findViewById(R.id.imageViewShare);
        relativeBar = (RelativeLayout) findViewById(R.id.relativeBar);
        imageViewScrollBack = (ImageView) findViewById(R.id.imageViewScrollBack);
        imageViewScrollShare = (ImageView) findViewById(R.id.imageViewScrollShare);
        scrollView = (MyScrollViewForWeb) findViewById(R.id.scrollView);
        imageViewBg = (ImageView) findViewById(R.id.imageViewBg);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewDesciption = (TextView) findViewById(R.id.textViewDesciption);
        textViewDateTime = (TextView) findViewById(R.id.textViewDateTime);
        imageViewCollection = (ImageView) findViewById(R.id.imageViewCollection);
        mWebView = (WebView) findViewById(R.id.webView);
        buttonFetch = (Button) findViewById(R.id.buttonFetch);
        linearLayoutInfo = (LinearLayout) findViewById(R.id.linearLayoutInfo);
        textViewTagAndDate = (TextView) findViewById(R.id.textViewTagAndDate);
        textViewBrowseCount = (TextView) findViewById(R.id.textViewBrowseCount);
        linearLayoutDate = (LinearLayout) findViewById(R.id.linearLayoutDate);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        title = (TextView) findViewById(R.id.title);

        buttonFetch.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);
        imageViewShare.setOnClickListener(this);
        imageViewScrollBack.setOnClickListener(this);
        imageViewScrollShare.setOnClickListener(this);
        imageViewCollection.setOnClickListener(this);
    }

    //收藏优惠券
    private void collectedCoupon(final boolean isCollect, String couponId) {
        AroundApi.getInstance().couponCollect(mContext, isCollect, couponId, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                isCollected = !isCollected;
                if (isCollected) {
                    showCustomToast("收藏成功");
                    imageViewCollection.setBackgroundResource(R.drawable.around_details_collection);
                } else {
                    showCustomToast("已取消收藏");
                    imageViewCollection.setBackgroundResource(R.drawable.around_details_collection_none);
                }
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    private void showCollectionDialog() {
        mDialog = DialogUtil.getConfirmDialog(mContext, "是否取消收藏？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonConfirm: {
                        collectedCoupon(isCollected, mInfo.id);
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        break;
                    }
                    case R.id.buttonCancel: {
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        break;
                    }
                }
            }
        });
        mDialog.show();
    }

    //获取资讯信息
    private void getNewsInfoDetails(String id) {
        AroundApi.getInstance().getNewsInfoDetails(mContext, id, new ApiConfig.ApiRequestListener<NewsInfo>() {
            @Override
            public void onSuccess(String msg, NewsInfo data) {
                newsInfo = data;
                linearLayoutInfo.setVisibility(View.VISIBLE);
                linearLayoutDate.setVisibility(View.GONE);

                ImageloaderUtil.displayImage(mContext, newsInfo.img, imageViewBg);
                textViewBrowseCount.setText(newsInfo.accTimes);
                textViewTagAndDate.setText(newsInfo.tag + "  |  " + RelativeDateFormat.format(newsInfo.opeTime));
                if (!TextUtils.isEmpty(newsInfo.described) && !newsInfo.described.equals("null")) {
                    textViewDesciption.setText(newsInfo.described);
                } else {
                    textViewDesciption.setVisibility(View.GONE);
                }

                textViewTitle.setText(newsInfo.title);
                if (TextUtils.isEmpty(newsInfo.linkUrl)) {
                    buttonFetch.setVisibility(View.GONE);
                }
                webUrl = newsInfo.linkUrl;
                mWebView.loadData(newsInfo.detainstructions, "text/html; charset=UTF-8", null);//这种写法可以正确解码
//            mWebView.loadUrl("http://v.qq.com/iframe/player.html?vid=m0305cupakg&width=600&height=502.5&auto=0 ");
                if (TextUtils.isEmpty(webUrl)) {
                    buttonFetch.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }


    private void getCouponDetails(String couponId) {
        AroundApi.getInstance().getCouponDetails(mContext, couponId, new ApiConfig.ApiRequestListener<CouponInfo>() {
            @Override
            public void onSuccess(String msg, CouponInfo data) {
                mInfo = data;
                if (mInfo != null) {
                    ImageloaderUtil.displayImage(mContext, mInfo.titleimg, imageViewBg);
                    textViewTitle.setText(mInfo.privilegeName);
                    if (TextUtils.isEmpty(mInfo.privilegePrice)) {
                        textViewDesciption.setVisibility(View.GONE);
                    } else {
                        textViewDesciption.setText(mInfo.privilegePrice);
                    }
                    textViewDateTime.setText("有效期至" + DateUtil.formatToOther(mInfo.endTime, "yyyy-MM-dd", "yyyy年MM月dd日"));
                    if (mInfo.status.split("-")[1].equals("0")) {
                        isCollected = false;
                        imageViewCollection.setBackgroundResource(R.drawable.around_details_collection_none);
                    } else {
                        isCollected = true;
                        imageViewCollection.setBackgroundResource(R.drawable.around_details_collection);
                    }
                    webUrl = mInfo.linkUrl;
//            textViewLoadHtml.setText(mInfo.privilegedetail);
//            mWebView.loadData(mInfo.privilegedetail,"text/html","utf-8") ;
                    mWebView.loadData(mInfo.privilegedetail, "text/html; charset=UTF-8", null);//这种写法可以正确解码

                    if (TextUtils.isEmpty(webUrl)) {
                        buttonFetch.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack: {
                finish();
                break;
            }
            case R.id.imageViewShare: {
                if (mInfo != null) {
                    getShareDialog(
                            mInfo.share_link,
                            mInfo.privilegeName,
                            UserInfo.get().prefixQiNiu
                                    + mInfo.thumbnail
                            , mInfo.described);
                } else {
                    getShareDialog(
                            newsInfo.share_link,
                            newsInfo.described,
                            UserInfo.get().prefixQiNiu
                                    + newsInfo.img
                            , newsInfo.title);
                }
                showShareDialog();
                break;
            }
            case R.id.imageViewScrollBack: {
                finish();
                break;
            }
            case R.id.imageViewScrollShare: {
                if (mInfo != null) {
                    getShareDialog(
                            mInfo.share_link,
                            mInfo.privilegeName,
                            UserInfo.get().prefixQiNiu
                                    + mInfo.thumbnail
                            , mInfo.described);

                } else {
                    getShareDialog(
                            newsInfo.share_link,
                            newsInfo.described,
                            UserInfo.get().prefixQiNiu
                                    + newsInfo.thumbnail
                            , newsInfo.title);
                }
                showShareDialog();
                break;
            }
            //收藏
            case R.id.imageViewCollection: {
                if (getIntent().hasExtra("dialogType")
                        && getIntent().getStringExtra("dialogType").equals("show") && isCollected) {
                    showCollectionDialog();
                } else {
                    collectedCoupon(isCollected, mInfo.id);
                }

                break;
            }
            //领取优惠券
            case R.id.buttonFetch: {
                Intent intent = new Intent(this, ServiceSearchActivity.class);
                intent.putExtra("title", "领取优惠");
                intent.putExtra("weburl", webUrl);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }


}
