package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

public class PicValidateInfo {
	public PicValidateInfo(JSONObject jsonObject) throws JSONException {
		classcode = jsonObject.getString("classcode");
		id = jsonObject.getString("id");
		image = jsonObject.getString("image");
		name = jsonObject.getString("name");
	}

	public String classcode;
	public String id;
	public String image;
	public String name;
	public boolean isCheck=false;
}
