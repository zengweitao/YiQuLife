package com.quanmai.yiqu.ui.ycoin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.IntegralApi;
import com.quanmai.yiqu.api.vo.AwardRecordInfo;
import com.quanmai.yiqu.api.vo.ScoreDayRecord;
import com.quanmai.yiqu.api.vo.ScoreMonthRecord;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.adapter.FetchRecordAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通用户：益币/礼品领取记录
 * 礼品赠送人员：赠送记录
 */
public class FetchRecordActivity extends BaseActivity {

    PullToRefreshListView listView;
    TextView tv_title;
    int page = 0;
    List<ScoreDayRecord> mRecords;
    FetchRecordAdapter mAdapter;
    boolean isYCoinRecord;
    boolean isGivingRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_record);
        showLoadingDialog("正在加载数据...");
        if (getIntent().hasExtra("type") && getIntent().getStringExtra("type").equals("YCoin")) {
            isYCoinRecord = true;
        } else {
            isYCoinRecord = false;
        }

        if (getIntent().hasExtra("type") && getIntent().getStringExtra("type").equals("GivingRecord")) {
            isGivingRecord = true;
        }
        initView();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        listView = (PullToRefreshListView) findViewById(R.id.listView);

        View view = LayoutInflater.from(this).inflate(R.layout.empty_view, null);
        listView.setEmptyView(view);

        if (isGivingRecord) {
            tv_title.setText("赠送记录");
        } else if (isYCoinRecord) {
            tv_title.setText("益币领取记录");
        } else {
            tv_title.setText("礼品领取记录");
        }

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isGivingRecord) {
                    getAwardRecord("");
                } else if (isYCoinRecord) {
                    getYCoinsFetchRecord();
                } else {
                    getAwardRecord(UserInfo.get().getHouseCodeX());
                }
            }
        });

        mAdapter = new FetchRecordAdapter(mContext);
        listView.setAdapter(mAdapter);
        listView.setRefreshing();
    }

    private void Refresh() {
        page = 0;
        if (isGivingRecord) {
            getAwardRecord("");
        } else if (isYCoinRecord) {
            getYCoinsFetchRecord();
        } else {
            getAwardRecord(UserInfo.get().getHouseCodeX());
        }
    }

    /**
     * 益币领取记录了
     */
    private void getYCoinsFetchRecord() {
        IntegralApi.get().ScoreRecord(mContext, page, 1, null,new ApiConfig.ApiRequestListener<CommonList<ScoreMonthRecord>>() {
            @Override
            public void onSuccess(String msg, CommonList<ScoreMonthRecord> data) {
                dismissLoadingDialog();
                listView.onRefreshComplete();
                mRecords = new ArrayList<ScoreDayRecord>();

                for (int i = 0; i < data.size(); i++) {
                    mRecords.addAll(data.get(i).dayRecords);
                }

                if (page == 0) {
                    mAdapter.clear();
                }

                mAdapter.add(mRecords);


                if (data.max_page > data.current_page) {
                    listView.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    if (mAdapter.getCount() > 0) {
                        Utils.showCustomToast(FetchRecordActivity.this, "已到最后");
                    }
                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                page = data.current_page + 1;
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
                listView.onRefreshComplete();
                if (page == 0) {
                    mAdapter.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getAwardRecord(String houseid) {
        IntegralApi.get().getAwardRecord(this, page, houseid, new ApiConfig.ApiRequestListener<CommonList<AwardRecordInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<AwardRecordInfo> data) {
                dismissLoadingDialog();
                listView.onRefreshComplete();

                if (page==0){
                    mAdapter.clear();
                }
                mAdapter.addAwards(data);

                if (data.max_page > data.current_page) {
                    listView.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    if (mAdapter.getCount() > 0) {
                        Utils.showCustomToast(FetchRecordActivity.this, "已到最后");
                    }
                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                page = data.current_page + 1;
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
                listView.onRefreshComplete();
                if (page == 0) {
                    mAdapter.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
