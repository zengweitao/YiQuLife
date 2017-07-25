package com.quanmai.yiqu.api.vo;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhanjinj on 16/3/31.
 */
public class VipIntroduction {
	public List<Member> members;

	public static VipIntroduction vipIntroduction;

	public static VipIntroduction getInstance() {
		if (vipIntroduction == null) {
			vipIntroduction = new VipIntroduction();
		}
		return vipIntroduction;
	}

	public VipIntroduction get(JSONObject jsonObject) {
		if (jsonObject == null) {
			return null;
		}
		vipIntroduction = new VipIntroduction();
		vipIntroduction = new Gson().fromJson(jsonObject.toString(), VipIntroduction.class);
		return vipIntroduction;
	}
}
