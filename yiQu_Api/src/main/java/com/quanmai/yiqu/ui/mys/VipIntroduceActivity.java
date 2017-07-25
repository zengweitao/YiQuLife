package com.quanmai.yiqu.ui.mys;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.IntegralApi;
import com.quanmai.yiqu.api.vo.Member;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.adapter.VipIntroduceAdapter;

/**
 * 等级特权说明页面
 */
public class VipIntroduceActivity extends BaseActivity implements View.OnClickListener {
    Context mContext;
    LinearLayout viewMain;
    PullToRefreshListView mListView;
    LinearLayout linearLayoutNoData;
    TextView textViewNoData;

    VipIntroduceAdapter mAdapter;
    CommonList<Member> mMembers;   //会员等级实体类列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_introduce);
        mContext = this;

        init();
        initVipIntroduceData();
    }


    private void init() {
        ((TextView) findViewById(R.id.tv_title)).setText("等级特权说明");
        viewMain = (LinearLayout) findViewById(R.id.viewMaim);
        mListView = (PullToRefreshListView) findViewById(R.id.listView);
        linearLayoutNoData = (LinearLayout) findViewById(R.id.linearLayoutNoData);
        textViewNoData = (TextView) findViewById(R.id.textViewNoData);

        mListView.setPullToRefreshOverScrollEnabled(false);
        mListView.setEmptyView(linearLayoutNoData);
        mAdapter = new VipIntroduceAdapter(mContext);
        mListView.setAdapter(mAdapter);

        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    private void initVipIntroduceData() {
//        if (getIntent().hasExtra("members")) {
//            mMembers = (CommonList<Member>) getIntent().getSerializableExtra("members");
//            mAdapter.addAll(mMembers);
//        } else {
        getVipIntroduce();
//        }
    }


    private void getVipIntroduce() {
        showLoadingDialog("请稍候");
        IntegralApi.get().getVipIntroduce(mContext, new ApiConfig.ApiRequestListener<CommonList<Member>>() {
            @Override
            public void onSuccess(String msg, CommonList<Member> data) {
                if (data != null && data.size() > 0) {
                    mAdapter.addAll(data);
                } else {
                    textViewNoData.setText("暂无数据");
                }

                dismissLoadingDialog();
            }

            @Override
            public void onFailure(String msg) {
                showShortToast(msg);
                dismissLoadingDialog();
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
        }
    }

}
