package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * Created by 95138 on 2016/11/14.
 */

public class ChooseEqDetailInfo implements Serializable {
    private String amenityAreaId;  //设施点ID
    private String amenityName;    //设施名
    private int amenityNum;     //设施数量
    private String amenityid;      //设施ID
    private String ischeck;        //是否巡检过
    private String classgistTypeid;  //条例查询类型

    public String getClassgistTypeid() {
        return classgistTypeid;
    }

    public void setClassgistTypeid(String classgistTypeid) {
        this.classgistTypeid = classgistTypeid;
    }

    public String getAmenityAreaId() {
        return amenityAreaId;
    }

    public void setAmenityAreaId(String amenityAreaId) {
        this.amenityAreaId = amenityAreaId;
    }

    public String getAmenityName() {
        return amenityName;
    }

    public void setAmenityName(String amenityName) {
        this.amenityName = amenityName;
    }

    public int getAmenityNum() {
        return amenityNum;
    }

    public void setAmenityNum(int amenityNum) {
        this.amenityNum = amenityNum;
    }

    public String getAmenityid() {
        return amenityid;
    }

    public void setAmenityid(String amenityid) {
        this.amenityid = amenityid;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }
}
