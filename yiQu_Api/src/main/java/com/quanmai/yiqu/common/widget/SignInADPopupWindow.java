package com.quanmai.yiqu.common.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.common.util.ImageloaderUtil;

/**
 * Created by zhanjj on 2016/8/10.
 * 签到成功弹窗(广告)
 */
public class SignInADPopupWindow extends PopupWindow {
    Context mContext;
    View mMenuView;
    static CircularImageView imgAdvertisement;
    Button buttonConfirm;

    private int surplusTime ; //倒计时时间
    Runnable mRunnable;
    Handler mHandler = new Handler();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public SignInADPopupWindow(Activity context) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_window_ad_sign_success, null);

        initView();
        initPopWindow();
    }

    private void initView() {
        imgAdvertisement = (CircularImageView) mMenuView.findViewById(R.id.imgAdvertisement);
        buttonConfirm = (Button) mMenuView.findViewById(R.id.buttonConfirm);
        buttonConfirm.setText("2s");
        buttonConfirm.setEnabled(false);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        countDown(buttonConfirm,2);
    }

    //倒计时
    private void countDown(final Button button,int second){
        surplusTime = second;
         mRunnable= new Runnable() {
            @Override
            public void run() {
                if (surplusTime--==1){
                    button.setText("确定");
                    button.setEnabled(true);
                    button.setBackgroundResource(R.drawable.sign_btn_bg);
                }else {
                    button.setBackgroundColor(mContext.getResources().getColor(R.color.text_color_cdcdcd));
                    button.setEnabled(false);
                    button.setText(surplusTime+"s");
                    mHandler.postDelayed(mRunnable,1000);
                }
            }
        };
        mHandler.postDelayed(mRunnable,1000);
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

    public static PopupWindow showOnCenter(Activity context, final View parent, String uri, final View.OnClickListener onClickListener) {
        final SignInADPopupWindow popupWindow = new SignInADPopupWindow(context);
        ImageloaderUtil.displayImage(context, uri, imgAdvertisement, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                imgAdvertisement.setOnClickListener(onClickListener);
                popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        return popupWindow;
    }
}
