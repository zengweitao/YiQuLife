package com.quanmai.yiqu.ui.booking;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.BookingApi;
import com.quanmai.yiqu.api.vo.RecycleGarbageListInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.booking.Adapter.ShowParbagePriceAdapter01;

import java.util.ArrayList;

public class ShowParbagePriceActivity extends BaseActivity implements View.OnClickListener{

    private PullToRefreshListView pulltorefresh_parbage_price;
    private ShowParbagePriceAdapter01 mShowParbagePriceAdapter01;
    private TextView tv_title;
    private ImageView imageview_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_parbage_price);
        init();
        getGrabageList();
    }

    private void init() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        imageview_top = (ImageView)findViewById(R.id.imageview_top);
        tv_title.setText("价格表");
        pulltorefresh_parbage_price = (PullToRefreshListView) findViewById(R.id.pulltorefresh_parbage_price);
        mShowParbagePriceAdapter01 = new ShowParbagePriceAdapter01(mContext);
        pulltorefresh_parbage_price.setAdapter(mShowParbagePriceAdapter01);
        pulltorefresh_parbage_price.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        imageview_top.setOnClickListener(this);
        pulltorefresh_parbage_price.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getGrabageList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
    }

    //获取可回收垃圾列表
    private void getGrabageList() {
        showLoadingDialog();
        BookingApi.get().GetGarbageList(mContext, new ApiConfig.ApiRequestListener<CommonList<RecycleGarbageListInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<RecycleGarbageListInfo> data) {
                dismissLoadingDialog();
                if (data!=null&&data.size()!=0){
                    mShowParbagePriceAdapter01.add(data);
                }
                pulltorefresh_parbage_price.onRefreshComplete();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_top:
                pulltorefresh_parbage_price.post(new Runnable() {
                    @Override
                    public void run() {
                        pulltorefresh_parbage_price.getRefreshableView().smoothScrollToPosition(0);
                    }
                });
                break;
        }
    }
}
