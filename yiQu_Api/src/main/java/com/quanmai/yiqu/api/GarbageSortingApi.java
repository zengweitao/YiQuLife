package com.quanmai.yiqu.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.quanmai.yiqu.api.vo.BannerTwoInfo;
import com.quanmai.yiqu.api.vo.GarbageClassifyInfo;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 殷伟超 on 2016/6/28.
 */
public class GarbageSortingApi extends ApiConfig {
    private static GarbageSortingApi mInstance;

    public static GarbageSortingApi get() {
        if (mInstance == null) {
            mInstance = new GarbageSortingApi();
        }
        return mInstance;
    }

    /**
     * 8061 获取分享文章分类
     *
     * @param context
     * @param listener
     */
    public void ArticleList(Context context ,
                                final ApiRequestListener<CommonList<GarbageClassifyInfo>> listener) {
        HttpPost(context, "type=8061", new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<GarbageClassifyInfo> infos = new CommonList<GarbageClassifyInfo>();
                JSONArray jsonArray = jsonObject.optJSONArray("saclass");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    GarbageClassifyInfo info = new Gson().fromJson(object.toString(),GarbageClassifyInfo.class);
                    infos.add(info);
                }
                listener.onSuccess("", infos);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }
}
