package com.quanmai.yiqu.api;

import android.content.Context;

import com.quanmai.yiqu.api.vo.FixClass;
import com.quanmai.yiqu.api.vo.FixInfo;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by accelerate on 16/3/2.
 */
public class RepairsApi extends ApiConfig {
	private static RepairsApi mInstance;

	public static RepairsApi get() {
		if (mInstance == null) {
			mInstance = new RepairsApi();
		}
		return mInstance;
	}

	/**
	 * 8019 报修提交
	 *
	 * @param context
	 * @param fixInfo  保修信息model
	 * @param listener
	 */
	public void FixCommit(Context context, FixInfo fixInfo,
						  final ApiRequestListener<String> listener) {
		String paramStr = "type=8019" + "&address_id=" + fixInfo.address_id
				+ "&come_time=" + fixInfo.come_time + "&problem_id="
				+ fixInfo.problem_id + "&problem=" + fixInfo.problem
				+ "&description=" + fixInfo.description + "&describeimg="
				+ fixInfo.picurl;
		HttpPost(context, paramStr, new HttpCallBack() {

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

	/**
	 * 8020报修记录
	 *
	 * @param context
	 * @param page     页数
	 * @param listener
	 */
	public void FixRecords(Context context, int page,
						   final ApiRequestListener<CommonList<FixInfo>> listener) {
		String paramStr = "type=8020&page=" + page;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<FixInfo> commonList = new CommonList<FixInfo>();
				commonList.current_page = jsonObject.getInt("current_page");
				commonList.max_page = jsonObject.getInt("max_page");
				if (jsonObject.has("list")) {
					JSONArray array = jsonObject.getJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new FixInfo(array.getJSONObject(i)));
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
	 * 8043管理员报修查看列表
	 *
	 * @param context
	 * @param page      页数
	 * @param classcode 分类编码
	 * @param listener
	 */
	public void FixManageList(Context context, int page, String classcode,
							  final ApiRequestListener<CommonList<FixInfo>> listener) {
		String paramStr = "type=8043&page=" + page + "&classcode=" + classcode;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<FixInfo> commonList = new CommonList<FixInfo>();
				commonList.current_page = jsonObject.getInt("current_page");
				commonList.max_page = jsonObject.getInt("max_page");
				if (jsonObject.has("list")) {
					JSONArray array = jsonObject.getJSONArray("list");
					for (int i = 0; i < array.length(); i++) {
						commonList.add(new FixInfo(array.getJSONObject(i)));
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
	 * 8044管理员报修详情获取
	 *
	 * @param context
	 * @param service_id String
	 * @param listener
	 */
	public void FixManageDetail(Context context, String service_id,
								final ApiRequestListener<FixInfo> listener) {
		String paramStr = "type=8044&service_id=" + service_id;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), new FixInfo(
						jsonObject));
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}


	/**
	 * 8052物业报修分类（管理员）
	 *
	 * @param context
	 * @param listener
	 */
	public void FixManagerClass(Context context,
								final ApiRequestListener<CommonList<FixClass>> listener) {
		String paramStr = "type=8052";
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<FixClass> commonList = new CommonList<FixClass>();
				if (jsonObject.getString("status").equals("1")) {
					if (jsonObject.has("current_page")) {
						commonList.current_page = jsonObject
								.getInt("current_page");
					}
					if (jsonObject.has("max_page")) {
						commonList.max_page = jsonObject.getInt("max_page");
					}
					if (jsonObject.has("classlist")) {
						JSONArray array = jsonObject.getJSONArray("classlist");
						for (int i = 0; i < array.length(); i++) {
							commonList.add(new FixClass(array.getJSONObject(i)));
						}
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
	 * 8053物业报修分类(用户)
	 *
	 * @param context
	 * @param listener
	 */
	public void FixUserClass(Context context,
							 final ApiRequestListener<CommonList<FixClass>> listener) {
		String paramStr = "type=8053";
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				CommonList<FixClass> commonList = new CommonList<FixClass>();
				if (jsonObject.getString("status").equals("1")) {
					if (jsonObject.has("current_page")) {
						commonList.current_page = jsonObject
								.getInt("current_page");
					}
					if (jsonObject.has("max_page")) {
						commonList.max_page = jsonObject.getInt("max_page");
					}
					if (jsonObject.has("list")) {
						JSONArray array = jsonObject.getJSONArray("list");
						for (int i = 0; i < array.length(); i++) {
							commonList.add(new FixClass(array.getJSONObject(i)));
						}
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
	 * 57.	物业报修处理
	 *
	 * @param context
	 * @param service_id 报修id
	 * @param listener
	 */
	public void FixProcessing(Context context, String service_id, final ApiRequestListener<String> listener) {
		String paramStr = "type=8056&&service_id=" + service_id;
		HttpPost(context, paramStr, new HttpCallBack() {
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
