package com.quanmai.yiqu.common.widget;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.common.view.wheelview.NumericWheelAdapter;
import com.common.view.wheelview.WheelView;
import com.quanmai.yiqu.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
public class MonthSelectionDialog extends Dialog implements
		android.view.View.OnClickListener {
	private static int START_YEAR = 1900, END_YEAR = 2100;
	private WheelView wv_year, wv_month;
	private Button mBtnButton1;// 底部按钮1
	String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	String[] months_little = { "4", "6", "9", "11" };
	final List<String> list_big, list_little;


	public MonthSelectionDialog(Context context, OnDateSetListener callBack) {
		super(context, R.style.DataSheet);
		setContentView(R.layout.dialog_month);
		Calendar mCalendar = Calendar.getInstance();
		mCallBack = callBack;
		list_big = Arrays.asList(months_big);
		list_little = Arrays.asList(months_little);
		int year = mCalendar.get(Calendar.YEAR);
		int month = mCalendar.get(Calendar.MONTH);
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		init(year, month, day);
		initEvents();
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		Window w = getWindow();
		w.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		WindowManager.LayoutParams lp = w.getAttributes();
		w.setWindowAnimations(R.style.mDialogAnimation); // 设置窗口弹出动画
		lp.gravity = Gravity.BOTTOM;
		onWindowAttributesChanged(lp);
	}

//	public DateSelectionDialog(Context context, int year, int month, int day,
//			OnDateTimeSetListener callBack) {
//		super(context, R.style.DataSheet);
//		setContentView(R.layout.common_date_dialog);
//		mCallBack = callBack;
//		list_big = Arrays.asList(months_big);
//		list_little = Arrays.asList(months_little);
//		init(year, month - 1, day - 1);
//		initEvents();
//		setCancelable(true);
//		setCanceledOnTouchOutside(false);
//	}

	private void init(int year, int month, int day) {
		mBtnButton1 = (Button) findViewById(R.id.dialog_generic_btn_button1);
		// 年
		wv_year = (WheelView) findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
		// 月
		wv_month = (WheelView) findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month);
	

	}

	private void initEvents() {
		mBtnButton1.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_generic_btn_button1:
			if (mCallBack != null) {
				mCallBack.onDateTimeSet(wv_year.getCurrentItem() + START_YEAR,
						wv_month.getCurrentItem() + 1);
			}
			if (isShowing()) {
				dismiss();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void dismiss() {
		if (isShowing()) {
			super.dismiss();
		}
	}

	public interface OnDateSetListener {
		void onDateTimeSet(int year, int monthOfYear);
	}

	private final OnDateSetListener mCallBack;

}
