package com.example.godcode.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/5.
 */

public class BankCard {


    /**
     * result : [{"fK_UserID":12,"bankID":0,"bankName":"磁条卡卡号:","bankTime":"2018-07-07T19:17:43.5244006","bankNumber":0,"mobile":"13076737863","bankCardNumber":"621421703107","bindType":4,"bindMoney":0,"userName":"123456","id":5},{"fK_UserID":12,"bankID":0,"bankName":"农业银行-金穗通宝卡(银联卡)-借记卡:","bankTime":"2018-07-09T14:38:37.1804012","bankNumber":0,"mobile":"13076737863","bankCardNumber":"6228480402637874213","bindType":2,"bindMoney":0.1,"userName":"123456","id":10005}]
     * targetUrl : null
     * success : true
     * error : null
     * unAuthorizedRequest : false
     * __abp : true
     */

    private Object targetUrl;
    private boolean success;
    private Object error;
    private boolean unAuthorizedRequest;
    private boolean __abp;
    private List<ResultBean> result;

    public Object getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(Object targetUrl) {
        this.targetUrl = targetUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public boolean isUnAuthorizedRequest() {
        return unAuthorizedRequest;
    }

    public void setUnAuthorizedRequest(boolean unAuthorizedRequest) {
        this.unAuthorizedRequest = unAuthorizedRequest;
    }

    public boolean is__abp() {
        return __abp;
    }

    public void set__abp(boolean __abp) {
        this.__abp = __abp;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * fK_UserID : 12
         * bankID : 0
         * bankName : 磁条卡卡号:
         * bankTime : 2018-07-07T19:17:43.5244006
         * bankNumber : 0
         * mobile : 13076737863
         * bankCardNumber : 621421703107
         * bindType : 4
         * bindMoney : 0.0
         * userName : 123456
         * id : 5
         */

        private int fK_UserID;
        private int bankID;
        private String bankName;
        private String bankTime;
        private int bankNumber;
        private String mobile;
        private String bankCardNumber;
        private int bindType;
        private double bindMoney;
        private String userName;
        private int id;

        public int getFK_UserID() {
            return fK_UserID;
        }

        public void setFK_UserID(int fK_UserID) {
            this.fK_UserID = fK_UserID;
        }

        public int getBankID() {
            return bankID;
        }

        public void setBankID(int bankID) {
            this.bankID = bankID;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankTime() {
            return bankTime;
        }

        public void setBankTime(String bankTime) {
            this.bankTime = bankTime;
        }

        public int getBankNumber() {
            return bankNumber;
        }

        public void setBankNumber(int bankNumber) {
            this.bankNumber = bankNumber;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getBankCardNumber() {
            return bankCardNumber;
        }

        public void setBankCardNumber(String bankCardNumber) {
            this.bankCardNumber = bankCardNumber;
        }

        public int getBindType() {
            return bindType;
        }

        public void setBindType(int bindType) {
            this.bindType = bindType;
        }

        public double getBindMoney() {
            return bindMoney;
        }

        public void setBindMoney(double bindMoney) {
            this.bindMoney = bindMoney;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
