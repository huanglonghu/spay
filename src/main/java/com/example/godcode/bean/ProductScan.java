package com.example.godcode.bean;

/**
 * Created by Administrator on 2018/7/17.
 */

public class ProductScan {

    /**
     * result : {"productNumber":"784","productName":"密谋我搂你","isValid":true,"thumbnailImgPath":"/Files/Pictures/ProductType/a8b16a12-94dd-4fa2-89c2-5306781ba177.jpg","money":13,"isBind":false,"id":19}
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
         * productNumber : 784
         * productName : 密谋我搂你
         * isValid : true
         * thumbnailImgPath : /Files/Pictures/ProductType/a8b16a12-94dd-4fa2-89c2-5306781ba177.jpg
         * money : 13.0
         * isBind : false
         * id : 19
         */

        private String productNumber;
        private String productName;
        private boolean isValid;
        private String thumbnailImgPath;
        private double money;
        private boolean isBind;
        private int id;

        public String getProductNumber() {
            return productNumber;
        }

        public void setProductNumber(String productNumber) {
            this.productNumber = productNumber;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public boolean isIsValid() {
            return isValid;
        }

        public void setIsValid(boolean isValid) {
            this.isValid = isValid;
        }

        public String getThumbnailImgPath() {
            return thumbnailImgPath;
        }

        public void setThumbnailImgPath(String thumbnailImgPath) {
            this.thumbnailImgPath = thumbnailImgPath;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public boolean isIsBind() {
            return isBind;
        }

        public void setIsBind(boolean isBind) {
            this.isBind = isBind;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
