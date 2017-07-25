package com.quanmai.yiqu.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.quanmai.yiqu.R;

/**
 * Created by James on 2016/8/11.
 * 圆形/圆角ImageView
 */
public class CircularImageView extends ImageView {
    private int mType; //类型
    private static final int TYPE_CIRCLE = 0; //圆形
    private static final int TYPE_ROUND = 1; //圆角
    private static final int TYPE_ROUND_TOP = 2; //顶部圆角

    private Paint paint;
    private Paint paintBorder;
    private Bitmap mSrcBitmap; //资源位图
    private float mRadius; //圆角的弧度
    private int mWidth; //宽度
    private int mHeight; //高度

    private boolean isKeepRadio; //是否保持宽高比

    public CircularImageView(final Context context) {
        this(context, null);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView, defStyle, 0);
        mRadius = ta.getDimension(R.styleable.CircularImageView_circular_radius, 0);
        mType = ta.getInt(R.styleable.CircularImageView_circular_type, 0);
        isKeepRadio = ta.getBoolean(R.styleable.CircularImageView_circular_is_keep_radio, false);
        int srcResource = attrs.getAttributeResourceValue(
                "http://schemas.android.com/apk/res/android", "src", 0);
        if (srcResource != 0) {
            mSrcBitmap = BitmapFactory.decodeResource(getResources(), srcResource);
        }

        ta.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int width = canvas.getWidth() - getPaddingLeft() - getPaddingRight();
        int height = canvas.getHeight() - getPaddingTop() - getPaddingBottom();
        mSrcBitmap = drawableToBitmap(getDrawable());
        if (mSrcBitmap == null) {
            super.onDraw(canvas);
            return;
        }

        switch (mType) {
            case TYPE_CIRCLE: {
                Bitmap reSizeImage = reSizeImageC(mSrcBitmap, width, height);
                canvas.drawBitmap(createCircleImage(reSizeImage, width, height),
                        getPaddingLeft(), getPaddingTop(), null);
                break;
            }
            case TYPE_ROUND: {
                Bitmap reSizeImage = reSizeImage(mSrcBitmap, width, height);
                canvas.drawBitmap(createRoundImage(reSizeImage, width, height),
                        getPaddingLeft(), getPaddingTop(), null);
                break;
            }
            case TYPE_ROUND_TOP: {
                Bitmap reSizeImage = reSizeImage(mSrcBitmap, width, height);
                canvas.drawBitmap(createTopRoundImage(reSizeImage, width, height),
                        getPaddingLeft(), getPaddingTop(), null);
                break;
            }

        }
    }

    /**
     * 画圆角
     *
     * @param source
     * @param width
     * @param height
     * @return
     */
    private Bitmap createRoundImage(Bitmap source, int width, int height) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        // 核心代码取两个图片的交集部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 画顶部圆角
     *
     * @param source
     * @param width
     * @param height
     * @return
     */
    private Bitmap createTopRoundImage(Bitmap source, int width, int height) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawRoundRect(new RectF(0, 0, width, mRadius * 2), mRadius, mRadius, paint);
        canvas.drawRect(new RectF(0, mRadius, width, height), paint);
        // 核心代码取两个图片的交集部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }


    /**
     * 画圆
     *
     * @param source
     * @param width
     * @param height
     * @return
     */
    private Bitmap createCircleImage(Bitmap source, int width, int height) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(width / 2, height / 2, Math.min(width, height) / 2,
                paint);
        // 核心代码取两个图片的交集部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, (width - source.getWidth()) / 2,
                (height - source.getHeight()) / 2, paint);
        return target;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (getDrawable() == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        } else {
            mSrcBitmap = drawableToBitmap(getDrawable());
        }

        //测量宽度
        if (widthSpecMode == MeasureSpec.EXACTLY) {
            mWidth = widthSpecSize;
        } else {
            //显示图片原始大小（宽度）
            mWidth = mSrcBitmap.getWidth() + getPaddingLeft() + getPaddingRight(); //UNSPECIFIED
            if (widthSpecMode == MeasureSpec.AT_MOST) { //wrap_content,子控件不能超过父控件,此时我们取传递过来的大小和图片本身大小的小者
                mWidth = Math.min(widthSpecSize, mWidth);
            }
        }

        //测量高度
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            mHeight = heightSpecSize;
        } else {
            //显示图片原始大小（高度）
            mHeight = mSrcBitmap.getHeight() + getPaddingTop() + getPaddingBottom();
            //是否保持宽高比（宽度不变，高度缩放）
            if (isKeepRadio) {
                mHeight = mWidth * mSrcBitmap.getHeight() / mSrcBitmap.getWidth();
            }
            if (heightSpecMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(heightSpecSize, mHeight);
            }
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            if (mSrcBitmap != null) {
                return mSrcBitmap;
            } else {
                return null;
            }
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 重设Bitmap的宽高
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private Bitmap reSizeImage(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算出缩放比
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 矩阵缩放bitmap
        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 重设Bitmap的宽高
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private Bitmap reSizeImageC(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int x = (newWidth - width) / 2;
        int y = (newHeight - height) / 2;
        if (x > 0 && y > 0) {
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, null, true);
        }

        float scale = 1;

        if (width > height) {
            // 按照宽度进行等比缩放
            scale = ((float) newWidth) / width;

        } else {
            // 按照高度进行等比缩放
            // 计算出缩放比
            scale = ((float) newHeight) / height;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
