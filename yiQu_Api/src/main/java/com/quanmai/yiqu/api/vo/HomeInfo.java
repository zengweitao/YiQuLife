package com.quanmai.yiqu.api.vo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.quanmai.yiqu.base.CommonList;

public class HomeInfo {

	public HomeInfo(JSONObject jsonObject) throws JSONException {
		JSONArray array = null;
		if (jsonObject.has("adList")){
			array = jsonObject.getJSONArray("adList");
			adList = new CommonList<AdvertInfo>();
			for (int i = 0; i < array.length(); i++) {
				adList.add(new AdvertInfo(array.getJSONObject(i)));
			}
		}
		classList = new CommonList<AdvertInfo>();
		if (jsonObject.has("classList")) {
			array = jsonObject.getJSONArray("classList");
			for (int i = 0; i < array.length(); i++) {
				classList.add(new AdvertInfo(array.getJSONObject(i)));
			}
		}
	}

	public CommonList<AdvertInfo> adList;

	public CommonList<AdvertInfo> classList;

}
