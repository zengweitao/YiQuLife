package com.quanmai.yiqu.ui.ycoin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.IntegralApi;
import com.quanmai.yiqu.api.vo.AppointedUserInfo;
import com.quanmai.yiqu.api.vo.AwardRecordInfo;
import com.quanmai.yiqu.base.BaseActivity;

/**
 * 赠送礼品-确认赠送页面
 */
public class GivingConfirmActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title;
    private TextView tvGiftName;
    private TextView tvGiftPrice;
    private TextView tvGiftUser;
    private Button btnConfirm;

    private AwardRecordInfo awardRecordInfo;
    private String houseCode = ""; //扫描——用户号
    private String account = "";   //扫描——手动输入手机号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giving_confirm);
        initView();
        init();
        getAppointedUserInfo(houseCode, account);
        tv_title.setText("确认赠送");
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tvGiftName = (TextView) findViewById(R.id.tvGiftName);
        tvGiftPrice = (TextView) findViewById(R.id.tvGiftPrice);
        tvGiftUser = (TextView) findViewById(R.id.tvGiftUser);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(this);
    }

    private void init() {
        if (getIntent().hasExtra("AwardRecordInfo")) {
            awardRecordInfo = (AwardRecordInfo) getIntent().getSerializableExtra("AwardRecordInfo");
        }
        tvGiftName.setText(awardRecordInfo.getGiftName());
        tvGiftPrice.setText(awardRecordInfo.getAmount());

        if (getIntent().hasExtra("houseCode")) {
            houseCode = getIntent().getStringExtra("houseCode");
        }
        if (getIntent().hasExtra("account")) {
            account = getIntent().getStringExtra("account");
        }

//        if (!TextUtils.isEmpty(account)) {
//            tvGiftUser.setText(account);
//        }
    }

    //获取指定用户信息
    private void getAppointedUserInfo(String houseCode, final String tel) {
        showLoadingDialog();
        IntegralApi.get().appointedUserInfo(mContext, houseCode, tel, new ApiConfig.ApiRequestListener<AppointedUserInfo>() {
            @Override
            public void onSuccess(String msg, AppointedUserInfo data) {
                dismissLoadingDialog();
                if (data == null) {
                    return;
                }
                if (!TextUtils.isEmpty(data.getHousecode())) {
                    tvGiftUser.setText(data.getHousecode());
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        });
    }

    //赠送礼品
    private void giftGiving(String operator, String giftId, String nums, final String houseCode, String account) {
        showLoadingDialog();
        IntegralApi.get().giftGiving(mContext, operator, giftId, nums, houseCode, account, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast("赠送成功");
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
                giftGiving(mSession.getUsername(), awardRecordInfo.getId(), "", houseCode, account);
                break;
            }
        }
    }
}
