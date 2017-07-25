package com.quanmai.yiqu.common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.quanmai.yiqu.api.vo.RecycleGarbageListInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbagesInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbagesMapInfo;
import com.quanmai.yiqu.base.App;
import com.quanmai.yiqu.base.CommonList;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpHost;
//import org.apache.http.HttpResponse;
//import org.apache.http.ParseException;
//import org.apache.http.util.EntityUtils;

@SuppressLint("DefaultLocale")
public class Utils {
    public static boolean sDebug = false;
    public static String sLogTag;

    private static final String TAG = "Utils";
    private static long lastClickTime;
    // UTF-8 encoding
    private static final String ENCODING_UTF8 = "UTF-8";

    private static WeakReference<Calendar> calendar;

    /**
     * <p>
     * Get UTF8 bytes from a string
     * </p>
     *
     * @param string String
     * @return UTF8 byte array, or null if failed to get UTF8 byte array
     */
    public static byte[] getUTF8Bytes(String string) {
        if (string == null)
            return new byte[0];

        try {
            return string.getBytes(ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            /*
             * If system doesn't support UTF-8, use another way
			 */
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);
                dos.writeUTF(string);
                byte[] jdata = bos.toByteArray();
                bos.close();
                dos.close();
                byte[] buff = new byte[jdata.length - 2];
                System.arraycopy(jdata, 2, buff, 0, buff.length);
                return buff;
            } catch (IOException ex) {
                return new byte[0];
            }
        }
    }

    /**
     * <p>
     * Get string in UTF-8 encoding
     * </p>
     *
     * @param b byte array
     * @return string in utf-8 encoding, or empty if the byte array is not
     * encoded with UTF-8
     */
    public static String getUTF8String(byte[] b) {
        if (b == null)
            return "";
        return getUTF8String(b, 0, b.length);
    }

    public static String getPrice(double price) {
        return String.format("%.2f元", price);
    }

    /**
     * <p>
     * Get string in UTF-8 encoding
     * </p>
     */
    public static String getUTF8String(byte[] b, int start, int length) {
        if (b == null) {
            return "";
        } else {
            try {
                return new String(b, start, length, ENCODING_UTF8);
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
    }

    /**
     * <p>
     * Parse int value from string
     * </p>
     *
     * @param value string
     * @return int value
     */
    public static int getInt(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }

        try {
            return Integer.parseInt(value.trim(), 10);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String getPrice1(double price) {
        return String.format("￥%.2f", price);
    }

    public static String getPrice2(double price) {
        return String.format("%.2f", price);
    }

    public static String getPrice3(String price) {
        return String.format("￥%.2f", Double.parseDouble(price));
    }

    public static String getPrice4(String price) {
        return String.format("%.2f", Double.parseDouble(price));
    }

    public static String getPrice5(String price) {
        return String.format("%.1f", Double.parseDouble(price));
    }

    /**
     * <p>
     * Parse float value from string
     * </p>
     *
     * @param value string
     * @return float value
     */
    public static float getFloat(String value) {
        if (value == null)
            return 0f;

        try {
            return Float.parseFloat(value.trim());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    /**
     * <p>
     * Parse long value from string
     * </p>
     *
     * @param value string
     * @return long value
     */
    public static long getLong(String value) {
        if (value == null)
            return 0L;

        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public static void V(String msg) {
        if (sDebug) {
            Log.v(sLogTag, msg);
        }
    }

    public static void V(String msg, Throwable e) {
        if (sDebug) {
            Log.v(sLogTag, msg, e);
        }
    }

    public static void D(String msg) {
        if (sDebug) {
            Log.d(sLogTag, msg);
        }
    }

    public static void D(String msg, Throwable e) {
        if (sDebug) {
            Log.d(sLogTag, msg, e);
        }
    }

    public static void I(String msg) {
        if (sDebug) {
            Log.i(sLogTag, msg);
        }
    }

    public static void I(String msg, Throwable e) {
        if (sDebug) {
            Log.i(sLogTag, msg, e);
        }
    }

    public static void W(String msg) {
        if (sDebug) {
            Log.w(sLogTag, msg);
        }
    }

    public static void W(String msg, Throwable e) {
        if (sDebug) {
            Log.w(sLogTag, msg, e);
        }
    }

    public static void E(String msg) {
        if (sDebug) {
            Log.e(sLogTag, msg);
        }
    }

    public static void E(String msg, Throwable e) {
        if (sDebug) {
            Log.e(sLogTag, msg, e);
        }
    }

    public static void LogDialog(Context context, String string) {
        if (sDebug) {
            new AlertDialog.Builder(context)
                    .setTitle("API信息")
                    // 设置标题
                    .setMessage(string)
                    // 设置提示消息
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {// 设置确定的按键
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // do something
                                    dialog.cancel();
                                }
                            }).setCancelable(false)// 设置按返回键是否响应返回，这是是不响应
                    .show();// 显示
        }
    }

    // public static String formatDate(long time) {
    // if (calendar == null || calendar.get() == null) {
    // calendar = new WeakReference<Calendar>(Calendar.getInstance());
    // }
    // Calendar target = calendar.get();
    // target.setTimeInMillis(time);
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // return sdf.format(target.getTime());
    // }

    // public static String formatDate(long time) {
    // return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault())
    // .format(new Date(time * 1000));
    // }

    public static String formatTime(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(date);
    }

    /**
     * 格式化时间（Format：yyyy-MM-dd HH:mm）
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(new Date(time * 1000));
    }

    public static String formatDate(long time) {
        return new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
                .format(new Date(time * 1000));
    }

    public static String formatDate(String time) {
        return new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
                .format(new Date(Long.valueOf(time) * 1000));
    }

    public static String formatTime(String time) {
        return new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                .format(new Date(Long.valueOf(time) * 1000));
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTodayDate() {
        if (calendar == null || calendar.get() == null) {
            calendar = new WeakReference<Calendar>(Calendar.getInstance());
        }
        Calendar today = calendar.get();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(today.getTime());
    }

    public static StringBuffer getMonth(long TimeInMillis) {
        StringBuffer strBuff = new StringBuffer(getFormatTime(TimeInMillis)
                .split("-")[1]);
        return strBuff;
    }

    public static String getFormatTime(long TimeInMillis) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date(TimeInMillis * 1000));
    }

//	@SuppressLint("SimpleDateFormat")
//	public static String convertGMTToLoacale(String gmt) {
//		String cc = gmt.substring(0, 19) + gmt.substring(33, 38);
//		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy",
//				new Locale("English"));
//		try {
//			Date date = null;
//			try {
//				date = sdf.parse(cc);
//			} catch (java.text.ParseException e) {
//				e.printStackTrace();
//			}
//			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
//			String result = dateformat.format(date);
//			return result;
//		} catch (ParseException e) {
//		}
//		return "";
//	}

    /**
     * @param datestr 日期字符串
     * @param day     相对天数，为正数表示之后，为负数表示之前
     * @return 指定日期字符串n天之前或者之后的日期
     */
    @SuppressLint("SimpleDateFormat")
    public static java.util.Date getBeforeAfterDate(String datestr, int day) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date olddate = null;
        df.setLenient(false);
        try {
            olddate = new java.util.Date(df.parse(datestr).getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("日期转换错误");
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(olddate);

        int Year = cal.get(Calendar.YEAR);
        int Month = cal.get(Calendar.MONTH);
        int Day = cal.get(Calendar.DAY_OF_MONTH);

        int NewDay = Day + day;

        cal.set(Calendar.YEAR, Year);
        cal.set(Calendar.MONTH, Month);
        cal.set(Calendar.DAY_OF_MONTH, NewDay);

        return new java.util.Date(cal.getTimeInMillis());
    }

    /**
     * 返回网络是否是可用的
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.w(TAG, "couldn't get connectivity manager");
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0, length = info.length; i < length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns whether the network is roaming
     */
    public static boolean isNetworkRoaming(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            // Log.w(Constants.TAG, "couldn't get connectivity manager");
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null
                    && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            } else {
            }
        }
        return false;
    }

    // /**
    // * Get the decrypted HTTP response body<br>
    // *
    // * @return if response is empty or some error occured when decrypt process
    // * will return EMPTY string
    // */
    // public static String getDecryptedResponseBody(HttpEntity entity) {
    // byte[] response = SecurityUtil.decryptHttpEntity(entity);
    // return Utils.getUTF8String(response);
    // }
    //
    // private static GoogleAnalyticsTracker mTracker;
    //
    // public static void trackEvent(Context context, String... paras) {
    // if (paras == null || paras.length != 3) {
    // return;
    // }
    // if (mTracker == null) {
    // mTracker = GoogleAnalyticsTracker.getInstance();
    // mTracker.setProductVersion("GfanMobile",
    // String.valueOf(Session.get(context).getVersionCode()));
    // mTracker.start(com.mappn.gfan.Constants.GOOGLE_UID, context);
    // }
    // mTracker.trackEvent(paras[0], paras[0] + "_" + paras[1] + paras[2], "",
    // 0);
    // Collector.setAppClickCount(String.format(com.mappn.gfan.Constants.STATISTICS_FORMAT,
    // paras[0], paras[1], paras[2]));
    // }

    // /**
    // * Show toast information
    // *
    // * @param context
    // * application context
    // * @param text
    // * the information which you want to show
    // * @return show toast dialog
    // */
    // public static void makeEventToast(Context context, String text, boolean
    // isLongToast) {
    //
    // Toast toast = null;
    // if (isLongToast) {
    // toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
    // } else {
    // toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    // }
    // View v = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
    // TextView textView = (TextView) v.findViewById(R.id.text);
    // textView.setText(text);
    // toast.setView(v);
    // toast.show();
    // }

    // /**
    // * 从商家版Zip包中获取加密文件输入流
    // */
    // public static DecryptStream getDecryptStream(File file, String entryName)
    // {
    // try {
    // ZipFile zipPackage = new ZipFile(file);
    // ZipEntry entry = zipPackage.getEntry(entryName);
    // if (entry == null) {
    // return null;
    // }
    // return new DecryptStream(zipPackage.getInputStream(entry));
    // } catch (IOException e) {
    // }
    // return null;
    // }

    // /**
    // * 从商家版Zip包中获取普通文件输入流
    // */
    // public static InputStream getNormalStream(File file, String entryName) {
    // try {
    // ZipFile zipPackage = new ZipFile(file);
    // ZipEntry entry = zipPackage.getEntry(entryName);
    // if (entry == null) {
    // return null;
    // }
    // return zipPackage.getInputStream(entry);
    // } catch (IOException e) {
    // }
    // return null;
    // }

    // /**
    // * 获取商家版加密后的APK文件，并拷贝到SD卡上（/sdcard/gfan/apk）
    // * @param root 商家版应用包文件
    // * @param entryName APK文件
    // * @return 拷贝后的文件
    // */
    // public static File getEncryptApk(File root, String entryName) {
    //
    // InputStream in = null;
    // FileOutputStream fos = null;
    // File outputFile = null;
    // try {
    // outputFile = new File(Environment.getExternalStorageDirectory() +
    // "/gfan/apk",
    // entryName);
    // fos = new FileOutputStream(outputFile);
    // in = getDecryptStream(root, entryName);
    // if (in == null) {
    // return null;
    // }
    // copyFile(in, fos);
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // return outputFile;
    // }

    // /**
    // * 获取商家版APK对应的ICON文件，并拷贝到SD卡上（/sdcard/gfan/.cache）
    // * @param root 商家版应用包文件
    // * @param entryName ICON文件
    // * @return 拷贝后的文件
    // */
    // public static File getApkIcon(File root, String entryName) {
    // InputStream in = null;
    // FileOutputStream fos = null;
    // File outputFile = null;
    // try {
    // outputFile = new File(Environment.getExternalStorageDirectory() +
    // "/gfan/.cache",
    // entryName);
    // fos = new FileOutputStream(outputFile);
    // in = getNormalStream(root, entryName);
    // if (in == null) {
    // return null;
    // }
    // copyFile(in, fos);
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // return outputFile;
    // }

    /**
     * 文件拷贝工具类
     *
     * @param dst 目标文件
     * @throws IOException
     */
    public static void copyFile(InputStream in, FileOutputStream dst)
            throws IOException {
        byte[] buffer = new byte[8192];
        int len = 0;
        while ((len = in.read(buffer)) > 0) {
            dst.write(buffer, 0, len);
        }
        in.close();
        dst.close();
    }

//	/**
//	 * 解析HTTP String Entity
//	 * 
//	 * @param response
//	 *            HTTP Response
//	 * @return 市场API返回的消息(String)
//	 */
//	public static String getStringResponse(HttpResponse response) {
//		HttpEntity entity = response.getEntity();
//		try {
//			return entity == null ? null : EntityUtils.toString(response
//					.getEntity());
//		} catch (ParseException e) {
//			D("getStringResponse meet ParseException", e);
//		} catch (IOException e) {
//			D("getStringResponse meet IOException", e);
//		}
//		return null;
//	}

    // /**
    // * 解析HTTP InputStream Entity
    // *
    // * @param response
    // * HTTP Response
    // * @return API返回的消息(InputStream)
    // */
    // public static InputStream getInputStreamResponse(HttpResponse response) {
    // HttpEntity entity = response.getEntity();
    // try {
    // if (entity == null)
    // return null;
    // return AndroidHttpClient.getUngzippedContent(entity);
    // } catch (IllegalStateException e) {
    // D("getInputStreamResponse meet IllegalStateException", e);
    // } catch (IOException e) {
    // D("getInputStreamResponse meet IOException", e);
    // }
    // return null;
    // }

    /**
     * 界面切换动画
     *
     * @return
     */
    public static LayoutAnimationController getLayoutAnimation() {
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(100);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        return controller;
    }

    /**
     * 比较两个文件的签名是否一致
     */
    public static boolean compareFileWithSignature(String path1, String path2) {

        long start = System.currentTimeMillis();
        if (TextUtils.isEmpty(path1) || TextUtils.isEmpty(path2)) {
            return false;
        }

        String signature1 = getFileSignatureMd5(path1);
        String signature2 = getFileSignatureMd5(path2);

        V("compareFileWithSignature total time is "
                + (System.currentTimeMillis() - start));
        if (!TextUtils.isEmpty(signature1) && signature1.equals(signature2)) {
            return true;
        }
        return false;
    }

    /**
     * 获取应用签名MD5
     */
    public static String getFileSignatureMd5(String targetFile) {

        try {
            JarFile jarFile = new JarFile(targetFile);
            // 取RSA公钥
            JarEntry jarEntry = jarFile.getJarEntry("AndroidManifest.xml");

            if (jarEntry != null) {
                InputStream is = jarFile.getInputStream(jarEntry);
                byte[] buffer = new byte[8192];
                while (is.read(buffer) > 0) {
                    // do nothing
                }
                is.close();
                Certificate[] certs = jarEntry == null ? null : jarEntry
                        .getCertificates();
                if (certs != null && certs.length > 0) {
                    String rsaPublicKey = String.valueOf(certs[0]
                            .getPublicKey());
                    return getMD5(rsaPublicKey);
                }
            }
        } catch (IOException e) {
            W("occur IOException when get file signature", e);
        }
        return "";
    }


    /**
     * Get Base64 encode String
     *
     * @param string
     * @return
     */
    public static String getEncodeBase64(String string) {

        byte[] bytesResult = Base64.encode(string.getBytes(), Base64.DEFAULT);

        return new String(bytesResult);
    }

    /**
     * Get Base64 decode String
     *
     * @param string
     * @return
     */
    public static String getDecodeBase64(String string) {
        String strResult = "";
        try {
            byte[] byteArray = Base64.decode(string, Base64.DEFAULT);
            strResult = new String(byteArray);
            return strResult;
        } catch (Exception e) {
//            e.printStackTrace();
            return strResult;
        }
    }

    /**
     * Get MD5 Code
     */
    public static String getMD5(String text) {
        try {
            byte[] byteArray = text.getBytes("utf8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(byteArray, 0, byteArray.length);
            return convertToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Convert byte array to Hex string
     */
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * Check whether the SD card is readable
     */
    public static boolean isSdcardReadable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)
                || Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Check whether the SD card is writable
     */
    public static boolean isSdcardWritable() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    // /** 显示自定义Toast提示(来自res) **/
    // public static void showCustomToast(Context context,int resId) {
    // View toastRoot = LayoutInflater.from(context).inflate(
    // R.layout.common_toast, null);
    // ((HandyTextView) toastRoot.findViewById(R.id.toast_text))
    // .setText(context.getString(resId));
    // Toast toast = new Toast(context);
    // toast.setGravity(Gravity.CENTER, 0, 0);
    // toast.setDuration(Toast.LENGTH_SHORT);
    // toast.setView(toastRoot);
    // toast.show();
    // }

    /**
     * 显示自定义Toast提示(来自res)
     **/
    public static void showCustomToast(Context context, int resId) {
        Toast.makeText(App.getInstance(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showCustomToast(Context context, String text) {
        Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    // /** 显示自定义Toast提示(来自String) **/
    // public static void showCustomToast(Context context,String text) {
    // View toastRoot = LayoutInflater.from(context).inflate(
    // R.layout.common_toast, null);
    // ((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
    // Toast toast = new Toast(context);
    // toast.setDuration(Toast.LENGTH_SHORT);
    // toast.setView(toastRoot);
    // toast.show();
    // }

    /**
     * 解析二维码地址
     */
    public static HashMap<String, String> parserUri(Uri uri) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        String paras[] = uri.getQuery().split("&");
        for (String s : paras) {
            if (s.indexOf("=") != -1) {
                String[] item = s.split("=");
                parameters.put(item[0], item[1]);
            } else {
                return null;
            }
        }
        return parameters;
    }

//	/**
//	 * 检查默认Proxy
//	 */
//	@SuppressWarnings("deprecation")
//	public static HttpHost detectProxy(Context context) {
//		ConnectivityManager cm = (ConnectivityManager) context
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo ni = cm.getActiveNetworkInfo();
//		if (ni != null && ni.isAvailable()
//				&& ni.getType() == ConnectivityManager.TYPE_MOBILE) {
//			String proxyHost = android.net.Proxy.getDefaultHost();
//			int port = android.net.Proxy.getDefaultPort();
//			if (proxyHost != null) {
//				return new HttpHost(proxyHost, port, "http");
//			}
//		}
//		return null;
//	}

    /**
     * 卸载应用
     *
     * @param context 应用上下文
     * @param pkgName 包名
     */
    public static void uninstallApk(Context context, String pkgName) {
        Uri packageURI = Uri.parse("package:" + pkgName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(uninstallIntent);
    }

    public static void clearCache(Context context) {
        File file = Environment.getDownloadCacheDirectory();
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
        file = context.getCacheDir();
        files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }

    public static List<String> stringsToList(final String[] src) {
        if (src == null || src.length == 0) {
            return null;
        }
        final List<String> result = new ArrayList<String>();
        for (int i = 0; i < src.length; i++) {
            result.add(src[i]);
        }
        return result;
    }

    /**
     * 字符串中截取数字
     */
    public static String getNumbers(String start) {
        boolean isStart = false;
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < start.length(); i++) {
            String one = start.substring(i, i + 1);
            char ar = one.charAt(0);
            // 是数字
            if (ar >= '0' && ar <= '9') {
                str.append(ar);
                isStart = true;
            } else {
                if (isStart) {
                    if (i < start.length() - 1) {
                        str.append("-");
                    }
                }
                isStart = false;
            }
        }
        return str.toString();
    }

    /**
     * 倒计时计算时精度会丢失，造成最后2秒时都显示还剩1秒
     */
    public static long getCountDownTime(long time) {
        return time = time + (time * 2 / 1000);
    }

    final static long CountDownTime = getCountDownTime(60000);
    static long DownTime;

    /**
     * 检测之前的倒计时是否完成
     */
    public static void Detection(final Button button) {
        long difference = DownTime - System.currentTimeMillis();
        if (difference > 1000 && difference < CountDownTime) {
            new CountDownTimer(difference, 1000) {
                public void onTick(long millisUntilFinished) {
                    if (button != null) {
                        button.setBackgroundColor(0X00000000);
                        button.setClickable(false);

                        button.setText(millisUntilFinished / 1000 + "s后重新发送");
                        button.setTextColor(0xff757575);
                    }
                }

                public void onFinish() {
                    if (button != null) {
                        // button.setBackgroundResource(R.drawable.bg_btn_code);
                        button.setTextColor(0xffeb6100);
                        button.setClickable(true);
                        button.setText("获取验证码");
                    }
                }
            }.start();
        }
    }

    public static void wait(final Button button) {
        DownTime = System.currentTimeMillis() + CountDownTime;
        new CountDownTimer(CountDownTime, 1000) {
            public void onTick(long millisUntilFinished) {
                if (button != null) {
                    button.setClickable(false);
                    button.setText(millisUntilFinished / 1000 + "s后重新获取");
                }
            }

            public void onFinish() {
                if (button != null) {
                    button.setClickable(true);
                    button.setText("获取验证码");
                }
            }
        }.start();
    }


    /**
     * 判断在EditText输入的内容是否为空
     *
     * @param editText
     * @return false
     */
    public static boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    /**
     * 随机获取6位数的验证码
     *
     * @return (int)((Math.random() * 9 + 1) * 100000)
     */
    public static String randomVerifyCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    /**
     * 判断是不是有效合理的身份证号码
     *
     * @param str
     * @return pattern.matcher(str).matches()
     */
    public static boolean isIdCard(String str) {
        Pattern pattern = Pattern
                .compile("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断输入的是不是汉字
     *
     * @param str
     * @return pattern.matcher(str).matches()
     */
    public static boolean isChinese(String str) {
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]{0,}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断输入的是不是合理的手机号码
     *
     * @param str
     * @return pattern.matcher(str).matches()
     */
    public static boolean isTelNumber(String str) {
        Pattern pattern = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断输入的是不是数字
     *
     * @param str
     * @return pattern.matcher(str).matches()
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是不是有效合理的银行账号
     *
     * @param str
     * @return
     */
    public static boolean isBankAccountNumber(String str) {
        Pattern pattern = Pattern
                .compile("(^\\d{16}$)|(^\\d{18}$)|(^\\d{19}$)");
        return pattern.matcher(str).matches();
    }

    public static boolean isRightPwd(String str) {
        Pattern pattern = Pattern.compile("^(?![^a-zA-Z]+$)(?!\\D+$).{8,15}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 获取用户配置
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return value
     */
    public static boolean getConfig(Context context, String key,
                                    boolean defaultValue) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        boolean value = sp.getBoolean(key, defaultValue);
        return value;
    }

    /**
     * url 编码
     *
     * @param paramString 需要编码的字符串
     */
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            Log.v(sLogTag, "toURLEncoded error:" + paramString);
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            Log.v(sLogTag, "toURLEncoded error:" + paramString, localException);
        }

        return "";
    }


    /**
     * 保存用户配置
     *
     * @param context
     * @param key
     * @param value
     * @return editor.commit()
     */
    public static boolean saveConfig(Context context, String key, boolean value) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        return editor.commit();// 提交
    }

    /**
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 设置子类ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, listView);
            item.measure(0, 0);
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    // public static void loadHtml(WebView v) {
    // v.loadUrl(Qurl.update_rule);
    // }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {

        }
        return versionName;
    }

    public static boolean isPassWord(String password) {
        Pattern p = Pattern.compile("^\\w{6,}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static boolean isMobileNO(String mobiles) {

        String telRegex = "[1][3587]\\d{9}";// "[1]"代表第1位为数字1，"[3587]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);

    }

    /**
     * 把单个或多个view设置成visible
     *
     * @param view
     */
    public static void setVisible(View... view) {
        for (int i = 0; i < view.length; i++) {
            view[i].setVisibility(View.VISIBLE);
        }
    }

    /**
     * 把单个或多个view设置成invisible
     *
     * @param view
     */
    public static void setInvisible(View... view) {
        for (int i = 0; i < view.length; i++) {
            view[i].setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 把单个或多个view设置成gone
     *
     * @param view
     */
    public static void setGone(View... view) {
        for (int i = 0; i < view.length; i++) {
            view[i].setVisibility(View.GONE);
        }
    }

    /**
     * 获取图片旋转角度
     */
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.d(TAG, "cannot read exif" + ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    /**
     * 将某张图片按照一定角度旋转
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        Matrix m = new Matrix();
        m.postRotate(degree);
        returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                bm.getHeight(), m, true);
        return returnBm;
    }


    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    //将map转为json字符串
    public static String toJson(Map<RecycleGarbagesInfo, Double> map, CommonList<RecycleGarbageListInfo> mData) {
        String str = "";
        for (Map.Entry<RecycleGarbagesInfo, Double> entry : map.entrySet()) {
            int point = 0;
            String url = "";
            for (int i = 0; i < mData.size(); i++) {
                CommonList<RecycleGarbagesInfo> list = mData.get(i).getGarbageList();
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).equals(entry.getKey())) {
                        point = Integer.parseInt(list.get(j).point);
                        Log.e("--point值","== "+point);
                        if (!TextUtils.isEmpty(list.get(j).pic)) {
                            url = list.get(j).pic;
                        }
                    }
                }
            }

            str = str + "{" + "garbage:\"" + entry.getKey().getGarbage() + "\"," + "quantity:\"" + entry.getValue() + "\"," + "point:\"" + Math.round(point * entry.getValue()) + "\"," + "pic:\"" + url + "\"}" + ",";

        }
        String newStr = "[" + str.substring(0, str.length() - 1) + "]";
        return newStr;
    }


    public static boolean isOnTime(String time) {
        boolean isOnTime = false;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(time);  //目标日期
            Date today = new Date();  //当天日期
            //判断天数
            if (date != null) {
                String timeDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(today);

                if (todayDate.equals(timeDate)) {
//					int startTime =0;
//					int endTime = 0;
//					if (selectDate.equals("8:00~12:00")){
//						startTime = 7;
//						endTime = 12;
//					}else if (selectDate.equals("14:00~18:00")){
//						startTime = 13;
//						endTime = 18;
//					}
//
//					Calendar cal = Calendar.getInstance();
//
//					int hour = cal.get(Calendar.HOUR_OF_DAY);
//					int minute = cal.get(Calendar.MINUTE);
//					int minuteOfDay = hour*60+minute;
//					if (minuteOfDay>=startTime*60&&minuteOfDay<=endTime*60){
//						isOnTime = true;
//					}else {
//						isOnTime = false;
//					}
                    isOnTime = true;
                } else {
                    isOnTime = false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isOnTime;
    }

    /**
     * 下载文件
     *
     * @param context
     * @param strDownloadUri
     * @return 下载请求分配的一个唯一的ID
     */
    public static Long appDownload(Context context, String strDownloadUri) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(strDownloadUri);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("益趣生活")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        Long reference = downloadManager.enqueue(request);
        return reference;
    }

    /**
     * 允许editText输入长度  汉字占两个字符  数字英文等占一个字符
     * @param maxLen 最大长度
     * @return
     */
    public static InputFilter getEditTextLength(final int maxLen){
        return  new InputFilter() {
            @Override
            public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
                int dindex = 0;
                int count = 0;
                while (count <= maxLen && dindex < dest.length()) {
                    char c = dest.charAt(dindex++);
                    if (c < 128) {
                        count = count + 1;
                    } else {
                        count = count + 2;
                    }
                }

                if (count > maxLen) {
                    return dest.subSequence(0, dindex - 1);
                }

                int sindex = 0;
                while (count <= maxLen && sindex < src.length()) {
                    char c = src.charAt(sindex++);
                    if (c < 128) {
                        count = count + 1;
                    } else {
                        count = count + 2;
                    }
                }

                if (count > maxLen) {
                    sindex--;
                }
                return src.subSequence(0, sindex);
            }
        };
    }

}
