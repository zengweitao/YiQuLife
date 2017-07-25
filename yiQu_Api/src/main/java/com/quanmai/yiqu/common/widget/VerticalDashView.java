package com.quanmai.yiqu.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.quanmai.yiqu.R;

public class VerticalDashView extends View {

    private Paint paint;

    public VerticalDashView(Context context) {
        super(context);
    }

    public VerticalDashView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalDashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        paint.setStrokeWidth(widthSize);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#898989"));
        setLayerType(LAYER_TYPE_SOFTWARE, paint);
        PathEffect effects = new DashPathEffect(new float[]{10, 10}, 1);
        paint.setPathEffect(effects);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0, getTop(), 0, getBottom(), paint);
    }
}