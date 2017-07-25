package com.quanmai.yiqu.common.widget;

import com.quanmai.yiqu.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CountDownView extends LinearLayout {
	protected LayoutInflater mInflater;
	protected Context mContext;
	TextView hourTv;
	TextView minTv;
	TextView SecTv;
	View layout;
	TextView titleTv;
	private long distanceTime = 0;
	private boolean RuningState = false;

	public CountDownView(Context context) {
		super(context);
		init(context);
	}

	public CountDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		View mHeaderView = mInflater.inflate(R.layout.view_countdown, this,
				false);
		titleTv = (TextView) mHeaderView.findViewById(R.id.text);
		hourTv = (TextView) mHeaderView.findViewById(R.id.hour);
		minTv = (TextView) mHeaderView.findViewById(R.id.min);
		SecTv = (TextView) mHeaderView.findViewById(R.id.sec);
		layout = mHeaderView.findViewById(R.id.lt);
		addView(mHeaderView);
	}

	public void setTitle(String String) {
		titleTv.setText(String);
	}

	public void setTitleColor(String String) {
		titleTv.setTextColor(Color.parseColor(String));
	}

	public void setTime(long Time) {
		if (Time >= 0) {
			setText(Time);
			layout.setVisibility(View.VISIBLE);
			this.distanceTime = Time;
			StartCountDown();
		} else {
			layout.setVisibility(View.GONE);
			StopCountDown();
		}

	}

	private class MyThread implements Runnable {
		@Override
		public void run() {
			while (RuningState) {
				try {
					Thread.sleep(1000); // sleep 1000ms
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				} catch (Exception e) {
				}
			}
		}
	}

	private void StopCountDown() {
		RuningState = false;
		layout.setVisibility(View.GONE);
	}

	private void StartCountDown() {
		// 如果当前没有线程在倒数,则开启一个倒数线程
		if (!RuningState) {
			RuningState = true;
			new Thread(new MyThread()).start();
		}

	}

	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() { // handle
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				distanceTime--;
				if (distanceTime <= 0) {
					titleTv.setText("倒计时已结束");
					StopCountDown();
				} else {
					setText(distanceTime);
				}
			}
			super.handleMessage(msg);
		}
	};

	private void setText(long time) {
		if (time > 0) {
			long hours = (time % (24 * 60 * 60)) / (60 * 60);
			long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
			long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;

			hourTv.setText(timeStrFormat(String.valueOf(hours)));
			minTv.setText(timeStrFormat(String.valueOf(minutes)));
			SecTv.setText(timeStrFormat(String.valueOf(second)));

		} else {
			hourTv.setText("00");
			minTv.setText("00");
			SecTv.setText("00");
			StopCountDown();
		}

	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		RuningState = false;
	}

	/**
	 * format time
	 * 
	 * @param timeStr
	 * @return
	 */
	private static String timeStrFormat(String timeStr) {
		switch (timeStr.length()) {
		case 1:
			timeStr = "0" + timeStr;
			break;
		}
		return timeStr;
	}

}
