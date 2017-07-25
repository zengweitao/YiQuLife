package com.quanmai.yiqu.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;
import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.common.Utils;
//import com.umeng.common.message.Log;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class QiniuUtil {
	private static QiniuUtil instance;
	Context c;
	List<String> list;
	 String token="";
	 String imgnames = "";
	static UploadManager uploadManager;
	int index = 0, failCount = 0;
	String AccessKey = "EKJbydCyUcT2H9AnRTtTb985Kyh-QGUWcAxpvvvP";
	String SecretKey = "eYcPyA79A_AJ60jtoDmE9IIzkjwskB7YkehBxLve";
	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";
	OnQiniuUploadListener listener;
	static {
		Configuration config = new Configuration.Builder()
				.chunkSize(256 * 1024)// 分片上传时，每片的大小。 默认 256K
				.putThreshhold(512 * 1024)// 启用分片上传阀值。默认 512K
				.connectTimeout(10)// 链接超时。默认 10秒
				.responseTimeout(60)// 服务器响应超时。默认 60秒
				.recorder(null)// recorder 分片上传时，已上传片记录器。默认 null
				.recorder(null, null)// keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
				.zone(Zone.zone0)// 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
				.build();
		// 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
		uploadManager = new UploadManager(config);
	}

	/*
	 * public QiniuUtil(Context c, String token, List<String> list){ this.c = c;
	 * this.token = token; this.list = list; }
	 */
	public QiniuUtil(Context c, List<String> list,
			OnQiniuUploadListener onQiniuUploadListener) {
		this.c = c;

		this.list = list;
		this.listener = onQiniuUploadListener;
		 this.token = getToken();
	}

	public String getToken() {
		try {
			JSONObject _json = new JSONObject();
			long _dataline = System.currentTimeMillis() / 1000 + 3600;
			_json.put("deadline", _dataline);// 有效时间为一个小时
			_json.put("scope", "images");
			String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json
					.toString().getBytes());
			byte[] _sign = HmacSHA1Encrypt(_encodedPutPolicy, SecretKey);
			String _encodedSign = UrlSafeBase64.encodeToString(_sign);
			String _uploadToken = AccessKey + ':' + _encodedSign + ':'
					+ _encodedPutPolicy;
			return _uploadToken;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 
	 * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
	 * 
	 * @param encryptText
	 *            被签名的字符串
	 * @param encryptKey
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
			throws Exception {
		byte[] data = encryptKey.getBytes(ENCODING);
		// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
		SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
		// 生成一个指定 Mac 算法 的 Mac 对象
		Mac mac = Mac.getInstance(MAC_NAME);
		// 用给定密钥初始化 Mac 对象
		mac.init(secretKey);
		byte[] text = encryptText.getBytes(ENCODING);
		// 完成 Mac 操作
		return mac.doFinal(text);
	}

	public void upload() {
		if (index < list.size()) {
			if (list.get(index).startsWith("http")) { // 不上传，用原来的名称
				imgnames += list.get(index).substring(24) + ",";
				index++;
				upload();

			} else {
				final String key = "a"+UserInfo.get().userid+"-0-"
						+ DateUtil.formatToString(new Date(System.currentTimeMillis()),"yyyyMMddHHmmss") + ".jpg"; // 唯一性
				Log.e("--查看名字","== "+key);
				uploadManager.put(getimage(list.get(index)), key, token,
						new UpCompletionHandler() {
							@Override
							public void complete(String arg0,
									ResponseInfo info, JSONObject arg2) {
								if (info.statusCode == 200) {
									imgnames += key + ",";
									index++;
									failCount = 0;
									upload();
								} else {
									if (failCount < 3) {
										upload();
									} else {// 上传超过3次算失败
										imgnames = null;
										listener.failure();
									}
									failCount++;
								}
							}
						}, null);
			}
		} else {
			listener.success(imgnames.substring(0, imgnames.length() - 1));
		}
	}

	public interface OnQiniuUploadListener {
		void success(String names);

		void failure();
	}

	public byte[] getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 1280f;// 这里设置高度为800f
		float ww = 720f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		if (Utils.getExifOrientation(srcPath)!=0){
			bitmap = Utils.rotateBitmapByDegree(bitmap,Utils.getExifOrientation(srcPath));
		}
		if (be == 1)
			return getBytes(srcPath);
		else
			return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	public byte[] compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 500 && options > 0) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		/*
		 * ByteArrayInputStream isBm = new
		 * ByteArrayInputStream(baos.toByteArray(
		 * ));//把压缩后的数据baos存放到ByteArrayInputStream中 Bitmap bitmap =
		 * BitmapFactory.decodeStream(isBm, null,
		 * null);//把ByteArrayInputStream数据生成图片 try { FileOutputStream out = new
		 * FileOutputStream
		 * (Environment.getExternalStorageDirectory()+"/myPicture2.jpg");
		 * bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); out.flush();
		 * out.close(); } catch (FileNotFoundException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		return baos.toByteArray();
	}

	public byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
}