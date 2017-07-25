package com.quanmai.yiqu.api.vo;

import java.io.Serializable;

/**
 * Created by James on 2016/8/19.
 * 答题赚积分--选项实体
 */
public class Option implements Serializable {
    public String content; //选项内容
    public String id;
    public String optionno;  //选项序号
    public String questionid;  //题目id
    public String url; //图片地址
    public Boolean isChecked; //是否选中
}
