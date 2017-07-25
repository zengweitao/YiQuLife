package com.quanmai.yiqu.api.vo;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by James on 2016/8/2.
 * 实名制成员实体类
 */
public class RealNameMember implements Serializable {
    public String imgurl; //头像
    public String phone; //电话
    public String name; //姓名
    public String type; //用户类型 0.普通成员 1.户主
    public String usermemberid; //成员信息表id

    public RealNameMember() {

    }

    public RealNameMember(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.length() == 0) {
            return;
        }
        this.imgurl = jsonObject.optString("imgurl");
        this.phone = jsonObject.optString("phone");
        this.name = jsonObject.optString("name");
        this.type = jsonObject.optString("type");
        this.usermemberid = jsonObject.optString("usermemberid");
    }

    public boolean compare(RealNameMember realNameMember) {
        if (realNameMember == null) {
            return false;
        }
        if (this.imgurl.equals(realNameMember.imgurl)
                && this.phone.equals(realNameMember.phone)
                && this.name.equals(realNameMember.name)
                && this.type.equals(realNameMember.type)
                && this.usermemberid.equals(realNameMember.usermemberid)) {
            return true;
        } else {
            return false;
        }
    }
}
