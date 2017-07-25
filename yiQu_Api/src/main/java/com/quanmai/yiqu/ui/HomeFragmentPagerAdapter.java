package com.quanmai.yiqu.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页页面滑动适配器
 *
 * @author WZX
 */
public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    Fragment mCurrentFragment = new Fragment();

    List<Fragment> mList;

    public HomeFragmentPagerAdapter(FragmentManager fms) {
        super(fms);
        mList = new ArrayList<>();
    }

    public void addItem(Fragment fragment) {
        mList.add(fragment);
    }

    @Override
    public Fragment getItem(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        this.mCurrentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
