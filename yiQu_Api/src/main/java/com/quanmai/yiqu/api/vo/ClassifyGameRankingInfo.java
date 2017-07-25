package com.quanmai.yiqu.api.vo;

import com.nostra13.universalimageloader.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Chasing-Li on 2017/5/14.
 */

public class ClassifyGameRankingInfo {
    String contestName; //竞赛名称
    String commcode; //小区编号
    String commname; //小区名
    String img_address; //头像请求地址
    String msg; //返回信息
    Long response_time; //请求时间
    String status; //请求状态值
    List<ContestRankListInfo> contestRankList; //排名信息
    int count; //游戏竞赛次数
    int current; //游戏竞赛第几次


    public String getImg_address() {
        return img_address;
    }

    public void setImg_address(String img_address) {
        this.img_address = img_address;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public String getCommcode() {
        return commcode;
    }

    public void setCommcode(String commcode) {
        this.commcode = commcode;
    }

    public String getCommname() {
        return commname;
    }

    public void setCommname(String commname) {
        this.commname = commname;
    }

    public List<ContestRankListInfo> getContestRankList() {
        return contestRankList;
    }

    public void setContestRankList(List<ContestRankListInfo> contestRankList) {
        this.contestRankList = contestRankList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getResponse_time() {
        return response_time;
    }

    public void setResponse_time(Long response_time) {
        this.response_time = response_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public static class ContestRankListInfo{
        public ContestRankListInfo(JSONObject jsonObject) throws JSONException {
            rank = jsonObject.getString("rank");
            account = jsonObject.getString("account");
            maxScore = jsonObject.getString("maxScore");
            isCurrent = jsonObject.optString("isCurrent");
            imgUrl = jsonObject.optString("imgUrl");
        }
        String imgUrl; //头像图片名字
        String rank; //排名
        String account; //用户名
        String maxScore; //得分
        String isCurrent; //是否为当前用户  1：为当前用户

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getMaxScore() {
            return maxScore;
        }

        public void setMaxScore(String maxScore) {
            this.maxScore = maxScore;
        }

        public String getIsCurrent() {
            return isCurrent;
        }

        public void setIsCurrent(String isCurrent) {
            this.isCurrent = isCurrent;
        }
    }
}
