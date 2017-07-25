package com.quanmai.yiqu.api.vo;

import com.quanmai.yiqu.base.CommonList;

import java.util.List;

/**
 * 商家信息列表
 * Created by zhanjinj on 16/5/30.
 */
public class ShopListInfo {
    public static final String typeBusiness = "0"; //全网商家type
    public static final String typeAround = "1"; //周边商家type

    public String msg;
    public List<Data> data;

    public class Data {
        public CommonList<ShopInfo> infoList;
        public String type;
    }
}
