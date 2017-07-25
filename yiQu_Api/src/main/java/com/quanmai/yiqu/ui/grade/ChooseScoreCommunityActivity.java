package com.quanmai.yiqu.ui.grade;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GradeApi;
import com.quanmai.yiqu.api.vo.ScoreCommInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.grade.adapter.CommunityChooseAdapter;

public class ChooseScoreCommunityActivity extends BaseActivity {

    TextView tv_title;
    PullToRefreshListView listView;
    CommunityChooseAdapter mAdapter;

    public static final int TYPE_CLASSIFICATION_SCORE = 101;    //分类评分
    public static final int TYPE_EQUIPMENT_SCORE = 102;         //设施评分
    public static final int TYPE_CLEAN_SCORE = 103;             //清运评分
    public static final int TYPE_PUBLISH_SCORE = 104;           //宣传评分
    public static final int TYPE_EQUIPMENT_SCORE_RECORD = 105;  //设施评分记录
    public static final int TYPE_CLEAN_SCORE_RECORD = 106;      //清运评分记录

    int scoreTyp;  //跳转类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_score_community);

        scoreTyp = getIntent().getIntExtra("ScoreType", 0);
        initView();
        GetComm();

    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("选择小区");
        listView = (PullToRefreshListView) findViewById(R.id.listView);

        mAdapter = new CommunityChooseAdapter(this, scoreTyp);

        listView.setAdapter(mAdapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                GetComm();
            }
        });
    }

    /**
     * 获取小区列表
     */
    private void GetComm() {
        GradeApi.get().ChooseScoreComm(mContext, new ApiConfig.ApiRequestListener<CommonList<ScoreCommInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<ScoreCommInfo> data) {
                listView.onRefreshComplete();
                mAdapter.clear();
                mAdapter.add(data);
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

}
