package com.quanmai.yiqu.ui.common;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.PinchImageView;

/**
 * 查看图片大图
 */
public class PicViewActivity extends Activity {
    private static final long ANIM_TIME = 400; //动画时长

    private PinchImageView pic;
    private ImageView imgBg;
    private String urlImg;  //图片链接

    private ObjectAnimator mBackgroundAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_view);
        pic = (PinchImageView) findViewById(R.id.pic);
        imgBg = (ImageView) findViewById(R.id.imgBg);

        //获取参数
        urlImg = getIntent().getStringExtra("urlImg");

        //view初始化
        pic.post(new Runnable() {
            @Override
            public void run() {
                //背景动画
                mBackgroundAnimator = ObjectAnimator.ofFloat(imgBg, "alpha", 0f, 1f);
                mBackgroundAnimator.setDuration(ANIM_TIME);
                mBackgroundAnimator.start();

                //加载图片
                ImageloaderUtil.displayImage(PicViewActivity.this,urlImg,pic,new SimpleImageLoadingListener(){
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        mBackgroundAnimator = ObjectAnimator.ofFloat(pic, "alpha", 0f, 1f);
                        mBackgroundAnimator.setDuration(ANIM_TIME);
                        mBackgroundAnimator.start();
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);
                        mBackgroundAnimator = ObjectAnimator.ofFloat(pic, "alpha", 0f, 1f);
                        mBackgroundAnimator.setDuration(ANIM_TIME);
                        mBackgroundAnimator.start();
                    }
                });
            }
        });

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pic.playSoundEffect(SoundEffectConstants.CLICK);
                finish();
            }
        });
    }
}
