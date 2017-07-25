package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

import com.quanmai.yiqu.common.util.RelativeDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreDayRecord {
	public ScoreDayRecord(JSONObject jsonObject) throws JSONException {
		time=jsonObject.getString("time");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(time);
			SimpleDateFormat format1 = new SimpleDateFormat("MM-dd HH:mm");
			time = format1.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		type=jsonObject.getInt("type");
		score=jsonObject.getInt("score");

		name=jsonObject.getString("name");
	}
	public  int type;
	public  int score;
	public  String time;
	public  String name;
}
