package com.quanmai.yiqu.ui.mys.publishmanage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.GoodsApi;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.widget.CustomDialog;
import com.quanmai.yiqu.share.ShareActivity;
import com.quanmai.yiqu.ui.publish.PublishUnusedActivity;

/**
 * 发布管理
 */
public class PublishManageActivity extends ShareActivity implements OnClickListener {
    public static String ACTION_REFRESH_DATA = "action_refresh_data";

    PublishManageAdapter mAdapter;
    PullToRefreshListView mListView;
    LinearLayout linearLayoutNoData;
    TextView textViewNoData;
    int page = 0;

    LocalBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_manage);
        ((TextView) findViewById(R.id.tv_title)).setText("我的发布");

        init();
        showLoadingDialog("请稍候");
        refresh();
    }

    private void init() {
        ((TextView) findViewById(R.id.tv_right)).setText("发布物品");

        linearLayoutNoData = (LinearLayout) findViewById(R.id.linearLayoutNoData);
        linearLayoutNoData.setVisibility(View.GONE);
        textViewNoData = (TextView) findViewById(R.id.textViewNoData);
        textViewNoData.setText("还没有发布内容");
        mListView = (PullToRefreshListView) findViewById(R.id.list);

        mAdapter = new PublishManageAdapter(this, new PublishManageAdapter.PublishManagerListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mListView.setEmptyView(linearLayoutNoData);
        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getGoodsManageList();
            }
        });

        findViewById(R.id.tv_right).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_right).setOnClickListener(this);

        mBroadcastReceiver = new LocalBroadcastReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver, new IntentFilter(ACTION_REFRESH_DATA));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBroadcastReceiver);
    }

    private void refresh() {
        page = 0;
        getGoodsManageList();
    }

    private void getGoodsManageList() {
        GoodsApi.get().GoodsManageList(mContext, true, page,
                new ApiRequestListener<CommonList<GoodsBasic>>() {

                    @Override
                    public void onSuccess(String msg, CommonList<GoodsBasic> data) {
                        dismissLoadingDialog();

                        mListView.onRefreshComplete();
                        if (page == 0) {
                            mAdapter.clear();
                        }
                        mAdapter.add(data);
                        if (mAdapter.getCount() == 0) {
                            textViewNoData.setText("还没有发布内容");
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
                        showCustomToast(msg);
                        if (page == 0) {
                            mAdapter.clear();
                            mAdapter.notifyDataSetChanged();
                        }
                        if (mAdapter.getCount() == 0) {
                            textViewNoData.setText(msg);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                if (UserInfo.get().getIsbind().equals("0")) {
                    CustomDialog dialog = new CustomDialog.Builder(PublishManageActivity.this)
                            .setTitle("提示")
                            .setMessage("由于发布的信息仅限本小区可见，请您先在本小区设备上扫码取袋，绑定设备后再发布。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();

                    dialog.show();
                } else {
                    startActivity(PublishUnusedActivity.class);
                }
                break;

            default:
                break;
        }
    }

    class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_REFRESH_DATA.equals(intent.getAction())) {
                showLoadingDialog();
                refresh();
            }
        }
    }
}
