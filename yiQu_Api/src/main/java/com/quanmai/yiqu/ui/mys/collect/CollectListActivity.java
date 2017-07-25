package com.quanmai.yiqu.ui.mys.collect;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.CollectApi;
import com.quanmai.yiqu.api.vo.CollectionGoods;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.DialogUtil.OnDialogSelectId;

/**
 * 我的收藏页面
 */
public class CollectListActivity extends BaseActivity implements OnClickListener {
    PullToRefreshListView mListView;
    LinearLayout linearLayoutNoData;
    TextView textViewNoData;

    CollectListAdapter mAdapter;
    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ((TextView) findViewById(R.id.tv_title)).setText("我的收藏");
        init();
    }

    private void init() {
        linearLayoutNoData = (LinearLayout) findViewById(R.id.linearLayoutNoData);
        textViewNoData = (TextView) findViewById(R.id.textViewNoData);
        mAdapter = new CollectListAdapter(mContext, this);

        mListView = (PullToRefreshListView) findViewById(R.id.list);
        mListView.setEmptyView(linearLayoutNoData);
        mListView.setAdapter(mAdapter);
        mListView.setRefreshing();
        mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getGoodsCollectionList();
            }
        });

        showLoadingDialog();
        Refresh();
    }

    private void Refresh() {
        page = 0;
        getGoodsCollectionList();
    }

    /**
     * 获取收藏列表
     */
    private void getGoodsCollectionList() {
        CollectApi.get().GoodsCollectionList(mContext, page,
                new ApiRequestListener<CommonList<CollectionGoods>>() {

                    @Override
                    public void onSuccess(String msg, CommonList<CollectionGoods> data) {
                        dismissLoadingDialog();

                        mListView.onRefreshComplete();
                        if (page == 0) {
                            mAdapter.clear();
                        }
                        mAdapter.add(data);
                        if (mAdapter.getCount() == 0) {
                            textViewNoData.setText("暂无收藏");
                        }
                        if (data.max_page > data.current_page) {
                            mListView.setMode(Mode.BOTH);
                        } else {
                            mListView.setMode(Mode.PULL_FROM_START);
                        }
                        page = data.current_page + 1;
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();

                        mListView.onRefreshComplete();
                        if (page == 0) {
                            mAdapter.clear();
                            mAdapter.notifyDataSetChanged();
                        }
                        if (mAdapter.getCount() == 0) {
                            textViewNoData.setText(msg);
                        } else {
                            showCustomToast(msg);
                        }
                    }
                });
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            default: {
                DialogUtil.showDeleteDialog(mContext, "确认取消收藏？", new OnDialogSelectId() {
                    @Override
                    public void onClick(View view) {
                        showLoadingDialog("请稍候");
                        Api.get().GoodsCollectionCancel(mContext, (String) v.getTag(),
                                new ApiRequestListener<String>() {
                                    @Override
                                    public void onSuccess(String msg, String data) {
                                        dismissLoadingDialog();
                                        showCustomToast(msg);
                                        Refresh();
                                    }

                                    @Override
                                    public void onFailure(String msg) {
                                        dismissLoadingDialog();
                                        showCustomToast(msg);
                                    }
                                });
                    }
                });
                break;
            }
        }
    }
}
