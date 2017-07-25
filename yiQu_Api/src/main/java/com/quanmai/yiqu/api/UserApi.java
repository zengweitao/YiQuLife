package com.quanmai.yiqu.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.quanmai.yiqu.api.vo.AddressRoomInfo;
import com.quanmai.yiqu.api.vo.BuildInfo;
import com.quanmai.yiqu.api.vo.ChooseRoomInfo;
import com.quanmai.yiqu.api.vo.CityInfo;
import com.quanmai.yiqu.api.vo.CommCodeInfo;
import com.quanmai.yiqu.api.vo.CommServiceList;
import com.quanmai.yiqu.api.vo.CommunityInfo;
import com.quanmai.yiqu.api.vo.RealNameMember;
import com.quanmai.yiqu.api.vo.SearchUserDataInfo;
import com.quanmai.yiqu.api.vo.ServicesInfo;
import com.quanmai.yiqu.api.vo.UnitInfo;
import com.quanmai.yiqu.api.vo.UserDetailsByHouseCodeInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by accelerate on 16/3/2.
 */
public class UserApi extends ApiConfig {

    private static UserApi mInstance;

    public static UserApi get() {
        if (mInstance == null) {
            mInstance = new UserApi();
        }
        return mInstance;
    }

    /**
     * 8001 获取注册验证码
     *
     * @param context
     * @param phone    手机号码
     * @param listener 状态监听
     */
    public void RegisteredCode(Context context, String phone,
                               final ApiConfig.ApiRequestListener<String> listener) {
        String paramStr = "type=8001&phone=" + phone;
        HttpPost(context, paramStr, new ApiConfig.HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"), jsonObject.optString("code"));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8002 用户注册
     *
     * @param context
     * @param phone    手机号码
     * @param code     验证码
     * @param password 密码
     * @param listener
     */
    public void Registered(Context context, String phone, String code,
                           String password, final ApiRequestListener<String> listener) {
        String paramStr = "type=8002&phone=" + phone + "&code=" + code
                + "&password=" + Utils.getMD5(password);
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"),
                        jsonObject.getString("token"));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8003 用户登录
     *
     * @param context
     * @param phone    手机号码
     * @param pwd      密码
     * @param listener
     */
    public void Login(Context context, String phone, String pwd,
                      final ApiRequestListener<String> listener) {
        String paramStr = "type=8003&phone=" + phone + "&password="
                + Utils.getMD5(pwd);
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"),
                        jsonObject.getString("token"));

            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8004 获取找回密码验证码
     *
     * @param context
     * @param phone    手机号码
     * @param listener
     */
    public void FindPwdCode(Context context, String phone,
                            final ApiRequestListener<String> listener) {
        String paramStr = "type=8004&phone=" + phone;
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
     * 8005 找回密码
     *
     * @param context
     * @param phone    手机号码
     * @param code     验证码
     * @param password 密码
     * @param listener
     */
    public void FindPwd(Context context, String phone, String code,
                        String password, final ApiRequestListener<String> listener) {
        String paramStr = "type=8005&phone=" + phone + "&code=" + code
                + "&password=" + Utils.getMD5(password);
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess(jsonObject.getString("msg"),
                        jsonObject.getString("token"));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8006 修改密码
     *
     * @param context
     * @param password    密码
     * @param newpassword 新密码
     * @param listener
     */
    public void ChangePwd(Context context, String password, String newpassword,
                          final ApiRequestListener<String> listener) {
        String paramStr = "type=8006&password=" + Utils.getMD5(password)
                + "&newpassword=" + Utils.getMD5(newpassword);
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {

                listener.onSuccess(jsonObject.getString("msg"),
                        jsonObject.getString("token"));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });

    }

    /**
     * 8058获取社区服务列表
     *
     * @param context
     * @param commcode 社区代码
     * @param listener
     */
    public void GetCommunityServiceList(Context context, String commcode, final ApiRequestListener<CommServiceList> listener) {
        String paramStr = "type=8058&&commcode=" + commcode;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                if (jsonObject.has("commServiceinfo")) {
                    listener.onSuccess(jsonObject.getString("msg"), new CommServiceList(jsonObject));
                } else {
                    listener.onFailure("");
                }


            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8057获取社区列表
     *
     * @param context
     * @param city     需要获取社区列表的城市
     * @param listener
     */
    public void GetCommunityList(Context context, String city, final ApiRequestListener<CommCodeInfo> listener) {
        String paramStr = "type=8057";
        if (city != null && city != "") {
            paramStr = paramStr + "&&areacode=" + city;
        }
        HttpPost(context, paramStr, new HttpCallBack() {

            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                if (jsonObject.has("commCodeList")) {
                    listener.onSuccess(jsonObject.getString("msg"), new CommCodeInfo(jsonObject));
                } else {
                    listener.onFailure("");
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8091 获取社区便民服务列表
     */
    public void GetCommunityService(Context context, String areaCode, final ApiRequestListener<CommonList<ServicesInfo>> listener) {
        String paramStr = "type=8091&areaCode=" + areaCode;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                if (jsonObject.has("peoplemanageList")) {
                    CommonList<ServicesInfo> list = new CommonList<ServicesInfo>();
                    JSONArray jsonArray = jsonObject.optJSONArray("peoplemanageList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add(new ServicesInfo(jsonArray.getJSONObject(i)));
                    }
                    listener.onSuccess("", list);
                } else {
                    listener.onFailure("没有数据");
                }
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }


    /**
     * 8101 实名制注册
     *
     * @param context
     * @param commcode
     * @param buildingno
     * @param unit
     * @param room
     * @param listener
     */
    public void residentBinding(Context context, String realname, String commcode, String buildingno, String unit, String room, final ApiRequestListener<Map<String, String>> listener) {
        String paramStr = "type=8101&realname=" + realname + "&commcode=" + commcode + "&buildingno=" + buildingno
                + "&unit=" + unit + "&room=" + room;

        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Map<String, String> stringMap = new HashMap<String, String>();
                stringMap.put("hostname", jsonObject.optString("hostname"));
                stringMap.put("isexsit", jsonObject.optString("isexsit", "0")); //是否已被实名过 0.否 1.是
                stringMap.put("phone", jsonObject.optString("phone"));
                stringMap.put("address", jsonObject.optString("address"));
                stringMap.put("usercompareid", jsonObject.optString("usercompareid"));
                listener.onSuccess(jsonObject.optString("msg"), stringMap);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8102 实名制获取市和小区
     *
     * @param context
     * @param commname 小区名(可不传) 上传该参数为小区搜索功能
     * @param listener
     */
    public void getCityOrCommutity(Context context, String commname, final ApiRequestListener<CommonList<CityInfo>> listener) {
        String paramStr = TextUtils.isEmpty(commname) ? "type=8102" : "type=8102&commname=" + commname;

        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                if (jsonObject.has("arList")) {
                    CommonList<CityInfo> cityInfos = new CommonList<CityInfo>();
                    JSONArray jsonArray = jsonObject.optJSONArray("arList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        CityInfo cityInfo = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), CityInfo.class);
                        for (int j = 0; j < cityInfo.commList.size(); j++) {
                            cityInfo.commList.get(j).city = cityInfo.city;
                        }
                        cityInfos.add(cityInfo);
                    }
                    listener.onSuccess("", cityInfos);
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
     * 8103 实名制获取幢
     *
     * @param context
     * @param commcode 小区编号
     * @param listener
     */
    public void getBuild(Context context, String commcode, final ApiRequestListener<CommunityInfo> listener) {
        String paramStr = "type=8103&commcode=" + commcode;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                CommunityInfo communityInfo = new CommunityInfo();
                if (jsonObject.has("buildList")) {
                    communityInfo = new Gson().fromJson(jsonObject.toString(), CommunityInfo.class);
                    listener.onSuccess("", communityInfo);
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
     * 8104 实名制获取单元楼和房间号
     *
     * @param context
     * @param commcode  小区编号
     * @param buildcode 幢
     * @param listener
     */
    public void getUnitAndRoom(Context context, String commcode, String buildcode, final ApiRequestListener<ChooseRoomInfo> listener) {
        String paramStr = "type=8104&commcode=" + commcode + "&buildcode=" + buildcode;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                ChooseRoomInfo chooseRoomInfo = new Gson().fromJson(jsonObject.toString(),ChooseRoomInfo.class);
                if (chooseRoomInfo.getStatus().equals("1")&&jsonObject.has("arList")) {
                    listener.onSuccess(chooseRoomInfo.getMsg(), chooseRoomInfo);
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
     * 8312 获取住户地址列表
     *
     * @param context
     * @param commcode  小区编号
     * @param listener
     */
    public void getUserAddress(Context context, String commcode, final ApiRequestListener<AddressRoomInfo> listener) {
        String paramStr = "type=8312&commcode=" + commcode ;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                AddressRoomInfo addressRoomInfo = new Gson().fromJson(jsonObject.toString(),AddressRoomInfo.class);
                listener.onSuccess(addressRoomInfo.getMsg(), addressRoomInfo);
                /*if (jsonObject.optString("status").equals("1")&&jsonObject.has("buildingList")) {
                    listener.onSuccess(addressRoomInfo.getMsg(), addressRoomInfo);
                } else {
                    listener.onFailure(jsonObject.optString("msg"));
                }*/
            }
            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8105 实名制用户申诉提交
     *
     * @param context
     * @param name
     * @param commcode
     * @param buildingno
     * @param unit
     * @param room
     * @param description
     * @param listener
     */
    public void residentAppeal(Context context, String name, String commcode, String buildingno, String unit, String room, String description, final ApiRequestListener<String> listener) {
        String paramStr = "type=8105&name=" + name + "&commcode=" + commcode + "&buildingno=" + buildingno + "&unit=" + unit
                + "&room=" + room + "&description=" + description;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                listener.onSuccess("", jsonObject.optString("msg"));
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8106 获取实名制用户信息
     *
     * @param context
     * @param listener
     */
    public void getResidentBindingInfo(Context context, final ApiRequestListener<Map<String, Object>> listener) {
        String paramStr = "type=8106";
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("username", jsonObject.optString("username"));
                objectMap.put("tel", jsonObject.optString("tel"));
                objectMap.put("address", jsonObject.optString("address"));
                objectMap.put("isreal", jsonObject.optString("isreal", "0")); //是否实名制 0.否 1.是
                objectMap.put("isappeal", jsonObject.optString("isappeal", "0")); //是否已经申诉	0.否 1.是
                objectMap.put("bindtatus", jsonObject.optString("bindtatus", "0")); //绑定状态 0.没有申请解绑 1.绑定待审核
                objectMap.put("appealstatus", jsonObject.optString("appealstatus")); //申诉状态	0 正在处理 1.申诉成功 2.申诉失败
                objectMap.put("usercompareid", jsonObject.optString("usercompareid")); //实名制住户信息表id
                objectMap.put("remark", jsonObject.optString("remark")); //用户申请入户状态标识 0.通过 1.审核中
                objectMap.put("usertype", jsonObject.optString("usertype")); //用户类型 0.无 1.户主 2.成员 3.申请成员中 4.申诉中
                objectMap.put("housecode", jsonObject.optString("housecode")); //住房编码，在小区编码后面再加4位
                objectMap.put("housecodeX", jsonObject.optString("housecodeX")); //加密住房编码，housecode加密过的编码，64位字符串

                if (jsonObject.has("membersList")) { //指定用户成员信息
                    ArrayList<RealNameMember> membersList = new ArrayList<RealNameMember>();
                    JSONArray jsonArray = jsonObject.optJSONArray("membersList");
                    if (!(jsonArray.length() == 0)) {
                        for (int i = 0; i < jsonArray.length(); i++) {
//                            RealNameMember member = new Gson().fromJson(jsonArray.get(i).toString(), RealNameMember.class);
                            RealNameMember member = new RealNameMember((JSONObject) jsonArray.get(i));
                            membersList.add(member);
                        }
                    }
                    objectMap.put("membersList", membersList);
                }
                listener.onSuccess(jsonObject.optString("msg"), objectMap);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8107 获取实名制申诉信息
     *
     * @param context
     * @param listener
     */
    public void getResidentAppealInfo(Context context, final ApiRequestListener<Map<String, String>> listener) {
        String paramStr = "type=8107";
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Map<String, String> stringMap = new HashMap<String, String>();
                stringMap.put("username", jsonObject.optString("username"));
                stringMap.put("tel", jsonObject.optString("tel"));
                stringMap.put("address", jsonObject.optString("address"));
                stringMap.put("appealid", jsonObject.optString("appealid")); //申诉表id
                listener.onSuccess(jsonObject.optString("msg"), stringMap);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8108 取消申诉
     *
     * @param context
     * @param appealid
     * @param listener
     */
    public void cancelResidentAppeal(Context context, String appealid, final ApiRequestListener<String> listener) {
        String paramStr = "type=8108&appealid=" + appealid;
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
     * 8109 住户解绑申请
     *
     * @param context
     * @param usercompareid 实名制住户信息表id
     * @param description   描述
     * @param listener
     */
    public void residentUnbind(Context context, String usercompareid, String description, final ApiRequestListener<String> listener) {
        String paramStr = "type=8109&usercompareid=" + usercompareid + "&description=" + description;
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
     * 8110 取消申请解绑
     *
     * @param context
     * @param listener
     */
    public void cancelResidentUnbind(Context context, final ApiRequestListener<String> listener) {
        String paramStr = "type=8110";
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
     * 8111 获取app版本信息
     *
     * @param context
     * @param listener
     */
    public void getAppVersionInfo(Context context, final ApiRequestListener<Map<String, String>> listener) {
        String paramStr = "type=8111";
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Map<String, String> stringMap = new HashMap<String, String>();
                if (!jsonObject.has("avi")) {
                    listener.onSuccess("", stringMap);
                    return;
                }

                JSONObject jsonObjectAvi = jsonObject.optJSONObject("avi");
                stringMap.put("currentversion", jsonObjectAvi.optString("currentversion")); //当前版本
                stringMap.put("id", jsonObjectAvi.optString("id"));
                stringMap.put("description", jsonObjectAvi.optString("description")); //更新说明
                stringMap.put("latestversion", jsonObjectAvi.optString("latestversion")); //最新版本
                stringMap.put("updatemode", jsonObjectAvi.optString("updatemode")); //更新模式 0.强制 1.提醒 2.不提醒
                stringMap.put("url", jsonObjectAvi.optString("url")); //下载更新地址
                listener.onSuccess("", stringMap);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });

    }

    /**
     * 8112 户主添加成员
     *
     * @param context
     * @param phone
     * @param name
     * @param usercompareid 实名制住户信息表id
     * @param listener
     */
    public void addMember(Context context, String phone, String name, String usercompareid, final ApiRequestListener<String> listener) {
        String paramStr = "type=8112&phone=" + phone + "&name=" + name + "&usercompareid=" + usercompareid;
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
     * 8113 实名制成员申请审核
     *
     * @param context
     * @param usermemberid 成员表id
     * @param checktype    审核标识 0.不通过 1.通过
     * @param listener
     */
    public void checkMembersApply(Context context, String usermemberid, String phone, String checktype, final ApiRequestListener<String> listener) {
        String paramStr = "type=8113&usermemberid=" + "&phone=" + phone + "&usermemberid=" + usermemberid + "&checktype=" + checktype;
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
     * 8114 成员申请入户
     *
     * @param context
     * @param phone
     * @param name
     * @param usercompareid 实名制住户信息表id
     * @param listener
     */
    public void membersApply(Context context, String phone, String name, String usercompareid, final ApiRequestListener<String> listener) {
        String paramStr = "type=8114&phone=" + phone + "&name=" + name + "&usercompareid=" + usercompareid;
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
     * 8115 实名制成员获取申请列表
     *
     * @param context
     * @param usercompareid 实名制住户信息表id
     * @param listener
     */
    public void getMembersApplyList(Context context, String usercompareid, final ApiRequestListener<ArrayList<RealNameMember>> listener) {
        String paramStr = "type=8115&usercompareid=" + usercompareid;
        HttpPost(context, paramStr, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                ArrayList<RealNameMember> members = new ArrayList<RealNameMember>();
                if (jsonObject.has("membersApply")) {
                    JSONArray jsonArray = jsonObject.optJSONArray("membersApply");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            RealNameMember realNameMember = new Gson().fromJson(jsonArray.opt(i).toString(), RealNameMember.class);
                            members.add(realNameMember);
                        }
                    }
                }
                listener.onSuccess(jsonObject.optString("msg"), members);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 8116 户主删除成员
     *
     * @param context
     * @param usermemberid 成员表id
     * @param listener
     */
    public void deleteMember(Context context, String usermemberid, String phone, final ApiRequestListener<String> listener) {
        final String paramStr = "type=8116&usermemberid=" + usermemberid + "&phone=" + phone;
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
     * 8117 成员退出本户
     *
     * @param context
     * @param phone
     * @param listener
     */
    public void userMembersExit(Context context, String phone, final ApiRequestListener<String> listener) {
        String paramStr = "type=8117&phone=" + phone;
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
     * 8118 用户取消申请入户
     *
     * @param context
     * @param listener
     */
    public void membersCancelApply(Context context, final ApiRequestListener<String> listener) {
        String paramStr = "type=8118";
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
     * 8171  指定用户关键字模糊搜索（单元号、姓名、手机号等）
     *
     * @param context
     * @param buildcode 幢
     * @param commcode  小区编码
     * @param search    搜索关键字
     * @param listener
     */
    public void membersFuzzySearch(Context context, final String buildcode, String commcode, String search, final ApiRequestListener<SearchUserDataInfo> listener) {
        String strParam = "type=8171&buildcode=" + buildcode + "&commcode=" + commcode + "&search=" + search;
        HttpPost(context, strParam, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                Log.e("--搜索信息","- "+jsonObject.toString());
                SearchUserDataInfo searchUserDataInfo = new Gson().fromJson(jsonObject.toString(),SearchUserDataInfo.class);
                if (!jsonObject.has("arList")) {
                    listener.onFailure(jsonObject.optString("msg"));
                    return;
                }
                listener.onSuccess(jsonObject.optString("msg"), searchUserDataInfo);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }

    /**
     * 扫描用户码获取用户的详细信息
     * @param context
     * @param queryInfo
     * @param listener
     */
    public void getUserDetailsByHouseCode(Context context, final String queryInfo, final ApiRequestListener<UserDetailsByHouseCodeInfo> listener){
        String strParam = "type=8301&queryInfo=" + queryInfo;
        HttpPost(context, strParam, new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                UserDetailsByHouseCodeInfo mUserDetailsByHouseCodeInfo = new Gson().fromJson(jsonObject.toString(),UserDetailsByHouseCodeInfo.class);
                listener.onSuccess(jsonObject.optString("msg"), mUserDetailsByHouseCodeInfo);
            }

            @Override
            public void onFailure(String msg) {
                listener.onFailure(msg);
            }
        });
    }
}
