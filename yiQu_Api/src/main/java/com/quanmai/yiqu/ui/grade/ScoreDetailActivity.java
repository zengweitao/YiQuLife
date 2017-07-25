package com.quanmai.yiqu.ui.grade;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GradeApi;
import com.quanmai.yiqu.api.vo.ScoreRecordInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.widget.DateSelectionDialog;
import com.quanmai.yiqu.ui.grade.adapter.GradeAdapter;

/**
 * 评分明细页面
 */
public class ScoreDetailActivity extends BaseActivity {

    LinearLayout linearLayoutDate;
    TextView textViewDate;
    TextView textViewCount;
    TextView iv_no_data;
    PullToRefreshListView mListView;
    TextView tv_title;

    GradeAdapter adapter;
    String datetime = new String();
    int page = 0;
    private DateSelectionDialog dateSelectionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        datetime = getIntent().getStringExtra("datetime");
        if (datetime != null) {
            Refresh();
        }
        init();
        linearLayoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate();
            }
        });
    }

    private void init() {
        linearLayoutDate = (LinearLayout) findViewById(R.id.linearLayoutDate);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewCount = (TextView) findViewById(R.id.textViewCount);
        iv_no_data = (TextView) findViewById(R.id.iv_no_data);
        mListView = (PullToRefreshListView) findViewById(R.id.list);
        tv_title = (TextView) findViewById(R.id.tv_title);

        tv_title.setText("评分明细");
        textViewDate.setText(datetime);
        adapter = new GradeAdapter(mContext);
        adapter.addTime(datetime);
        mListView.setAdapter(adapter);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                Refresh();
            }

            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                getListData();
            }
        });
        mListView.setEmptyView(iv_no_data);
    }

    /**
     * 选择生日的Dialog
     */
    private void chooseDate() {
        if (dateSelectionDialog == null) {
            dateSelectionDialog = new DateSelectionDialog(mContext,
                    new DateSelectionDialog.OnDateTimeSetListener() {

                        @Override
                        public void onDateTimeSet(int year, int monthOfYear,
                                                  int day) {
                            String mMonth = "", mDay = "";
                            if (monthOfYear < 10) {
                                mMonth = "0" + monthOfYear;
                            } else {
                                mMonth = monthOfYear + "";
                            }

                            if (day < 10) {
                                mDay = "0" + day;
                            } else {
                                mDay = day + "";
                            }
                            textViewDate.setText(year + "." + mMonth + "." + mDay);
                            datetime = year + "-" + mMonth + "-" + mDay;
                            adapter.addTime(datetime);
                            Refresh();
                        }

                    });
        }
        dateSelectionDialog.show();
    }

    private void Refresh() {
        page = 0;
        getListData();
    }

    private void getListData() {
        showLoadingDialog("数据加载中...");
        GradeApi.get().ScroeRecord(mContext, page, datetime,
                new ApiConfig.ApiRequestListener<CommonList<ScoreRecordInfo>>() {

                    @Override
                    public void onSuccess(String msg,
                                          CommonList<ScoreRecordInfo> data) {
                        dismissLoadingDialog();
                        textViewCount.setText(String.valueOf(data.total));
                        mListView.onRefreshComplete();
                        if (page == 0) {
                            adapter.clear();
                        }
                        adapter.add(data);
                        if (adapter.getCount() == 0) {
                            iv_no_data.setText("暂无数据");
                        }

                        if (data.max_page > data.current_page) {
                            mListView.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                        page = data.current_page + 1;

                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();
                        mListView.onRefreshComplete();
                        if (adapter.getCount() == 0) {
                            iv_no_data.setText(msg);
                        } else {
                            showCustomToast(msg);
                        }

                    }
                });
    }
}
