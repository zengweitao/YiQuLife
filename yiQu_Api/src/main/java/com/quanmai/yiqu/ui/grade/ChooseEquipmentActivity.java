package com.quanmai.yiqu.ui.grade;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GradeApi;
import com.quanmai.yiqu.api.vo.ChooseEqInfo;
import com.quanmai.yiqu.api.vo.ScoreCommDetailInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.grade.adapter.EquipmentChooseAdapter;

public class ChooseEquipmentActivity extends BaseActivity {

    TextView tv_title;
    PullToRefreshListView listView;
    TextView tvCommunity;

    EquipmentChooseAdapter mAdapter;
    int mScoreType;
    ScoreCommDetailInfo mInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_equipment);

        mScoreType = getIntent().getIntExtra("ScoreType",0);
        mInfo = (ScoreCommDetailInfo)getIntent().getSerializableExtra("Info");

        initView();

        if (mInfo!=null){
            GetEquipmentList(mInfo.getCommcode());
        }
    }

    private void initView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("选择设施");
        listView = (PullToRefreshListView)findViewById(R.id.listView);

        View mHeadView = LayoutInflater.from(this).inflate(R.layout.choose_eq_head_item,null);
        tvCommunity = (TextView)mHeadView.findViewById(R.id.tvCommunity);
        listView.getRefreshableView().addHeaderView(mHeadView);

        mAdapter = new EquipmentChooseAdapter(this,mScoreType,mInfo.getCommname(),mInfo);
        listView.setAdapter(mAdapter);

        if (mInfo!=null){
            tvCommunity.setText(mInfo.getCommname());
        }
        mHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if (mScoreType==ChooseScoreCommunityActivity.TYPE_EQUIPMENT_SCORE){
                    intent = new Intent(mContext,ChooseScoreCommunityActivity.class);
                    intent.putExtra("ScoreType",ChooseScoreCommunityActivity.TYPE_EQUIPMENT_SCORE);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }else if (mScoreType==ChooseScoreCommunityActivity.TYPE_CLEAN_SCORE){
                    intent = new Intent(mContext,ChooseScoreCommunityActivity.class);
                    intent.putExtra("ScoreType",ChooseScoreCommunityActivity.TYPE_CLEAN_SCORE);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }
                finish();
                startActivity(intent);
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mInfo!=null){
                    GetEquipmentList(mInfo.getCommcode());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mInfo!=null){
            GetEquipmentList(mInfo.getCommcode());
        }
    }

    private void GetEquipmentList(String CommCode){
        if (mScoreType==ChooseScoreCommunityActivity.TYPE_EQUIPMENT_SCORE){
            GradeApi.get().ChooseScoreEquipment(mContext,CommCode, new ApiConfig.ApiRequestListener<CommonList<ChooseEqInfo>>() {
                @Override
                public void onSuccess(String msg, CommonList<ChooseEqInfo> data) {
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
}
