package com.quanmai.yiqu.ui.mys.realname;

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
import com.quanmai.yiqu.api.vo.RoomInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.StringUtil;

/**
 * 住户申诉页面
 */
public class ResidentAppealActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvName;
    private TextView tvPhone;
    private TextView tvAddress;
    private EditText edtReason;
    private Button btnConfirm;

    private String mName; //住户姓名
    private String mPhone; //住户手机号
    private RoomInfo mRoomInfo;  //住户住房地址实体


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_appeal);
        initView();
        init();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("申诉");
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
        mRoomInfo = (RoomInfo) getIntent().getSerializableExtra("roominfo");

        tvName.setText(mName);
        tvPhone.setText(StringUtil.phoneEncrypt(mPhone));
        if (mRoomInfo != null) {
            tvAddress.setText(mRoomInfo.toString());
        }
    }

    //实名制用户申诉提交
    private void residentAppeal(String name, String commcode, String buildingno, String unit, String room, String description) {
        showLoadingDialog();
        UserApi.get().residentAppeal(mContext, name, commcode, buildingno, unit, room, description,
                new ApiConfig.ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
                        dismissLoadingDialog();
                        showCustomToast(data);
                        Intent intent = new Intent(mContext, UndeterminedActivity.class);
                        intent.putExtra("name", mName);
                        intent.putExtra("phone", mPhone);
                        intent.putExtra("roominfo", mRoomInfo);
                        intent.putExtra("appealstatus", "0");
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.btnConfirm: {
                if (TextUtils.isEmpty(edtReason.getText())) {
                    showCustomToast("请填写您的申诉原因");
                    return;
                }
                residentAppeal(mName, mRoomInfo.commcode, mRoomInfo.buildingno, mRoomInfo.buildno, mRoomInfo.room,
                        edtReason.getText().toString());
                break;
            }

            default:
                break;

        }
    }
}
