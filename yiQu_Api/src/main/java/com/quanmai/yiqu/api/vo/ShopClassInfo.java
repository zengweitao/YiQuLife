package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * （周边）商家分类实体类
 * Created by James on 2016/9/13.
 */
public class ShopClassInfo implements Serializable {
    public String id;   //类型id
    public String type; //类型名
    public boolean isCheck = false; //是否被选中
}
