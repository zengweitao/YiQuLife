// Copyright 2013 Square, Inc.
package com.quanmai.yiqu.ui.mys.record.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.mys.record.view.QMonthCellDescriptor.RangeState;

//最小的view
public class QCalendarCellView extends FrameLayout {
    private static final int[] STATE_SELECTABLE = {R.attr.state_selectable};
    private static final int[] STATE_CURRENT_MONTH = {R.attr.state_current_month};
    private static final int[] STATE_TODAY = {R.attr.state_today};
    private static final int[] STATE_HIGHLIGHTED = {R.attr.state_highlighted};
    private static final int[] STATE_RANGE_FIRST = {R.attr.state_range_first};
    private static final int[] STATE_RANGE_MIDDLE = {R.attr.state_range_middle};
    private static final int[] STATE_RANGE_LAST = {R.attr.state_range_last};
    private boolean isSelectable = false;
    private boolean isCurrentMonth = false;
    private boolean isToday = false;
    private boolean isHighlighted = false;
    private RangeState rangeState = RangeState.NONE;
    private QCalendarTextView textView;
    private TextView text;
    private ImageView imgBg;
    private Context mContext;

    public QCalendarCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutParams lp = new LayoutParams(Utils.dp2px(mContext, 32),
                Utils.dp2px(mContext, 32));
        lp.gravity = Gravity.CENTER;
        imgBg = new ImageView(mContext);
        imgBg.setBackgroundResource(R.drawable.bg_oval);
        addView(imgBg, lp);
        imgBg.setVisibility(GONE);

        ColorStateList colors = getResources().getColorStateList(
                R.color.calendar_text_selector);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        textView = new QCalendarTextView(context);
        textView.setTextColor(colors);
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);
        addView(textView, layoutParams);

        LayoutParams layoutParams1 = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams1.gravity = Gravity.RIGHT;
        text = new TextView(context);
        text.setTextColor(0xff0094da);
        text.setTextSize(14);
        text.setGravity(Gravity.RIGHT);
        addView(text, layoutParams1);
    }

    public CharSequence getText() {
        return textView.getText();
    }

    public void setText(int s) {
        textView.setText(s);
    }

    public void setMark(String s) {
        if ("is_sign_in".equals(s) || "is_inspection".equals(s)) {
            text.setVisibility(View.GONE);
            textView.setTextColor(getResources().getColor(R.color.orange_f5a623));
            imgBg.setVisibility(VISIBLE);

            setEnabled(true);
            setClickable(true);
            return;
        }
        text.setText(s);
    }

    public void setTypeface(Typeface typeface) {
        textView.setTypeface(typeface);

    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setCellTextColor(ColorStateList colors) {
        // textView.setTextColor(colors);
    }

    public void setText(CharSequence s) {
        textView.setText(s);
    }

    public void setSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
        textView.setSelectable(isSelectable);
        refreshDrawableState();
    }

    public void setCurrentMonth(boolean isCurrentMonth) {
        this.isCurrentMonth = isCurrentMonth;
        textView.setCurrentMonth(isCurrentMonth);
        refreshDrawableState();
    }

    public void setToday(boolean isToday) {
        this.isToday = isToday;
        textView.setToday(isToday);
        refreshDrawableState();
    }

    public void setRangeState(QMonthCellDescriptor.RangeState rangeState) {
        this.rangeState = rangeState;
        textView.setRangeState(rangeState);
        refreshDrawableState();
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
        textView.setHighlighted(highlighted);
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
