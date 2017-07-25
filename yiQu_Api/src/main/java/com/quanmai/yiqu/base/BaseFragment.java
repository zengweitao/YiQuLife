package com.quanmai.yiqu.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quanmai.yiqu.common.FlippingLoadingDialog;
import com.quanmai.yiqu.common.Utils;

public abstract class BaseFragment extends Fragment {
    public Context mContext;
    //public Session mSession;

    protected FlippingLoadingDialog mLoadingDialog;
    protected View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = this.getActivity();
        //mSession = Session.get(mContext);
        mLoadingDialog = new FlippingLoadingDialog(mContext, "");

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//		init();

        return mView;
    }

    public View findViewById(int id) {
        return mView.findViewById(id);
    }

//    protected abstract void init();


    protected void showLoadingDialog() {
        ((BaseFragmentActivity) getActivity()).showLoadingDialog();
    }

    protected void showLoadingDialog(String text) {
        ((BaseFragmentActivity) getActivity()).showLoadingDialog(text);
    }

    protected void dismissLoadingDialog() {
        if (getActivity() == null) {
            return;
        }
        ((BaseFragmentActivity) getActivity()).dismissLoadingDialog();
    }

    protected void showCustomToast(String text) {
        Utils.showCustomToast(mContext, text);
    }

    protected void showCustomToast(int resId) {
        Utils.showCustomToast(mContext, resId);
    }

    /**
     * 跳转到指定的Activity
     *
     * @param cls 指定的Activity.class
     */
    protected void startActivity(Class<?> cls) {
        startActivity(new Intent(mContext, cls));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
