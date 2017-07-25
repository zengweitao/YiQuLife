package com.quanmai.yiqu.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.quanmai.yiqu.api.vo.BagRecordInfo;
import com.quanmai.yiqu.api.vo.FetchBagRecordInfo;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.api.vo.ZoneInfo;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by accelerate on 16/3/2.
 */
public class UserInfoApi extends ApiConfig {
    private static UserInfoApi mInstance;

    public static UserInfoApi get() {
        if (mInstance == null) {
            mInstance = new UserInfoApi();
        }
        return mInstance;
    }

    /**
     * 8007 获取用户信息
     * @param context
     * @param listener
     */

    public void UserDetail(final Context context,
                           final ApiRequestListener<UserInfo> listener) {
        String paramStr = "type=8007";
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"), new UserInfo(jsonObject));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);

            }
        });
    }

    /**
     * 8008 设置昵称设置性别
     * @param context
     * @param img    头像
     * @param alias    用户昵称
     * @param sex      性别
     * @param birth    生日
     * @param listener
     */
    public void UserSetting(Context context, String img, String alias,
                            String sex, String birth, final ApiRequestListener<String> listener) {
        StringBuilder params = new StringBuilder();
        params.append("type=8008");
        params.append("&img=");
        params.append(img);
        params.append("&alias=");
        params.append(alias);
        params.append("&sex=");
        params.append(sex);
        params.append("&birth=");
        params.append(birth);

        HttpPost(context, params.toString(), new HttpCallBack() {

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
     * 8024用户空间用户信息
     * @param context
     * @param user_id   用户id
     * @param listener
     */
    public void ZoneDetail(Context context, String user_id,
                           final ApiRequestListener<ZoneInfo> listener) {
        String paramStr = "type=8024&user_id=" + user_id;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"), new ZoneInfo(
                        jsonObject));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8025用户空间商品列表
     * @param context
     * @param page   页数
     * @param user_id    用户id
     * @param listener
     */
    public void ZoneGoodsList(Context context, int page, String user_id,
                              final ApiRequestListener<CommonList<GoodsBasic>> listener) {
        String paramStr = "type=8025&page=" + page + "&user_id=" + user_id;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<GoodsBasic> commonList = new CommonList<GoodsBasic>();
                if (jsonObject.has("current_page")) {
                    commonList.current_page = jsonObject.getInt("current_page");
                }
                if (jsonObject.has("max_page")) {
                    commonList.max_page = jsonObject.getInt("max_page");
                }
                if (jsonObject.has("goodsList")) {
                    JSONArray array = jsonObject.getJSONArray("goodsList");
                    for (int i = 0; i < array.length(); i++) {
                        commonList.add(new GoodsBasic(array.getJSONObject(i)));
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
     * 8059 我的首页
     * @param context
     * @param listener
     */

    public void getUserHome(final Context context,
                           final ApiRequestListener<UserInfo> listener) {
        String paramStr = "type=8059";
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"), new UserInfo(jsonObject));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);

            }
        });
    }

    /**
     * 8136 获取个人取袋列表
     * @param context
     * @param page
     * @param listener
     */
    public void getFetchBagInfoList(Context context, int page,String queryInfo,final ApiRequestListener<CommonList<FetchBagRecordInfo>> listener){
        String paramStr = "type=8136&page="+page;
        if(queryInfo!=null&&queryInfo!=""&&queryInfo!="null"){
            paramStr+="&queryInfo="+queryInfo;
        }
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<FetchBagRecordInfo> infoList = new CommonList<FetchBagRecordInfo>();

                if (jsonObject.has("max_page")){
                    infoList.max_page=jsonObject.optInt("max_page");
                }

                if (jsonObject.has("bagsList")){
                    JSONArray jsonArray = jsonObject.optJSONArray("bagsList");
                    for (int i =0;i<jsonArray.length();i++){
                        infoList.add(new Gson().fromJson(jsonArray.get(i).toString(),FetchBagRecordInfo.class));
                    }
                    listener.onSuccess(jsonObject.optString("msg"),infoList);
                }else {
                   listener.onFailure("暂无取袋记录");
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8138 获取个人取袋列表
     * @param context
     * @param page
     * @param listener
     */
    public void getBagInfoList(Context context, final int page, String terminalno, String getBagTime,
                               int nums, String phone, final ApiRequestListener<CommonList<String>> listener){
        String paramStr = "type=8138&page="+page+"&terminalno="+terminalno+"&getBagTime="+getBagTime+
                "&nums="+nums+"&phone="+phone;
        Log.e("-- 获取取代详情",paramStr);
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<String> infoList = new CommonList<String>();
                if (jsonObject.has("max_page")){
                    infoList.max_page=jsonObject.optInt("max_page");
                }

                if (jsonObject.has("barcodesList")){
                    JSONArray jsonArray = jsonObject.optJSONArray("barcodesList");
                    for (int i =0;i<jsonArray.length();i++){
                        infoList.add(jsonArray.getString(i).toString());
                    }
                    listener.onSuccess(jsonObject.optString("msg"),infoList);
                }else {
                    listener.onFailure(jsonObject.optString("msg"));
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

}
