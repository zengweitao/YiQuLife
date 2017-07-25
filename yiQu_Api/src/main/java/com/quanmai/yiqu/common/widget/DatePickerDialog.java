package com.quanmai.yiqu.common.widget;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.common.view.wheelview.OnWheelChangedListener;
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

public class DatePickerDialog extends Dialog implements
		android.view.View.OnClickListener {
	private static int START_YEAR = 2015, END_YEAR = 2030;
	private WheelView wv_year, wv_month, wv_day;
	private Button mBtnButton1;// 底部按钮1
	String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	String[] months_little = { "4", "6", "9", "11" };
	final List<String> list_big, list_little;

	public static void getDialog(Context context, String title, int year,
			int month, int day, OnDateTimeSetListener callBack) {
		DatePickerDialog mBaseDialog;
		if (year == 0 || month == 0) {
			mBaseDialog = new DatePickerDialog(context, callBack);
		} else {
			mBaseDialog = new DatePickerDialog(context, year, month, day,
					callBack);
		}
		mBaseDialog.setTitle(title);
		mBaseDialog.setCancelable(true);
		mBaseDialog.setCanceledOnTouchOutside(true);
		mBaseDialog.show();
		// return mBaseDialog;
	}

//	public static void getDialog(Context context, String title,
//			OnDateTimeSetListener callBack) {
//		DatePickerDialog mBaseDialog = new DatePickerDialog(context, callBack);
//		mBaseDialog.setTitle(title);
//		mBaseDialog.setCancelable(true);
//		mBaseDialog.setCanceledOnTouchOutside(true);
//		mBaseDialog.show();
//		// return mBaseDialog;
//	}

	public DatePickerDialog(Context context, OnDateTimeSetListener callBack) {
		super(context, R.style.DataSheet);
		setContentView(R.layout.dialog_date);
		Calendar mCalendar = Calendar.getInstance();
		mCallBack = callBack;
		list_big = Arrays.asList(months_big);
		list_little = Arrays.asList(months_little);
		int year = mCalendar.get(Calendar.YEAR);
		int month = mCalendar.get(Calendar.MONTH);
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		Window w = getWindow();
		w.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		WindowManager.LayoutParams lp = w.getAttributes();
		w.setWindowAnimations(R.style.mDialogAnimation); // 设置窗口弹出动画
		lp.gravity = Gravity.BOTTOM;
		onWindowAttributesChanged(lp);
		init(year, month+1, day);
		initEvents();
	}

	public DatePickerDialog(Context context, int year, int month, int day,
			OnDateTimeSetListener callBack) {
		super(context, R.style.DataSheet);
		setContentView(R.layout.dialog_date);
		mCallBack = callBack;
		list_big = Arrays.asList(months_big);
		list_little = Arrays.asList(months_little);
		init(year, month - 1, day - 1);
		initEvents();
		setCancelable(true);
		setCanceledOnTouchOutside(false);
	}

	private void init(int year, int month, int day) {
		mBtnButton1 = (Button) findViewById(R.id.dialog_generic_btn_button1);
		// 年
		wv_year = (WheelView) findViewById(R.id.year);
		wv_year.setAdapter(new DatePickerAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
		// 月
		wv_month = (WheelView) findViewById(R.id.month);
		wv_month.setAdapter(new DatePickerAdapter(0, 12, "%02d"));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month);
		wv_day = (WheelView) findViewById(R.id.day);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new DatePickerAdapter(0, 31, "%02d"));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new DatePickerAdapter(0, 30, "%02d"));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new DatePickerAdapter(0, 29, "%02d"));
			else
				wv_day.setAdapter(new DatePickerAdapter(0, 28, "%02d"));
		}
		wv_day.setCyclic(true);
		wv_day.setLabel("日");
		wv_day.setCurrentItem(day);

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				wv_month.setCurrentItem(0);
				wv_day.setAdapter(new DatePickerAdapter(0, 0));
//				wv_day.setCurrentItem(0);
//				if (list_big
//						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
//					wv_day.setAdapter(new DatePickerAdapter(0, 31, "%02d"));
//				} else if (list_little.contains(String.valueOf(wv_month
//						.getCurrentItem() + 1))) {
//					wv_day.setAdapter(new DatePickerAdapter(0, 30, "%02d"));
//				} else {
//					if ((year_num % 4 == 0 && year_num % 100 != 0)
//							|| year_num % 400 == 0)
//						wv_day.setAdapter(new DatePickerAdapter(0, 29, "%02d"));
//					else
//						wv_day.setAdapter(new DatePickerAdapter(0, 28, "%02d"));
//				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue;
				// 判断大小月及是否闰年,用来确定"日"的数据
				wv_day.setCurrentItem(0);
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new DatePickerAdapter(0, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new DatePickerAdapter(0, 30));
				} else if(month_num==0){
					wv_day.setAdapter(new DatePickerAdapter(0, 0));
				}else{
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new DatePickerAdapter(0, 29));
					else
						wv_day.setAdapter(new DatePickerAdapter(0, 28));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
	}

	private void initEvents() {
		mBtnButton1.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_generic_btn_button1:
			if (mCallBack != null) {
				mCallBack.onDateTimeSet(
						wv_year.getCurrentItem()+ START_YEAR,
						wv_month.getCurrentItem(),
						 wv_day.getCurrentItem());
			}
			if (isShowing()) {
				dismiss();
			}
			break;
		}
	}

	@Override
	public void dismiss() {
		if (isShowing()) {
			super.dismiss();
		}
	}

	public interface OnDateTimeSetListener {
		void onDateTimeSet(int year, int monthOfYear, int day);
	}
	
	

	private final OnDateTimeSetListener mCallBack;

}
