package com.quanmai.yiqu.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chasing-Li on 2017/5/24.
 */

public class SearchUserDataInfo implements Serializable{

    /**
     * arList : [{"buildno":"1单元","roomList":[{"mtel":"18612751734","name":"fujd","room":"1室","unit":"1单元"}]}]
     * msg : 成功
     * response_time : 1495617584064
     * status : 1
     */

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
         * roomList : [{"mtel":"18612751734","name":"fujd","room":"1室","unit":"1单元"}]
         */

        private String buildno;
        private List<RoomListBean> roomList;

        public String getBuildno() {
            return buildno;
        }

        public void setBuildno(String buildno) {
            this.buildno = buildno;
        }

        public List<RoomListBean> getRoomList() {
            return roomList;
        }

        public void setRoomList(List<RoomListBean> roomList) {
            this.roomList = roomList;
        }

        public static class RoomListBean {
            /**
             * mtel : 18612751734
             * name : fujd
             * room : 1室
             * unit : 1单元
             */

            private String mtel;
            private String name;
            private String room;
            private String unit;

            public String getMtel() {
                return mtel;
            }

            public void setMtel(String mtel) {
                this.mtel = mtel;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRoom() {
                return room;
            }

            public void setRoom(String room) {
                this.room = room;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }
        }
    }
}
