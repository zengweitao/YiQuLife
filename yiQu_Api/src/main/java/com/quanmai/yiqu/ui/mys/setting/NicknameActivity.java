package com.quanmai.yiqu.ui.mys.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.StringUtil;

/**
 * 昵称修改
 */
public class NicknameActivity extends BaseActivity implements OnClickListener {
	private EditText et_nickname;
	private String nickname;
	private TextView tv_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_nickname);
		init();
	}

	private void init() {
		((TextView) findViewById(R.id.tv_title)).setText("个人资料设置");
		tv_right = (TextView) findViewById(R.id.tv_right);
		tv_right.setText("完成");
		tv_right.setOnClickListener(this);
		et_nickname = (EditText) findViewById(R.id.et_nickname);
		et_nickname.setText(UserInfo.get().alias);

		final int mMaxLength = 20;
		et_nickname.addTextChangedListener(new TextWatcher() {
			int intCount = 0;
			int selectionEnd = 0;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String strEdit = et_nickname.getText().toString().trim();
				String strFilter = StringUtil.stringFilter(strEdit);
				if (!strEdit.equals(strFilter)) {
					et_nickname.setText(strFilter);
				}

				et_nickname.setSelection(et_nickname.length());

				intCount = et_nickname.length();
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (intCount > mMaxLength) {
					selectionEnd = et_nickname.getSelectionEnd();
					s.delete(mMaxLength, selectionEnd);
					et_nickname.setText(s.toString());
				}
			}
		});


	}

	@Override
	protected void onPause() {
		super.onPause();
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(NicknameActivity.this.getCurrentFocus().getWindowToken(), 0);
//		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); //如果输入法在窗口上已经显示，则隐藏，反之则显示

	}

	@Override
	public void onClick(View v) {
		nickname = et_nickname.getText().toString().trim();
		if (nickname.equals("")) {
			showCustomToast("请输入您的昵称");
			return;
		} else {
			Intent data = new Intent(mContext, PersonalInfoSettingActivity.class);
			data.putExtra("nickname", nickname);
			setResult(1, data);
			finish();
		}
	}
}
