package com.quanmai.yiqu.ui.transaction.list;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.GoodsApi;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.transaction.filterview.ExpandTabView;
import com.quanmai.yiqu.ui.transaction.filterview.ExpandTabView.OnSelectListener1;

/**
 * 交易列表
 */
public class TransactionListActivity extends BaseActivity implements
        OnClickListener {
    private PullToRefreshListView mListView;
    TextView iv_no_data;
    private int page = 0;
    private String area_id = new String();
    private String class_id = new String();
    private String sort_type = new String();
    private TransactionListAdapter0 mAdapter;
    ExpandTabView expandTabView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        ((TextView) findViewById(R.id.tv_title)).setText(getIntent().getStringExtra("class_name"));
        class_id = getIntent().getStringExtra("class_id");
        init();
    }

    private void init() {
        expandTabView = (ExpandTabView) findViewById(R.id.expandTabView);
        iv_no_data = (TextView) findViewById(R.id.iv_no_data);


        mListView = (PullToRefreshListView) findViewById(R.id.list);
        mListView.setEmptyView(iv_no_data);
        mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                Refresh();

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                GoodsList();

            }
        });
        mAdapter = new TransactionListAdapter0(mContext);
        mListView.setAdapter(mAdapter);
        expandTabView.setOnSelectListener(new OnSelectListener1() {

            @Override
            public void onSelect(String id1, String id2, String id3) {
                area_id = id1;
                class_id = id2;
                sort_type = id3;
                showLoadingDialog("请稍候");
                Refresh();
            }
        });
        mListView.setRefreshing();
    }


    void Refresh() {
        page = 0;
        GoodsList();

    }

    private void GoodsList() {
        GoodsApi.get().GoodsList(mContext, page, "", area_id, class_id,
                sort_type, new ApiRequestListener<CommonList<GoodsBasic>>() {


                    @Override
                    public void onSuccess(String msg,
                                          CommonList<GoodsBasic> data) {
                        dismissLoadingDialog();
                        mListView.onRefreshComplete();
                        if (page == 0) {
                            mAdapter.clear();
                        }
                        mAdapter.add(data);
                        if (mAdapter.getCount() == 0) {
                            iv_no_data.setText("暂无数据");
                        }
                        if (data.max_page > data.current_page) {
                            mListView.setMode(Mode.BOTH);
                        } else {
                            mListView.setMode(Mode.PULL_FROM_START);
                        }
                        page = data.current_page + 1;
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();
                        showCustomToast(msg);
                        mListView.onRefreshComplete();
                        if (page == 0) {
                            mAdapter.clear();
                            mAdapter.notifyDataSetChanged();
                        }
                        if (mAdapter.getCount() == 0) {
                            iv_no_data.setText(msg);
                        } else {

                        }
                    }
                });


    }

    @Override
    public void onClick(View v) {

    }


}
