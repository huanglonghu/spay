package com.example.godcode.bean;

/**
 * Created by Administrator on 2018/7/5.
 */

public class DivideIncome {


    /**
     * result : {"toDayMoney":0,"yesterDayMoney":0,"toDayCoin":0,"sumCoin":0,"toDayBanknote":0,"sumBanknote":0,"monthMoney":0}
     * targetUrl : null
     * success : true
     * error : null
     * unAuthorizedRequest : false
     * __abp : true
     */

    private ResultBean result;
    private Object targetUrl;
    private boolean success;
    private Object error;
    private boolean unAuthorizedRequest;
    private boolean __abp;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

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

    public static class ResultBean {
        /**
         * toDayMoney : 0.0
         * yesterDayMoney : 0.0
         * toDayCoin : 0.0
         * sumCoin : 0.0
         * toDayBanknote : 0.0
         * sumBanknote : 0.0
         * monthMoney : 0.0
         */

        private double toDayMoney;
        private double yesterDayMoney;
        private int toDayCoin;
        private int sumCoin;
        private int toDayBanknote;
        private int sumBanknote;
        private double monthMoney;

        public double getToDayMoney() {
            return toDayMoney;
        }

        public void setToDayMoney(double toDayMoney) {
            this.toDayMoney = toDayMoney;
        }

        public double getYesterDayMoney() {
            return yesterDayMoney;
        }

        public void setYesterDayMoney(double yesterDayMoney) {
            this.yesterDayMoney = yesterDayMoney;
        }

        public int getToDayCoin() {
            return toDayCoin;
        }

        public void setToDayCoin(int toDayCoin) {
            this.toDayCoin = toDayCoin;
        }

        public int getSumCoin() {
            return sumCoin;
        }

        public void setSumCoin(int sumCoin) {
            this.sumCoin = sumCoin;
        }

        public int getToDayBanknote() {
            return toDayBanknote;
        }

        public void setToDayBanknote(int toDayBanknote) {
            this.toDayBanknote = toDayBanknote;
        }

        public int getSumBanknote() {
            return sumBanknote;
        }

        public void setSumBanknote(int sumBanknote) {
            this.sumBanknote = sumBanknote;
        }

        public double getMonthMoney() {
            return monthMoney;
        }

        public void setMonthMoney(double monthMoney) {
            this.monthMoney = monthMoney;
        }
    }
}
