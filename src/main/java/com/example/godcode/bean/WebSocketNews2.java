package com.example.godcode.bean;

/**
 * Created by Administrator on 2018/7/31.
 */

public class WebSocketNews2 {

    /**
     * EventType : 5
     * Data : {"msg":"您的好友向您转账！","RelatedKey":10046,"Id":10267,"TransactionType":8}
     */

   // {"EventType":6,"Data":{"msg":"您有新的二维码收款入账！","RelatedKey":10055,"Id":10291,"TransationType":2}}

    private int EventType;
    private DataBean Data;

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

    public static class DataBean {
        /**
         * msg : 您的好友向您转账！
         * RelatedKey : 10046
         * Id : 10267
         * TransactionType : 8
         */

        private String msg;
        private int RelatedKey;
        private int Id;
        private int TransactionType;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getRelatedKey() {
            return RelatedKey;
        }

        public void setRelatedKey(int RelatedKey) {
            this.RelatedKey = RelatedKey;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getTransactionType() {
            return TransactionType;
        }

        public void setTransactionType(int TransactionType) {
            this.TransactionType = TransactionType;
        }
    }
}
