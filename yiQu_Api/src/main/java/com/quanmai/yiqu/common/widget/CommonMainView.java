package com.quanmai.yiqu.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class CommonMainView extends FrameLayout {
	public boolean isTouch=false;
	public CommonMainView(Context context) {
		super(context);
	}

	public CommonMainView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
		    isTouch=true;
			break;
		case MotionEvent.ACTION_CANCEL:
			isTouch=false;
			break;
		case MotionEvent.ACTION_UP:
			isTouch=false;
			break;
		default:
			break;
		}
		 return super.onInterceptTouchEvent(e);
	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent arg0) {
//		Utils.E("onTouchEvent");
//		switch (arg0.getAction()) {
//		case MotionEvent.ACTION_UP:
//			isTouch=false;
//			break;
//		default:
//			break;
//		}
//		return super.onTouchEvent(arg0);
//	}



}
