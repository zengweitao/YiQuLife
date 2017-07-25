package com.quanmai.yiqu.ui.transaction;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.quanmai.yiqu.ui.unused.UnusedFragmentAdapter;

/**
 * 交易列表(搜索)
 */
public class TransactionSearchActivity extends BaseActivity implements
        OnClickListener {
    private PullToRefreshListView mListView;
    LinearLayout linear_no_data;
    private EditText et_search_content;
    RelativeLayout relativeLayoutTimeSorting,
            relativeLayoutPriceSorting, relativeLayoutDegreeSorting;
    TextView textViewTime, textViewPrice, textViewDegree;

    private int page = 0;
    private String keyword = new String();
    private String sort_type = new String();
    UnusedFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_search);
        init();
    }

    private void init() {
        relativeLayoutTimeSorting = (RelativeLayout) findViewById(R.id.relativeLayoutTimeSorting);
        relativeLayoutPriceSorting = (RelativeLayout) findViewById(R.id.relativeLayoutPriceSorting);
        relativeLayoutDegreeSorting = (RelativeLayout) findViewById(R.id.relativeLayoutDegreeSorting);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewPrice = (TextView) findViewById(R.id.textViewPrice);
        textViewDegree = (TextView) findViewById(R.id.textViewDegree);
        linear_no_data = (LinearLayout) findViewById(R.id.linear_no_data);
        et_search_content = (EditText) findViewById(R.id.et_search_content);
        relativeLayoutTimeSorting.setOnClickListener(this);
        relativeLayoutPriceSorting.setOnClickListener(this);
        relativeLayoutDegreeSorting.setOnClickListener(this);
        et_search_content
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (event != null
                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                                && event.getAction() == KeyEvent.ACTION_DOWN) {
                            search();
                            return true;
                        }
                        return false;
                    }
                });

        mListView = (PullToRefreshListView) findViewById(R.id.list);
        mListView.setEmptyView(linear_no_data);
        mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                if (!TextUtils.isEmpty(keyword)) {
                    Refresh();
                }
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                GoodsList();

            }
        });
        mAdapter = new UnusedFragmentAdapter(mContext);
        mListView.setAdapter(mAdapter);
        findViewById(R.id.iv_clear).setOnClickListener(this);
        findViewById(R.id.iv_search).setOnClickListener(this);

    }

    void Refresh() {
        page = 0;
        GoodsList();

    }

    //获取商品
    private void GoodsList() {
        GoodsApi.get().GoodsList(mContext, page, keyword, "", "",
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
//						if (mAdapter.getCount() == 0) {
//							iv_no_data.setText("暂无数据");
//						}
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
//						if (mAdapter.getCount() == 0) {
//							iv_no_data.setText(msg);
//						} else {
//
//						}
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear: {
                et_search_content.setText(null);
                break;

            }
            case R.id.iv_search: {
                search();
                break;
            }
            //时间排序
            case R.id.relativeLayoutTimeSorting: {
                textViewTime.setTextColor(getResources().getColor(R.color.theme));
                textViewPrice.setTextColor(getResources().getColor(R.color.text_color_default));
                textViewDegree.setTextColor(getResources().getColor(R.color.text_color_default));
                sort_type = "3";
                if (!TextUtils.isEmpty(keyword)) {
                    Refresh();
                }
                break;
            }
            //价格排序
            case R.id.relativeLayoutPriceSorting: {
                textViewTime.setTextColor(getResources().getColor(R.color.text_color_default));
                textViewPrice.setTextColor(getResources().getColor(R.color.theme));
                textViewDegree.setTextColor(getResources().getColor(R.color.text_color_default));
                sort_type = "1";
                if (!TextUtils.isEmpty(keyword)) {
                    Refresh();
                }
                break;
            }
            //成色排序
            case R.id.relativeLayoutDegreeSorting: {
                textViewTime.setTextColor(getResources().getColor(R.color.text_color_default));
                textViewPrice.setTextColor(getResources().getColor(R.color.text_color_default));
                textViewDegree.setTextColor(getResources().getColor(R.color.theme));
                sort_type = "2";
                if (!TextUtils.isEmpty(keyword)) {
                    Refresh();
                }
                break;
            }
            default:
                break;
        }
    }

    //搜索商品
    private void search() {
        keyword = et_search_content.getText().toString();
        if (keyword.trim().equals("")) {
            showCustomToast("请输入搜索内容");
            return;
        }
        showLoadingDialog("请稍候");
        Refresh();
    }
}
