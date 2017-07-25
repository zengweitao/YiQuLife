package com.quanmai.yiqu.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.quanmai.yiqu.api.vo.BagRepertoryInfo;
import com.quanmai.yiqu.api.vo.CompanyInfo;
import com.quanmai.yiqu.api.vo.DialogCommunityInfo;
import com.quanmai.yiqu.api.vo.GrantBagDetailsInfo;
import com.quanmai.yiqu.api.vo.MachineDataInfo;
import com.quanmai.yiqu.api.vo.MachineDataQueryInfo;
import com.quanmai.yiqu.api.vo.MaterialOrUserDetailsInfo;
import com.quanmai.yiqu.base.CommonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 2016/10/14.
 * 手工发袋api
 * 以下方法中的参数“listener”，如无说明，皆为回调函数
 */

public class HandworkApi extends ApiConfig {
    private static HandworkApi mInstance;

    public static HandworkApi get() {
        if (mInstance == null) {
            mInstance = new HandworkApi();
        }
        return mInstance;
    }

    /**
     * 8128 获取库存信息
     *
     * @param context
     * @param listener
     */
    public void getRepertoryInfo(Context context, final ApiRequestListener<Map<String, Object>> listener) {
        String paramStr = "type=8128";
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("address", jsonObject.optString("address", ""));
                map.put("commcode", jsonObject.optString("commcode", ""));
                map.put("commname", jsonObject.optString("commname", ""));
                map.put("address", jsonObject.optString("address", ""));
                if (jsonObject.has("garList")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("garList");
                    List<BagRepertoryInfo> garList = new CommonList<BagRepertoryInfo>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        garList.add(new Gson().fromJson(jsonArray.get(i).toString(), BagRepertoryInfo.class));
                    }
                    map.put("garList", garList);
                }
                listener.onSuccess(jsonObject.optString("msg"), map);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8129 库存详情
     *
     * @param context
     * @param commcode
     * @param listener
     */
    public void getRepertoryDetails(Context context, String commcode, final ApiRequestListener<Map<String, Object>> listener) {
        String paramStr = "type=8129&commcode=" + commcode;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("issuetype", jsonObject.optString("issuetype", ""));        //发放类型
                map.put("totalout", jsonObject.optString("totalout", ""));          //发放总数
                map.put("totalremain", jsonObject.optString("totalremain", ""));    //总剩余发放量
                if (jsonObject.has("inventList")) {
                    List<GrantBagDetailsInfo> list = new ArrayList<GrantBagDetailsInfo>();
                    for (int i = 0; i < jsonObject.optJSONArray("inventList").length(); i++) {
                        GrantBagDetailsInfo info = new Gson().fromJson(jsonObject.optJSONArray("inventList").get(i).toString(), GrantBagDetailsInfo.class);
                        list.add(info);
                    }
                    map.put("inventList", list);
                }
                listener.onSuccess(jsonObject.optString("msg"), map);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8130 发袋信息
     *
     * @param context
     * @param barcode  垃圾袋编号
     * @param listener
     */
    public void getGrantBagInfo(Context context, String barcode, final ApiRequestListener<Map<String, Object>> listener) {
        String paramStr = "type=8130&barcode=" + barcode;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("commcode", jsonObject.optString("commcode", ""));    //小区编号
                map.put("commname", jsonObject.optString("commname", ""));    //小区名
                map.put("barcode", jsonObject.optString("barcode", ""));    //小区名
                map.put("bagtype", jsonObject.optString("bagtype", ""));      //垃圾袋类型:（1.厨余垃圾 2其他垃圾 3.有害垃圾 4.可回收）
                map.put("bagnum", jsonObject.optString("bagnum", ""));        //垃圾袋数量
                map.put("issuetype", jsonObject.optString("issuetype", ""));  //发放类型
                map.put("issuenums", jsonObject.optString("issuenums", ""));  //每次发放数量
                listener.onSuccess(jsonObject.optString("msg"), map);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8131 发袋信息提交
     *
     * @param context
     * @param phone      接收人手机
     * @param realname   接收人姓名
     * @param startcode  起始编号
     * @param endcode    截至编号
     * @param bagnum     发放数量
     * @param bagtype    袋子类型
     * @param issuetype  发放类型
     * @param commcode   小区编号
     * @param commname   小区名
     * @param buildingno 幢
     * @param unit       单元
     * @param room       室
     * @param listener
     */
    public void submitGrantBagInfo(Context context, String usercompareid,String phone, String realname, String startcode,
                                   String endcode, String bagnum, String bagtype, String issuetype,
                                   String commcode, String commname, String buildingno, String unit,
                                   String room, final ApiRequestListener<String> listener) {
        String paramStr = "type=8131&usercompareid="+usercompareid+"&phone=" + phone + "&realname=" + realname + "&startcode=" + startcode
                + "&endcode=" + endcode + "&bagnum=" + bagnum + "&bagtype=" + bagtype + "&issuetype=" + issuetype
                + "&commcode=" + commcode + "&commname=" + commname + "&buildingno=" + buildingno
                + "&unit=" + unit + "&room=" + room;
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

    /**
     * 8132 库存信息提交
     *
     * @param context
     * @param phone     送货人手机
     * @param realname  送货人姓名
     * @param startcode 起始编号
     * @param endcode   截至编号
     * @param bagnum    发放数量
     * @param bagtype   袋子类型
     * @param commcode  小区编号
     * @param commname  小区名
     * @param company   送货单位
     * @param listener
     */
    public void submitRepertoryInfo(Context context, String phone, String realname, String startcode,
                                    String endcode, String bagnum, String bagtype, String commcode,
                                    String commname, String company, final ApiRequestListener<String> listener) {
        String paramStr = "type=8132&phone=" + phone + "&realname=" + realname + "&startcode=" + startcode
                + "&endcode=" + endcode + "&bagnum=" + bagnum + "&bagtype=" + bagtype
                + "&commcode=" + commcode + "&commname=" + commname + "&company=" + company;
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


    /**
     * 8133 送货单位列表
     *
     * @param context
     * @param listener
     */
    public void unitListInfo(Context context, final ApiRequestListener<List<CompanyInfo>> listener) {
        String paramStr = "type=8133";
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                if (jsonObject.has("companyList")) {
                    List<CompanyInfo> list = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.optJSONArray("companyList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add(new Gson().fromJson(jsonArray.get(i).toString(), CompanyInfo.class));
                    }
                    listener.onSuccess(jsonObject.optString("msg"), list);
                } else {
                    listener.onFailure("msg");
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure("msg");
            }
        });
    }

    /**
     * 8134 获取指定户下的用户信息
     *
     * @param context
     * @param commcode   小区编号
     * @param buildingno
     * @param unit
     * @param room
     * @param listener
     */
    public void realNameUserInfo(Context context, String commcode, String buildingno, String unit, String room,
                                 final ApiRequestListener<Map<String, Object>> listener) {
        String paramStr = "type=8134&commcode=" + commcode + "&buildingno=" + buildingno + "&unit=" + unit + "&room=" + room;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("receiveman", jsonObject.optString("receiveman", ""));    //接收人
                map.put("receivemtle", jsonObject.optString("receivemtel", ""));  //接收人手机
                listener.onSuccess(jsonObject.optString("msg"), map);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 扫描机器码获取该机器的货物数据 8218
     * @param context
     * @param terminalno
     * @param listener
     */
    public void getDataByMachineCode(Context context,String terminalno, final ApiRequestListener<MachineDataInfo> listener){
        String paramStr = "type=8218&terminalno=" + terminalno;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                MachineDataInfo data=new Gson().fromJson(jsonObject.toString(),MachineDataInfo.class);
                if (data.getStatus().equals("1")){
                    listener.onSuccess(jsonObject.optString("msg"), data);
                }
            }
            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 提交物料上架信息 8219
     * @param context
     * @param terminalno
     * @param listener
     */
    public void submitDataToMachineCode(Context context,String terminalno,String barcode,String channel, final ApiRequestListener<MachineDataInfo> listener){
        String paramStr = "type=8219&terminalno=" + terminalno+"&barcode="+barcode+"&channel="+channel;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                MachineDataInfo data=new Gson().fromJson(jsonObject.toString(),MachineDataInfo.class);
                if (data.getStatus().equals("1")){
                    listener.onSuccess(jsonObject.optString("msg"), data);
                }
            }
            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8313 库存详情
     *
     * @param context
     * @param commcode
     * @param listener
     */
    public void getMaterialDetails(Context context,String status, String commcode,String date,String pageNum, final ApiRequestListener<JSONObject> listener) {
        String paramStr;
        if (date==null||date.equals("")||date.equals("null")||date.equals(null)){
            paramStr = "type=8313&commcode=" + commcode+"&status="+status+"&pageNum="+pageNum;
        }else {
            paramStr = "type=8313&commcode=" + commcode+"&date="+date+"&status="+status+"&pageNum="+pageNum;
        }
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Log.e("--8313返回json","== "+jsonObject.toString());
                listener.onSuccess(jsonObject.optString("msg"), jsonObject);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8314库存详情
     *
     * @param context
     * @param authority
     * @param listener
     */
    public void getCommunityList(Context context,String authority, final ApiRequestListener<DialogCommunityInfo> listener) {
        String paramStr = "type=8314&authority=" + authority;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                if (jsonObject.has("commList")){
                    DialogCommunityInfo info = new Gson().fromJson(jsonObject.toString(),DialogCommunityInfo.class);
                    listener.onSuccess(jsonObject.optString("msg"), info);
                }else {
                    listener.onFailure(jsonObject.optString("msg"));
                }
            }
            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

}
