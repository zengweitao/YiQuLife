package com.quanmai.yiqu.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2016/12/29.
 */

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter{
    private List<Fragment> mFragments;

    public BaseFragmentPagerAdapter(FragmentManager fm) {
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

    public void addItem(Fragment fragment) {
        if (fragment != null) {
            mFragments.add(fragment);
        }
    }
}
