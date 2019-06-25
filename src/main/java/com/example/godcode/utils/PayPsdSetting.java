package com.example.godcode.utils;

import com.example.godcode.greendao.entity.User;
import com.example.godcode.greendao.option.UserOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.ClickSureListener;
import com.example.godcode.ui.activity.BaseActivity;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.view.TransferAccuntView;

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

    private BaseActivity activity;

    public void chackPwd(double money, ClickSureListener clickSureListener) {
        TransferAccuntView transferAccuntView = new TransferAccuntView(activity, money, new ClickSureListener() {
            @Override
            public void clickSure() {
                User user = UserOption.getInstance().querryUser(Constant.userId);
                boolean setPwd = user.getSetPwd();
                if (setPwd) {
                    clickSureListener.isPwdExit(true);
                } else {
                    HttpUtil.getInstance().hasPsd(Constant.userId).subscribe(
                            isPayPsdSetStr -> {
                                if (isPayPsdSetStr.equals("no")) {
                                    clickSureListener.isPwdExit(false);
                                } else {
                                    clickSureListener.isPwdExit(true);
                                }
                            }
                    );

                }
            }
        });
        transferAccuntView.show();
    }

}
