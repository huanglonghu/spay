package com.example.godcode.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.godcode.ui.base.GodCodeApplication;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/7/30.
 */

public class SharepreferenceUtil {

    private void SharepreferenceUtil() {
    }

    private static SharepreferenceUtil sharepreferenceUtil;

    public static SharepreferenceUtil getInstance() {
        SharepreferenceUtil spUtil = sharepreferenceUtil;
        if (sharepreferenceUtil == null) {
            synchronized (SharepreferenceUtil.class) {
                if (sharepreferenceUtil == null) {
                    spUtil = new SharepreferenceUtil();
                }
            }
        }
        return spUtil;
    }


    public void saveAccessToken(String accseeToken, String loginToken, long time) {
        SharedPreferences sp = GodCodeApplication.getInstance().getSharedPreferences("sy", Context.MODE_PRIVATE);
        sp.edit().putString("accessToken", accseeToken).putString("loginToken", loginToken).putLong("tokenTime", time).commit();
    }

    public String getAccessToken() {
        SharedPreferences sp = GodCodeApplication.getInstance().getSharedPreferences("sy", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");
        return accessToken;
    }

    public String getLoginToken() {
        SharedPreferences sp = GodCodeApplication.getInstance().getSharedPreferences("sy", Context.MODE_PRIVATE);
        String loginToken = sp.getString("loginToken", "");
        return loginToken;
    }

    public long getTokenTime() {
        SharedPreferences sp = GodCodeApplication.getInstance().getSharedPreferences("sy", Context.MODE_PRIVATE);
        long tokenTime = sp.getLong("tokenTime", 0);
        return tokenTime;
    }


}
