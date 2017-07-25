package com.quanmai.yiqu.ui.mys.realname;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.vo.RealNameMember;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.ui.mys.realname.adapter.MembersApplyOfCheckAdapter;

import java.util.ArrayList;

/**
 * 审核（入户申请）界面
 */
public class MembersApplyOfCheckActivity extends BaseActivity implements View.OnClickListener {

    private PullToRefreshListView list;
    private MembersApplyOfCheckAdapter mAdapter;
    private LinearLayout linearLayoutNoData;
    private TextView textViewNoData;
    private String usercompareid; //实名制住户信息表id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_apply_of_check);
        initView();
        init();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("审核入户申请");
        findViewById(R.id.iv_back).setOnClickListener(this);
        this.list = (PullToRefreshListView) findViewById(R.id.list);
        linearLayoutNoData = (LinearLayout) findViewById(R.id.linearLayoutNoData);
        linearLayoutNoData.setVisibility(View.GONE);
        textViewNoData = (TextView) findViewById(R.id.textViewNoData);
        textViewNoData.setText("暂无申请");
        list.setEmptyView(linearLayoutNoData);

        mAdapter = new MembersApplyOfCheckAdapter(mContext);
        list.setAdapter(mAdapter);
    }

    private void init() {
        list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getApplyList(usercompareid);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        if (getIntent().hasExtra("usercompareid")) {
            usercompareid = getIntent().getStringExtra("usercompareid");
            showLoadingDialog();
            getApplyList(usercompareid);
        } else {
            linearLayoutNoData.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取申请列表
     *
     * @param usercompareid
     */
    private void getApplyList(String usercompareid) {
        UserApi.get().getMembersApplyList(mContext, usercompareid, new ApiConfig.ApiRequestListener<ArrayList<RealNameMember>>() {
            @Override
            public void onSuccess(String msg, ArrayList<RealNameMember> data) {
                dismissLoadingDialog();
                list.onRefreshComplete();
                if (data == null || data.size() == 0) {
                    return;
                }
                mAdapter.clean();
                mAdapter.add(data);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                list.onRefreshComplete();
                showCustomToast(msg);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(Activity.RESULT_OK);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                setResult(Activity.RESULT_OK);
                finish();
                break;
            }
        }
    }
}
