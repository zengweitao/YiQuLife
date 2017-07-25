package com.quanmai.yiqu.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.content.res.AssetManager;
/**
 * 
 */
public class FileUtil {

	public static String getOrCreateDbdir(Context context){
		String path = "/data/data/"+ context.getPackageName()+"/databases/";
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
		return path;
	}
	
	public static boolean copyAssetsToFilesystem(Context context, String assetsSrc, String des){
		File file = new File(des);
		if (file.exists())
			return true;
		InputStream istream = null;
		OutputStream ostream = null;
		try{
			AssetManager am = context.getAssets();
			istream = am.open(assetsSrc);
			ostream = new FileOutputStream(des);
			byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = istream.read(buffer))>0){
	    		ostream.write(buffer, 0, length);
	    	}
	    	istream.close();
	    	ostream.close();
		}
		catch(Exception e){
			e.printStackTrace();
			try{
				if(istream!=null)
			    	istream.close();
				if(ostream!=null)
			    	ostream.close();
			}
			catch(Exception ee){
				ee.printStackTrace();
			}
			return false;
		}
		return true;
	}
}