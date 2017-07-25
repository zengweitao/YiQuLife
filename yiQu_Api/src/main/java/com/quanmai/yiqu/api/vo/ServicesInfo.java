package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 95138 on 2016/5/18.
 */
public class ServicesInfo {
    public String href;    //链接url
    public String name;    //服务名称
    public String remarks; //备注
    public String areaCode;
    public String icon;
    public String num;

    public ServicesInfo(JSONObject jsonObject) throws JSONException {
        href = jsonObject.optString("href");
        name = jsonObject.optString("name");
        remarks = jsonObject.optString("remarks");
        areaCode = jsonObject.optString("areaCode");
        icon = jsonObject.optString("icon");
        num = jsonObject.optString("num");

    }
}
