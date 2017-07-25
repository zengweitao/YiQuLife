package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * Created by James on 2016/11/15.
 * 个人取袋记录实体类
 */

public class FetchBagRecordInfo implements Serializable {
    public String terminalno;   //设备编号
    public int nums;            //取袋数量
    public String barcode;      //垃圾袋起始编号
    public String opetime;      //取袋时间
}
