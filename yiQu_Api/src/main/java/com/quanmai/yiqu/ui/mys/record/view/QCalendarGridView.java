// Copyright 2012 Square, Inc.
package com.quanmai.yiqu.ui.mys.record.view;

import com.quanmai.yiqu.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * 绘制一个日历的网格. All children must be {@link QCalendarRowView}s. 比如分割线就是在这画的
 * 第一行是标题没有分频器上面画出它。
 */
public class QCalendarGridView extends ViewGroup {
	/**
	 * 网格线不正排队在某些设备 (Nexus 7, Nexus 5). Fudging the 协调了半个点似乎解决这个问题同上不破坏其它设备.
	 */
	private static final float FLOAT_FUDGE = 0.5f;

	private final Paint dividerPaint = new Paint();
	private int oldWidthMeasureSize;
	private int oldNumRows;

	public QCalendarGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		dividerPaint
				.setColor(getResources().getColor(R.color.calendar_divider));
	}

	public void setDividerColor(int color) {
		dividerPaint.setColor(color);
	}

	public void setDayBackground(int resId) {
		for (int i = 1; i < getChildCount(); i++) {
			if (i > 1)
				((QCalendarRowView) getChildAt(i)).setCellBackground(resId);
		}
	}

	public void setDayTextColor(int resId) {
		for (int i = 0; i < getChildCount(); i++) {
			ColorStateList colors = getResources().getColorStateList(resId);
			if (i > 1)
				((QCalendarRowView) getChildAt(i)).setCellTextColor(colors);
		}
	}

	public void setDisplayHeader(boolean displayHeader) {
		getChildAt(0).setVisibility(displayHeader ? VISIBLE : GONE);
	}

	public void setTypeface(Typeface typeface) {
		for (int i = 0; i < getChildCount(); i++) {
			if (i > 1)
				((QCalendarRowView) getChildAt(i)).setTypeface(typeface);
		}
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		if (getChildCount() == 0) {
			((QCalendarRowView) child).setIsHeaderRow(true);
		}
		super.addView(child, index, params);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		// final ViewGroup row = (ViewGroup) getChildAt(1);
		// int top = row.getTop();
		// int bottom = getBottom();
		// // Left side border.
		// final int left = row.getChildAt(0).getLeft() + getLeft();
		// canvas.drawLine(left + FLOAT_FUDGE, top, left + FLOAT_FUDGE, bottom,
		// dividerPaint);
		//
		// // Each cell's right-side border.
		// for (int c = 0; c < 7; c++) {
		// float x = left + row.getChildAt(c).getRight() - FLOAT_FUDGE;
		// canvas.drawLine(x, top, x, bottom, dividerPaint);
		// }
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		final boolean retVal = super.drawChild(canvas, child, drawingTime);
		// Draw a bottom border.
		final int bottom = child.getBottom() - 1;
		canvas.drawLine(child.getLeft(), bottom, child.getRight() - 2, bottom,
				dividerPaint);
		return retVal;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		QLogr.d("Grid.onMeasure w=%s h=%s",
				MeasureSpec.toString(widthMeasureSpec),
				MeasureSpec.toString(heightMeasureSpec));
		int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
		if (oldWidthMeasureSize == widthMeasureSize) {
			QLogr.d("SKIP Grid.onMeasure");
			setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
			return;
		}
		long start = System.currentTimeMillis();
		oldWidthMeasureSize = widthMeasureSize;
		int cellSize = widthMeasureSize / 7;
		// Remove any extra pixels since /7 is unlikely to give whole nums.
		widthMeasureSize = cellSize * 7;
		int totalHeight = 0;
		final int rowWidthSpec = makeMeasureSpec(widthMeasureSize, EXACTLY);
		final int rowHeightSpec = makeMeasureSpec(cellSize, EXACTLY);
		for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
			final View child = getChildAt(c);
			if (child.getVisibility() == View.VISIBLE) {
				if (c == 0) { // It's the header: height should be wrap_content.
					measureChild(child, rowWidthSpec,
							makeMeasureSpec(cellSize, AT_MOST));
				} else {
					measureChild(child, rowWidthSpec, rowHeightSpec);
				}
				totalHeight += child.getMeasuredHeight();
			}
		}
		final int measuredWidth = widthMeasureSize + 2; // Fudge factor to make
														// the borders show up.
		setMeasuredDimension(measuredWidth, totalHeight);
		QLogr.d("Grid.onMeasure %d ms", System.currentTimeMillis() - start);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		long start = System.currentTimeMillis();
		top = 0;
		for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
			final View child = getChildAt(c);
			final int rowHeight = child.getMeasuredHeight();
			child.layout(left, top, right, top + rowHeight);
			top += rowHeight;
		}
		QLogr.d("Grid.onLayout %d ms", System.currentTimeMillis() - start);
	}

	public void setNumRows(int numRows) {
		if (oldNumRows != numRows) {
			// If the number of rows changes, make sure we do a re-measure next
			// time around.
			oldWidthMeasureSize = 0;
		}
		oldNumRows = numRows;
	}
}
