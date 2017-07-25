package com.quanmai.yiqu.ui.mys.realname;

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
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.vo.CityInfo;
import com.quanmai.yiqu.api.vo.CommunityInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.mys.realname.adapter.AddressChoiceListAdapter;

/**
 * 地址搜索页面（搜索小区）
 */
public class AddressSearchActivity extends BaseActivity implements View.OnClickListener {
    private final int REQUEST_CODE = 101;

    private PullToRefreshListView listView;
    private LinearLayout linearLayoutNoData;
    private TextView textViewNoData;
    private ImageView iv_search;
    private ImageView iv_back;
    private EditText et_search_content;
    private ImageView iv_clear;

    private AddressChoiceListAdapter<CommunityInfo> mListAdapter;
    private CommonList<CommunityInfo> mCommunityInfos = new CommonList<>();
    private String strKeyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);
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

        this.iv_search.setOnClickListener(this);
        this.iv_back.setOnClickListener(this);
        this.iv_clear.setOnClickListener(this);
    }

    private void init() {
        mListAdapter = new AddressChoiceListAdapter<>(mContext, new CommunityInfo());
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

        et_search_content.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(AddressSearchActivity.this.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    if (TextUtils.isEmpty(et_search_content.getText().toString())) {
                        showCustomToast("请输入关键字搜索");
                        return false;
                    }
                    strKeyword = et_search_content.getText().toString();
                    linearLayoutNoData.setVisibility(View.GONE);
                    getCityOrCommunity(strKeyword);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 获取小区地址
     *
     * @param commname
     */
    private void getCityOrCommunity(String commname) {
        showLoadingDialog();
        UserApi.get().getCityOrCommutity(mContext, commname, new ApiConfig.ApiRequestListener<CommonList<CityInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<CityInfo> data) {
                listView.onRefreshComplete();
                dismissLoadingDialog();
                mCommunityInfos.clear();
                mListAdapter.clear();

                if (data == null || data.size() <= 0) {
                    linearLayoutNoData.setVisibility(View.VISIBLE);
                    textViewNoData.setText("无搜索结果");
                    return;
                } else {
                    linearLayoutNoData.setVisibility(View.GONE);
                }

                for (int i = 0; i < data.size(); i++) {
                    CityInfo cityInfo = data.get(i);
                    for (int j = 0; j < cityInfo.commList.size(); j++) {
                        CommunityInfo communityInfo = cityInfo.commList.get(j);
                        mCommunityInfos.add(communityInfo);
                    }
                }
                mListAdapter.addAll(mCommunityInfos);
            }

            @Override
            public void onFailure(String msg) {
                listView.onRefreshComplete();
                mCommunityInfos.clear();
                mListAdapter.clear();
                dismissLoadingDialog();
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
            case R.id.iv_search: {
                if (TextUtils.isEmpty(et_search_content.getText().toString())) {
                    showCustomToast("请输入关键字搜索");
                    return;
                }
                strKeyword = et_search_content.getText().toString();
                linearLayoutNoData.setVisibility(View.GONE);
                getCityOrCommunity(strKeyword);
                break;
            }
            case R.id.iv_clear: {
                et_search_content.setText(null);
                break;
            }
        }
    }
}
