package com.quanmai.yiqu.api.vo;

import com.quanmai.yiqu.base.CommonList;

/**
 * 资讯信息列表实体
 * Created by zhanjinj on 16/5/30.
 */
public class NewsListInfo {
    public int current_page;
    public int max_page;
    public String msg;
    public CommonList<NewsInfo> infoList;
}
