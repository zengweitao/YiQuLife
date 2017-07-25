package com.quanmai.yiqu.ui.booking.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.api.AddressApi;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.vo.AddressInfo;
import com.quanmai.yiqu.base.BaseFragment;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.booking.AddRecycleAddressActivity;
import com.quanmai.yiqu.ui.booking.BookingSecond2Activity;
import com.quanmai.yiqu.ui.booking.BookingSecondActivity;
import com.quanmai.yiqu.ui.classifigarbage.ClassifyGarbageActivity;

/**
 * 废品预约fragment
 * Created by James on 2016/12/29.
 */

public class BookingFragment extends BaseFragment implements View.OnClickListener {
    View mContentView;
    ScrollView scrollViewContent;
    Button buttonStart;
    FrameLayout frameLayoutContent;
    WebView webView;
    LinearLayout linear_no_data;
    ImageView imageViewContent;


    String[] shareUrl; //分享的url，截取出来匹配上即表示可分享
    String webUrl; //垃圾分类网页链接
    private final int FINISH_ADD_ADDRESS = 201; //跳转到地址选择
    public static Activity instance;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.fragment_booking, null);
        init();
        initSetting();
        //getAddressList(-1);
        webUrl = ApiConfig.CLSSSIFIGARBAGE_URL + "/yiqu/garbage/garbage.html";
        return mContentView;
    }

    //初始化
    private void init() {
        scrollViewContent = (ScrollView) mContentView.findViewById(R.id.scrollViewContent);
        buttonStart = (Button) mContentView.findViewById(R.id.buttonStart);
        frameLayoutContent = (FrameLayout) mContentView.findViewById(R.id.frameLayoutContent);
        webView = (WebView) mContentView.findViewById(R.id.webView);
        linear_no_data = (LinearLayout) mContentView.findViewById(R.id.linear_no_data);
        imageViewContent = (ImageView) mContentView.findViewById(R.id.imageViewContent);
        buttonStart.setOnClickListener(this);
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
                shareUrl = url.split("&", url.length());
                Intent intent = new Intent(mContext, ClassifyGarbageActivity.class);
                intent.putExtra("webUrl", shareUrl[0]);
                startActivity(intent);
                return true;
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

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //立即预约
            case R.id.buttonStart: {
                Intent intent=new Intent(getContext(),BookingSecond2Activity.class);
//                Bundle mBundle = new Bundle();
//                mBundle.putSerializable("address",mAddressInfo);
//                intent.putExtras(mBundle);
                startActivity(intent);
//                if (!TextUtils.isEmpty(Session.get(mContext).getBookingDetailAddress())) {
//                   startActivity(BookingSecondActivity.class);
//
//                } else {
//                    startActivity(AddRecycleAddressActivity.class);
//                }
                break;
            }
        }
    }
}
