package com.quanmai.yiqu.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 95138 on 2016/11/14.
 */

public class ChooseEqInfo implements Serializable {
    private String ischeck;  //是否巡检完
    private String letter;   //小区名
    private List<ChooseEqDetailInfo> list;  //设备列表

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public List<ChooseEqDetailInfo> getList() {
        return list;
    }

    public void setList(List<ChooseEqDetailInfo> list) {
        this.list = list;
    }
}
