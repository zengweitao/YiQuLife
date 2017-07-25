package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * Created by James on 2016/7/8.
 * 房号实体类
 */
public class RoomInfo implements Serializable {
    public String city = ""; //城市名
    public String commname = ""; //小区名
    public String commcode = ""; //小区编码
    public String buildingno = ""; //幢号
    public String buildno = ""; //单元号
    public String room = ""; //房号
    public String name = ""; //用户名
    public String phone = ""; //电话
    public String usercompareid = ""; //id

    public String toString() {
        return city + " " + commname + " " + buildingno + " " + buildno + " " + room;
    }
}
