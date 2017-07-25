package com.quanmai.yiqu.api.vo;

import com.quanmai.yiqu.base.CommonList;

import java.io.Serializable;
import java.util.List;

/**
 * Created by James on 2016/7/7.
 * 楼栋实体类
 */
public class BuildInfo implements Serializable{

    public String commcode; //（上级）小区编号
    public String commname; //（上级）小区名
    public String buildName; //栋号
    public CommonList<UnitInfo> arList; //单元列表

    public BuildInfo(){
        arList = new CommonList<>();
    }
}
