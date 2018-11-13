package com.example.godcode.ui.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class MyDatePickerDialog extends DatePickerDialog{
    public MyDatePickerDialog(@NonNull Context context, @Nullable OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context,DatePickerDialog.THEME_HOLO_LIGHT, listener, year, month, dayOfMonth);
    }
}
