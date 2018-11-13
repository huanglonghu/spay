package com.example.godcode.bean;

/**
 * Created by Administrator on 2018/10/30.
 */

public class WsHeart {


    /**
     * EventType : 21
     * Data : {"IOSVer":"2.1.4","AndroidVer":"2.1.5","IOSVerDes":"1.增加手机话费充值|2.优化主界面","AndroidVerDes":"1.增加手机话费充值|2.优化主界面"}
     * Flag : 3b83e4d1-55e0-441c-8cff-0010cf581832
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
         * IOSVer : 2.1.4
         * AndroidVer : 2.1.5
         * IOSVerDes : 1.增加手机话费充值|2.优化主界面
         * AndroidVerDes : 1.增加手机话费充值|2.优化主界面
         */

        private String IOSVer;
        private String AndroidVer;
        private String IOSVerDes;
        private String AndroidVerDes;

        public String getIOSVer() {
            return IOSVer;
        }

        public void setIOSVer(String IOSVer) {
            this.IOSVer = IOSVer;
        }

        public String getAndroidVer() {
            return AndroidVer;
        }

        public void setAndroidVer(String AndroidVer) {
            this.AndroidVer = AndroidVer;
        }

        public String getIOSVerDes() {
            return IOSVerDes;
        }

        public void setIOSVerDes(String IOSVerDes) {
            this.IOSVerDes = IOSVerDes;
        }

        public String getAndroidVerDes() {
            return AndroidVerDes;
        }

        public void setAndroidVerDes(String AndroidVerDes) {
            this.AndroidVerDes = AndroidVerDes;
        }
    }
}
