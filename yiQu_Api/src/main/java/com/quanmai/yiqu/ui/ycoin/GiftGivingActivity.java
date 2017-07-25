package com.quanmai.yiqu.ui.ycoin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.IntegralApi;
import com.quanmai.yiqu.api.vo.AwardRecordInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.ycoin.adapter.GiftGivingAdapter;

import java.util.List;

/**
 * 赠送礼品页面
 */
public class GiftGivingActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title;
    private PullToRefreshListView llGiftList;
    private TextView tv_right;

    private int currentPage=0;

    String inputType;
    private ListView giveListView;
    private GiftGivingAdapter mGiftGivingAdapter;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_giving);
        initView();
        getGiftList(0);
    }

    private void initView() {
        //获取用户码或手机号
        if (getIntent().hasExtra("code")&&getIntent().getStringExtra("code").trim().length()==64) {
            inputType="houseCode";
        }
        if (getIntent().hasExtra("code")&&getIntent().getStringExtra("code").trim().length()==11) {
            inputType="account";
        }
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("赠送礼品");
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText("赠送记录");
        tv_right.setOnClickListener(this);
        llGiftList = (PullToRefreshListView) findViewById(R.id.llGiftList);
        giveListView = llGiftList.getRefreshableView();
        header = View.inflate(mContext, R.layout.give_present_view_header,null);
        giveListView.addHeaderView(header);
        mGiftGivingAdapter = new GiftGivingAdapter(mContext, new OnTextViewClickListener() {
            @Override
            public void OnClick(int position, View v, ViewGroup parent) {
                AwardRecordInfo recordInfo = (AwardRecordInfo) mGiftGivingAdapter.getItem(position);
                if (getIntent().hasExtra("wherecome")&&getIntent().getStringExtra("wherecome").equals("notscan")){
                    submit(inputType,getIntent().getStringExtra("code"),"AwardRecordInfo", recordInfo);
                }else {
                    Intent intent = new Intent(mContext, ScanGivingActivity.class);
                    intent.putExtra("AwardRecordInfo", recordInfo);
                    startActivity(intent);
                }
            }
        });
        llGiftList.setAdapter(mGiftGivingAdapter);
        llGiftList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getGiftList(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getGiftList(currentPage);
            }
        });
    }

    //获取礼物清单
    private void getGiftList(final int page) {
        showLoadingDialog();
        IntegralApi.get().getGiftList(mContext, page, new ApiConfig.ApiRequestListener<CommonList<AwardRecordInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<AwardRecordInfo> data) {
                dismissLoadingDialog();
                llGiftList.onRefreshComplete();
                if (data.size() == 0) {
                    return;
                }
                if (page==0){
                    mGiftGivingAdapter.clear();
                }
                if (data.max_page>page+1){
                    llGiftList.setMode(PullToRefreshBase.Mode.BOTH);
                }else {
                    llGiftList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                mGiftGivingAdapter.add(data);
                currentPage++;
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
                llGiftList.onRefreshComplete();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.tv_right: {
                Intent intent = new Intent(mContext, FetchRecordActivity.class);
                intent.putExtra("type", "GivingRecord");
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }
    //页面跳转
    private void submit(String inputType, String result,String infoType,AwardRecordInfo recordInfo) {
        Intent intent = new Intent(mContext, GivingConfirmActivity.class);
        intent.putExtra(infoType, recordInfo);
        intent.putExtra(inputType, result.trim());
        startActivity(intent);
    }

}
