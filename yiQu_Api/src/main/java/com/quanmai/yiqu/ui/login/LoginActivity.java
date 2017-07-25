package com.quanmai.yiqu.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.UserInfoApi;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.HomeActivity;
import com.quanmai.yiqu.ui.fragment.AroundFragment;
import com.quanmai.yiqu.ui.fragment.PersonalFragment;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private EditText editTextAccount, editTextPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        ((TextView) findViewById(R.id.tv_title)).setText("登录");

        editTextAccount = (EditText) findViewById(R.id.editTextAccount);
        editTextPsw = (EditText) findViewById(R.id.editTextPsw);

        findViewById(R.id.textViewRegister).setOnClickListener(this);
        findViewById(R.id.textViewForgetPsw).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewRegister: {
                startActivityForResult(RegisterActivity.class, 1);
                break;
            }

            case R.id.textViewForgetPsw: {
                Intent intent = new Intent(mContext, ForgetPwdActivity.class);
                startActivityForResult(intent, 1);
                break;
            }

            case R.id.buttonLogin: {
                login();
                break;
            }

            case R.id.iv_back: {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("currentItem", 0);
                startActivity(intent);
                finish();
                break;
            }
            default:
                break;
        }
    }

    private void login() {
        final String phone = editTextAccount.getText().toString().trim();
        String pwd = editTextPsw.getText().toString().trim();
        if (phone.equals("")) {
            showCustomToast("请输入手机号");
            return;
        }
        if (!Utils.isMobileNO(phone)) {
            showCustomToast("请输入正确的手机号");
            return;
        }
        if (pwd.equals("")) {
            showCustomToast("请输入密码");
            return;
        }

        showLoadingDialog();
        UserApi.get().Login(mContext, phone, pwd, new ApiRequestListener<String>() {

            @Override
            public void onSuccess(String msg, String token) {
                dismissLoadingDialog();
                showCustomToast(msg);
               // UserInfo.get().setPhone(phone);
                mSession.Login(phone, token);
                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.putExtra("loginSuccess", true);
                startActivity(intent);
                getUserInfo();
                finish();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("currentItem", 0);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 & resultCode == 1) {
            startActivity(HomeActivity.class);
            finish();
        }
    }

    private void getUserInfo() {
        UserInfoApi.get().getUserHome(mContext, new ApiRequestListener<UserInfo>() {
            @Override
            public void onSuccess(String msg, UserInfo data) {
                mSession.setBind(!("0".equals(UserInfo.get().isbind)));
                mSession.setBookingCommunity(UserInfo.get().community);

                //增加推送标签，标签数量可多个
                Set<String> set = new HashSet();
                for (int i = 0;i<UserInfo.get().tag.size();i++){
                    UserInfo.Tag childTag = UserInfo.get().tag.get(i);
                    set.add(childTag.tagvalue);
                }
                //过滤掉无效的 tags
                set = JPushInterface.filterValidTags(set);
                //调用此 API 来设置别名和标签
                JPushInterface.setAliasAndTags(LoginActivity.this, UserInfo.get().userid,set, new TagAliasCallback() {
                    @Override
                    public void gotResult(int responseCode, String alias, Set<String> tags) {
                        Utils.E("JPush set setAliasAndTags--->" + ((responseCode == 0) ? "success" : "false"));
                    }
                });


                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(AroundFragment.ACTION_NETWORKING_TO_REFRESH_DATA));
                LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(new Intent(PersonalFragment.ACTION_REFRESH_PAGE));
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }
}
