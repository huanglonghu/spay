package com.example.spay.ui.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.android.qzs.voiceannouncementlibrary.VoiceUtils;
import com.example.spay.utils.SharepreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.Locale;


public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        EventBus.getDefault().register(this);
        changeAppLanguage();
    }

    public abstract void init();


    public void changeAppLanguage() {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale  = SharepreferenceUtil.getInstance().getLanuageIsChinese() ? Locale.CHINA : Locale.US;//这是SharedPreferences工具类，用于保存设置，代码很简单，自己实现吧
        res.updateConfiguration(conf, dm);
    }

    @Subscribe
    public void onEvent(String str) {
        switch (str) {
            case "REFRESH_LANGUAGE":
                changeAppLanguage();
                recreate();//刷新界面
                break;
        }
    }


    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
