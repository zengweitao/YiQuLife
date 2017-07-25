package com.quanmai.yiqu.ui.mys.handwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.HandworkApi;
import com.quanmai.yiqu.api.vo.CompanyInfo;
import com.quanmai.yiqu.base.BaseActivity;

import java.util.List;

/**
 * 选择送货单位页面
 */
public class UnitChooseActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title;
    private LinearLayout llMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_choose);
        initView();
        init();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("选择送货单位");
        llMain = (LinearLayout) findViewById(R.id.llMain);

        iv_back.setOnClickListener(this);
    }

    private void init() {
        getUnitListInfo();
    }

    //获取送货单位列表信息
    private void getUnitListInfo() {
        showLoadingDialog();
        HandworkApi.get().unitListInfo(mContext, new ApiConfig.ApiRequestListener<List<CompanyInfo>>() {
            @Override
            public void onSuccess(String msg, List<CompanyInfo> data) {
                dismissLoadingDialog();
                if (data == null || data.size() == 0) {
                    return;
                }
                refresh(data);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    private void refresh(final List<CompanyInfo> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_unit_choose, null);
            TextView tvUnit = (TextView) itemView.findViewById(R.id.tvUnit);
            tvUnit.setText(list.get(i).companyname);
            tvUnit.setTag(list.get(i));
            tvUnit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    intent.putExtra("companyInfo",(CompanyInfo) v.getTag());
                    setResult(RESULT_OK, intent);
                    UnitChooseActivity.this.finish();
                }
            });
            llMain.addView(itemView);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            default:
                break;
        }
    }
}
