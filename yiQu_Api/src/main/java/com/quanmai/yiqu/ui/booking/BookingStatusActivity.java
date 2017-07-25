package com.quanmai.yiqu.ui.booking;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.BookingApi;
import com.quanmai.yiqu.api.RecycleApi;
import com.quanmai.yiqu.api.vo.BookingDetailInfo;
import com.quanmai.yiqu.api.vo.GarbageDetailsInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.adapter.BookingStatusAdapter;
import com.quanmai.yiqu.ui.booking.Adapter.BookingPhotoRecyclerAdapter;
import com.quanmai.yiqu.ui.booking.Adapter.BookingStatusPhotoRecyclerAdapter;
import com.quanmai.yiqu.ui.recycle.fragment.RecycleOrderNoTakeFragment;
import com.quanmai.yiqu.ui.views.CustomFlowLayout;
import com.quanmai.yiqu.ui.views.CustomListView;
import com.quanmai.yiqu.ui.views.TagAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BookingStatusActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_title;
    CustomListView listView;
    LinearLayout linearLayoutContent;
    LinearLayout linear_no_data;
    LinearLayout relativeContent;
    RecyclerView recyclerview_recycle_photo;

    String orderId;
    String userId;
    String recycler;
    String predictPonits;
    BookingDetailInfo mInfo;   //预约信息列表
    final int UPDATE_INFO = 201;
    List<String> statusList;
    List<String> timeList;
    BookingStatusAdapter mAdapter;
    public static boolean isForeground = false;
    private TextView textView_order_name,textView_order_phone,textView_order_address,
            textView_order_dataAndtime,textview_cancel,textview_order;
    private CustomFlowLayout mobile_flow_layout;
    private BookingStatusPhotoRecyclerAdapter mBookingPhotoRecyclerAdapter;
    private TagAdapter mMobileTagAdapter;
    private ImageView iv_back;
    BookingDetailInfo mBookingDetailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_status);

        if (getIntent().hasExtra("recycler")){
            recycler=getIntent().getStringExtra("recycler");
        }
        if (getIntent().hasExtra("userId")){
            userId=getIntent().getStringExtra("userId");
        }
        if (getIntent().hasExtra("orderId")){
            orderId = getIntent().getStringExtra("orderId");
        }else if (getIntent().hasExtra("id")){
            orderId = getIntent().getStringExtra("id");
        }
        init();
        if(!TextUtils.isEmpty(orderId)){
            GetGarbageStatus(orderId);
        }
        if (getIntent().hasExtra("BookingDetailInfo")){
            mBookingDetailInfo = (BookingDetailInfo) getIntent().getSerializableExtra("BookingDetailInfo");
            bookingDetails(mBookingDetailInfo);
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


    private void init() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("订单状态");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        listView = (CustomListView)findViewById(R.id.listView);

        textView_order_name = (TextView) findViewById(R.id.textView_order_name);
        textView_order_phone = (TextView) findViewById(R.id.textView_order_phone);
        textView_order_address = (TextView) findViewById(R.id.textView_order_address);
        textView_order_dataAndtime = (TextView) findViewById(R.id.textView_order_dataAndtime);
        recyclerview_recycle_photo = (RecyclerView)findViewById(R.id.recyclerview_recycle_photo);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerview_recycle_photo.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        mobile_flow_layout = (CustomFlowLayout) findViewById(R.id.mobile_flow_layout);
        relativeContent = (LinearLayout)findViewById(R.id.relativeContent);
        linear_no_data = (LinearLayout)findViewById(R.id.linear_no_data);

        textview_order = (TextView)findViewById(R.id.textview_order);
        textview_cancel = (TextView)findViewById(R.id.textview_cancel);
        textview_cancel.setOnClickListener(this);
        textview_order.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        if (getIntent().hasExtra("recycler")&&recycler.equals("order")){
            textView_order_dataAndtime.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            textview_cancel.setVisibility(View.GONE);
            textview_order.setVisibility(View.VISIBLE);
        }
        statusList = new ArrayList<>();
        timeList = new ArrayList<>();
        mAdapter = new BookingStatusAdapter(mContext);
        listView.setAdapter(mAdapter);

    }

    /**
     * 添加适配器
     */
    public void addGridViewAdapter(ArrayList<String> imglist) {
        mBookingPhotoRecyclerAdapter = new BookingStatusPhotoRecyclerAdapter(this,   imglist);
        recyclerview_recycle_photo.addItemDecoration(mBookingPhotoRecyclerAdapter.getSpaceItemDecoration(30));
        recyclerview_recycle_photo.setAdapter(mBookingPhotoRecyclerAdapter);
    }

    /**
     * 刷新适配器
     */
    public void notifyGridViewAdapter() {
        if (mBookingPhotoRecyclerAdapter != null) {
            mBookingPhotoRecyclerAdapter.notifyDataSetChanged();
        }
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
        textView_order_name.setText(info.publisher);
        textView_order_phone.setText(info.mobile);
        textView_order_address.setText(info.address);
        textView_order_dataAndtime.setText(info.rangeDate+" "+info.rangeTime);
        String[] images=info.pic.split(",");
        List<String> imlist= Arrays.asList(images);
        ArrayList<String> imglist=new ArrayList<>();
        imglist.addAll(imlist);
        addGridViewAdapter(imglist);

        mMobileTagAdapter = new TagAdapter<>(this);
        mobile_flow_layout.setTagCheckedMode(CustomFlowLayout.FLOW_TAG_KEEP_BRIGHT);
        mobile_flow_layout.setAdapter(mMobileTagAdapter);
        String[] memo=info.memo.split(",");
        List<String> memos=  Arrays.asList(memo);
        mMobileTagAdapter.onlyAddAll(memos);
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

    /**
     * 回收人员接单
     *
     * @param id
     * @param userid
     */
    private void confirmAppointmentOrder(String id, String userid) {
        if(UserInfo.get().getUserid().equals(userid)){

        }
        showLoadingDialog();
        RecycleApi.get().confirmAppointmentOrder(mContext, id, userid, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast(msg);
                finish();

                /*if (mOrderInfos.size() <= 0) {
                    //接单成功，发送广播刷新“已接单”列表页面
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(RecycleOrderNoTakeFragment.ACTION_NETWORKING_TO_REFRESH_NO_TAKE));
                }*/
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode==UPDATE_INFO){
                mInfo = (BookingDetailInfo)data.getSerializableExtra("info");
                predictPonits = mInfo.point;
                bookingDetails(mInfo);
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回
            case R.id.iv_back:{
                finish();
                break;
            }
            //取消预约
            case R.id.textview_cancel:{
                if (getIntent().hasExtra("BookingDetailInfo")){
                    cancelBooking(mBookingDetailInfo.id,mBookingDetailInfo.recycleId);
                }else {
                    cancelBooking(orderId,mInfo.recycleId);
                }
                break;
            }
            //接单
            case R.id.textview_order:{
                confirmAppointmentOrder(orderId,userId);
                break;
            }
            default:
                break;
        }
    }

    private String getDate(){
        String str = "";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        str = simpleDateFormat.format(date);
        return str;
    }
}
