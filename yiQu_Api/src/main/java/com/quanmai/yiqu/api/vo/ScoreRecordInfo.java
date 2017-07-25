package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 评分明细实体类
 */
public class ScoreRecordInfo implements Serializable{
    public ScoreRecordInfo(JSONObject jsonObject) throws JSONException {
        barcode = jsonObject.getString("barcode");
        score = jsonObject.getString("score");
        opetime = jsonObject.getString("opetime");
        address = jsonObject.optString("address");
        img = jsonObject.optString("img");
        ctext = jsonObject.optString("context");
    }

    public String barcode; //袋子编号
    public String score; //分类得分
    public String opetime; //得分日期
    public String address; //地址
    public String img; //图片  XXX.jpg,XXx.png
    public String ctext; //评论
}
