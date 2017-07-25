package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class BagInfo implements Serializable {
    public static final String TYPE_BAG = "1"; //福二袋A
    public static final String TYPE_PAPER = "2"; //纸要你
    public static final String TYPE_SMART_BAG = "5"; //福二袋B、"8" 福二袋C

    public int balancePoints;   //用户剩余积分
    public String name;
    public String img;          //提示大图
    public int count;           //数量,每天最多可兑换数量
    public int score;           //所需积分,多少积分兑换一个物品
    public int point;           //用户可用积分
    public int fednum;          //用户可用福袋
    public String adscription;  //商品类型: 1.福二代（发袋单位：个） 2.纸要你 5.智能发袋机（发袋单位：卷）
    public int type;            //出袋类型：0.不可选 1.可选
    public String singlepoint;  //单个福袋对应积分

    private static final long serialVersionUID = 5197475638468277645L;

    public BagInfo(JSONObject jsonObject) throws JSONException {
        name = jsonObject.optString("name");
        img = jsonObject.optString("img");
        count = jsonObject.optInt("count");
        score = jsonObject.optInt("score");
        point = jsonObject.optInt("point");
        fednum = jsonObject.optInt("fednum");
        adscription = jsonObject.optString("adscription");
        type = jsonObject.optInt("type", 1);
        singlepoint = jsonObject.optString("singlepoint");
    }

}
