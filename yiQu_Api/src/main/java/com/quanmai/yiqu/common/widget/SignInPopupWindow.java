package com.quanmai.yiqu.common.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.quanmai.yiqu.R;

/**
 * Created by yin on 16/3/30.
 */
public class SignInPopupWindow extends PopupWindow {
    private View mMenuView;
    Button buttonConfirm;
    TextView textViewIntegral, textViewSign, textViewSignDay, textViewSignIntegral;
    Button buttonLevel;
    CircleView circleViewSign;
    TextView textViewExperience;

    public SignInPopupWindow(Activity context){
        //获得popup布局
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.activity_sign_success, null);

        //初始化控件
        initView();
        initPopWindow();

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
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

    private void initView() {
        buttonConfirm = (Button) mMenuView.findViewById(R.id.buttonConfirm);
        textViewIntegral = (TextView) mMenuView.findViewById(R.id.textViewLeft);
        textViewSign = (TextView) mMenuView.findViewById(R.id.textViewSign);
        textViewSignDay = (TextView) mMenuView.findViewById(R.id.textViewSignDay);
        textViewSignIntegral = (TextView) mMenuView.findViewById(R.id.textViewSignIntegral);
        buttonLevel = (Button)mMenuView.findViewById(R.id.tvLevel);
        circleViewSign = (CircleView) mMenuView.findViewById(R.id.circleViewSign);
        textViewExperience = (TextView) mMenuView.findViewById(R.id.textViewExperience);

    }

    public void setData(String signpoint, String signDay, String score, String level){
        if (signDay.equals("1")) {
            textViewSign.setText("已签到");
        } else {
            textViewSign.setText("已连续签到");
        }
        textViewSignDay.setText(signDay+"天");
        textViewSignIntegral.setText("积分：" + score);
        textViewIntegral.setText("+" + signpoint + "积分");
        textViewExperience.setText("+" + signpoint + "经验");
        buttonLevel.setText(level);
        circleViewSign.setDegree(Double.parseDouble(signDay) * 18);
    }
}
