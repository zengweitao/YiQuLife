package com.quanmai.yiqu.ui.comment;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.MessageApi;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 评论
 */
public class MessageActivity extends BaseActivity implements OnClickListener {
	private EditText et_content;
	private TextView tv_alias;
	TextView tv_right;

	private String comment = new String();
	private String user_id = new String();
	private String user_alias = new String();
	LinearLayout content;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		init();
	}

	private void init() {
		if (getIntent().hasExtra("user_id")) {
			user_id = getIntent().getStringExtra("user_id");
		}

		tv_alias = (TextView) findViewById(R.id.tv_alias);
		et_content = (EditText) findViewById(R.id.et_content);
		if (getIntent().hasExtra("alias")) {
			user_alias = getIntent().getStringExtra("alias");
			((TextView) findViewById(R.id.tv_title)).setText("回复");
			tv_alias.setVisibility(View.VISIBLE);
			tv_alias.setText("回复@" + user_alias + ":");
		} else {
			((TextView) findViewById(R.id.tv_title)).setText("私信");
		}
		content = (LinearLayout)findViewById(R.id.content);
		findViewById(R.id.btn_sure).setOnClickListener(this);
		tv_right = (TextView) findViewById(R.id.tv_right);
		tv_right.setText("确认");
		tv_right.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_right:
				sure();
				break;
		}
	}


	private void sure() {

		if (user_id.equals("")) {
			return;
		}
		String content = et_content.getText().toString().trim();
		if (content.equals("")) {
			showCustomToast("请输入内容文字");
			return;
		}


//		if (!comment.equals("")) {
//			content = content + "//" + comment;
//		}
		showLoadingDialog("请稍候");
		MessageApi.get().Message(mContext, user_id, Utils.toURLEncoded(content),
				new ApiRequestListener<String>() {

					@Override
					public void onSuccess(String msg, String data) {
						dismissLoadingDialog();
						showCustomToast(msg);
						setResult(RESULT_OK);

						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
						if (isOpen){
							imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
						}

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
	protected void onDestroy() {

		super.onDestroy();
	}
}

