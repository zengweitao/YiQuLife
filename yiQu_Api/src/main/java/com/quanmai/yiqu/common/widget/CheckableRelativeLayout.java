package com.quanmai.yiqu.common.widget;

/**
 * Created by James on 2016/8/18.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import com.quanmai.yiqu.R;

/**
 * Created by James on 2016/8/17.
 * 具有选中、未选中状态的RelativeLayout
 */
public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
    private boolean mChecked;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public CheckableRelativeLayout(Context context) {
        this(context, null);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckableRelativeLayout, defStyleAttr, 0);
        mChecked = typedArray.getBoolean(R.styleable.CheckableRelativeLayout_checked, false);
        typedArray.recycle();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace+1);
        if (isChecked()){
            mergeDrawableStates(drawableState,CHECKED_STATE_SET);
        }
        return drawableState;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        if (onCheckedChangeListener != null) {
            this.mOnCheckedChangeListener = onCheckedChangeListener;
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param RelativeLayout The compound relativeLayout view whose state has changed.
         * @param isChecked      The new checked state of buttonView.
         */
        void onCheckedChanged(RelativeLayout RelativeLayout, boolean isChecked);
    }
}