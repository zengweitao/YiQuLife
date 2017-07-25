package com.quanmai.yiqu.ui.Around;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.AroundApi;
import com.quanmai.yiqu.api.vo.NewsListInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.Around.adapter.NewsListAdapter;
import com.quanmai.yiqu.ui.common.WebActivity;

/**
 * 资讯搜索界面
 */
public class NewsSearchActivity extends BaseActivity implements View.OnClickListener {

    //    private TextView tvSearch;
    private PullToRefreshListView listView;
    //    private EditText editSearch;
    private LinearLayout linearLayoutNoData;
    private TextView textViewNoData;
    private ImageView iv_search;
    private ImageView iv_back;
    private EditText et_search_content;
    private ImageView iv_clear;

    int currentPage = 0;
    String strCondition = "";
    NewsListAdapter mListAdapter;
    NewsListInfo mNewsListInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_search);
        initView();
        init();
    }

    private void initView() {
        this.listView = (PullToRefreshListView) findViewById(R.id.listView);
        this.iv_search = (ImageView) findViewById(R.id.iv_search);
        this.iv_back = (ImageView) findViewById(R.id.iv_back);
        this.iv_clear = (ImageView) findViewById(R.id.iv_clear);
        this.et_search_content = (EditText) findViewById(R.id.et_search_content);
        this.linearLayoutNoData = (LinearLayout) findViewById(R.id.linearLayoutNoData);
        this.linearLayoutNoData.setVisibility(View.GONE);
        this.textViewNoData = (TextView) findViewById(R.id.textViewNoData);
        this.textViewNoData.setText("无搜索结果");

        this.iv_search.setOnClickListener(this);
        this.iv_back.setOnClickListener(this);
        this.iv_search.setOnClickListener(this);
        this.iv_clear.setOnClickListener(this);

    }

    private void init() {
        mListAdapter = new NewsListAdapter(mContext);
        listView.setAdapter(mListAdapter);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 0;
                getNewsInfoList(strCondition);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getNewsInfoList(strCondition);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("http_url", mNewsListInfo.infoList.get(position - 1).linkUrl);
                startActivity(intent);
            }
        });

        et_search_content.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(NewsSearchActivity.this.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    if (TextUtils.isEmpty(et_search_content.getText().toString())) {
                        showCustomToast("请输入关键字搜索");
                        return false;
                    }
                    strCondition = et_search_content.getText().toString();
                    showLoadingDialog();
                    linearLayoutNoData.setVisibility(View.GONE);
                    currentPage = 0;
                    getNewsInfoList(strCondition);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 获取咨询列表信息
     */
    private void getNewsInfoList(String condition) {
        AroundApi.getInstance().getNewsInfoList(mContext, condition, currentPage, new ApiConfig.ApiRequestListener<NewsListInfo>() {
            @Override
            public void onSuccess(String msg, NewsListInfo data) {
                listView.onRefreshComplete();
                dismissLoadingDialog();
                if (data == null) return;

                if (data.infoList.isEmpty()) {
                    linearLayoutNoData.setVisibility(View.VISIBLE);
                    textViewNoData.setText("无搜索结果");
                }

                if (currentPage == 0) mListAdapter.clean();
                mListAdapter.add(data.infoList);

                if (data.current_page < data.max_page) {
                    listView.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    if (mListAdapter.getCount() > 0) {
                        Utils.showCustomToast(mContext, "已到最后");
                    }
                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                currentPage = data.current_page + 1;
                mNewsListInfo = data;
            }

            @Override
            public void onFailure(String msg) {
                listView.onRefreshComplete();
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
//                if (TextUtils.isEmpty(editSearch.getText().toString())) {
//                    showCustomToast("请输入关键字搜索");
//                    return;
//                }
//                strCondition = editSearch.getText().toString();
//                showLoadingDialog();
//                linearLayoutNoData.setVisibility(View.GONE);
//                currentPage = 0;
//                getNewsInfoList(strCondition);
                finish();
                break;
            }
            case R.id.iv_search: {
                if (TextUtils.isEmpty(et_search_content.getText().toString())) {
                    showCustomToast("请输入关键字搜索");
                    break;
                }
                strCondition = et_search_content.getText().toString();
                showLoadingDialog();
                linearLayoutNoData.setVisibility(View.GONE);
                currentPage = 0;
                getNewsInfoList(strCondition);
                break;
            }
            case R.id.iv_clear: {
                et_search_content.setText(null);
                break;
            }
        }
    }
}
