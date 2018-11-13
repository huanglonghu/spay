package com.example.godcode.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/31.
 */

public class WebSocketNews1 implements Serializable{

    /**
     * EventType : 19
     * Data : {"ProductNumber":"SY155","CoinCount":0,"PaperMoney":0,"ScanQRMoney":1}
     * Flag : 78c0d03d-83f8-4129-bdd3-26224e30b1ec
     */

    private int EventType;
    private DataBean Data;
    private String Flag;

    public int getEventType() {
        return EventType;
    }

    public void setEventType(int EventType) {
        this.EventType = EventType;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String Flag) {
        this.Flag = Flag;
    }

    public static class DataBean {
        /**
         * ProductNumber : SY155
         * CoinCount : 0
         * PaperMoney : 0.0
         * ScanQRMoney : 1.0
         */

        private String ProductNumber;
        private int CoinCount;
        private double PaperMoney;
        private double ScanQRMoney;

        public String getProductNumber() {
            return ProductNumber;
        }

        public void setProductNumber(String ProductNumber) {
            this.ProductNumber = ProductNumber;
        }

        public int getCoinCount() {
            return CoinCount;
        }

        public void setCoinCount(int CoinCount) {
            this.CoinCount = CoinCount;
        }

        public double getPaperMoney() {
            return PaperMoney;
        }

        public void setPaperMoney(double PaperMoney) {
            this.PaperMoney = PaperMoney;
        }

        public double getScanQRMoney() {
            return ScanQRMoney;
        }

        public void setScanQRMoney(double ScanQRMoney) {
            this.ScanQRMoney = ScanQRMoney;
        }
    }
}
