package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yin on 16/3/9.
 */
public class AdInfo {
    public AdInfo() {
        // TODO Auto-generated constructor stub
    }

    public AdInfo(JSONObject object) throws JSONException {
        adver_url = object.optString("adver_url");
        adver_id = object.optString("adver_id");
        adver_img = object.optString("adver_img");
        adver_content = object.optString("adver_content");
        adver_alias = object.optString("adver_alias");
    }

    // 图片的URL地址
    public String adver_url;
    public String adver_id;
    public String adver_img;
    public String adver_content;
    public String adver_alias;      //自定义点击事件id
}
