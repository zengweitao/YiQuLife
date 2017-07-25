package com.quanmai.yiqu.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.quanmai.yiqu.R;

/**
 * Created by zhanjinj on 16/4/11.
 * 等待弹窗
 */
public class LoadingDialog extends Dialog {

	TextView tv_loading;
	CircularProgressView progress_view;

	String strLoading;

	public LoadingDialog(Context context, String strLoading) {
		super(context, R.style.MyDialogStyle);
		this.strLoading = strLoading;
		setContentView(R.layout.dialog_loading);

		init();
	}

	public LoadingDialog(Context context, int theme, String strLoading) {
		super(context, theme);
		this.strLoading = strLoading;

		setContentView(R.layout.dialog_loading);

		init();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void init() {
		progress_view = (CircularProgressView) findViewById(R.id.progress_view);
		tv_loading = (TextView) findViewById(R.id.tv_loading);
		setLoadingText(strLoading);

		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}

	public void setLoadingText(String strLoading) {
		if (strLoading != null && strLoading.trim().length() > 0) {
			tv_loading.setText(strLoading);
		}
	}

	@Override
	public void dismiss() {
		if (isShowing()) {
			super.dismiss();
		}
	}
}
