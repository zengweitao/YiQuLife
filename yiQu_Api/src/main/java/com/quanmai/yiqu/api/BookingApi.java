package com.quanmai.yiqu.api;

import android.content.Context;

import com.quanmai.yiqu.api.vo.BookingDetailInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbageListInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbagesInfo;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 95138 on 2016/4/19.
 */
public class BookingApi extends Api {
    private static BookingApi mInstance;

    public static BookingApi get() {
        if (mInstance == null) {
            mInstance = new BookingApi();
        }
        return mInstance;
    }
    /**
     * 8073 获取可回收垃圾列表
     *
     * */
    public void GetGarbageList(Context context,final ApiRequestListener<CommonList<RecycleGarbageListInfo>> listener){
        String paramStr = "type=8073";
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<RecycleGarbageListInfo> infoList = new CommonList<RecycleGarbageListInfo>();
                JSONArray jsonArrayGarbageList = jsonObject.optJSONArray("data");
                for (int i=0;i<jsonArrayGarbageList.length();i++){
                    RecycleGarbageListInfo infos = new RecycleGarbageListInfo();

                    JSONObject jsonObject1 = jsonArrayGarbageList.optJSONObject(i);
                    infos.setId(jsonObject1.optString("id"));
                    infos.setTypeName(jsonObject1.optString("typeName"));

                    JSONArray jsonArray = jsonObject1.optJSONArray("garbageList");
                    CommonList<RecycleGarbagesInfo> commonList = new CommonList<RecycleGarbagesInfo>();
                    for (int j=0;j<jsonArray.length();j++){
                        RecycleGarbagesInfo recycleGarbagesInfo = new RecycleGarbagesInfo(jsonArray.getJSONObject(j));
                        commonList.add(recycleGarbagesInfo);
                    }
                    infos.setGarbageList(commonList);
                    infoList.add(infos);
                }
                listener.onSuccess("", infoList);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8069 确认预约
     * @param point 积分
     * @param publisher 预约人
     * @param rangeDate 预约日期
     * @param rangeTime 预约时段
     * @param address 预约人详细地址
     * @param mobile 预约人手机号码
     * @param recycleDetails 回收废品的json字符串
     * */
    public void ConfirmBooking(Context context,String point,
                               String publisher,String rangeDate,
                               String rangeTime,String address,String mobile,String recycleDetails,final ApiRequestListener<String> listener){
        String paramStr = "type=8069"+"&point="+point+"&publisher="+publisher+"&rangeDate="+rangeDate+"&rangeTime="+rangeTime
                +"&address="+address+"&mobile="+mobile+"&recycleDetails="+recycleDetails;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess("",jsonObject.optString("id"));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8172简单预约信息
     *
     * @param publisher 预约人
     * @param rangeDate 预约日期
     * @param rangeTime 预约时段
     * @param address 预约人详细地址
     * @param mobile 预约人手机号码
     * @param description 回收废品的json字符串
     * */
    public void SimpleBooking(Context context, String publisher,String rangeDate,
                               String rangeTime,String address,String mobile,String description,
                              String images,final ApiRequestListener<String> listener){
        String paramStr = "type=8172"+"&publisher="+publisher+"&rangeDate="+rangeDate+"&rangeTime="+rangeTime
                +"&address="+address+"&mobile="+mobile+"&description="+description+"&images="+images;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess("",jsonObject.optString("id"));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8076 用户订单详细
     * @param orderid  //订单id
     * */
    public void GetGarbageStatus(Context context,String orderid,final ApiRequestListener<BookingDetailInfo> listener){
        String paramStr = "type=8076"+"&orderid="+orderid;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess("",new BookingDetailInfo(jsonObject));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8071 修改用户订单
     *
     * @param orderid  //订单id
     * @param point //积分
     * @param recycleDetails  //修改后的垃圾列表  json字符串形式
     * */
    public void ModifyBooking(Context context,String orderid,String point,String recycleDetails,final ApiRequestListener<String> listener){
        String paramStr = "type=8071"+"&orderid="+orderid+"&point="+point+"&recycleDetails="+recycleDetails;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess("","");
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8072 取消预约
     *
     * @param id  订单id
     * */
    public void CancelBookings(Context context,String id,String recycleId,final ApiRequestListener<String> listener){
        String paramStr;
        if (recycleId.equals("")||recycleId.equals(null)||recycleId.equals("null")){
            paramStr="type=8072&id="+id;
        }else {
            paramStr ="type=8072&id="+id +"&recycleId="+recycleId;
        }
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.optString("msg"),"");
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**g
     * 8075  查询用户预约列表
     *
     * @param page  //页数
     * @param statue  //订单状态 发布状态initial("初始"), verified("已确认"), recycle("已回收"), cancel("取消"), completed("已完成"),overdue("已过期");
     * */
    public void SearchBookingList(Context context,int page,String statue,final ApiRequestListener<CommonList<BookingDetailInfo>> listener){
        String paramStr;
        if (statue.equals("")||statue.equals(null)||statue.equals("null")){
            paramStr = "type=8075"+"&page="+page;
        }else {
            paramStr = "type=8075"+"&page="+page+"&statue="+statue;
        }
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<BookingDetailInfo> commonList = new CommonList<BookingDetailInfo>();
                if (jsonObject.has("current_page")) {
                    commonList.current_page = jsonObject.getInt("current_page");
                }
                if (jsonObject.has("max_page")) {
                    commonList.max_page = jsonObject.getInt("max_page");
                }
                if (jsonObject.has("orderList")) {
                    JSONArray array = jsonObject.getJSONArray("orderList");
                    for (int i = 0; i < array.length(); i++) {
                        commonList.add(new BookingDetailInfo(array.getJSONObject(i)));
                    }
                }
                listener.onSuccess("",commonList);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8079 删除预约记录
     *
     * @param orderid  订单id
     * */
    public void DeleteBookingRecord(Context context,String orderid,final ApiRequestListener<String> listener){
        String paramStr = "type=8079"+"&orderid="+orderid;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess("","");
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8078 预约状态查询
     *
     * @param orderid 订单id
     * @return
     * statue 发布状态initial("初始"), verified("已确认"), recycle("已回收"), cancel("取消"), completed("已完成"),overdue("已过期");
     * opeTime 操作时间
     * */
    public void SearchBookingStatus(Context context,String orderid,final ApiRequestListener<Map<String,String>> listener){
        String paramStr = "type=8078"+"&orderid="+orderid;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Map<String,String> data = new HashMap<String, String>();
                JSONArray jsonArray = jsonObject.optJSONArray("recordList");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    data.put(jsonObject1.optString("statue"),jsonObject1.optString("opeTime"));
                }
                listener.onSuccess("",data);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }
}
