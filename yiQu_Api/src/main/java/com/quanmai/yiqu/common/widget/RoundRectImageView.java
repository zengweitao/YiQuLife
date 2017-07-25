package com.quanmai.yiqu.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.quanmai.yiqu.R;

/**
 * Created by yin on 16/3/17.
 */
public class RoundRectImageView extends ImageView{

    int mRadius;//圆角

    public RoundRectImageView(Context context) {
        this(context,null);
    }

    public RoundRectImageView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public RoundRectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.RoundRectImageView,defStyleAttr,0);
        mRadius = a.getDimensionPixelSize(R.styleable.RoundRectImageView_roundRectRadius,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,getResources().getDisplayMetrics()));
        a.recycle();

    }

    @Override
    public void draw(Canvas canvas) {
        //实例化一个和ImageView一样大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);

        //实例化一个canvas，这个canvas对应的内存为上面的bitmap
        Canvas canvas2 = new Canvas(bitmap);
        if (bitmap.isRecycled()) {
            bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                    Bitmap.Config.ARGB_8888);
            canvas2 = new Canvas(bitmap);
        }

        //将imageView自己绘制到canvas2上，这个导致bitmap里面存放了imageView
        super.draw(canvas2);

        //利用canvas画一个圆角矩形，这个会修改bitmap的数据
        drawRoundAngle(canvas2);

        //将裁剪好的bitmap绘制到系统当前canvas上，这样裁剪好的imageview就能显示到屏幕上
        Paint paint = new Paint();
        paint.setXfermode(null);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        bitmap.recycle();
    }

    private void drawRoundAngle(Canvas canvas2) {
        Paint maskPaint = new Paint();
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Path maskPath = new Path();
        maskPath.addRoundRect(new RectF(0.0F, 0.0F, getWidth(), getHeight()), mRadius, mRadius, Path.Direction.CW);

        //这是设置了填充模式，非常关键
        maskPath.setFillType(Path.FillType.INVERSE_WINDING);
        canvas2.drawPath(maskPath, maskPaint);
    }
}
