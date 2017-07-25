package com.quanmai.yiqu.tabfragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * viewpager切换加载fragment类
 */
public class AlphaTabsIndicator extends LinearLayout {

    public ViewPager mViewPager;
    private OnTabChangedListner mListner;
    private List<AlphaTabView> mTabViews;
    private boolean ISINIT;

    //子View的数量
    private int mChildCounts;

    //当前的条目索引
    private int mCurrentItem = 0;

    public AlphaTabsIndicator(Context context) {
        this(context, null);
    }

    public AlphaTabsIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaTabsIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        post(new Runnable() {
            @Override
            public void run() {
                isInit();
            }
        });
    }

    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
        init();
    }

    public void setOnTabChangedListner(OnTabChangedListner listner) {
        this.mListner = listner;
        isInit();
    }

    public AlphaTabView getCurrentItemView() {
        isInit();
        return mTabViews.get(mCurrentItem);
    }

    public AlphaTabView getTabView(int tabIndex) {
        isInit();
        return mTabViews.get(tabIndex);
    }

    public void removeAllBadge() {
        isInit();
        for (AlphaTabView alphaTabView : mTabViews) {
            alphaTabView.removeShow();
        }
    }

    public void setTabCurrenItem(int tabIndex) {
        if (tabIndex < mChildCounts && tabIndex > -1) {
            mTabViews.get(tabIndex).performClick();
        } else {
            throw new IllegalArgumentException("IndexOutOfBoundsException");
        }

    }

    private void isInit() {
        if (!ISINIT) {
            init();
        }
    }

    public void init() {
        ISINIT = true;
        mTabViews = new ArrayList<>();
        mChildCounts = getChildCount();

        if (null != mViewPager) {
            if (null == mViewPager.getAdapter()) {
                throw new NullPointerException("viewpager的adapter为null");
            }
            if (mViewPager.getAdapter().getCount() != mChildCounts) {
                throw new IllegalArgumentException("子View数量必须和ViewPager条目数量一致");
            }
            //对ViewPager添加监听
            mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        }

        for (int i = 0; i < mChildCounts; i++) {
            if (getChildAt(i) instanceof AlphaTabView) {
                AlphaTabView tabView = (AlphaTabView) getChildAt(i);
                mTabViews.add(tabView);
                //设置点击监听
                tabView.setOnClickListener(new MyOnClickListener(i));
            } else {
                throw new IllegalArgumentException("TabIndicator的子View必须是TabView");
            }
        }

        mTabViews.get(mCurrentItem).setIconAlpha(1.0f);
    }

    private class MyOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //滑动时的透明度动画
            if (positionOffset > 0) {
                mTabViews.get(position).setIconAlpha(1 - positionOffset);
                mTabViews.get(position + 1).setIconAlpha(positionOffset);
            }
            //滑动时保存当前按钮索引
            mCurrentItem = position;
        }

        @Override
        public void onPageSelected(int position) {
            if (!Session.get(getContext()).isLogin()&&position==1){
                initViewPager(0);
                Utils.showCustomToast(getContext(), "未登录，请登录后再试");
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
            super.onPageSelected(position);
            resetState();
            mTabViews.get(position).setIconAlpha(1.0f);
            mCurrentItem = position;
        }
    }

    public  void initViewPager(int page){
        if (null != mListner) {
            mListner.onTabSelected(page);
        }
        if (null != mViewPager) {
            //不能使用平滑滚动，否者颜色改变会乱
            mViewPager.setCurrentItem(page, false);
        }
    }

    private class MyOnClickListener implements OnClickListener {

        private int currentIndex;

        public MyOnClickListener(int i) {
            this.currentIndex = i;
        }

        @Override
        public void onClick(View v) {

            if (Session.get(getContext()).isLogin()) {
                //点击前先重置所有按钮的状态
                resetState();
                mTabViews.get(currentIndex).setIconAlpha(1.0f);
                if (null != mListner) {
                    mListner.onTabSelected(currentIndex);
                }
                if (null != mViewPager) {
                    //不能使用平滑滚动，否者颜色改变会乱
                    mViewPager.setCurrentItem(currentIndex, false);
                }
                //点击是保存当前按钮索引
                mCurrentItem = currentIndex;
            } else {
                if (currentIndex==1){
                    Utils.showCustomToast(getContext(), "未登录，请登录后再试");
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    if (null != mListner) {
                        mListner.onTabSelected(0);
                    }
                    if (null != mViewPager) {
                        //不能使用平滑滚动，否者颜色改变会乱
                        mViewPager.setCurrentItem(0, false);
                    }
                    //点击是保存当前按钮索引
                    mCurrentItem = 0;
                }
            }
        }
    }

    /**
     * 重置所有按钮的状态
     */
    private void resetState() {
        for (int i = 0; i < mChildCounts; i++) {
            mTabViews.get(i).setIconAlpha(0);
        }
    }

    private static final String STATE_INSTANCE = "instance_state";
    private static final String STATE_ITEM = "state_item";

    /**
     * @return 当View被销毁的时候，保存数据
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_ITEM, mCurrentItem);
        return bundle;
    }

    /**
     * @param state 用于恢复数据使用
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentItem = bundle.getInt(STATE_ITEM);
            //重置所有按钮状态
            resetState();
            //恢复点击的条目颜色
            mTabViews.get(mCurrentItem).setIconAlpha(1.0f);
            super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
