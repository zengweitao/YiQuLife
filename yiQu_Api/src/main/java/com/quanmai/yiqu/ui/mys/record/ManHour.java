package com.quanmai.yiqu.ui.mys.record;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;

import com.quanmai.yiqu.common.Utils;

import android.util.Log;

public class ManHour {
	public int month;
	public int year;
	public HashMap<Integer, String> days;

	public ManHour(JSONArray jsonArray) throws JSONException {
		Calendar now = Calendar.getInstance();
		long time;
		Date date;
		if (jsonArray.length() > 0) {
			 time = jsonArray.getLong(0);
			date = new java.util.Date(time * 1000);
			now.setTime(date);
		}
		month = now.get(Calendar.MONTH) + 1;
		year = now.get(Calendar.YEAR);
		days = new HashMap<Integer, String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			 time= jsonArray.getLong(i);
			 date = new java.util.Date(time * 1000);
			 now.setTime(date);
			 Utils.E("now.get(Calendar.DAY_OF_MONTH)="+now.get(Calendar.DAY_OF_MONTH));
			 days.put(now.get(Calendar.DAY_OF_MONTH),"is_sign_in");
		}

	}
	
	public ManHour(int year, int month){
		this.year = year;
		this.month = month;
		days = new HashMap<Integer, String>();
	}
}
