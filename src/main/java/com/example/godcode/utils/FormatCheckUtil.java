package com.example.godcode.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/8/9.
 */

public class FormatCheckUtil {

    private DecimalFormat decimalFormat;

    private FormatCheckUtil() {
    }

    private static FormatCheckUtil defaultInstance;

    public static FormatCheckUtil getInstance() {
        FormatCheckUtil dateUtil = defaultInstance;
        if (defaultInstance == null) {
            synchronized (DateUtil.class) {
                if (defaultInstance == null) {
                    dateUtil = new FormatCheckUtil();
                }
            }
        }
        return dateUtil;
    }

    public boolean checkPhoneNumber(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return false;
        }
//        String first = phoneNum.substring(0, 1);
//        if (!first.equals("1")) {
//            return false;
//        }
//        if (phoneNum.length() != 11) {
//            return false;
//        }
        return true;
    }

    public boolean checkYzm(String yzm) {


        if (TextUtils.isEmpty(yzm)) {
            return false;
        }
        if (yzm.length() != 6) {
            return false;
        }
        return true;
    }


    public String getLast4Num(String bankNumber) {
        String last4Num = bankNumber.substring(bankNumber.length() - 4);
        return last4Num;
    }

    public String get2double(double num) {
        if (decimalFormat == null) {
            decimalFormat = new DecimalFormat("0.00");
        }
        String format = decimalFormat.format(num);

        return format;
    }


}
