package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * Created by zhanjinj on 16/3/31.
 * 会员特权说明
 */
public class Member implements Serializable {
    public String vipcode;      //会员等级
    public String vipprivilege; //会员特权
    public int fednums;         //福利数
    public int id;
    public int lowpoint;        //最小积分
    public int maxpoint;        //最大积分
    public String opetime;
    public String vipname;      //VIP名
}
