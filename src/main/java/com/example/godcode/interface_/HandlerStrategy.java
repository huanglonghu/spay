package com.example.godcode.interface_;

import android.graphics.Bitmap;

import okhttp3.MultipartBody;

public abstract class HandlerStrategy {

    public void onActivityResult(String text) {

    }

    public void onActivityResult(MultipartBody.Part filePart, Bitmap bitmap){};


}
