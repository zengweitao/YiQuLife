package com.quanmai.yiqu.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.common.WebActivity;

public class RegisterActivity extends BaseActivity implements OnClickListener {
    EditText editPhone, editVerifyCode, editPwd, editSurePwd;
    Button btnVerifyCode;
    CheckBox checkBoxAgree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_register);
        init();
    }


    private void init() {
        ((TextView) findViewById(R.id.tv_title)).setText("注册");

        editPhone = (EditText) findViewById(R.id.editTextPhone);
        editVerifyCode = (EditText) findViewById(R.id.editTextVerifyCode);
        editPwd = (EditText) findViewById(R.id.editTextPwd);
        editSurePwd = (EditText) findViewById(R.id.editTextSurePwd);
        checkBoxAgree = (CheckBox) findViewById(R.id.checkBoxAgree);
        btnVerifyCode = (Button) findViewById(R.id.buttonVerifyCode);

        btnVerifyCode.setOnClickListener(this);
        findViewById(R.id.buttonRegister).setOnClickListener(this);
        findViewById(R.id.tv_introduce).setOnClickListener(this);
    }


    private void getVerifyCode() {
        String phone = editPhone.getText().toString().trim();
        if (phone.equals("")) {
            showCustomToast("请输入手机号");
        } else {
            showLoadingDialog("请稍候");
            UserApi.get().RegisteredCode(mContext, phone, new ApiConfig.ApiRequestListener<String>() {

                @Override
                public void onSuccess(String msg, String data) {
                    dismissLoadingDialog();
                    showCustomToast(msg);
                    Utils.wait(btnVerifyCode);
                }

                @Override
                public void onFailure(String msg) {
                    dismissLoadingDialog();
                    showCustomToast(msg);
                }
            });
        }
    }


    private void register() {
        final String phone = editPhone.getText().toString().trim();
        String verifyCode = editVerifyCode.getText().toString().trim();
        String pwd = editPwd.getText().toString().trim();
        String sure_pwd = editSurePwd.getText().toString().trim();

        if (phone.equals("")) {
            showCustomToast("请输入手机号");
            return;
        }
        if (!Utils.isMobileNO(phone)) {
            showCustomToast("请输入正确的手机号");
            return;
        }
        if (verifyCode.equals("")) {
            showCustomToast("请输入验证码");
            return;
        }

        if (pwd.length() < 6 || pwd.length() > 20) {
            showCustomToast("请设置6-20位的密码");
            return;
        }
        if (sure_pwd.equals("")) {
            showCustomToast("请确认密码");
            return;
        }
        if (!sure_pwd.equals(pwd)) {
            showCustomToast("两次输入的密码不一致，请重新输入");
            return;
        }
        if (!checkBoxAgree.isChecked()) {
            showCustomToast("请确认用户协议");
            return;
        }
        showLoadingDialog("请稍候");
        Api.get().Registered(mContext, phone, verifyCode, pwd, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String token) {
                dismissLoadingDialog();
                showCustomToast(msg);
                mSession.Login(phone, token);
                setResult(1);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonVerifyCode: {
                getVerifyCode();
                break;
            }

            case R.id.buttonRegister: {
                register();
                break;
            }

            case R.id.tv_introduce:
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("http_url", ApiConfig.INTRODUCE_URL);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
