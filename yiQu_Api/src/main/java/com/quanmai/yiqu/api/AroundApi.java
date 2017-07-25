package com.quanmai.yiqu.api;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.quanmai.yiqu.api.vo.BannerTwoInfo;
import com.quanmai.yiqu.api.vo.CouponInfo;
import com.quanmai.yiqu.api.vo.NewsInfo;
import com.quanmai.yiqu.api.vo.NewsListInfo;
import com.quanmai.yiqu.api.vo.ShopClassInfo;
import com.quanmai.yiqu.api.vo.ShopInfo;
import com.quanmai.yiqu.api.vo.ShopListInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.common.util.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 周边
 * Created by zhanjinj on 16/5/27.
 */
public class AroundApi extends ApiConfig {
    private AroundApi() {

    }

    private static class AroundInstanceApi {
        private static AroundApi instance = new AroundApi();
    }

    public static AroundApi getInstance() {
        return AroundInstanceApi.instance;
    }

    /**
     * 8084 banner管理
     *
     * @param context
     * @param listener
     */
    public void GetAroundBanner(Context context, final ApiRequestListener<BannerTwoInfo> listener) {
        String paramStr = "type=8084";
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                BannerTwoInfo bannerTwoInfo = new Gson().fromJson(jsonObject.toString(), BannerTwoInfo.class);
                String msg = jsonObject.optString("msg");
                listener.onSuccess(msg, bannerTwoInfo);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8085 商户信息列表
     *
     * @param context
     * @param shopType 商户类型参数（可不传：获取所有数据）
     * @param listener
     */
    public void getShopInfoList(Context context, String shopType, final ApiRequestListener<ShopListInfo> listener) {
        String paramStr = "type=8085&&shopType=" + shopType;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                ShopListInfo shopListInfo = new Gson().fromJson(jsonObject.toString(), ShopListInfo.class);
                String msg = jsonObject.optString("msg");
                listener.onSuccess(msg, shopListInfo);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8086 商户优惠券列表
     *
     * @param context
     * @param shopId
     * @param page
     */
    public void getShopCouponList(Context context, String shopId, int page, final ApiRequestListener<CommonList<CouponInfo>> listener) {
        String paramStr = "type=8086&shopId=" + shopId + "&page=" + page;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<CouponInfo> commonList = new CommonList<CouponInfo>();
                if (jsonObject.has("current_page")) {
                    commonList.current_page = jsonObject.getInt("current_page");
                }
                if (jsonObject.has("max_page")) {
                    commonList.max_page = jsonObject.getInt("max_page");
                }
                if (jsonObject.has("infoList")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("infoList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        CouponInfo couponInfo = new Gson().fromJson(jsonArray.optJSONObject(i).toString(), CouponInfo.class);
                        commonList.add(couponInfo);
                    }
                    listener.onSuccess("", commonList);
                }

            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8094资讯信息列表
     *
     * @param context
     * @param page
     * @param listener
     */
    public void getNewsInfoList(Context context, String condition, int page, final ApiRequestListener<NewsListInfo> listener) {
        String paramStr = "";
        if (TextUtils.isEmpty(condition)) {
            paramStr = "type=8094&page=" + page;
        } else {
            paramStr = "type=8094&condition=" + condition + "&page=" + page;
        }

        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                NewsListInfo newsListInfo = new Gson().fromJson(jsonObject.toString(), NewsListInfo.class);
                String msg = jsonObject.optString("msg");
                listener.onSuccess(msg, newsListInfo);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });

    }

    /**
     * 8087 摇一摇获得积分/优惠券
     */
    public void GetSuperiseByShake(final Context context, final String flag, final ApiRequestListener<CouponInfo> listener) {
        String paramStr = "";
        if (!TextUtils.isEmpty(flag)) {
            paramStr = "type=8087&&flag=1";
        } else {
            paramStr = "type=8087";
        }

        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                String str = String.valueOf(SPUtils.get(context, "sysTime", ""));
                if (!TextUtils.isEmpty(str)) {
                    //数据清零
                    if (DateUtil.DateCompare(jsonObject.optString("sysTime"), str)) {
                        SPUtils.put(context, "fetchTimes", 0);
                    }
                }
                //记录时间跟最大次数
                SPUtils.put(context, "maxTimes", jsonObject.optInt("times"));
                SPUtils.put(context, "sysTime", jsonObject.optString("sysTime"));

                if (jsonObject.optJSONObject("couponInfo").has("shopid")) {
                    CouponInfo couponInfo = new Gson().fromJson(jsonObject.optJSONObject("couponInfo").toString(), CouponInfo.class);
                    listener.onSuccess("", couponInfo);
                } else {
                    listener.onSuccess(jsonObject.optString("points"), null);
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8088 用户领取优惠券
     * <p/>
     * 如果优惠券id为空，则传points积分
     *
     * @param couponId 优惠券id
     * @param points   积分数
     */
    public void getMyShakeCoupon(final Context context, final String couponId, final String points, final ApiRequestListener<String> listener) {
        String paramStr = "type=8088&" + "couponId=" + couponId + "&points=" + points;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess("", "");
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8093 优惠券数量统计
     */
    public void countCouponAmount(final Context context, final ApiRequestListener<String> listener) {
        String paramStr = "type=8093";
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess("", jsonObject.optString("total"));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8089我的优惠券列表
     *
     * @param page 页数
     */
    public void getMyShakeCouponList(final Context context, final int page, final ApiRequestListener<CommonList<CouponInfo>> listener) {
        String paramStr = "type=8089&page=" + page;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<CouponInfo> commonList = new CommonList<CouponInfo>();
                if (jsonObject.has("current_page")) {
                    commonList.current_page = jsonObject.getInt("current_page");
                }
                if (jsonObject.has("max_page")) {
                    commonList.max_page = jsonObject.getInt("max_page");
                }
                if (jsonObject.has("infoList")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("infoList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        CouponInfo couponInfo = new Gson().fromJson(jsonArray.optJSONObject(i).toString(), CouponInfo.class);
                        commonList.add(couponInfo);
                    }
                    listener.onSuccess("", commonList);
                }

            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8096删除优惠券
     *
     * @param id 优惠券id
     */
    public void deleteShakeCoupon(final Context context, final String id, final ApiRequestListener<String> listener) {
        String paramStr = "type=8096&id=" + id;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess("", "");
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8097/8098 收藏优惠卷/取消收藏优惠卷
     *
     * @param context
     * @param isCollect
     * @param couponId
     * @param listener
     */
    public void couponCollect(final Context context, Boolean isCollect, String couponId, final ApiRequestListener<String> listener) {
        String paramStr = isCollect ? "type=8098&couponId=" + couponId : "type=8097&couponId=" + couponId;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess("", jsonObject.optString("msg"));
            }

            @Override
            public void onFailure(String msg) {
                listener.onSuccess("", msg);
            }
        });
    }

    /**
     * 8099 收藏优惠券列表接口
     *
     * @param context
     * @param shopId
     * @param page
     * @param listener
     */
    public void getCollectShopCouponList(Context context, String shopId, int page, final ApiRequestListener<CommonList<CouponInfo>> listener) {
        String paramStr = "type=8099&shopId=" + shopId + "&page=" + page;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<CouponInfo> commonList = new CommonList<CouponInfo>();
                if (jsonObject.has("current_page")) {
                    commonList.current_page = jsonObject.getInt("current_page");
                }
                if (jsonObject.has("max_page")) {
                    commonList.max_page = jsonObject.getInt("max_page");
                }
                if (jsonObject.has("infoList")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("infoList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        CouponInfo couponInfo = new Gson().fromJson(jsonArray.optJSONObject(i).toString(), CouponInfo.class);
                        commonList.add(couponInfo);
                    }
                    listener.onSuccess("", commonList);
                }

            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }


    /**
     * 8100 收藏优惠券商户列表
     *
     * @param context
     * @param listener
     */
    public void getCollectedShopList(Context context, final ApiRequestListener<List<ShopInfo>> listener) {
        String paramStr = "type=8100";
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                List<ShopInfo> shopInfoList = new ArrayList<ShopInfo>();
                JSONArray jsonArray = jsonObject.optJSONArray("infoList");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShopInfo shopInfo = new Gson().fromJson(jsonArray.get(i).toString(), ShopInfo.class);
                        shopInfoList.add(shopInfo);
                    }
                }
                String msg = jsonObject.optString("msg");
                listener.onSuccess(msg, shopInfoList);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8095资讯详细信息
     *
     * @param context
     * @param id       //资讯id
     * @param listener
     */

    public void getNewsInfoDetails(Context context, String id, final ApiRequestListener<NewsInfo> listener) {
        String paramStr = "type=8095&id=" + id;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                NewsInfo newsInfo = new Gson().fromJson(jsonObject.optJSONObject("information").toString(), NewsInfo.class);
                newsInfo.isgroup = jsonObject.optString("isgroup");
                newsInfo.share_link = jsonObject.optString("share_link");
                String msg = jsonObject.optString("msg");
                listener.onSuccess(msg, newsInfo);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });

    }

    /**
     * 8092  优惠券详细信息
     *
     * @param context
     * @param couponId //优惠券id
     */
    public void getCouponDetails(final Context context, String couponId, final ApiRequestListener<CouponInfo> listener) {
        String paramStr = "type=8092&&couponId=" + couponId;

        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CouponInfo couponInfo = new Gson().fromJson(jsonObject.optJSONObject("couponInfo").toString(), CouponInfo.class);
                couponInfo.share_link = jsonObject.optString("share_link");
                listener.onSuccess("", couponInfo);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8121 商户分类接口
     *
     * @param context
     * @param listener
     */
    public void getShopClassList(final Context context, final ApiRequestListener<List<ShopClassInfo>> listener) {
        String strParam = "type=8121";
        HttpPost(context, strParam, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                List<ShopClassInfo> shopClassInfos = new ArrayList<ShopClassInfo>();
                if (jsonObject.has("list")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (jsonArray.optJSONObject(i) != null) {
                            shopClassInfos.add(new Gson().fromJson(jsonArray.optJSONObject(i).toString(),
                                    ShopClassInfo.class));
                        }
                    }
                }
                listener.onSuccess(jsonObject.optString("msg"), shopClassInfos);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });

    }

}
