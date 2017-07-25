package com.quanmai.yiqu.ui.booking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.utils.L;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.BookingApi;
import com.quanmai.yiqu.api.vo.BookingDetailInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.widget.CustomDialog;
import com.quanmai.yiqu.ui.adapter.RecycleRecordsAdapter;

public class RecycleRecordActivity extends BaseActivity {

    TextView tv_title;
    PullToRefreshListView listView;
    LinearLayout linear_no_data;

    RecycleRecordsAdapter mAdapter;
    int page = 0;
    CommonList<BookingDetailInfo> mData;
    CustomDialog mCustomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_record);

        init();
        showLoadingDialog();
        searchBookingList();
    }

    //初始化
    private void init() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        listView = (PullToRefreshListView)findViewById(R.id.listView);
        linear_no_data = (LinearLayout)findViewById(R.id.linear_no_data);

        tv_title.setText("订单记录");
        listView.setEmptyView(linear_no_data);
        mData = new CommonList<>();
        mAdapter = new RecycleRecordsAdapter(mContext, new RecycleRecordsAdapter.OnDeleteRecordListener() {
            @Override
            public void deleted(BookingDetailInfo info) {
                deleteRecord(info);
            }
        });

        listView.setAdapter(mAdapter);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                searchBookingList();
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(RecycleRecordActivity.this,BookingDetailsActivity.class);
//                intent.putExtra("orderId",mData.get(position-1).id);
//                startActivity(intent);
//            }
//        });
    }

    private void Refresh(){
        page=0;
        searchBookingList();
    }

    //查询预约列表
    private void searchBookingList(){//"cancel,completed,overdue,recycle"
        BookingApi.get().SearchBookingList(mContext, page, "", new ApiConfig.ApiRequestListener<CommonList<BookingDetailInfo>>() {
            @Override
            public void onSuccess(String msg,CommonList<BookingDetailInfo> data) {
                dismissLoadingDialog();
                listView.onRefreshComplete();

                if (page == 0) {
                    mAdapter.clear();
                }
                mData = data;
                mAdapter.addData(mData);

                if (data.max_page > data.current_page) {
                    listView.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    if (mAdapter.getCount()>0){
                        Utils.showCustomToast(RecycleRecordActivity.this, "已到最后");
                    }
                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                page = data.current_page + 1;
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }
    //删除预约记录
    private void deleteRecord(final BookingDetailInfo info){
        mCustomDialog = new CustomDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否删除？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCustomDialog.dismiss();
                        deleteBookingRecord(info);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCustomDialog.dismiss();
                    }
                })
                .create();
        mCustomDialog.show();
    }

    private void deleteBookingRecord(final BookingDetailInfo info){
        showLoadingDialog();
        BookingApi.get().DeleteBookingRecord(mContext, info.id, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
//                mData.remove(info);
                mData.remove(info);
                mAdapter.removeData(info);
                showCustomToast("删除成功");
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }
}
