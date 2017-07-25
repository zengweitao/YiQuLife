package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

public class ScorePayInfo {
    public String successimg;
    public int balancePoints;

    public ScorePayInfo(JSONObject jsonObject) throws JSONException {
        successimg = jsonObject.optString("successimg");
        balancePoints = jsonObject.getInt("balancePoints");
    }
}