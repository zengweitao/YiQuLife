package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * Created by James on 2016/11/14.
 */

public class GradeScoreDetailsInfo implements Serializable {

    /**
     * gitName : 设施实际数量
     * id : 16
     * num : 5
     * score : 0.5
     */

    public String gitName;  //扣分条例名称
    public int id;
    public int num;         //设施点扣分数量
    public String score;    //所扣分值

    public GradeScoreDetailsInfo() {
        gitName = "";
        id = 0;
        num = 0;
        score = "0";
    }
}
