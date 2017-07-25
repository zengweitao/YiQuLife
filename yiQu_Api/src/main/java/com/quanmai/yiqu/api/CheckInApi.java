package com.quanmai.yiqu.api;

import android.content.Context;

import com.quanmai.yiqu.api.vo.SignData;
import com.quanmai.yiqu.ui.mys.record.ManHour;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by accelerate on 16/3/2.
 */
public class CheckInApi extends ApiConfig {
    private static CheckInApi mInstance;

    public static CheckInApi get() {
        if (mInstance == null) {
            mInstance = new CheckInApi();
        }
        return mInstance;
    }

    /**
     * 8010 签到
     * @param context
     * @param listener
     */
    public void Sign(final Context context, final ApiRequestListener<SignData> listener) {
        String paramStr = "type=8010";
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"), new SignData(jsonObject));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8011 签到记录查询
     * @param context
     * @param year    年
     * @param month    月
     * @param listener
     */
    public void SignRecord(final Context context, final int year,
                           final int month, final ApiRequestListener<ManHour> listener) {
        // Calendar now = Calendar.getInstance();
        String paramStr = "type=8011&year=" + year + "&month=" + month;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                String signpoint="5";
                if(jsonObject.has("signpoint"))
                {
                    signpoint=jsonObject.getString("signpoint");
                }
                if (jsonObject.has("times")) {
                    ManHour manHour = new ManHour(jsonObject
                            .getJSONArray("times"));

                    listener.onSuccess(jsonObject.getString("msg") + "#"
                            + jsonObject.getString("signdays") + "#"
                            + jsonObject.getString("totaldays")+"#"+signpoint, manHour);
                } else {
                    listener.onSuccess(jsonObject.getString("msg") + "#"
                            + jsonObject.getString("signdays") + "#"
                            + jsonObject.getString("totaldays")+"#"+signpoint, new ManHour(
                            year, month));
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }
}
