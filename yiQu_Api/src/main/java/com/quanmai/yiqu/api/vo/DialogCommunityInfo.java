package com.quanmai.yiqu.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/7/14.
 */

public class DialogCommunityInfo implements Serializable {

    /**
     * commList : [{"commcode":"33010800102001","commname":"连园小区"},{"commcode":"33010800201001","commname":"益趣科技"},{"commcode":"33010800103001","commname":"铂金时代公寓"}]
     * msg : 查询成功
     * response_time : 1499336878746
     * status : 1
     */

    private String msg;
    private long response_time;
    private String status;
    private List<CommListBean> commList;

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

    public List<CommListBean> getCommList() {
        return commList;
    }

    public void setCommList(List<CommListBean> commList) {
        this.commList = commList;
    }

    public static class CommListBean {
        /**
         * commcode : 33010800102001
         * commname : 连园小区
         */

        private String commcode;
        private String commname;

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
    }
}
