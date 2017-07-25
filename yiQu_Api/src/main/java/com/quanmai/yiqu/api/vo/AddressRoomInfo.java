package com.quanmai.yiqu.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/7/11.
 */

public class AddressRoomInfo implements Serializable{

    /**
     * community : {"buildingList":[{"buildingNo":"1","unitList":[{"roomList":[{"mtel":"17757149982","name":"李长兴","roomNo":"1","usercompareid":"10347"}],"unitNo":"1"}]},{"buildingNo":"11","unitList":[{"roomList":[{"mtel":"18857583972","name":"垃圾袋","roomNo":"33","usercompareid":"10381"}],"unitNo":"22"}]},{"buildingNo":"3","unitList":[{"roomList":[{"mtel":"18826410930","name":"林子盈","roomNo":"310","usercompareid":"2667"}],"unitNo":"1"},{"roomList":[{"mtel":"17855124196","name":"汪焱","roomNo":"3E","usercompareid":"12171"}],"unitNo":"2"},{"roomList":[{"mtel":"15670977381","name":"李长兴","roomNo":"306","usercompareid":"14048"}],"unitNo":"6"}]},{"buildingNo":"5","unitList":[{"roomList":[{"mtel":"13042047504","name":"尹恩","roomNo":"3","usercompareid":"10380"}],"unitNo":"44"}]}],"commcode":"33010800201001","commname":"益趣科技"}
     * msg : 成功
     * response_time : 1499336438404
     * status : 1
     */

    private CommunityBean community;
    private String msg;
    private long response_time;
    private String status;

    public CommunityBean getCommunity() {
        return community;
    }

    public void setCommunity(CommunityBean community) {
        this.community = community;
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

    public static class CommunityBean {
        /**
         * buildingList : [{"buildingNo":"1","unitList":[{"roomList":[{"mtel":"17757149982","name":"李长兴","roomNo":"1","usercompareid":"10347"}],"unitNo":"1"}]},{"buildingNo":"11","unitList":[{"roomList":[{"mtel":"18857583972","name":"垃圾袋","roomNo":"33","usercompareid":"10381"}],"unitNo":"22"}]},{"buildingNo":"3","unitList":[{"roomList":[{"mtel":"18826410930","name":"林子盈","roomNo":"310","usercompareid":"2667"}],"unitNo":"1"},{"roomList":[{"mtel":"17855124196","name":"汪焱","roomNo":"3E","usercompareid":"12171"}],"unitNo":"2"},{"roomList":[{"mtel":"15670977381","name":"李长兴","roomNo":"306","usercompareid":"14048"}],"unitNo":"6"}]},{"buildingNo":"5","unitList":[{"roomList":[{"mtel":"13042047504","name":"尹恩","roomNo":"3","usercompareid":"10380"}],"unitNo":"44"}]}]
         * commcode : 33010800201001
         * commname : 益趣科技
         */

        private String commcode;
        private String commname;
        private List<BuildingListBean> buildingList;

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

        public List<BuildingListBean> getBuildingList() {
            return buildingList;
        }

        public void setBuildingList(List<BuildingListBean> buildingList) {
            this.buildingList = buildingList;
        }

        public static class BuildingListBean {
            /**
             * buildingNo : 1
             * unitList : [{"roomList":[{"mtel":"17757149982","name":"李长兴","roomNo":"1","usercompareid":"10347"}],"unitNo":"1"}]
             */

            private String buildingNo;
            private List<UnitListBean> unitList;

            public String getBuildingNo() {
                return buildingNo;
            }

            public void setBuildingNo(String buildingNo) {
                this.buildingNo = buildingNo;
            }

            public List<UnitListBean> getUnitList() {
                return unitList;
            }

            public void setUnitList(List<UnitListBean> unitList) {
                this.unitList = unitList;
            }

            public static class UnitListBean {
                /**
                 * roomList : [{"mtel":"17757149982","name":"李长兴","roomNo":"1","usercompareid":"10347"}]
                 * unitNo : 1
                 */

                private String unitNo;
                private List<RoomListBean> roomList;

                public String getUnitNo() {
                    return unitNo;
                }

                public void setUnitNo(String unitNo) {
                    this.unitNo = unitNo;
                }

                public List<RoomListBean> getRoomList() {
                    return roomList;
                }

                public void setRoomList(List<RoomListBean> roomList) {
                    this.roomList = roomList;
                }

                public static class RoomListBean {
                    /**
                     * mtel : 17757149982
                     * name : 李长兴
                     * roomNo : 1
                     * usercompareid : 10347
                     */

                    private String mtel;
                    private String name;
                    private String roomNo;
                    private String usercompareid;

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

                    public String getRoomNo() {
                        return roomNo;
                    }

                    public void setRoomNo(String roomNo) {
                        this.roomNo = roomNo;
                    }

                    public String getUsercompareid() {
                        return usercompareid;
                    }

                    public void setUsercompareid(String usercompareid) {
                        this.usercompareid = usercompareid;
                    }
                }
            }
        }
    }
}
