package com.quanmai.yiqu.common.widget;

import java.util.ArrayList;

import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.AdvertInfo;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class FocusMap extends FrameLayout implements OnPageChangeListener {
	protected Context mContext;
	/**
	 * 装点点的ImageView数组
	 */
	ViewGroup group;
	private ViewPager advPager = null;
	protected LayoutInflater mInflater;
	private ImageView[] imageViews = null;
    int height=-1;
    Boolean is_visiable = true;
	public FocusMap(Context context,int height) {
		super(context);
		this.height=height;
		init(context);
	}

	public FocusMap(Context context) {
		super(context);
		init(context);
	}
	public FocusMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ViewPager getPagerView() {
		return advPager;

	}

	private void init(Context context) {
		mArrayList=new ArrayList<AdvertInfo>();
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.view_focus_map, this);
		advPager = (ViewPager) findViewById(R.id.adv_pager);
		group = (ViewGroup)findViewById(R.id.viewGroup);
		advPager.setOnPageChangeListener(this);
		// View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		// int h=view.getMeasuredWidth()/5;
		// int h=getWidth()/5;
		if(height<0)
		{
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay()
			.getMetrics(dm);
			height=dm.widthPixels *25/64; // 得到宽度
		}

		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				height);
		setLayoutParams(layoutParams);
//		addView(view);
	}

	@Override
	public void onPageScrollStateChanged(int position) {

	}



	private Runnable mTicker;
	private Handler mHandler;
	/** 运行启动自动翻页 **/
	private boolean Automaticsliding = false;
	int nowpage;
	/** 是否正在执行自动翻页 **/
	private boolean running = false;

	public void setAutomaticsliding(boolean b) {
		if (b) {

			if (!Automaticsliding) {
				Automaticsliding = true;
				if (!isRunning()) {
					OnRun();
					mHandler = new Handler();
					mTicker = new Runnable() {
						public void run() {
							setNextItem();
							if (Automaticsliding) {
								mHandler.postDelayed(mTicker, 4000);
							} else {
								OnStop();
							}
						}
					};
					mHandler.postDelayed(mTicker, 4000);
				} else {
					/* 如果还在运行，则不重新开启 */
//					Log.e("q", "无改变");
				}
			} else {
				/* 如果已经自动翻页，则不再开启自动翻页 */
			}
		} else {
			Automaticsliding = false;
		}

	}

	private void OnRun() {
//		Log.e("q", "开始运行");
		running = true;
	}

	private void OnStop() {
//		Log.e("q", "停止运行");
		running = false;

	}

	public boolean isRunning() {
		return running;
	}

	/**
	 * 向下翻一页，如果为空或者长度为1，则不支持自动翻页，关闭自动翻页
	 */
	public void setNextItem() {
		if (imageViews == null) {
			Automaticsliding = false;
			return;
		}
		if (imageViews.length <= 1) {
			Automaticsliding = false;
			return;
		}
		if (nowpage != advPager.getCurrentItem()) {
			nowpage = advPager.getCurrentItem();
			return;
		}
		nowpage++;
		if (nowpage > imageViews.length - 1) {
			nowpage = 0;
		}
		advPager.setCurrentItem(nowpage, true);
		// }
	}

	public void setCurrentItem(int position) {
		advPager.setCurrentItem(position);
	}


	public AdvertInfo getItem(int position) {
		return mArrayList.get(position);

	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		for (int i = 0; i < imageViews.length; i++) {
			imageViews[position % imageViews.length]
					.setBackgroundResource(R.drawable.adv_focus_radius);
			if (position % imageViews.length != i) {
				imageViews[i].setBackgroundResource(R.drawable.adv_radius);
			}
		}
	}
	ArrayList<AdvertInfo> mArrayList;
	public void setAdapter(ArrayList<AdvertInfo> arrayList) {
		mArrayList=arrayList;
		imageViews = new ImageView[arrayList.size()];
		group.removeAllViews();
		if (arrayList.size() > 1) {
			for (int i = 0; i < arrayList.size(); i++) {
				imageViews[i] = new ImageView(mContext);
				imageViews[i].setScaleType(ImageView.ScaleType.FIT_XY);

				if (i == 0) {
					// 默认选中第一张图片
					imageViews[i]
							.setBackgroundResource(R.drawable.adv_focus_radius);
				} else {
					imageViews[i].setBackgroundResource(R.drawable.adv_radius);
				}
				LinearLayout.LayoutParams wmParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				wmParams.setMargins(3, 0, 3, 5);
				wmParams.width = 10;
				wmParams.height = 10;
				group.addView(imageViews[i], wmParams);
			}
		}
		AdvAdapter adapter = new AdvAdapter(mContext, arrayList);
		advPager.setAdapter(adapter);
	}

	private class AdvAdapter extends PagerAdapter {
		private ArrayList<AdvertInfo> mArrayList = null;
		private Context mContext;

		public AdvAdapter(Context context, ArrayList<AdvertInfo> list) {
			mContext = context;
			if (list != null) {
				mArrayList = list;
			} else {
				mArrayList = new ArrayList<AdvertInfo>();
			}

		}

		@Override
		public int getCount() {
			return mArrayList.size();

		}


		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ScaleType.FIT_XY);
			ImageloaderUtil.displayImage(mContext, mArrayList.get(position).picurl, imageView);


//			imageView.setImageResource(R.drawable.test_1);
			// imageView.setImageResource((Integer) mArrayList.get(position));
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (mOnScrollListener != null) {
						mOnScrollListener.onItemClick(position,
								mArrayList.get(position));
					}
				}
			});

			((ViewPager) container).addView(imageView, 0);

			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}

	OnItemClickListener mOnScrollListener;

	public interface OnItemClickListener {
		public void onItemClick(int position, Object object);
	}

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnScrollListener = l;
	}

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// getParent().requestDisallowInterceptTouchEvent(true);
	// return super.onInterceptTouchEvent(ev);
	// }
	private float xDistance, yDistance, xLast, yLast;

	public boolean onInterceptTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:// 按下
			xDistance = yDistance = 0f;
			xLast = event.getX();
			yLast = event.getY();
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		// 滑动，在此对里层viewpager的第一页做处理
		case MotionEvent.ACTION_MOVE:
			// 里层viewpager已经是第一页，此时继续向左滑（手指从左往右滑）
			final float curX = event.getX();
			final float curY = event.getY();
			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			if (yDistance > xDistance) {
				getParent().requestDisallowInterceptTouchEvent(false);
			} else if (xLast < curX && advPager.getCurrentItem() == 0) {
				getParent().requestDisallowInterceptTouchEvent(false);
			} else {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			xLast = curX;
			yLast = curY;
			break;
		case MotionEvent.ACTION_UP:// 抬起
		case MotionEvent.ACTION_CANCEL:
			getParent().requestDisallowInterceptTouchEvent(false);
			break;
		}
		return false;
	}
	// 注入里层viewpager

}
