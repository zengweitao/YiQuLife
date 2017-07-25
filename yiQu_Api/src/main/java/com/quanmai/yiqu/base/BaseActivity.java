package com.quanmai.yiqu.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.common.LoadingDialog;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.widget.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

public class BaseActivity extends Activity {
    public Context mContext;
    public Session mSession;
    //	protected ProgressDialog dialog;
//    protected FlippingLoadingDialog mLoadingDialog;
    private LoadingDialog mLoadingDialog;
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mLoadingDialog = new LoadingDialog(this, "请稍候");
        mLoadingDialog.setCanceledOnTouchOutside(true);
        mContext = this;
        mSession = Session.get(mContext);
        mIntent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager manager = new SystemBarTintManager(this);
            manager.setStatusBarTintEnabled(true);
            manager.setStatusBarTintColor(getResources().getColor(R.color.theme));
        }
        super.onCreate(savedInstanceState);
    }


    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams wlp = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            wlp.flags |= bits;
        } else {
            wlp.flags &= ~bits;
        }
        win.setAttributes(wlp);
    }

    protected void showShortToast(String text) {
        Utils.showCustomToast(this, text);
    }

    public void showLoadingDialog() {
        if (mLoadingDialog == null) return;

        if (mLoadingDialog.isShowing()) return;

        mLoadingDialog.show();
    }

    public void showLoadingDialog(String text) {
        if (mLoadingDialog == null) return;

        if (mLoadingDialog.isShowing()) return;

        if (text != null) mLoadingDialog.setLoadingText(text);

        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void showCustomToast(String text) {
        Utils.showCustomToast(mContext, text);
    }

    protected void showCustomToast(int resId) {
        Utils.showCustomToast(mContext, resId);
    }

    public void finish(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }
        finish();
    }

    // public void goShoppingCar() {
    // mIntent.setClass(mContext, ShoppingCarActivity.class);
    //
    // if (mSession.isLogin()) {
    // if (getIntent().getBooleanExtra("fromCar", false)) {
    // finish();
    // } else {
    // startActivity(mIntent);
    // }
    // } else {
    // startActivityForResult(new Intent(mContext, LoginActivity.class),
    // ActivityResult.SKIP);
    // }
    // }

    // public void goAddressManagement() {
    // mIntent.setClass(mContext, AddressActivity.class);
    // if (mSession.isLogin()) {
    // startActivity(mIntent);
    // } else {
    // startActivityForResult(new Intent(mContext, LoginActivity.class),
    // ActivityResult.SKIP);
    // }
    // }

    // public void goOrderManagement() {
    // mIntent.setClass(mContext, OrderActivity.class);
    // if (mSession.isLogin()) {
    //
    // startActivity(mIntent);
    // } else {
    // startActivityForResult(new Intent(mContext, LoginActivity.class),
    // ActivityResult.SKIP);
    // }
    // }

    // public void goHotActivity() {
    // mIntent.setClass(mContext, WebActivity.class);
    // mIntent.putExtra("http_url", Qurl.hothuodong);
    // if (mSession.isLogin()) {
    // startActivity(mIntent);
    // } else {
    // startActivityForResult(new Intent(mContext, LoginActivity.class),
    // ActivityResult.SKIP);
    // }
    //
    // }

    // public void goCoupons() {
    // mIntent.setClass(mContext, CouponsActivity.class);
    // mIntent.putExtra("from", 1);
    // if (mSession.isLogin()) {
    // startActivity(mIntent);
    // } else {
    // startActivityForResult(new Intent(mContext, LoginActivity.class),
    // ActivityResult.SKIP);
    // }
    //
    // }

    // public void goSettingActivity() {
    // startActivity(new Intent(mContext, SettingActivity.class));
    // }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ActivityResult.SKIP && resultCode == 1) {

            startActivity(mIntent);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void onResume() {
        super.onResume();
//		try{
//			MobclickAgent.onResume(this);
//		}catch (ExceptionInInitializerError e){
//			e.printStackTrace();
//		}

//        Bugtags.onResume(this);
        JPushInterface.onResume(this);

        MobclickAgent.onPageStart((String) getTitle());//统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }

    protected void onPause() {
        super.onPause();
//		try{
//			MobclickAgent.onResume(this);
//		}catch (ExceptionInInitializerError e){
//			e.printStackTrace();
//		}
//        Bugtags.onPause(this);
        JPushInterface.onPause(this);

        MobclickAgent.onPageEnd((String) getTitle()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Bugtags.onDispatchTouchEvent(this, ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 把单个或多个view设置成visible
     *
     * @param view
     */
    protected void setVisible(View... view) {
        for (int i = 0; i < view.length; i++) {
            view[i].setVisibility(View.VISIBLE);
        }
    }

    /**
     * 把单个或多个view设置成invisible
     *
     * @param view
     */
    protected void setInvisible(View... view) {
        for (int i = 0; i < view.length; i++) {
            view[i].setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 把单个或多个view设置成gone
     *
     * @param view
     */
    protected void setGone(View... view) {
        for (int i = 0; i < view.length; i++) {
            view[i].setVisibility(View.GONE);
        }
    }

    /**
     * 跳转到指定的Activity
     *
     * @param cls 指定的Activity.class
     */
    protected void startActivity(Class<?> cls) {
        startActivity(new Intent(mContext, cls));
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(new Intent(mContext, cls), requestCode);
    }
}
