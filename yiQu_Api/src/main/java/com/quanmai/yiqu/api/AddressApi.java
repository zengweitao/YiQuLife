package com.quanmai.yiqu.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.quanmai.yiqu.api.vo.AddressInfo;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by accelerate on 16/3/2.
 */
public class AddressApi extends Api {
    private static AddressApi mInstance;

    public static AddressApi get() {
        if (mInstance == null) {
            mInstance = new AddressApi();
        }
        return mInstance;
    }

    /**
     * 8015 设为默认地址
     * @param context
     * @param id    地址id
     * @param listener
     */
    public void AddressDelete(Context context, int id, final ApiRequestListener<String> listener) {
        String paramStr = "type=8009" + "&id=" + id;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"), "");
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8012 获取地址列表
     * @param context
     * @param page    页数
     * @param listener
     */
    public void AddressList(Context context, int page,
                            final ApiRequestListener<CommonList<AddressInfo>> listener) {
        String paramStr;
        if (page<0){
            paramStr = "type=8012";
        }else {
           paramStr = "type=8012" + "&page=" + page;
        }
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<AddressInfo> commonList = new CommonList<AddressInfo>();
                if (jsonObject.has("current_page")) {
                    commonList.current_page = jsonObject.getInt("current_page");
                }
                if (jsonObject.has("max_page")) {
                    commonList.max_page = jsonObject.getInt("max_page");
                }
                Log.e("--地址列表","== "+jsonObject);
                if (jsonObject.has("list")) {
                    JSONArray array = jsonObject.getJSONArray("list");
                    if (array.length()>0){
                        for (int i = 0; i < array.length(); i++) {

                            AddressInfo info = new AddressInfo(array
                                    .getJSONObject(i));
                            commonList.add(info);
                        }
                    }
                }
                listener.onSuccess(jsonObject.getString("msg"), commonList);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }
    /**
     * 8013 新增地址
     * @param context
     * @param name
     * @param phone
     * @param address
     * @param listener
     */
    public void AddressAdd(Context context, String name,String phone,String address,
                           final ApiRequestListener<String> listener) {
        String paramStr = "type=8013&name="+name + "&phone="+phone+"&address="+address;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {

                listener.onSuccess(jsonObject.getString("msg"), "");
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }
    /**
     * 8014 编辑地址
     * @param context
     * @param id
     * @param name
     * @param phone
     * @param address
     * @param listener
     */
    public void AddressEdit(Context context, int id,String name,String phone,String address,
                            final ApiRequestListener<String> listener) {
        String paramStr = "type=8014&id=" +id+"&name="+name+"&phone="+phone+"&address="+address ;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"), "");
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8015 设为默认地址
     * @param context
     * @param id    地址id
     * @param listener
     */
    public void AddressDefault(Context context, int id,
                               final ApiRequestListener<String> listener) {
        String paramStr = "type=8015" + "&id=" + id;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"), "");
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }
}
