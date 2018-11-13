package com.example.godcode.utils;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/6.
 */

public class QuerryBankCard {

    private static String openBinNum(Context context) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String str = null;
        try {
            InputStream is = context.getResources().getAssets().open("binNum.txt");
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = is.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }
            is.close();
            outputStream.close();
            str = outputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    private static List<Long> getBinNum(Context context) {
        String binNum = openBinNum(context);
        String[] binArr = binNum.split(",");
        List<Long> lon = new ArrayList<>();
        for (int i = 0; i < binArr.length; i++) {
            if (i % 2 == 0)
                lon.add(Long.parseLong(binArr[i]));

        }
        return lon;
    }

    private static List<String> getBinName(Context context) {

        String binNum = openBinNum(context);
        String[] binArr = binNum.split(",");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < binArr.length; i++) {
            if (i % 2 != 0) list.add(binArr[i]);
        }
        return list;
    }


    public static int binarySearch(List<Long> srcArray, long des) {
        int low = 0;
        int high = srcArray.size() - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (des == srcArray.get(middle)) {
                return middle;
            } else if (des < srcArray.get(middle)) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return -1;
    }


    public static String getNameOfBank(Context context, long binNum) {
        Log.e("sangfei.code", "bankBin: " + binNum);
        int index = 0;
        index = binarySearch(getBinNum(context), binNum);
        if (index == -1) {
            return "磁条卡卡号:\n";
        }
        return getBinName(context).get(index) + ":\n";
    }

}
