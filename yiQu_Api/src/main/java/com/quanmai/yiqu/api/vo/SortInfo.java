package com.quanmai.yiqu.api.vo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SortInfo {
	public SortInfo(String id,String name) {
		sort_name=name;
		sort_id=id;
		this.haschild=false;
		childs=null;
	}
	
	//地址用的
	public SortInfo(String id,String name,boolean haschild) {
		sort_name=name;
		sort_id=id;
		this.haschild=haschild;
		childs=null;
	}
	
	public SortInfo(JSONObject jsonObject) throws JSONException {
		sort_name=jsonObject.getString("class_name");
		sort_id=jsonObject.getString("class_id");
		if(jsonObject.has("child"))
		{
		JSONArray array=jsonObject.getJSONArray("child");
		childs=new ArrayList<SortInfo>();
		for (int i = 0; i < array.length(); i++) {
			childs.add(new SortInfo(array.getJSONObject(i)));
		}
		}else {
			this.haschild=false;
			childs=null;
		}
	}
	public String sort_name;
	public String sort_id;
	public ArrayList<SortInfo> childs;
	public boolean haschild;
}
