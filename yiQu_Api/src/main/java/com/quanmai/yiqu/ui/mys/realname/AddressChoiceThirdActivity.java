package com.quanmai.yiqu.ui.mys.realname;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.vo.CommunityInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.ui.mys.realname.adapter.AddressChoiceListAdapter;

/**
 * Created by James on 2016/7/8.
 * 住址选择页面--选择楼栋
 */
public class AddressChoiceThirdActivity extends BaseActivity implements View.OnClickListener {
    private final int REQUEST_CODE = 101;

    private PullToRefreshListView listView;

    private AddressChoiceListAdapter<String> mListAdapter;
    private CommunityInfo mCommunityInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_choice);
        initView();
        init();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("选择楼栋");
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.rlSearch).setVisibility(View.GONE);

        this.listView = (PullToRefreshListView) findViewById(R.id.listView);
    }

    private void init() {
        mCommunityInfo = (CommunityInfo) getIntent().getSerializableExtra("communityInfo");
        mListAdapter = new AddressChoiceListAdapter(mContext, new String());

        listView.setAdapter(mListAdapter);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getBuild(mCommunityInfo.commcode);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                intent.setClass(mContext,AddressChoiceFourthMainActivity.class);
                String buildcode = (String) mListAdapter.getItem(position - 1);
                intent.putExtra("city", mCommunityInfo.city);
                intent.putExtra("commname", mCommunityInfo.commname);
                intent.putExtra("commcode", mCommunityInfo.commcode);
                intent.putExtra("buildcode", buildcode);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        listView.setRefreshing();
    }

    /**
     * 实名制获取幢
     *
     * @param commcode
     */
    private void getBuild(String commcode) {
        UserApi.get().getBuild(mContext, commcode, new ApiConfig.ApiRequestListener<CommunityInfo>() {
            @Override
            public void onSuccess(String msg, CommunityInfo data) {
                listView.onRefreshComplete();
                if (data == null || data.buildList.size() == 0) {
                    showCustomToast(msg);
                    return;
                }

                mListAdapter.addAll(data.buildList);
                mCommunityInfo.buildList = data.buildList;
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
            setResult(RESULT_OK, data);
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
            default:
                break;
        }
    }
}
