package com.quanmai.yiqu.ui.grade;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GradeApi;
import com.quanmai.yiqu.api.vo.GradeScoreDetailsInfo;
import com.quanmai.yiqu.api.vo.GradeScoreInfo;
import com.quanmai.yiqu.api.vo.ScoreCommDetailInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.CircularImageView;
import com.quanmai.yiqu.ui.transaction.LookupBigPicActivity;

import java.util.ArrayList;

/**
 * 设备or清运评分记录详情
 */
public class GradeScoreDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private TextView tvCommunity;               //小区名
    private TextView tvEquipment;               //设施点名称
    private CircularImageView imgEquipment1;    //评分图1
    private CircularImageView imgEquipment2;    //评分图2
    private CircularImageView imgEquipment3;    //评分图3
    private RelativeLayout rlEquipment;         //设施评分标题
    private RelativeLayout rlClean;             //清运评分标题
    private LinearLayout llList;                //评分列表

    private int type;
    private String imgs;                        //图片链接
    private ArrayList<String> imgList;          //图片链接集合
    private GradeScoreInfo mScoreInfo;          //评分记录实体
    private ScoreCommDetailInfo mCommInfo;    //巡检-选择小区-小区详细信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_score_detail);
        initView();
        init();
        getGradeScoreDetails();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("");
        tvCommunity = (TextView) findViewById(R.id.tvCommunity);
        tvEquipment = (TextView) findViewById(R.id.tvEquipment);
        imgEquipment1 = (CircularImageView) findViewById(R.id.imgEquipment1);
        imgEquipment2 = (CircularImageView) findViewById(R.id.imgEquipment2);
        imgEquipment3 = (CircularImageView) findViewById(R.id.imgEquipment3);
        rlEquipment = (RelativeLayout) findViewById(R.id.rlEquipment);
        rlClean = (RelativeLayout) findViewById(R.id.rlClean);
        llList = (LinearLayout) findViewById(R.id.llList);

    }

    private void init() {
        type = getIntent().getIntExtra("ScoreType", GradeScoreActivity.TYPE_EQUIPMENT_SCORE_RECORD);
        mScoreInfo = (GradeScoreInfo) getIntent().getSerializableExtra("GradeScoreInfo");
        if (type == GradeScoreActivity.TYPE_EQUIPMENT_SCORE_RECORD) {
            rlEquipment.setVisibility(View.VISIBLE);
            tv_title.setText("设备评分记录详情");
            tvEquipment.setText(mScoreInfo.amenityareaName + "-" + mScoreInfo.amenityName); //设施点+设施名
        } else {
            rlClean.setVisibility(View.VISIBLE);
            tv_title.setText("清运评分记录详情");
            tvEquipment.setText(mScoreInfo.amenityareaName);
        }

        mCommInfo = (ScoreCommDetailInfo) getIntent().getSerializableExtra("CommInfo");
        tvCommunity.setText(mCommInfo.getCommname()); //小区名

        imgs = mScoreInfo.checkImg;
        initImg(imgs);
    }

    //扣分记录详情列表查询
    private void getGradeScoreDetails() {
        showLoadingDialog();
        GradeApi.get().gradeScoreDetails(mContext, type == GradeScoreActivity.TYPE_EQUIPMENT_SCORE_RECORD ? 1 : 2, mScoreInfo.id, new ApiConfig.ApiRequestListener<CommonList<GradeScoreDetailsInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<GradeScoreDetailsInfo> data) {
                dismissLoadingDialog();
                if (data == null) {
                    return;
                }
                initScoreList(data, type == GradeScoreActivity.TYPE_EQUIPMENT_SCORE_RECORD);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    private void initImg(String strImg) {
        if (TextUtils.isEmpty(strImg)) {
            return;
        }

        imgList = new ArrayList<>();
        String[] strings = strImg.trim().split(",");
        for (int i = 0; i < strings.length; i++) {
            imgList.add(strings[i]);
            switch (i) {
                case 0: {
                    ImageloaderUtil.displayImage(mContext, strings[0], imgEquipment1);
                    imgEquipment1.setVisibility(View.VISIBLE);
                    imgEquipment1.setTag(i);
                    imgEquipment1.setOnClickListener(this);
                    break;
                }
                case 1: {
                    ImageloaderUtil.displayImage(mContext, strings[1], imgEquipment2);
                    imgEquipment2.setVisibility(View.VISIBLE);
                    imgEquipment2.setTag(i);
                    imgEquipment2.setOnClickListener(this);
                    break;
                }
                case 2: {
                    ImageloaderUtil.displayImage(mContext, strings[2], imgEquipment3);
                    imgEquipment3.setVisibility(View.VISIBLE);
                    imgEquipment3.setTag(i);
                    imgEquipment3.setOnClickListener(this);
                    break;
                }
                default:
                    break;
            }
        }
    }

    private void lookUpBigPic(int position) {
        Intent intent = new Intent();
        intent.setClass(mContext, LookupBigPicActivity.class);
        intent.putExtra("current", position);
        intent.putStringArrayListExtra("piclist", imgList);
        startActivity(intent);
    }

    //初始化评分列表
    private void initScoreList(CommonList<GradeScoreDetailsInfo> list, Boolean isShowNum) {
        if (list == null || list.size() == 0) {
            return;
        }

        for (GradeScoreDetailsInfo info : list) {
            View childView = LayoutInflater.from(mContext).inflate(R.layout.item_grade_score_detail, null);
            TextView tvEquipmentName = (TextView) childView.findViewById(R.id.tvEquipmentName);
            TextView tvEquipmentNum = (TextView) childView.findViewById(R.id.tvEquipmentNum);
            TextView tvScore = (TextView) childView.findViewById(R.id.tvScore);

            tvEquipmentName.setText(info.gitName);
            tvScore.setText("-" + info.score);
            if (isShowNum) {
                tvEquipmentNum.setVisibility(View.VISIBLE);
                tvEquipmentNum.setText(info.num + "");
            } else {
                tvEquipmentNum.setVisibility(View.GONE);
            }
            llList.addView(childView);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgEquipment1: {
                lookUpBigPic((Integer) v.getTag());
                break;
            }
            case R.id.imgEquipment2: {
                lookUpBigPic((Integer) v.getTag());
                break;
            }
            case R.id.imgEquipment3: {
                lookUpBigPic((Integer) v.getTag());
                break;
            }
            default:
                break;
        }
    }
}
