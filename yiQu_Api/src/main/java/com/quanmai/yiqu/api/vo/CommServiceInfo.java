package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 95138 on 2016/3/9.
 */
public class CommServiceInfo {
    public CommServiceInfo(JSONObject object) throws JSONException {
        service_content = object.optString("servicecontent");
        contact_tel = object.optString("contacttel");
        title = object.optString("title");
        commcode = object.optString("commcode");
        commname = object.optString("commname");
        address = object.optString("address");
    }

    public String service_content;//服务内容
    public String contact_tel; //服务电话
    public String title; //服务标题
    public String commcode;//社区编码
    public String commname; //社区名称
    public String address;//详细地址
}
