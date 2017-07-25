package com.quanmai.yiqu.ui.Around;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.AroundApi;
import com.quanmai.yiqu.api.vo.NewsInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.util.RelativeDateFormat;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.common.widget.MyScrollViewForWeb;
import com.quanmai.yiqu.share.ShareCopyActivity;
import com.quanmai.yiqu.ui.groupbuy.GroupBuyActivity;
import com.quanmai.yiqu.ui.groupbuy.RNCacheViewManager;

/**
 * 团购界面
 */
public class GroupBuyingActivity extends ShareCopyActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private ImageView imgBg;
    private TextView tvTagAndDate;
    private TextView tvBrowseCount;
    private TextView tvGoodTitle;
    private TextView tvDescription;
    private TextView tvShopName;
    private TextView tvShopAddress;
    private ImageView imgPhoneCall;
    private WebView webView;
    private MyScrollViewForWeb scrollView;
    private ImageView imgBack;
    private ImageView imgShare;
    private TextView tvTitle;
    private ImageView imgScrollBack;
    private ImageView imgScrollShare;
    private RelativeLayout relativeBar;
    private TextView tvGroupPrice; //团购价（文本组件）
    private TextView tvOriPrice;   //原价（文本组件）
    private TextView tvApply;
    private TextView tvApplyNum;
    private RelativeLayout rlApply;

    Dialog callDialog;
    NewsInfo newsInfo;
    String webUrl;

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_buying);
        initView();
        init();
        initWebView();
        initTitleBar();
        RNCacheViewManager.init((Activity) this, new GroupBuyActivity("RnForYiQu",new Bundle(),this));
    }

    private void initTitleBar() {
        int sdkInt = android.os.Build.VERSION.SDK_INT;
        if (sdkInt<19){
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) imgBack.getLayoutParams();
            lp.topMargin = Utils.dp2px(this,0);
            imgBack.setLayoutParams(lp);

            FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams) imgShare.getLayoutParams();
            lp1.topMargin = Utils.dp2px(this,0);
            imgShare.setLayoutParams(lp1);

            FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) relativeBar.getLayoutParams();
            lp2.height = Utils.dp2px(this,44);
            relativeBar.setLayoutParams(lp2);

            RelativeLayout.LayoutParams lp3 = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
            lp3.topMargin = Utils.dp2px(this,0);
            tvTitle.setLayoutParams(lp3);

            RelativeLayout.LayoutParams lp4 = (RelativeLayout.LayoutParams) imgScrollBack.getLayoutParams();
            lp4.topMargin = Utils.dp2px(this,0);
            imgScrollBack.setLayoutParams(lp4);

            RelativeLayout.LayoutParams lp5 = (RelativeLayout.LayoutParams) imgScrollShare.getLayoutParams();
            lp5.topMargin = Utils.dp2px(this,0);
            imgScrollShare.setLayoutParams(lp5);
        }
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgBg = (ImageView) findViewById(R.id.imgBg);
        tvTagAndDate = (TextView) findViewById(R.id.tvTagAndDate);
        tvBrowseCount = (TextView) findViewById(R.id.tvBrowseCount);
        tvGoodTitle = (TextView) findViewById(R.id.tvGoodTitle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvShopName = (TextView) findViewById(R.id.tvShopName);
        tvShopAddress = (TextView) findViewById(R.id.tvShopAddress);
        imgPhoneCall = (ImageView) findViewById(R.id.imgPhoneCall);
        webView = (WebView) findViewById(R.id.webView);
        scrollView = (MyScrollViewForWeb) findViewById(R.id.scrollView);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgShare = (ImageView) findViewById(R.id.imgShare);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        imgScrollBack = (ImageView) findViewById(R.id.imgScrollBack);
        imgScrollShare = (ImageView) findViewById(R.id.imgScrollShare);
        relativeBar = (RelativeLayout) findViewById(R.id.relativeBar);
        tvGroupPrice = (TextView) findViewById(R.id.tvGroupPrice);
        tvOriPrice = (TextView) findViewById(R.id.tvOriPrice);
        tvApply = (TextView) findViewById(R.id.tvApply);
        tvApplyNum = (TextView) findViewById(R.id.tvApplyNum);
        rlApply = (RelativeLayout) findViewById(R.id.rlApply);

        tvTitle.setText("优惠详情");
        imgBack.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgScrollBack.setOnClickListener(this);
        imgScrollShare.setOnClickListener(this);
        imgPhoneCall.setOnClickListener(this);
        rlApply.setOnClickListener(this);
    }

    private void init() {
        if (getIntent().hasExtra("newsInfoId")) {
            id = getIntent().getStringExtra("newsInfoId");
            getNewsInfoDetails(id);
        }

        //监听scroll滑动，控制标题隐藏或显示
        scrollView.setOnScrollChangedListener(new MyScrollViewForWeb.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView who, int x, int y, int oldx, int oldy) {
                if (y < 100) {
                    imgBack.setVisibility(View.VISIBLE);
                    imgShare.setVisibility(View.VISIBLE);
                    relativeBar.setVisibility(View.GONE);
                } else {
                    imgBack.setVisibility(View.GONE);
                    imgShare.setVisibility(View.GONE);
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
        getNewsInfoDetails(id);
    }

    @Override
    protected void onDestroy() {
        if (getIntent().hasExtra("type")){
            super.onDestroy();
            return;
        }else {
            RNCacheViewManager.onDestroy();
            RNCacheViewManager.clear();
            super.onDestroy();
        }

    }

    private void initWebView() {
        webView.getSettings()
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.clearFocus();
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
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
                webView.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
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

    //获取资讯信息详情
    private void getNewsInfoDetails(String id) {
        AroundApi.getInstance().getNewsInfoDetails(mContext, id, new ApiConfig.ApiRequestListener<NewsInfo>() {
            @Override
            public void onSuccess(String msg, NewsInfo data) {
                if (data == null) {
                    return;
                }
                newsInfo = data;

                ImageloaderUtil.displayImage(mContext, newsInfo.img, imgBg);
                tvBrowseCount.setText(newsInfo.accTimes); //商品浏览数
                tvTagAndDate.setText(newsInfo.tag + "  |  " + RelativeDateFormat.format(newsInfo.opeTime)); //日期

                tvGoodTitle.setText(newsInfo.title); //商品标题
                if (!TextUtils.isEmpty(newsInfo.described) && !newsInfo.described.equals("null")) {  //商品描述
                    tvDescription.setText(newsInfo.described);
                } else {
                    tvDescription.setVisibility(View.GONE);
                }

                webUrl = newsInfo.linkUrl;
                if (StringUtil.isURL(webUrl)) {
                    webView.loadUrl(webUrl);
                }else {
                    webView.loadData(newsInfo.detainstructions, "text/html; charset=UTF-8", null);//这种写法可以正确解码
                }

                tvShopName.setText(newsInfo.shopname);
                tvShopAddress.setText(newsInfo.address);

                tvOriPrice.setText("￥"+newsInfo.oriprice);
                tvOriPrice.getPaint().setAntiAlias(true); //抗锯齿
                tvOriPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //添加中部横线
                tvGroupPrice.setText(newsInfo.groupprice);
                tvApplyNum.setText(newsInfo.applynums+"人已报名");
                if ("1".equals(newsInfo.isgroup)) {
                    tvApply.setText("已报名");
                    rlApply.setEnabled(false);
                    rlApply.setBackgroundColor(Color.parseColor("#EEEEEE"));
                    tvApply.setTextColor(Color.parseColor("#575757"));
                    tvApplyNum.setTextColor(Color.parseColor("#575757"));
                } else {
                    if (newsInfo.groupstatus.equals("1")){
                        tvApply.setText("团购报名");
                        rlApply.setEnabled(true);
                        rlApply.setBackgroundColor(Color.parseColor("#48c299"));
                        tvApply.setTextColor(Color.parseColor("#ffffff"));
                        tvApplyNum.setTextColor(Color.parseColor("#ffffff"));
                    }else {
                        rlApply.setEnabled(false);
                        rlApply.setBackgroundColor(Color.parseColor("#EEEEEE"));
                        tvApply.setTextColor(Color.parseColor("#575757"));
                        tvApplyNum.setTextColor(Color.parseColor("#575757"));
                        if(newsInfo.groupstatus.equals("2")){
                            tvApply.setText("已开团");
                        }else {
                            tvApply.setText("已完成");
                        }
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
            case R.id.imgBack: {
                finish();
                break;
            }
            case R.id.imgScrollBack: {
                finish();
                break;
            }
            case R.id.imgShare: {
                if (newsInfo != null) {
                    getShareDialog(newsInfo.share_link,
                            newsInfo.described,
                            newsInfo.img.startsWith("http")? newsInfo.img:UserInfo.get().prefixQiNiu + newsInfo.img,
                            newsInfo.title);
                    showShareDialog();
                }
                break;
            }
            case R.id.imgScrollShare: {
                if (newsInfo != null) {
                    getShareDialog(
                            newsInfo.share_link,
                            newsInfo.described,
                            newsInfo.thumbnail.startsWith("http")? newsInfo.thumbnail:UserInfo.get().prefixQiNiu + newsInfo.thumbnail,
                            newsInfo.title);
                    showShareDialog();
                }
                break;
            }
            case R.id.imgPhoneCall: {
                if (!TextUtils.isEmpty(newsInfo.tel)) {
                    callDialog = DialogUtil.getCallDialog(mContext, newsInfo.tel.trim(), this);
                    callDialog.show();
                }
                break;
            }
            case R.id.buttonConfirm: {
                String phoneNum = newsInfo.tel.trim();

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNum));
                startActivity(intent);

                if (callDialog.isShowing()) {
                    callDialog.dismiss();
                }
                break;
            }

            case R.id.rlApply:{
                Intent intent = new Intent(this, GroupBuyActivity.class);
                intent.putExtra("result", Session.get(this).getToken());
                intent.putExtra("data", new Gson().toJson(newsInfo));
                intent.putExtra("phone",Session.get(this).getUsername());
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }
}
