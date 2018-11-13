package com.example.godcode.bean;

/**
 * Created by Administrator on 2018/9/3.
 */

public class WebSocketNews3 {


    /**
     * EventType : 1
     * Data : {"msg":"用户'sy1535799652'请求添加您为好友！"}
     */

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
         * msg : 用户'sy1535799652'请求添加您为好友！
         */

        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
