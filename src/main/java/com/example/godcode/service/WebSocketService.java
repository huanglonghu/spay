package com.example.godcode.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.godcode.bean.LoginResponse;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.activity.BaseActivity;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.LogUtil;
import com.example.godcode.utils.SharepreferenceUtil;
import com.example.godcode.utils.TimeUtil;
import com.example.godcode.utils.WebSocketUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class WebSocketService extends Service {


    private WebSocketUtil webSocketUtil;
    private Disposable subscribe;

    @Override
    public void onCreate() {
        LogUtil.log("------服务被创建------");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.log("-------服务被绑定-------");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.log("-------服务被解绑--------");
        if (webSocketUtil != null) {
            webSocketUtil.stopWebSocket();
        }
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.log("--------服务被销毁------");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }


    public class MyBinder extends Binder {
        public void connectWebSocket(BaseActivity activity) {
            webSocketUtil = new WebSocketUtil(activity);
            webSocketUtil.connect(Constant.webSocketUrl + "?userId=" + Constant.userId);

        }
    }


}
