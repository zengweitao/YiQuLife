package com.quanmai.yiqu.ui.booking;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseActivity;

public class NotMyCommunityActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_my_community);

        ((TextView)findViewById(R.id.tv_title)).setText("不是我所在的小区怎么办");
    }

}
