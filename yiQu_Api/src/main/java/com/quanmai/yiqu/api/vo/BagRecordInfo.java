package com.quanmai.yiqu.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/6/7.  取袋详情实体类
 */

public class BagRecordInfo implements Serializable {

    /**
     * barcodesList : ["11245678901290","11245678901291","11245678901292","11245678901293","11245678901294","11245678901295","11245678901296","11245678901297","11245678901298","11245678901299","11245678901300","11245678901301","11245678901302","11245678901303","11245678901304"]
     * max_page : 2
     * msg :  成功
     * response_time : 1496393842168
     * status : 1
     */

    private int max_page;
    private String msg;
    private long response_time;
    private String status;
    private List<String> barcodesList;

    public int getMax_page() {
        return max_page;
    }

    public void setMax_page(int max_page) {
        this.max_page = max_page;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getResponse_time() {
        return response_time;
    }

    public void setResponse_time(long response_time) {
        this.response_time = response_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getBarcodesList() {
        return barcodesList;
    }

    public void setBarcodesList(List<String> barcodesList) {
        this.barcodesList = barcodesList;
    }
}
