package com.quanmai.yiqu.ui.comment;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.CommentApi;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 评论
 */
public class CommentActivity extends BaseActivity implements OnClickListener {
	private EditText et_content;
	private TextView tv_comment;
	private String comment = new String();
	private String goods_id = new String();
	private String user_alias = new String();
	private String customerid = "";//发布人id
	private String goods_name = "";//商品名
	TextView tv_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		init();
	}

	private void init() {
		tv_right = (TextView) findViewById(R.id.tv_right);
		tv_right.setText("发送");
		tv_right.setOnClickListener(this);
		if (getIntent().hasExtra("goods_id")) {
			goods_id = getIntent().getStringExtra("goods_id");
		}

		if (getIntent().hasExtra("customerid")) {
			customerid = getIntent().getStringExtra("customerid");
		}

		if (getIntent().hasExtra("goods_name")) {
			goods_name = getIntent().getStringExtra("goods_name");
		}

		tv_comment = (TextView) findViewById(R.id.tv_alias);
		et_content = (EditText) findViewById(R.id.et_content);
		if (getIntent().hasExtra("alias")) {
			user_alias = getIntent().getStringExtra("alias");
			((TextView) findViewById(R.id.tv_title)).setText("回复");
			tv_comment.setVisibility(View.VISIBLE);
			tv_comment.setText("回复@" + user_alias + ":");
		} else {
			((TextView) findViewById(R.id.tv_title)).setText("评论");

		}
		findViewById(R.id.btn_sure).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_right: {
				sure();
				break;
			}
			default:
				break;
		}

	}

	private void sure() {
		String content = "";
		if (goods_id.equals("")) {
			return;
		}
		if (!TextUtils.isEmpty(user_alias)){
			content = "回复@" + user_alias + ":"+et_content.getText().toString().trim();
		}else {
			content = et_content.getText().toString().trim();
		}

		if (content.equals("")) {
			showCustomToast("请输入内容文字");
			return;
		}

//		if (!comment.equals("")) {
//			content = comment + content;
//		}
		showLoadingDialog("请稍候");
		CommentApi.get().GoodsComment(mContext, goods_id, Utils.toURLEncoded(content), customerid, goods_name,
				new ApiRequestListener<String>() {

					@Override
					public void onSuccess(String msg, String data) {
						dismissLoadingDialog();
						showCustomToast(msg);
						setResult(RESULT_OK);
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
						if (isOpen) {
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


}
