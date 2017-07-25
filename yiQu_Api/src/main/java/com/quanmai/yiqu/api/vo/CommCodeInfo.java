package com.quanmai.yiqu.api.vo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yin on 16/3/9.
 */
public class CommCodeInfo {
    public List<CommunityAddressInfo> commCodeList;

    public CommCodeInfo(JSONObject object) throws JSONException {
        JSONArray jsonArray = object.getJSONArray("commCodeList");
        commCodeList = new ArrayList<>();
        for (int i=0;i<jsonArray.length();i++){
            commCodeList.add(new CommunityAddressInfo(jsonArray.getJSONObject(i)));
        }

    }
}
