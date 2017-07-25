package com.quanmai.yiqu.ui.Around;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.AroundApi;
import com.quanmai.yiqu.api.vo.CouponInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.widget.CustomDialog;
import com.quanmai.yiqu.ui.adapter.CouponListAdapter;
import com.quanmai.yiqu.ui.common.WebActivity;

public class MyShakeCouponActivity extends BaseActivity {

    TextView tv_title;
    LinearLayout linear_no_data;
    PullToRefreshListView listView;
    CouponListAdapter mAdapter;

    int page = 0;
    CommonList<CouponInfo> mList;
    CustomDialog mCustomDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shake_coupon);

        init();
        showLoadingDialog();
        GetMyShakeCouponList();
    }

    private void init() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("我的优惠券");
        linear_no_data = (LinearLayout)findViewById(R.id.linear_no_data);
        listView = (PullToRefreshListView)findViewById(R.id.list);

        mList = new CommonList<>();
        listView.setEmptyView(linear_no_data);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                GetMyShakeCouponList();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CouponInfo info  = (CouponInfo)mAdapter.getItem(position);
                showDeleteDialog(info);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CouponInfo info  = (CouponInfo)mAdapter.getItem(position);
                Intent intent = new Intent(MyShakeCouponActivity.this,ShowCouponDirectlyActivity.class);
                intent.putExtra("type","shake");
                intent.putExtra("info",info.id);
                startActivity(intent);
            }
        });

        mAdapter = new CouponListAdapter(this);
        listView.setAdapter(mAdapter);
    }

    //获取优惠券列表
    private void GetMyShakeCouponList(){
        AroundApi.getInstance().getMyShakeCouponList(mContext, page, new ApiConfig.ApiRequestListener<CommonList<CouponInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<CouponInfo> data) {
                dismissLoadingDialog();
                listView.onRefreshComplete();
                if (page == 0) {
                    mAdapter.clear();
                }
                mAdapter.add(data);
                if (mAdapter.getCount() == 0) {

                }
                if (data.max_page > data.current_page) {
                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                } else {
                    if (mAdapter.getCount()>0){
                        Utils.showCustomToast(MyShakeCouponActivity.this, "已到最后");
                    }
                    listView.setMode(PullToRefreshBase.Mode.DISABLED);
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
    //删除优惠券
    private void deleteMyCoupon(final CouponInfo info){
        AroundApi.getInstance().deleteShakeCoupon(mContext, info.shopid, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                showCustomToast("删除成功");
                mAdapter.remove(info);
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    private void showDeleteDialog(final CouponInfo info){
        mCustomDialog = new CustomDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否删除？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCustomDialog.dismiss();
                        deleteMyCoupon(info);
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

}
