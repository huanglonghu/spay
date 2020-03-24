package com.example.spay.ui.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;

public class MyDatePickerDialog extends DatePickerDialog{
    public MyDatePickerDialog(@NonNull Context context,  OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context,DatePickerDialog.THEME_HOLO_LIGHT, listener, year, month, dayOfMonth);
    }
}
