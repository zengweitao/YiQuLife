package com.quanmai.yiqu.ui.mys.realname;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.vo.CityInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.mys.realname.adapter.AddressChoiceListAdapter;

/**
 * 住址选择页面
 */
public class AddressChoiceActivity extends BaseActivity implements View.OnClickListener {
    private final int REQUEST_CODE = 101;

    private RelativeLayout rlSearch;
    private PullToRefreshListView listView;

    private AddressChoiceListAdapter<CityInfo> mListAdapter;
    private CommonList<CityInfo> mCityInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_choice);
        initView();
        init();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("选择小区");
        findViewById(R.id.iv_back).setOnClickListener(this);

        this.listView = (PullToRefreshListView) findViewById(R.id.listView);
        this.rlSearch = (RelativeLayout) findViewById(R.id.rlSearch);
        rlSearch.setOnClickListener(this);
    }

    private void init() {
        mListAdapter = new AddressChoiceListAdapter(mContext, new CityInfo());

        listView.setAdapter(mListAdapter);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getCityOrCommunity("");
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, AddressChoiceSecondActivity.class);
                CityInfo cityInfo = mCityInfos.get(position - 1);
                intent.putExtra("cityInfo", cityInfo);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        listView.setRefreshing();
    }

    /**
     * 获取小区地址
     *
     * @param commname
     */
    private void getCityOrCommunity(String commname) {
        UserApi.get().getCityOrCommutity(mContext, commname, new ApiConfig.ApiRequestListener<CommonList<CityInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<CityInfo> data) {
                listView.onRefreshComplete();
                if (data == null) {
                    return;
                }
                mListAdapter.addAll(data);
                mCityInfos = data;
            }

            @Override
            public void onFailure(String msg) {
                listView.onRefreshComplete();
                showCustomToast(msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE == requestCode && data != null && data.hasExtra("finish")) {
            this.finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.rlSearch: {
                Intent intent = new Intent(mContext, AddressSearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            }
            default:
                break;
        }
    }
}
