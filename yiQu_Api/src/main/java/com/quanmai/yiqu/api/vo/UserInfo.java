package com.quanmai.yiqu.api.vo;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {
    public final static String USER_ALL =  "-1";         //拥有所有权限
    public final static String USER_GENERAL = "0";       //0 普通用户
    public final static String USER_RECONDITION = "1";   //1 检修员
    public final static String USER_INSPECTION = "2";    //2 巡检员
    public final static String USER_RECYCLE = "3";       //3 回收人员
    public final static String USER_MANAGER = "4";       //4 积分赠送管理员
    public final static String USER_HANDWORK = "5";      //5 手工发袋管理员
    public final static String USER_WAREHOUSE = "6";     //6 入库管理员
    public final static String USER_GIFT_GIVING= "7";    //7 礼品赠送员（益币）

    public static String prefixQiNiu = "https://imgs.eacheart.com:9444/"; //域名链接:七牛图片存储路径前缀


    private static UserInfo mInstance;
    public String img_address;          // 域名链接:七牛图片存储路径
    public String phone;
    public int sex;
    public int score;
    public int growthvalue;             //成长值
    public String birth;
    public String face;
    public String alias;                // 用户昵称
    public String username;             // 用户姓名
    public String usertype;             // 0普通用户，1检修员，2 巡检员
    public String share_url;
    public int freefednums;             //当月剩余的免费福袋
    public int level;                   // 1:普通会员2:黄金会员,3:铂金会员,4:钻石会员
    public String vipname;              // 普通会员,黄金会员,铂金会员,钻石会员
    public int sign;                    // 是否签到 -1:默认值 0:未签到.1:已签到
    public VipInfo vipInfo;
    public String level_introducton;    //等级介绍
    public String isbind;               //是否绑定 0未绑定 1绑定 2绑定小区且小区已开通废品回收功能
    public String userid;
    public String commcode;             //用户所在小区编码
    public String community;            //用户所在小区
    public List<Tag> tag;               //标签，其目的主要是方便开发者根据标签，来批量下发 Push 消息
    public int amount;
    public String houseCode;            //绑定住户的编码
    public String houseCodeX;           //绑定住户的二维码字符串
    public String isScored;             //是否有个人评分的权限
    public String isGameContest;             //本小区是否开通游戏竞赛


    public UserInfo() {
        userid = "";
        phone = new String();
        sex = 0;
        score = 0;
        growthvalue = 0;
        score = 0;
        sign = -1;
        img_address = "https://imgs.eacheart.com:9444/";
        usertype = "0";
        face = new String();
        birth = new String();
        alias = "未设置";
        freefednums = 0;
        level = 0;
        vipname = new String();
        share_url = new String();
        vipInfo = new VipInfo();
        level_introducton = "";
        isbind = "0";
        commcode = "";
        community = "";
        tag = new ArrayList<>();
        amount = 0;
        houseCode = "";
        houseCodeX = "";
        isScored = "0";
        isGameContest = "0";
    }

    public UserInfo(JSONObject jsonObject) throws JSONException {
        UserInfo userInfo = new Gson().fromJson(jsonObject.toString(), UserInfo.class);
        userInfo.vipInfo = new VipInfo();
        userInfo.vipInfo.setLevel(userInfo.getLevel());
        mInstance = userInfo;
    }

    public static UserInfo get() {
        if (mInstance == null) {
            mInstance = new UserInfo();
        }
        return mInstance;
    }

    public String getIsGameContest() {
        return isGameContest;
    }

    public void setIsGameContest(String isGameContest) {
        this.isGameContest = isGameContest;
    }

    public String getIsScored() {
        return isScored;
    }

    public void setIsScored(String isScored) {
        this.isScored = isScored;
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

    public String getHouseCodeX() {
        return houseCodeX;
    }

    public void setHouseCodeX(String houseCodeX) {
        this.houseCodeX = houseCodeX;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public static void clean() {
        mInstance = new UserInfo();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getIsbind() {
        return isbind;
    }

    public void setIsbind(String isbind) {
        this.isbind = isbind;
    }

    public static String getUserGeneral() {
        return USER_GENERAL;
    }

    public String getImg_address() {
        return img_address;
    }

    public void setImg_address(String img_address) {
        this.img_address = img_address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getFreefednums() {
        return freefednums;
    }

    public void setFreefednums(int freefednums) {
        this.freefednums = freefednums;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getVipname() {
        return vipname;
    }

    public void setVipname(String vipname) {
        this.vipname = vipname;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public class Tag{
        public String tagkey;       //标签key
        public String tagvalue;     //标签值
    }

}
