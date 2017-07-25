package com.quanmai.yiqu.ui.mys.record;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.common.view.wheelview.NumericWheelAdapter;
import com.common.view.wheelview.OnWheelChangedListener;
import com.common.view.wheelview.WheelView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseActivity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 积分查询
 * 
 * @author
 * 
 */
public class IntegrateQueryActivity extends BaseActivity implements
		OnClickListener {
	private PopupWindow popSelectDate;

	private TextView tvStartDate, tvEndDate;

	private final static int START_YEAR = 1900, END_YEAR = 2100;
	private WheelView wv_year, wv_month, wv_day;
	String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	String[] months_little = { "4", "6", "9", "11" };
	List<String> list_big, list_little;
	private View contentView;

	private boolean isStartDate;
	private int startYear, startMouth, startDay, endYear, endMouth, endDay;

	String starttime = new String();
	String endtime = new String();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integtate_query);

		init();
		initSelectDatePop();
		Calendar mCalendar = Calendar.getInstance();
		startYear = START_YEAR + wv_year.getCurrentItem();
		startMouth = wv_month.getCurrentItem() + 1;
		startDay = wv_day.getCurrentItem() + 1;
		endYear = startYear;
		endMouth = startMouth;
		endDay = startDay;
		String year=String.valueOf(startYear);
		String month=String.format("%02d",startMouth);
		String day=String.format("%02d",startDay);
		tvStartDate.setText(year
				+ "年" + month + "月"
				+ day + "日");
		tvEndDate.setText(year
				+ "年" + month + "月"
				+ day + "日");
		starttime=year+"-"+month+"-"+day;
		endtime=starttime;
	}
	
	private void init() {
		((TextView) findViewById(R.id.tv_title)).setText("得分查询");
		tvEndDate = (TextView) findViewById(R.id.tv_end_date);
		tvStartDate = (TextView) findViewById(R.id.tv_start_date);
		tvEndDate.setOnClickListener(this);
		tvStartDate.setOnClickListener(this);
		findViewById(R.id.btn_quary).setOnClickListener(this);
		
		
	
	}
	


	/**
	 * 初始化日期选择
	 */
	private void initSelectDatePop() {
		contentView = View.inflate(mContext,
				R.layout.common_date_popw, null);
		popSelectDate = new PopupWindow(contentView,
				tvEndDate.getLayoutParams().width, LayoutParams.WRAP_CONTENT,
				true);
		popSelectDate.setTouchable(true);
		popSelectDate.setBackgroundDrawable(new ColorDrawable());
		popSelectDate.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (isStartDate) {
					startYear = START_YEAR + wv_year.getCurrentItem();
					startMouth = wv_month.getCurrentItem() + 1;
					startDay = wv_day.getCurrentItem() + 1;
					String year=String.valueOf(startYear);
					String month=String.format("%02d",startMouth);
					String day=String.format("%02d",startDay);
					
					tvStartDate.setText(year
							+ "年" + month + "月"
							+ day + "日");
				
					starttime=year+"-"+month+"-"+day;
				} else {
					endYear = START_YEAR + wv_year.getCurrentItem();
					endMouth = wv_month.getCurrentItem() + 1;
					endDay = wv_day.getCurrentItem() + 1;
					String year=String.valueOf(endYear);
					String month=String.format("%02d",endMouth);
					String day=String.format("%02d",endDay);
					
					tvEndDate.setText(year
							+ "年" + month + "月"
							+ day + "日");
					endtime=year+"-"+month+"-"+day;
				}
			}
		});

		Calendar mCalendar = Calendar.getInstance();
		int year = mCalendar.get(Calendar.YEAR);
		int month = mCalendar.get(Calendar.MONTH);
		int day = mCalendar.get(Calendar.DAY_OF_MONTH);
		list_big = Arrays.asList(months_big);
		list_little = Arrays.asList(months_little);
		initDate(year, month, day - 1);
	}

	private void initDate(int year, int month, int day) {
		// 年
		wv_year = (WheelView) contentView.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
		// 月
		wv_month = (WheelView) contentView.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month);
		wv_day = (WheelView) contentView.findViewById(R.id.day);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
		}
		wv_day.setCyclic(true);
		wv_day.setLabel("日");
		wv_day.setCurrentItem(day);

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_start_date:
			isStartDate = true;
			popSelectDate.showAsDropDown(v);
			break;

		case R.id.tv_end_date:
			isStartDate = false;
			popSelectDate.showAsDropDown(v);
			break;

		case R.id.btn_quary:
			quaryIntegrateDetail();
			break;
		default:
			break;
		}
	}

	private void quaryIntegrateDetail() {
		if(endtime.equals("")||starttime.equals(""))
		{
			showCustomToast("请选择正确的起始日期和结束日期");
			return;
		}
		if ((endYear > startYear)
				|| (endYear == startYear && endMouth > startMouth)
				|| (endYear == startYear && endMouth == startMouth && endDay >= startDay)) {
			Intent intent = new Intent(mContext, IntegrateDetailActivity.class);
			intent.putExtra("starttime", starttime);
			intent.putExtra("endtime", endtime);
			startActivity(intent);
		} else {
			showCustomToast("请选择正确的起始日期和结束日期");
		}
	}
}
