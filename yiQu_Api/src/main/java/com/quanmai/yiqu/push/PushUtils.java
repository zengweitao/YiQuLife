//package com.quanmai.yiqu.push;
//
//import com.quanmai.yiqu.common.Utils;
////import com.umeng.message.PushAgent;
//
//import android.content.Context;
//import android.os.AsyncTask;
//
//public class PushUtils {
//	public static void setAlias(Context context, String alias) {
//		new AddAliasTask(context, alias).execute();
//	}
//
//}
//
//class AddAliasTask extends AsyncTask<Void, Void, Boolean> {
//
//	String alias;
//	Context context;
//
//	public AddAliasTask(Context context, String aliasString) {
//		// TODO Auto-generated constructor stub
//		this.alias = aliasString;
//		this.context = context;
//	}
//
//	protected Boolean doInBackground(Void... params) {
//		try {
//			//一台设备上最大支持20个类型的alias。而每个类型的alias同时只能存在一个，同一个类型的新alias会覆盖旧alias。
//			return PushAgent.getInstance(context).addAlias(alias, "user_id");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	@Override
//	protected void onPostExecute(Boolean result) {
//		if (Boolean.TRUE.equals(result))
//			Utils.E("alias was set successfully.");
//	}
//
//}
