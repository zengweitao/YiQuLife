package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * Created by zhanjinj on 16/5/30.
 */
public class CouponInfo implements Serializable {
    public String id;               //自增id
    public String shopid;           //商家id
    public String privilegeName;    //优惠名称
    public String price;            //优惠价格
    public String privilegePrice;
    public String described;        //优惠详细说明
    public String privilegedetail;  //优惠详细说明
    public String privilegeTitle;   //优惠标题
    public String titleimg;         //标题图片
    public String thumbnail;        //缩略图
    public String tag;              //标签
    public String startTime;        //优惠有效起始日期
    public String endTime;          //优惠有效截止日期
    public String linkUrl;          //外链地址
    public String type;             //类型
    public String couponQuantity;   //总库存
    public String couponnum;        //优惠券剩余库存
    public String createTime;       //创建时间
    public String createUser;       //创建人
    public String status;           // 是否过期-收藏标志  0—*未过期 1-*已过期 *-0 -未收藏 *-1-已收藏
    public String msg;              //
    public String flag;             //0 直接展示  1 外链接
    public String share_link = "";  //分享链接


}
