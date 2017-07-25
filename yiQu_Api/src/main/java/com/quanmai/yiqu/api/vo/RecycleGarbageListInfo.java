package com.quanmai.yiqu.api.vo;

import com.quanmai.yiqu.base.CommonList;

import java.io.Serializable;

/**
 * Created by 殷伟超 on 2016/6/28.
 */
public class RecycleGarbageListInfo implements Serializable{
    private String id;
    private String typeName;
    private CommonList<RecycleGarbagesInfo> garbageList;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public CommonList<RecycleGarbagesInfo> getGarbageList() {
        return garbageList;
    }

    public void setGarbageList(CommonList<RecycleGarbagesInfo> garbageList) {
        this.garbageList = garbageList;
    }
}
