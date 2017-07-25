package com.quanmai.yiqu.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.quanmai.yiqu.Session;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.QHttpClient;
import com.quanmai.yiqu.common.util.QHttpClient.ConnectionHandler;
import com.quanmai.yiqu.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ApiConfig {
//    protected static String URL = "https://apps.eacheart.com:9443/"; //正式打包版接口服务器
      protected static String URL = "https://test.eacheart.com:9443"; //测试环境服务器2
//    protected static String URL = "http://120.26.115.240:9999"; //生产环境服务器
//    protected static String URL = "http://115.28.228.192:9090"; //测试环境服务器


    public static String CLSSSIFIGARBAGE_URL = "http://app.eacheart.com";
    //public static String GARBAGE_GAME_URL = "http://game.eacheart.com";
    public static String GARBAGE_GAME_URL = "https://game.eacheart.com/";   //正式打包版游戏地址
    public static String INTRODUCE_URL = "http://qd.eacheart.com/introduce.html";
    public static String DROP_GARBAGE_URL = "http://www.gzztnet.com/web/toufangshuoming/index.htm";  //废品投放说明地址
    public static String DROP_LAJI_URL = "http://www.gzztnet.com/web/lajitoufang/index.htm";  //垃圾投放说明地址
    public static String LOGO_URL = "https://app.eacheart.com/img/scan_button.png?time=";  //首页扫码logo地址


    protected static String getGlobalvariable(Context context) {
        Session session = Session.get(context);
        return "&token=" + session.getToken() + "&location="
                + session.getLocation_x() + "," + session.getLocation_y()
                + "&version=" + session.getVersion() + "&os="
                + session.getOsVersion() + "&imei=" + session.getImei();
    }

    public interface ApiRequestListener<T> {
        void onSuccess(String msg, T data);

        void onFailure(String msg);
    }

//	protected static RequestParams analyzeParam1(String paramStr) {
//		Utils.E(paramStr);
//		RequestParams params = new RequestParams();
//		if (paramStr != null) {
//			String[] tempArray = paramStr.split("&");
//			for (String perParam : tempArray) {
//				String[] perParams = perParam.split("=");
//				if (perParams.length == 2) {
//					Utils.E(perParams[0] + "=" + perParams[1]);
//					params.addBodyParameter(perParams[0], perParams[1]);
//				}
//				// else if (perParams.length == 1) {
//				// params.addBodyParameter(perParams[0], "");
//				// }
//			}
//		}
//		return params;
//	}

//	protected static void HttpPost2(final Context context, String params,
//			final HttpCallBack callBack) {
//		HttpUtils http = new HttpUtils();
//		http.configCurrentHttpCacheExpiry(1000 * 50);
//		http.send(HttpRequest.HttpMethod.POST, URL, analyzeParam(params
//				+ getGlobalvariable(context)), new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				Log.e("q",
//						"HttpException.getExceptionCode()="
//								+ arg0.getExceptionCode());
//				Log.e("q",
//						"HttpException.getLocalizedMessage()="
//								+ arg0.getLocalizedMessage());
//				Log.e("q", "HttpException.getMessage()=" + arg0.getMessage());
//				switch (arg0.getExceptionCode()) {
//				case 0:
//					callBack.onFailure("服务端未响应");
//					break;
//				default:
//					callBack.onFailure("网络连接错误");
//					break;
//				}
//
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> arg0) {
//				Log.e("q", arg0.result);
//				JSONObject jsonObject;
//				String msg = null;
//				try {
//					jsonObject = new JSONObject(arg0.result);
//					int status = jsonObject.getInt("status");
//					msg = jsonObject.getString("msg");
//					if (System.currentTimeMillis() / 1000 > 1454083200) {
//						if ((context instanceof LoginActivity)) {
//							callBack.onFailure("当前版本已过期，请重新下载");
//						} else {
//							Utils.showCustomToast(context, "当前版本已过期，请重新下载");
//							Intent intent = new Intent(context,
//									LoginActivity.class);
//							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//									| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//							context.startActivity(intent);
//						}
//					} else if (status == 1) {
//						callBack.onSuccess(jsonObject);
//					} else if (status == -1) {
//						Utils.showCustomToast(context, msg);
//						Intent intent = new Intent(context, LoginActivity.class);
//						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//								| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//						context.startActivity(intent);
//					} else {
//						callBack.onFailure(msg);
//					}
//				} catch (JSONException e) {
//					if (msg == null) {
//						callBack.onFailure("数据解析失败");
//					} else {
//						callBack.onFailure(msg);
//
//					}
//					e.printStackTrace();
//				}
//
//			}
//		});
//	}

    protected static void HttpPost(final Context context, String params,
                                   final HttpCallBack callBack) {
        Utils.E(URL);
        QHttpClient.PostConnection3(URL, params
                + getGlobalvariable(context) + "\"", new ConnectionHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                int status = jsonObject.getInt("status");
                String msg = jsonObject.getString("msg");
                if (status == 1) {
                    callBack.onSuccess(jsonObject);
                } else if (status == -1) {
                    Session.get(context).Logout();
                    callBack.onFailure(msg);
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                } else {
                    callBack.onFailure(msg);
                }
            }

            @Override
            public void onFailure(String msg) {
                callBack.onFailure(msg);
            }
        });
    }

    public interface HttpCallBack {
        public void onSuccess(JSONObject jsonObject) throws JSONException;

        public void onFailure(String msg);
    }

}
