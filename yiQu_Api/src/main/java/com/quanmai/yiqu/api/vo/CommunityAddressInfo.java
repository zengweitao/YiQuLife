package com.quanmai.yiqu.api.vo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yin on 16/3/9.
 */
public class CommunityAddressInfo {
    public String commcode;
    public String commname;
//    public String id;
//    public String level;
//    public String parentid;

    public CommunityAddressInfo(JSONObject jsonObject) throws JSONException {
        commcode = jsonObject.optString("commcode");
        commname = jsonObject.optString("commname");
//        id = jsonObject.optString("id");
//        level = jsonObject.optString("level");
//        parentid = jsonObject.optString("parentid");
    }
}
