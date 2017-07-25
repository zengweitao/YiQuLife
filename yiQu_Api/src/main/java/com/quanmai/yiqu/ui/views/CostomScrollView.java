package com.quanmai.yiqu.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by admin on 2017/6/8.
 */

public class CostomScrollView extends ScrollView {
    private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;
    public CostomScrollView(Context context) {
        super(context);
        mGestureDetector = new GestureDetector(context, onGestureListener);
    }

    public CostomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, onGestureListener);
    }

    public CostomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGestureDetector = new GestureDetector(context, onGestureListener);
    }
    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceY) >= Math.abs(distanceX)) {
                return true;
            }
            // 当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview 停住不能滚动
            setParentScrollAble(false);
            return false;
        }
    };

    /**
     * 是否把滚动事件交给父scrollview
     *
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {
        // 这里的parentScrollView就是listview外面的那个scrollview
        getParent().requestDisallowInterceptTouchEvent(!flag);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    /*
    *
    * 重写该方法，达到使ListView适应ScrollView的效果
    */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
