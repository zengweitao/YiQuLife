package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Noticeinfo implements Serializable {
    private static final long serialVersionUID = -5243999537423731878L;
    public Noticeinfo(JSONObject object) throws JSONException {
        notice_link = object.getString("notice_link");
        notice_name = object.getString("notice_name");
        notice_content = object.getString("notice_content");
        notice_time = object.getString("notice_time");
    }

    /**
     * 题目
     */
    public String notice_name;
    /**
     * 通知内容
     */
    public String notice_content;
    /**
     * 通知链接
     */
    public String notice_link;
    /**
     * 通知时间
     */
    public String notice_time;
}
