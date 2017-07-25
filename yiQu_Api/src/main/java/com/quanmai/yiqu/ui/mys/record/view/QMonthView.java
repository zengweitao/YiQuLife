// Copyright 2012 Square, Inc.
package com.quanmai.yiqu.ui.mys.record.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.quanmai.yiqu.R;

//一个月
public class QMonthView extends LinearLayout {
	View lt_year;
	TextView tv_month, tv_year, tv_count;
	QCalendarGridView grid;

	private Listener listener;
	private OnClickListener onClickListener;
	private boolean isRtl;

	// 线的颜色
	static int dividerColor = 0xFFCDCDCD;
	//	static int dayBackgroundResId = R.drawable.calendar_bg_selector;
	static int dayTextColorResId = R.color.calendar_text_selector;
	int yearNum, monthNum, dayNum;


	// static Calendar today = Calendar.getInstance(Locale.getDefault());
	// static SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE",
	// Locale.getDefault());

	/**
	 * @param parent
	 * @param inflater
	 * @param listener
	 * @return
	 */
	public static QMonthView create(ViewGroup parent, LayoutInflater inflater,
									Listener listener) {
		final QMonthView view = (QMonthView) inflater.inflate(
				R.layout.view_month, parent, false);
		view.setDividerColor(dividerColor);
		view.setDayTextColor(dayTextColorResId);
		view.setDisplayHeader(true);
//		if (dayBackgroundResId != 0) {
//			view.setDayBackground(dayBackgroundResId);
//		}
		view.isRtl = isRtl(Locale.getDefault());
		view.listener = listener;
		return view;
	}

	public void init(QMonthDescriptor month,
					 List<List<QMonthCellDescriptor>> cells, boolean displayOnly,
					 Typeface titleTypeface, Typeface dateTypeface, int size) {
		long start = System.currentTimeMillis();
		setDividerColor(dividerColor);
//		if (dayBackgroundResId != 0) {
//			setDayBackground(dayBackgroundResId);
//		}
		isRtl = isRtl(Locale.getDefault());

		Calendar calendar = Calendar.getInstance();
		yearNum = calendar.get(Calendar.YEAR); // Calendar.YEAR是属性
		monthNum = calendar.get(Calendar.MONTH) + 1;
		dayNum = calendar.get(Calendar.DATE); //日
		tv_year.setText(yearNum + "." + monthNum + "." + dayNum);
		/*		tv_year.setText(month.getYear() + "年");
		tv_month.setText((month.getMonth() + 1) + "月");*/
		// tv_month.setText((month.getMonth() + 1) + "月工时合计：");
		// tv_count.setText(size + "工");
		final int numRows = cells.size();
		grid.setNumRows(numRows);
		for (int i = 0; i < 6; i++) {
			QCalendarRowView weekRow = (QCalendarRowView) grid.getChildAt(i + 1);
			weekRow.setListener(listener);
			if (i < numRows) {
				weekRow.setVisibility(VISIBLE);
				weekRow.setClickable(false);
				List<QMonthCellDescriptor> week = cells.get(i);
				for (int c = 0; c < week.size(); c++) {
					QMonthCellDescriptor cell = week.get(isRtl ? 6 - c : c);
					QCalendarCellView cellView = (QCalendarCellView) weekRow
							.getChildAt(c);

					String cellDate = Integer.toString(cell.getValue());
					if (!cellView.getText().equals(cellDate)) {
						cellView.setText(cellDate);
					}

					if (onClickListener != null) {
						cellView.setOnClickListener(onClickListener);
					}


					cellView.setSelectable(cell.isSelectable());
					cellView.setSelected(cell.isSelected());
//					cellView.setEnabled(cell.isCurrentMonth());
//					cellView.setClickable(!displayOnly);
					cellView.setEnabled(false);    //使之不可用
					cellView.setClickable(false);    //使之不响应点击事件

					cellView.setCurrentMonth(cell.isCurrentMonth());
					cellView.setToday(cell.isToday());
					cellView.setRangeState(cell.getRangeState());
					cellView.setHighlighted(cell.isHighlighted());
					cellView.setMark(cell.getStrMark());
					cellView.setTag(cell);
				}
			} else {
				weekRow.setVisibility(GONE);
			}
		}

		if (dateTypeface != null) {
			grid.setTypeface(dateTypeface);
		}

		QLogr.d("MonthView.init took %d ms", System.currentTimeMillis() - start);
	}

	public void setOnTitleClickListener(OnClickListener clickListener) {
		lt_year.setOnClickListener(clickListener);
	}

	public void hideYear() {
		lt_year.setVisibility(View.GONE);
	}

	public void showYear() {
		lt_year.setVisibility(View.VISIBLE);
	}

	private static boolean isRtl(Locale locale) {
		final int directionality = Character.getDirectionality(locale
				.getDisplayName(locale).charAt(0));
		return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT
				|| directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
	}

	public QMonthView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// public void setDecorators(List<CalendarCellDecorator> decorators) {
	// this.decorators = decorators;
	// }
	// public List<CalendarCellDecorator> getDecorators() {
	// return decorators;
	// }

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		tv_month = (TextView) findViewById(R.id.tv_month);
		tv_year = (TextView) findViewById(R.id.tv_year);
		tv_count = (TextView) findViewById(R.id.tv_count);
		lt_year = findViewById(R.id.lt_year);
		grid = (QCalendarGridView) findViewById(R.id.calendar_grid);

	}

	public OnClickListener getOnClickListener() {
		return onClickListener;
	}

	@Override
	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public void setDividerColor(int color) {
		grid.setDividerColor(color);
	}

	public void setDayBackground(int resId) {
		grid.setDayBackground(resId);
	}

	public void setDayTextColor(int resId) {
		grid.setDayTextColor(resId);
	}

	public void setDisplayHeader(boolean displayHeader) {
		grid.setDisplayHeader(displayHeader);
	}

	public interface Listener {
		void handleClick(QMonthCellDescriptor cell);
	}
}