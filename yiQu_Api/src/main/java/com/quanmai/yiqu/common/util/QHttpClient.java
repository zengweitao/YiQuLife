package com.quanmai.yiqu.common.util;

import android.os.Handler;
import android.os.Message;

import com.quanmai.yiqu.common.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class QHttpClient {
    private static String analyzeString(String paramStr) {
        String params = new String();
        if (paramStr != null) {
            String[] tempArray = paramStr.split("&");
            for (String perParam : tempArray) {
                String[] perParams = perParam.split("=");
                if (perParams.length == 2) {
                    Utils.E(perParams[0] + "=" + perParams[1]);
                    if (params.length() == 0) {
                        params = perParams[0] + "=" + perParams[1];
                    } else {
                        params = params + "&" + perParams[0] + "="
                                + perParams[1];
                    }
                }
            }
        }
        return params;
    }


    // multipart/form-data
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("multipart/form-data; charset=utf-8");

    public static void PostConnection3(final String Url,
                                       final String paramStr,
                                       final ConnectionHandler connectionHandler) {
        final UserPresenterHandler mHandler = new UserPresenterHandler(connectionHandler);
        final OkHttpClient client = new OkHttpClient();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                String requeString = new String();
                int statusCode = 0;
                try {
                    Request request = new Request.Builder()
                            .url(Url)
                            .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,
                                    analyzeString(paramStr))).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        statusCode = 200;
                        requeString = response.body().string().trim();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JSONObject jsonObj = null;
                if (requeString != null) {
                    try {
                        jsonObj = new JSONObject(requeString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Utils.E(Url);
                Utils.E(paramStr);
                Utils.E("" + statusCode);
                Utils.E("" + requeString);
                msg.obj = jsonObj;
                msg.arg1 = statusCode;
                mHandler.sendMessage(msg);
            }

        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    public interface ConnectionHandler {
        public void onSuccess(JSONObject jsonObject) throws JSONException;

        public void onFailure(String msg);
    }

    static class UserPresenterHandler extends Handler {
        // private final WeakReference<ConnectionHandler> mReference;

        private ConnectionHandler mReference;

        UserPresenterHandler(ConnectionHandler connectionHandler) {
            // mReference = new
            // WeakReference<ConnectionHandler>(connectionHandler);
            mReference = connectionHandler;
        }

        @Override
        public void handleMessage(Message msg) {
            // ConnectionHandler handler = mReference.get();
            if (mReference != null) {
                if (msg.arg1 == 200) {
                    if (msg.obj != null) {
                        JSONObject jsonObject = (JSONObject) msg.obj;
                        try {
                            mReference.onSuccess(jsonObject);
                        } catch (JSONException e) {
//							mReference.onFailure("数据解析错误");
                            e.printStackTrace();
                        }

                    } else {
                        mReference.onFailure("网络连接错误");
                    }
                } else {
                    mReference.onFailure("网络连接错误");
                }
            }
        }
    }
}
