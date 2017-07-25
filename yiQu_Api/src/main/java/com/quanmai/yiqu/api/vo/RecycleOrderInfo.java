package com.quanmai.yiqu.api.vo;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhanjinj on 16/4/19.
 */
public class RecycleOrderInfo implements Serializable{
    public String id;       //订单id
    public int recycleId;    //回收人id
    public String userId;      //订单发布人id
    public String point;       //积分
    public String publisher;   //发布人
    public String publishTime;  //发布时间
    public String statue;     //订单预约状态
    public String recycleName;  //回收人姓名
    public String recycleTime;  //回收人接单时间
    public String rangeDate;   //预约日期
    public String rangeTime;   //预约时间段
    public String address;    //地址
    public String mobile;     //手机
    public String portrait;    //头像
    public List<RecycleGarbageInfo> recycleDetails; //垃圾信息

    public RecycleOrderInfo get(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        RecycleOrderInfo recycleInfo = null;
        try {
            recycleInfo = new Gson().fromJson(jsonObject.toString(), RecycleOrderInfo.class);
        } catch (JsonSyntaxException e) {
            Log.e("recycleInfo数据解析异常--->", e.toString());
        }
        return recycleInfo;
    }


}
