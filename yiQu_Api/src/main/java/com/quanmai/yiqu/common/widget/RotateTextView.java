package com.quanmai.yiqu.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zhanjinj on 16/6/20.
 */
public class RotateTextView extends TextView {
    public RotateTextView(Context context) {
        super(context);
    }


    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(45, getMeasuredWidth() / 3, getMeasuredWidth() / 3);
        super.onDraw(canvas);
    }
}
