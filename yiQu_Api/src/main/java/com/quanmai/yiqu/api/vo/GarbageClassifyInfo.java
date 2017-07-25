package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * Created by 殷伟超 on 2016/6/28.
 */
public class GarbageClassifyInfo implements Serializable {
    public String saclass;  //分类数组
    public String code;     //分类编号Id
    public String name;     // 分类名称
    public String img;      //分类图片
    public String url;      //分类跳转链接
    public String depict;   //分类描述

    //可回收垃圾分类信息
    public String id;       //
    public String image;    //图片
    public String number;   //编码
    public String optime;   //描述

}
