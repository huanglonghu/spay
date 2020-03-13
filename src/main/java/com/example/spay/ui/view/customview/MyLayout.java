package com.example.spay.ui.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.example.spay.utils.LogUtil;

public class MyLayout  extends LinearLayout {
    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout(Context context,  AttributeSet attrs, int defStyleAttr) {
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
        LogUtil.log("1==========onTouchEvent==========="+str);

        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        String str="";
        switch (ev.getAction()){
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
        LogUtil.log("1==========dispatchTouchEvent==========="+str);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        String str="";
        switch (ev.getAction()){
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
        LogUtil.log("1==========onInterceptTouchEvent==========="+str);
        return super.onInterceptTouchEvent(ev);
    }
}
