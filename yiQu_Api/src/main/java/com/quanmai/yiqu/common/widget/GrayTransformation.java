package com.quanmai.yiqu.common.widget;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.quanmai.yiqu.common.util.BitmapUtils;

/**
 * Created by 95138 on 2016/5/31.
 */
public class GrayTransformation extends BitmapTransformation {

    public GrayTransformation(Context context) {
        super(context);
    }

    public GrayTransformation(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = BitmapUtils.toGrayscale(toTransform);
        bitmap = BitmapUtils.getTransparentBitmap(bitmap,50);

        return bitmap;
    }

    @Override
    public String getId() {
        return "com.quanmai.yiqu.common.widget";
    }
}
