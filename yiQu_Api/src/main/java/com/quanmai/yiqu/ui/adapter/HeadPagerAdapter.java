package com.quanmai.yiqu.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by zhanjinj on 16/3/6.
 */
public class HeadPagerAdapter extends PagerAdapter {

	List<ImageView> mViewList;


	public HeadPagerAdapter(List<ImageView> imageViewList){
		if (imageViewList.size()>0){
			this.mViewList = imageViewList;
		}
	}


	@Override
	public int getCount() {
		if (mViewList.size()>1){
			return Integer.MAX_VALUE;
		}else {
			return mViewList.size();
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		position %= mViewList.size();
		if (position<0){
			position = mViewList.size()+position;
		}
		ImageView imageView = mViewList.get(position);

		ViewParent vp = imageView.getParent();
		if (vp!=null){
			ViewGroup vg = (ViewGroup)vp;
			vg.removeView(imageView);
		}

		container.addView(imageView);

		return imageView;
	}
}
