package com.quanmai.yiqu.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by James on 2016/7/7.
 * 楼栋实体类
 */
public class ChooseRoomInfo implements Serializable{
    /**
     * arList : [{"buildno":"1单元","roomList":["301室","401室","501室"]},{"buildno":"2单元","roomList":["301室","401室","501室","601室"]},{"buildno":"3单元","roomList":["101室","301室","401室"]},{"buildno":"4单元","roomList":["401室","402室"]},{"buildno":"5单元","roomList":["201室","301室","401室","501室"]},{"buildno":"6单元","roomList":["201室","301室","401室","501室","601室","602室"]},{"buildno":"7单元","roomList":["301室","401室","501室","601室"]},{"buildno":"8单元","roomList":["201室","301室","401室","501室","601室"]},{"buildno":"9单元","roomList":["401室","402室","403室","404室","405室","501室","502室","503室","504室","601室","602室","603室"]}]
     * msg : 成功
     * response_time : 1495524007306
     * status : 1
     */
    public String commcode; //（上级）小区编号
    public String commname; //（上级）小区名
    public String buildName; //栋号

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

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    private String msg;
    private long response_time;
    private String status;
    private List<ArListBean> arList;

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

    public List<ArListBean> getArList() {
        return arList;
    }

    public void setArList(List<ArListBean> arList) {
        this.arList = arList;
    }

    public static class ArListBean {
        /**
         * buildno : 1单元
         * roomList : ["301室","401室","501室"]
         */

        private String buildno;
        private List<String> roomList;

        public String getBuildno() {
            return buildno;
        }

        public void setBuildno(String buildno) {
            this.buildno = buildno;
        }

        public List<String> getRoomList() {
            return roomList;
        }

        public void setRoomList(List<String> roomList) {
            this.roomList = roomList;
        }
    }

    /*public String commcode; //（上级）小区编号
    public String commname; //（上级）小区名
    public String buildName; //栋号
    public CommonList<UnitInfo> arList; //单元列表

    public BuildInfo(){
        arList = new CommonList<>();
    }*/
}
