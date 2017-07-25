package com.quanmai.yiqu.share;

import com.quanmai.yiqu.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class ShareDialog extends Dialog implements OnClickListener {
	public onShareListener mShareListener;

	public ShareDialog(Context context, onShareListener listener) {
		super(context, R.style.Theme_UMDialog);
		
		mShareListener = listener;
		setContentView(R.layout.dialog_share);
		setCanceledOnTouchOutside(true);
		
		findViewById(R.id.tv_cancel).setOnClickListener(this);
		findViewById(R.id.iv_wxf).setOnClickListener(this);
		findViewById(R.id.iv_wx).setOnClickListener(this);
//		findViewById(R.id.iv_qq).setOnClickListener(this);
		findViewById(R.id.iv_qzone).setOnClickListener(this);
		findViewById(R.id.iv_wb).setOnClickListener(this);
		
		Window w = getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		w.setWindowAnimations(R.style.mDialogAnimation); // 设置窗口弹出动画
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		lp.gravity = Gravity.BOTTOM;
		onWindowAttributesChanged(lp);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancel:
			dismiss();
			break;
		case R.id.iv_wxf:
			mShareListener.toWXF();
			dismiss();
			break;
		case R.id.iv_wx:
			mShareListener.toWX();
			dismiss();
			break;
		case R.id.iv_qq:
			mShareListener.toQQ();
			dismiss();
			break;
		case R.id.iv_qzone:
			mShareListener.toQZONE();
			dismiss();
			break;
		case R.id.iv_wb:
			mShareListener.toWB();
			dismiss();
			break;
		}
	}

	public interface onShareListener {
		/** 分享到微信朋友圈 */
		void toWXF();

		/** 分享给微信好友 */
		void toWX();

		/** 分享给QQ好友 */
		void toQQ();

		/** 分享到QQ空间 */
		void toQZONE();

		/** 分享到新浪微博 */
		void toWB();
	}
}
