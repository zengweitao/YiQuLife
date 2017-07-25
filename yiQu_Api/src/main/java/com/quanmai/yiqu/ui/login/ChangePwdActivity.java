package com.quanmai.yiqu.ui.login;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.base.BaseActivity;

/**
 * 修改登录密码
 */
public class ChangePwdActivity extends BaseActivity implements OnClickListener {
	private EditText et_pwd, et_new_pwd, et_new_pwd_confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);
		init();
	}

	private void init() {
		((TextView) findViewById(R.id.tv_title)).setText("修改登录密码");
		((TextView) findViewById(R.id.textViewPhone)).setText(mSession.getUsername() + "");
		et_pwd = (EditText) findViewById(R.id.editTextOldPwd);
		et_new_pwd = (EditText) findViewById(R.id.editTextNewPwd);
		et_new_pwd_confirm = (EditText) findViewById(R.id.editTextNewPwdConfirm);

		findViewById(R.id.btn_finish).setOnClickListener(this);
		findViewById(R.id.iv_back).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_finish: {
				ChangePwd();
				break;
			}
			case R.id.iv_back: {
				finish();
				break;
			}
			default:
				break;
		}
	}


	private void ChangePwd() {
		String pwd = et_pwd.getText().toString().trim();
		String newPwd = et_new_pwd.getText().toString().trim();
		String newPwdConfirm = et_new_pwd_confirm.getText().toString().trim();
		if (pwd.equals("")) {
			showCustomToast("请输入当前密码");
			return;
		}
		if (newPwd.length() < 6 || newPwd.length() > 20) {
			showCustomToast("请设置6-20位的密码");
			return;
		}
		if (newPwdConfirm.equals("")) {
			showCustomToast("请输入新密码");
			return;
		}
		if (!newPwd.equals(newPwdConfirm)) {
			showCustomToast("两次输入的密码不一致，请重新输入");
		}
		showLoadingDialog("请稍候");
		Api.get().ChangePwd(mContext, pwd, newPwd, new ApiRequestListener<String>() {
			@Override
			public void onSuccess(String msg, String token) {
				dismissLoadingDialog();
				showCustomToast(msg);
				mSession.Login(mSession.getUsername(), token);
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