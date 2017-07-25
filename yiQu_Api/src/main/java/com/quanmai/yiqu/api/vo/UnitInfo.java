package com.quanmai.yiqu.api.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2016/7/7.
 * 单元实体类
 */
public class UnitInfo implements Serializable {

    public String buildName; //（上级）栋号
    public String buildno; //单元号
    public List<String> roomList; //房号
    public UnitInfo(){
        roomList = new ArrayList<>();
    }
}
