package com.quanmai.yiqu.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.quanmai.yiqu.api.vo.ClassifyGameMessageInfo;
import com.quanmai.yiqu.api.vo.ClassifyGameRankingInfo;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Chasing-Li on 2017/5/14.
 */

public class ClassifyGameApi extends ApiConfig {
    private static ClassifyGameApi mInstance;

    public static ClassifyGameApi get() {
        if (mInstance == null) {
            mInstance = new ClassifyGameApi();
        }
        return mInstance;
    }
    /**
     * 8303 游戏竞赛排名
     *
     * @param context
     * @param listener
     */
    public void getClassifyGameCompeititonRanking(Context context,String current,final ApiRequestListener<ClassifyGameRankingInfo> listener) {
        String paramStr = "type=8303&current="+current ;

        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                if (jsonObject.getString("status").equals("1")) {
                    Gson gson=new Gson();
                    ClassifyGameRankingInfo data=gson.fromJson(jsonObject.toString(),ClassifyGameRankingInfo.class);
                    listener.onSuccess(jsonObject.optString("msg"), data);
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8302 游戏竞赛信息
     *
     * @param context
     * @param listener
     */
    public void getClassifyGameCompetitionMessage(Context context, final ApiRequestListener<ClassifyGameMessageInfo> listener) {

        String paramStr = "type=8302" ;

        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {

                Gson gson=new Gson();
                ClassifyGameMessageInfo mClassifyGameMessageInfo = gson.fromJson(jsonObject.toString(),ClassifyGameMessageInfo.class);
                listener.onSuccess(jsonObject.optString("msg"), mClassifyGameMessageInfo);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }
}
