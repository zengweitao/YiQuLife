package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * 巡检扣分条例实体
 * Created by 95138 on 2016/11/15.
 */

public class PointPenaltyInfo implements Serializable {
    private String checkname;  //条例名
    private String id;             //id
    private String score;         //条例分值
    private Boolean isCheck;   //是否选中（用于清运评分）

    public PointPenaltyInfo(){
        isCheck=false;
    }

    public String getCheckname() {
        return checkname;
    }

    public void setCheckname(String checkname) {
        this.checkname = checkname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }
}
