package com.quanmai.yiqu.common.widget;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
public class CommonSlidingPaneLayout extends SlidingPaneLayout {
	CommonMainView mainView;
	public CommonSlidingPaneLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setMainView(CommonMainView view) {
		mainView = view;

	}

	public CommonSlidingPaneLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CommonSlidingPaneLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		if (mainView!=null) {
			if(mainView.isTouch)
			{
			return false;
			}
		} 
			return super.onInterceptTouchEvent(e);
		

		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (mainView!=null) {
			if(mainView.isTouch)
			{
			return false;
			}
		} 
		return super.onTouchEvent(arg0);
	}
}
