package com.example.godcode.ui.activity;

import android.support.v7.app.AppCompatActivity;

import com.example.godcode.ui.base.BaseFragment;


public abstract class BaseActivity extends AppCompatActivity{

    public abstract void notifyFragmentDataChange(BaseFragment fragment);
}
