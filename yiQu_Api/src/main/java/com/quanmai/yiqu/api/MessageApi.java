package com.quanmai.yiqu.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.quanmai.yiqu.api.vo.CommentInfo;
import com.quanmai.yiqu.api.vo.Noticeinfo;
import com.quanmai.yiqu.api.vo.PicValidateInfo;
import com.quanmai.yiqu.api.vo.QuestionInfo;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by accelerate on 16/3/2.
 */
public class MessageApi extends ApiConfig {

    private static MessageApi mInstance;

    public static MessageApi get() {
        if (mInstance == null) {
            mInstance = new MessageApi();
        }

        return mInstance;
    }


    /**
     * 8041留言列表
     *
     * @param context
     * @param page     页数
     * @param listener
     */
    public void MessageList(Context context, int page,
                            final ApiRequestListener<CommonList<CommentInfo>> listener) {
        String paramStr = "type=8041&page=" + page;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<CommentInfo> commonList = new CommonList<CommentInfo>();
                if (jsonObject.has("current_page")) {
                    commonList.current_page = jsonObject.getInt("current_page");
                }
                if (jsonObject.has("max_page")) {
                    commonList.max_page = jsonObject.getInt("max_page");
                }
                if (jsonObject.has("list")) {
                    JSONArray array = jsonObject.getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        commonList.add(new CommentInfo(array.getJSONObject(i)));
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
     * 8042系统通知
     *
     * @param context
     * @param page     页数
     * @param listener
     */
    public void NoticeList(Context context, int page,
                           final ApiRequestListener<CommonList<Noticeinfo>> listener) {
        String paramStr = "type=8042&page=" + page;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<Noticeinfo> commonList = new CommonList<Noticeinfo>();
                if (jsonObject.has("current_page")) {
                    commonList.current_page = jsonObject.getInt("current_page");
                }
                if (jsonObject.has("max_page")) {
                    commonList.max_page = jsonObject.getInt("max_page");
                }
                if (jsonObject.has("list")) {
                    JSONArray array = jsonObject.getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        commonList.add(new Noticeinfo(array.getJSONObject(i)));
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
     * 8048留言
     *
     * @param context
     * @param user_id         商品id
     * @param comment_content 留言内容
     * @param listener
     */
    public void Message(Context context, String user_id,
                        String comment_content, final ApiRequestListener<String> listener) {
        String paramStr = "type=8048&user_id=" + user_id + "&content="
                + comment_content;
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
     * 8054获取图片验证页面
     *
     * @param context
     * @param listener
     */
    public void PicValidate(Context context,
                            final ApiRequestListener<CommonList<PicValidateInfo>> listener) {
        String paramStr = "type=8054";
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<PicValidateInfo> commonList = new CommonList<PicValidateInfo>();
                commonList.name = jsonObject.getString("classname");
                if (jsonObject.has("list")) {
                    JSONArray array = jsonObject.getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        commonList.add(new PicValidateInfo(array
                                .getJSONObject(i)));
                    }
                }
                listener.onSuccess(jsonObject.getString("answers"), commonList);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });

    }


    /**
     * 8064图片验证成功
     *
     * @param context
     * @param listener
     */
    public void PicValidateSuccess(Context context, final ApiRequestListener<Map<String, Object>> listener) {
        String paramStr = "type=8064";
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("singletimes", jsonObject.optString("singletimes", "0")); //本次可领取福利（福袋或积分）个数
                map.put("monthlasttimes", jsonObject.optString("monthlasttimes")); //本月剩余领取次数
                map.put("receivepoint", jsonObject.optString("receivepoint")); //本月领取福袋数
                map.put("receivetype", jsonObject.optString("receivetype", "0")); //福利类型 0. 福袋 1. 积分

                listener.onSuccess(jsonObject.optString("msg"), map);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }


    /**
     * 8056图片验证成功
     *
     * @param context
     * @param service_id service_id
     * @param listener
     */
    public void FixAttend(Context context, String service_id,
                          final ApiRequestListener<String> listener) {
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

    /**
     * 8063 获取福袋答题机会
     *
     * @param context
     * @param listener
     */
    public void GetAnswerQuestionsChange(Context context, final ApiRequestListener<String> listener) {
        String paramStr = "type=8063";
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
//				String str = "本月福袋已领取："+jsonObject.optString("receivetimes","0")+"次，"+"剩余："+jsonObject.optString("lasttimes","0")+"次";
                listener.onSuccess(jsonObject.optString("receivetimes", "0"), jsonObject.optString("lasttimes", "0"));

            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8119 获取广告答题信息
     *
     * @param context
     * @param listener
     */
    public void getAdAnswerQuestionInfo(Context context, final ApiRequestListener<QuestionInfo> listener) {
        String paramStr = "type=8119";
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                if (jsonObject.optString("isexist").equals("0")) {
                    listener.onFailure(jsonObject.optString("msg"));
                    return;
                }

                if (jsonObject.has("questionList")) {
                    JSONArray optionsList = jsonObject.optJSONArray("questionList");
                    QuestionInfo questionInfo = new Gson().fromJson(optionsList.opt(0).toString(), QuestionInfo.class);
                    String isexist = jsonObject.optString("isexist"); //是否有答题题目 0.无 1.有
                    listener.onSuccess(isexist, questionInfo);
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
     * 8120 广告答题得分
     *
     * @param context
     * @param contentid 题目id
     * @param points    题目分值
     * @param listener
     */
    public void adAnswerQuestionGetPoint(Context context, String contentid, String points, final ApiRequestListener<String> listener) {
        String paramStr = "type=8120&contentid=" + contentid + "&points=" + points;
        Log.i("8120--->", paramStr);
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
