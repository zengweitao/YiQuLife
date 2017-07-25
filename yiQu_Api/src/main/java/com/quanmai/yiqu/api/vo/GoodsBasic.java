package com.quanmai.yiqu.api.vo;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.quanmai.yiqu.common.util.RelativeDateFormat;

public class GoodsBasic {

	public String id;             //商品id
	public String name;           //商品名
	public int degree;            //商品成色
	public double price;          //商品价格
	public int type;              //商品类型 0-卖，1-捐赠
	public ArrayList<String> img; //商品图片
	public int collection_count;  //收藏数
	public int comment_count;     //评论数
	public int accesses_count;    //访问数
	public String release_time;   //发布时间
	public int goods_status;      //商品状态 0-开启，1-关闭
	public int verifyflag;        //审核状态 0-未审核，1-审核通过，2-审核未通过

	public GoodsBasic(JSONObject jsonObject) throws JSONException {
		id = jsonObject.getString("goods_id");
		name = jsonObject.getString("goods_name");
		degree = jsonObject.getInt("goods_degree");
		price = jsonObject.getDouble("goods_price");
		type = jsonObject.getInt("goods_type");
		String[] object = jsonObject.getString("goods_img").split(",");
		img = new ArrayList<String>();
		for (int i = 0; i < object.length; i++) {
			img.add(object[i]);
		}
		collection_count = jsonObject.getInt("collection_count");
		comment_count = jsonObject.getInt("comment_count");
		accesses_count = jsonObject.getInt("accesses_count");
		release_time = RelativeDateFormat.format(jsonObject
				.getString("release_time"));
		goods_status = jsonObject.optInt("goods_status");
		verifyflag = jsonObject.optInt("verifyflag");
		if (collection_count < 0) {
			collection_count = 0;
		}
		if (comment_count < 0) {
			comment_count = 0;
		}
		if (accesses_count < 0) {
			accesses_count = 0;
		}

	}

}
