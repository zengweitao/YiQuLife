package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

public class FixClass {
	public FixClass(JSONObject jsonObject) throws JSONException {
		classcode=jsonObject.getString("classcode");
		classname=jsonObject.getString("classname");
		classimage=jsonObject.getString("classimage");
		if(jsonObject.has("classcontentnum"))
		classcontentnum=jsonObject.getInt("classcontentnum");
	}
	public String classcode;
	public String classname;
	public String classimage;
	public int classcontentnum;
}
