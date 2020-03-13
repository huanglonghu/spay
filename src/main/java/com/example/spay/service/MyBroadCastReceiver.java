package com.example.spay.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.spay.bean.LoginResponse;
import com.example.spay.http.HttpUtil;
import com.example.spay.constant.Constant;
import com.example.spay.utils.GsonUtil;
import com.example.spay.utils.LogUtil;
import com.example.spay.utils.SharepreferenceUtil;

/**
 * Created by Administrator on 2018/9/7.
 */

public class MyBroadCastReceiver extends BroadcastReceiver {
    private static int ikk = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case "ELITOR_CLOCK":
                LogUtil.log("-------时间到了-------");
                HttpUtil.getInstance().refreshAccessToken().subscribe(
                        loginStr -> {
                            LoginResponse loginResponse = GsonUtil.getInstance().fromJson(loginStr, LoginResponse.class);
                            LoginResponse.ResultBean result = loginResponse.getResult();
                            String accessToken = result.getAccessToken();
                            long l = System.currentTimeMillis();
                            Constant.expireInSeconds = result.getExpireInSeconds();
                            SharepreferenceUtil.getInstance().saveAccessToken(accessToken, Constant.uniquenessToken, l);
                        }
                );
                break;
            default:
                break;
        }
    }
}
