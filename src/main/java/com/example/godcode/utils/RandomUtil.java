package com.example.godcode.utils;

import java.util.Random;

/**
 * Created by Administrator on 2018/7/31.
 */

public class RandomUtil {

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

    private RandomUtil(){}

    private static RandomUtil defaultInstance;

    public static RandomUtil getInstance(){
        RandomUtil randomUtil=defaultInstance;
        if(defaultInstance == null ){
            synchronized (RandomUtil.class){
                if(defaultInstance == null){
                    randomUtil=new RandomUtil();
                    defaultInstance=randomUtil;
                }
            }
        }
        return randomUtil;
    }


}
