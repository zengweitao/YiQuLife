package com.quanmai.yiqu.api.vo;

/**
 * Created by Chasing-Li on 2017/4/23.
 */
public class ClassifyScoreRankingDetailInfo {
    private String account;
    private String scoreTotal;
    private String scoreTimes;
    private float scoreAvg;
    private String isCurrent;
    private String rank;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public float getScoreAvg() {
        return scoreAvg;
    }

    public void setScoreAvg(float scoreAvg) {
        this.scoreAvg = scoreAvg;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getScoreTotal() {
        return scoreTotal;
    }

    public void setScoreTotal(String scoreTotal) {
        this.scoreTotal = scoreTotal;
    }

    public String getScoreTimes() {
        return scoreTimes;
    }

    public void setScoreTimes(String scoreTimes) {
        this.scoreTimes = scoreTimes;
    }

    public String getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(String isCurrent) {
        this.isCurrent = isCurrent;
    }
}
