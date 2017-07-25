package com.quanmai.yiqu.ui.grade;

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
import com.quanmai.yiqu.api.GradeApi;
import com.quanmai.yiqu.api.vo.GradeScoreInfo;
import com.quanmai.yiqu.api.vo.ScoreCommDetailInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.grade.adapter.GradeScoreAdapter;

/**
 * 设备or清运评分记录
 */
public class GradeScoreActivity extends BaseActivity implements View.OnClickListener {
    public static final int TYPE_EQUIPMENT_SCORE_RECORD = 105;         //设施评分记录
    public static final int TYPE_CLEAN_SCORE_RECORD = 106;             //清运评分记录

    private TextView tv_title;
    private TextView tvCommunity;
    private PullToRefreshListView listView;

    private int type;                         //页面类型（设备或者清运）
    private ScoreCommDetailInfo mCommInfo;    //巡检-选择小区-小区详细信息
    private TextView textViewNoData;
    private LinearLayout linearLayoutNoData;

    private int currentPage = 0;              //当前页码
    private GradeScoreAdapter mAdapter;     //列表适配器

    private String amenityid;               //设施DI或者设施点ID，设备或清运评分跳转到此页面使用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_score);
        initView();
        init();
        initListView();

        getGradeScoreListInfo(amenityid);
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tvCommunity = (TextView) findViewById(R.id.tvCommunity);
        listView = (PullToRefreshListView) findViewById(R.id.listView);
        textViewNoData = (TextView) findViewById(R.id.textViewNoData);
        linearLayoutNoData = (LinearLayout) findViewById(R.id.linearLayoutNoData);
        textViewNoData.setText("暂没评分记录");
        listView.setEmptyView(linearLayoutNoData);
    }

    private void init() {
        type = getIntent().getIntExtra("ScoreType", TYPE_EQUIPMENT_SCORE_RECORD);
        if (TYPE_EQUIPMENT_SCORE_RECORD == type) {
            tv_title.setText("设备评分记录");
        } else {
            tv_title.setText("清运评分记录");
        }

        mCommInfo = (ScoreCommDetailInfo) getIntent().getSerializableExtra("CommInfo");
        tvCommunity.setText(mCommInfo.getCommname());

        if (getIntent().hasExtra("amenityid")) {
            amenityid = getIntent().getStringExtra("amenityid");
        } else {
            amenityid = "";
        }
    }

    private void initListView() {
        mAdapter = new GradeScoreAdapter(mContext, type);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                GradeScoreInfo info = mAdapter.getItem(position);
                intent.setClass(mContext, GradeScoreDetailActivity.class);
                intent.putExtra("GradeScoreInfo", info);
                startActivity(intent);
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
    }

    //刷新页面
    private void refreshData() {
        currentPage = 0;
        getGradeScoreListInfo(amenityid);
    }

    //打分记录查询
    private void getGradeScoreListInfo(String amenityid) {
        showLoadingDialog();
        GradeApi.get().gradeScore(mContext, mCommInfo.getCommcode(), type == TYPE_EQUIPMENT_SCORE_RECORD ? 1 : 2, currentPage, amenityid,
                new ApiConfig.ApiRequestListener<CommonList<GradeScoreInfo>>() {
                    @Override
                    public void onSuccess(String msg, CommonList<GradeScoreInfo> data) {
                        dismissLoadingDialog();
                        listView.onRefreshComplete();
                        if (data == null) {
                            return;
                        }

                        if (currentPage == 0) {
                            mAdapter.clear();
                        }
                        currentPage++;

                        if (currentPage < data.max_page) {
                            listView.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }

                        mAdapter.add(data);
                        if (mAdapter.getCount() == 0) {
                            textViewNoData.setText("暂没评分记录");
                        }
                        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();
                        listView.onRefreshComplete();
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
