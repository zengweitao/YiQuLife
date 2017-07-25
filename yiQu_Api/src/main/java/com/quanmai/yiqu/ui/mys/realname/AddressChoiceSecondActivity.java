package com.quanmai.yiqu.ui.mys.realname;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.CityInfo;
import com.quanmai.yiqu.api.vo.CommunityInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.mys.realname.adapter.AddressChoiceListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 住址选择-选择小区页面
 * Created by James on 2016/7/7.
 */
public class AddressChoiceSecondActivity extends BaseActivity implements View.OnClickListener {
    private final int REQUEST_CODE = 101;

    private RelativeLayout rlSearch;
    private PullToRefreshListView listView;

    private AddressChoiceListAdapter<CommunityInfo> mListAdapter;
    private List<String> mStringList = new ArrayList<>();
    private CityInfo mCityInfo;
    private CommonList<CommunityInfo> mCommunityInfos;


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
        mCityInfo = (CityInfo) getIntent().getSerializableExtra("cityInfo");
        mCommunityInfos = mCityInfo.commList;
        mListAdapter = new AddressChoiceListAdapter(mContext, new CommunityInfo());
        mListAdapter.addAll(mCommunityInfos);

        listView.setAdapter(mListAdapter);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommunityInfo communityInfo = (CommunityInfo) mListAdapter.getItem(position - 1);
                Intent intent = new Intent(mContext, AddressChoiceThirdActivity.class);
                intent.putExtra("communityInfo", communityInfo);
                startActivityForResult(intent, REQUEST_CODE);
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
            case R.id.rlSearch:{
                Intent intent = new Intent(mContext, AddressSearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            }
            default:
                break;

        }
    }
}
