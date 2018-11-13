package com.example.godcode.utils;

import android.view.View;

import com.example.godcode.http.HttpUtil;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.activity.BaseActivity;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.fragment.deatailFragment.SetPayPsdFragment;
import com.example.godcode.ui.view.KeyBoard;
import com.example.godcode.ui.view.PsdPopupWindow;


public class PayPsdSetting {

    private PayPsdSetting() {
    }

    private static PayPsdSetting defaultInstance;

    public static PayPsdSetting getInstance() {

        PayPsdSetting payPsdSetting = defaultInstance;

        if (defaultInstance == null) {
            synchronized (PayPsdSetting.class) {
                if (defaultInstance == null) {
                    payPsdSetting = new PayPsdSetting();
                    defaultInstance = payPsdSetting;
                }
            }
        }
        return payPsdSetting;
    }

    public void initContext(BaseActivity activity) {
        this.activity = activity;
    }


    public void isPayPsdSet(String title, double money, View view, KeyBoard.PsdLengthWatcher watcher,int type) {
        if (PayPsdSetting.getInstance().isCheckPsd()) {
            if (Constant.isPayPsdSet) {
                inputPsd(title, money, view, watcher);
            } else {
                step2SetPsd(type);
            }
        } else {
            HttpUtil.getInstance().hasPsd(Constant.userId).subscribe(
                    isPayPsdSetStr -> {
                        isCheckPsd = true;
                        if (isPayPsdSetStr.equals("no")) {
                            step2SetPsd(type);
                        } else {
                            Constant.isPayPsdSet = true;
                            inputPsd(title, money, view, watcher);
                        }
                    }
            );
        }
    }

    private void step2SetPsd(int type) {
        LogUtil.log("------PsdSetting---------");
        SetPayPsdFragment setPayPsdFragment = new SetPayPsdFragment();
        setPayPsdFragment.initData(type);
        Presenter.getInstance().step2Fragment(setPayPsdFragment, "setPayPsd");
    }

    private BaseActivity activity;

    public void inputPsd(String title, double money, View view, KeyBoard.PsdLengthWatcher watcher) {
        KeyBoard keyBoard = new KeyBoard(activity);
        keyBoard.setPsdLengthWatcher(watcher);
        PsdPopupWindow.getInstance(activity).show(title, money, view, keyBoard);
    }


    private boolean isCheckPsd;

    public boolean isCheckPsd() {
        return isCheckPsd;
    }


}
