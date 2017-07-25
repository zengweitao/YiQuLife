package com.quanmai.yiqu.ui.recycle.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.RecycleApi;
import com.quanmai.yiqu.api.vo.RecycleOrderInfo;
import com.quanmai.yiqu.base.BaseFragment;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.recycle.OnRefreshListener;
import com.quanmai.yiqu.ui.recycle.adapter.OrderFinishedAdapter;

/**
 * Created by zhanjinj on 16/4/18.
 * 回收人员回收订单列表（已完成）
 */
@SuppressLint("ValidFragment")
public class RecycleOrderFinishedFragment extends BaseFragment {
    public static String ACTION_NETWORKING_TO_REFRESH_FINISH = "action_networking_to_refresh_finish";

    View mView;
    PullToRefreshListView listViewOrder;
    LinearLayout linearLayoutNoData;
    TextView textViewNoData;

    OrderFinishedAdapter mAdapter;
    LocalBroadcastReceiver mReceiver;
    OnRefreshListener mOnRefreshListener;
    int page = 0;

    public RecycleOrderFinishedFragment(OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
    }
    public RecycleOrderFinishedFragment() {
    }
    protected void init() {
        listViewOrder = (PullToRefreshListView) mView.findViewById(R.id.llRecycleInfo);

        linearLayoutNoData = (LinearLayout) mView.findViewById(R.id.linearLayoutNoData);
        textViewNoData = (TextView) mView.findViewById(R.id.textViewNoData);

        textViewNoData.setText("暂无完成订单");
        listViewOrder.setEmptyView(linearLayoutNoData);

        mAdapter = new OrderFinishedAdapter(getActivity());
        listViewOrder.setAdapter(mAdapter);

        listViewOrder.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mOnRefreshListener.refresh();
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getRecycleOrderList();
            }
        });

        mReceiver = new LocalBroadcastReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, new IntentFilter(ACTION_NETWORKING_TO_REFRESH_FINISH));

        showLoadingDialog();
        refresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_recycle_order, null);
        init();
        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    private void refresh() {
        page = 0;
        getRecycleOrderList();
    }

    private void getRecycleOrderList() {
        RecycleApi.get().getRecycleOrderList(getActivity(), page, "completed,recycle,cancel,overdue", new ApiConfig.ApiRequestListener<CommonList<RecycleOrderInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<RecycleOrderInfo> data) {
                dismissLoadingDialog();
                listViewOrder.onRefreshComplete();

                if (page == 0) mAdapter.clean();

                mAdapter.add(data);
                if (mAdapter.getCount() == 0) textViewNoData.setText("暂无完成订单");

                if (data.max_page > data.current_page) {
                    listViewOrder.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listViewOrder.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
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

    class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_NETWORKING_TO_REFRESH_FINISH.equals(intent.getAction())) {
                refresh();
            }
        }
    }

}
