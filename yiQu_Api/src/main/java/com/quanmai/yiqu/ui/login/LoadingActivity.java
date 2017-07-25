package com.quanmai.yiqu.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.platform.comapi.map.A;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GoodsApi;
import com.quanmai.yiqu.api.UserInfoApi;
import com.quanmai.yiqu.api.vo.AdInfo;
import com.quanmai.yiqu.api.vo.BannerInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.common.FileUtil;
import com.quanmai.yiqu.common.util.BitmapUtils;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.ui.HomeActivity;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class LoadingActivity extends Activity implements View.OnClickListener {
    private Animation animation;
    private View view;
    Context mContext;
    Session mSession;
    ViewPager viewPager;
    Button buttonExperience;
    List<ImageView> viewList;
    int[] res = new int[]{R.drawable.loading_one, R.drawable.loading_two, R.drawable.loading_three, R.drawable.loading_four_bg};
    private ImageView imgAdvertisement;
    private TextView tvCountDown;
    private ImageView imgLaunch;
    private RelativeLayout rlCountDown;
    private boolean isCountTime = false;
    private CountDownTimer mDownTimer;

    List<AdInfo> infos;     //广告列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view = View.inflate(this, R.layout.activity_loading, null);
        setContentView(view);
        mContext = LoadingActivity.this;
        initView();
        FileUtil.copyAssetsToFilesystem(this, "area.db",
                FileUtil.getOrCreateDbdir(this) + "area.db");
        mSession = Session.get(this);
        if (mSession.isLogin()) {
            GetUserInfo();
        }
    }

    private void initView() {
        infos = new ArrayList<AdInfo>();
        imgAdvertisement = (ImageView) findViewById(R.id.imgAdvertisement);
        tvCountDown = (TextView) findViewById(R.id.tvCountDown);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        buttonExperience = (Button) findViewById(R.id.buttonExperience);
        imgLaunch = (ImageView) findViewById(R.id.imgLaunch);
        rlCountDown = (RelativeLayout) findViewById(R.id.rlCountDown);
        rlCountDown.setOnClickListener(this);
    }

    private void GetUserInfo() {
        UserInfoApi.get().getUserHome(this, new ApiConfig.ApiRequestListener<UserInfo>() {
            @Override
            public void onSuccess(String msg, UserInfo data) {
                mSession.setBookingCommunity(UserInfo.get().community);
                Log.e("mark", UserInfo.get().community + "");
            }

            @Override
            public void onFailure(String msg) {
            }
        });
    }

    private void initViewPager() {
        viewList = new ArrayList<>();

        for (int i = 0; i < res.length; i++) {
            ImageView imageView = new ImageView(LoadingActivity.this);
            imageView.setBackgroundResource(res[i]);
            viewList.add(imageView);
        }
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 3) {
                    buttonExperience.setVisibility(View.VISIBLE);
                } else {
                    buttonExperience.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        buttonExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSession.setFirst(false);
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        into();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    private void into() {
        initAd();
        // 设置动画效果是alpha，在anim目录下的alpha.xml文件中定义动画效果
        animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        // 给view设置动画效果
        view.startAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            // 这里监听动画结束的动作，在动画结束的时候开启一个线程，这个线程中绑定一个Handler,并
            // 在这个Handler中调用goHome方法，而通过postDelayed方法使这个方法延迟500毫秒执行，达到
            // 达到持续显示第一屏500毫秒的效果
            @Override
            public void onAnimationEnd(Animation arg0) {
                if (mSession.isFirst()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initViewPager();
                        }
                    }, 1000); //延时1000毫秒
                } else {
                   // initAd();
                    final Bitmap bitmapAd = BitmapUtils.getBitmap("yiqu", getResources().getString(R.string.ad_launch) + ".jpg");
                    if (bitmapAd != null) {
                        ImageloaderUtil.displayImage(mContext,mSession.getUrlAdImg(),imgAdvertisement);
                        imgAdvertisement.setImageBitmap(bitmapAd);
                        if (!TextUtils.isEmpty(mSession.getUrlAd())) {
                            imgAdvertisement.setEnabled(true);
                            imgAdvertisement.setClickable(true);
                            imgAdvertisement.setTag(mSession.getUrlAd());
                            imgAdvertisement.setOnClickListener(LoadingActivity.this);
                        }
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (bitmapAd != null) {
                                aDCountDown();
                            } else {
                                finish();
                            }
                        }
                    }, 1000);

                }
            }
        });
    }

    //获取启动页广告
    private void initAd() {
       final List<AdInfo> list=new ArrayList<>();
        Log.e("--请求广告也","  进入广告请求");
        GoodsApi.get().GetAvd(mContext, "6", new ApiConfig.ApiRequestListener<BannerInfo>() {
            @Override
            public void onSuccess(String msg, BannerInfo data) {
               // Log.e("--请求广告也","  "+data.adList);
                if (data == null || data.adList == null || data.adList.size() == 0) {
                    BitmapUtils.deleteBitmap( "yiqu", getResources().getString(R.string.ad_launch)+ ".jpg");
                    return;
                }
                infos.clear();
                infos.addAll(data.adList);
                    list.addAll(data.adList);
                    mSession.setUrlAd(data.adList.get(0).adver_url); //更新URL
                    if (!data.adList.get(0).adver_img.equals(mSession.getUrlAdImg())) { //广告图片更新
                        mSession.setUrlAdImg(data.adList.get(0).adver_img);
                        ImageLoader.getInstance().loadImage(mSession.getUrlAdImg(), new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                                final Bitmap bitmapAd = BitmapUtils.getBitmap("yiqu", getResources().getString(R.string.ad_launch) + ".jpg");
                                BitmapUtils.saveBitmap(loadedImage, "yiqu", getResources().getString(R.string.ad_launch));
                                super.onLoadingComplete(imageUri, view, loadedImage);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                super.onLoadingFailed(imageUri, view, failReason);
                            }
                        });
                    }
            }

            @Override
            public void onFailure(String msg) {
                Log.e("--请求广告也","  "+msg);
                BitmapUtils.deleteBitmap( "yiqu", getResources().getString(R.string.ad_launch)+ ".jpg");
            }
        });
           /* final Bitmap bitmapAd = BitmapUtils.getBitmap("yiqu", getResources().getString(R.string.ad_launch) + ".jpg");
            if (bitmapAd != null) {
                ImageloaderUtil.displayImage(mContext,mSession.getUrlAdImg(),imgAdvertisement);
                //imgAdvertisement.setImageBitmap(bitmapAd);
                if (!TextUtils.isEmpty(mSession.getUrlAd())) {
                    imgAdvertisement.setEnabled(true);
                    imgAdvertisement.setClickable(true);
                    imgAdvertisement.setTag(mSession.getUrlAd());
                    imgAdvertisement.setOnClickListener(LoadingActivity.this);
                }
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (bitmapAd != null) {
                        aDCountDown();
                    } else {
                        finish();
                    }
                }
            }, 1000);*/
    }

    //启动页倒计时
    private void aDCountDown() {
        animation = AnimationUtils.loadAnimation(mContext, R.anim.popup_window_out);
        // 给view设置动画效果
        imgLaunch.startAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //TODO onAnimationEnd执行两次，尚不清楚原因
                imgLaunch.clearAnimation();
                imgLaunch.setVisibility(View.GONE);
                if (!isCountTime) {
                    isCountTime = true;
                    mDownTimer = new CountDownTimer(3106, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tvCountDown.setText(millisUntilFinished / 1000 + "");
                        }

                        @Override
                        public void onFinish() {
                            finish();
                        }
                    };
                    mDownTimer.start();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void finish() {

        if (!isFinishing()) {
            if (mDownTimer != null) {
                mDownTimer.cancel(); //取消倒计时器
            }

            //无需登录，先进入主页
            if (mSession != null && mSession.isFirst() == false) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("currentItem", 0);
                startActivity(intent);
            }
        }

        overridePendingTransition(R.anim.activity_int, R.anim.activity_out);
        super.finish();
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));


            return viewList.get(position);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlCountDown: {
                finish();
                break;
            }
            case R.id.imgAdvertisement: {
//                MobclickAgent.onEvent(mContext, "initAdvertisement"); //友盟自定义事件统计——启动页广告
                if (infos.size() > 0 && infos.get(0) != null) {
                    MobclickAgent.onEvent(mContext, infos.get(0).adver_alias); //友盟自定义事件统计——启动页广告
                }
                finish();
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("http_url", (String) v.getTag());
                startActivity(intent);
                break;
            }
        }
    }
}
