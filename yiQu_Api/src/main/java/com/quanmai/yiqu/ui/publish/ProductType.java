package com.quanmai.yiqu.ui.publish;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;


public class ProductType {
	String class_id;
	String class_name;
	public String getClass_id() {
		return class_id;
	}

	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public ProductType(String class_id, String class_name) {
		this.class_id = class_id;
		this.class_name = class_name;
	}

	public static List<ProductType> get(JSONArray array){
		List<ProductType> list = new ArrayList<ProductType>();
		for (int i = 0; i < array.length(); i++) {
			try {
				list.add(new ProductType(array.getJSONObject(i).getString("class_id"), array.getJSONObject(i).getString("class_name")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
}
