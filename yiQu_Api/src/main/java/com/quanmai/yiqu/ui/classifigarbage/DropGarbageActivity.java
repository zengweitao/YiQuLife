package com.quanmai.yiqu.ui.classifigarbage;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.RecycleApi;
import com.quanmai.yiqu.api.vo.GarbageClassifyInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.ui.booking.GarbageQRCodeActivity;
import com.quanmai.yiqu.ui.booking.GarbageThrowRecordActivity;
import com.quanmai.yiqu.ui.common.WebActivity;

import java.util.List;

/**
 * 垃圾投放界面
 * */
public class DropGarbageActivity extends BaseActivity {

    private LinearLayout llChooseList;
    private TextView tv_title;
    private TextView tv_right;
    private String strCode; //垃圾袋编码
    private ImageView imageViewBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_garbage);

        initView();
        getGarbageClassifyList();

        imageViewBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("http_url", ApiConfig.DROP_LAJI_URL);
                intent.putExtra("title","使用说明");
                startActivity(intent);
            }
        });
    }

    private void initView() {
        llChooseList = (LinearLayout)findViewById(R.id.llChooseList);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_right = (TextView) findViewById(R.id.tv_right);
        imageViewBanner = (ImageView)findViewById(R.id.imageViewBanner);

        tv_title.setText("垃圾投放");
        tv_right.setText("投放记录");
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DropGarbageActivity.this,GarbageThrowRecordActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });
    }

    /***
     * 8168获取可回收垃圾分类信息
     * */
    private void getGarbageClassifyList() {
        RecycleApi.get().getRecyclerGarbageClassifyList(mContext, "1",new ApiConfig.ApiRequestListener<List<GarbageClassifyInfo>>() {
            @Override
            public void onSuccess(String msg, List<GarbageClassifyInfo> data) {
                dismissLoadingDialog();
                if (data == null || data.size() == 0) {
                    return;
                }
                initList(data);

            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    private void initList(List<GarbageClassifyInfo> classifyInfos) {
        for (GarbageClassifyInfo classifyInfo : classifyInfos) {
            View childView = LayoutInflater.from(mContext).inflate(R.layout.item_garbage_throw_list, null);
            TextView tvGarbageType = (TextView) childView.findViewById(R.id.tvGarbageType);
            CheckedTextView tvConfirm = (CheckedTextView) childView.findViewById(R.id.tvConfirm);

            tvGarbageType.setText(classifyInfo.name);
            tvConfirm.setTag(classifyInfo);
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GarbageClassifyInfo info = (GarbageClassifyInfo) v.getTag();
                    getRecyclerGarbageCode(info.number, info.name);
                }
            });
            llChooseList.addView(childView);
        }
    }

    /**
     * 8169后台生成可回收垃圾编码
     * */
    private void getRecyclerGarbageCode(String bagtype,final String garbagname) {
        RecycleApi.get().getRecyclerGarbageCode(mContext, bagtype, garbagname, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                strCode = data;
                Intent intent = new Intent(mContext, GarbageQRCodeActivity.class);
                intent.putExtra("type","dorpgarbage");
                intent.putExtra("strCode",strCode);
                intent.putExtra("garbagname",garbagname);
                startActivity(intent);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }
}
