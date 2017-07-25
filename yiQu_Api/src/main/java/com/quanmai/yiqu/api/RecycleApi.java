package com.quanmai.yiqu.api;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.GarbageClassifyInfo;
import com.quanmai.yiqu.api.vo.GarbageThrowRecordInfo;
import com.quanmai.yiqu.api.vo.RecycleOrderInfo;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanjinj on 16/4/20.
 */
public class RecycleApi extends ApiConfig {
    private static RecycleApi mInstance;

    public static RecycleApi get() {

        if (mInstance == null) {
            mInstance = new RecycleApi();
        }

        return mInstance;
    }


    //订单状态：initial；取消：cancel；已确认：verified；已回收：recycle；
//    public enum orderStatus {
//        CANCEL("cancel"), COMPLETED("completed"), INITIAL("initial"), RECYCLE("recycle"), VERIFIED("verified");
//
//        private String mString;
//
//        orderStatus(String string) {
//            this.mString = string;
//        }
//
//        @Override
//        public String toString() {
//            return mString;
//        }
//    }

    public enum orderStatus {
        cancel("已取消", R.color.text_color_808080),
        completed("已完成", R.color.text_color_5397e7),
        initial("初始", R.color.text_color_default),
        recycle("已回收", R.color.text_color_5397e7),
        verified("已确认", R.color.text_color_default),
        overdue("未回收", R.color.text_color_fe5236);
        private String mName;
        private int mColor;

        orderStatus(String name, int color) {
            this.mName = name;
            this.mColor = color;
        }


        public int getColor() {
            return mColor;
        }

        public String getStrName() {
            return mName;
        }
    }

    /**
     * 8070 回收人员接单(预约回收)
     *
     * @param context
     * @param orderid  订单id
     * @param listener
     */
    public void confirmAppointmentOrder(Context context, String orderid, String userid, final ApiRequestListener<String> listener) {
        String strParams = "type=8070&orderid=" + orderid + "&userid=" + userid;
        HttpPost(context, strParams, new HttpCallBack() {
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
     * 8074 物品回收订单确认
     *
     * @param context
     * @param orderid        订单id
     * @param point          积分
     * @param recycleDetails 回收垃圾json字符串 recycleDetails=[{id:"",garbage:"",quantity:""},{}]
     * @param listener
     */
    public void confirmRecycleOrder(Context context, String orderid, String userid, String point, String recycleDetails, final ApiRequestListener<String> listener) {
        String strParams = "type=8074&orderid=" + orderid + "&userid=" + userid + "&point=" + point + "&recycleDetails=" + recycleDetails;
        HttpPost(context, strParams, new HttpCallBack() {
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
     * 8075 用户订单列表查询
     *
     * @param context
     * @param page
     * @param status   initial("初始"), verified("已确认"), recycle("已回收"), cancel("取消"), completed("已完成"),overdue("已过期");
     * @param listener
     */
    public void getRecycleOrderList(Context context, int page, String status,
                                    final ApiRequestListener<CommonList<RecycleOrderInfo>> listener) {
        String strParams = "type=8075&page=" + page + "&statue=" + status;
        HttpPost(context, strParams, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        CommonList<RecycleOrderInfo> list = new CommonList<RecycleOrderInfo>();

                        if (jsonObject.optInt("status") != 1) {
                            listener.onSuccess(jsonObject.optString("msg"), null);
                            return;
                        }

                        JSONArray jsonArray = jsonObject.optJSONArray("orderList");
                        list.max_page = jsonObject.optInt("max_page");
                        list.current_page = jsonObject.optInt("current_page");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(new RecycleOrderInfo().get(jsonArray.getJSONObject(i)));
                            }
                        }

                        listener.onSuccess(jsonObject.optString("msg"), list);
                    }

                    @Override
                    public void onFailure(String msg) {
                        listener.onFailure(msg);
                    }
                }
        );
    }


    /**
     * 8076 用户订单详细(普通用户、回收人员通用)
     *
     * @param context
     * @param orderid  //订单id
     * @param listener
     */
    public void getRecycleOrderDetail(Context context, String orderid,
                                      final ApiRequestListener<RecycleOrderInfo> listener) {
        String strParams = "type=8076&orderid=" + orderid;
        HttpPost(context, strParams, new HttpCallBack() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {

                        if (jsonObject.optInt("status") != 1) {
                            listener.onSuccess(jsonObject.optString("msg"), null);
                            return;
                        }

                        RecycleOrderInfo orderInfo = new RecycleOrderInfo().get(jsonObject);
                        listener.onSuccess(jsonObject.optString("msg"), orderInfo);
                    }

                    @Override
                    public void onFailure(String msg) {
                        listener.onFailure(msg);
                    }
                }

        );
    }


    /**
     * 8077订单数量统计
     *
     * @param context
     */
    public void getRecycleOrderNum(Context context, final ApiRequestListener<Map<String, Integer>> listener) {
        String strParams = "type=8077";
        HttpPost(context, strParams, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Map<String, Integer> map = new HashMap<String, Integer>();

                if (jsonObject == null) {
                    listener.onSuccess("", map);
                } else if (jsonObject.optJSONArray("countList") != null && jsonObject.optString("countList").length() > 0) {
                    JSONArray jsonArray = jsonObject.optJSONArray("countList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        map.put(object.optString("statue"), object.optInt("quantity"));
                    }
                    listener.onSuccess(jsonObject.optString("msg"), map);
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }


    /**
     * 8168获取可回收垃圾分类信息
     * @garbageclass 0是可回收箱（四门）投放，1是普通分类箱投放（厨余和其他）
     *
     * @param context
     * @param listener
     */
    public void getRecyclerGarbageClassifyList(Context context, String garbageclass , final ApiRequestListener<List<GarbageClassifyInfo>> listener) {
        String strParams = "type=8168&garbageclass="+garbageclass;

        HttpPost(context, strParams, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                List<GarbageClassifyInfo> garbageInfos = new ArrayList<GarbageClassifyInfo>();

                JSONArray jsonArray = jsonObject.optJSONArray("recyList");
                if (jsonArray == null || jsonArray.length() <= 0) {
                    listener.onFailure(jsonObject.optString("msg"));
                    return;
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    GarbageClassifyInfo info = new Gson().fromJson(jsonArray.getJSONObject(i).toString(),
                            GarbageClassifyInfo.class);
                    garbageInfos.add(info);
                }
                listener.onSuccess(jsonObject.optString("msg"), garbageInfos);

            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8169后台生成可回收垃圾编码
     *
     * @param context
     * @param bagtype       编码
     * @param garbagname    名称
     * @param listener
     */
    public void getRecyclerGarbageCode(Context context, String bagtype, String garbagname,
                                       final ApiRequestListener<String> listener) {
        String strParams = "type=8169&bagtype="+bagtype+"&garbagname="+garbagname;
        HttpPost(context, strParams, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                String strCode=jsonObject.optString("code");
                if (TextUtils.isEmpty(strCode)){
                    listener.onFailure(jsonObject.optString("msg"));
                    return;
                }

                listener.onSuccess(jsonObject.optString("msg"),strCode);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8170获取用户可回收垃圾投放记录
     *
     * @param context
     * @param page
     * @param listener
     */
    public void getThrowRecordList(Context context,String garbageclass ,int page,final ApiRequestListener<CommonList<GarbageThrowRecordInfo>> listener) {
        String strParams = "type=8170&garbageclass="+garbageclass+"&page="+page;
        HttpPost(context, strParams, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<GarbageThrowRecordInfo> garbageInfos = new CommonList<GarbageThrowRecordInfo>();

                JSONArray jsonArray = jsonObject.optJSONArray("garbageList");
                if (jsonArray == null || jsonArray.length() <= 0) {
                    listener.onFailure(jsonObject.optString("msg"));
                    return;
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    GarbageThrowRecordInfo info = new Gson().fromJson(jsonArray.getJSONObject(i).toString(),
                            GarbageThrowRecordInfo.class);
                    garbageInfos.add(info);
                }
                listener.onSuccess(jsonObject.optString("msg"), garbageInfos);

            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }





}
