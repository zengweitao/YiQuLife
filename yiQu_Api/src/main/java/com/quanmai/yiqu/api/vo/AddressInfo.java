package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class AddressInfo implements Serializable{
	private static final long serialVersionUID = -192308161265758483L;
	
	public AddressInfo(JSONObject jsonObject) throws JSONException {
		if (jsonObject.has("name")) {
			name = jsonObject.getString("name");
		}else {
			name=new String();
		}
		if (jsonObject.has("phone")) {
			phone = jsonObject.getString("phone");
		}else {
			phone=new String();
		}
		if (jsonObject.has("customerid")) {
			customerid = jsonObject.getInt("customerid");
		}else {
			customerid=0;
		}
		if (jsonObject.has("address")) {
			address = jsonObject.getString("address");
		}else {
			address=new String();
		}
		if (jsonObject.has("sortfield")) {
			sortfield = jsonObject.getInt("sortfield");
		}else {
			sortfield=0;
		}
		if (jsonObject.has("id")) {
			id = jsonObject.getInt("id");
		}else {
			id=0;
		}
	}
	
	/*public AddressInfo(String name,String phone,String area_name,String area_id,String area_level,String position) {
		this.address_id=new String();
		this.address_type=0;
		this.name=name;
		this.phone=phone;
		this.area_name=area_name;
		this.area_id=area_id;
		this.area_level=area_level;
		this.position=position;
	}*/
	public AddressInfo(String name,String phone,String address,int customerid,int id,int sortfield) {

		this.address=address;
		this.name=name;
		this.phone=phone;
		this.customerid=customerid;
		this.id=id;
		this.sortfield=sortfield;
	}

	/*public int address_type;
	public String address_id;
	public String name;
	public String phone;
	public String area_name;
	public String area_id;
	public String area_level;
	public String position;*/

	public String address;
	public int customerid;
	public int id;
	public String name;
	public String phone;
	public int sortfield;
	
	@Override
	public String toString() {
		return "address="+address+"&name=" + name + "&phone=" + phone + "&customerid=" + customerid
				+ "&sortfield=" + sortfield ;
	}



}
