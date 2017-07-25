package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

public class AdvertInfo {
	public AdvertInfo() {
		// TODO Auto-generated constructor stub
	}

	public AdvertInfo(JSONObject object) throws JSONException {
		if (object.has("class_id")) {
			id = object.getString("class_id");
			name = object.getString("class_name");
			picurl = object.getString("class_img");
		} else {
			id = object.getString("adver_id");
			link_value = object.getString("adver_url");
			picurl = object.getString("adver_img");
		}
	}

	// 图片的URL地址
	public String id;
	public String name;
	public String link_value;
	public String picurl;
	public int link_type;
	public int picId;
}
