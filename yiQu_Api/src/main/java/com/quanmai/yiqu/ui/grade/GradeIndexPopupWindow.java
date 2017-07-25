package com.quanmai.yiqu.ui.grade;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quanmai.yiqu.R;

/**
 * Created by 95138 on 2016/11/10.
 */

public class GradeIndexPopupWindow extends PopupWindow implements View.OnClickListener{
    View mMenuView;
    RelativeLayout rlClassification,rlEquipment,rlClean,rlPublish;
    Context mContext;

    public GradeIndexPopupWindow(Activity context){
        //获得popup布局
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.grade_index_popup_intem, null);
        mContext = context;

        //初始化控件
        initView();
        initPopWindow();
    }

    private void initView() {
        rlClassification = (RelativeLayout)mMenuView.findViewById(R.id.rlClassification);
        rlEquipment = (RelativeLayout)mMenuView.findViewById(R.id.rlEquipment);
        rlClean = (RelativeLayout)mMenuView.findViewById(R.id.rlClean);
        rlPublish = (RelativeLayout)mMenuView.findViewById(R.id.rlPublish);

        rlClassification.setOnClickListener(this);
        rlEquipment.setOnClickListener(this);
        rlClean.setOnClickListener(this);
        rlPublish.setOnClickListener(this);
    }

    private void initPopWindow() {
        //设置PopupWindow的View
        this.setContentView(mMenuView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnim);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置PopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            //分类评分记录
            case R.id.rlClassification:{
                intent = new Intent(mContext,GradeManagerActivity.class);
                mContext.startActivity(intent);
                dismiss();
                break;
            }
            //设施评分记录
            case R.id.rlEquipment:{
                intent = new Intent(mContext,ChooseScoreCommunityActivity.class);
                intent.putExtra("ScoreType",ChooseScoreCommunityActivity.TYPE_EQUIPMENT_SCORE_RECORD);
                mContext.startActivity(intent);
                dismiss();
                break;
            }
            //清运评分记录
            case R.id.rlClean:{
                intent = new Intent(mContext,ChooseScoreCommunityActivity.class);
                intent.putExtra("ScoreType",ChooseScoreCommunityActivity.TYPE_CLEAN_SCORE_RECORD);
                mContext.startActivity(intent);
                dismiss();
                break;
            }
            case R.id.rlPublish:{
                Toast.makeText(mContext.getApplicationContext(),"开发中",Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            }
            default:break;
        }
    }
}
