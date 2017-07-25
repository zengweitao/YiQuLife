package com.quanmai.yiqu.ui.grade;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.ui.grade.adapter.FetvhBagRecordDetailAdapter;

/**
 * 取袋记录明细页面
 */
public class FetchBagRecordDetailActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tvEquipmentNo;
    private TextView tvFetchNumber;
    private TextView tvDate;

    private FetchBagRecordInfo mBagRecordInfo;
    private PullToRefreshListView pull_bag_record_deatil;
    private FetvhBagRecordDetailAdapter mFetvhBagRecordDetailAdapter;
    int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_bag_record_detail);
        initView();
        initData();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("取袋记录明细");
        pull_bag_record_deatil = (PullToRefreshListView) findViewById(R.id.pull_bag_record_deatil);
        tvEquipmentNo = (TextView) findViewById(R.id.tvEquipmentNo);
        tvFetchNumber = (TextView) findViewById(R.id.tvFetchNumber);
        tvDate = (TextView) findViewById(R.id.tvDate);
        mFetvhBagRecordDetailAdapter = new FetvhBagRecordDetailAdapter(mContext);
        pull_bag_record_deatil.setAdapter(mFetvhBagRecordDetailAdapter);
        mFetvhBagRecordDetailAdapter.add(getIntent().getStringArrayListExtra("BagInfoList"));
        if (getIntent().getIntExtra("max_page",0)>1){
            pull_bag_record_deatil.setMode(PullToRefreshBase.Mode.BOTH);
        }
    }

    private void initData() {
        if (getIntent().hasExtra("BagRecordInfo")) {
            mBagRecordInfo = (FetchBagRecordInfo) getIntent().getSerializableExtra("BagRecordInfo");
        }
        tvEquipmentNo.setText("设备号：" + mBagRecordInfo.terminalno);
        tvFetchNumber.setText("取袋数量：" + mBagRecordInfo.nums);
        tvDate.setText(DateUtil.formatToOther(mBagRecordInfo.opetime, "yyyy-MM-dd hh:mm:ss", "yyyy.MM.dd HH:mm"));
        if (!getIntent().hasExtra("BagInfoList")) {
            //无取袋编号，不显示
            return;
        }
        pull_bag_record_deatil.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 0;
                getBagRecordList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getBagRecordList();
            }
        });

    }

    /**
     * 获取指定设备的取袋详情
     */
    private void getBagRecordList() {
        UserInfoApi.get().getBagInfoList(mContext, currentPage, mBagRecordInfo.terminalno,
                mBagRecordInfo.opetime, mBagRecordInfo.nums, getIntent().getStringExtra("phone"), new ApiConfig.ApiRequestListener<CommonList<String>>() {
                    @Override
                    public void onSuccess(String msg, CommonList<String> data) {
                        pull_bag_record_deatil.onRefreshComplete();
                        if (data == null) {
                            return;
                        }
                        if (currentPage == 0) {
                            mFetvhBagRecordDetailAdapter.clear();
                        }
                        currentPage++;

                        if (currentPage < data.max_page) {
                            pull_bag_record_deatil.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            pull_bag_record_deatil.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                        mFetvhBagRecordDetailAdapter.add(data);
                    }

                    @Override
                    public void onFailure(String msg) {
                        pull_bag_record_deatil.onRefreshComplete();
                        showCustomToast(msg);
                    }
                });
    }
}
