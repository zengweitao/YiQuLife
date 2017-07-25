package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * 资讯信息实体
 * Created by zhanjinj on 16/5/30.
 */
public class NewsInfo implements Serializable {
    public String img;              //封面图片
    public String thumbnail;        //缩略图
    public String title;            //标题
    public String tag;              //标签
    public String described;        //说明简介
    public String detainstructions; //详细说明
    public String beginTime;        //有效日期起
    public String endTime;          //有效日期止
    public String linkUrl;          //外链地址（可选）
    public String userId;           //操作人
    public String opeTime;          //操作时间
    public String msg;              //说明
    public String status;           //优惠券状态 0.未使用 1.已使用
    public String accTimes;         //浏览次数
    public String share_link;       //分享链接
    public String id;               //id
    public String top;              //置顶 0否 1是
    public String special;          //是否专题 0否 1是
    public String specialName;      //专题名
    public String type;             //类型 1.资讯 2.团购
    public String shopname;         //商家名
    public String tel;              //商家电话
    public String address;          //商家地址
    public String oriprice;         //原价
    public String groupprice;       //团购价
    public String isgroup;          //是否参加该团购 1.已参加团购 0.未参加
    public String applynums;        //团购人数
    public String groupstatus;      //团购状态   1.报名中  2.已开团    3.已完成
}
