package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * Created by James on 2016/11/14.
 * 设备、清运记录实体类
 */

public class GradeScoreInfo implements Serializable {

    /**
     * amenityName     ：垃圾桶
     * amenityareaName : 灯塔豪园C
     * checkImg :"strImg,strImg,strImg" 逗号隔开
     * cleanupScore : 3
     * datetime : 2016-04-11 15:32
     * id : 6
     */

    public String amenityName;      //设施名
    public String amenityareaName;  //设施点名称
    public String checkImg;         //图片
    public double checkScore;          //评分
    public String datetime;         //日期
    public int id;                  //打分记录表ID
}
