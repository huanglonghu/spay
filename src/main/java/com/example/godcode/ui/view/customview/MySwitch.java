package com.example.godcode.ui.view.customview;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.AttributeSet;
import android.widget.Switch;

public class MySwitch extends Switch {
    public MySwitch(Context context) {
        super(context);
    }

    public MySwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @BindingAdapter("android:checked")
    public static void setText(MySwitch view, boolean b) {
        view.setChecked(b);
    }
}
