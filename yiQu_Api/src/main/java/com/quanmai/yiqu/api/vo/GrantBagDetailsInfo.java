package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * 发放环保袋详情实体类
 * Created by James on 2016/10/14.
 */

public class GrantBagDetailsInfo implements Serializable {
    public String opetime;          //时间
    public String materialsource;  //物料来源（1.接收 2.发放）
    public String bagnums;         //数量
    public String bagtype;         //垃圾袋类型:1.厨余垃圾 2其他垃圾 3.有害垃圾 4.可回收 5.通用 6.通用（带收紧带）,默认是厨余垃圾
    public String place;           //袋子去处
}
