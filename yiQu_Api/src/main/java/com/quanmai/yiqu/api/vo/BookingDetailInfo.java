package com.quanmai.yiqu.api.vo;

import android.text.TextUtils;
import com.quanmai.yiqu.base.CommonList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 95138 on 2016/4/20.
 */
public class BookingDetailInfo implements Serializable {
    public String address;  //预约人地址
    public String id;
    public String mobile; //预约人手机号码
    public String point;    //预计能获得的积分
    public String publishTime; //发布时间
    public String publisher; //预约人
    public String rangeDate; //预约日期
    public String rangeTime; //预约时段
    public String recycleId; //回收id
    public String recycleName; //回收人名
    public String recycleTime;
    public String userId;
    public String statue; //订单状态  发布状态initial("初始"), verified("已确认"), recycle("已回收"), cancel("取消"), completed("已完成"),overdue("已过期");
    public CommonList<GarbageDetailsInfo> garbageDetailsInfos;
    public String memo;  //预约回收记录列表的垃圾详情字段
    public String pic; //预约回收记录列表的第一张图片


    public BookingDetailInfo(JSONObject jsonObject) throws JSONException{
        address = jsonObject.optString("address");
        id = jsonObject.optString("id");
        mobile = jsonObject.optString("mobile");
        point = jsonObject.optString("point");
        publishTime = jsonObject.optString("publishTime");
        publisher = jsonObject.optString("publisher");
        rangeDate = jsonObject.optString("rangeDate");
        rangeTime = jsonObject.optString("rangeTime");
        recycleId = jsonObject.optString("recycleId");
        recycleName = jsonObject.optString("recycleName");
        recycleTime = jsonObject.optString("recycleTime");
        userId = jsonObject.optString("userId");
        statue = jsonObject.optString("statue");
        garbageDetailsInfos = new CommonList<>();
        if (jsonObject.has("recycleDetails")){
            JSONArray jsonArray = jsonObject.optJSONArray("recycleDetails");
            for (int i=0;i<jsonArray.length();i++){
                garbageDetailsInfos.add(new GarbageDetailsInfo(jsonArray.getJSONObject(i)));
            }
        }

        if (jsonObject.has("memo")){
            memo = jsonObject.optString("memo");
        }

        if (jsonObject.has("pic")){
            pic = jsonObject.optString("pic");
        }

        if(!TextUtils.isEmpty(recycleTime)){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date= format.parse(recycleTime);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
                recycleTime = simpleDateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

}
