package com.quanmai.yiqu.api.vo;

import java.util.List;

/**
 * Created by zhanjinj on 16/5/27.
 */
public class BannerTwoInfo {
    public String msg;
    public String response_time;
    public String status;
    public List<info> infoList;

    public class info {
        public String id; //用户id
        public String linkUrl; //外链地址
        public String num; //序号
        public String img; //图片
        public String status; //优惠劵状态
        public String opeTime; //操作时间
        public String type; //类别  “1”：优惠券  “2”：资讯 “3”：跳外链接
    }

}
