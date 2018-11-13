package com.example.godcode;

import android.text.TextUtils;

import com.example.godcode.utils.TimeUtil;

import org.json.JSONObject;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("EventType", 20);
            String s = jsonObject.toString();
//            byte[] bytes = s.getBytes();
            System.out.println("-------str-----"+s);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public String getLast4Num(String bankNumber) {
        String last4Num = bankNumber.substring(bankNumber.length() - 4);
        return last4Num;
    }


}
