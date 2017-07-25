package com.quanmai.yiqu.ui.mys.record;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.vo.ScoreRecordInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.grade.adapter.GradeAdapter;

/**
 * 积分详情
 *
 * @author
 */
public class IntegrateDetailActivity extends BaseActivity {


    TextView iv_no_data;
    GradeAdapter adapter;
    int page = 0;
    private PullToRefreshListView list;
    String starttime = new String();
    String endtime = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrate_detail);
        init();
    }

    private void init() {
        if (getIntent().hasExtra("starttime")) {
            starttime = getIntent().getStringExtra("starttime");
        }
        if (getIntent().hasExtra("endtime")) {
            endtime = getIntent().getStringExtra("endtime");
        }
        ((TextView) findViewById(R.id.tv_title)).setText("得分详情");
        iv_no_data = (TextView) findViewById(R.id.iv_no_data);
        list = (PullToRefreshListView) findViewById(R.id.list);
        adapter = new GradeAdapter(mContext);
        list.getRefreshableView().setAdapter(adapter);
        list.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                Refresh();
            }

            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                getListData();
            }
        });
        list.setEmptyView(iv_no_data);
        list.setRefreshing();
    }

    private void Refresh() {
        page = 0;
        getListData();
    }

    private void getListData() {
        Api.get().ScroeRecord(mContext, page, starttime, endtime, new ApiRequestListener<CommonList<ScoreRecordInfo>>() {

            @Override
            public void onSuccess(String msg,
                                  CommonList<ScoreRecordInfo> data) {
                dismissLoadingDialog();
                list.onRefreshComplete();
                if (page == 0) {
                    adapter.clear();
                }
                adapter.add(data);
                if (adapter.getCount() == 0) {
                    iv_no_data.setText("暂无数据");
                }

                if (data.max_page > data.current_page) {
                    list.setMode(Mode.BOTH);
                } else {
                    list.setMode(Mode.PULL_FROM_START);
                }
                page = data.current_page + 1;

            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                list.onRefreshComplete();
                if (adapter.getCount() == 0) {
                    iv_no_data.setText(msg);
                } else {
                    showCustomToast(msg);
                }

            }
        });
    }

}
