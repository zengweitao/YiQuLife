package com.quanmai.yiqu.ui.integration;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.StringUtil;

import java.text.SimpleDateFormat;

public class ShowQRCodeActivity extends BaseActivity {

    TextView tv_title;
    ImageView imageViewQRcode;
    TextView textViewTime;
    String qrcode="";
    public static boolean isForeground = false;
    public static String mId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode);

        if (getIntent().hasExtra("qrcode")){
            qrcode = getIntent().getStringExtra("qrcode");
        }

        if (getIntent().hasExtra("id")){
            mId = getIntent().getStringExtra("id");
        }
        init();

        if (!TextUtils.isEmpty("qrcode")){
            imageViewQRcode.setImageBitmap(StringUtil.stringToBitmap(qrcode));
        }

        timer.start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }

    private void init() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        textViewTime = (TextView)findViewById(R.id.textViewTime);
        imageViewQRcode = (ImageView)findViewById(R.id.imageViewQRcode);
        tv_title.setText("扫描二维码");
        textViewTime.setText("5分钟");
    }

    private CountDownTimer timer = new CountDownTimer(5*60*1000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {

            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");//初始化Formatter的转换格式。

            String hms = formatter.format(millisUntilFinished);

            textViewTime.setText(hms);
        }

        @Override
        public void onFinish() {
            timer.cancel();
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
