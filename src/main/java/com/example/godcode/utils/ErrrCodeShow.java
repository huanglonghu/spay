package com.example.godcode.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ErrrCodeShow {
    public static void showToast(int errorCode, Context context,String body) {
        String content = "";
        switch (errorCode) {
            case 0:
                if(body.contains("Login failed!")){
                    content="登录失败";
                }else {
                    content = "网络访问失败";//多种情况导致0
                }
                break;
            case 2000:
                content = "用户不存在！";
                break;
            case 2001:
                content = "余额不足！";
                break;
            case 2002:
                content = "此手机号用户已存在，请勿重复注册！";
                break;
            case 2003:
                content = "银行卡号格式错误！";
                break;
            case 2004:
                content = "手机号格式错误！";
                break;
            case 2005:
                content = "货币类型错误！";
                break;
            case 2006:
                content = "提现状态错误！";
                break;
            case 2007:
                content = "用户未设置支付密码或者未绑定银行卡！";
                break;
            case 2008:
                content = "密码输入错误！";
                break;
            case 2009:
                content = "支付订单不存在！";
                break;
            case 2010:
                content = "支付订单不属于此用户！";
                break;
            case 2011:
                content = "当前用户余额不足！";
                break;
            case 2012:
                content = "当前订单已超过退款时间！";
                break;
            case 2013:
                content = "此用户名已存在！";
                break;
            case 2014:
                content = "密码长度最少为6位！";
                break;
            case 2015:
                content = "金额必须大于0";
                break;
            case 2016:
                content="传入参数错误！";
                break;
            case 2018:
                content="退款失败！";
                break;
            case 2019:
                content="暂无可提取金额！";
                break;
            case 2020:
                content = "金额错误，绑定失败！";
                break;
            case 2021:
                content="银行卡已绑定";
                break;
            case 2022:
                content="验证码已过期或错误！";
                break;
            case 2023:
                content="交易记录不存在！";
                break;
            case 3000:
                content = "此产品类别不存在！";
                break;
            case 3001:
                content = "产品已存在,无需重复添加！";
                break;
            case 3002:
                content = "产品不存在！";
                break;
            case 3003:
                content = "此产品非当前用户所有！";
                break;
            case 3004:
                content = "营收分成不合法！";
                break;
            case 3005:
                content = "产品价格已存在，无需重复添加！";
                break;
            case 3006:
                content = "当前产品未绑定商户！";
                break;
            case 3007:
                content = "已经退款成功请勿重复退款！";
                break;
            case 3009:
                content = "此设备已被绑定";
                break;
            case 3010:
                content = "分成用户已存在！";
                break;
            case 3011:
                content="此类别下已存在产品，不能删除！";
                break;
            case 3012:
                content="请先删除子级类别！";
                break;
            case 3014:
                content="设备不在线，请稍后重试！";
                break;
            case 3015:
                content="设备应答超时，请稍后重试！";
                break;
            case 4000:
                content = "好友不存在";
                break;
            case 4001:
                content = "好友已存在";
                break;
            case 4003:
                content = "不能添加自己为好友！";
                break;
            case 4004:
                content = "用户已登录";
                break;
            case 4005:
                content="用户未登录！";
                break;
            case 4006:
                content="异地登录";
                break;
            case 6002:
                content="数据校验错误";
                break;
            case 6006:
                content="不符合申请条件";
                break;
        }
        if (!TextUtils.isEmpty(content)) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }
    }


}
