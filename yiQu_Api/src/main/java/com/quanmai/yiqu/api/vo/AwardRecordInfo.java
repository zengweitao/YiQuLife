package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * 礼品记录信息
 * Created by 95138 on 2017/3/2.
 */

public class AwardRecordInfo implements Serializable{
    private String amount;   //益币扣除数量
    private String giftName;  //礼物名称
    private String houseCode;  //住户编号
    private String inputTime;  //赠送时间
    private String content;    //益币兑换内容
    private String id;
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getInputTime() {
        return inputTime;
    }

    public void setInputTime(String inputTime) {
        this.inputTime = inputTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
