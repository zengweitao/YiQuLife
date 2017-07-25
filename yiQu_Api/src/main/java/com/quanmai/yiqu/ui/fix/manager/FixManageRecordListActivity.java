package com.quanmai.yiqu.ui.fix.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.vo.FixInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.fix.FixRecordAdapter;
import com.quanmai.yiqu.ui.fix.FixRecordDetailActivity;

/**
 * 管理员报修记录列表
 */
public class FixManageRecordListActivity extends BaseActivity {
    TextView iv_no_data;
    FixRecordAdapter adapter;
    int page = 0;
    private PullToRefreshListView list;
    String classcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_list);
        init();
    }


    protected void init() {
        classcode = getIntent().getStringExtra("classcode");
        ((TextView) findViewById(R.id.tv_title)).setText(getIntent().getStringExtra("classname"));
        iv_no_data = (TextView) findViewById(R.id.iv_no_data);
        list = (PullToRefreshListView) findViewById(R.id.list);
        adapter = new FixRecordAdapter(mContext);
        list.getRefreshableView().setAdapter(adapter);
        list.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                Refresh();

            }

            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                getListData();
            }
        });
        list.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                FixInfo fixInfo = (FixInfo) parent.getAdapter().getItem(position);
                Intent intent = new Intent(mContext, FixRecordDetailActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("service_id", fixInfo.id);
                startActivityForResult(intent, 1);
            }
        });
        list.setEmptyView(iv_no_data);
        list.setRefreshing();
    }

    private void Refresh() {
        page = 0;
        getListData();
    }

    private void getListData() {
        Api.get().FixManageList(mContext, page, classcode,
                new ApiRequestListener<CommonList<FixInfo>>() {

                    @Override
                    public void onSuccess(String msg,
                                          CommonList<FixInfo> data) {
                        dismissLoadingDialog();
                        list.onRefreshComplete();
                        if (page == 0) {
                            adapter.clear();
                        }
                        adapter.add(data);
                        if (adapter.getCount() == 0) {
                            iv_no_data.setText("暂无数据");
                        }

                        if (data.max_page > data.current_page) {
                            list.setMode(Mode.BOTH);
                        } else {
                            list.setMode(Mode.PULL_FROM_START);
                        }
                        page = data.current_page + 1;

                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();
                        list.onRefreshComplete();
                        if (adapter.getCount() == 0) {
                            iv_no_data.setText(msg);
                        } else {
                            showCustomToast(msg);
                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Refresh();
        }
    }


}