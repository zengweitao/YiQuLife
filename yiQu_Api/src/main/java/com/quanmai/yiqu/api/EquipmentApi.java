package com.quanmai.yiqu.api;

import android.content.Context;

import com.quanmai.yiqu.api.vo.BarCodeInfo;
import com.quanmai.yiqu.baidu.DeviceBean;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by accelerate on 16/3/2.
 */
public class EquipmentApi extends ApiConfig {
	private static EquipmentApi mInstance;

	public static EquipmentApi get() {
		if (mInstance == null) {
			mInstance = new EquipmentApi();
		}
		return mInstance;
	}


	/**
	 * 8045条形码获取
	 *
	 * @param context
	 * @param bar_code 条形码信息
	 * @param listener
	 */
	public void BarCode(Context context, String bar_code,
						final ApiRequestListener<BarCodeInfo> listener) {
		String paramStr = "type=8045&bar_code=" + bar_code;
		HttpPost(context, paramStr, new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.getString("msg"), new BarCodeInfo(jsonObject));

			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}


	/**
	 * 8046获取周围设备坐标
	 *
	 * @param context
	 * @param listener
	 */
	public void NearbyDevices(Context context,
							  final ApiRequestListener<CommonList<DeviceBean>> listener) {
		HttpPost(context, "type=8046", new HttpCallBack() {

			@Override
			public void onSuccess(JSONObject jsonObject) throws JSONException {
				listener.onSuccess(jsonObject.optString("msg"), DeviceBean
						.parseDeviceBean(jsonObject.optString("list")));
				// listener.onSuccess(
				// jsonObject.optString("msg"),
				// DeviceBean
				// .parseDeviceBean("[{\"location\":\"120,30\",\"name\":\"福二袋-2031\",\"address\":\"回龙观东区旺角龙锦三街北侧\"},{\"location\":\"120.01,30.1\",\"name\":\"福二袋-2031\",\"address\":\"回龙观东区旺角龙锦三街北侧\"}]"));
			}

			@Override
			public void onFailure(String msg) {
				listener.onFailure(msg);
			}
		});
	}
}
