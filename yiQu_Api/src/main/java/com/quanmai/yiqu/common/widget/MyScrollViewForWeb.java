package com.quanmai.yiqu.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by 殷伟超 on 2016/6/14.
 */
public class MyScrollViewForWeb extends ScrollView {
    private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;

    public MyScrollViewForWeb(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    // Return false if we're scrolling in the x direction
    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }

    /**
     * 公共接口：ScrollView滚动监听
     */
    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollView who, int x, int y, int oldx, int oldy);
    }

    private OnScrollChangedListener mOnScrollChangedListener;

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (mOnScrollChangedListener != null) {
            //使用公共接口触发滚动信息的onScrollChanged方法，将滚动位置信息暴露给外部
            mOnScrollChangedListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    /**
     * 暴露给外部的方法：设置滚动监听
     * @param listener
     */
    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }
}
