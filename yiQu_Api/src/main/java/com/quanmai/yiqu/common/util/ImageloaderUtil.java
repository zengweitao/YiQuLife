package com.quanmai.yiqu.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.UserInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ImageloaderUtil {

    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "yiqu");
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration
                .Builder(context)
                .threadPoolSize(3) //线程池内加载的数量
                .discCache(new UnlimitedDiskCache(cacheDir)) //自定义缓存路径
                .defaultDisplayImageOptions(getDisplayImageOptions())
                .memoryCache(new WeakMemoryCache());
        ImageLoaderConfiguration config = builder.build();
        ImageLoader.getInstance().init(config);
    }

    @SuppressWarnings("deprecation")
    public static DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.default_header)
//				.showImageForEmptyUri(R.drawable.default_header)
//				.showImageOnFail(R.drawable.default_header)
                .resetViewBeforeLoading()
                .showImageOnFail(R.drawable.default_header)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100, true, true, true))
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        return defaultOptions;
    }



    /**
     *可以设置加载各阶段的预显示图片，进行图片本地缓存
     * @param OnLoading 加载中显示的图片
     * @param ForEmptyUri 图片的uri为空时显示的图片
     * @param OnFail 加载失败时显示的图片
     * @return
     */
    public static DisplayImageOptions getDisplayImageOptions(int OnLoading,int ForEmptyUri,int OnFail) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(OnLoading)
				.showImageForEmptyUri(ForEmptyUri)
				.showImageOnFail(OnFail)
                .resetViewBeforeLoading()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100, true, true, true))
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        return defaultOptions;
    }

    /**
     *
     * @param OnLoading
     * @param ForEmptyUri
     * @param OnFail
     * @return
     */
    public static DisplayImageOptions getNotSdDisplayImageOptions(int OnLoading,int ForEmptyUri,int OnFail) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(OnLoading)
                .showImageForEmptyUri(ForEmptyUri)
                .showImageOnFail(OnFail)
                .resetViewBeforeLoading()
                .cacheInMemory(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100, true, true, true))
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        return defaultOptions;
    }

    @SuppressWarnings("deprecation")
    public static ArrayList<String> getGalleryPhotos(Activity act) {
        ArrayList<String> galleryList = new ArrayList<String>();
        try {
            final String[] columns = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;
            Cursor imagecursor = act.managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null,
                    orderBy);
            if (imagecursor != null && imagecursor.getCount() > 0) {
                while (imagecursor.moveToNext()) {
                    String item = new String();
                    int dataColumnIndex = imagecursor
                            .getColumnIndex(MediaStore.Images.Media.DATA);
                    item = imagecursor.getString(dataColumnIndex);
                    galleryList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(galleryList);
        return galleryList;
    }

    public static void displayImage(Context mContext, String uri, ImageView imageAware,DisplayImageOptions options) {
        if (uri == null) {
            return;
        }
        if (uri.indexOf("http") == -1) {
            uri = UserInfo.get().prefixQiNiu + uri;
        }
        ImageLoader.getInstance().displayImage(uri, imageAware,options);
    }

    public static void displayImage(Context mContext, String uri, ImageView imageAware) {
        if (uri == null) {
            return;
        }
        if (!uri.contains("http")) {
            uri = UserInfo.get().prefixQiNiu + uri;
        }
        ImageLoader.getInstance().displayImage(uri, imageAware);
    }

    public static void displayImage(Context mContext, String uri, ImageView imageAware, ImageLoadingListener listener) {
        if (uri == null) {
            return;
        }
        if (!uri.contains("http")) {
            uri =  UserInfo.get().prefixQiNiu + uri;
        }
        ImageLoader.getInstance().displayImage(uri, imageAware, listener);
    }

    public static Bitmap loadImage(Context mContext, String uri) {
        Bitmap bitmap = null;
        if (uri == null) {
            return null;
        }

        if (uri.indexOf("http") == -1) {
            uri =  UserInfo.get().prefixQiNiu + uri;
        }
        bitmap = ImageLoader.getInstance().loadImageSync(uri);

        return bitmap;
    }

    public static Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = null;
        try {
            int width = view.getWidth();
            int height = view.getHeight();
            if (width != 0 && height != 0) {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                view.layout(0, 0, width, height);
                view.setBackgroundColor(Color.WHITE);
                view.draw(canvas);
            }
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;

    }

    public static boolean saveImageToGallery(Context context, Bitmap bmp, boolean isPng) {
        if (bmp == null) {
            return false;
        }
        File appDir = new File(Environment.getExternalStorageDirectory(),
                context.getString(R.string.app_name));
        if (!appDir.exists()) {
            if (!appDir.mkdir()) {
                return false;
            }
        }
        String fileName;
        if (isPng) {
            fileName = System.currentTimeMillis() + ".png";
        } else {
            fileName = System.currentTimeMillis() + ".jpg";
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (isPng) {
                bmp.compress(CompressFormat.PNG, 100, fos);
            } else {
                bmp.compress(CompressFormat.JPEG, 100, fos);
            }
            bmp.recycle();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        context.sendBroadcast(
                new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(appDir)));
        return true;
    }

    public static void t(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
