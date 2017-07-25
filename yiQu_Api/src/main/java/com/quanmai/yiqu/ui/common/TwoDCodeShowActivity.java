package com.quanmai.yiqu.ui.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseActivity;

public class TwoDCodeShowActivity extends BaseActivity {
    private TextView textview_scan_result, tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_dcode_show);
        textview_scan_result = (TextView) findViewById(R.id.textview_scan_result);
        textview_scan_result.setText(getIntent().getStringExtra("resultString"));
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("扫描结果");
    }
}
