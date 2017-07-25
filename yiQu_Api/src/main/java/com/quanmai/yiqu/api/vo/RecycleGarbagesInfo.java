package com.quanmai.yiqu.api.vo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by 95138 on 2016/4/19.
 */
public class RecycleGarbagesInfo implements Serializable {
    private static final long serialVersionUID = -192301231265758483L;
    public String id;
    public String garbage; //物品名
    public String type;//物品描述 例：纸箱大小必须小于17cm
    public String point; //该废品每个所能获得的积分
    public String pic; //图片路径
    public String unit; //单位
    public String quantity; //数量
    public boolean isClick;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGarbage() {
        return garbage;
    }

    public void setGarbage(String garbage) {
        this.garbage = garbage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public RecycleGarbagesInfo(JSONObject jsonObject) throws JSONException {
        id = jsonObject.optString("id");
        garbage = jsonObject.optString("garbage");
        type = jsonObject.optString("type");
        point = jsonObject.optString("point");
        pic = jsonObject.optString("pic");
        unit = jsonObject.optString("unit");
        quantity = jsonObject.optString("quantity");
    }
}
