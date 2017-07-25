package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * Created by 95138 on 2016/4/19.
 */
public class BookingAddress implements Serializable {
    private static final long serialVersionUID = -192308161264558486L;
    public String name;
    public String phone;
    public String community;
    public String detailAddress;

    public BookingAddress(String name,String phone,String community,String detailAddress){
        this.name = name;
        this.phone = phone;
        this.community = community;
        this.detailAddress = detailAddress;
    }

}
