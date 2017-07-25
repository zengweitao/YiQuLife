package com.quanmai.yiqu.ui.booking.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.RecycleApi;
import com.quanmai.yiqu.api.vo.GarbageClassifyInfo;
import com.quanmai.yiqu.base.BaseFragment;
import com.quanmai.yiqu.ui.booking.GarbageQRCodeActivity;
import com.quanmai.yiqu.ui.common.WebActivity;

import java.util.List;

/**
 * 废品投放 fragment
 * Created by James on 2016/12/29.
 */
public class GarbageThrowFragment extends BaseFragment {
    private View mContentView;
    private TextView tvChooseTitle;
    private TextView tvChooseExplain;
    private LinearLayout llChooseList;
    private ImageView imageViewBanner;

    private String strCode; //垃圾袋编码

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.fragment_garbage_throw, null);
        initView(mContentView);
        getGarbageClassifyList();

        imageViewBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("http_url", ApiConfig.DROP_GARBAGE_URL);
                intent.putExtra("title","使用说明");
                startActivity(intent);
            }
        });
        return mContentView;
    }

    private void initView(View mContentView) {
        tvChooseTitle = (TextView) mContentView.findViewById(R.id.tvChooseTitle);
        tvChooseExplain = (TextView) mContentView.findViewById(R.id.tvChooseExplain);
        llChooseList = (LinearLayout) mContentView.findViewById(R.id.llChooseList);
        imageViewBanner = (ImageView)mContentView.findViewById(R.id.imageViewBanner);
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

    //获取垃圾分类信息列表
    private void getGarbageClassifyList() {
        RecycleApi.get().getRecyclerGarbageClassifyList(mContext, "0",new ApiConfig.ApiRequestListener<List<GarbageClassifyInfo>>() {
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

    //生成可回收垃圾编码
    private void getRecyclerGarbageCode(String bagtype,final String garbagname) {
        RecycleApi.get().getRecyclerGarbageCode(mContext, bagtype, garbagname, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                strCode = data;
                Intent intent = new Intent(mContext, GarbageQRCodeActivity.class);
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

