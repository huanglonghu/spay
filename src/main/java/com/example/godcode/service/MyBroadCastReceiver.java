package com.example.godcode.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.example.godcode.bean.LoginResponse;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.LogUtil;
import com.example.godcode.utils.SharepreferenceUtil;
import com.umeng.commonsdk.internal.a;

import static com.umeng.commonsdk.internal.a.l;

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
                            Intent intent1 = new Intent(context, TimeService.class);
                            context.startService(intent1);
                        }
                );
                break;
            default:
                break;
        }
    }
}