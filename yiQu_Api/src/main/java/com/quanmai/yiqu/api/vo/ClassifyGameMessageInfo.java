package com.quanmai.yiqu.api.vo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chasing-Li on 2017/5/16.
 */

public class ClassifyGameMessageInfo {
    String contestName; //竞赛名字
    String commname; //小区名字
    String commcode; //小区编号
    String createdTime; //活动创建时间
    String description; //游戏介绍
    String startTime; //活动开始时间
    String endTime; //活动结束时间
    String status; //返回状态
    String msg; //返回状态信息
    String response_time; //请求时间
    private String gameStatus; //活动状态
    private String gameStatusDes; //游戏状态信息


    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getCommname() {
        return commname;
    }

    public void setCommname(String commname) {
        this.commname = commname;
    }

    public String getCommcode() {
        return commcode;
    }

    public void setCommcode(String commcode) {
        this.commcode = commcode;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResponse_time() {
        return response_time;
    }

    public void setResponse_time(String response_time) {
        this.response_time = response_time;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameStatusDes() {
        return gameStatusDes;
    }

    public void setGameStatusDes(String gameStatusDes) {
        this.gameStatusDes = gameStatusDes;
    }

}
