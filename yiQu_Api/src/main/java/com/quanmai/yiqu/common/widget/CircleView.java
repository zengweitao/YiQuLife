package com.quanmai.yiqu.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.quanmai.yiqu.R;


/**
 * Created by 95138 on 2016/2/29.
 */
public class CircleView extends View {

    Paint mFirstPaint ,mSecondPaint,mThirdPaint;
    int mBorderWidth;  //圆的厚度
    Shader mLinearGradient;  //渐变色
    float mProgress; //进度
    boolean isStop = true;
    double degree;

    public CircleView(Context context) {
        this (context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this (context, attrs, 0 );
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView ,defStyleAttr,0 );

        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleView_mBorderWidth ,
                (int)TypedValue. applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f
                        , getResources().getDisplayMetrics()));

        a.recycle();

        intPaint();

    }

    private void intPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true); //锯齿
        paint.setStyle(Paint.Style. STROKE); //填充样式
        paint.setDither( true);
        paint.setStrokeCap(Paint.Cap.ROUND);  //笔头
        paint.setStrokeWidth(mBorderWidth);

        mFirstPaint = new Paint(paint);
        mSecondPaint = new Paint(paint);
        mThirdPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centre = getWidth()/2 ;  //圆中心的 x坐标，以自身view为标准
        int radius = centre-mBorderWidth/2;  //半径
        // 第一个底圆
        mLinearGradient = new LinearGradient(0,0,0,getHeight(),
                Color.parseColor("#F9F9F9"),Color. parseColor("#EFEFEF"), Shader.TileMode.CLAMP );
        mFirstPaint .setShader(mLinearGradient);
        canvas.drawCircle(centre, centre, radius, mFirstPaint);

        //第二个渐变圆
        mLinearGradient = new LinearGradient(0,0,0,getHeight(),
                Color.parseColor("#FE855A"),Color. parseColor("#F25867"), Shader.TileMode.CLAMP );
        mSecondPaint .setShader(mLinearGradient);

        RectF oval = new RectF(centre-radius,centre-radius,centre+radius,centre+radius);
        canvas.drawArc(oval, -90, mProgress, false, mSecondPaint);

        // 第三个阴影圆
        mThirdPaint .setColor(Color.parseColor( "#000000"));
        mThirdPaint.setAlpha(20);
        mThirdPaint.setAntiAlias(true);
        mThirdPaint.setDither(true);
        mThirdPaint.setStrokeWidth(1);
//        mThirdPaint.setStrokeWidth(mBorderWidth);
        mThirdPaint .setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(centre,centre,radius,mThirdPaint);
        canvas.drawCircle(centre,centre,centre+1, mThirdPaint);
        canvas.drawCircle(centre,centre,(radius-mBorderWidth /2 )-1 ,mThirdPaint);
    }

    public double getDegree() {
        return degree;
    }

    public void setDegree(final double degree) {

        new Thread(){
            @Override
            public void run() {
                while (isStop){
                    mProgress++;
                    if (mProgress==degree){
                        isStop= false;
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}