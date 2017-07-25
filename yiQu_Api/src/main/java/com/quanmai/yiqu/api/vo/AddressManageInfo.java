package com.quanmai.yiqu.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/6/21.
 */

public class AddressManageInfo implements Serializable {

    /**
     * current_page : 0
     * list : [{"address":"连园小区三幢一单元201","customerid":329,"id":7,"name":"王桑","phone":"13790303985","sortfield":1},{"address":"连园小区四幢二单元203","customerid":329,"id":8,"name":"王桑桑","phone":"13790303985","sortfield":0}]
     * max_page : 1
     * msg : 成功
     * response_time : 1497956707954
     * status : 1
     */

    private int current_page;
    private int max_page;
    private String msg;
    private long response_time;
    private String status;
    private List<ListBean> list;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getMax_page() {
        return max_page;
    }

    public void setMax_page(int max_page) {
        this.max_page = max_page;
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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * address : 连园小区三幢一单元201
         * customerid : 329
         * id : 7
         * name : 王桑
         * phone : 13790303985
         * sortfield : 1
         */

        private String address;
        private int customerid;
        private int id;
        private String name;
        private String phone;
        private int sortfield;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getCustomerid() {
            return customerid;
        }

        public void setCustomerid(int customerid) {
            this.customerid = customerid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getSortfield() {
            return sortfield;
        }

        public void setSortfield(int sortfield) {
            this.sortfield = sortfield;
        }
    }
}
