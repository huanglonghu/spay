package com.example.godcode;

import com.example.godcode.utils.DateUtil;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        //2019-05-29T14:08:38.9006277+08:00
        String s="2019-05-29T14:08:38.9006277+08:00";
        String[] split = s.split("\\.");
        System.out.println(split.length);
        long stringToDate = DateUtil.getInstance().getStringToDate(split[0], "yyyy-MM-dd'T'HH:mm:ss");
        String s1 = DateUtil.getInstance().formatTime(stringToDate);
        System.out.println(s1);

    }





}
