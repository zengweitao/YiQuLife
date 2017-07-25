package com.quanmai.yiqu.api.vo;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by zhanjinj on 16/3/23.
 */
public class InspectionRecordInfo {
	public int year;
	public int month;
	public String checkrate;
	public HashMap<Integer, String> days;

	public InspectionRecordInfo() {
		checkrate = "0";
		days = new HashMap<>();
	}

	public InspectionRecordInfo(int year, int month) {
		this.year = year;
		this.month = month;
		this.checkrate = "0";
		this.days = new HashMap<>();
	}

	public InspectionRecordInfo(JSONObject jsonObject) {
		if (jsonObject.has("checkrate")) {
			this.checkrate = jsonObject.optString("checkrate");
		}

		JSONArray jsonArray = jsonObject.optJSONArray("dateList");
		days = new HashMap<>();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);

		if (jsonArray.length() > 0) {
			try {
				date = format.parse(jsonArray.getString(0));
				calendar.setTime(date);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		this.year = calendar.get(Calendar.YEAR);
		this.month = calendar.get(Calendar.MONTH) + 1;

		if (jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					String strDate = jsonArray.optString(i);
					date = format.parse(strDate);
					calendar.setTime(date);
					days.put(calendar.get(Calendar.DAY_OF_MONTH), "is_inspection");
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
