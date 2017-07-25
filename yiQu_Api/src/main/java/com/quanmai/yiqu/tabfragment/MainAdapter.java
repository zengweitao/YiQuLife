package com.quanmai.yiqu.tabfragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.fragment.HomePageFragment;
import com.quanmai.yiqu.ui.fragment.PersonalFragment;
import com.quanmai.yiqu.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/13.
 */

public class MainAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private AlphaTabsIndicator alphaTabsIndicator;
    private List<Fragment> fragments = new ArrayList<>();
    private String[] titles = {"首页", "我的"};

    public MainAdapter(FragmentManager fm, Context context) {
        super(fm);
        fragments.add(HomePageFragment.newInstance(titles[0]));
        fragments.add(PersonalFragment.newInstance(titles[1]));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        /*if (0 == position) {
            alphaTabsIndicator.getTabView(0).showNumber(alphaTabsIndicator.getTabView(0).getBadgeNumber() - 1);
        } else if (2 == position) {
            alphaTabsIndicator.getCurrentItemView().removeShow();
        } else if (3 == position) {
            alphaTabsIndicator.removeAllBadge();
        }*/
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
