package com.quanmai.yiqu.ui.login;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;

/**
 * 重置密码
 */
public class ForgetPwdActivity extends BaseActivity implements OnClickListener {
	EditText editTextPhone, editTextVerifyCode, editTextPwd, editTextSurePwd;
	Button buttonVerifyCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		init();
	}

	private void init() {
		((TextView) findViewById(R.id.tv_title)).setText("重置密码");
		editTextPhone = (EditText) findViewById(R.id.editTextPhone);
		editTextVerifyCode = (EditText) findViewById(R.id.editTextVerifyCode);
		editTextPwd = (EditText) findViewById(R.id.editTextPwd);
		editTextSurePwd = (EditText) findViewById(R.id.editTextSurePwd);

		buttonVerifyCode = (Button) findViewById(R.id.buttonVerifyCode);

		buttonVerifyCode.setOnClickListener(this);
		findViewById(R.id.buttonResetPwd).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.buttonVerifyCode:
				getVerifyCode();
				break;

			case R.id.buttonResetPwd:
				resetPwd();
				break;
		}
	}

	private void getVerifyCode() {
		// TODO Auto-generated method stub
		String phone = editTextPhone.getText().toString().trim();
		if (phone.equals("")) {
			showCustomToast("请输入手机号");
		} else {
			if (!Utils.isMobileNO(phone)) {
				showCustomToast("请输入正确的手机号");
				return;
			}
			showLoadingDialog("请稍候");
			Api.get().FindPwdCode(mContext, phone,
					new ApiRequestListener<String>() {

						@Override
						public void onSuccess(String msg, String data) {
							dismissLoadingDialog();
							showCustomToast(msg);
							Utils.wait(buttonVerifyCode);
						}

						@Override
						public void onFailure(String msg) {
							dismissLoadingDialog();
							showCustomToast(msg);
						}
					});
		}
	}

	private void resetPwd() {
		final String phone = editTextPhone.getText().toString().trim();
		String code = editTextVerifyCode.getText().toString().trim();
		String pwd = editTextPwd.getText().toString().trim();
		String surePwd = editTextSurePwd.getText().toString().trim();


		if (phone.equals("")) {
			showCustomToast("请输入手机号");
			return;
		}
		if (!Utils.isMobileNO(phone)) {
			showCustomToast("请输入正确的手机号");
			return;
		}
		if (code.equals("")) {
			showCustomToast("请输入验证码");
			return;
		}
		if (pwd.length() < 6 || pwd.length() > 16) {
			showCustomToast("请设置6-16位的密码");
			return;
		}
		if (!pwd.equals(surePwd)) {
			showCustomToast("两次输入的密码不一致，请重新输入");
			return;
		}

		showLoadingDialog("请稍候");
		UserApi.get().FindPwd(mContext, phone, code, pwd,
				new ApiRequestListener<String>() {
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

}