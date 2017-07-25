package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yin on 16/3/11.
 */
public class SignData {
    public SignData(JSONObject jsonObject) throws JSONException {
        if (jsonObject.optJSONArray("adList") != null && jsonObject.optJSONArray("adList").length() > 0) {
            adInfo = new AdInfo(jsonObject.optJSONArray("adList").optJSONObject(0));
        }
        vipInfo = new VipInfo(jsonObject, "level");
        signdays = jsonObject.optString("signdays");
        score = jsonObject.optString("score");
        totaldays = jsonObject.optString("totaldays");
        signpoint = jsonObject.optString("signpoint");
    }

    public AdInfo adInfo = new AdInfo();  //广告
    public VipInfo vipInfo; //vip等级
    public String signdays; //连续签到天数
    public String score;    //当前积分数
    public String totaldays; //总共签到天数
    public String signpoint;//每次签到能够+的积分数
}
