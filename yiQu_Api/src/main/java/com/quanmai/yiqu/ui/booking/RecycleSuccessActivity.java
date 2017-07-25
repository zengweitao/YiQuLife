package com.quanmai.yiqu.ui.booking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.RecycleApi;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.ui.fragment.PersonalFragment;

public class RecycleSuccessActivity extends BaseActivity {

    TextView tv_title;
    TextView textViewHint;
    TextView textViewGetIntegral;
    TextView textViewGetExperience;
    TextView textViewTotalIntegral;
    Button buttonOK;
    ImageView iv_back;

    String point = ""; //积分
    String orderid = "";
    String mature = ""; //成长值
    String totalPoint = ""; //总积分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_successed);

        Intent intent = getIntent();

        if (intent.hasExtra("orderId")) orderid = intent.getStringExtra("orderId");

        if (intent.hasExtra("status") && intent.getStringExtra("status").equals("completed")) {
            String list[] = orderid.split(";");
            orderid = list[0];
            point = list[1];
            totalPoint = list[2];
            UserInfo.get().setScore((int) Double.parseDouble(totalPoint));
        } else {
            if (intent.hasExtra("point")) {
                point = intent.getStringExtra("point");
                totalPoint = String.valueOf(UserInfo.get().getAmount() + Integer.parseInt(point));
                UserInfo.get().setScore(Integer.parseInt(totalPoint));
            }
        }

        if (intent.hasExtra("receivedPoint")) {
            point = intent.getStringExtra("receivedPoint");
        }

        if (intent.hasExtra("receivedMature")) {
            mature = intent.getStringExtra("receivedMature");
        }

        if (intent.hasExtra("totalPoint")) {
            totalPoint = intent.getStringExtra("totalPoint");
        }


        init();
        //完成
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();

                RecycleApi.get().confirmAppointmentOrder(RecycleSuccessActivity.this, orderid, "", new ApiConfig.ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
                        dismissLoadingDialog();
                        showCustomToast(msg);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(PersonalFragment.ACTION_NETWORKING_TO_REFRESH_DATA));
                        finish();
                    }
                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();
                        showCustomToast(msg);
                        finish();
                    }
                });
            }
        });

    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        textViewHint = (TextView) findViewById(R.id.textViewHint);
        textViewGetIntegral = (TextView) findViewById(R.id.textViewGetIntegral);
        textViewGetExperience = (TextView) findViewById(R.id.textViewGetExperience);
        textViewTotalIntegral = (TextView) findViewById(R.id.textViewTotalIntegral);
        buttonOK = (Button) findViewById(R.id.buttonOK);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        tv_title.setText("回收成功");
        if ( Double.parseDouble(point)>1000){
            textViewGetIntegral.setText("1000");
        }else {
            textViewGetIntegral.setText(((int) Double.parseDouble(point)) + "");
        }
        textViewGetExperience.setText(((int) Double.parseDouble(point)) + "");
        textViewTotalIntegral.setText(((int) Double.parseDouble(totalPoint)) + "");
        iv_back.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
