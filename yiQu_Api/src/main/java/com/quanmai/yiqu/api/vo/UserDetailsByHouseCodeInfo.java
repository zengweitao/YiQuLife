package com.quanmai.yiqu.api.vo;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chasing-Li on 2017/5/18.
 */

public class UserDetailsByHouseCodeInfo implements Serializable {

    /**
     * amount : 31.0
     * commcode : 33010800102001
     * community : 连园小区
     * concernsinfoRespList : [{"fednums":"0","months":"01"},{"fednums":"0","months":"02"}]
     * freefednums : 0
     * gender : 1
     * growthvalue : 23984.0
     * hoursenum : 90幢1单元100
     * houseCode : 330108001020018888
     * houseCodeX : 5CBFE4CE7CF70942AFDD6AC021D129078784A99B78680C596C6E90684C627A6F
     * img_address : http://img.eacheart.com/
     * imgurl : ios_images/images0-20160411100479.png
     * level : 6
     * level_introducton : 每日可领取100个环保袋
     发布闲置物品优先推荐
     连续签到10天额外送25积分25成长值
     * msg : 成功
     * nickname : 春风小子测试
     * response_time : 1495017193349
     * score : 23159.0
     * status : 1
     * userName : 益趣科技
     * userPhone : 18958111903
     * vipname : LV6
     */
    private static UserDetailsByHouseCodeInfo mInstance;
    public String amount; //用户益币数
    public String commcode; //小区编号
    public String community; //小区名
    public int freefednums; //用户免费福袋数
    public String gender; //性别
    public float growthvalue; //成长值
    public String hoursenum; //门牌号
    public String houseCode; //用户编码
    public String houseCodeX; //用户编译后的编码
    public String img_address; //获取图片地址
    public String imgurl; //图片名
    public String level; //积分等级:1,普通2.黄金3.铂金.4钻石
    public String level_introducton; //等级介绍
    public String msg; //返回信息
    public String nickname; //昵称
    public long response_time; //返回时间
    public String score; //用户积分
    public String status; //返回状态值
    public String userName; //用户姓名
    public String userPhone; //用户手机号
    public String vipname; //VIP等级名  “LV6”
    public List<ConcernsinfoRespListBean> concernsinfoRespList; //福袋使用详情按月查询列表
    public ConcernsinfoRespListBean mConcernsinfoRespListBean; //单个月使用福袋情况

    public UserDetailsByHouseCodeInfo() {
        amount = "0.0";
        commcode = "";
        community = "益趣科技";
        freefednums = 0;
        gender = "1";
        growthvalue = 0;
        hoursenum = "";
        img_address = "https://imgs.eacheart.com:9444/";
        houseCode = "";
        houseCodeX = "";
        imgurl = "";
        level = "";
        level_introducton = "";
        msg = "";
        nickname = "";
        response_time = 0;
        score = "0.0";
        status = "1";
        userName = "";
        userPhone = new String();
        vipname = new String();
        mConcernsinfoRespListBean = new ConcernsinfoRespListBean();
        concernsinfoRespList = new ArrayList<>();
    }

    public UserDetailsByHouseCodeInfo(JSONObject jsonObject) throws JSONException {
        UserDetailsByHouseCodeInfo mUserDetailsByHouseCodeInfo = new Gson().fromJson(jsonObject.toString(), UserDetailsByHouseCodeInfo.class);
        mInstance = mUserDetailsByHouseCodeInfo;
    }

    public static UserDetailsByHouseCodeInfo get() {
        if (mInstance == null) {
            mInstance = new UserDetailsByHouseCodeInfo();
        }
        return mInstance;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCommcode() {
        return commcode;
    }

    public void setCommcode(String commcode) {
        this.commcode = commcode;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public int getFreefednums() {
        return freefednums;
    }

    public void setFreefednums(int freefednums) {
        this.freefednums = freefednums;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getGrowthvalue() {
        return growthvalue;
    }

    public void setGrowthvalue(float growthvalue) {
        this.growthvalue = growthvalue;
    }

    public String getHoursenum() {
        return hoursenum;
    }

    public void setHoursenum(String hoursenum) {
        this.hoursenum = hoursenum;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getHouseCodeX() {
        return houseCodeX;
    }

    public void setHouseCodeX(String houseCodeX) {
        this.houseCodeX = houseCodeX;
    }

    public String getImg_address() {
        return img_address;
    }

    public void setImg_address(String img_address) {
        this.img_address = img_address;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel_introducton() {
        return level_introducton;
    }

    public void setLevel_introducton(String level_introducton) {
        this.level_introducton = level_introducton;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getResponse_time() {
        return response_time;
    }

    public void setResponse_time(long response_time) {
        this.response_time = response_time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getVipname() {
        return vipname;
    }

    public void setVipname(String vipname) {
        this.vipname = vipname;
    }

    public List<ConcernsinfoRespListBean> getConcernsinfoRespList() {
        return concernsinfoRespList;
    }

    public void setConcernsinfoRespList(List<ConcernsinfoRespListBean> concernsinfoRespList) {
        this.concernsinfoRespList = concernsinfoRespList;
    }

    public static class ConcernsinfoRespListBean implements Serializable{
        /**
         * fednums : 0
         * months : 01
         */

        private String fednums;
        private String months;

        public String getFednums() {
            return fednums;
        }

        public void setFednums(String fednums) {
            this.fednums = fednums;
        }

        public String getMonths() {
            return months;
        }

        public void setMonths(String months) {
            this.months = months;
        }
    }
}
