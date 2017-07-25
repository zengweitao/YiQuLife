package com.quanmai.yiqu.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.quanmai.yiqu.common.util.ImageloaderUtil;

/**
 * Created by yin on 16/3/28.
 */
public class LocalImageHolderView implements Holder<String> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, final int position, String data) {
        ImageloaderUtil.displayImage(context, data, imageView);

    }

}
