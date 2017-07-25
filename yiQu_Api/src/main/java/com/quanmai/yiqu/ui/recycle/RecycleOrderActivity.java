package com.quanmai.yiqu.ui.recycle;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.RecycleApi;
import com.quanmai.yiqu.api.vo.RecycleOrderInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseFragmentActivity;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.push.JPushReceiver;
import com.quanmai.yiqu.ui.recycle.adapter.RecycleOrderPagerAdapter;
import com.quanmai.yiqu.ui.recycle.fragment.RecycleOrderFinishedFragment;
import com.quanmai.yiqu.ui.recycle.fragment.RecycleOrderNoTakeFragment;
import com.quanmai.yiqu.ui.recycle.fragment.RecycleOrderUnreceivedFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 回收订单页面
 */
public class RecycleOrderActivity extends BaseFragmentActivity implements View.OnClickListener, OnRefreshListener {
    public static String ACTION_HAVE_RECEIVE_ORDER = "action_have_receive_order";
    public static String ACTION_CHANGE_CURRENT_PAGER = "action_change_current_pager";
    public static String ACTION_REFRESH_RECYCLE_ORDER_NUM = "action_refresh_recycle_order_num";


    TextView tvUnreceivedNum, tvNotTakeNum, tvFinishedNum;
    TextView tvUnreceived, tvNotTake, tvFinished;
    RadioGroup radioGroupTab;
    ViewPager viewPagerOrder;

    RecycleOrderPagerAdapter pagerAdapter;

    List<RecycleOrderInfo> mOrderInfos;
    LocalBroadcastReceiver mReceiver;

    Dialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_order);
        ((TextView) findViewById(R.id.tv_title)).setText("回收订单");
        init();
        initViewPager();

        showLoadingDialog();
        getRecycleOrderNum();

        Intent intent = this.getIntent();
        viewPagerOrder.setCurrentItem(intent.getIntExtra("currentItem", 0));
        JPushReceiver.mOldType = "";
    }

    @Override
    public void onNewIntent(Intent intent) {
        viewPagerOrder.setCurrentItem(intent.getIntExtra("currentItem", 0));
        super.onNewIntent(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    public void init() {
        tvUnreceivedNum = (TextView) findViewById(R.id.tvUnreceivedNum);
        tvNotTakeNum = (TextView) findViewById(R.id.tvNotTakeNum);
        tvFinishedNum = (TextView) findViewById(R.id.tvFinishedNum);

        tvUnreceived = (TextView) findViewById(R.id.tvUnreceived);
        tvNotTake = (TextView) findViewById(R.id.tvNotTake);
        tvFinished = (TextView) findViewById(R.id.tvFinished);

        radioGroupTab = (RadioGroup) findViewById(R.id.radioGroupTab);
        viewPagerOrder = (ViewPager) findViewById(R.id.viewPagerOrder);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.llUnreceived).setOnClickListener(this);
        findViewById(R.id.llNotTake).setOnClickListener(this);
        findViewById(R.id.llFinished).setOnClickListener(this);

        mReceiver = new LocalBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(RecycleOrderNoTakeFragment.ACTION_NETWORKING_TO_REFRESH_NO_TAKE);
        filter.addAction(RecycleOrderFinishedFragment.ACTION_NETWORKING_TO_REFRESH_FINISH);
        filter.addAction(ACTION_CHANGE_CURRENT_PAGER);
        filter.addAction(ACTION_HAVE_RECEIVE_ORDER);
        filter.addAction(ACTION_REFRESH_RECYCLE_ORDER_NUM);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, filter);

        mOrderInfos = new ArrayList<>();
        mDialog = new Dialog(mContext);
    }

    public void initViewPager() {
        pagerAdapter = new RecycleOrderPagerAdapter(this.getSupportFragmentManager());
        pagerAdapter.addItem(new RecycleOrderUnreceivedFragment(this));
        pagerAdapter.addItem(new RecycleOrderNoTakeFragment(this));
        pagerAdapter.addItem(new RecycleOrderFinishedFragment(this));

        viewPagerOrder.setOffscreenPageLimit(2);
        viewPagerOrder.setAdapter(pagerAdapter);
        viewPagerOrder.setCurrentItem(0);
        tvUnreceivedNum.setSelected(true);
        tvUnreceived.setSelected(true);
        viewPagerOrder.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: {
                        initTab();
                        tvUnreceivedNum.setSelected(true);
                        tvUnreceived.setSelected(true);
                        break;
                    }
                    case 1: {
                        initTab();
                        tvNotTakeNum.setSelected(true);
                        tvNotTake.setSelected(true);
                        break;
                    }
                    case 2: {
                        initTab();
                        tvFinishedNum.setSelected(true);
                        tvFinished.setSelected(true);
                        break;
                    }
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void initTab() {
        tvUnreceivedNum.setSelected(false);
        tvNotTakeNum.setSelected(false);
        tvFinishedNum.setSelected(false);
        tvUnreceived.setSelected(false);
        tvNotTake.setSelected(false);
        tvFinished.setSelected(false);
    }

    public void getRecycleOrderNum() {
        RecycleApi.get().getRecycleOrderNum(this, new ApiConfig.ApiRequestListener<Map<String, Integer>>() {
            @Override
            public void onSuccess(String msg, Map<String, Integer> data) {
                if (data.isEmpty()) {
                    return;
                }

                tvUnreceivedNum.setText(StringUtil.intNullFilter(
                        data.get(RecycleApi.orderStatus.initial.toString())) + "");
                tvNotTakeNum.setText(StringUtil.intNullFilter(
                        data.get(RecycleApi.orderStatus.verified.toString())) + "");
                tvFinishedNum.setText(
                        StringUtil.intNullFilter(data.get(RecycleApi.orderStatus.recycle.toString())) +
                                StringUtil.intNullFilter(data.get(RecycleApi.orderStatus.completed.toString())) +
                                StringUtil.intNullFilter(data.get(RecycleApi.orderStatus.cancel.toString())) +
                                StringUtil.intNullFilter(data.get(RecycleApi.orderStatus.overdue.toString())) + "");

            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (viewPagerOrder.getCurrentItem()==1){
            viewPagerOrder.setCurrentItem(1);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(RecycleOrderNoTakeFragment.ACTION_NETWORKING_TO_REFRESH_NO_TAKE));
        }else if (viewPagerOrder.getCurrentItem()==0){
            viewPagerOrder.setCurrentItem(1);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(RecycleOrderNoTakeFragment.ACTION_NETWORKING_TO_REFRESH_NO_TAKE));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.llUnreceived: {
                viewPagerOrder.setCurrentItem(0);
                break;
            }

            case R.id.llNotTake: {
                viewPagerOrder.setCurrentItem(1);
                break;
            }
            case R.id.llFinished: {
                viewPagerOrder.setCurrentItem(2);
                break;
            }
            case R.id.btnCancel: {
                refresh();
                viewPagerOrder.setCurrentItem(0);
                mOrderInfos.remove(mOrderInfos.size() - 1);

                if (mDialog.isShowing()) mDialog.dismiss();
                showAppointmentDialog();

                //已处理完所有Dialog
                if (mOrderInfos.size() <= 0) {
                    //忽视接单，发送广播刷新页面“待接单”列表
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(RecycleOrderUnreceivedFragment.ACTION_NETWORKING_TO_REFRESH_UNRECEIVE));
                }
                break;
            }
            case R.id.btnConfirm: {
                confirmAppointmentOrder(mOrderInfos.get(mOrderInfos.size() - 1).id, mOrderInfos.get(mOrderInfos.size() - 1).userId);
                break;
            }

        }
    }

    /**
     * 回收人员接单
     *
     * @param id
     * @param userid
     */
    public void confirmAppointmentOrder(String id, String userid) {
        if(UserInfo.get().getUserid().equals(userid)){

        }
        showLoadingDialog();
        RecycleApi.get().confirmAppointmentOrder(mContext, id, userid, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast(msg);
                viewPagerOrder.setCurrentItem(1);
                mOrderInfos.remove(mOrderInfos.size() - 1);

                if (mDialog.isShowing()) mDialog.dismiss();

                showAppointmentDialog();

                if (mOrderInfos.size() <= 0) {
                    //接单成功，发送广播刷新“已接单”列表页面
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(RecycleOrderNoTakeFragment.ACTION_NETWORKING_TO_REFRESH_NO_TAKE));
                }
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
                mOrderInfos.remove(mOrderInfos.size() - 1);

                if (mDialog.isShowing()) mDialog.dismiss();
            }
        });
    }

    /**
     * 按集合队列依次显示预约订单弹窗
     */
    public void showAppointmentDialog() {
        if (mOrderInfos.size() > 0 && !mDialog.isShowing()) {
            RecycleOrderInfo recycleOrderInfo = mOrderInfos.get(mOrderInfos.size() - 1);
            mDialog = DialogUtil.getAppointmentDialog(mContext, "您有一条新的回收预约",
                    recycleOrderInfo.rangeDate.replace("_", ".") + " " + recycleOrderInfo.rangeTime,
                    recycleOrderInfo.address, RecycleOrderActivity.this);
            mDialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = (Fragment) pagerAdapter.instantiateItem(viewPagerOrder, 1);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void refresh() {
        getRecycleOrderNum();
    }

    class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (RecycleOrderNoTakeFragment.ACTION_NETWORKING_TO_REFRESH_NO_TAKE.equals(intent.getAction()) ||
                    RecycleOrderFinishedFragment.ACTION_NETWORKING_TO_REFRESH_FINISH.equals(intent.getAction()) ||
                    ACTION_REFRESH_RECYCLE_ORDER_NUM.equals(intent.getAction())) {
                getRecycleOrderNum();
            } else if (ACTION_CHANGE_CURRENT_PAGER.equals(intent.getAction())) {
                int pager = intent.getIntExtra("currentPager", 0);
                if (viewPagerOrder.isShown() && pagerAdapter != null && pagerAdapter.getCount() >= (pager + 1)) {
                    viewPagerOrder.setCurrentItem(pager);
                }
            } else if (ACTION_HAVE_RECEIVE_ORDER.equals(intent.getAction())) {
                String strExtras = intent.getExtras().getString(JPushInterface.EXTRA_EXTRA);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(strExtras);
                    RecycleOrderInfo orderInfo = new RecycleOrderInfo();
                    orderInfo.id = jsonObject.optString("id");
                    orderInfo.rangeDate = jsonObject.optString("rangeDate");
                    orderInfo.userId = jsonObject.optString("userid");
                    orderInfo.rangeTime = jsonObject.optString("rangeTime");
                    orderInfo.address = jsonObject.optString("address");

                    mOrderInfos.add(orderInfo);
                } catch (JSONException e) {
                    Log.e("JPush.EXTRA--->", "json解析失败");
                }

                showAppointmentDialog();
            }
        }
    }


}
