package com.quanmai.yiqu.api.vo;

import com.quanmai.yiqu.common.util.RelativeDateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yin on 16/3/9.
 */
public class BannerInfo {
    public BannerInfo() {
        // TODO Auto-generated constructor stub
    }

    public BannerInfo(JSONObject object) throws JSONException {
        JSONArray jsonArray = object.getJSONArray("adList");
        adList = new ArrayList<>();
        for (int i=0;i<jsonArray.length();i++){
            adList.add(new AdInfo(jsonArray.getJSONObject(i)));
        }

    }

    public List<AdInfo> adList;

}
