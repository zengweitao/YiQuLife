package com.quanmai.yiqu.common.widget;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by yin on 16/3/31.
 */
public class CameraProtectActivity extends Activity {
    private String mImageFilePath;
    private ArrayList<String> imgList = new ArrayList<>();
    public static final String IMAGEFILEPATH = "ImageFilePath";
    public final static String IMAGE_PATH = "image_path";
    static Activity mContext;
    public final static int GET_IMAGE_REQ = 5000;
    private static Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgList.addAll(getIntent().getStringArrayListExtra("imgList")) ;
        //判断 activity被销毁后 有没有数据被保存下来
        if (savedInstanceState != null) {

            mImageFilePath = savedInstanceState.getString(IMAGEFILEPATH);
            if (imgList!=null){
                imgList.clear();
                imgList.addAll(savedInstanceState.getStringArrayList("list"));
            }
//            Log.e("mark","list---"+imgList.size());
//            Log.e("mark", "123---savedInstanceState:  " + mImageFilePath);

            File mFile = new File(IMAGEFILEPATH);
            if (mFile.exists()) {
                Log.e("mark", "图片拍摄成功"+mImageFilePath);
                Intent rsl = new Intent();
                rsl.putExtra(IMAGE_PATH, mImageFilePath);
                rsl.putStringArrayListExtra("imgList",imgList);
                setResult(Activity.RESULT_OK, rsl);
//                Log.e("mark", "123---savedInstanceState---" + "图片拍摄成功");
                finish();
            } else {
//                Log.e("mark", "123---savedInstanceState----" + "图片拍摄失败");
            }
        }

        mContext = this;
        applicationContext = getApplicationContext();
        if (savedInstanceState == null) {
            initialUI();
        }

    }

    public void initialUI() {
        //根据时间生成 后缀为  .jpg 的图片
        long ts = System.currentTimeMillis();
        mImageFilePath = getCameraPath() + ts + ".jpg";
        File out = new File(mImageFilePath);
        showCamera(out);

    }

    private void showCamera(File out) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); // set
        startActivityForResult(intent, GET_IMAGE_REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (GET_IMAGE_REQ == requestCode && resultCode == Activity.RESULT_OK) {
            Log.e("--照片名 ","== "+mImageFilePath);
            Intent rsl = new Intent();
            rsl.putExtra(IMAGE_PATH, mImageFilePath);
            rsl.putStringArrayListExtra("imgList",imgList);
            mContext.setResult(Activity.RESULT_OK, rsl);
            mContext.finish();

        } else {
            mContext.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ImageFilePath", mImageFilePath + "");
        outState.putStringArrayList("list",imgList);
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    public static String getCameraPath() {
        String filePath = getImageRootPath() + "/camera/";
        File file = new File(filePath);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        file = null;
        return filePath;
    }

    public static String getImageRootPath() {
        String filePath = getAppRootPath() + "/image";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = null;
        return filePath;
    }

    public static String getAppRootPath() {
        String filePath = "/a";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            filePath = Environment.getExternalStorageDirectory() + filePath;
        } else {
            filePath = applicationContext.getCacheDir() + filePath;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = null;
        File nomedia = new File(filePath + "/.nomedia");
        if (!nomedia.exists())
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
            }
        return filePath;
    }

}
