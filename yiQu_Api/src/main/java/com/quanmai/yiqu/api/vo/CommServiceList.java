package com.quanmai.yiqu.api.vo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yin on 16/3/10.
 */
public class CommServiceList {
    public CommServiceList(JSONObject object) throws JSONException {
        CommServiceList = new ArrayList<>();
        JSONArray array = object.getJSONArray("commServiceinfo");
        for (int i=0;i<array.length();i++){
            CommServiceList.add(new CommServiceInfo(array.getJSONObject(i)));
        }
    }

    public List<CommServiceInfo> CommServiceList;
}
