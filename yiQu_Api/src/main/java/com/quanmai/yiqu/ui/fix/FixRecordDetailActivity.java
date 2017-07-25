package com.quanmai.yiqu.ui.fix;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.vo.FixInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.ImageloaderUtil;

public class FixRecordDetailActivity extends BaseActivity {
    ImageView iv_pic;
    TextView tv_time, tv_problem, tv_phone, tv_address, tv_deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_detail);
        init();
    }

    private void init() {
        ((TextView) findViewById(R.id.tv_title)).setText("报修详情");
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        tv_time = ((TextView) findViewById(R.id.tv_time));
        tv_phone = ((TextView) findViewById(R.id.tv_phone));
        tv_address = ((TextView) findViewById(R.id.tv_address));
        tv_problem = ((TextView) findViewById(R.id.tv_problem));
        tv_deal = ((TextView) findViewById(R.id.tv_deal));
        fixManageDetail();
        // if(!"".equals(getIntent().getStringExtra("reason_type")) &&
        // getIntent().getStringExtra("reason_type")!=null)
        // ((TextView)
        // findViewById(R.id.tv_reason_type)).setText(getIntent().getStringExtra("reason_type"));
        // else
        // ((TextView)
        // findViewById(R.id.tv_reason_type)).setVisibility(View.GONE);
    }

    private void FixAttend() {
        showLoadingDialog("请稍候");
        Api.get().FixAttend(mContext, getIntent().getStringExtra("service_id"),
                new ApiRequestListener<String>() {

                    @Override
                    public void onSuccess(String msg, String data) {
                        dismissLoadingDialog();
                        showCustomToast(msg);
                        setResult(RESULT_OK);
                        finish();

                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();
                        showCustomToast(msg);

                    }
                });

    }

    private void fixManageDetail() {
        showLoadingDialog("请稍候");
        Api.get().FixManageDetail(mContext,
                getIntent().getStringExtra("service_id"),
                new ApiRequestListener<FixInfo>() {

                    @Override
                    public void onSuccess(String msg, FixInfo data) {
                        dismissLoadingDialog();
                        ImageloaderUtil.displayImage(mContext, data.picurl, iv_pic);
                        tv_time.setText(data.come_time);
                        tv_problem.setText("问题:" + data.description);
                        tv_phone.setText("手机:" + data.phone);
                        tv_address.setText("地址:" + data.address);
                        if (getIntent().getIntExtra("type", 0) == 1
                                && data.result == 0) {
                            tv_deal.setVisibility(View.VISIBLE);
                            tv_deal.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    FixAttend();
                                }
                            });
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