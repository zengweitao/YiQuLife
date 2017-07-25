package com.quanmai.yiqu.api;

import android.content.Context;

import com.quanmai.yiqu.api.vo.CommentInfo;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by accelerate on 16/3/2.
 */
public class CommentApi extends ApiConfig {

	private static CommentApi mInstance;

	public static CommentApi get() {
		if (mInstance == null) {
			mInstance = new CommentApi();
		}
		return mInstance;
	}


	/**
	 * 8027商品评论
	 *
	 * @param context
	 * @param page     页数
	 * @param goods_id 商品id
	 * @param listener
	 */
	public void GoodsCommentList(Context context, int page, String goods_id,
								 final ApiConfig.ApiRequestListener<CommonList<CommentInfo>> listener) {
		String paramStr = "type=8027&page=" + page + "&goods_id=" + goods_id;
		HttpPost(context, paramStr, new ApiConfig.HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<CommentInfo> commonList = new CommonList<CommentInfo>();
				if (jsonObject.has("total")){
					commonList.total = jsonObject.optInt("total");
				}
				if (jsonObject.has("current_page")) {
					commonList.current_page = jsonObject.getInt("current_page");
				}
				if (jsonObject.has("max_page")) {
					commonList.max_page = jsonObject.getInt("max_page");
				}
				if (jsonObject.has("comments")) {
					JSONArray array = jsonObject.getJSONArray("comments");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new CommentInfo(array.getJSONObject(i)));
					}
				}
				listener.onSuccess(jsonObject.getString("msg"), commonList);
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}


	/**
	 * 8028发布评论
	 *
	 * @param context
	 * @param goods_id        商品id
	 * @param comment_content 评论内容
	 * @param customerid      发布人id
	 * @param goods_name      商品名字
	 * @param listener
	 */
	public void GoodsComment(Context context, String goods_id,
							 String comment_content, String customerid,String goods_name,final ApiConfig.ApiRequestListener<String> listener) {
		String paramStr = "type=8028&goods_id=" + goods_id
				+ "&comment_content=" + comment_content+"&customerid="+customerid+"&goods_name="+goods_name;
		HttpPost(context, paramStr, new ApiConfig.HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), "");

			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}
}
