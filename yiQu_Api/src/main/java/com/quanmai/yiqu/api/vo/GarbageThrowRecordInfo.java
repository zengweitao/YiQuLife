package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * 垃圾投放-投放记录实体类
 * Created by James on 2016/12/29.
 */

public class GarbageThrowRecordInfo implements Serializable {
    public String garbagname;   //名称
    public String weight;       //重量
    public String opetime;      //时间
    public String status;       //状态 0.未确认 1.已确认
    public String points;       //积分
}
