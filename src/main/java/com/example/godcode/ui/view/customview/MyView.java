package com.example.godcode.ui.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.godcode.utils.LogUtil;

public class MyView extends TextView {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String str="";
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                str="down";
                break;
            case MotionEvent.ACTION_MOVE:
                str="move";
                break;
            case MotionEvent.ACTION_UP:
                str="up";
                break;
        }
        boolean b = super.onTouchEvent(event);
        LogUtil.log("2==========onTouchEvent==========="+str+"==========="+b);
        return b;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        String str="";
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                str="down";
                break;
            case MotionEvent.ACTION_MOVE:
                str="move";
                break;
            case MotionEvent.ACTION_UP:
                str="up";
                break;
        }

        boolean b = super.dispatchTouchEvent(event);
        LogUtil.log("2==========dispatchEvent==========="+str+"==============="+b);

        return b;
    }
}
