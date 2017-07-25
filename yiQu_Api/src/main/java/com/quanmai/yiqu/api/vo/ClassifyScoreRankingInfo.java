package com.quanmai.yiqu.api.vo;


import java.util.List;

/**
 * Created by Chasing-Li on 2017/4/23.
 */
public class ClassifyScoreRankingInfo {

    private String msg;
    private Long response_time;
    private String status;
    private String commcode;
    private String community;
    private String sortDate;
    private int currentRank;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(int currentRank) {
        this.currentRank = currentRank;
    }

    private List<ClassifyScoreRankingDetailInfo> commScoreRankList;

    public String getCommcode() {
        return commcode;
    }

    public void setCommcode(String commcode) {
        this.commcode = commcode;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getSortDate() {
        return sortDate;
    }

    public void setSortDate(String sortDate) {
        this.sortDate = sortDate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ClassifyScoreRankingDetailInfo> getCommScoreRankList() {
        return commScoreRankList;
    }

    public void setCommScoreRankList(List<ClassifyScoreRankingDetailInfo> commScoreRankList) {
        this.commScoreRankList = commScoreRankList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getResponse_time() {
        return response_time;
    }

    public void setResponse_time(Long response_time) {
        this.response_time = response_time;
    }
}
