package com.quanmai.yiqu.common.widget;

import java.io.Serializable;
import java.util.Map;

/**
 * 实现序列化的工具类，用以在intent中传送map类
 * Created by James on 2016/10/17.
 */

public class SerializableMap implements Serializable {
    private Map<String, Object> mMap;

    public void setMap(Map<String, Object> map) {
        this.mMap = map;
    }

    public Map<String, Object> getMap() {
        return mMap;
    }
}
