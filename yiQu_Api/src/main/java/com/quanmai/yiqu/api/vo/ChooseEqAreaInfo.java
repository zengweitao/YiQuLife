package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * Created by James on 2016/11/15.
 * 巡检-清运评分 设施点实体类
 */

public class ChooseEqAreaInfo implements Serializable {

    /**
     * amenityareaName : 灯塔豪园B
     * id : 7
     * ischeck : 0
     */

    private String amenityareaName; //设施点名称
    private int amenityareaid;      //设施点id
    private String ischeck;         //是否已巡检

    public String getAmenityareaName() {
        return amenityareaName;
    }

    public void setAmenityareaName(String amenityareaName) {
        this.amenityareaName = amenityareaName;
    }

    public int getId() {
        return amenityareaid;
    }

    public void setId(int amenityareaid) {
        this.amenityareaid = amenityareaid;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }
}
