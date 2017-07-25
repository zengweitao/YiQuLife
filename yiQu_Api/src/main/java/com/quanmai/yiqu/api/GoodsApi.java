package com.quanmai.yiqu.api;

import android.content.Context;
import android.util.Log;

import com.quanmai.yiqu.api.vo.BannerInfo;
import com.quanmai.yiqu.api.vo.GoodsBasic;
import com.quanmai.yiqu.api.vo.GoodsInfo;
import com.quanmai.yiqu.api.vo.HomeInfo;
import com.quanmai.yiqu.api.vo.SortInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.publish.ProductType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by accelerate on 16/3/2.
 * 以下方法中的参数“listener”，如无说明，皆为回调函数
 */
public class GoodsApi extends ApiConfig {

    private static GoodsApi mInstance;

    public static GoodsApi get() {
        if (mInstance == null) {
            mInstance = new GoodsApi();
        }
        return mInstance;
    }


    /**
     * 8021商品首页
     *
     * @param context
     * @param listener 回调函数
     */
    public void DealHomePage(Context context,
                             final ApiConfig.ApiRequestListener<HomeInfo> listener) {
        String paramStr = "type=8021";
        HttpPost(context, paramStr, new ApiConfig.HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                // CommonList<AdvertInfo> commonList = new
                // CommonList<AdvertInfo>();
                // JSONArray array = jsonObject.getJSONArray("classList");
                // for (int i = 0; i < array.length(); i++) {
                // commonList.add(new AdvertInfo(array.getJSONObject(i)));
                // }
                listener.onSuccess(jsonObject.getString("msg"), new HomeInfo(
                        jsonObject));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8022商品筛选
     *
     * @param context
     * @param listener
     */
    public void GoodsScreening(Context context,
                               final ApiRequestListener<CommonList<SortInfo>> listener) {
        String paramStr = "type=8022";
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<SortInfo> commonList = new CommonList<SortInfo>();
                if (jsonObject.has("classList")) {
                    JSONArray array = jsonObject.getJSONArray("classList");
                    for (int i = 0; i < array.length(); i++) {
                        commonList.add(new SortInfo(array.getJSONObject(i)));
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
     * 8023商品列表
     *
     * @param context
     * @param page      页数
     * @param keyword   搜索关键字
     * @param area_id   地区id
     * @param class_id  分类id
     * @param sort_type 排序id  1：价格 2：成色 3：时间
     * @param listener
     */
    public void GoodsList(Context context, int page, String keyword,
                          String area_id, String class_id, String sort_type,
                          final ApiRequestListener<CommonList<GoodsBasic>> listener) {
        String paramStr = "type=8023&page=" + page + "&keyword=" + keyword
                + "&area_id=" + area_id + "&class_id=" + class_id
                + "&sort_type=" + sort_type;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<GoodsBasic> commonList = new CommonList<GoodsBasic>();
                if (jsonObject.has("current_page")) {
                    commonList.current_page = jsonObject.getInt("current_page");
                }
                if (jsonObject.has("max_page")) {
                    commonList.max_page = jsonObject.getInt("max_page");
                }
                if (jsonObject.has("goodsList")) {
                    JSONArray array = jsonObject.getJSONArray("goodsList");
                    for (int i = 0; i < array.length(); i++) {
                        commonList.add(new GoodsBasic(array.getJSONObject(i)));
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
     * 8026商品详情
     *
     * @param context
     * @param goods_id 商品id
     * @param listener
     */
    public void GoodsDetail(Context context, String goods_id,
                            final ApiRequestListener<GoodsInfo> listener) {
        String paramStr = "type=8026&goods_id=" + goods_id;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"), new GoodsInfo(
                        jsonObject));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }


    /**
     * 8032发布管理列表 0正常 1已关闭
     *
     * @param context
     * @param isAll
     * @param page     page
     * @param listener
     */
    public void GoodsManageList(Context context, boolean isAll, int page,
                                final ApiRequestListener<CommonList<GoodsBasic>> listener) {
        int status = 0;
        if (!isAll) {
            status = 1;
        }
        String paramStr = "type=8032&status=" + status + "&page=" + page;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<GoodsBasic> commonList = new CommonList<GoodsBasic>();
                if (jsonObject.has("current_page")) {
                    commonList.current_page = jsonObject.getInt("current_page");
                }
                if (jsonObject.has("max_page")) {
                    commonList.max_page = jsonObject.getInt("max_page");
                }
                if (jsonObject.has("goodsList")) {
                    JSONArray array = jsonObject.getJSONArray("goodsList");
                    for (int i = 0; i < array.length(); i++) {
                        commonList.add(new GoodsBasic(array.getJSONObject(i)));
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
     * 8033已删除列表
     *
     * @param context
     * @param page     页数
     * @param listener
     */
    public void GoodsDeletedList(Context context, int page,
                                 final ApiRequestListener<CommonList<GoodsBasic>> listener) {
        String paramStr = "type=8032&page=" + page;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<GoodsBasic> commonList = new CommonList<GoodsBasic>();
                if (jsonObject.has("current_page")) {
                    commonList.current_page = jsonObject.getInt("current_page");
                }
                if (jsonObject.has("max_page")) {
                    commonList.max_page = jsonObject.getInt("max_page");
                }
                if (jsonObject.has("list")) {
                    JSONArray array = jsonObject.getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        commonList.add(new GoodsBasic(jsonObject));
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
     * 8034清空已删除的列表
     *
     * @param context
     * @param listener
     */
    public void GoodsDeletedListClear(Context context,
                                      final ApiRequestListener<String> listener) {
        HttpPost(context, "type=8034", new HttpCallBack() {

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
     * 8035发布产品分类选择列表
     *
     * @param context
     * @param listener
     */
    public void GoodsClassList(Context context,
                               final ApiRequestListener<List<ProductType>> listener) {
        HttpPost(context, "type=8022", new HttpCallBack() {

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess("",
                        ProductType.get(jsonObject.getJSONArray("classList")));
            }

        });
    }


    /**
     * 8036发布产品
     *
     * @param context
     * @param goods_name        商品名，标题
     * @param goods_description 商品描述
     * @param goods_img         图片
     * @param goods_type        产品类型
     * @param goods_price       价格
     * @param class_id          分类id
     * @param class_name        分类名
     * @param goods_degree      新旧程度 1-10
     * @param phone             联系电话
     * @param listener
     */
    public void GoodsRelease(Context context, String goods_name,
                             String goods_description, String goods_img, String goods_type,
                             String goods_price, String class_id, String class_name,
                             String goods_degree, String phone,
                             final ApiRequestListener<String> listener) {
        HttpPost(context, "type=8036&goods_name=" + goods_name
                + "&goods_description=" + goods_description + "&goods_img="
                + goods_img + "&goods_type=" + goods_type + "&goods_price="
                + goods_price + "&class_id=" + class_id + "&class_name="
                + class_name + "&goods_degree=" + goods_degree + "&address_id=0&phone=" + phone, new HttpCallBack() {

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
     * 8037编辑产品
     *
     * @param context
     * @param goods_id          商品id
     * @param goods_name        商品名，标题
     * @param goods_description 商品描述
     * @param goods_img         图片
     * @param goods_type        产品类型
     * @param goods_price       价格
     * @param class_id          分类id
     * @param class_name        分类名
     * @param goods_degree      新旧程度 1-10
     * @param position          地点
     * @param phone             联系电话
     * @param listener
     */
    public void GoodsEdit(Context context, String goods_id, String goods_name,
                          String goods_description, String goods_img, String goods_type,
                          String goods_price, String class_id, String class_name,
                          String goods_degree, String position, String phone,
                          final ApiRequestListener<String> listener) {
        HttpPost(context, "type=8037&address_id=0&goods_id=" + goods_id
                + "&goods_name=" + goods_name + "&goods_description="
                + goods_description + "&goods_img=" + goods_img
                + "&goods_type=" + goods_type + "&goods_price=" + goods_price
                + "&class_id=" + class_id + "&class_name=" + class_name
                + "&goods_degree=" + goods_degree + "&position=" + position
                + "&phone=" + phone, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                if (jsonObject.getInt("status") == 0) {
                    listener.onFailure(jsonObject.getString("msg"));
                } else {
                    listener.onSuccess(jsonObject.getString("msg"), "");
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }


    /**
     * 8038删除产品
     *
     * @param context
     * @param goods_id 商品id
     * @param listener
     */
    public void GoodsDelete(Context context, String goods_id,
                            final ApiRequestListener<String> listener) {
        HttpPost(context, "type=8038&goods_id=" + goods_id, new HttpCallBack() {

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
     * 8039关闭产品 0开启 1关闭
     *
     * @param context
     * @param isOpen   开启or关闭产品，0开启 1关闭
     * @param goods_id 商品id
     * @param listener
     */
    public void GoodsClosed(Context context, boolean isOpen, String goods_id,
                            final ApiRequestListener<String> listener) {
        int status = 0;
        if (!isOpen) {
            status = 1;
        }
        HttpPost(context, "type=8039&goods_id=" + goods_id + "&status="
                + status, new HttpCallBack() {

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
     * 8040分享产品
     *
     * @param context
     * @param goods_id 商品id
     * @param listener
     */
    public void GoodsShare(Context context, String goods_id,
                           final ApiRequestListener<String> listener) {
        String paramStr = "type=8040&goods_id=" + goods_id;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {

                listener.onSuccess(jsonObject.getString("msg"),
                        jsonObject.toString());
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8060获取广告信息
     *
     * @param context
     * @param adType   广告类型 "1"主页  “2”闲置  "3"社区服务  "4"领取垃圾袋  "5"签到页面  "6"启动页 "7"领取福袋
     * @param listener
     */
    public void GetAvd(Context context, String adType, final ApiRequestListener<BannerInfo> listener) {
        String paramStr = "type=8060&&adType=" + adType;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Log.e("--请求广告","== "+jsonObject.toString());
                if (jsonObject.has("adList")) {
                    listener.onSuccess(jsonObject.optString("msg"), new BannerInfo(jsonObject));
                } else {
                    listener.onFailure(jsonObject.optString("msg"));
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8068 商品评论删除接口
     *
     * @param context
     * @param comment_id 用户评论id
     * @param goods_id   商品id
     * @param user_id    用户表id(不传:删除自己的评论，传：删除别人评论)
     * @param listener
     */
    public void DeleteComment(Context context,
                              String comment_id, String goods_id, String user_id, final ApiRequestListener<String> listener) {
        String paramStr = "type=8068&comment_id=" + comment_id + "&goods_id=" + goods_id + "&user_id=" + user_id;
        HttpPost(context, paramStr, new HttpCallBack() {
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
