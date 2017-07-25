package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;

public class BarCodeInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8196885321120664480L;
	public BarCodeInfo() {
		// TODO Auto-generated constructor stub
	}
	public BarCodeInfo(JSONObject jsonObject)  throws JSONException {
		code=new String();
		address=jsonObject.getString("address");
		time=jsonObject.getString("get_time");
		terminalno=jsonObject.getString("terminalno");
		phone=jsonObject.getString("phone");
		// TODO Auto-generated constructor stub
	}
	public String code;
	public	String address;
	public	String time;
	public	String phone;
	public	String terminalno;
}
