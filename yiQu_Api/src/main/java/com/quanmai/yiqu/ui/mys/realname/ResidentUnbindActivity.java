package com.quanmai.yiqu.ui.mys.realname;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.StringUtil;

/**
 * Created by James on 2016/7/15.
 * 申请解绑页面
 */
public class ResidentUnbindActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvName;
    private TextView tvPhone;
    private TextView tvAddress;
    private EditText edtReason;
    private Button btnConfirm;

    private String mName; //住户姓名
    private String mPhone; //住户手机号
    private String mAddress; //住户住房地址
    private String usercompareid; //实名制住户信息表id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_appeal);
        initView();
        init();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("申请解绑");
        findViewById(R.id.iv_back).setOnClickListener(this);

        this.btnConfirm = (Button) findViewById(R.id.btnConfirm);
        this.edtReason = (EditText) findViewById(R.id.edtReason);
        this.tvAddress = (TextView) findViewById(R.id.tvAddress);
        this.tvPhone = (TextView) findViewById(R.id.tvPhone);
        this.tvName = (TextView) findViewById(R.id.tvName);

        btnConfirm.setOnClickListener(this);
    }

    private void init() {
        mName = getIntent().getStringExtra("name");
        mPhone = getIntent().getStringExtra("phone");
        mAddress = getIntent().getStringExtra("address");
        usercompareid = getIntent().getStringExtra("usercompareid");

        edtReason.setHint("请填写您的解绑原因");
        tvName.setText(mName);
        tvPhone.setText(StringUtil.phoneEncrypt(mPhone));
        if (!TextUtils.isEmpty(mAddress)) {
            tvAddress.setText(mAddress);
        }
    }

    //申请解绑
    private void residentUnbind(String usercompareid, String description) {
        showLoadingDialog();
        UserApi.get().residentUnbind(mContext, usercompareid, description, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast(data);
                Intent intent = getIntent();
                intent.setClass(mContext, UndeterminedActivity.class);
                intent.putExtra("type", UndeterminedActivity.TYPE_UNBIND);
                startActivity(intent);

                setResult(Activity.RESULT_OK, intent);
                finish();
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
            case R.id.btnConfirm: {
                if (TextUtils.isEmpty(edtReason.getText().toString())) {
                    showCustomToast("请填写您的申请原因");
                    return;
                }
                residentUnbind(usercompareid, edtReason.getText().toString());
                break;
            }

            default:
                break;

        }
    }
}
