package com.quanmai.yiqu.ui.grade;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GradeApi;
import com.quanmai.yiqu.api.vo.ChooseEqAreaInfo;
import com.quanmai.yiqu.api.vo.ScoreCommDetailInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.grade.adapter.ChooseEqAreaAdapter;

/**
 * 巡检-选择设施点页面（用于清运评分）
 */
public class ChooseEqAreaActivity extends BaseActivity implements View.OnClickListener {
    public static int REQUEST_CODE = 101;

    private TextView tv_title;
    private PullToRefreshListView listView;
    private TextView tvCommunity;


    private int mScoreType;
    private ScoreCommDetailInfo mInfo;
    private ChooseEqAreaAdapter mAdapter;   //列表适配器
    private TextView tvEQTitle;
    private TextView tvEQStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_equipment_area);
        initView();
        init();

        if (mInfo != null) {
            showLoadingDialog();
            getEquipmentList(mInfo.getCommcode());
        }
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("选择设施点");
        listView = (PullToRefreshListView) findViewById(R.id.listView);
    }

    private void init() {
        mScoreType = getIntent().getIntExtra("ScoreType", 0);
        mInfo = (ScoreCommDetailInfo) getIntent().getSerializableExtra("Info");

        View mHeadView = LayoutInflater.from(this).inflate(R.layout.choose_eq_head_item, null);
        mHeadView.findViewById(R.id.llChooseEqItem).setVisibility(View.VISIBLE);
        tvEQTitle = (TextView) mHeadView.findViewById(R.id.tvEQTitle);
        tvEQStatus = (TextView) mHeadView.findViewById(R.id.tvEQStatus);
        tvCommunity = (TextView) mHeadView.findViewById(R.id.tvCommunity);
        tvEQTitle.setText("设施点");
        listView.getRefreshableView().addHeaderView(mHeadView);
        if (mInfo != null) {
            tvCommunity.setText(mInfo.getCommname());
        }

        mAdapter = new ChooseEqAreaAdapter(this, mScoreType, mInfo);
        listView.setAdapter(mAdapter);

        mHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChooseScoreCommunityActivity.class);
                intent.putExtra("ScoreType", ChooseScoreCommunityActivity.TYPE_CLEAN_SCORE);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(intent);
            }
        });
    }

    //获取设备列表
    private void getEquipmentList(String CommCode) {
        GradeApi.get().ChooseCleanScoreEquipment(mContext, CommCode, new ApiConfig.ApiRequestListener<CommonList<ChooseEqAreaInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<ChooseEqAreaInfo> data) {
                dismissLoadingDialog();
                mAdapter.clear();
                if (!TextUtils.isEmpty(msg)) {
                    if ("1".equals(msg)) {
                        tvEQStatus.setText("完成");
                        tvEQStatus.setTextColor(Color.parseColor("#979797"));
                    } else {
                        tvEQStatus.setText("未完成");
                        tvEQStatus.setTextColor(Color.parseColor("#ED5639"));
                    }
                }
                mAdapter.add(data);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getEquipmentList(mInfo.getCommcode());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                break;
            }
            default:
                break;
        }
    }
}
