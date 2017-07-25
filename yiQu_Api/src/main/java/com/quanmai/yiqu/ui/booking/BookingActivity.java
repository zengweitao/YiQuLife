package com.quanmai.yiqu.ui.booking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseFragmentActivity;
import com.quanmai.yiqu.base.BaseFragmentPagerAdapter;
import com.quanmai.yiqu.ui.booking.fragment.BookingFragment;
import com.quanmai.yiqu.ui.booking.fragment.GarbageThrowFragment;
import com.quanmai.yiqu.ui.recycle.RecycleOrderActivity;

/**
 * 废品回收&废品投放 页面
 */
public class BookingActivity extends BaseFragmentActivity implements View.OnClickListener {

    static final String RECORD_RECYCLE = "订单记录";
    static final String RECORD_THROW = "投放记录";

    public static Activity instance;

    ImageView iv_back;
    TextView textViewGarbageRecycle;    //废品回收
    TextView textViewGarbageThrow;      //废品投放
    TextView tv_right;
    ViewPager viewPager;
    BaseFragmentPagerAdapter mPagerAdapter;

    String strCurrentType;  //当前页面显示“废品回收”或者“废品投放”


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        initView();
        init();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        textViewGarbageRecycle = (TextView) findViewById(R.id.textViewGarbageRecycle);
        textViewGarbageThrow = (TextView) findViewById(R.id.textViewGarbageThrow);
        tv_right = (TextView) findViewById(R.id.tv_right);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //点击事件
        textViewGarbageRecycle.setOnClickListener(this);
        textViewGarbageThrow.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        instance = this;
    }

    //初始化
    private void init() {
        strCurrentType = RECORD_THROW;    //默认显示投放记录
        tv_right.setText(strCurrentType);

        mPagerAdapter = new BaseFragmentPagerAdapter(this.getSupportFragmentManager());
        mPagerAdapter.addItem(new GarbageThrowFragment());
        mPagerAdapter.addItem(new BookingFragment());
        viewPager.setAdapter(mPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: {
                        textViewGarbageRecycle.setBackgroundResource(R.drawable.bg_booking_right_fill);
                        textViewGarbageRecycle.setTextColor(Color.parseColor("#ffffff"));
                        textViewGarbageThrow.setBackgroundResource(R.drawable.bg_booking_left);
                        textViewGarbageThrow.setTextColor(getResources().getColor(R.color.theme));
                        strCurrentType = RECORD_THROW;
                        break;
                    }
                    case 1: {
                        textViewGarbageRecycle.setBackgroundResource(R.drawable.bg_booking_right);
                        textViewGarbageRecycle.setTextColor(mContext.getResources().getColor(R.color.theme));
                        textViewGarbageThrow.setBackgroundResource(R.drawable.bg_booking_left_fill);
                        textViewGarbageThrow.setTextColor(Color.parseColor("#ffffff"));
                        strCurrentType = RECORD_RECYCLE;

                        break;
                    }
                }
                tv_right.setText(strCurrentType);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //废品投放
            case R.id.textViewGarbageThrow: {
                viewPager.setCurrentItem(0);
                break;
            }
            //垃圾回收
            case R.id.textViewGarbageRecycle: {
                if (UserInfo.get().usertype.contains(UserInfo.USER_RECYCLE)) {
                    startActivity(new Intent(mContext, RecycleOrderActivity.class));
                    return;
                }
                viewPager.setCurrentItem(1);
                break;
            }
            //回收记录
            case R.id.tv_right: {
                switch (strCurrentType) {
                    case RECORD_RECYCLE: {
                        startActivity(RecycleRecordActivity.class);
                        break;
                    }
                    case RECORD_THROW: {
                        startActivity(GarbageThrowRecordActivity.class);
                        break;
                    }
                }
                break;
            }

            default:
                break;
        }
    }

}
