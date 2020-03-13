package com.example.spay.utils;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.Random;

public class WxPay {

    char[] datas={
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9'};
    public  String getRandom(){//生成32位随机数
        StringBuffer randomStr = new StringBuffer();
        Random random=new Random();
        for (int i=0;i<16;i++){
            int index = random.nextInt(62);
            randomStr.append(datas[index]);
        }
        return randomStr.toString();
    }

    IWXAPI api;
    public void pay(){
        String random = getRandom();
        long time = System.currentTimeMillis() / 1000;
        PayReq request = new PayReq();
        request.appId = "wxf145d2ce67346702";
        request.partnerId = "1453774202";
        request.prepayId= "1101000000140415649af9fc314aa427";
        request.packageValue = "Sign=WXPay";
        request.nonceStr= random;
        request.timeStamp= String.valueOf(time);
        request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
        String signStr="appId="+request.appId+"&partnerId="+request.partnerId+"&prepayId="+request.prepayId+"&packageValue="+request.packageValue+"&nonceStr="+request.nonceStr
                +"&timeStamp="+request.timeStamp;
        String SignTemp=signStr+"&key=e10adc3849ba56abbe56e056f20f883z";

        api.sendReq(request);
    }
}
