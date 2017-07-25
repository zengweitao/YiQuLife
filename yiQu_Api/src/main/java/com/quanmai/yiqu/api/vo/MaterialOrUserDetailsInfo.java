package com.quanmai.yiqu.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/7/5.
 */

public class MaterialOrUserDetailsInfo implements Serializable {

    /**
     * inventoryList : [{"bagtype":"厨余垃圾","issuenums":30,"opetime":"2017-06-15 10:13:41","startnum":"94170100001391"},{"bagtype":"厨余垃圾","issuenums":30,"opetime":"2017-06-15 10:13:57","startnum":"95170100001391"},{"bagtype":"厨余垃圾","issuenums":30,"opetime":"2017-06-15 10:14:10","startnum":"96170100001391"},{"bagtype":"厨余垃圾","issuenums":30,"opetime":"2017-06-15 10:14:43","startnum":"97170100001391"},{"bagtype":"厨余垃圾","issuenums":30,"opetime":"2017-06-15 10:16:43","startnum":"98170100001391"},{"bagtype":"厨余垃圾","issuenums":30,"opetime":"2017-06-15 11:28:53","startnum":"97170100001361"},{"bagtype":"厨余垃圾","issuenums":30,"opetime":"2017-06-15 11:53:35","startnum":"97170100001331"},{"bagtype":"其他垃圾","issuenums":30,"opetime":"2017-06-15 13:45:46","startnum":"97170100001301"},{"bagtype":"厨余垃圾","issuenums":30,"opetime":"2017-06-15 13:47:40","startnum":"97170100001271"},{"bagtype":"其他垃圾","issuenums":30,"opetime":"2017-06-15 13:56:31","startnum":"92170100001471"}]
     * msg : 查询成功
     * response_time : 1498447979285
     * status : 1
     * totalCount : 27
     * totalPage : 3
     */

    private String msg;
    private long response_time;
    private String status;
    private int totalCount;
    private int totalPage;
    private List<InventoryListBean> inventoryList;
    private List<UsercompareListBean> usercompareList;

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

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<InventoryListBean> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<InventoryListBean> inventoryList) {
        this.inventoryList = inventoryList;
    }

    public List<UsercompareListBean> getUsercompareList() {
        return usercompareList;
    }

    public void setUsercompareList(List<UsercompareListBean> usercompareList) {
        this.usercompareList = usercompareList;
    }

    public static class InventoryListBean implements Serializable{
        /**
         * bagtype : 厨余垃圾
         * issuenums : 30
         * opetime : 2017-06-15 10:13:41
         * startnum : 94170100001391
         * * endnum : 10000000006
         * recipient : 付进东
         * recipientmtel : 18612751734
         */

        private String bagtype;
        private int issuenums;
        private String opetime;
        private String startnum;

        private String endnum;
        private String recipient;
        private String recipientmtel;


        public String getBagtype() {
            return bagtype;
        }

        public void setBagtype(String bagtype) {
            this.bagtype = bagtype;
        }

        public int getIssuenums() {
            return issuenums;
        }

        public void setIssuenums(int issuenums) {
            this.issuenums = issuenums;
        }

        public String getOpetime() {
            return opetime;
        }

        public void setOpetime(String opetime) {
            this.opetime = opetime;
        }

        public String getStartnum() {
            return startnum;
        }

        public void setStartnum(String startnum) {
            this.startnum = startnum;
        }

        public String getEndnum() {
            return endnum;
        }

        public void setEndnum(String endnum) {
            this.endnum = endnum;
        }

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public String getRecipientmtel() {
            return recipientmtel;
        }

        public void setRecipientmtel(String recipientmtel) {
            this.recipientmtel = recipientmtel;
        }
    }

    public static class UsercompareListBean implements Serializable{
        /**
         * hoursenum : 先锋营1号 1号203室
         * id : 11998
         * mtel : 13950158404
         * name : 柯辉煌
         */

        private String hoursenum;
        private int id;
        private String mtel;
        private String name;

        public String getHoursenum() {
            return hoursenum;
        }

        public void setHoursenum(String hoursenum) {
            this.hoursenum = hoursenum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

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
    }
}
