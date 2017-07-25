package com.quanmai.yiqu.common.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private float xDistance, yDistance, xLast, yLast, xDown, yDown;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xDown = xLast = ev.getX();
			yDown = yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();
			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;
			if (xDistance > yDistance) {
				//如果是最后一项,且往左边滑动时
				//或是第一项，且往右边滑动时
				if ((this.getCurrentItem() == this.getAdapter().getCount() - 1 && curX - xDown < 0)
						|| this.getCurrentItem() == 0 && curX - xDown > 0) {
					break;
				}
				getParent().requestDisallowInterceptTouchEvent(true);// 这句话的作用
				// 告诉父view，我的单击事件我自行处理，不要阻碍我。
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
