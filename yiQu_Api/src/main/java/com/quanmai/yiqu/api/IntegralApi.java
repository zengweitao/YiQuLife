package com.quanmai.yiqu.api;

import android.content.Context;

import com.google.gson.Gson;
import com.quanmai.yiqu.api.vo.ActivityIntegrationInfo;
import com.quanmai.yiqu.api.vo.AppointedUserInfo;
import com.quanmai.yiqu.api.vo.AwardRecordInfo;
import com.quanmai.yiqu.api.vo.BagInfo;
import com.quanmai.yiqu.api.vo.Member;
import com.quanmai.yiqu.api.vo.ScoreMonthRecord;
import com.quanmai.yiqu.api.vo.ScorePayInfo;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by accelerate on 16/3/2.
 */
public class IntegralApi extends ApiConfig {
    private static IntegralApi mInstance;

    public static IntegralApi get() {
        if (mInstance == null) {
            mInstance = new IntegralApi();
        }
        return mInstance;
    }

    /**
     * 8016 获取积分商品
     * @param context
     * @param Terminalno 终端号
     * @param listener
     */
    public void QRCode(Context context, String Terminalno, final ApiRequestListener<BagInfo> listener) {
        String paramStr = "type=8016&terminalno=" + Terminalno;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"), new BagInfo(
                        jsonObject));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8017 积分商品支付
     *
     * @param context
     * @param Terminalno  终端号
     * @param count       数量
     * @param abscription 归属系统（1：环保袋 2：纸要你）
     * @param listener
     */
    public void ScorePay(Context context, String Terminalno, int count, String abscription,
                         final ApiRequestListener<ScorePayInfo> listener) {
        String paramStr = "type=8017&count=" + count + "&terminalno="
                + Terminalno;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"), new ScorePayInfo(jsonObject));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8017 积分商品支付
     *
     * @param context
     * @param Terminalno  终端号
     * @param count       数量
     * @param abscription 归属系统（1：环保袋 2：纸要你）
     * @param consumetype 扣除积分类型（0：默认积分和福袋 1：仅积分）
     * @param listener
     */
    public void ScorePay(Context context, String Terminalno, int count, String abscription, String consumetype,
                         final ApiRequestListener<ScorePayInfo> listener) {
        String paramStr = "type=8017&count=" + count + "&terminalno="
                + Terminalno + "&consumetype=" + consumetype;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"),
                        new ScorePayInfo(jsonObject));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8018 获取积分记录
     *
     * @param context
     * @param page      页数
     * @param pointtype 1 益币  2 积分  3 福袋
     * @param listener
     */
    public void ScoreRecord(Context context, int page,int pointtype,String queryInfo,
                            final ApiRequestListener<CommonList<ScoreMonthRecord>> listener) {
        String paramStr = "type=8018&pointtype="+pointtype+"&page=" + page;
        if(queryInfo!=null&&queryInfo!=""&&queryInfo!="null"){
            paramStr+="&queryInfo="+queryInfo;
        }
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<ScoreMonthRecord> commonList = new CommonList<ScoreMonthRecord>();
                if (jsonObject.has("current_page")) {
                    commonList.current_page = jsonObject.getInt("current_page");
                }
                if (jsonObject.has("max_page")) {
                    commonList.max_page = jsonObject.getInt("max_page");
                }
                if (jsonObject.has("list")) {
                    JSONArray array = jsonObject.getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        commonList.add(new ScoreMonthRecord(array
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
     * 8051分享送积分
     *
     * @param context
     * @param listener
     */
    public void ShareSuccess(Context context,
                             final ApiRequestListener<String> listener) {
        String paramStr = "type=8051";
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
     * 56.	领取积分
     *
     * @param context
     * @param listener
     */
    public void ReceiveScore(Context context, final ApiRequestListener<String> listener) {
        String paramStr = "type=8044";
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
     * 8067 会员等级说明
     *
     * @param context
     * @param listener
     */
    public void getVipIntroduce(Context context, final ApiRequestListener<CommonList<Member>> listener) {
        String paramStr = "type=8067";
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<Member> commonList = new CommonList<Member>();
                if (jsonObject.has("members") && jsonObject.optJSONArray("members").length() > 0) {
                    JSONArray jsonArray = jsonObject.optJSONArray("members");
                    for (int i = 0; i < jsonObject.optJSONArray("members").length(); i++) {
                        Member member = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), Member.class);
                        commonList.add(member);
                    }
                }
                listener.onSuccess(jsonObject.optString("msg"), commonList);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 80083 获取积分赠送类型
     */
    public void getIntegrationGiveType(Context context, final ApiRequestListener<CommonList<ActivityIntegrationInfo>> listener) {
        String paramStr = "type=8083";
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<ActivityIntegrationInfo> commonList = new CommonList<ActivityIntegrationInfo>();
                if (jsonObject.has("activityList")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("activityList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ActivityIntegrationInfo activityIntegrationInfo = new ActivityIntegrationInfo(jsonArray.getJSONObject(i));
                        commonList.add(activityIntegrationInfo);
                    }
                }
                listener.onSuccess("", commonList);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8080生成二维码
     *
     * @param activityId 活动类型
     * @param points     积分数量
     */
    public void makeQRCode(Context context, String activityId, String points, final ApiRequestListener<String> listener) {
        String param = "type=8080" + "&pointType=" + activityId + "&points=" + points;
        HttpPost(context, param, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.optString("pointRecordId"), jsonObject.optString("imgData"));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8082 积分领取
     *
     * @param context
     * @param hash
     * @param pointRecordId
     * @param activityId
     * @param defaultPoint
     * @param listener
     */
    public void getIntegrationGive(Context context, String hash, String pointRecordId, String activityId, String defaultPoint,
                                   final ApiRequestListener<String> listener) {
        String parsmStr = "type=8082&hash=" + hash + "&pointRecordId=" + pointRecordId + "&activityId=" + activityId + "&defaultPoint=" + defaultPoint;
        HttpPost(context, parsmStr, new HttpCallBack() {
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
     * 8210益币换奖品记录查询（赠送记录接口通用）
     *
     * @param context
     * @param page
     * @param houseid  指定用户户号(可选传)
     *                 不传为用户自身手机号查询记录
     *                 传了完整的户号则用户号查询该户号下所有记录
     *                 传不完整户号则是户号模糊查询
     * @param page     当前页
     * @param listener
     */
    public void getAwardRecord(Context context, int page, String houseid,
                               final ApiRequestListener<CommonList<AwardRecordInfo>> listener) {
        String parsmStr = "type=8210&page=" + page + "&houseid=" + houseid;
        HttpPost(context, parsmStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<AwardRecordInfo> commonList = new CommonList<AwardRecordInfo>();

                if (jsonObject.has("current_page")) {
                    commonList.current_page = jsonObject.getInt("current_page");
                }
                if (jsonObject.has("max_page")) {
                    commonList.max_page = jsonObject.getInt("max_page");
                }

                if (jsonObject.has("arList") && jsonObject.optJSONArray("arList").length() > 0) {
                    JSONArray array = jsonObject.optJSONArray("arList");
                    for (int i = 0; i < array.length(); i++) {
                        AwardRecordInfo info = new Gson().fromJson(array.optJSONObject(i).toString(), AwardRecordInfo.class);
                        commonList.add(info);
                    }
                }
                listener.onSuccess(jsonObject.optString("msg"), commonList);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });

    }

    /**
     * 8209赠送益币接口
     * @param context
     * @param phone
     * @param amount
     * @param listener
     */
    public void getGiveYcoin(Context context, String phone, String amount, String giftType ,final ApiRequestListener<String> listener) {
        String parsmStr = "type=8209&phone=" + phone + "&amount=" + amount + "&giftType=" + giftType;
        HttpPost(context, parsmStr, new HttpCallBack() {
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
     * 8213 积分成长说明接口
     *
     * @param context
     * @param os
     * @param listener
     */
    public void getGrowIntegral(Context context, String os, final ApiRequestListener<Map<String, String>> listener) {
        String strParam = "type=8213&os=" + os;
        HttpPost(context, strParam, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Map<String, String> strMap = new HashMap<String, String>();
                if (jsonObject.has("arList")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("arList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject childObject = jsonArray.getJSONObject(i);
                        String explanation = childObject.optString("explanation");
                        String title = childObject.optString("title");
                        strMap.put(title, explanation);
                    }
                    listener.onSuccess(jsonObject.optString("msg"), strMap);
                } else {
                    listener.onFailure(jsonObject.optString("msg"));
                    return;
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8211奖品清单查询
     *
     * @param context
     * @param page
     * @param listener
     */
    public void getGiftList(Context context, int page, final ApiRequestListener<CommonList<AwardRecordInfo>> listener) {
        String strParam = "type=8211&page=" + page;
        HttpPost(context, strParam, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<AwardRecordInfo> recordInfoList = new CommonList<AwardRecordInfo>();
                if (jsonObject.has("maxPage")){
                    recordInfoList.max_page=jsonObject.optInt("maxPage");
                }
                if (jsonObject.has("arList")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("arList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        AwardRecordInfo awardRecordInfo = new Gson().fromJson(jsonArray.get(i).toString(), AwardRecordInfo.class);
                        recordInfoList.add(awardRecordInfo);
                    }
                }
                listener.onSuccess(jsonObject.optString("msg"), recordInfoList);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8212 礼品发放接口
     *
     * @param context
     * @param operator  发放人员手机号
     * @param giftId    礼品ID。后续需求变更如果多选，传"1,2,5,6,7"格式，后台做解析
     * @param nums      数量。后续需求变更如果多选，传"1,2,5,6,7"格式，后台做解析
     * @param houseCode 户号编码
     * @param account   用户手机
     * @param listener
     */
    public void giftGiving(Context context, String operator, String giftId, String nums, String houseCode, String account, final ApiRequestListener<String> listener) {
        String strParam = "type=8212&operator=" + operator + "&giftId=" + giftId + "&nums=" + nums
                + "&houseCode=" + houseCode + "&account=" + account;
        HttpPost(context, strParam, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.optString("msg"), jsonObject.optString("msg"));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8214指定用户信息查询接口
     *
     * @param context
     * @param houseCode 户号（加密过的），选填，户号手机号二选一
     * @param account   手机号，选填
     * @param listener
     */
    public void appointedUserInfo(Context context, String houseCode, String account, final ApiRequestListener<AppointedUserInfo> listener) {
        String paramStr = "type=8214&houseCode=" + houseCode + "&account=" + account;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                AppointedUserInfo info = new Gson().fromJson(jsonObject.toString(), AppointedUserInfo.class);
                if (info == null) {
                    listener.onFailure(jsonObject.optString("msg"));
                } else {
                    listener.onSuccess(jsonObject.optString("msg"), info);
                }

            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });

    }

    /**
     * 8215现场垃圾回收接口
     *
     * @param context
     * @param point          积分
     * @param publisher      指定用户名（垃圾提供者）
     * @param address        地址
     * @param mobile         指定用户手机号
     * @param recycleDetails 回收垃圾（json字符串：[{},{}]），包含如下字段：
     *                       garbage	回收物品名
     *                       quantity	回收数量
     *                       point	积分
     *                       pic	图片
     * @param listener
     */
    public void recycleLocal(Context context, String point, String publisher, String address, String mobile,
                             String recycleDetails,String orderId, final ApiRequestListener<String> listener) {
        String paramStr;
        if (orderId.equals("")||orderId.equals(null)||orderId.equals("null")){
            paramStr="type=8215&point=" + point + "&publisher=" + publisher + "&address=" + address
                    + "&mobile=" + mobile + "&recycleDetails=" + recycleDetails;
        }else {
            paramStr="type=8215&point=" + point + "&publisher=" + publisher + "&address=" + address
                    + "&mobile=" + mobile + "&recycleDetails=" + recycleDetails+"&orderId="+orderId;
        }
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.optString("msg"), jsonObject.optString("msg"));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

}
