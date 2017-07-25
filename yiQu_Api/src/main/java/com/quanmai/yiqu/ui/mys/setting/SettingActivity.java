package com.quanmai.yiqu.ui.mys.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IWxCallback;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DataCleanManager;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.ui.fragment.AroundFragment;
import com.quanmai.yiqu.ui.groupbuy.RNCacheViewManager;
import com.quanmai.yiqu.ui.login.ChangePwdActivity;
import com.quanmai.yiqu.ui.login.LoginActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
    private TextView tv_app_version, tv_cache_size;
    private String cache_size;
    private Dialog dialog;
    TextView tv_red;

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    tv_red.setVisibility(View.VISIBLE);
                    break;
                case 102:
                    tv_red.setVisibility(View.GONE);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_setting);
        ((TextView) findViewById(R.id.tv_title)).setText("设置");
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getFeedBackMessage(this);
    }

    private void init() {
        tv_cache_size = (TextView) findViewById(R.id.tv_cache_size);
        tv_app_version = (TextView) findViewById(R.id.tv_app_version);
        tv_red = (TextView) findViewById(R.id.tv_red);
        tv_cache_size.setText(getCacheSize());
        tv_app_version.setText(getVersionName());

        findViewById(R.id.ll_personal_info_setting).setOnClickListener(this);
        findViewById(R.id.ll_modify_pwd).setOnClickListener(this);
        findViewById(R.id.ll_about).setOnClickListener(this);
        findViewById(R.id.ll_clear_cache).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
        findViewById(R.id.ll_feedBack).setOnClickListener(this);
        getCacheSize();
    }

    private void getFeedBackMessage(Context context) {
        FeedbackAPI.getFeedbackUnreadCount(context, "", new IWxCallback() {
            @Override
            public void onSuccess(Object... objects) {
                Message message = new Message();

                if (Integer.parseInt(String.valueOf(objects[0])) > 0) {
                    message.what = 101;
                    myHandler.sendMessage(message);
//                    tv_red.setVisibility(View.VISIBLE);
                } else {
                    message.what = 102;
                    myHandler.sendMessage(message);
//                    tv_red.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.e("mark", "onError");
            }

            @Override
            public void onProgress(int i) {
                Log.e("mark", "onProgress");
            }
        });
    }

    private String getCacheSize() {
        try {
            cache_size = DataCleanManager.getTotalCacheSize(mContext);
            return cache_size;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getVersionName() {
        if (Utils.getAppVersionName(mContext).trim().length() > 0) {
            return "v" + Utils.getAppVersionName(mContext).trim();
        }
        return "";
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_personal_info_setting:
                startActivity(PersonalInfoSettingActivity.class);
                break;

//            case R.id.linear_common_address:
//                startActivity(AddressActivity.class);
//                break;

            case R.id.ll_modify_pwd:
                Intent intent = new Intent(mContext, ChangePwdActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_about:
                startActivity(AboutActivity.class);
                break;

            case R.id.ll_clear_cache:
                DataCleanManager.clearAllCache(mContext);
                showCustomToast("清除缓存完成");
                tv_cache_size.setText(getCacheSize());
                break;

            case R.id.btn_logout:
                logout("退出登录后再次进入需要重新输入登录密码");
                break;

            case R.id.buttonConfirm:
                dialog.dismiss();
                mSession.Logout();
                startActivity(LoginActivity.class);
                setResult(1);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(AroundFragment.ACTION_NETWORKING_TO_REFRESH_DATA));
                RNCacheViewManager.onDestroy();
                RNCacheViewManager.clear();
                finish();
                break;

            case R.id.buttonCancel:
                dialog.dismiss();
                break;

            case R.id.ll_feedBack: {
                Map<String, String> map = new HashMap<String, String>();
                map.put("enableAudio", "1");
                map.put("themeColor", "#48c299");
                FeedbackAPI.setUICustomInfo(map);
                FeedbackAPI.openFeedbackActivity(SettingActivity.this);
                break;
            }
        }
    }

    private void logout(String title) {
        dialog = DialogUtil.getConfirmDialog(this, title, this);
        dialog.show();
    }
}
