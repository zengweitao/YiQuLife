package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by 95138 on 2016/4/29.
 */
public class ActivityIntegrationInfo implements Serializable {

    public String activityId;  //活动id
    public String activityName;  //活动名称
    public String activityType;
    public String beginTime;
    public String defaultPoint;  //默认每次领取积分数
    public String endTime;
    public String point; //已领取积分
    public String totalPoint; //活动总积分
    public String userId; //活动发布人
    public String optTime; //活动发布时间
    public String limitPoint; //最大限额

    public ActivityIntegrationInfo(JSONObject jsonObject)throws JSONException {
        activityId = jsonObject.optString("activityId");
        activityName = jsonObject.optString("activityName");
        activityType = jsonObject.optString("activityType");
        beginTime = jsonObject.optString("beginTime");
        defaultPoint = jsonObject.optString("defaultPoint");
        endTime = jsonObject.optString("endTime");
        point = jsonObject.optString("point");
        totalPoint = jsonObject.optString("totalPoint");
        userId = jsonObject.optString("userId");
        optTime = jsonObject.optString("optTime");
        limitPoint = jsonObject.optString("limitPoint");
    }
}
