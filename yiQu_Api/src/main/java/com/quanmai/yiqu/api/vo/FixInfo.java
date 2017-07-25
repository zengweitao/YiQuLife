package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

import com.quanmai.yiqu.common.util.RelativeDateFormat;

public class FixInfo {
	public FixInfo(String address_id, String come_time, String problem_id,
			String problem, String description, String picurl) {
		this.address_id = address_id;
		this.come_time = come_time;
		this.problem_id = problem_id;
		this.problem = problem;
		this.description = description;
		this.picurl = picurl;
	}

	public FixInfo(JSONObject jsonObject) throws JSONException {
		address_id = jsonObject.getString("address_id");
		come_time = RelativeDateFormat.format(jsonObject.getString("come_time"));
		problem_id = jsonObject.getString("problem_id");
		problem = jsonObject.getString("problem");
		description = jsonObject.getString("description");
		name = jsonObject.getString("contacter");
		phone = jsonObject.getString("contacttel");
		picurl = jsonObject.getString("describeimg");
		result = jsonObject.getInt("repairresult");
		id = jsonObject.getString("service_id");
		if (jsonObject.has("address")) {
			address = jsonObject.getString("address");
		} else {
			address = new String();
		}
	}

	public String address_id;
	public String address;
	public String come_time;
	public String problem_id;
	public String problem;
	public String description;
	public String name;
	public String phone;
	public int result;
	public String id;
	public String picurl;
	public String come_date;
}
