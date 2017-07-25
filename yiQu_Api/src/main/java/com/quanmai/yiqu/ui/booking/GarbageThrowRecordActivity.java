package com.quanmai.yiqu.ui.booking;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.RecycleApi;
import com.quanmai.yiqu.api.vo.GarbageThrowRecordInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;

/**
 * 废品投放记录页面
 */
public class GarbageThrowRecordActivity extends BaseActivity {

    private ImageView iv_back;
    private TextView textViewNoData;
    private LinearLayout linearLayoutNoData;
    private PullToRefreshListView list;
    private GarbageThrowRecordAdapter mAdapter;

    private int currentPage;
    private String type="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_throw_record);

        if (getIntent().hasExtra("type")){
            type = getIntent().getStringExtra("type");
        }

        initView();
        init();

        list.setRefreshing();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("投放记录");

        iv_back = (ImageView) findViewById(R.id.iv_back);
        list = (PullToRefreshListView) findViewById(R.id.list);

        textViewNoData = (TextView) findViewById(R.id.textViewNoData);
        linearLayoutNoData = (LinearLayout) findViewById(R.id.linearLayoutNoData);
        textViewNoData = (TextView) findViewById(R.id.textViewNoData);
        textViewNoData.setText("暂无投放记录");
        list.setEmptyView(linearLayoutNoData);
        list.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private void init() {
        mAdapter = new GarbageThrowRecordAdapter(mContext,type);
        list.setAdapter(mAdapter);
        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 0;
                getThrowRecordList(currentPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getThrowRecordList(currentPage);
            }
        });
    }

    //获取垃圾投放记录
    private void getThrowRecordList(int page) {
        RecycleApi.get().getThrowRecordList(mContext,type,page,new ApiConfig.ApiRequestListener<CommonList<GarbageThrowRecordInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<GarbageThrowRecordInfo> data) {
                dismissLoadingDialog();
                if (data == null) {
                    return;
                }
                if (currentPage == 0) {
                    list.onRefreshComplete();
                    mAdapter.clear();
                }
                mAdapter.add(data);
                if (mAdapter.getCount() == 0) {
                    textViewNoData.setText("暂无投放记录");
                }
                if (data.max_page > data.current_page) {
                    list.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                currentPage = data.current_page + 1;
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }


}
