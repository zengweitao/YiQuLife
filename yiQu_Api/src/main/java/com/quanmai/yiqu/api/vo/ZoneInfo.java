package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

import com.quanmai.yiqu.R;

public class ZoneInfo {
	public ZoneInfo(JSONObject jsonObject) throws JSONException {
		alias=jsonObject.getString("alias");
		vipInfo=new VipInfo(jsonObject, "level");
		sex=jsonObject.getInt("sex");
		switch (sex) {
		case 1:
			sex_name = "男";
			sex_img_id=R.drawable.sex_man;
			break;
		case 2:
			sex_name = "女";
			sex_img_id=R.drawable.sex_female;
			break;
		default:
			sex_name = "男";
			sex_img_id=R.drawable.sex_man;
			break;
		}
		face=jsonObject.getString("userimg");
//		score=jsonObject.getInt("score");
//		donation_count=jsonObject.getInt("donation_count");
//		phone=jsonObject.getString("phone");
	}
        public String alias;
        public VipInfo vipInfo;
//        public String area_name;
        public int sex;
        public String sex_name;
        public int sex_img_id;
        public String phone;
    	public String face;
}
