package com.quanmai.yiqu.api;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.quanmai.yiqu.api.vo.ChooseEqAreaInfo;
import com.quanmai.yiqu.api.vo.ChooseEqInfo;
import com.quanmai.yiqu.api.vo.ClassifyScoreRankingDetailInfo;
import com.quanmai.yiqu.api.vo.ClassifyScoreRankingInfo;
import com.quanmai.yiqu.api.vo.GradeScoreDetailsInfo;
import com.quanmai.yiqu.api.vo.GradeScoreInfo;
import com.quanmai.yiqu.api.vo.InspectionRecordInfo;
import com.quanmai.yiqu.api.vo.PointPenaltyInfo;
import com.quanmai.yiqu.api.vo.ScoreCommInfo;
import com.quanmai.yiqu.api.vo.ScoreRecordInfo;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhanjinj on 16/3/2.
 */
public class GradeApi extends ApiConfig {
    private static GradeApi mInstance;

    public static GradeApi get() {
        if (mInstance == null) {
            mInstance = new GradeApi();
        }
        return mInstance;
    }

    /**
     * 8217 用户个人打分
     *
     * @param context
     * @param points   得分
     * @param images    打分图片（不为空）
     * @param listener
     */
    public void markPersonal(Context context, String points, String images, final ApiRequestListener<String> listener) {

        String paramStr = "type=8217&points=" + points + "&images=" + images;

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


    /**
     * 8047评分
     *
     * @param context
     * @param points   评分
     * @param bar_code 条码信息
     * @param image    图片
     * @param listener
     */
    public void Points(Context context, String points, String bar_code,
                       String image,String comment, final ApiRequestListener<String> listener) {
        String parram = "";
        if (image == null)
            parram = "type=8047&points=" + points + "&bar_code=" + bar_code+"&context="+comment;
        else
            parram = "type=8047&points=" + points + "&bar_code=" + bar_code
                    + "&image=" + image+"&context="+comment;
        HttpPost(context, parram, new HttpCallBack() {

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
     * 8049用户得分记录
     * type=8049 垃圾分类得分时间格式要传为2012-01-01也就是yyyy-MM-dd这种格式
     *
     * @param context
     * @param page      页数
     * @param starttime 起始时间
     * @param endtime   截止时间
     * @param listener
     */
    public void ScroeRecord(Context context, int page, String starttime,
                            String endtime,
                            final ApiRequestListener<CommonList<ScoreRecordInfo>> listener) {
        String paramStr = "type=8049&page=" + page + "&starttime=" + starttime
                + "&endtime=" + endtime;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<ScoreRecordInfo> commonList = new CommonList<ScoreRecordInfo>();
                if (jsonObject.getString("status").equals("1")) {
                    if (jsonObject.has("current_page")) {
                        commonList.current_page = jsonObject
                                .getInt("current_page");
                    }
                    if (jsonObject.has("max_page")) {
                        commonList.max_page = jsonObject.getInt("max_page");
                    }
                    if (jsonObject.has("scoreList")) {
                        JSONArray array = jsonObject.getJSONArray("scoreList");
                        for (int i = 0; i < array.length(); i++) {
                            commonList.add(new ScoreRecordInfo(array
                                    .getJSONObject(i)));
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
     * 8049 用户得分记录
     *
     * @param context
     * @param page
     * @param listener
     */
    public void getScoreRecord(Context context, int page, String queryInfo, final ApiRequestListener<CommonList<ScoreRecordInfo>> listener) {
        String paramStr = "type=8049&page=" + page;
        if (queryInfo!=null&&queryInfo!=""&&queryInfo!="null"){
            paramStr+="&queryInfo="+queryInfo;
        }
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<ScoreRecordInfo> commonList = new CommonList<ScoreRecordInfo>();
                if (jsonObject.getString("status").equals("1")) {
                    if (jsonObject.has("current_page")) {
                        commonList.current_page = jsonObject
                                .getInt("current_page");
                    }
                    if (jsonObject.has("max_page")) {
                        commonList.max_page = jsonObject.getInt("max_page");
                    }
                    if (jsonObject.has("scoreList")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("scoreList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            commonList.add(new ScoreRecordInfo(jsonArray.getJSONObject(i)));
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
     * 8300垃圾分类得分排名
     *
     * @param context
     * @param date      查询月份  非必传
     * @param listener
     */
    public void ClassifyScoreRanking(Context context,String date,final ApiRequestListener<CommonList<ClassifyScoreRankingInfo>> listener){
        String paramStr = "type=8300&sort_time=" + date ;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<ClassifyScoreRankingInfo> commList = new CommonList<>();
                if (jsonObject.has("commScoreRankList")) {
                    for (int i = 0; i < jsonObject.optJSONArray("commScoreRankList").length(); i++){
                        ClassifyScoreRankingInfo info = new Gson().fromJson(jsonObject.toString(), ClassifyScoreRankingInfo.class);
                        commList.add(info);
                    }
                    listener.onSuccess(jsonObject.optString("msg"), commList);
                } else {
                    listener.onFailure("没有排名信息");
                }
            }
            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }


    /**
     * 8050评分员打分记录
     *
     * @param context
     * @param page     页数
     * @param datetime 起始时间
     * @param listener
     */
    public void ScroeRecord(Context context, int page, String datetime,
                            final ApiRequestListener<CommonList<ScoreRecordInfo>> listener) {
        String paramStr = "type=8050&page=" + page + "&datetime=" + datetime;
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<ScoreRecordInfo> commonList = new CommonList<ScoreRecordInfo>();
                if (jsonObject.getString("status").equals("1")) {
                    if (jsonObject.has("current_page")) {
                        commonList.current_page = jsonObject
                                .getInt("current_page");
                    }
                    if (jsonObject.has("max_page")) {
                        commonList.max_page = jsonObject.getInt("max_page");
                    }

                    if (jsonObject.has("scoreList")) {
                        JSONArray array = jsonObject.getJSONArray("scoreList");
                        for (int i = 0; i < array.length(); i++) {
                            commonList.add(new ScoreRecordInfo(array
                                    .getJSONObject(i)));
                        }
                    }

                    commonList.raterScore = jsonObject.optString("raterScore");
                    commonList.total = jsonObject.optInt("total");
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
     * 8066 获取巡检员月巡检时间记录
     *
     * @param context
     * @param strDate
     * @param listener
     */
    public void InspectionRecordByMonth(Context context, final String strDate, final ApiRequestListener<InspectionRecordInfo> listener) {
        String paramStr = "type=8066&date=" + strDate;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                InspectionRecordInfo recordInfo = null;

                if (jsonObject.has("dateList")) {
                    recordInfo = new InspectionRecordInfo(jsonObject);
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                    try {
                        Date date = dateFormat.parse(strDate);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        recordInfo = new InspectionRecordInfo(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                listener.onSuccess(jsonObject.optString("msg"), recordInfo);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }


    /**
     * 8160 选择打分小区
     */
    public void ChooseScoreComm(Context context, final ApiRequestListener<CommonList<ScoreCommInfo>> listener) {
        String param = "type=8160";

        HttpPost(context, param, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<ScoreCommInfo> commList = new CommonList<ScoreCommInfo>();
                if (jsonObject.has("arList")) {
                    for (int i = 0; i < jsonObject.optJSONArray("arList").length(); i++) {
                        ScoreCommInfo info = new Gson().fromJson(jsonObject.optJSONArray("arList").optJSONObject(i).toString(), ScoreCommInfo.class);
                        commList.add(info);
                    }
                    listener.onSuccess(jsonObject.optString("msg"), commList);
                } else {
                    listener.onFailure("数据解析失败");
                }

            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8161 查询小区下的设施点
     */
    public void ChooseScoreEquipment(Context context, String commCode, final ApiRequestListener<CommonList<ChooseEqInfo>> listener) {
        String param = "type=8161&commcode=" + commCode;

        HttpPost(context, param, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<ChooseEqInfo> commList = new CommonList<ChooseEqInfo>();
                if (jsonObject.has("arList")) {
                    for (int i = 0; i < jsonObject.optJSONArray("arList").length(); i++) {
                        ChooseEqInfo info = new Gson().fromJson(jsonObject.optJSONArray("arList").optJSONObject(i).toString(), ChooseEqInfo.class);
                        commList.add(info);
                    }
                    listener.onSuccess(jsonObject.optString("msg"), commList);
                } else {
                    listener.onFailure("没有设施");
                }

            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8166 查询小区下的设施点（清运专用）
     *
     * @param context
     * @param commCode 小区编码
     * @param listener
     */
    public void ChooseCleanScoreEquipment(Context context, String commCode, final ApiRequestListener<CommonList<ChooseEqAreaInfo>> listener) {
        String param = "type=8166&commcode=" + commCode;

        HttpPost(context, param, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<ChooseEqAreaInfo> commList = new CommonList<ChooseEqAreaInfo>();
                if (jsonObject.has("arList")) {
                    for (int i = 0; i < jsonObject.optJSONArray("arList").length(); i++) {
                        ChooseEqAreaInfo info = new Gson().fromJson(jsonObject.optJSONArray("arList").optJSONObject(i).toString(), ChooseEqAreaInfo.class);
                        commList.add(info);
                    }
                    listener.onSuccess(jsonObject.optString("ischeck"), commList);
                } else {
                    listener.onFailure("没有设施");
                }

            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8162 巡检扣分条例查询
     */
    public void GradeItemList(Context context, String classgistType, final ApiRequestListener<CommonList<PointPenaltyInfo>> listener) {
        String param = "type=8162&classgistType=" + classgistType;

        HttpPost(context, param, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommonList<PointPenaltyInfo> commonList = new CommonList<PointPenaltyInfo>();
                if (jsonObject.has("arList")) {
                    for (int i = 0; i < jsonObject.optJSONArray("arList").length(); i++) {
                        JSONObject object = jsonObject.optJSONArray("arList").optJSONObject(i);
                        PointPenaltyInfo info = new Gson().fromJson(object.toString(), PointPenaltyInfo.class);
                        commonList.add(info);
                    }
                    listener.onSuccess(jsonObject.optString("msg"), commonList);
                } else {
                    listener.onFailure("没有检测到扣分项");
                }

            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8164 打分记录查询(含清运、设施点打分记录)
     *
     * @param context
     * @param commcode  小区编号
     * @param amenityid 设施DI或者设施点ID(可传可不传)
     * @param scoreType 打分类型ID（1为设施点打分记录，2为清运打分记录）
     * @param page      页码(可传可不传)
     * @param listener
     */
    public void gradeScore(Context context, String commcode, int scoreType, int page, String amenityid, final ApiRequestListener<CommonList<GradeScoreInfo>> listener) {
        String paramStr = "";
        if (TextUtils.isEmpty(amenityid)) {
            paramStr = "type=8164&commcode=" + commcode + "&scoreType=" + scoreType + "&page=" + page;
        } else {
            paramStr = "type=8164&commcode=" + commcode + "&scoreType=" + scoreType + "&page=" + page + "&amenityid=" + amenityid;
        }
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                if (!jsonObject.has("arList") || jsonObject.optJSONArray("arList") == null) {
                    listener.onFailure(jsonObject.optString("msg"));
                    return;
                }

                JSONArray jsonArray = jsonObject.optJSONArray("arList");
                CommonList<GradeScoreInfo> list = new CommonList<GradeScoreInfo>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(new Gson().fromJson(jsonArray.get(i).toString(), GradeScoreInfo.class));
                }
                list.max_page = jsonObject.optInt("maxPage");
                listener.onSuccess(jsonObject.optString("msg"), list);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }


    /**
     * 8165 扣分记录查询(含清运、设施点扣分记录)
     *
     * @param context
     * @param scoreType 打分类型ID（1为设施点打分记录，2为清运打分记录）
     * @param inspectid 打分记录表ID
     * @param listener
     */
    public void gradeScoreDetails(Context context, int scoreType, int inspectid, final ApiRequestListener<CommonList<GradeScoreDetailsInfo>> listener) {
        String paramStr = "type=8165&scoreType=" + scoreType + "&inspectid=" + inspectid;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                if (!jsonObject.has("arList") || jsonObject.optJSONArray("arList") == null) {
                    listener.onFailure(jsonObject.optString("msg"));
                    return;
                }

                JSONArray jsonArray = jsonObject.optJSONArray("arList");
                CommonList<GradeScoreDetailsInfo> list = new CommonList<GradeScoreDetailsInfo>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(new Gson().fromJson(jsonArray.get(i).toString(), GradeScoreDetailsInfo.class));
                }
                listener.onSuccess(jsonObject.optString("msg"), list);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8163 设施打分
     *
     * @param amenityAreaId 设施点ID
     * @param amenityId     设施ID
     * @param arlist        扣分项id和扣分值数组([[1,0.5],[2,0.4],[3,0.6]]把int[]数组放入List，然后转成json字符串传给后台)
     * @param amenityImg    图片名 (xxx.jpg,xxx.png,xxx.jpg)
     */
    public void GradeEquipment(Context context, String amenityAreaId, String amenityId, String arlist, String amenityImg, final ApiRequestListener<String> listener) {
        String param = "type=8163&amenityAreaId=" + amenityAreaId + "&amenityId=" + amenityId + "&arlist=" + arlist + "&amenityImg=" + amenityImg;

        HttpPost(context, param, new HttpCallBack() {

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
     * 8167 设施点清运打分
     *
     * @param context
     * @param commNo        小区编号
     * @param amenityAreaId 设施点ID
     * @param arlist        扣分项id和扣分值数组(JSon字符串)
     * @param amenityImg    图片名
     * @param listener
     */
    public void cleanUpGrade(Context context, String commNo, String amenityAreaId, String arlist, String amenityImg,
                             final ApiRequestListener<String> listener) {
        String paramStr = "type=8167&commNo=" + commNo + "&amenityAreaId=" + amenityAreaId
                + "&arlist=" + arlist + "&amenityImg=" + amenityImg;
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
