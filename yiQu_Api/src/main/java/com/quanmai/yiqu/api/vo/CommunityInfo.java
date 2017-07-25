package com.quanmai.yiqu.api.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2016/7/7.
 * 小区实体类
 */
public class CommunityInfo implements Serializable {
    public String city; //（上级）城市名
    public String commcode; //小区编号
    public String commname; //小区名
    public List<String> buildList;  //小区所有的幢数

    public CommunityInfo(){
        this.buildList = new ArrayList<>();
    }
}
