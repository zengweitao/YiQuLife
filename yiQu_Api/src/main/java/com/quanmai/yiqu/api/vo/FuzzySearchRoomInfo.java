package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * 指定用户-模糊搜索房间号信息实体类
 * Created by Kin on 2017/3/3.
 */

public class FuzzySearchRoomInfo implements Serializable {

    /**
     * "mtel": "13705713489",
     * "name": "杨耀俊",
     * "room": "1002室",
     * "unit": "1单元"
     */

    public String mtel;
    public String name;
    public String room;
    public String unit;
}
