package com.quanmai.yiqu.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.quanmai.yiqu.common.util.ImageloaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yin on 16/4/6.
 */
public class HomePageImageHolder implements Holder<String> {
    private ImageView imageView;
    private List<String> list;
    public static int count=0;
    public HomePageImageHolder(List<String> urls,Context context){
        list = new ArrayList<>();
        list.addAll(urls);
        ImageloaderUtil.initImageLoader(context);
    }
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position,final String data) {
        ImageloaderUtil.displayImage(context,data,imageView);
    }
}
