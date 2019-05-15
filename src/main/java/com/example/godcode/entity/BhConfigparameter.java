package com.example.godcode.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.godcode.BR;


public class BhConfigparameter extends BaseObservable {

    private boolean checkAll;

    public BhConfigparameter() {
    }


    @Bindable
    public void setCheckAll(boolean checkAll) {
        this.checkAll = checkAll;
    }

    public boolean isCheckAll() {
        return isCheckAll();
    }


}
