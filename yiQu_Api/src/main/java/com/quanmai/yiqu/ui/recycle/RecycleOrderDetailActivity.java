package com.quanmai.yiqu.ui.recycle;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.RecycleApi;
import com.quanmai.yiqu.api.vo.RecycleGarbageInfo;
import com.quanmai.yiqu.api.vo.RecycleOrderInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.StringUtil;

public class RecycleOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    TextView tvOrderStatus, tvFinishedDate, tvAppointmentDate, tvUserName, tvPhoneNum, tvAddress, tvGainPoint;
    LinearLayout llRecycleInfoList;

    Dialog callDialog;
    String orderid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_order_detail);
        ((TextView) findViewById(R.id.tv_title)).setText("回收订单详情");
        initView();
        initData();
    }


    private void initView() {
        tvOrderStatus = (TextView) findViewById(R.id.tvOrderStatus);
        tvFinishedDate = (TextView) findViewById(R.id.tvFinishedDate);
        tvAppointmentDate = (TextView) findViewById(R.id.tvAppointmentDate);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvPhoneNum = (TextView) findViewById(R.id.tvPhoneNum);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvGainPoint = (TextView) findViewById(R.id.tvGainPoint);
        llRecycleInfoList = (LinearLayout) findViewById(R.id.llRecycleInfoList);

        findViewById(R.id.iv_back).setOnClickListener(this);
        tvPhoneNum.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = this.getIntent();
        orderid = intent.getStringExtra("orderid");
        getRecycleOrderDetail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.tvPhoneNum: {
                String phoneNum = tvPhoneNum.getText().toString().trim();

                callDialog = DialogUtil.getCallDialog(mContext, phoneNum, this);
                callDialog.show();
                break;
            }

            case R.id.buttonConfirm: {
                String phoneNum = tvPhoneNum.getText().toString().trim();

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNum));
                mContext.startActivity(intent);

                if (callDialog.isShowing()) {
                    callDialog.dismiss();
                }
                break;
            }
            case R.id.buttonCancel: {
                if (callDialog.isShowing()) {
                    callDialog.dismiss();
                }
                break;
            }

            default:
                break;
        }
    }


    private void getRecycleOrderDetail() {
        showLoadingDialog();
        RecycleApi.get().getRecycleOrderDetail(this, orderid, new ApiConfig.ApiRequestListener<RecycleOrderInfo>() {
            @Override
            public void onSuccess(String msg, RecycleOrderInfo data) {
                dismissLoadingDialog();

                if (data == null) {
                    return;
                }

                tvFinishedDate.setText(DateUtil.timeFormatToMinnue(StringUtil.stringNullFilter(data.recycleTime)));
                tvAppointmentDate.setText(data.rangeDate.replace("-", ".") + " " + data.rangeTime);
                tvUserName.setText(data.publisher);
                tvPhoneNum.setText(data.mobile);
                tvAddress.setText(data.address);

                if (RecycleApi.orderStatus.cancel.toString().equals(data.statue)) {
//                    tvGainPoint.setText("0积分");
                } else {
                    findViewById(R.id.rlGainPoint).setVisibility(View.VISIBLE);
                    findViewById(R.id.rlCuttingLine).setVisibility(View.VISIBLE);
                    findViewById(R.id.llRecycleInfoList).setVisibility(View.VISIBLE);
                    tvGainPoint.setText((Integer.parseInt(data.point) >= 1000 ? 1000 : data.point) + "益币");
                }

                for (RecycleApi.orderStatus status : RecycleApi.orderStatus.values()) {
                    if (status.toString().equals(data.statue)) {
                        tvOrderStatus.setText(status.getStrName());
                        tvOrderStatus.setTextColor(getResources().getColor(status.getColor()));
                    }
                }

                for (int i = 0; i < data.recycleDetails.size(); i++) {
                    RecycleGarbageInfo garbageInfo = data.recycleDetails.get(i);
                    View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recycle_detail, null);
                    Log.e("--获得益币","== "+garbageInfo.point);
                    ((TextView) itemView.findViewById(R.id.tvRecycleType)).setText(garbageInfo.garbage);
                    ((TextView) itemView.findViewById(R.id.tvRecycleQuantity)).setText("x" + garbageInfo.quantity);
                    ((TextView) itemView.findViewById(R.id.tvRecyclePoint)).setText((Integer.parseInt(garbageInfo.point) >= 1000 ?
                            1000 : garbageInfo.point) + "益币");
                    llRecycleInfoList.addView(itemView);
                }

                if (data.recycleDetails.size() > 0) {
                    View cuttingView = LayoutInflater.from(mContext).inflate(R.layout.view_cutting_line_1px, llRecycleInfoList,false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    params.setMargins(0, Utils.dp2px(mContext, 10), 0, 0);
                    cuttingView.setLayoutParams(params);
                    llRecycleInfoList.addView(cuttingView);
                }
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }
}


