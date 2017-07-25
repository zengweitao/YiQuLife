package com.quanmai.yiqu.api.vo;

import com.quanmai.yiqu.base.CommonList;

import java.io.Serializable;

/**
 * Created by James on 2016/7/7.
 * 城市实体类
 */
public class CityInfo implements Serializable {
    public String city; //城市名
    public CommonList<CommunityInfo> commList; //小区列表

    public CityInfo() {
        this.city = "";
        this.commList = new CommonList<>();
    }
}
