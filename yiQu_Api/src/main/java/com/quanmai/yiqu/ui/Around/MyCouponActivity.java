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
import com.quanmai.yiqu.api.vo.ShopInfo;
import com.quanmai.yiqu.base.BaseFragmentActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.widget.CustomDialog;
import com.quanmai.yiqu.ui.Around.adapter.MyCouponListAdapter;

import java.util.List;

public class MyCouponActivity extends BaseFragmentActivity implements View.OnClickListener {
    public static int REQUEST_CODE_MY_COUPON = 101;

    private PullToRefreshListView mListView;
    private LinearLayout linearLayoutNoData;
    private TextView textViewNoData;

    MyCouponListAdapter mAdapter;
    int page = 0;
    CustomDialog mCustomDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);
        ((TextView) findViewById(R.id.tv_title)).setText("我的优惠券");
        initView();
        init();
    }

    private void initView() {
        this.linearLayoutNoData = (LinearLayout) findViewById(R.id.linearLayoutNoData);
        linearLayoutNoData.setVisibility(View.GONE);
        this.textViewNoData = (TextView) findViewById(R.id.textViewNoData);
        textViewNoData.setText("暂无内容");

        this.mListView = (PullToRefreshListView) findViewById(R.id.list);
        mListView.setEmptyView(linearLayoutNoData);

        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    private void refresh() {
        page = 0;
        getCollectedShopList();
        GetMyShakeCouponList();
    }

    private void init() {
        mAdapter = new MyCouponListAdapter(mContext);
        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                GetMyShakeCouponList();
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    CouponInfo info = (CouponInfo) mAdapter.getItem(position);
                    showDeleteDialog(info);
                } catch (Exception e) {
//                    e.printStackTrace();
                }
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    CouponInfo info = (CouponInfo) mAdapter.getItem(position);
                    Intent intent = new Intent(mContext, ShowCouponDirectlyActivity.class);
                    intent.putExtra("type","shake");
                    intent.putExtra("info", info.id);
                    startActivity(intent);
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });

        showLoadingDialog();
        getCollectedShopList();
        GetMyShakeCouponList();
    }

    //获取收藏优惠券商户列表
    private void getCollectedShopList() {
        AroundApi.getInstance().getCollectedShopList(mContext, new ApiConfig.ApiRequestListener<List<ShopInfo>>() {
            @Override
            public void onSuccess(String msg, List<ShopInfo> shopInfoList) {
                dismissLoadingDialog();

                mListView.onRefreshComplete();
                mAdapter.clearShopList();
                mAdapter.addShopList(shopInfoList);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //获取优惠券列表
    private void GetMyShakeCouponList() {
        AroundApi.getInstance().getMyShakeCouponList(mContext, page, new ApiConfig.ApiRequestListener<CommonList<CouponInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<CouponInfo> data) {
                dismissLoadingDialog();
                mListView.onRefreshComplete();
                if (page == 0) {
                    mAdapter.clearCouponList();
                }
                mAdapter.addCouponList(data);

                if (data.max_page > data.current_page) {
                    mListView.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    if (mAdapter.getCount() > 0) {
                        Utils.showCustomToast(mContext, "已到最后");
                    }
                    mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
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
    private void deleteMyCoupon(final CouponInfo info) {
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

    private void showDeleteDialog(final CouponInfo info) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_MY_COUPON) {
            refresh();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
        }

    }


}
