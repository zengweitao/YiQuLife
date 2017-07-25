package com.quanmai.yiqu.common.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;

public class PhotoUtil {
	public static boolean isSdcardExisting() {
		final String state = Environment.getExternalStorageState();

		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static Bitmap toSmall(Bitmap bitmap, String path) {
		int scale = 1;
		int width_tmp = bitmap.getWidth();
		int height_tmp = bitmap.getHeight();

		while (true) {
			if (width_tmp / 2 < 100 || height_tmp / 2 < 100)
				break;

			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale); // 长和宽放大缩小的比例
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(path);

			try {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100,
						fileOutputStream);
			} finally {
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (IOException e) {
					
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return bitmap;
	}
	
	public static Bitmap getBitmapFromUri(ContentResolver cr, Uri uri) {
		try {
			return BitmapFactory.decodeStream(cr.openInputStream(uri));
		} catch (FileNotFoundException e) {

		}
		
		return null;
	}
}
