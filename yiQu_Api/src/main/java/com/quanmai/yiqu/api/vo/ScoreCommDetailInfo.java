package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * 巡检-选择小区-小区详细信息
 * Created by 95138 on 2016/11/14.
 */

public class ScoreCommDetailInfo implements Serializable{
    private String commcode; //编码
    private String commname; //小区名
    private String firstpy; //小区所属范围--首字母

    public String getCommcode() {
        return commcode;
    }

    public void setCommcode(String commcode) {
        this.commcode = commcode;
    }

    public String getCommname() {
        return commname;
    }

    public void setCommname(String commname) {
        this.commname = commname;
    }

    public String getFirstpy() {
        return firstpy;
    }

    public void setFirstpy(String firstpy) {
        this.firstpy = firstpy;
    }
}
