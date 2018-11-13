package com.example.godcode.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class RemberPsd {

    private  SharedPreferences jdpsd;

    private RemberPsd() {

    }

    private static RemberPsd defaultInstance;

    public static RemberPsd getInstance() {
        RemberPsd remberPsd = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RemberPsd.class) {
                remberPsd = new RemberPsd();
                defaultInstance = remberPsd;
            }
        }
        return remberPsd;
    }

    public void init(Context context){
        jdpsd = context.getSharedPreferences("jdpsd", 0);
    }


    public void savePsd(String user, String psd) {
        jdpsd.edit().putString("user", user).commit();
        jdpsd.edit().putString("psd", psd).commit();
    }

    public String getUser(){
        return jdpsd.getString("user","");
    }

    public String getPsd(){
        return jdpsd.getString("psd","");
    }

}
