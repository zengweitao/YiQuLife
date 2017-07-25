package com.quanmai.yiqu.ui.recycle.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 回收订单滑动适配器
 *
 * @author zhanjinj
 */
public class RecycleOrderPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public void addItem(Fragment fragment) {
        if (fragment != null) {
            mFragments.add(fragment);
        }
    }

    public RecycleOrderPagerAdapter(FragmentManager fm) {
        super(fm);
        this.mFragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
