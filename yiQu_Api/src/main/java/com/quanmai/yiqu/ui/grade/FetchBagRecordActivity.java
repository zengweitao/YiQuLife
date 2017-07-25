package com.quanmai.yiqu.ui.grade;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.UserInfoApi;
import com.quanmai.yiqu.api.vo.FetchBagRecordInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.grade.adapter.FetchBagAdapter;

/**
 * 取袋记录页面
 */
public class FetchBagRecordActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private PullToRefreshListView listView;

    private FetchBagAdapter mAdapter;
    private TextView textViewNoData;
    private LinearLayout linearLayoutNoData;

    private int currentPage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_bag_record);
        initView();
        init();

        getFetchBagRecordList();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("取袋记录");
        textViewNoData = (TextView) findViewById(R.id.textViewNoData);
        textViewNoData.setText("还没取袋记录喲");
        linearLayoutNoData = (LinearLayout) findViewById(R.id.linearLayoutNoData);
        listView = (PullToRefreshListView) findViewById(R.id.listView);
        listView.setEmptyView(linearLayoutNoData);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private void init() {
        mAdapter = new FetchBagAdapter(mContext);
        listView.setAdapter(mAdapter);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getFetchBagRecordList();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getBagRecordList(position-1);
            }
        });
    }

    private void refreshData(){
        currentPage=0;
        getFetchBagRecordList();
    }

    //获取取袋记录列表
    private void getFetchBagRecordList() {
        showLoadingDialog("正在获取数据...");
        UserInfoApi.get().getFetchBagInfoList(mContext, currentPage,getIntent().getStringExtra("code"),new ApiConfig.ApiRequestListener<CommonList<FetchBagRecordInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<FetchBagRecordInfo> data) {
                dismissLoadingDialog();
                listView.onRefreshComplete();
                if (data == null) {
                    return;
                }

                if (currentPage==0){
                    mAdapter.clear();
                }
                currentPage++;

                if (currentPage<data.max_page){
                    listView.setMode(PullToRefreshBase.Mode.BOTH);
                }else {
                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                mAdapter.add(data);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                listView.onRefreshComplete();
                showCustomToast(msg);
            }
        });
    }

    /**
     * 获取指定设备的取袋详情
     */
    String phone=Session.get(mContext).getUsername();
    private void getBagRecordList(final int position) {
        if (getIntent().hasExtra("phone")){
            phone=getIntent().getStringExtra("phone");
        }
        showLoadingDialog("正在获取数据...");
        UserInfoApi.get().getBagInfoList(mContext, 0,mAdapter.getItem(position).terminalno,
                mAdapter.getItem(position).opetime,mAdapter.getItem(position).nums, phone,new ApiConfig.ApiRequestListener<CommonList<String>>() {
            @Override
            public void onSuccess(String msg, CommonList<String> data) {
                dismissLoadingDialog();
                if (data == null) {
                    return;
                }
                Intent intent = new Intent(mContext, FetchBagRecordDetailActivity.class);
                intent.putExtra("BagRecordInfo", mAdapter.getItem(position));
                intent.putStringArrayListExtra("BagInfoList",data);
                intent.putExtra("phone",phone);
                intent.putExtra("max_page",data.max_page);
                mContext.startActivity(intent);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                break;
            }

        }
    }
}
