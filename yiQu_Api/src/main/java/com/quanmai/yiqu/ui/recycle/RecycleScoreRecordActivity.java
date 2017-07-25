package com.quanmai.yiqu.ui.recycle;

import android.content.Context;
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
import com.quanmai.yiqu.api.vo.ScoreRecordInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.grade.ClassificationDetailsActivity;
import com.quanmai.yiqu.ui.recycle.adapter.RecycleScoreRecordAdapter;

/**
 * Created by zhanjinj on 16/5/11.
 * 回收垃圾分类得分
 */
public class RecycleScoreRecordActivity extends BaseActivity implements View.OnClickListener {
    PullToRefreshListView listView;
    LinearLayout linearLayoutNoData;
    TextView textViewNoData;
    CommonList<ScoreRecordInfo> mScoreRecordInfo;

    RecycleScoreRecordAdapter mAdapter;

    int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_score_record);
        init();
        showLoadingDialog();
        getRecycleScoreRecore(mContext, currentPage);
    }

    private void init() {
        mScoreRecordInfo=new CommonList<ScoreRecordInfo>();
        ((TextView) findViewById(R.id.tv_title)).setText("垃圾分类记录");
        ((TextView) findViewById(R.id.tv_right)).setText("得分说明");

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_right).setOnClickListener(this);

        linearLayoutNoData = (LinearLayout) findViewById(R.id.linearLayoutNoData);
        textViewNoData = (TextView) findViewById(R.id.textViewNoData);
        textViewNoData.setText("暂无评分记录");

        mAdapter = new RecycleScoreRecordAdapter(mContext);
        listView = (PullToRefreshListView) findViewById(R.id.listView);
        listView.setEmptyView(linearLayoutNoData);
        listView.setAdapter(mAdapter);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 0;
                getRecycleScoreRecore(mContext, currentPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getRecycleScoreRecore(mContext, currentPage);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(mContext, ClassificationDetailsActivity.class);
                intent.putExtra("Info",mScoreRecordInfo.get(position-1));
                startActivity(intent);
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
            case R.id.tv_right: {
                startActivity(RecycleScoreExplainActivity.class);
                break;
            }
        }
    }


    /**
     * 获取垃圾分类得分记录
     */
    private void getRecycleScoreRecore(Context context, final int page) {
        GradeApi.get().getScoreRecord(context, page, getIntent().getStringExtra("code"),new ApiConfig.ApiRequestListener<CommonList<ScoreRecordInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<ScoreRecordInfo> data) {
                dismissLoadingDialog();
                listView.onRefreshComplete();
                mScoreRecordInfo.addAll(data);

                if (page == 0) {
                    mAdapter.clean();
                }
                mAdapter.add(data);

                if (mAdapter.getCount() <= 0) {
                    textViewNoData.setText("暂无评分记录");
                }

                if (data.max_page > page) {
                    listView.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }

                currentPage = data.current_page + 1;
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
                listView.onRefreshComplete();
            }
        });
    }


}
