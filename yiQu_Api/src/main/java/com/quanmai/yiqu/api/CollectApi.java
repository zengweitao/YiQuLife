package com.quanmai.yiqu.api;

import android.content.Context;

import com.quanmai.yiqu.api.vo.CollectionGoods;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by accelerate on 16/3/2.
 */
public class CollectApi extends ApiConfig {
	private static CollectApi mInstance;

	public static CollectApi get() {
		if (mInstance == null) {
			mInstance = new CollectApi();
		}
		return mInstance;
	}

	/**
	 * 8029收藏商品列表
	 *
	 * @param context
	 * @param page     页数
	 * @param listener
	 */
	public void GoodsCollectionList(Context context, int page,
									final ApiRequestListener<CommonList<CollectionGoods>> listener) {
		String paramStr = "type=8029&page=" + page;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<CollectionGoods> commonList = new CommonList<CollectionGoods>();
				if (jsonObject.has("current_page")) {
					commonList.current_page = jsonObject.getInt("current_page");
				}
				if (jsonObject.has("max_page")) {
					commonList.max_page = jsonObject.getInt("max_page");
				}
				if (jsonObject.has("goodsCollectList")) {
					JSONArray array = jsonObject
							.getJSONArray("goodsCollectList");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new CollectionGoods(array
								.getJSONObject(i)));
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
	 * 8030收藏商品
	 *
	 * @param context
	 * @param goods_id 商品id
	 * @param listener
	 */
	public void GoodsCollection(Context context, String goods_id,
								final ApiRequestListener<String> listener) {
		HttpPost(context, "type=8030&goods_id=" + goods_id, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.optString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}


	/**
	 * 8031取消收藏
	 *
	 * @param context
	 * @param goods_id 商品id
	 * @param listener
	 */

	public void GoodsCollectionCancel(Context context, String goods_id,
									  final ApiRequestListener<String> listener) {
		HttpPost(context, "type=8031&goods_id=" + goods_id, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.optString("msg"), "");
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}
}
