package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

public class GoodsInfo extends GoodsBasic {

	public GoodsInfo(JSONObject jsonObject) throws JSONException {
		super(jsonObject);
		description = jsonObject.optString("goods_description");
		class_id = jsonObject.optString("class_id");
		class_name = jsonObject.optString("class_name");
//		address_id = jsonObject.getString("address_id");
//		area_id = jsonObject.getString("area_id");
		area_name = jsonObject.optString("area_name");
//		position = jsonObject.getString("position");
		user_id = jsonObject.optString("user_id");
		alias = jsonObject.optString("alias");
		phone = jsonObject.optString("phone");
		face = jsonObject.optString("user_img");
		vipInfo=new VipInfo(jsonObject, "user_level");
		goods_status=jsonObject.optInt("goods_status");
	}
	/** 是否收藏 */
	public int goods_status;
	/** 商品描述 */
	public String description;
	/** 类别id */
	public String class_id;
	/** 类别名 */
	public String class_name;
//	/** 地址id */
//	public String address_id;
//	/** 地区id */
//	public String area_id;
	/** 地区名字 */
	public String area_name;
	/** 详细地址 */
//	public String position;
	/** 用户id */
	public String user_id;
	public VipInfo vipInfo;
	/** 用户昵称 */
	public String alias;
	/** 用户号码 */
	public String phone;

	public String face;

}
