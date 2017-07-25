package com.quanmai.yiqu.api.vo;

/**
 * 商家信息
 * Created by zhanjinj on 16/5/30.
 */
public class ShopInfo {
    //用于周边——商家列表
    public String address;
    public String communityId;
    public String communityName;
    public String id;
    public String memo;
    public String name;
    public String phone;
    public String shoppic;
    public String type;

    //用于我的优惠卷——收藏优惠卷商家列表
    public String collectNum; //收藏次数
    public String shopId;
    public String shopName;
    public String shopPic;
}
