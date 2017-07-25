package com.quanmai.yiqu.baidu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.quanmai.yiqu.base.CommonList;

public class DeviceBean {
	/** 设备坐标 */
	private String location;
	/** 设备名 */
	private String name;
	/** 设备详细地址 */
	private String address;

	public String getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public double getLat() {
		return Double.parseDouble(location.split(",")[0]);
	}

	public double getLng() {
		return Double.parseDouble(location.split(",")[1]);
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public static CommonList<DeviceBean> parseDeviceBean(String jsonString) {
		CommonList<DeviceBean> list = new CommonList<DeviceBean>();
		try {
			JSONArray array = new JSONArray(jsonString);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				DeviceBean bean = new DeviceBean();
				bean.address = object.optString("address");
				bean.location = object.optString("location");
				bean.name = object.optString("name");
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
}
