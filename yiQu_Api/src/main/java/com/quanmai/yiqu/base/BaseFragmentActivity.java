package com.quanmai.yiqu.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.common.LoadingDialog;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.widget.SystemBarTintManager;

import cn.jpush.android.api.JPushInterface;

public class BaseFragmentActivity extends FragmentActivity {
    public Context mContext;
    public Session mSession;
    //	protected FlippingLoadingDialog mLoadingDialog;
//	protected ProgressDialog dialog;
    private LoadingDialog mLoadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLoadingDialog = new LoadingDialog(this, "请稍候");
        mContext = this;
        mSession = Session.get(this);

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

    @Override
    public void onResume() {
        super.onResume();
//		Bugtags.onResume(this);
        JPushInterface.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        Bugtags.onPause(this);
        JPushInterface.onPause(this);
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
        if (mLoadingDialog == null)
            return;

        if (mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        dismissLoadingDialog();

        super.onDestroy();
//		Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//		Bugtags.onDispatchTouchEvent(this, ev);
        return super.dispatchTouchEvent(ev);
    }

    public void finish(View view) {
        finish();
    }

    public void showCustomToast(String text) {
        Utils.showCustomToast(mContext, text);
    }

    public void showCustomToast(int resId) {
        Utils.showCustomToast(mContext, resId);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param cls 指定的Activity.class
     */
    public void startActivity(Class<?> cls) {
        startActivity(new Intent(mContext, cls));
    }

//	private long lastExitTime;
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (System.currentTimeMillis() - lastExitTime > 1500) {
//				lastExitTime = System.currentTimeMillis();
//				Toast.makeText(this, "再按一次，退出应用", Toast.LENGTH_SHORT).show();
//			} else {
//				finish();
//			}
//			return true;
//		}
//
//		return super.onKeyDown(keyCode, event);
//	}
}
