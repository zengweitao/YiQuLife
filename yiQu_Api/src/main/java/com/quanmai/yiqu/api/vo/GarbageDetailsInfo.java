package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by 95138 on 2016/4/20.
 */
public class GarbageDetailsInfo implements Serializable {
    public String garbage;//废品
    public String id;
    public String orderId;  //订单id
    public String quantity; //数量
    public String point; //积分

    public GarbageDetailsInfo(String garbage,String quantity){
        this.garbage = garbage;
        this.quantity = quantity;
        id = "";
        orderId = "";
        point = "0";
    }

    public GarbageDetailsInfo(JSONObject jsonObject) throws JSONException {
        garbage = jsonObject.optString("garbage");
        id = jsonObject.optString("id");
        orderId = jsonObject.optString("orderId");
        quantity = jsonObject.optString("quantity");
        point = jsonObject.optString("point");
    }
}
