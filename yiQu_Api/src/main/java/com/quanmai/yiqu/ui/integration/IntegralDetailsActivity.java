package com.quanmai.yiqu.ui.integration;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.IntegralApi;
import com.quanmai.yiqu.api.vo.ScoreMonthRecord;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.adapter.IntegralDetailsAdapter;

public class IntegralDetailsActivity extends BaseActivity {

    TextView tv_title,textview_details;
    LinearLayout linear_no_data;
    PullToRefreshListView list;
    IntegralDetailsAdapter mAdapter;

    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_details);
        showLoadingDialog("正在加载数据...");
        init();
    }

    private void init() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        linear_no_data = (LinearLayout)findViewById(R.id.linear_no_data);
        list = (PullToRefreshListView)findViewById(R.id.list);
        textview_details= (TextView) findViewById(R.id.textview_details);

        if (getIntent().getIntExtra("point_type",0)==2){
            tv_title.setText("积分详情");
            textview_details.setText("积分值");
        }else if (getIntent().getIntExtra("point_type",0)==3){
            tv_title.setText("福袋详情");
            textview_details.setText("福袋数");
        }else if (getIntent().getIntExtra("point_type",0)==1){
            tv_title.setText("益币详情");
            textview_details.setText("益币数");
        }

        list.setEmptyView(linear_no_data);

        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                GetIntegralList();
            }
        });

        mAdapter = new IntegralDetailsAdapter(mContext,getIntent().getIntExtra("point_type",0));
        list.setAdapter(mAdapter);
        list.setRefreshing();
    }


    private void Refresh() {
        page = 0;
        GetIntegralList();
    }

    private void GetIntegralList() {
        IntegralApi.get().ScoreRecord(mContext, page, getIntent().getIntExtra("point_type",0) ,getIntent().getStringExtra("code"), new ApiConfig.ApiRequestListener<CommonList<ScoreMonthRecord>>() {
            @Override
            public void onSuccess(String msg, CommonList<ScoreMonthRecord> data) {
                dismissLoadingDialog();
                list.onRefreshComplete();
                if (page == 0) {
                    mAdapter.clear();
                }
                mAdapter.add(data);
                if (mAdapter.getCount() == 0) {

                }
                if (data.max_page > data.current_page) {
                    list.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    if (mAdapter.getCount()>0){
                        Utils.showCustomToast(IntegralDetailsActivity.this, "已到最后");
                    }
                    list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                page = data.current_page + 1;
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
                list.onRefreshComplete();
                if (page == 0) {
                    mAdapter.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
