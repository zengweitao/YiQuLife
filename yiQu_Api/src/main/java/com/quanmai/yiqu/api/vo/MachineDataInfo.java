package com.quanmai.yiqu.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chasing-Li on 2017/5/20.
 */

public class MachineDataInfo implements Serializable{

    /**
     * firstChannelNum : ["Z2LY0120171554","160120009900","Z2LY0120181554","160711170555","160120009960","160120010000","160711170544","160120010029"]
     * fourthChannelNum : ["92170100000143","T1170500000096","160711170532"]
     * msg : 上架成功
     * response_time : 1495506225590
     * secondChannelNum : ["160711170531","Z2LY0120181584","Z2LY0120181614","160711170590","160711170561","160711170550"]
     * status : 1
     * thirdChannelNum : ["Z2LY0120171584","Z2LY0120171614","160711170560","160711170570"]
     * totalNum : 21
     */

    private String msg;
    private long response_time;
    private String status;
    private int totalNum;
    private List<String> firstChannelNum;
    private List<String> fourthChannelNum;
    private List<String> secondChannelNum;
    private List<String> thirdChannelNum;

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

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<String> getFirstChannelNum() {
        return firstChannelNum;
    }

    public void setFirstChannelNum(List<String> firstChannelNum) {
        this.firstChannelNum = firstChannelNum;
    }

    public List<String> getFourthChannelNum() {
        return fourthChannelNum;
    }

    public void setFourthChannelNum(List<String> fourthChannelNum) {
        this.fourthChannelNum = fourthChannelNum;
    }

    public List<String> getSecondChannelNum() {
        return secondChannelNum;
    }

    public void setSecondChannelNum(List<String> secondChannelNum) {
        this.secondChannelNum = secondChannelNum;
    }

    public List<String> getThirdChannelNum() {
        return thirdChannelNum;
    }

    public void setThirdChannelNum(List<String> thirdChannelNum) {
        this.thirdChannelNum = thirdChannelNum;
    }
}
