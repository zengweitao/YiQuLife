package com.quanmai.yiqu.ui.booking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.BookingApi;
import com.quanmai.yiqu.api.vo.BookingDetailInfo;
import com.quanmai.yiqu.api.vo.GarbageDetailsInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.adapter.BookingStatusAdapter;
import com.quanmai.yiqu.ui.classifigarbage.ClassifyGarbageActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BookingStatusRecordActivity extends BaseActivity implements View.OnClickListener {

    TextView textViewGrabageRecycle,textViewClassify;
    TextView tv_right;
    //预约状态
    RelativeLayout relativeStatusContent;
    ListView listView;
    LinearLayout linear_no_data;
    LinearLayout linearLayoutContent;
    LinearLayout linearLayoutEditAmount;
    LinearLayout linearLayoutCancel;
    RelativeLayout relativeContent;
    //分类知识
    FrameLayout frameLayoutContent;
    WebView webView;
    LinearLayout linearNoData;

    String orderId;
    String predictPonits;
    BookingDetailInfo mInfo;   //预约信息列表
    final int UPDATE_INFO = 201;
    List<String> statusList;
    List<String> timeList;
    BookingStatusAdapter mAdapter;

    public static boolean isForeground = false;
    String []shareUrl;
    String weburl; //垃圾分类网页链接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_status_record);

        if (getIntent().hasExtra("orderId")){
            orderId = getIntent().getStringExtra("orderId");
        }

        init();
        initSetting();
        if(!TextUtils.isEmpty(orderId)){
            GetGarbageStatus(orderId);
        }else {
            linear_no_data.setVisibility(View.VISIBLE);
            relativeContent.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.hasExtra("orderId")){
            if (intent.hasExtra("status")){
                Intent i = new Intent(this,RecycleSuccessActivity.class);
                i.putExtra("status","completed");
                i.putExtra("orderId",intent.getStringExtra("orderId"));
                startActivity(i);
                this.finish();
            }else {
                orderId = intent.getStringExtra("orderId");
                GetGarbageStatus(orderId);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }


    //初始化
    private void init() {
        textViewGrabageRecycle = (TextView)findViewById(R.id.textViewGrabageRecycle);
        textViewClassify = (TextView)findViewById(R.id.textViewClassify);
        tv_right = (TextView)findViewById(R.id.tv_right);
        tv_right.setText("预约记录");
        //预约状态
        relativeStatusContent = (RelativeLayout)findViewById(R.id.relativeStatusContent);
        listView = (ListView)findViewById(R.id.listView);
        linear_no_data = (LinearLayout)findViewById(R.id.linear_no_data);
        linearLayoutContent = (LinearLayout)findViewById(R.id.linearLayoutContent);
        linearLayoutEditAmount = (LinearLayout)findViewById(R.id.linearLayoutEditAmount);
        linearLayoutCancel = (LinearLayout)findViewById(R.id.linearLayoutCancel);
        relativeContent = (RelativeLayout)findViewById(R.id.relativeContent);
        //分类知识
        frameLayoutContent = (FrameLayout)findViewById(R.id.frameLayoutContent);
        webView = (WebView)findViewById(R.id.webView);
        linearNoData = (LinearLayout)findViewById(R.id.linearNoData);

        textViewGrabageRecycle.setOnClickListener(this);
        textViewClassify.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        linearLayoutEditAmount.setOnClickListener(this);
        linearLayoutCancel.setOnClickListener(this);

        statusList = new ArrayList<>();
        timeList = new ArrayList<>();
        mAdapter = new BookingStatusAdapter(mContext);
        listView.setAdapter(mAdapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //预约状态
            case R.id.textViewGrabageRecycle:{
                textViewGrabageRecycle.setBackgroundResource(R.drawable.bg_booking_left);
                textViewGrabageRecycle.setTextColor(getResources().getColor(R.color.theme));
                textViewClassify.setBackgroundResource(R.drawable.bg_booking_right_fill);
                textViewClassify.setTextColor(Color.parseColor("#ffffff"));
                relativeStatusContent.setVisibility(View.VISIBLE);
                frameLayoutContent.setVisibility(View.GONE);

                if(!TextUtils.isEmpty(orderId)){
                    GetGarbageStatus(orderId);
                }else {
                    linear_no_data.setVisibility(View.VISIBLE);
                    relativeContent.setVisibility(View.GONE);
                }
                break;
            }
            //分类知识
            case R.id.textViewClassify:{
                textViewGrabageRecycle.setBackgroundResource(R.drawable.bg_booking_left_fill);
                textViewGrabageRecycle.setTextColor(Color.parseColor("#ffffff"));
                textViewClassify.setBackgroundResource(R.drawable.bg_booking_right);
                textViewClassify.setTextColor(getResources().getColor(R.color.theme));
                relativeStatusContent.setVisibility(View.GONE);
                frameLayoutContent.setVisibility(View.VISIBLE);

                weburl = ApiConfig.CLSSSIFIGARBAGE_URL+"/yiqu/garbage/garbage.html";
                webView.loadUrl(weburl);
                break;
            }
            //预约记录
            case R.id.tv_right:{
                startActivity(RecycleRecordActivity.class);
                break;
            }
            //修改数量
            case R.id.linearLayoutEditAmount:{
                Intent intent = new Intent(this,EditAmountActivity.class);
                intent.putExtra("points",predictPonits);
                intent.putExtra("info",mInfo);
                intent.putExtra("orderId",orderId);
                startActivityForResult(intent,201);
                break;
            }
            //取消预约
            case R.id.linearLayoutCancel:{
                cancelBooking(orderId,mInfo.recycleId);
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==UPDATE_INFO){
            mInfo = (BookingDetailInfo)data.getSerializableExtra("info");
            predictPonits = mInfo.point;
            bookingDetails(mInfo);
        }
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
                Intent intent = new Intent(BookingStatusRecordActivity.this, ClassifyGarbageActivity.class);
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

    }

    //获取预约详情
    private void GetGarbageStatus (String id){
        showLoadingDialog();
        BookingApi.get().GetGarbageStatus(mContext, id, new ApiConfig.ApiRequestListener<BookingDetailInfo>() {
            @Override
            public void onSuccess(String msg, BookingDetailInfo info) {
                mInfo = info;
                predictPonits = info.point;
                bookingDetails(info);
                searchBookingStatus(orderId);
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
                linear_no_data.setVisibility(View.VISIBLE);
                relativeContent.setVisibility(View.GONE);
                dismissLoadingDialog();
            }
        });
    }
    //添加预约详情
    private void bookingDetails(BookingDetailInfo info){
        CommonList<GarbageDetailsInfo> mGarbageDetails = info.garbageDetailsInfos;
        linearLayoutContent.removeAllViews();
        for (int i=0;i<mGarbageDetails.size();i++){
            GarbageDetailsInfo  garbageDetailsInfo = mGarbageDetails.get(i);

            View detailsView = LayoutInflater.from(this).inflate(R.layout.item_booking_details,null);
            TextView title = (TextView)detailsView.findViewById(R.id.textViewGarbage);
            TextView quantity = (TextView)detailsView.findViewById(R.id.textViewQuantity) ;

            title.setText(garbageDetailsInfo.garbage);
            quantity.setText(garbageDetailsInfo.quantity+"个");
            linearLayoutContent.addView(detailsView);
        }
        View view = new View(this);
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        viewParams.setMargins(0,Utils.dp2px(this,5),0,0);
        view.setLayoutParams(viewParams);
        view.setBackgroundColor(Color.parseColor("#cdcdcd"));
        linearLayoutContent.addView(view);

        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, Utils.dp2px(this,10),0,0);
        textView.setLayoutParams(params);
        textView.setTextSize(14);
        textView.setTextColor(Color.parseColor("#575757"));
        textView.setText("收货信息："+info.publisher+" "+info.mobile+" "+mSession.getBookingCommunity()+" "+info.address);
        linearLayoutContent.addView(textView);
    }


    //取消预约
    private void cancelBooking(String id,String recycleId){
        showLoadingDialog();
        BookingApi.get().CancelBookings(mContext, id,recycleId, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast("已取消");
                finish();
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    //订单状态查询
    private void searchBookingStatus(String orderId){
        statusList.clear();
        timeList.clear();
        mAdapter.clear();
        BookingApi.get().SearchBookingStatus(mContext, orderId, new ApiConfig.ApiRequestListener<Map<String, String>>() {
            @Override
            public void onSuccess(String msg, Map<String,String> data) {
                dismissLoadingDialog();
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    if (entry.getKey().equals("initial")){
                        if (statusList.size()>0){
                            statusList.remove(0);
                            statusList.add(0,"commit");  //已提交
                            statusList.remove(1);
                            statusList.add(1,"initial");  //等待回收
                        }else {
                            statusList.add(0,"commit");  //已提交
                            statusList.add(1,"initial");  //等待回收
                        }
                       if (timeList.size()>0){
                           timeList.remove(0);
                           timeList.add(0,entry.getValue());
                           timeList.remove(1);
                           timeList.add(1,entry.getValue());
                       }else {
                           timeList.add(0,entry.getValue());
                           timeList.add(1,entry.getValue());
                       }
                    }

                    if (entry.getKey().equals("verified")){
                        if (statusList==null||statusList.size()!=2){
                            statusList.add(0,"");
                            statusList.add(1,"");
                            timeList.add(0,"");
                            timeList.add(1,"");
                        }
                        statusList.add(2,"verified");
                        timeList.add(2,entry.getValue());
                    }
                }

                if (mInfo.rangeTime.equals("全天")){
                    if (Utils.isOnTime(mInfo.rangeDate)){
                        if (statusList.size()>2){
                            statusList.add(3,"standby");
                            timeList.add(3,getDate());
                        }
                    }
                }else {
                    if (Utils.isOnTime(mInfo.rangeDate)){
                        if (statusList.size()>2){
                            statusList.add(3,"standby");
                            timeList.add(3,getDate());
                        }
                    }
                }

                mAdapter.addInfo(mInfo);
                mAdapter.addData(timeList,statusList);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
                linear_no_data.setVisibility(View.VISIBLE);
                relativeContent.setVisibility(View.GONE);
            }
        });
    }

    private String getDate(){
        String str = "";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate  =   new Date(System.currentTimeMillis());//获取当前时间
        str = simpleDateFormat.format(curDate);
        return str;
    }



}
