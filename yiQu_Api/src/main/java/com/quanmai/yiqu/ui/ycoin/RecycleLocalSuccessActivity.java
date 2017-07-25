package com.quanmai.yiqu.ui.ycoin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.RecycleGarbagesInfo;
import com.quanmai.yiqu.base.BaseActivity;

import java.util.List;

/**
 * 现场回收——回收成功页面
 */
public class RecycleLocalSuccessActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title;
    private LinearLayout llRecycleLocal;
    private Button buttonOK;

    private  List<RecycleGarbagesInfo> garbageInfoList;
    private int totalPoint; //预计能获取的积分数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_local_success);
        initView();
        init();
        tv_title.setText("回收成功");
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        llRecycleLocal = (LinearLayout) findViewById(R.id.llRecycleLocal);
        buttonOK = (Button) findViewById(R.id.buttonOK);

        iv_back.setOnClickListener(this);
        buttonOK.setOnClickListener(this);
    }

    private void init() {
        if (getIntent().hasExtra("TotalPoint")) {
            totalPoint = getIntent().getIntExtra("TotalPoint", 0);
        }
        if (getIntent().hasExtra("GarbageInfoList")) {
            garbageInfoList = (List<RecycleGarbagesInfo>) getIntent().getSerializableExtra("GarbageInfoList");
            initGarbageList();
        }
    }

    private void initGarbageList() {
        for (RecycleGarbagesInfo garbageInfo : garbageInfoList) {
            View viewGarbage = LayoutInflater.from(mContext).inflate(R.layout.item_recycle_garbage, null);
            TextView tvGarbageName = (TextView) viewGarbage.findViewById(R.id.tvGarbageName);
            TextView tvGarbageNum = (TextView) viewGarbage.findViewById(R.id.tvGarbageNum);
            TextView tvGetIntegral = (TextView) viewGarbage.findViewById(R.id.tvGetIntegral);
            tvGarbageName.setText(garbageInfo.garbage);
            tvGarbageNum.setText("x" + garbageInfo.quantity);
            tvGetIntegral.setText(garbageInfo.point + "益币");
            llRecycleLocal.addView(viewGarbage);
        }
        View viewTotal = LayoutInflater.from(mContext).inflate(R.layout.item_recycle_garbage_total, null);
        ((TextView) viewTotal.findViewById(R.id.tvTotalIntegral)).setText(totalPoint + "益币");
        llRecycleLocal.addView(viewTotal);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.buttonOK:
                finish();
                break;
        }
    }
}
