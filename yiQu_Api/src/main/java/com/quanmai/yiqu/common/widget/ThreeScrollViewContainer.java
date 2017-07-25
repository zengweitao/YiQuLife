package com.quanmai.yiqu.common.widget;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * 包含两个ScrollView的容器 更多详解见博客http://dwtedx.com
 * 
 * @author chenjing
 * 
 */
public class ThreeScrollViewContainer extends RelativeLayout {

	/**
	 * 自动上滑
	 */
	public static final int AUTO_UP = 0;
	/**
	 * 自动下滑
	 */
	public static final int AUTO_DOWN = 1;
	/**
	 * 动画完成
	 */
	public static final int DONE = 2;
	/**
	 * 动画速度
	 */
	public static final float SPEED = 6.5f;

	private boolean isMeasured = false;

	/**
	 * 用于计算手滑动的速度
	 */
	private VelocityTracker vt;

	private int mViewHeight;
	private int mViewWidth;

	private View topView;
	private View centerView;
	private View bottomView;

	private boolean FirstcanPullUp;
	private boolean SecoundcanPullUp;
	private boolean SecoundcanPullDown;
	private boolean ThridcanPullDown;
	private int state = DONE;

	/**
	 * 记录当前展示的是哪个view，0是topView，1是centerView
	 */
	private int mCurrentViewIndex = 0;
	/**
	 * 手滑动距离，这个是控制布局的主要变量
	 */
	private float mMoveLen;
	private MyTimer mTimer;
	private float mLastY;
	/**
	 * 用于控制是否变动布局的另一个条件，mEvents==0时布局可以拖拽了，mEvents==-1时可以舍弃将要到来的第一个move事件，
	 * 这点是去除多点拖动剧变的关键
	 */
	private int mEvents;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
//			Log.e("state", "state=" + state);
//			Log.e("state", "mCurrentViewIndex=" + mCurrentViewIndex);
			if (mMoveLen != 0) {
				if (state == AUTO_UP) {
					mMoveLen -= SPEED;
			
					if (mCurrentViewIndex == 0&&mMoveLen <= -mViewHeight) {
						mMoveLen = -mViewHeight;
						state = DONE;
						mCurrentViewIndex = 1;
					} else if (mCurrentViewIndex == 2&&mMoveLen <= -2 * mViewHeight) {
						mMoveLen = -2*mViewHeight;
						state = DONE;
						mCurrentViewIndex = 2;
					}else if(mCurrentViewIndex==1)
					{
						if (SecoundcanPullDown&&mMoveLen <= -mViewHeight) {
							mMoveLen = -mViewHeight;
							state = DONE;
							mCurrentViewIndex = 1;
						} else if (mMoveLen <= -2 * mViewHeight) {
							mMoveLen = -2*mViewHeight;
							state = DONE;
							mCurrentViewIndex = 2;
						}
					}
				} else if (state == AUTO_DOWN) {
//					Log.e("state", "mCurrentViewIndex=" + mCurrentViewIndex);
//					Log.e("state", "mMoveLen=" + (-mMoveLen));
//					Log.e("state", "mViewHeight=" + mViewHeight);
					mMoveLen += SPEED;
					if (mCurrentViewIndex == 0&&mMoveLen >= 0) {
						mMoveLen = 0;
						state = DONE;
						mCurrentViewIndex = 0;
					}else if (mCurrentViewIndex == 2&&mMoveLen >= -mViewHeight) {
						mMoveLen = -mViewHeight;
						state = DONE;
						mCurrentViewIndex = 2;
					}else if(mCurrentViewIndex==1)
					{
//						Log.e("q", "SecoundcanPullDown="+SecoundcanPullDown);
						 if (SecoundcanPullUp&&mMoveLen >= -mViewHeight) {
							mMoveLen = -mViewHeight;
							state = DONE;
							mCurrentViewIndex = 1;
						}else if (mMoveLen >= 0) {
							mMoveLen = 0;
							state = DONE;
							mCurrentViewIndex = 0;
						  
						}
					}
					
					
				} else {
					mTimer.cancel();
				}
			} else if (mTimer != null) {
				mTimer.cancel();
			}

			requestLayout();
		}

	};

	public ThreeScrollViewContainer(Context context) {
		super(context);
		init();
	}

	public ThreeScrollViewContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ThreeScrollViewContainer(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		FirstcanPullUp = true;
		vt = VelocityTracker.obtain();
		mTimer = new MyTimer(handler);
	}
	private float xDistance, yDistance, xLast, yLast;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			vt.clear();
			mLastY = ev.getY();
			vt.addMovement(ev);
			mEvents = 0;
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			// 多一只手指按下或抬起时舍弃将要到来的第一个事件move，防止多点拖拽的bug
			mEvents = -1;
			break; 
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;

//			Log.e("q", "FirstcanPullUp=" + FirstcanPullUp);
//			Log.e("q", "SecoundcanPullDown=" + SecoundcanPullDown);
//			Log.e("q", "SecoundcanPullUp=" + SecoundcanPullUp);
//			Log.e("q", "mCurrentViewIndex=" + mCurrentViewIndex);
//			Log.e("q", "------------------------");
			vt.addMovement(ev);
			if (yDistance>xDistance&&FirstcanPullUp && mEvents == 0 && mCurrentViewIndex == 0) { // 当前在第一个时
				mMoveLen += (ev.getY() - mLastY);
//				Log.e("q", "mMoveLen=" + mMoveLen);
				// 防止上下越界
				if (mMoveLen > 0) { // 位移不小于0
					mMoveLen = 0;
					mCurrentViewIndex = 0;
				} else if (mMoveLen < -mViewHeight) { // 位移不大于一个屏幕
					mMoveLen = -mViewHeight;
					mCurrentViewIndex = 1;

				}
				if (mMoveLen < -8) {
					// 防止事件冲突
//					Log.e("q", " 防止事件冲突");
					ev.setAction(MotionEvent.ACTION_CANCEL);
				}
			} else if (SecoundcanPullDown && mEvents == 0
					&& mCurrentViewIndex == 1) { // 当前在第二个时

				mMoveLen += (ev.getY() - mLastY);
//				Log.e("q", "mMoveLen=" + mMoveLen);
				// 防止上下越界
				if (mMoveLen > 0) {
					mMoveLen = 0;
					mCurrentViewIndex = 0;
				} else if (mMoveLen < -mViewHeight) {
					mMoveLen = -mViewHeight;
					mCurrentViewIndex = 1;
				}

				if (mMoveLen > 8 - mViewHeight) {
					// 防止事件冲突
//					Log.e("q", " 防止事件冲突");
					ev.setAction(MotionEvent.ACTION_CANCEL);
				}
			}

			else if (SecoundcanPullUp && mEvents == 0 && mCurrentViewIndex == 1) { // 当前在第二个时

				mMoveLen += (ev.getY() - mLastY);
//				Log.e("q", "mMoveLen=" + mMoveLen);
				// 防止上下越界
				if (mMoveLen > -mViewHeight) {
					mMoveLen = -mViewHeight;
					mCurrentViewIndex = 1;
				} else if (mMoveLen < -2 * mViewHeight) {
					mMoveLen = -2 * mViewHeight;
					mCurrentViewIndex = 2;
				}

				if (mMoveLen > 8 - 2 * mViewHeight && mMoveLen < -mViewHeight) {
					// 防止事件冲突
//					Log.e("q", " 防止事件冲突");
					ev.setAction(MotionEvent.ACTION_CANCEL);
				}
			} else if (ThridcanPullDown && mEvents == 0
					&& mCurrentViewIndex == 2) { // 当前在第二个时
				mMoveLen += (ev.getY() - mLastY);
				// 防止上下越界
				if (mMoveLen < -2 * mViewHeight) {
					mMoveLen = -2 * mViewHeight;
					mCurrentViewIndex = 2;
				} else if (mMoveLen > -mViewHeight) {
					mMoveLen = -mViewHeight;
					mCurrentViewIndex = 1;
				}
				if (mMoveLen > 8 - 2 * mViewHeight && mMoveLen < -mViewHeight) {
					// 防止事件冲突
					ev.setAction(MotionEvent.ACTION_CANCEL);
				}
			} else
				mEvents++;

//			Log.e("q", "mEvents=" + mEvents);
			mLastY = ev.getY();
			requestLayout();
			break;
		case MotionEvent.ACTION_UP:
			mLastY = ev.getY();
			vt.addMovement(ev);
			vt.computeCurrentVelocity(700);
			// 获取Y方向的速度
			float mYV = vt.getYVelocity();
			if (mMoveLen == 0 || mMoveLen == -mViewHeight || mMoveLen == -2*mViewHeight)
			{}else {
//				if ((-mViewHeight<mMoveLen&&mMoveLen <= -mViewHeight / 2)  || (-2*mViewHeight<mMoveLen&&mMoveLen <= -mViewHeight*3 / 2)) {
				if (Math.abs(mYV) < 500) {
				// 速度小于一定值的时候当作静止释放，这时候两个View往哪移动取决于滑动的距离
////				if (mMoveLen <= -mViewHeight / 2) {
////					state = AUTO_UP;
////				} else
					if ((0>mMoveLen&&mMoveLen > -mViewHeight / 2)||(-mViewHeight>mMoveLen&&mMoveLen > -mViewHeight*3/ 2)) {
					state = AUTO_DOWN;
				}else {
					state = AUTO_UP;
				}
			} else {
				// 抬起手指时速度方向决定两个View往哪移动
				if (mYV < 0)
					state = AUTO_UP;
				else
					state = AUTO_DOWN;
			}
			mTimer.schedule(1);
			}

			break;

		}
		
		
		return super.dispatchTouchEvent(ev);
	}
	


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		topView.layout(0, (int) mMoveLen, mViewWidth,
				topView.getMeasuredHeight() + (int) mMoveLen);
		centerView.layout(0, topView.getMeasuredHeight() + (int) mMoveLen,
				mViewWidth, topView.getMeasuredHeight() + (int) mMoveLen
						+ centerView.getMeasuredHeight());
		bottomView.layout(0,
				topView.getMeasuredHeight() + centerView.getMeasuredHeight()
						+ (int) mMoveLen, mViewWidth,
				topView.getMeasuredHeight() + centerView.getMeasuredHeight()
						+ (int) mMoveLen + bottomView.getMeasuredHeight());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (!isMeasured) {
			isMeasured = true;

			mViewHeight = getMeasuredHeight();
			mViewWidth = getMeasuredWidth();

			topView = getChildAt(0);
			centerView = getChildAt(1);
			bottomView = getChildAt(2);

//			topView.setOnTouchListener(topViewTouchListener);
			centerView.setOnTouchListener(centerViewTouchListener);
			bottomView.setOnTouchListener(bottomViewTouchListener);

		}
	}

//	private OnTouchListener topViewTouchListener = new OnTouchListener() {
//
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			ScrollView sv = (ScrollView) v;
//			if (sv.getScrollY() == (sv.getChildAt(0).getMeasuredHeight() - sv
//					.getMeasuredHeight()) && mCurrentViewIndex == 0)
//				FirstcanPullUp = true;
//			else
//				FirstcanPullUp = false;
//			return false;
//		}
//	};
	private OnTouchListener centerViewTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ScrollView sv = (ScrollView) v;
			if (sv.getScrollY() == 0 && mCurrentViewIndex == 1)
				SecoundcanPullDown = true;
			else
				SecoundcanPullDown = false;

			if (sv.getScrollY() == (sv.getChildAt(0).getMeasuredHeight() - sv
					.getMeasuredHeight()) && mCurrentViewIndex == 1)
				SecoundcanPullUp = true;
			else
				SecoundcanPullUp = false;
			return false;
		}
	};

	private OnTouchListener bottomViewTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ScrollView sv = (ScrollView) v;
			if (sv.getScrollY() == 0 && mCurrentViewIndex == 2)
				ThridcanPullDown = true;
			else
				ThridcanPullDown = false;
			return false;
		}
	};

	class MyTimer {
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer(Handler handler) {
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period) {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		public void cancel() {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
		}

		class MyTask extends TimerTask {
			private Handler handler;

			public MyTask(Handler handler) {
				this.handler = handler;
			}

			@Override
			public void run() {
				handler.obtainMessage().sendToTarget();
			}

		}
	}

}
