package com.quanmai.yiqu.ui.Around;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.quanmai.yiqu.ui.Around.adapter.CommonCouponListAdapter;

/**
 * 优惠券列表
 */
public class CouponListActivity extends BaseActivity implements View.OnClickListener {

    private PullToRefreshListView list;
    private LinearLayout linearLayoutNoData;
    private TextView textViewNoData;
    private TextView tvTitle;

    int currentPage = 0;
    int couponListType; //优惠券列表类型 0普通商家优惠券列表 1个人收藏优惠券列表
    String dialogType;  //show 取消收藏需要弹窗
    String shopId;
    String shopName;
    CommonList<CouponInfo> mCouponInfos;
    CommonCouponListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);
        initView();
        init();
    }

    private void initView() {
        this.tvTitle = (TextView) findViewById(R.id.tv_title);
        this.list = (PullToRefreshListView) findViewById(R.id.list);
        this.linearLayoutNoData = (LinearLayout) findViewById(R.id.linearLayoutNoData);
        this.linearLayoutNoData.setVisibility(View.GONE);
        this.textViewNoData = (TextView) findViewById(R.id.textViewNoData);
        this.textViewNoData.setText("暂无优惠劵");
        list.setEmptyView(linearLayoutNoData);

        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    private void init() {
        if (getIntent().hasExtra("dialogType")) {
            dialogType = getIntent().getStringExtra("dialogType");
        }
        shopId = getIntent().getStringExtra("shopId");
        shopName = getIntent().getStringExtra("shopName");
        couponListType = getIntent().getIntExtra("couponListType", 0);
        tvTitle.setText(shopName + "优惠");

        mListAdapter = new CommonCouponListAdapter(mContext, couponListType);
        list.setAdapter(mListAdapter);
        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (couponListType == 1) {
                    getCollectShopCouponList();
                } else {
                    getShopCouponList(shopId);
                }
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CouponInfo mInfo = mCouponInfos.get(position - 1);
                Intent intent = null;
                if (mInfo.flag.equals("0")) {
                    intent = new Intent(mContext, ShowCouponDirectlyActivity.class);
                    intent.putExtra("info", mInfo.id);
                } else {
                    intent = new Intent(mContext, AroundDetailsActivity.class);
                    intent.putExtra("info", mInfo.id);
                }

                if (!TextUtils.isEmpty(dialogType) && dialogType.equals("show")) {
                    intent.putExtra("dialogType", "show");
                }
                startActivity(intent);
            }
        });

        showLoadingDialog();
        list.setRefreshing();
    }

    private void refresh() {
        currentPage = 0;
        if (couponListType == 1) {
            getCollectShopCouponList();
        } else {
            getShopCouponList(shopId);
        }
    }

    //获取商家优惠券列表
    private void getShopCouponList(String shopId) {
        AroundApi.getInstance().getShopCouponList(mContext, shopId, currentPage, new ApiConfig.ApiRequestListener<CommonList<CouponInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<CouponInfo> data) {
                list.onRefreshComplete();
                dismissLoadingDialog();
                if (data == null) return;

                if (data.isEmpty()) {
                    linearLayoutNoData.setVisibility(View.VISIBLE);
                    textViewNoData.setText("暂无优惠劵");
                }

                if (currentPage == 0) {
                    mListAdapter.clear();
                    mCouponInfos = data;
                } else {
                    mCouponInfos.addAll(data);
                }
                mListAdapter.add(data);

                if (data.current_page < data.max_page) {
                    list.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    if (mListAdapter.getCount() > 0) {
                        Utils.showCustomToast(mContext, "已到最后");
                    }
                    list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                currentPage = data.current_page + 1;
            }

            @Override
            public void onFailure(String msg) {
                list.onRefreshComplete();
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //获取个人收藏的优惠券列表
    private void getCollectShopCouponList() {
        AroundApi.getInstance().getCollectShopCouponList(mContext, shopId, currentPage, new ApiConfig.ApiRequestListener<CommonList<CouponInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<CouponInfo> data) {
                list.onRefreshComplete();
                dismissLoadingDialog();
                if (data == null) return;

                if (data.isEmpty()) {
                    linearLayoutNoData.setVisibility(View.VISIBLE);
                    textViewNoData.setText("暂无优惠劵");
                }

                if (currentPage == 0) mListAdapter.clear();
                mListAdapter.add(data);

                if (data.current_page < data.max_page) {
                    list.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    if (mListAdapter.getCount() > 0) {
                        Utils.showCustomToast(mContext, "已到最后");
                    }
                    list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                currentPage = data.current_page + 1;
                mCouponInfos = data;
            }

            @Override
            public void onFailure(String msg) {
                list.onRefreshComplete();
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            default:
                break;
        }
    }
}
