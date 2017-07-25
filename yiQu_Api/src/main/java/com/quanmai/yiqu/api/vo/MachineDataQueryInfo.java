package com.quanmai.yiqu.api.vo;

/**
 * Created by Chasing-Li on 2017/5/23.
 */

public class MachineDataQueryInfo {

    /**
     * firstChannelNum : 8
     * fourthChannelNum : 3
     * msg : 成功
     * response_time : 1495506959283
     * secondChannelNum : 6
     * status : 1
     * thirdChannelNum : 4
     * totalNum : 21
     */

    private int firstChannelNum;
    private int fourthChannelNum;
    private String msg;
    private long response_time;
    private int secondChannelNum;
    private String status;
    private int thirdChannelNum;
    private int totalNum;

    public int getFirstChannelNum() {
        return firstChannelNum;
    }

    public void setFirstChannelNum(int firstChannelNum) {
        this.firstChannelNum = firstChannelNum;
    }

    public int getFourthChannelNum() {
        return fourthChannelNum;
    }

    public void setFourthChannelNum(int fourthChannelNum) {
        this.fourthChannelNum = fourthChannelNum;
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

    public int getSecondChannelNum() {
        return secondChannelNum;
    }

    public void setSecondChannelNum(int secondChannelNum) {
        this.secondChannelNum = secondChannelNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getThirdChannelNum() {
        return thirdChannelNum;
    }

    public void setThirdChannelNum(int thirdChannelNum) {
        this.thirdChannelNum = thirdChannelNum;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}
