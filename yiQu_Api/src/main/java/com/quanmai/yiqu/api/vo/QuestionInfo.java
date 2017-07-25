package com.quanmai.yiqu.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by James on 2016/8/19.
 * 答题赚积分--问题实体
 */
public class QuestionInfo implements Serializable {
    public String adurl; //广告链接
    public String answer; //答案
    public String question;  //题目内容
    public String questionid;  //题目id
    public String isradio;  //是否单选：0.单选 1.多选
    public String optiontype; //题目选项内容：0.文字 1.图片
    public String phone;
    public int points;  //题目分值
    public String urlAlert; //弹窗图片
    public String urladpic; //广告大图
    public String urlicon; //题目图标
    public List<Option> optionsList; //题目选项内容
}
