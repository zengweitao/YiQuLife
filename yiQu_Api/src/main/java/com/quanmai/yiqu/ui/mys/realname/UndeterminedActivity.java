package com.quanmai.yiqu.ui.mys.realname;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.StringUtil;

import java.util.Map;

/**
 * 住户申请入户、申诉、申请取消绑定 待审核页面
 */
public class UndeterminedActivity extends BaseActivity implements View.OnClickListener {
    public static final int TYPE_APPEAL = 0; //申请类型-住户申诉
    public static final int TYPE_UNBIND = 1; //申请类型-取消绑定
    public static final int TYPE_APPLY = 3; //申请类型-申请入户

    private android.widget.TextView tvName;
    private android.widget.TextView tvPhone;
    private android.widget.TextView tvStatus;
    private android.widget.TextView tvAddress;
    private android.widget.Button btnCancel;

    private String mName = ""; //住户姓名
    private String mPhone = ""; //住户手机号
    private String mAddress = ""; //住户住房地址
    private int type; //申请类型
    private String appealid = ""; //申诉表id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undetermined);
        initView();
        init();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("绑定住户");
        findViewById(R.id.iv_back).setOnClickListener(this);

        this.btnCancel = (Button) findViewById(R.id.btnCancel);
        this.tvAddress = (TextView) findViewById(R.id.tvAddress);
        this.tvStatus = (TextView) findViewById(R.id.tvStatus);
        this.tvPhone = (TextView) findViewById(R.id.tvPhone);
        this.tvName = (TextView) findViewById(R.id.tvName);

        btnCancel.setOnClickListener(this);
    }

    private void init() {
        Intent intent = this.getIntent();
        if (intent.hasExtra("type")) type = intent.getIntExtra("type", 0);
        if (intent.hasExtra("name")) mName = getIntent().getStringExtra("name");
        if (intent.hasExtra("phone")) mPhone = getIntent().getStringExtra("phone");
        if (intent.hasExtra("address")) mAddress = getIntent().getStringExtra("address");
        if (intent.hasExtra("status")) {
            tvStatus.setText(intent.getStringExtra("status"));
        } else {
            tvStatus.setText("（待审核）");
        }

        if (type == TYPE_APPEAL) {
            getResidentAppealInfo();
        } else if (type == TYPE_UNBIND) {
            tvName.setText(mName);
            tvPhone.setText(StringUtil.phoneEncrypt(mPhone));
            tvAddress.setText(mAddress);
            btnCancel.setText("取消申请解绑");
        } else if (type == TYPE_APPLY) {
            tvName.setText(mName);
            tvPhone.setText(StringUtil.phoneEncrypt(mPhone));
            tvAddress.setText(mAddress);
        }
    }

    //获取实名制申诉信息
    private void getResidentAppealInfo() {
        UserApi.get().getResidentAppealInfo(mContext, new ApiConfig.ApiRequestListener<Map<String, String>>() {
            @Override
            public void onSuccess(String msg, Map<String, String> data) {
                if (data == null || data.size() <= 0) {
                    showCustomToast(msg);
                    return;
                }
                mName = data.get("username");
                mPhone = data.get("tel");
                mAddress = data.get("address");
                appealid = data.get("appealid");

                tvName.setText(mName);
                tvPhone.setText(StringUtil.phoneEncrypt(mPhone));
                tvAddress.setText(mAddress);
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    //取消实名制申诉
    private void cancelResidentAppeal(String appealid) {
        showLoadingDialog();
        UserApi.get().cancelResidentAppeal(mContext, appealid, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast(data);
                finish();
                startActivity(ResidentBindingActivity.class);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //取消申请绑定
    private void cancelResidentUnbind() {
        showLoadingDialog();
        UserApi.get().cancelResidentUnbind(mContext, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast(data);

                Intent intent = getIntent();
                intent.setClass(mContext, BindingInfoActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //取消申请入户
    private void cancelApply() {
        showLoadingDialog();
        UserApi.get().membersCancelApply(mContext, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast(data);
                finish();
                startActivity(ResidentBindingActivity.class);
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
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.btnCancel: {
                switch (type) {
                    case TYPE_APPEAL: {
                        cancelResidentAppeal(appealid);
                        break;
                    }
                    case TYPE_UNBIND: {
                        cancelResidentUnbind();
                        break;
                    }
                    case TYPE_APPLY: {
                        cancelApply();
                        break;
                    }
                }
                break;
            }

            default:
                break;

        }
    }
}
