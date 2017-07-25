package com.quanmai.yiqu.api.vo;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by zhanjinj on 16/4/20.
 */
public class RecycleGarbageInfo {
    public String id;
    public String orderId;
    public String garbage;
    public String quantity;
    public String point;
    public String pic;
    private String commcode;
    private String commname;
    private String type;
    private String unit;

    public static RecycleGarbageInfo get(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        RecycleGarbageInfo garbageInfo = new Gson().fromJson(jsonObject.toString(), RecycleGarbageInfo.class);
        return garbageInfo;
    }

    public String getCommcode() {
        return commcode;
    }

    public void setCommcode(String commcode) {
        this.commcode = commcode;
    }

    public String getCommname() {
        return commname;
    }

    public void setCommname(String commname) {
        this.commname = commname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
