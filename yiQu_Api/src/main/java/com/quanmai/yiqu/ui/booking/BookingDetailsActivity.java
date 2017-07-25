package com.quanmai.yiqu.ui.booking;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.BookingApi;
import com.quanmai.yiqu.api.vo.BookingDetailInfo;
import com.quanmai.yiqu.api.vo.GarbageDetailsInfo;
import com.quanmai.yiqu.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

public class BookingDetailsActivity extends BaseActivity {


    TextView tv_title;
    TextView tv_right;
    ImageView imageViewStatus;
    TextView textViewStatus;
    TextView textViewStatusDescription;
    TextView textViewTime;
    TextView textViewBookingTime;
    LinearLayout linearLayoutContent;
    TextView textViewGotIntegral;
    TextView textViewTotalIntegral;
    TextView textViewDidntGetIntegral;
    TextView textViewName;
    TextView textViewPhone;
    TextView textViewAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        init();
        if (getIntent().hasExtra("BookingDetailInfo")){
            updateDetails((BookingDetailInfo) getIntent().getSerializableExtra("BookingDetailInfo"));
        }

        if (!TextUtils.isEmpty(getIntent().getStringExtra("orderId"))){
            GetGarbageStatus(getIntent().getStringExtra("orderId"));
        }

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map = new HashMap<String, String>();
                map.put("enableAudio","1");
                map.put("themeColor","#48c299");
                FeedbackAPI. setUICustomInfo(map);
                FeedbackAPI.openFeedbackActivity(BookingDetailsActivity.this);
            }
        });
    }

    //初始化
    private void init() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_right = (TextView)findViewById(R.id.tv_right);
        imageViewStatus = (ImageView) findViewById(R.id.imageViewStatus);
        textViewStatus = (TextView)findViewById(R.id.textViewStatus);
        textViewStatusDescription = (TextView)findViewById(R.id.textViewStatusDescription);
        textViewTime = (TextView)findViewById(R.id.textViewTime);
        textViewBookingTime = (TextView)findViewById(R.id.textViewBookingTime);
        linearLayoutContent = (LinearLayout) findViewById(R.id.linearLayoutContent);
        textViewGotIntegral = (TextView)findViewById(R.id.textViewGotIntegral);
        textViewTotalIntegral = (TextView)findViewById(R.id.textViewTotalIntegral);
        textViewDidntGetIntegral = (TextView)findViewById(R.id.textViewDidntGetIntegral);
        textViewName = (TextView)findViewById(R.id.textViewName);
        textViewPhone = (TextView)findViewById(R.id.textViewPhone);
        textViewAddress = (TextView)findViewById(R.id.textViewAddress);

        tv_title.setText("订单详情");
        tv_right.setText("反馈建议");
    }


    //获取预约详情
    private void GetGarbageStatus (String orderId){
        showLoadingDialog();
        BookingApi.get().GetGarbageStatus(mContext, orderId, new ApiConfig.ApiRequestListener<BookingDetailInfo>() {
            @Override
            public void onSuccess(String msg, BookingDetailInfo info) {
                dismissLoadingDialog();
                updateDetails(info);
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
                dismissLoadingDialog();
            }
        });
    }

    private void updateDetails(BookingDetailInfo info){
        if (info.statue.equals("overdue")){
            //已过期
            imageViewStatus.setBackgroundResource(R.drawable.icon_booking_details_failed);
            textViewStatus.setText("对方未回收");
            textViewStatusDescription.setText("我们仍会赠予您益币，若有疑问请反馈");
            textViewGotIntegral.setVisibility(View.VISIBLE);
            textViewTotalIntegral.setVisibility(View.VISIBLE);
            textViewDidntGetIntegral.setVisibility(View.GONE);
            textViewTotalIntegral.setText("总计   "+info.point+"益币");
        }else if (info.statue.equals("cancel")){
            //已取消
            imageViewStatus.setBackgroundResource(R.drawable.icon_booking_details_success);
            textViewStatus.setText("已取消预约");
            textViewStatusDescription.setText("有任何意见或建议欢迎联系我们");
            textViewGotIntegral.setVisibility(View.GONE);
            textViewTotalIntegral.setVisibility(View.GONE);
            textViewDidntGetIntegral.setVisibility(View.VISIBLE);
        }else if (info.statue.equals("completed")||info.statue.equals("recycle")){
            //已完成
            imageViewStatus.setBackgroundResource(R.drawable.icon_booking_details_success);
            textViewStatus.setText("废品回收完成");
            textViewStatusDescription.setText("有任何意见或建议欢迎联系我们");
            textViewGotIntegral.setVisibility(View.VISIBLE);
            textViewTotalIntegral.setVisibility(View.VISIBLE);
            textViewDidntGetIntegral.setVisibility(View.GONE);
            textViewTotalIntegral.setText("总计   "+info.point+"益币");
        }

        textViewTime.setText(info.recycleTime);
        textViewBookingTime.setText("预约时间："+info.rangeDate+" "+info.rangeTime);
        linearLayoutContent.removeAllViews();
        for (int i=0;i<info.garbageDetailsInfos.size();i++){
            GarbageDetailsInfo garbageDetailsInfo = info.garbageDetailsInfos.get(i);

            View detailsView = LayoutInflater.from(this).inflate(R.layout.item_booking_record_details,null);

            TextView tv_title = (TextView)detailsView.findViewById(R.id.textViewTitle);
            TextView tv_quantity = (TextView)detailsView.findViewById(R.id.textViewAmount) ;
            TextView tv_integral = (TextView)detailsView.findViewById(R.id.textViewIntegral) ;

            tv_title.setText(garbageDetailsInfo.garbage);
            tv_quantity.setText("x"+garbageDetailsInfo.quantity);
            if (Integer.parseInt(garbageDetailsInfo.point)>1000){
                tv_integral.setText("1000益币");
            }else {
                tv_integral.setText(garbageDetailsInfo.point+"益币");
            }

            linearLayoutContent.addView(detailsView);
        }

        textViewName.setText(info.publisher);
        textViewPhone.setText(info.mobile);
        textViewAddress.setText(mSession.getBookingCommunity()+" "+info.address);
    }

}
