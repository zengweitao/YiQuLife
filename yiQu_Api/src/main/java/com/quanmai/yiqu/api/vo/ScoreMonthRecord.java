package com.quanmai.yiqu.api.vo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScoreMonthRecord {
	public ScoreMonthRecord(JSONObject jsonObject) throws JSONException {
		month=jsonObject.optString("month");
		year=jsonObject.optString("year");
		cost=jsonObject.getInt("cost");
		get=jsonObject.getInt("get");
		dayRecords=new ArrayList<ScoreDayRecord>();
		JSONArray array=jsonObject.getJSONArray("child");
		for (int i = 0; i < array.length(); i++) {
			dayRecords.add(new ScoreDayRecord(array.getJSONObject(i)));
		}
	}
	public  String month;
	public  String year;
	public  int cost;
	public  int get;
	public  List<ScoreDayRecord> dayRecords;
}
