// Copyright 2013 Square, Inc.

package com.quanmai.yiqu.ui.mys.record.view;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.ui.mys.record.view.QMonthCellDescriptor.RangeState;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

//最小的view
public class QCalendarTextView extends TextView {
	private static final int[] STATE_SELECTABLE = { R.attr.state_selectable };
	private static final int[] STATE_CURRENT_MONTH = { R.attr.state_current_month };
	private static final int[] STATE_TODAY = { R.attr.state_today };
	private static final int[] STATE_HIGHLIGHTED = { R.attr.state_highlighted };
	private static final int[] STATE_RANGE_FIRST = { R.attr.state_range_first };
	private static final int[] STATE_RANGE_MIDDLE = { R.attr.state_range_middle };
	private static final int[] STATE_RANGE_LAST = { R.attr.state_range_last };

	private boolean isSelectable = false;
	private boolean isCurrentMonth = false;
	private boolean isToday = false;
	private boolean isHighlighted = false;
	private RangeState rangeState = RangeState.NONE;

	public QCalendarTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public QCalendarTextView(Context context) {
		super(context);
	}

	public void setSelectable(boolean isSelectable) {
		this.isSelectable = isSelectable;
		refreshDrawableState();
	}

	public void setCurrentMonth(boolean isCurrentMonth) {
		this.isCurrentMonth = isCurrentMonth;
		refreshDrawableState();
	}

	public void setToday(boolean isToday) {
		this.isToday = isToday;
		refreshDrawableState();
	}

	public void setRangeState(QMonthCellDescriptor.RangeState rangeState) {
		this.rangeState = rangeState;
		refreshDrawableState();
	}

	public void setHighlighted(boolean highlighted) {
		isHighlighted = highlighted;
		refreshDrawableState();
	}

	public boolean isCurrentMonth() {
		return isCurrentMonth;
	}

	public boolean isToday() {
		return isToday;
	}

	public boolean isSelectable() {
		return isSelectable;
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 5);
		//
		if (isSelectable) {
			mergeDrawableStates(drawableState, STATE_SELECTABLE);
		}

		if (isCurrentMonth) {
			mergeDrawableStates(drawableState, STATE_CURRENT_MONTH);
		}

		if (isToday) {
			mergeDrawableStates(drawableState, STATE_TODAY);
		}

		if (isHighlighted) {
			mergeDrawableStates(drawableState, STATE_HIGHLIGHTED);
		}

		if (rangeState == QMonthCellDescriptor.RangeState.FIRST) {
			mergeDrawableStates(drawableState, STATE_RANGE_FIRST);
		} else if (rangeState == QMonthCellDescriptor.RangeState.MIDDLE) {
			mergeDrawableStates(drawableState, STATE_RANGE_MIDDLE);
		} else if (rangeState == RangeState.LAST) {
			mergeDrawableStates(drawableState, STATE_RANGE_LAST);
		}

		return drawableState;
	}
}
