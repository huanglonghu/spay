package com.example.spay.utils;

import android.os.Bundle;

import com.example.spay.greendao.entity.User;
import com.example.spay.greendao.option.UserOption;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.ClickSureListener;
import com.example.spay.presenter.Presenter;
import com.example.spay.ui.activity.BaseActivity;
import com.example.spay.constant.Constant;
import com.example.spay.ui.fragment.pwd.SetPayPsdFragment;
import com.example.spay.ui.view.TransferAccuntView;

public class PayPwdSetting {

    private PayPwdSetting() {
    }

    private static PayPwdSetting defaultInstance;

    public static PayPwdSetting getInstance() {

        PayPwdSetting payPsdSetting = defaultInstance;

        if (defaultInstance == null) {
            synchronized (PayPwdSetting.class) {
                if (defaultInstance == null) {
                    payPsdSetting = new PayPwdSetting();
                    defaultInstance = payPsdSetting;
                }
            }
        }
        return payPsdSetting;
    }

    public void initContext(BaseActivity activity) {
        this.activity = activity;
    }

    private BaseActivity activity;

    public void checkPwd(double money, ClickSureListener clickSureListener) {
        TransferAccuntView transferAccuntView = new TransferAccuntView(activity, money, new ClickSureListener() {
            @Override
            public void clickSure() {
                verifyPwd(clickSureListener);
            }
        });
        transferAccuntView.show();
    }

    public void verifyPwd(ClickSureListener clickSureListener) {//验证密码是否存在
        User user = UserOption.getInstance().querryUser(Constant.userId);
        boolean setPwd = user.getSetPwd();
        LogUtil.log("========setPwd========"+setPwd);
        if (setPwd) {
            clickSureListener.isPwdExit(true);
        } else {
            HttpUtil.getInstance().hasPsd(Constant.userId).subscribe(
                    isPayPsdSetStr -> {
                        if (isPayPsdSetStr.equals("no")) {
                            SetPayPsdFragment setPayPsdFragment = new SetPayPsdFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("title","请设置支付密码");
                            setPayPsdFragment.setArguments(bundle);
                            Presenter.getInstance().step2Fragment(setPayPsdFragment, "setPwd");
                        } else {
                            user.setSetPwd(true);
                            UserOption.getInstance().updateUser(user);
                            clickSureListener.isPwdExit(true);
                        }
                    }
            );

        }
    }
}
