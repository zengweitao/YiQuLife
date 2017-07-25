package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * Created by Kin on 2017/3/8.
 * 指定用户信息
 */

public class AppointedUserInfo implements Serializable {

    /**
     * buildingno : 92
     * commname : 连园小区
     * msg : 查询成功
     * mtel : 13790303985
     * name : 金佳
     * response_time : 1488962781735
     * room : 102
     * status : 1
     * unit : 1
     * housecode:330108001020010001
     * housecodex:52F1672DD750B4E2156AD3CF7C315A58C1205A979E768B4DE4E9264AD33DFC64
     */

    public String buildingno;
    public String commname;
    public String msg;
    public String mtel;
    public String name;
    public long response_time;
    public String room;
    public String status;
    public String unit;
    public String housecode;    //住户码
    public String housecodex;   //住户码（加密）

    public String getBuildingno() {
        return buildingno;
    }

    public void setBuildingno(String buildingno) {
        this.buildingno = buildingno;
    }

    public String getCommname() {
        return commname;
    }

    public void setCommname(String commname) {
        this.commname = commname;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMtel() {
        return mtel;
    }

    public void setMtel(String mtel) {
        this.mtel = mtel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getResponse_time() {
        return response_time;
    }

    public void setResponse_time(long response_time) {
        this.response_time = response_time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getHousecode() {
        return housecode;
    }

    public void setHousecode(String housecode) {
        this.housecode = housecode;
    }

    public String getHousecodex() {
        return housecodex;
    }

    public void setHousecodex(String housecodex) {
        this.housecodex = housecodex;
    }
}
